package tekgenesis.showcase;

import tekgenesis.form.Action;
import tekgenesis.form.Actions;
import tekgenesis.form.exception.FormCannotBePopulatedException;
import tekgenesis.metadata.form.widget.FormFieldRef;
import tekgenesis.form.FormInstance;
import tekgenesis.form.extension.FormListener;
import tekgenesis.form.extension.FormListenerMap;
import tekgenesis.form.extension.FormListenerType;
import tekgenesis.form.FormRowInstance;
import tekgenesis.form.FormTable;
import tekgenesis.showcase.DisplayShowcase.ItemsRow;
import tekgenesis.model.KeyMap;
import tekgenesis.common.logging.LoggableInstance;
import tekgenesis.common.logging.Logger;
import tekgenesis.form.Message;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import tekgenesis.form.UiModelAccessor;
import tekgenesis.form.configuration.WidgetConfiguration;

/** 
 * Generated base class for form: DisplayShowcase.
 * Don't modify this as this is an auto generated class that's gets generated
 * every time the meta model file is modified. Use subclass instead.
 */
@SuppressWarnings({"DuplicateStringLiteralInspection", "WeakerAccess", "FieldMayBeFinal", "FieldCanBeLocal", "ConstantConditions", "ClassWithTooManyMethods", "ClassWithTooManyFields", "MagicNumber", "MethodOverridesStaticMethodOfSuperclass", "ParameterHidesMemberVariable", "FieldNameHidesFieldInSuperclass", "OverlyComplexClass", "RedundantCast", "UnusedReturnValue", "LocalVariableHidesMemberVariable", "UnusedParameters", "EmptyMethod"})
public abstract class DisplayShowcaseBase
    extends FormInstance<Void>
    implements LoggableInstance
{

    //~ Fields ...................................................................................................................

    @SuppressWarnings({"InstanceVariableMayNotBeInitialized", "UnusedDeclaration", "NullableProblems"}) @NotNull private UiModelAccessor f;

    //~ Methods ..................................................................................................................

    /** Invoked when populating a form instance */
    @NotNull public final Void populate() {
        throw new FormCannotBePopulatedException("tekgenesis.showcase.DisplayShowcase");
    }

    /** Associates a new message to the specified field and returns it for any further configuration. */
    @NotNull public Message message(@NotNull Field field, @NotNull String msg) { return f.msg(field, msg); }

    /** Returns true if the field value is not null. */
    public boolean isDefined(@NotNull Field... fields) { return f.defined(fields); }

    /** Resets the given fields. */
    protected void reset(@NotNull Field... fields) { f.reset(fields); }

    /** Focuses given field. */
    protected void focus(@NotNull Field field) { f.focus(field); }

    /** Returns the label of the given field. */
    @NotNull protected String label(@NotNull Field field) { return f.label(field); }

    /** Returns a typed configuration for a given field. */
    @NotNull protected <T extends WidgetConfiguration> T configuration(@NotNull Field field) { return f.config(field); }

    /** Returns the value of the check_box(hidePanel). */
    public boolean isHidePanel() { return f.get(Field.HIDE_PANEL, Boolean.class); }

    /** Sets the value of the check_box(hidePanel). */
    @NotNull public DisplayShowcase setHidePanel(boolean hidePanel) {
        f.set(Field.HIDE_PANEL, hidePanel);
        return (DisplayShowcase) this;
    }

    /** Returns the value of the combo_box(displayCombo). */
    @NotNull public DisplayOptions getDisplayCombo() {
        return DisplayOptions.valueOf(f.get(Field.DISPLAY_COMBO, String.class));
    }

    /** Sets the value of the combo_box(displayCombo). */
    @NotNull public DisplayShowcase setDisplayCombo(@NotNull DisplayOptions displayCombo) {
        f.set(Field.DISPLAY_COMBO, displayCombo);
        return (DisplayShowcase) this;
    }

    /** Sets the options of the combo_box(displayCombo). */
    public void setDisplayComboOptions(@NotNull Iterable<DisplayOptions> items) { f.opts(Field.DISPLAY_COMBO, items); }

    /** Sets the options of the combo_box(displayCombo) with the given KeyMap. */
    public void setDisplayComboOptions(@NotNull KeyMap items) { f.opts(Field.DISPLAY_COMBO, items); }

    /** Returns the value of the text_field(text1). */
    @NotNull public String getText1() { return f.get(Field.TEXT1, String.class); }

    /** Sets the value of the text_field(text1). */
    @NotNull public DisplayShowcase setText1(@NotNull String text1) {
        f.set(Field.TEXT1, text1);
        return (DisplayShowcase) this;
    }

    /** Returns the value of the text_field(text2). */
    @NotNull public String getText2() { return f.get(Field.TEXT2, String.class); }

    /** Sets the value of the text_field(text2). */
    @NotNull public DisplayShowcase setText2(@NotNull String text2) {
        f.set(Field.TEXT2, text2);
        return (DisplayShowcase) this;
    }

    /** Invoked when button(simpleSync) is clicked */
    @NotNull public abstract Action simpleSync();

    /** Returns the value of the check_box(disableCheck). */
    public boolean isDisableCheck() { return f.get(Field.DISABLE_CHECK, Boolean.class); }

    /** Sets the value of the check_box(disableCheck). */
    @NotNull public DisplayShowcase setDisableCheck(boolean disableCheck) {
        f.set(Field.DISABLE_CHECK, disableCheck);
        return (DisplayShowcase) this;
    }

    /** Returns the value of the check_box(hideCheck). */
    public boolean isHideCheck() { return f.get(Field.HIDE_CHECK, Boolean.class); }

    /** Sets the value of the check_box(hideCheck). */
    @NotNull public DisplayShowcase setHideCheck(boolean hideCheck) {
        f.set(Field.HIDE_CHECK, hideCheck);
        return (DisplayShowcase) this;
    }

    /** Returns the value of the text_field(input). */
    @NotNull public String getInput() { return f.get(Field.INPUT, String.class); }

    /** Sets the value of the text_field(input). */
    @NotNull public DisplayShowcase setInput(@NotNull String input) {
        f.set(Field.INPUT, input);
        return (DisplayShowcase) this;
    }

    /** Returns the value of the combo_box(combo). */
    @NotNull public Options getCombo() { return Options.valueOf(f.get(Field.COMBO, String.class)); }

    /** Sets the value of the combo_box(combo). */
    @NotNull public DisplayShowcase setCombo(@NotNull Options combo) {
        f.set(Field.COMBO, combo);
        return (DisplayShowcase) this;
    }

    /** Sets the options of the combo_box(combo). */
    public void setComboOptions(@NotNull Iterable<Options> items) { f.opts(Field.COMBO, items); }

    /** Sets the options of the combo_box(combo) with the given KeyMap. */
    public void setComboOptions(@NotNull KeyMap items) { f.opts(Field.COMBO, items); }

    /** Returns the value of the radio_group(radiogroup). */
    @NotNull public Options getRadiogroup() {
        return Options.valueOf(f.get(Field.RADIOGROUP, String.class));
    }

    /** Sets the value of the radio_group(radiogroup). */
    @NotNull public DisplayShowcase setRadiogroup(@NotNull Options radiogroup) {
        f.set(Field.RADIOGROUP, radiogroup);
        return (DisplayShowcase) this;
    }

    /** Sets the options of the radio_group(radiogroup). */
    public void setRadiogroupOptions(@NotNull Iterable<Options> items) { f.opts(Field.RADIOGROUP, items); }

    /** Sets the options of the radio_group(radiogroup) with the given KeyMap. */
    public void setRadiogroupOptions(@NotNull KeyMap items) { f.opts(Field.RADIOGROUP, items); }

    /** Invoked when button(button) is clicked */
    @NotNull public abstract Action addRow();

    /** Returns the value of the check_box(checkbox). */
    public boolean isCheckbox() { return f.get(Field.CHECKBOX, Boolean.class); }

    /** Sets the value of the check_box(checkbox). */
    @NotNull public DisplayShowcase setCheckbox(boolean checkbox) {
        f.set(Field.CHECKBOX, checkbox);
        return (DisplayShowcase) this;
    }

    /** Returns a {@link FormTable<ItemsRow>} instance to handle Items manipulation */
    @NotNull public final FormTable<ItemsRow> getItems() { return table(Field.ITEMS, ItemsRow.class); }

    /** Method to allow table row class override */
    @NotNull final <T> FormTable<T> table(@NotNull FormFieldRef field, @NotNull Class<T> rowClass) { return f.table(field, rowClass, this); }

    /** Register a form Listener */
    public static void addListener(@NotNull FormListenerType listenerType, @NotNull FormListener<DisplayShowcase> listener) { listeners.addListener(listenerType, listener); }

    /** Remove a form Listener */
    public static void removeListener(@NotNull FormListenerType listenerType, @NotNull FormListener<DisplayShowcase> listener) { listeners.removeListener(listenerType, listener); }

    @Override @NotNull public Logger logger() { return logger; }

    /** Returns utility methods to deal with {@link Action actions}. */
    @NotNull protected final Actions actions() { return actions; }

    //~ Fields ...................................................................................................................

    @NotNull private static FormListenerMap<DisplayShowcase> listeners = new FormListenerMap<>();
    @NotNull private static final Logger logger = Logger.getLogger(DisplayShowcase.class);

    //~ Inner Classes ............................................................................................................

    public abstract class ItemsRowBase
        implements FormRowInstance<ItemsRow>
    {

        //~ Fields ...................................................................................................................

        @SuppressWarnings({"InstanceVariableMayNotBeInitialized", "UnusedDeclaration", "NullableProblems"}) @NotNull private UiModelAccessor f;

        //~ Methods ..................................................................................................................

        /** Returns the value of the check_box(checkItem). */
        public boolean isCheckItem() { return f.get(Field.CHECK_ITEM, Boolean.class); }

        /** Sets the value of the check_box(checkItem). */
        @NotNull public ItemsRow setCheckItem(boolean checkItem) {
            f.set(Field.CHECK_ITEM, checkItem);
            return (ItemsRow) this;
        }

        /** Returns the value of the text_field(numberItem). */
        public int getNumberItem() { return f.get(Field.NUMBER_ITEM, Integer.class); }

        /** Sets the value of the text_field(numberItem). */
        @NotNull public ItemsRow setNumberItem(int numberItem) {
            f.set(Field.NUMBER_ITEM, numberItem);
            return (ItemsRow) this;
        }

        /** Associates a new message to the specified field and returns it for any further configuration. */
        @NotNull public Message message(@NotNull Field field, @NotNull String msg) { return f.msg(field, msg); }

        /** Returns true if the field value is not null. */
        public boolean isDefined(@NotNull Field... fields) { return f.defined(fields); }

        /** Resets the given fields. */
        protected void reset(@NotNull Field... fields) { f.reset(fields); }

        /** Focuses given field. */
        protected void focus(@NotNull Field field) { f.focus(field); }

        /** Return associated table. */
        @NotNull public final FormTable<ItemsRow> table() { return getItems(); }

        /** Remove row from table. */
        @NotNull public Action remove() {
            table().remove((ItemsRow) this);
            return actions().getDefault();
        }

        /** Returns a typed configuration for a given field. */
        @NotNull protected <T extends WidgetConfiguration> T configuration(@NotNull Field field) { return f.config(field); }

    }

    @SuppressWarnings("UnnecessarySemicolon")
    public enum Field
        implements tekgenesis.metadata.form.widget.FormFieldRef
    {
        $H0("$H0"),
        $M1("$M1"),
        $L2("$L2"),
        HIDE_PANEL("hidePanel"),
        HIDDEN_PANEL("hiddenPanel"),
        DISPLAY_COMBO("displayCombo"),
        $V3("$V3"),
        TEXT1("text1"),
        TEXT2("text2"),
        SIMPLE_SYNC("simpleSync"),
        $V4("$V4"),
        $H5("$H5"),
        DISABLE_CHECK("disableCheck"),
        HIDE_CHECK("hideCheck"),
        INPUT("input"),
        COMBO("combo"),
        RADIOGROUP("radiogroup"),
        BUTTON("button"),
        CHECKBOX("checkbox"),
        ITEMS("items"),
        CHECK_ITEM("checkItem"),
        NUMBER_ITEM("numberItem");

        //~ Fields ...................................................................................................................

        @Nullable private final String id;

        //~ Constructors .............................................................................................................

        Field(@Nullable final String id) { this.id = id; }

        //~ Methods ..................................................................................................................

        @NotNull public String id() { return id; }

    }
}
