
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
import java.util.Iterator;
import java.util.List;
import java.util.function.Predicate;

import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.ComplexPanel;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlowPanel;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import tekgenesis.check.CheckMsg;
import tekgenesis.common.core.Option;
import tekgenesis.common.logging.Logger;
import tekgenesis.metadata.form.IndexedWidget;
import tekgenesis.metadata.form.Parent;
import tekgenesis.metadata.form.SourceWidget;
import tekgenesis.metadata.form.widget.ButtonType;
import tekgenesis.metadata.form.widget.Form;
import tekgenesis.metadata.form.widget.FormAction;
import tekgenesis.metadata.form.widget.MultipleWidget;
import tekgenesis.metadata.form.widget.Widget;
import tekgenesis.view.client.Application;
import tekgenesis.view.client.ui.base.ModalAlert;
import tekgenesis.view.client.ui.base.ModalGlass;

import static tekgenesis.common.Predefined.notEmpty;
import static tekgenesis.common.core.Constants.DEPRECATED;
import static tekgenesis.common.core.Option.empty;
import static tekgenesis.common.core.Option.some;
import static tekgenesis.metadata.form.IndexedWidget.createFromReference;
import static tekgenesis.metadata.form.widget.WidgetType.FOOTER;
import static tekgenesis.metadata.form.widget.WidgetType.HEADER;
import static tekgenesis.metadata.form.widget.WidgetType.SEARCH_BOX;
import static tekgenesis.view.client.ClientUiModelRetriever.getRetriever;
import static tekgenesis.view.client.DynamicFormBox.CustomActionHandler;
import static tekgenesis.view.client.exprs.ExpressionEvaluatorFactory.ValidationResult;

/**
 * Form UI widget. {@link ContainerUI}
 */
public class FormUI extends Composite implements ModelUI {

    //~ Instance Fields ..............................................................................................................................

    private final List<WidgetUI> children = new ArrayList<>();

    private final ComplexPanel div = new FlowPanel();

    private final WidgetUIFinder finder;
    private final Form           metadata;

    //~ Constructors .................................................................................................................................

    /** Creates Form UI widget. */
    public FormUI(final Form metadata, final boolean deprecated) {
        this.metadata = metadata;
        finder        = new WidgetUIFinder(this);

        initWidget(div);

        addStyleName(FORM_CLASS_NAME);

        getElement().setId(metadata.getName());
    }

    //~ Methods ......................................................................................................................................

    @Override public void addChild(final WidgetUI w) {
        children.add(w);
        addChildPanel(w);
    }

    /** Behavior to be executed before form unload. */
    public void beforeUnload() {
        ModalGlass.hide();
    }

    /** Find a descendants child UI widget. */
    @NotNull public WidgetUIFinder finder() {
        return finder;
    }

    /** Focus on the given element. */
    public void focus(final SourceWidget f) {
        final Option<IndexedWidget> reference = createFromReference(getRetriever(), metadata, f.getPath());
        reference.ifPresent(indexed -> finder().find(indexed).map(FormUI::canFocus).map(FormUI::focus));
    }

    /** Focus on the nth element of the form. */
    public void focus(final int index) {
        finder().byPredicate(new Predicate<WidgetUI>() {
                    int i = 0;

                    @Override public boolean test(final WidgetUI ui) {
                        return ui.acceptsFocus() && index == i++;
                    }
                }, false).map(FormUI::focus);
    }

    /** Focus on the first element of the form. */
    public void focusFirst() {
        focus(0);
    }

    /** Focus on the first element of the form that has a validation error. */
    public void focusFirstError(@NotNull final Option<ValidationResult.Error> e) {
        for (final ValidationResult.Error error : e) {
            if (error.getSubformPath().isPresent() && !error.getSubformPath().get().isEmpty()) {
                final FormUI ui = Application.getInstance().getSubformView(error.getSubformPath().get());
                ui.focusFirstError(some(error.plain()));
            }
            else {
                // find ui of first error
                final Widget           w   = error.getWidget();
                final Option<Integer>  row = error.getRow();
                final Option<WidgetUI> ui  = row.isEmpty() ? finder().byWidget(w) : finder().byItemIndex(w, row.get());

                // try to focus it
                final Option<WidgetUI> focused = ui.map(FormUI::canFocus).map(FormUI::focus);

                // fallback to showing a modal if the message it not visible
                if (focused.isEmpty() || (focused.get() instanceof FieldWidgetUI && !((FieldWidgetUI) focused.get()).isErrorMessageVisible())) {
                    String field = "<hr>Field: " + notEmpty(w.getLabel(), w.getName());
                    for (final Integer r : row) {
                        final MultipleWidget table = w.getMultiple().get();
                        field += "<br>Table: " + notEmpty(table.getLabel(), table.getName());
                        field += "<br>Row: " + r;
                    }

                    final CheckMsg msg = error.getMsg().getFirst().getOrFail("Msg list can't be empty");
                    ModalAlert.show(msg.getText(), field);
                }
            }
        }
    }

    /** Focus on the search box. */
    public void focusSearch() {
        finder().byType(SEARCH_BOX).map(FormUI::focus);
    }

    public boolean hasButtonOf(ButtonType buttonType) {
        return finder().byButtonType(buttonType).filter(WidgetUI::isEnabled).isPresent();
    }

    /** Returns true if there is a child widget that has an error message. */
    public boolean hasErrorMessage() {
        return finder().byPredicate(ERROR_PREDICATE).isPresent();
    }

    /** Hide Form footer. */
    public void hideFooter() {
        finder().byType(FOOTER).map(FormUI::removeWidget);
    }

    /** Hide Form header. */
    public void hideHeader() {
        finder().byType(HEADER).map(FormUI::removeWidget);
    }

    @Override public Iterator<WidgetUI> iterator() {
        return children.iterator();
    }

    @NotNull @Override public Option<Parent<ModelUI>> parent() {
        return empty();
    }

    /** Swap footer. */
    public void swapFooter(@NotNull List<FormAction> actions, @NotNull CustomActionHandler handler) {
        final FlowPanel footer = GroupUI.createGroupDiv(FOOTER);
        for (final FormAction action : actions) {
            final Button b = ButtonUI.createActionButton(action.getAction(), action.getLabel());
            b.addClickHandler(handler.handlerFor(action));
            footer.add(b);
        }

        finder().byType(FOOTER).map(FormUI::removeWidget);
        div.add(footer);
    }

    /** Sets this FormUI to deprecate state. */
    public void setDeprecated(boolean deprecated) {
        if (deprecated) addStyleName(DEPRECATED);
        else removeStyleName(DEPRECATED);
    }

    @Override public String getId() {
        return "";
    }

    @NotNull @Override public Form getUiModel() {
        return metadata;
    }

    private void addChildPanel(final WidgetUI w) {
        div.add(w);
    }

    //~ Methods ......................................................................................................................................

    @Nullable private static WidgetUI canFocus(@Nullable final WidgetUI acceptFocus) {
        assert acceptFocus != null;
        if (acceptFocus.acceptsFocus()) return acceptFocus;  // happy path: focus widget

        // try to select the tab if the widget is in another tab
        WidgetUI tab = acceptFocus;
        for (com.google.gwt.user.client.ui.Widget p = acceptFocus.getParent(); p != null; p = p.getParent()) {
            if (p instanceof TabGroupUI) {
                ((TabGroupUI) p).setActiveTab(tab);
                return acceptFocus;  // tab found: focus widget
            }
            else if (p instanceof WidgetUI) tab = (WidgetUI) p;
        }

        return null;  // do not accept focus
    }

    @NotNull private static WidgetUI focus(final WidgetUI ui) {
        ui.setFocus(true);
        return ui;
    }

    @NotNull private static WidgetUI removeWidget(final WidgetUI ui) {
        ui.removeFromParent();
        return ui;
    }

    //~ Static Fields ................................................................................................................................

    public static final String FORM_CLASS_NAME = "form form-horizontal";

    public static final Logger logger = Logger.getLogger(FormUI.class);

    private static final Predicate<WidgetUI> ERROR_PREDICATE = ui -> ui instanceof FieldWidgetUI && ((FieldWidgetUI) ui).hasErrorMessage();
}  // end class FormUI
