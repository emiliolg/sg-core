
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.view.client.exprs;

import java.util.function.Predicate;

import org.jetbrains.annotations.NotNull;

import tekgenesis.code.Evaluator;
import tekgenesis.common.core.Option;
import tekgenesis.expr.Expression;
import tekgenesis.metadata.form.exprs.ItemContext;
import tekgenesis.metadata.form.exprs.WidgetExpressionEvaluator;
import tekgenesis.metadata.form.model.FilterData;
import tekgenesis.metadata.form.model.FormModel;
import tekgenesis.metadata.form.model.Model;
import tekgenesis.metadata.form.model.MultipleChanges;
import tekgenesis.metadata.form.model.MultipleModel;
import tekgenesis.metadata.form.model.RowModel;
import tekgenesis.metadata.form.widget.MultipleWidget;
import tekgenesis.metadata.form.widget.Widget;
import tekgenesis.metadata.form.widget.WidgetType;
import tekgenesis.view.client.ui.BaseMultipleUI;
import tekgenesis.view.client.ui.SectionUI;
import tekgenesis.view.client.ui.TableUI;
import tekgenesis.view.client.ui.WidgetUI;
import tekgenesis.view.client.ui.WidgetUIFinder;

import static java.lang.Math.min;

import static tekgenesis.common.collections.Colls.first;
import static tekgenesis.common.core.Option.empty;
import static tekgenesis.metadata.form.exprs.Expressions.evaluateBoolean;
import static tekgenesis.metadata.form.filter.FormFiltersClient.accepts;
import static tekgenesis.metadata.form.filter.FormFiltersClient.hasZeroOptionsCount;
import static tekgenesis.metadata.form.filter.FormFiltersClient.updateCount;
import static tekgenesis.metadata.form.model.FilterData.NONE;
import static tekgenesis.type.permission.PredefinedPermission.CREATE;
import static tekgenesis.type.permission.PredefinedPermission.QUERY;
import static tekgenesis.type.permission.PredefinedPermission.UPDATE;
import static tekgenesis.view.client.ui.multiple.LensEvents.REFRESH;

/**
 * Base class that evaluates the expressions that change the view/ui.
 */
@SuppressWarnings("OverlyComplexClass")
class UiExpressionsEvaluator implements WidgetExpressionEvaluator {

    //~ Instance Fields ..............................................................................................................................

    @NotNull private final Evaluator      evaluator;
    @NotNull private final WidgetUIFinder finder;

    //~ Constructors .................................................................................................................................

    /** Creates a UiExpressionsEvaluator given a WidgetUIFinder. */
    UiExpressionsEvaluator(@NotNull final WidgetUIFinder finder) {
        this.finder = finder;
        evaluator   = new Evaluator();
    }

    //~ Methods ......................................................................................................................................

    @Override public void evaluateExpressions(@NotNull MultipleWidget widget, @NotNull Model model, @NotNull MultipleChanges indexes) {
        final MultipleModel multiple = model.getMultiple(widget);

        finder.byWidget(widget).ifPresent(widgetUi -> {
            final BaseMultipleUI ui = (BaseMultipleUI) widgetUi;

            if (indexes.hasChanges()) ui.react(REFRESH);

            if (ui instanceof TableUI && model instanceof FormModel) {
                final TableUI table = (TableUI) ui;
                table.setPage(multiple.getCurrentPage());
            }

            final int mSize         = multiple.size();
            final int sectionsCount = ui.getSectionsCount();
            for (int section = 0; section < min(mSize, sectionsCount); section++) {
                final int item = ui.mapSectionToItem(section);
                evaluateSectionExpressions(multiple, ui.getSection(section), item);
            }

            ui.updateModel(multiple);
        });

        // Multiple widget expressions evaluation.
        evaluateExpressions(widget, model, empty());
    }

    @Override public void evaluateExpressions(@NotNull final Widget w, @NotNull final Model m, @NotNull final Option<ItemContext> section) {
        final Option<WidgetUI> ui = section.isPresent() ? finder.byItemIndex(w, section.get().getItem()) : finder.byWidget(w);
        ui.ifPresent(widget -> {
            final UiExpressionsVisitor visitor = new UiExpressionsVisitor(evaluator, finder, m, false);
            evaluateUIExprs(widget, m, visitor);
        });
    }

    /** Dispatch filtering change after load and sync. */
    void dispatchFilteringChange(@NotNull Model model, @NotNull SectionUI filtersUI, @NotNull String filtering) {
        final MultipleModel results = model.getMultiple(filtering);
        final MultipleModel filters = model.getMultiple(filtersUI.getMultipleModel());
        if (!filters.isEmpty())  //
            first(filtersUI.getMultipleModel().getTableElements(), DYNAMIC_WIDGET).ifPresent(dynamic ->
                    dispatchFilteringChange(filtersUI, results, filters, dynamic, empty(), finder));
    }

    /** Evaluate the UI expressions for a given specified row. */
    void evaluateExpressions(@NotNull MultipleWidget widget, @NotNull Model model, final Iterable<WidgetUI> columns, final int row)
    {
        final MultipleModel multiple = model.getMultiple(widget);
        evaluateSectionExpressions(multiple, columns, row);
    }

    private void evaluateSectionExpressions(@NotNull final MultipleModel multiple, @NotNull final Iterable<WidgetUI> columns, int item) {
        final RowModel             model   = multiple.getRow(item);
        final UiExpressionsVisitor visitor = new UiExpressionsVisitor(evaluator, finder, model, true);
        for (final WidgetUI column : columns)
            evaluateUIExprs(column, model, visitor);
    }

    /** Evaluate the UI expressions. */
    private void evaluateUIExprs(@NotNull final WidgetUI ui, @NotNull final Model model, @NotNull final UiExpressionsVisitor visitor) {
        if (!hide(evaluator, model, ui)) {  // if hidden, skip all ui/dom modification, until we show it again
            visitor.visitChild(ui);
        }
    }

    //~ Methods ......................................................................................................................................

    static void dispatchFilteringChange(@NotNull SectionUI filtersUI, @NotNull MultipleModel results, @NotNull MultipleModel filters,
                                        @NotNull Widget dynamic, @NotNull Option<RowModel> current, @NotNull WidgetUIFinder finder) {
        // Toggle filtered rows.
        final BaseMultipleUI multipleUI = (BaseMultipleUI) finder.byWidget(results.getMultipleWidget()).get();

        for (final RowModel row : results) {
            final FilterData data = row.getFilterData();
            if (data != NONE) {
                final boolean accepted = accepts(data.getBits(), filters);
                data.setAccepted(accepted);
                multipleUI.toggleFilteredSection(row.getRowIndex(), accepted);
            }
        }

        multipleUI.doneFiltering();

        // Update filters count
        updateCount(filters, results, dynamic, current);

        // Update filters ui!
        int section = 0;
        for (final RowModel filter : filters) {
            FormDataHandler.modelToView(filter, filtersUI.getSectionCell(section, dynamic));
            filtersUI.toggleFilteredSection(section, !hasZeroOptionsCount(filter.getOptions(dynamic)));
            section++;
        }
    }

    /** Boolean type expression for hide value. */
    static boolean hide(Evaluator evaluator, Model model, final WidgetUI ui) {
        final boolean    hide;
        final Expression e = ui.getModel().getHideExpression();
        if (e == Expression.FALSE) hide = false;
        else {
            hide = evaluateBoolean(evaluator, model, e);
            ui.setVisible(!hide);
        }
        return hide;
    }  // end method hide

    static boolean isReadOnly(Model model, Widget widget) {
        final boolean update = model.isUpdate();

        // we don't allow updating the pk
        // (but only if it's outside the table, because the model.isUpdate() is from the main form, not the rows)
        boolean readOnly = update && widget.belongsToPrimaryKey() && widget.getMultiple().isEmpty();
        if (!readOnly) readOnly = !model.hasPermission(QUERY.getName()) && !model.hasPermission(update ? UPDATE.getName() : CREATE.getName());
        return readOnly;
    }

    //~ Static Fields ................................................................................................................................

    private static final Predicate<Widget> DYNAMIC_WIDGET = widget -> widget != null && widget.getWidgetType() == WidgetType.DYNAMIC;
}  // end class UiExpressionsEvaluator
