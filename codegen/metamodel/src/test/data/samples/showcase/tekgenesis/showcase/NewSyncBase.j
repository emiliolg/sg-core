package tekgenesis.showcase;

import tekgenesis.form.Action;
import tekgenesis.form.Actions;
import tekgenesis.common.core.DateOnly;
import tekgenesis.form.exception.FormCannotBePopulatedException;
import tekgenesis.metadata.form.widget.FormFieldRef;
import tekgenesis.form.FormInstance;
import tekgenesis.form.extension.FormListener;
import tekgenesis.form.extension.FormListenerMap;
import tekgenesis.form.extension.FormListenerType;
import tekgenesis.form.FormRowInstance;
import tekgenesis.form.FormTable;
import tekgenesis.model.KeyMap;
import tekgenesis.common.logging.LoggableInstance;
import tekgenesis.common.logging.Logger;
import tekgenesis.form.Message;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import tekgenesis.showcase.NewSync.TableRow;
import tekgenesis.form.UiModelAccessor;
import tekgenesis.form.configuration.WidgetConfiguration;

/** 
 * Generated base class for form: NewSync.
 * Don't modify this as this is an auto generated class that's gets generated
 * every time the meta model file is modified. Use subclass instead.
 */
@SuppressWarnings({"DuplicateStringLiteralInspection", "WeakerAccess", "FieldMayBeFinal", "FieldCanBeLocal", "ConstantConditions", "ClassWithTooManyMethods", "ClassWithTooManyFields", "MagicNumber", "MethodOverridesStaticMethodOfSuperclass", "ParameterHidesMemberVariable", "FieldNameHidesFieldInSuperclass", "OverlyComplexClass", "RedundantCast", "UnusedReturnValue", "LocalVariableHidesMemberVariable", "UnusedParameters", "EmptyMethod"})
public abstract class NewSyncBase
    extends FormInstance<Void>
    implements LoggableInstance
{

    //~ Fields ...................................................................................................................

    @SuppressWarnings({"InstanceVariableMayNotBeInitialized", "UnusedDeclaration", "NullableProblems"}) @NotNull private UiModelAccessor f;

    //~ Methods ..................................................................................................................

    /** Invoked when populating a form instance */
    @NotNull public final Void populate() {
        throw new FormCannotBePopulatedException("tekgenesis.showcase.NewSync");
    }

    /** Invoked when the form is loaded */
    public abstract void load();

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

    /** Returns the value of the text_field(test). */
    @NotNull public String getTest() { return f.get(Field.TEST, String.class); }

    /** Sets the value of the text_field(test). */
    @NotNull public NewSync setTest(@NotNull String test) {
        f.set(Field.TEST, test);
        return (NewSync) this;
    }

    /** Returns a {@link FormTable<TableRow>} instance to handle Table manipulation */
    @NotNull public final FormTable<TableRow> getTable() { return table(Field.TABLE, TableRow.class); }

    /** Method to allow table row class override */
    @NotNull final <T> FormTable<T> table(@NotNull FormFieldRef field, @NotNull Class<T> rowClass) { return f.table(field, rowClass, this); }

    /** Sets the options of the combo_box(comboCol). */
    public void setComboColOptions(@NotNull Iterable<String> items) { f.opts(Field.COMBO_COL, items); }

    /** Sets the options of the combo_box(comboCol) with the given KeyMap. */
    public void setComboColOptions(@NotNull KeyMap items) { f.opts(Field.COMBO_COL, items); }

    /** Invoked when button(addRow) is clicked */
    @NotNull public abstract Action addClicked();

    /** Called on removing table row. */
    @NotNull public Action remove() { return getTable().getCurrent().remove(); }

    /** Invoked when button(addMessageFirst) is clicked */
    @NotNull public abstract Action addMessageToFirst();

    /** Invoked when button(setOptionsFirst) is clicked */
    @NotNull public abstract Action setOptionsToFirst();

    /** Invoked when button(setOptionsGlobal) is clicked */
    @NotNull public abstract Action setOptionsGlobal();

    /** Invoked when button(resetFirst) is clicked */
    @NotNull public abstract Action resetFirst();

    /** Invoked when button(configFirst) is clicked */
    @NotNull public abstract Action configFirst();

    /** Register a form Listener */
    public static void addListener(@NotNull FormListenerType listenerType, @NotNull FormListener<NewSync> listener) { listeners.addListener(listenerType, listener); }

    /** Remove a form Listener */
    public static void removeListener(@NotNull FormListenerType listenerType, @NotNull FormListener<NewSync> listener) { listeners.removeListener(listenerType, listener); }

    @Override @NotNull public Logger logger() { return logger; }

    /** Returns utility methods to deal with {@link Action actions}. */
    @NotNull protected final Actions actions() { return actions; }

    //~ Fields ...................................................................................................................

    @NotNull private static FormListenerMap<NewSync> listeners = new FormListenerMap<>();
    @NotNull private static final Logger logger = Logger.getLogger(NewSync.class);

    //~ Inner Classes ............................................................................................................

    public abstract class TableRowBase
        implements FormRowInstance<TableRow>
    {

        //~ Fields ...................................................................................................................

        @SuppressWarnings({"InstanceVariableMayNotBeInitialized", "UnusedDeclaration", "NullableProblems"}) @NotNull private UiModelAccessor f;

        //~ Methods ..................................................................................................................

        /** Returns the value of the text_field(strCol). */
        @NotNull public String getStrCol() { return f.get(Field.STR_COL, String.class); }

        /** Sets the value of the text_field(strCol). */
        @NotNull public TableRow setStrCol(@NotNull String strCol) {
            f.set(Field.STR_COL, strCol);
            return (TableRow) this;
        }

        /** Returns the value of the text_field(intCol). */
        public int getIntCol() { return f.get(Field.INT_COL, Integer.class); }

        /** Sets the value of the text_field(intCol). */
        @NotNull public TableRow setIntCol(int intCol) {
            f.set(Field.INT_COL, intCol);
            return (TableRow) this;
        }

        /** Returns the value of the date_box(dateCol). */
        @NotNull public DateOnly getDateCol() {
            return DateOnly.fromMilliseconds(f.get(Field.DATE_COL, Long.class));
        }

        /** Sets the value of the date_box(dateCol). */
        @NotNull public TableRow setDateCol(@NotNull DateOnly dateCol) {
            f.set(Field.DATE_COL, dateCol);
            return (TableRow) this;
        }

        /** Returns the value of the combo_box(comboCol). */
        @NotNull public String getComboCol() { return f.get(Field.COMBO_COL, String.class); }

        /** Sets the value of the combo_box(comboCol). */
        @NotNull public TableRow setComboCol(@NotNull String comboCol) {
            f.set(Field.COMBO_COL, comboCol);
            return (TableRow) this;
        }

        /** Sets the options of the combo_box(comboCol). */
        public void setComboColOptions(@NotNull Iterable<String> items) { f.opts(Field.COMBO_COL, items); }

        /** Sets the options of the combo_box(comboCol) with the given KeyMap. */
        public void setComboColOptions(@NotNull KeyMap items) { f.opts(Field.COMBO_COL, items); }

        /** Create and set a new AddressesForm instance */
        @NotNull public AddressesForm createAddress() { return f.init(Field.ADDRESS, AddressesForm.class); }

        /** Create and populates set a new AddressesForm instance with a pk */
        @NotNull public AddressesForm createAddress(@NotNull String key) { return f.init(Field.ADDRESS, AddressesForm.class, key); }

        /** 
         * Get the AddressesForm if defined, or null otherwise.
         * @see #createAddress
         */
        @Nullable public AddressesForm getAddress() { return f.subform(Field.ADDRESS, AddressesForm.class); }

        /** Associates a new message to the specified field and returns it for any further configuration. */
        @NotNull public Message message(@NotNull Field field, @NotNull String msg) { return f.msg(field, msg); }

        /** Returns true if the field value is not null. */
        public boolean isDefined(@NotNull Field... fields) { return f.defined(fields); }

        /** Resets the given fields. */
        protected void reset(@NotNull Field... fields) { f.reset(fields); }

        /** Focuses given field. */
        protected void focus(@NotNull Field field) { f.focus(field); }

        /** Return associated table. */
        @NotNull public final FormTable<TableRow> table() { return getTable(); }

        /** Remove row from table. */
        @NotNull public Action remove() {
            table().remove((TableRow) this);
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
        VIEW_SOURCE("viewSource"),
        $H2("$H2"),
        ADD_ROW_TOP("addRowTop"),
        REMOVE_ROW_TOP("removeRowTop"),
        TEST("test"),
        TABLE("table"),
        STR_COL("strCol"),
        INT_COL("intCol"),
        DATE_COL("dateCol"),
        COMBO_COL("comboCol"),
        ADDRESS("address"),
        REMOVE_IN_ROW("removeInRow"),
        $H3("$H3"),
        ADD_ROW("addRow"),
        REMOVE_ROW("removeRow"),
        ADD_MESSAGE_FIRST("addMessageFirst"),
        SET_OPTIONS_FIRST("setOptionsFirst"),
        SET_OPTIONS_GLOBAL("setOptionsGlobal"),
        RESET_FIRST("resetFirst"),
        CONFIG_FIRST("configFirst");

        //~ Fields ...................................................................................................................

        @Nullable private final String id;

        //~ Constructors .............................................................................................................

        Field(@Nullable final String id) { this.id = id; }

        //~ Methods ..................................................................................................................

        @NotNull public String id() { return id; }

    }
}
