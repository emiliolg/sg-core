
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.view.client.ui;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import tekgenesis.common.Predefined;
import tekgenesis.common.core.Option;
import tekgenesis.metadata.form.IndexedWidget;
import tekgenesis.metadata.form.widget.*;
import tekgenesis.view.client.Application;

import static tekgenesis.common.Predefined.cast;
import static tekgenesis.common.collections.Colls.listOf;
import static tekgenesis.common.core.Constants.NOT_FOUND;
import static tekgenesis.common.core.Constants.WIDGET_UI;
import static tekgenesis.common.core.Option.empty;
import static tekgenesis.common.core.Option.of;
import static tekgenesis.common.core.Option.option;
import static tekgenesis.metadata.form.widget.WidgetType.*;

/**
 * Utility class to find UI widgets in a model view.
 */
public class WidgetUIFinder {

    //~ Instance Fields ..............................................................................................................................

    private final ModelUI view;

    //~ Constructors .................................................................................................................................

    /** Creates the utility class associates with the given view. */
    public WidgetUIFinder(final ModelUI view) {
        this.view = view;
    }

    //~ Methods ......................................................................................................................................

    /**
     * Finds all matching UIs in the form. This includes searching inside tables.
     *
     * @param   predicate  the Predicate object
     *
     * @return  result List
     */
    @NotNull public <T extends WidgetUI> List<T> allByPredicate(@NotNull final Predicate<WidgetUI> predicate) {
        final List<T> result = new ArrayList<>();
        findDescendants(view, predicate, true, true, true, result);
        return result;
    }

    /**
     * Finds all matching UIs in a form by WidgetType.
     *
     * @param   types  List<WidgetType>
     *
     * @return  widgetUI List
     */
    @NotNull public <T extends WidgetUI> List<T> allByType(@NotNull final List<WidgetType> types) {
        final List<T> result = new ArrayList<>();
        findDescendants(view, typePredicate(types), true, true, true, result);
        return result;
    }

    /** Finds a buttonUI in the form by button type. */
    @NotNull public Option<ButtonUI> byButtonType(final ButtonType t) {
        return option(this.<ButtonUI>findDescendant(view, buttonTypePredicate(t), true, true));
    }

    /**
     * Finds a UI that's inside a table.
     *
     * @param   widget  the metaModel
     * @param   item    the item index in the row model
     *
     * @return  an options of the widgetUI: none if the item is no currently visible
     */
    @NotNull public Option<WidgetUI> byItemIndex(final Widget widget, final int item) {
        return find(view, widget, item);
    }

    /**
     * Finds a UI in the form.
     *
     * @param   widgetName  the name of the widget metaModel
     *
     * @return  the widgetUI
     */
    @NotNull public <T extends WidgetUI> T byName(final String widgetName) {
        return findOrFail(view, view.getUiModel().getElement(widgetName));
    }

    /**
     * Finds a UI that's inside a table.
     *
     * @param   widgetName    the name ot the widget metaModel
     * @param   sectionIndex  the row index in the view
     *
     * @return  the widgetUI
     */
    @NotNull public <T extends WidgetUI> T bySectionIndex(final String widgetName, final int sectionIndex) {
        return bySectionIndex(view.getUiModel().getElement(widgetName), sectionIndex);
    }

    /** Finds a toggleButtonUI in the form by toggle button type. */
    @NotNull public Option<ToggleButtonUI> byToggleButtonType(final ToggleButtonType t) {
        return option(this.<ToggleButtonUI>findDescendant(view, toggleButtonTypePredicate(t), true, true));
    }

    /**
     * Finds a UI in a form by WidgetType.
     *
     * @param   t  WidgetType
     *
     * @return  the widgetUI
     */
    @NotNull public <T extends WidgetUI> Option<T> byType(final WidgetType t) {
        return option(this.<T>findDescendant(view, typePredicate(listOf(t)), true, true));
    }

    /**
     * Finds a UI in the form.
     *
     * @param   widget  the metaModel
     *
     * @return  the widgetUI
     */
    @NotNull public <T extends WidgetUI> Option<T> byWidget(final Widget widget) {
        return find(view, widget);
    }

    /** Finds {@link WidgetUI ui} on given view by {@link IndexedWidget indexed widget}. */
    @NotNull public <T extends WidgetUI> Option<T> find(final IndexedWidget widget) {
        final Option<ModelUI> container = resolveIndexedContainer(widget, view);
        return container.flatMap(v -> find(v, widget.widget(), widget.item()));
    }

    /** Find {@link WidgetUI widget ui} by widget and item index. */
    @NotNull public <T extends WidgetUI> Option<T> find(@NotNull final Widget widget, @NotNull final Option<Integer> item) {
        return find(view, widget, item);
    }

    /**
     * Finds a UI that's inside a table.
     *
     * @param   widget  the metaModel
     * @param   item    the item index in the row model
     *
     * @return  an options of the widgetUI: none if the item is no currently visible
     */
    @NotNull public <T extends WidgetUI> Option<T> find(final Iterable<WidgetUI> parent, final Widget widget, final Option<Integer> item) {
        return item.isEmpty() ? find(parent, widget) : find(parent, widget, item.get());
    }

    /** Finds {@link WidgetUI ui} on given view by {@link IndexedWidget indexed widget}. */
    @NotNull public <T extends WidgetUI> T findOrFail(final IndexedWidget widget) {
        final Option<T> result = find(widget);
        return result.getOrFail(WIDGET_UI + widget.fqn() + NOT_FOUND);
    }

    public Option<WidgetDefUI> findWidgetDef(@NotNull IndexedWidget indexed) {
        if (!indexed.widget().isWidgetDef()) throw new IllegalArgumentException("Expected widget definition!");
        return cast(resolveIndexedContainer(indexed, view));
    }

    /**
     * Finds a UI in the form. This includes searching inside tables.
     *
     * @param   predicate  the Predicate object
     *
     * @return  the widgetUI
     */
    @NotNull <T extends WidgetUI> Option<T> byPredicate(final Predicate<WidgetUI> predicate) {
        return byPredicate(predicate, true);
    }

    @NotNull <T extends WidgetUI> Option<T> byPredicate(final Predicate<WidgetUI> predicate, final boolean insideGroups) {
        return option(this.<T>findDescendant(view, predicate, true, insideGroups));
    }

    private Predicate<WidgetUI> buttonTypePredicate(final ButtonType t) {
        return w -> w != null && w.getModel().getButtonType() == t;
    }

    /**
     * Finds a UI that's inside a table.
     *
     * @param   widget        the metaModel
     * @param   sectionIndex  the row index in the view
     *
     * @return  the widgetUI
     */
    @NotNull private <T extends WidgetUI> T bySectionIndex(final Widget widget, final int sectionIndex) {
        final MultipleUI multiple = cast(findOrFail(view, widget.getMultiple().get()));
        final Option<T>  result   = bySectionIndex(multiple, widget, sectionIndex);
        return result.getOrFail(WIDGET_UI + widget.getName() + NOT_FOUND);
    }

    @NotNull private <T extends WidgetUI> Option<T> bySectionIndex(MultipleUI multiple, Widget widget, int section) {
        if (multiple.getSectionsCount() <= section || section < 0) return empty();
        return of(findOrFail(multiple.getSection(section), widget));
    }

    @NotNull private <T extends WidgetUI> Option<T> find(final Iterable<WidgetUI> parent, final Widget widget) {
        if (widget.getWidgetType() == INTERNAL) return Option.empty();
        return option(findDescendant(parent, ui -> ui != null && widget == ui.getModel(), false, false));
    }

    @NotNull private <T extends WidgetUI> Option<T> find(@NotNull final Iterable<WidgetUI> parent, @NotNull final Widget widget, final int item) {
        final MultipleWidget multiple = widget.getMultiple().get();

        if (widget.getWidgetType() == INTERNAL || !(multiple.getWidgetType() == TABLE || multiple.getWidgetType() == SECTION)) return Option.empty();

        final MultipleUI ui = cast(findOrFail(parent, multiple));
        return ui.mapItemToSection(item).flatMap(value -> bySectionIndex(ui, widget, value));
    }

    @SuppressWarnings("IfStatementWithTooManyBranches")
    private <T extends WidgetUI> boolean find(@NotNull Iterable<WidgetUI> widgets, @NotNull Predicate<WidgetUI> condition, boolean includeGroups,
                                              boolean includeTables, boolean includeWidgets) {
        for (final WidgetUI ui : widgets) {
            boolean yield = false;

            if (condition.test(ui)) yield = true;
            else if (includeGroups && ui instanceof ContainerUI && !(ui instanceof WidgetDefUI))
                // search inside groups
                yield = find((ContainerUI) ui, condition, true, includeTables, includeWidgets);
            else if (includeWidgets && ui instanceof WidgetDefUI)
            // search inside groups
            yield = find((WidgetDefUI) ui, condition, includeGroups, includeTables, true);
            else if (includeTables && ui instanceof TableUI)
            // search inside tables
            yield = find(((TableUI) ui).getCells(), condition, includeGroups, true, includeWidgets);
            else if (ui instanceof InlineSubformUI) {
                // search inside inline subforms
                final String path        = ((InlineSubformUI) ui).getPath();
                final FormUI subformView = Application.getInstance().getSubformView(path);
                if (subformView != null)
                    yield = new WidgetUIFinder(subformView).find(subformView, condition, includeGroups, includeTables, includeWidgets);
            }
            else if (includeTables && ui instanceof SectionUI) {
                // search inside sections
                final SectionUI section = (SectionUI) ui;
                for (int i = 0; i < section.getSectionsCount() && !yield; i++)
                    yield = find(section.getSection(i), condition, includeGroups, true, includeWidgets);
            }

            if (yield) return true;
        }
        return false;
    }  // end method find

    @Nullable private <T extends WidgetUI> T findDescendant(final Iterable<WidgetUI> parent, final Predicate<WidgetUI> condition,
                                                            final boolean includeTables, final boolean includeWidgets) {
        return findDescendant(parent, condition, includeTables, true, includeWidgets);
    }

    @Nullable private <T extends WidgetUI> T findDescendant(final Iterable<WidgetUI> parent, final Predicate<WidgetUI> condition,
                                                            final boolean includeTables, final boolean includeGroups, final boolean includeWidgets) {
        final Object[] ref = new Object[1];

        find(parent,
            ui -> {
                if (condition.test(ui)) {
                    ref[0] = ui;
                    return true;
                }
                return false;
            },
            includeGroups,
            includeTables,
            includeWidgets);

        return cast(ref[0]);
    }

    private <T extends WidgetUI> void findDescendants(final Iterable<WidgetUI> parent, final Predicate<WidgetUI> condition,
                                                      final boolean includeGroups, final boolean includeTables, final boolean includeWidgets,
                                                      List<T> result) {
        find(parent, ui -> {
                if (condition.test(ui)) result.add(cast(ui));
                return false;
            },
            includeGroups, includeTables, includeWidgets);
    }

    private <T extends WidgetUI> T findOrFail(final Iterable<WidgetUI> parent, final Widget widget) {
        final Option<T> result = find(parent, widget);
        return result.getOrFail(WIDGET_UI + widget.getName() + NOT_FOUND);
    }

    private Option<ModelUI> resolveIndexedContainer(@NotNull final IndexedWidget indexed, @NotNull final ModelUI parent) {
        return indexed.foldLeft(of(parent), (previous, part) -> {
                final Widget widget = part.widget();
                //J-
                if (widget.isWidgetDef()) return previous.flatMap(container ->
                        Predefined.<Option<ModelUI>, Option<WidgetUI>>cast(find(container, widget, part.item())));
                //J+
                return previous;
            });
    }

    private Predicate<WidgetUI> toggleButtonTypePredicate(final ToggleButtonType t) {
        return w -> w != null && w.getModel().getToggleButtonType() == t;
    }

    private Predicate<WidgetUI> typePredicate(final List<WidgetType> types) {
        return w -> w != null && types.contains(w.getModel().getWidgetType());
    }
}  // end class WidgetUIFinder
