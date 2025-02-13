
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.view.client;

import java.util.List;
import java.util.Map;

import com.google.gwt.dom.client.Document;
import com.google.gwt.dom.client.Element;
import com.google.gwt.user.client.ui.RootPanel;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import tekgenesis.check.CheckMsg;
import tekgenesis.common.core.Option;
import tekgenesis.metadata.form.IndexedWidget;
import tekgenesis.metadata.form.model.FormConstants;
import tekgenesis.metadata.form.model.FormModel;
import tekgenesis.metadata.form.widget.Form;
import tekgenesis.metadata.form.widget.UiModel;
import tekgenesis.view.client.controller.FormController;
import tekgenesis.view.client.ui.*;

import static com.google.gwt.event.dom.client.DomEvent.fireNativeEvent;

import static tekgenesis.common.Predefined.cast;
import static tekgenesis.common.core.Option.empty;
import static tekgenesis.common.core.Option.some;
import static tekgenesis.metadata.form.IndexedWidget.createFromReference;
import static tekgenesis.metadata.form.SourceWidget.NONE;
import static tekgenesis.view.client.ClientUiModelRetriever.getRetriever;

/**
 * Wraps the {@link FormController} to test it.
 */
@SuppressWarnings("WeakerAccess")
public class FormTester {

    //~ Instance Fields ..............................................................................................................................

    private final Option<FormBox> box;
    private final FormController  formController;

    //~ Constructors .................................................................................................................................

    FormTester(final FormBox box) {
        this.box       = some(box);
        formController = box.getCurrent();
    }

    private FormTester(final FormController formController) {
        box                 = Option.empty();
        this.formController = formController;
    }

    //~ Methods ......................................................................................................................................

    /** Emulates firing the given key code. */
    public static void fireKeyEvent(int keyCode) {
        fireNativeEvent(Document.get().createKeyDownEvent(false, false, false, false, keyCode), RootPanel.get());
    }

    @NotNull private static IndexedWidget createIndexed(@NotNull String path, @NotNull UiModel model) {
        final Option<IndexedWidget> widget = createFromReference(getRetriever(), model, path);
        return widget.orElseThrow(() -> new IllegalArgumentException("Invalid widget path '" + path + "'"));
    }

    //~ Methods ......................................................................................................................................

    /** Cancels the form. */
    public void cancel() {
        formController.cancel(NONE);
    }

    /** Click a button or link. */
    public void click(final String widgetId) {
        finder().byName(widgetId).click();
    }

    /** Click a button or link in a specified row. */
    public void click(final String widgetId, int rowIndex) {
        finder().bySectionIndex(widgetId, rowIndex).click();
    }

    /** Deletes the form. */
    public void delete() {
        formController.delete(NONE);
    }

    /** Finds a CheckBox. */
    public CheckGroupTester getCheckGroup(final String widgetId) {
        return new CheckGroupTester(finder().byName(widgetId));
    }

    /** Allow access to model for testing. */
    public FormModel getFormModel() {
        return formController.getModel();
    }

    /** Finds a Tab. */
    public GroupTester getGroup(final String widgetId) {
        return new GroupTester(finder().byName(widgetId));
    }

    /** Finds a section. */
    public SectionTester getSection(final String sectionId) {
        final SectionUI section = finder().byName(sectionId);
        return new SectionTester(section);
    }

    /** Returns form tester for the given subform ui. */
    public SubformTester getSubformTester(AnchoredSubformUI ui) {
        return new SubformTester((DynamicFormBox) box.get(), ui);
    }

    /** Returns form tester for the given subform ui. */
    public SubformTester getSubformTester(InlineSubformUI ui) {
        return new SubformTester(ui);
    }

    /** Finds a suggest box. */
    public SuggestBoxTester getSuggestBox(String widgetId) {
        return new SuggestBoxTester(finder().byName(widgetId));
    }

    /** Finds a Tab. */
    public TabTester getTab(final String widgetId) {
        return new TabTester(finder().byName(widgetId));
    }

    /** Finds a table. */
    public TableTester getTable(final String tableId) {
        final TableUI table = finder().byName(tableId);
        return new TableTester(table);
    }

    /** Finds a TagsComboBox. */
    public TagsComboTester getTagsComboBox(final String widgetId) {
        return new TagsComboTester(finder().byName(widgetId));
    }

    /** Finds a TagsComboBox. */
    public TagsSuggestTester getTagsSuggest(final String widgetId) {
        return new TagsSuggestTester(finder().byName(widgetId));
    }

    /** Returns true if the form has validations errors. */
    public boolean hasError() {
        return formController.getView().hasErrorMessage();
    }

    /** Returns true if formBox has error message. */
    public boolean hasErrorMessage() {
        return Application.messages().hasError(box.get());
    }

    public boolean isDetailShowing() {
        return box.map(b -> b instanceof DynamicFormBox && ((DynamicFormBox) b).isDetailShowing()).orElse(false);
    }

    /** Returns true if the associated box exists and is showing a swipe popup. */
    public boolean isSwipeShowing() {
        return box.map(b -> b instanceof DynamicFormBox && ((DynamicFormBox) b).isSwipeShowing()).orElse(false);
    }

    /** Try to save the form. Returns true if validations passed ant the form is saved. */

    @SuppressWarnings("UnusedReturnValue")  // it will be used eventually.
    public boolean save() {
        return formController.save(NONE);
    }

    /** Return button tester on given path. */
    ButtonTester button(@NotNull final String path) {
        final IndexedWidget indexed = createIndexed(path, getUiModel());
        return new ButtonTester(finder().findOrFail(indexed));
    }

    /** Return field tester on given path. */
    FieldTester field(@NotNull final String path) {
        final IndexedWidget indexed = createIndexed(path, getUiModel());
        return new FieldTester(finder().findOrFail(indexed));
    }

    /** Finds a field. */
    FieldTester getField(final String widgetId) {
        return new FieldTester(finder().byName(widgetId));
    }

    /** Finds a field in a row. */
    FieldTester getField(final String widgetId, final int rowIndex) {
        return new FieldTester(finder().bySectionIndex(widgetId, rowIndex));
    }

    /** Return table tester on given path. */
    TableTester table(@NotNull final String path) {
        final IndexedWidget indexed = createIndexed(path, getUiModel());
        return new TableTester(finder().findOrFail(indexed));
    }

    /** Return widget definition tester on given path. */
    WidgetDefTester widget(@NotNull final String path) {
        final IndexedWidget indexed = createIndexed(path, getUiModel());
        return new WidgetDefTester(finder().findWidgetDef(indexed).getOrFail("WidgetDef not found!"));
    }

    private WidgetUIFinder finder() {
        return formController.getView().finder();
    }

    @NotNull private Form getUiModel() {
        return formController.getView().getUiModel();
    }

    //~ Inner Classes ................................................................................................................................

    /**
     * Wraps a {@link ButtonUI button} field to test it.
     */
    public static class ButtonTester extends FieldTester {
        private ButtonTester(WidgetUI ui) {
            super(ui);
        }

        /** Is button enabled in the ui. */
        public boolean isButtonEnabled() {
            if (ui instanceof ButtonUI) return ((ButtonUI) ui).isButtonEnabled();
            if (ui instanceof ToggleButtonUI) return ((ToggleButtonUI) ui).isButtonEnabled();
            throw new IllegalStateException("Attempting to test as button a: " + ui);
        }

        public String text() {
            if (ui instanceof ButtonUI) return ((ButtonUI) ui).getText();
            if (ui instanceof ToggleButtonUI) return ((ToggleButtonUI) ui).getText();
            throw new IllegalStateException("Attempting to test as button a: " + ui);
        }
    }

    /**
     * Wraps a CheckBoxUI field to test it.
     */
    public static class CheckGroupTester extends FieldTester {
        private CheckGroupTester(final CheckBoxGroupUI ui) {
            super(ui);
        }

        /** Returns the widget values. */
        public Iterable<?> getValues() {
            return asCheckGroup().getValues();
        }

        private CheckBoxGroupUI asCheckGroup() {
            return (CheckBoxGroupUI) asBase();
        }
    }

    /**
     * Wraps a WidgetUI field to test it.
     */
    static class FieldTester {
        final WidgetUI ui;

        private FieldTester(final WidgetUI ui) {
            this.ui = ui;
        }

        /** Clicks the widget. */
        public void click() {
            ui.click();
        }

        /** Force ui focus. */
        public void focus() {
            ui.setFocus(true);
        }

        /** Returns the value of the array widget. */
        @NotNull public Iterable<?> getArrayValue() {
            if (!(ui instanceof HasArrayValueUI))
                throw new IllegalStateException("Attempting to obtain an array value from a non-array valued widget.");
            return ((HasArrayValueUI) ui).getValues();
        }

        public Option<String> getLabel() {
            final Option<Element> map = ui.getLabelElem();
            return map.isPresent() ? some(map.get().toString()) : empty();
        }

        /** Returns validation message. */
        public CheckMsg getMsg() {
            return asBase().getCurrentMessage();
        }

        /** Returns the value of the scalar widget. */
        @Nullable public Object getScalarValue() {
            if (!(ui instanceof HasScalarValueUI)) throw new IllegalStateException("Attempting to obtain an scala value from a non-scalar widget.");
            return ((HasScalarValueUI) ui).getValue();
        }

        /**
         * Should use instead {@link #getScalarValue() scalar} and {@link #getArrayValue() array}
         * access!!
         */
        @Nullable public Object getValue() {
            if (!(ui instanceof HasValueUI)) throw notHasValue();

            if (ui instanceof HasScalarValueUI) return ((HasScalarValueUI) ui).getValue();
            else return ((HasArrayValueUI) ui).getValues();
        }

        /** Returns true if the widget has validations errors. */
        public boolean hasError() {
            return asBase().hasErrorMessage();
        }

        /** Returns if the widget is disabled. */
        public boolean isDisabled() {
            return ui.isDisabled();
        }

        /** Returns if the widget is readOnly. */
        public boolean isReadOnly() {
            return ui.isReadOnly();
        }

        /** Returns if the widget is visible. */
        public boolean isVisible() {
            return ui.isVisible();
        }

        /**
         * Sets the value of the widget. Should be divided on set scalar and set array values!!!!
         */
        public void setValue(final Object value) {
            if (!(ui instanceof HasValueUI)) throw notHasValue();

            if (ui instanceof HasScalarValueUI) ((HasScalarValueUI) ui).setValue(value, true);
            else {
                final Iterable<Object> cast = cast(value);
                ((HasArrayValueUI) ui).setValues(cast, true);
            }
        }  // end method setValue

        FieldWidgetUI asBase() {
            if (!(ui instanceof FieldWidgetUI)) throw new IllegalArgumentException("'" + ui.getId() + "' widget can't be cast to base!");
            return cast(ui);
        }

        InlineSubformUI asInlineSubformUI() {
            if (!(ui instanceof InlineSubformUI)) throw new IllegalArgumentException("'" + ui.getId() + "' widget isn't a subform!");
            return cast(ui);
        }

        AnchoredSubformUI asSubformUI() {
            if (!(ui instanceof AnchoredSubformUI)) throw new IllegalArgumentException("'" + ui.getId() + "' widget isn't a subform!");
            return cast(ui);
        }

        Map<Object, String> getOptions() {
            if (!(ui instanceof HasOptionsUI)) throw new IllegalStateException("'" + ui.getId() + "' is not an option component!");

            return ((HasOptionsUI) ui).getOptions();
        }

        boolean hasNullOption() {
            if (!(ui instanceof HasOptionsUI)) throw new IllegalStateException("'" + ui.getId() + "' is not an option component!");

            return ((HasOptionsUI) ui).hasNullOption();
        }

        private IllegalArgumentException notHasValue() {
            return new IllegalArgumentException("'" + ui.getId() + "' widget can't have a value!");
        }
    }  // end class FieldTester

    /**
     * Wraps a GroupUI field to test it.
     */
    public static class GroupTester {
        private final GroupUI ui;

        private GroupTester(final GroupUI ui) {
            this.ui = ui;
        }

        /** Returns if the widget is visible. */
        public boolean isVisible() {
            return ui.getStyleName().contains(FormConstants.ACTIVE_STYLE);
        }
    }

    /**
     * Wraps a SectionUI to test it.
     */
    public static class SectionTester {
        private final SectionUI section;

        private SectionTester(final SectionUI section) {
            this.section = section;
        }

        /** Returns the items count (visible+invisible rows). */
        public int size() {
            return section.getSectionsCount();
        }
    }

    public static class SubformTester {
        private final FormTester formTester;

        /** Creates a Subform Tester. */
        public SubformTester(InlineSubformUI ui) {
            Application.getInstance().getSubformView(ui.getPath()).focusFirst();
            // Awful hack to set as current the subform box.
            formTester = new FormTester(Application.getInstance().getActiveOrMain());
        }

        /** Creates a Subform Tester. */
        public SubformTester(DynamicFormBox box, AnchoredSubformUI ui) {
            formTester = new FormTester(box.findSubformController(ui));
        }

        /** Returns Subform FormTester. */
        public FormTester getFormTester() {
            return formTester;
        }
    }

    public static class SuggestBoxTester extends FieldTester {
        private SuggestBoxTester(SuggestBoxUI ui) {
            super(ui);
        }

        public String getSuggestLabel() {
            return asSuggestBox().getLabel();
        }

        /** Set text to widget, query oracle and select option from suggested results. */
        public void suggest(String text, final int option) {
            asSuggestBox().suggest(text, option, null);
        }

        /** Set text to widget, query oracle and select option from suggested results. */
        public void suggest(String text, final int option, final String expectedText) {
            asSuggestBox().suggest(text, option, expectedText);
        }

        private SuggestBoxUI asSuggestBox() {
            return (SuggestBoxUI) asBase();
        }
    }

    /**
     * Wraps a TabGroupUI field to test it.
     */
    public static class TabTester {
        private final TabGroupUI ui;

        private TabTester(final TabGroupUI ui) {
            this.ui = ui;
        }

        /** Execute tab click. */
        public void clickTab(final String tabName) {
            ui.clickTab(tabName);
        }

        /** Returns the label of tab number i, empty if doesn't exist. */
        public String getLabel(int i) {
            return ui.getTabLabel(i);
        }

        /** Returns if the widget is disabled. */
        public boolean isDisabled() {
            return ui.isDisabled();
        }

        /** Returns if the widget is visible. */
        public boolean isVisible() {
            return ui.isVisible();
        }
    }

    /**
     * Wraps a TagsComboBoxUI field to test it.
     */
    public static class TagsComboTester extends FieldTester {
        private TagsComboTester(final TagsComboBoxUI ui) {
            super(ui);
        }

        /** Returns the widget values. */
        public Iterable<?> getValues() {
            return asTagsComboBox().getValues();
        }

        private TagsComboBoxUI asTagsComboBox() {
            return (TagsComboBoxUI) asBase();
        }
    }

    /**
     * Wraps a TagsSuggestTester field to test it.
     */
    public static class TagsSuggestTester extends FieldTester {
        private TagsSuggestTester(final TagsSuggestBoxUI ui) {
            super(ui);
        }

        /** Returns the widget values. */
        public Iterable<?> getValues() {
            return asTagsSuggestBoxUI().getValues();
        }

        List<String> getTagsText() {
            return asTagsSuggestBoxUI().getTagsText();
        }

        private TagsSuggestBoxUI asTagsSuggestBoxUI() {
            return (TagsSuggestBoxUI) asBase();
        }
    }

    public static class WidgetDefTester {
        private final WidgetUIFinder finder;
        private final WidgetDefUI    widget;

        private WidgetDefTester(WidgetDefUI widget) {
            this.widget = widget;
            finder      = widget.finder();
        }

        /** Return field tester on given path. */
        FieldTester field(@NotNull final String path) {
            final IndexedWidget indexed = createIndexed(path, widget.getUiModel());
            return new FieldTester(finder.findOrFail(indexed));
        }

        /** Return table tester on given path. */
        TableTester table(@NotNull final String path) {
            final IndexedWidget indexed = createIndexed(path, widget.getUiModel());
            return new TableTester(finder.findOrFail(indexed));
        }

        /** Return widget definition tester on given path. */
        WidgetDefTester widget(@NotNull final String path) {
            final IndexedWidget indexed = createIndexed(path, widget.getUiModel());
            return new WidgetDefTester(finder.findOrFail(indexed));
        }
    }
}  // end class FormTester
