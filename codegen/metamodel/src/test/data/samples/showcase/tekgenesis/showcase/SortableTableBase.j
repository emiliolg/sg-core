package tekgenesis.showcase;

import tekgenesis.form.Action;
import tekgenesis.form.Actions;
import java.util.ArrayList;
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
import tekgenesis.common.Predefined;
import tekgenesis.showcase.SortableTable.TableRow;
import tekgenesis.form.UiModelAccessor;
import tekgenesis.form.configuration.WidgetConfiguration;

/** 
 * Generated base class for form: SortableTable.
 * Don't modify this as this is an auto generated class that's gets generated
 * every time the meta model file is modified. Use subclass instead.
 */
@SuppressWarnings({"DuplicateStringLiteralInspection", "WeakerAccess", "FieldMayBeFinal", "FieldCanBeLocal", "ConstantConditions", "ClassWithTooManyMethods", "ClassWithTooManyFields", "MagicNumber", "MethodOverridesStaticMethodOfSuperclass", "ParameterHidesMemberVariable", "FieldNameHidesFieldInSuperclass", "OverlyComplexClass", "RedundantCast", "UnusedReturnValue", "LocalVariableHidesMemberVariable", "UnusedParameters", "EmptyMethod"})
public abstract class SortableTableBase
    extends FormInstance<Void>
    implements LoggableInstance
{

    //~ Fields ...................................................................................................................

    @SuppressWarnings({"InstanceVariableMayNotBeInitialized", "UnusedDeclaration", "NullableProblems"}) @NotNull private UiModelAccessor f;

    //~ Methods ..................................................................................................................

    /** Invoked when populating a form instance */
    @NotNull public final Void populate() {
        throw new FormCannotBePopulatedException("tekgenesis.showcase.SortableTable");
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

    /** Invoked when text_field(test) value changes */
    @NotNull public abstract Action changeScalar();

    /** Returns the value of the text_field(test). */
    @NotNull public String getTest() { return f.get(Field.TEST, String.class); }

    /** Sets the value of the text_field(test). */
    @NotNull public SortableTable setTest(@NotNull String test) {
        f.set(Field.TEST, test);
        return (SortableTable) this;
    }

    /** Invoked when table(table) is clicked */
    @NotNull public abstract Action select();

    /** Returns a {@link FormTable<TableRow>} instance to handle Table manipulation */
    @NotNull public final FormTable<TableRow> getTable() { return table(Field.TABLE, TableRow.class); }

    /** Method to allow table row class override */
    @NotNull final <T> FormTable<T> table(@NotNull FormFieldRef field, @NotNull Class<T> rowClass) { return f.table(field, rowClass, this); }

    /** Register a form Listener */
    public static void addListener(@NotNull FormListenerType listenerType, @NotNull FormListener<SortableTable> listener) { listeners.addListener(listenerType, listener); }

    /** Remove a form Listener */
    public static void removeListener(@NotNull FormListenerType listenerType, @NotNull FormListener<SortableTable> listener) { listeners.removeListener(listenerType, listener); }

    @Override @NotNull public Logger logger() { return logger; }

    /** Returns utility methods to deal with {@link Action actions}. */
    @NotNull protected final Actions actions() { return actions; }

    //~ Fields ...................................................................................................................

    @NotNull private static FormListenerMap<SortableTable> listeners = new FormListenerMap<>();
    @NotNull private static final Logger logger = Logger.getLogger(SortableTable.class);

    //~ Inner Classes ............................................................................................................

    public abstract class TableRowBase
        implements FormRowInstance<TableRow>
    {

        //~ Fields ...................................................................................................................

        @SuppressWarnings({"InstanceVariableMayNotBeInitialized", "UnusedDeclaration", "NullableProblems"}) @NotNull private UiModelAccessor f;

        //~ Methods ..................................................................................................................

        /** Returns the value of the internal(key). */
        public int getKey() { return f.get(Field.KEY, Integer.class); }

        /** Sets the value of the internal(key). */
        @NotNull public TableRow setKey(int key) {
            f.set(Field.KEY, key);
            return (TableRow) this;
        }

        /** Returns the value of the display(integer). */
        public int getInteger() { return f.get(Field.INTEGER, Integer.class); }

        /** Sets the value of the display(integer). */
        @NotNull public TableRow setInteger(int integer) {
            f.set(Field.INTEGER, integer);
            return (TableRow) this;
        }

        /** Returns the value of the display(string). */
        @NotNull public String getString() { return f.get(Field.STRING, String.class); }

        /** Sets the value of the display(string). */
        @NotNull public TableRow setString(@NotNull String string) {
            f.set(Field.STRING, string);
            return (TableRow) this;
        }

        /** Invoked when text_field(currency) value changes */
        @NotNull public abstract Action change();

        /** Returns the value of the text_field(currency). */
        public double getCurrency() { return f.get(Field.CURRENCY, Double.class); }

        /** Sets the value of the text_field(currency). */
        @NotNull public TableRow setCurrency(double currency) {
            f.set(Field.CURRENCY, currency);
            return (TableRow) this;
        }

        /** Invoked when the user type something on suggest_box(simple) to create suggest list */
        @NotNull public abstract Iterable<SimpleEntity> suggest(@Nullable String query);

        /** Returns the value of the suggest_box(simple). */
        @NotNull public SimpleEntity getSimple() {
            return Predefined.ensureNotNull(SimpleEntity.find(getSimpleKey()), "'simple' not found");
        }

        /** Returns the key value of the suggest_box(simple). */
        @NotNull public String getSimpleKey() { return f.get(Field.SIMPLE, String.class); }

        /** Sets the value of the suggest_box(simple). */
        @NotNull public TableRow setSimple(@NotNull SimpleEntity simple) {
            f.set(Field.SIMPLE, simple);
            return (TableRow) this;
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
        TEST("test"),
        TABLE("table"),
        KEY("key"),
        ICON("icon"),
        INTEGER("integer"),
        STRING("string"),
        CURRENCY("currency"),
        SIMPLE("simple");

        //~ Fields ...................................................................................................................

        @Nullable private final String id;

        //~ Constructors .............................................................................................................

        Field(@Nullable final String id) { this.id = id; }

        //~ Methods ..................................................................................................................

        @NotNull public String id() { return id; }

    }
}
