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
import tekgenesis.common.logging.LoggableInstance;
import tekgenesis.common.logging.Logger;
import tekgenesis.form.Message;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import tekgenesis.showcase.SubformsShowcase.TableRow;
import tekgenesis.form.UiModelAccessor;
import tekgenesis.form.configuration.WidgetConfiguration;

/** 
 * Generated base class for form: SubformsShowcase.
 * Don't modify this as this is an auto generated class that's gets generated
 * every time the meta model file is modified. Use subclass instead.
 */
@SuppressWarnings({"DuplicateStringLiteralInspection", "WeakerAccess", "FieldMayBeFinal", "FieldCanBeLocal", "ConstantConditions", "ClassWithTooManyMethods", "ClassWithTooManyFields", "MagicNumber", "MethodOverridesStaticMethodOfSuperclass", "ParameterHidesMemberVariable", "FieldNameHidesFieldInSuperclass", "OverlyComplexClass", "RedundantCast", "UnusedReturnValue", "LocalVariableHidesMemberVariable", "UnusedParameters", "EmptyMethod"})
public abstract class SubformsShowcaseBase
    extends FormInstance<Void>
    implements LoggableInstance
{

    //~ Fields ...................................................................................................................

    @SuppressWarnings({"InstanceVariableMayNotBeInitialized", "UnusedDeclaration", "NullableProblems"}) @NotNull private UiModelAccessor f;

    //~ Methods ..................................................................................................................

    /** Invoked when populating a form instance */
    @NotNull public final Void populate() {
        throw new FormCannotBePopulatedException("tekgenesis.showcase.SubformsShowcase");
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

    /** Returns the value of the text_field(first). */
    @NotNull public String getFirst() { return f.get(Field.FIRST, String.class); }

    /** Sets the value of the text_field(first). */
    @NotNull public SubformsShowcase setFirst(@NotNull String first) {
        f.set(Field.FIRST, first);
        return (SubformsShowcase) this;
    }

    /** Returns the value of the text_field(last). */
    @NotNull public String getLast() { return f.get(Field.LAST, String.class); }

    /** Sets the value of the text_field(last). */
    @NotNull public SubformsShowcase setLast(@NotNull String last) {
        f.set(Field.LAST, last);
        return (SubformsShowcase) this;
    }

    /** Create and set a new AddressForm instance */
    @NotNull public AddressForm createAddress() { return f.init(Field.ADDRESS, AddressForm.class); }

    /** Create and populates set a new AddressForm instance with a pk */
    @NotNull public AddressForm createAddress(@NotNull String key) { return f.init(Field.ADDRESS, AddressForm.class, key); }

    /** 
     * Get the AddressForm if defined, or null otherwise.
     * @see #createAddress
     */
    @Nullable public AddressForm getAddress() { return f.subform(Field.ADDRESS, AddressForm.class); }

    /** Invoked when button(syncSubForm) is clicked */
    @NotNull public abstract Action changeSubForm();

    /** Returns the value of the internal(hideSubform). */
    public boolean isHideSubform() { return f.get(Field.HIDE_SUBFORM, Boolean.class); }

    /** Sets the value of the internal(hideSubform). */
    @NotNull public SubformsShowcase setHideSubform(boolean hideSubform) {
        f.set(Field.HIDE_SUBFORM, hideSubform);
        return (SubformsShowcase) this;
    }

    /** Returns a {@link FormTable<TableRow>} instance to handle Table manipulation */
    @NotNull public final FormTable<TableRow> getTable() { return table(Field.TABLE, TableRow.class); }

    /** Method to allow table row class override */
    @NotNull final <T> FormTable<T> table(@NotNull FormFieldRef field, @NotNull Class<T> rowClass) { return f.table(field, rowClass, this); }

    /** Register a form Listener */
    public static void addListener(@NotNull FormListenerType listenerType, @NotNull FormListener<SubformsShowcase> listener) { listeners.addListener(listenerType, listener); }

    /** Remove a form Listener */
    public static void removeListener(@NotNull FormListenerType listenerType, @NotNull FormListener<SubformsShowcase> listener) { listeners.removeListener(listenerType, listener); }

    @Override @NotNull public Logger logger() { return logger; }

    /** Returns utility methods to deal with {@link Action actions}. */
    @NotNull protected final Actions actions() { return actions; }

    //~ Fields ...................................................................................................................

    @NotNull private static FormListenerMap<SubformsShowcase> listeners = new FormListenerMap<>();
    @NotNull private static final Logger logger = Logger.getLogger(SubformsShowcase.class);

    //~ Inner Classes ............................................................................................................

    public abstract class TableRowBase
        implements FormRowInstance<TableRow>
    {

        //~ Fields ...................................................................................................................

        @SuppressWarnings({"InstanceVariableMayNotBeInitialized", "UnusedDeclaration", "NullableProblems"}) @NotNull private UiModelAccessor f;

        //~ Methods ..................................................................................................................

        /** Returns the value of the text_field(description). */
        @NotNull public String getDescription() { return f.get(Field.DESCRIPTION, String.class); }

        /** Sets the value of the text_field(description). */
        @NotNull public TableRow setDescription(@NotNull String description) {
            f.set(Field.DESCRIPTION, description);
            return (TableRow) this;
        }

        /** Create and set a new AddressForm instance */
        @NotNull public AddressForm createAddressInSubform() { return f.init(Field.ADDRESS_IN_SUBFORM, AddressForm.class); }

        /** Create and populates set a new AddressForm instance with a pk */
        @NotNull public AddressForm createAddressInSubform(@NotNull String key) {
            return f.init(Field.ADDRESS_IN_SUBFORM, AddressForm.class, key);
        }

        /** 
         * Get the AddressForm if defined, or null otherwise.
         * @see #createAddressInSubform
         */
        @Nullable public AddressForm getAddressInSubform() {
            return f.subform(Field.ADDRESS_IN_SUBFORM, AddressForm.class);
        }

        /** Create and set a new AddressForm instance */
        @NotNull public AddressForm createAddressInSubform2() { return f.init(Field.ADDRESS_IN_SUBFORM2, AddressForm.class); }

        /** Create and populates set a new AddressForm instance with a pk */
        @NotNull public AddressForm createAddressInSubform2(@NotNull String key) {
            return f.init(Field.ADDRESS_IN_SUBFORM2, AddressForm.class, key);
        }

        /** 
         * Get the AddressForm if defined, or null otherwise.
         * @see #createAddressInSubform2
         */
        @Nullable public AddressForm getAddressInSubform2() {
            return f.subform(Field.ADDRESS_IN_SUBFORM2, AddressForm.class);
        }

        /** Invoked when button($B5) is clicked */
        @NotNull public abstract Action hideSubform();

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
        $L2("$L2"),
        FIRST("first"),
        LAST("last"),
        ADDRESS("address"),
        PLACEHOLDER("placeholder"),
        SYNC_SUB_FORM("syncSubForm"),
        HIDE_SUBFORM("hideSubform"),
        TABLE("table"),
        DESCRIPTION("description"),
        $H3("$H3"),
        ADDRESS_IN_SUBFORM("addressInSubform"),
        $H4("$H4"),
        ADDRESS_IN_SUBFORM2("addressInSubform2"),
        $B5("$B5"),
        $H6("$H6"),
        ADD_ROW_BOTTOM("addRowBottom"),
        REMOVE_ROW_BOTTOM("removeRowBottom");

        //~ Fields ...................................................................................................................

        @Nullable private final String id;

        //~ Constructors .............................................................................................................

        Field(@Nullable final String id) { this.id = id; }

        //~ Methods ..................................................................................................................

        @NotNull public String id() { return id; }

    }
}
