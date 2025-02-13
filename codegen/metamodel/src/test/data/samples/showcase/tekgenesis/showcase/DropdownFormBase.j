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
import tekgenesis.showcase.DropdownForm.TableRow;
import tekgenesis.form.UiModelAccessor;
import tekgenesis.form.configuration.WidgetConfiguration;

/** 
 * Generated base class for form: DropdownForm.
 * Don't modify this as this is an auto generated class that's gets generated
 * every time the meta model file is modified. Use subclass instead.
 */
@SuppressWarnings({"DuplicateStringLiteralInspection", "WeakerAccess", "FieldMayBeFinal", "FieldCanBeLocal", "ConstantConditions", "ClassWithTooManyMethods", "ClassWithTooManyFields", "MagicNumber", "MethodOverridesStaticMethodOfSuperclass", "ParameterHidesMemberVariable", "FieldNameHidesFieldInSuperclass", "OverlyComplexClass", "RedundantCast", "UnusedReturnValue", "LocalVariableHidesMemberVariable", "UnusedParameters", "EmptyMethod"})
public abstract class DropdownFormBase
    extends FormInstance<Void>
    implements LoggableInstance
{

    //~ Fields ...................................................................................................................

    @SuppressWarnings({"InstanceVariableMayNotBeInitialized", "UnusedDeclaration", "NullableProblems"}) @NotNull private UiModelAccessor f;

    //~ Methods ..................................................................................................................

    /** Invoked when populating a form instance */
    @NotNull public final Void populate() {
        throw new FormCannotBePopulatedException("tekgenesis.showcase.DropdownForm");
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

    /** Returns the value of the check_box(a). */
    public boolean isA() { return f.get(Field.A, Boolean.class); }

    /** Sets the value of the check_box(a). */
    @NotNull public DropdownForm setA(boolean a) {
        f.set(Field.A, a);
        return (DropdownForm) this;
    }

    /** Returns the value of the check_box(b). */
    public boolean isB() { return f.get(Field.B, Boolean.class); }

    /** Sets the value of the check_box(b). */
    @NotNull public DropdownForm setB(boolean b) {
        f.set(Field.B, b);
        return (DropdownForm) this;
    }

    /** 
     * Invoked when label(first) is clicked
     * Invoked when label(second) is clicked
     */
    @NotNull public abstract Action firstClick();

    /** Returns a {@link FormTable<TableRow>} instance to handle Table manipulation */
    @NotNull public final FormTable<TableRow> getTable() { return table(Field.TABLE, TableRow.class); }

    /** Method to allow table row class override */
    @NotNull final <T> FormTable<T> table(@NotNull FormFieldRef field, @NotNull Class<T> rowClass) { return f.table(field, rowClass, this); }

    /** Register a form Listener */
    public static void addListener(@NotNull FormListenerType listenerType, @NotNull FormListener<DropdownForm> listener) { listeners.addListener(listenerType, listener); }

    /** Remove a form Listener */
    public static void removeListener(@NotNull FormListenerType listenerType, @NotNull FormListener<DropdownForm> listener) { listeners.removeListener(listenerType, listener); }

    @Override @NotNull public Logger logger() { return logger; }

    /** Returns utility methods to deal with {@link Action actions}. */
    @NotNull protected final Actions actions() { return actions; }

    //~ Fields ...................................................................................................................

    @NotNull private static FormListenerMap<DropdownForm> listeners = new FormListenerMap<>();
    @NotNull private static final Logger logger = Logger.getLogger(DropdownForm.class);

    //~ Inner Classes ............................................................................................................

    public abstract class TableRowBase
        implements FormRowInstance<TableRow>
    {

        //~ Fields ...................................................................................................................

        @SuppressWarnings({"InstanceVariableMayNotBeInitialized", "UnusedDeclaration", "NullableProblems"}) @NotNull private UiModelAccessor f;

        //~ Methods ..................................................................................................................

        /** Returns the value of the text_field(name). */
        @NotNull public String getName() { return f.get(Field.NAME, String.class); }

        /** Sets the value of the text_field(name). */
        @NotNull public TableRow setName(@NotNull String name) {
            f.set(Field.NAME, name);
            return (TableRow) this;
        }

        /** Returns the value of the text_field(lastname). */
        @NotNull public String getLastname() { return f.get(Field.LASTNAME, String.class); }

        /** Sets the value of the text_field(lastname). */
        @NotNull public TableRow setLastname(@NotNull String lastname) {
            f.set(Field.LASTNAME, lastname);
            return (TableRow) this;
        }

        /** Returns the value of the check_box(available). */
        public boolean isAvailable() { return f.get(Field.AVAILABLE, Boolean.class); }

        /** Sets the value of the check_box(available). */
        @NotNull public TableRow setAvailable(boolean available) {
            f.set(Field.AVAILABLE, available);
            return (TableRow) this;
        }

        /** 
         * Invoked when button(nr1) is clicked
         * Invoked when label(l1) is clicked
         * Invoked when label(l2) is clicked
         */
        @NotNull public abstract Action any();

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
        A("a"),
        B("b"),
        DDOWN("ddown"),
        FIRST("first"),
        SECOND("second"),
        $V0("$V0"),
        TABLE("table"),
        NAME("name"),
        LASTNAME("lastname"),
        $H1("$H1"),
        AVAILABLE("available"),
        NR1("nr1"),
        DROP("drop"),
        L1("l1"),
        L2("l2"),
        $B2("$B2"),
        $B3("$B3");

        //~ Fields ...................................................................................................................

        @Nullable private final String id;

        //~ Constructors .............................................................................................................

        Field(@Nullable final String id) { this.id = id; }

        //~ Methods ..................................................................................................................

        @NotNull public String id() { return id; }

    }
}
