package tekgenesis.showcase;

import tekgenesis.form.Action;
import tekgenesis.form.Actions;
import tekgenesis.showcase.DisabledTable.FirstTableRow;
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
import tekgenesis.showcase.DisabledTable.SecondTableRow;
import tekgenesis.form.UiModelAccessor;
import tekgenesis.form.configuration.WidgetConfiguration;

/** 
 * Generated base class for form: DisabledTable.
 * Don't modify this as this is an auto generated class that's gets generated
 * every time the meta model file is modified. Use subclass instead.
 */
@SuppressWarnings({"DuplicateStringLiteralInspection", "WeakerAccess", "FieldMayBeFinal", "FieldCanBeLocal", "ConstantConditions", "ClassWithTooManyMethods", "ClassWithTooManyFields", "MagicNumber", "MethodOverridesStaticMethodOfSuperclass", "ParameterHidesMemberVariable", "FieldNameHidesFieldInSuperclass", "OverlyComplexClass", "RedundantCast", "UnusedReturnValue", "LocalVariableHidesMemberVariable", "UnusedParameters", "EmptyMethod"})
public abstract class DisabledTableBase
    extends FormInstance<Void>
    implements LoggableInstance
{

    //~ Fields ...................................................................................................................

    @SuppressWarnings({"InstanceVariableMayNotBeInitialized", "UnusedDeclaration", "NullableProblems"}) @NotNull private UiModelAccessor f;

    //~ Methods ..................................................................................................................

    /** Invoked when populating a form instance */
    @NotNull public final Void populate() {
        throw new FormCannotBePopulatedException("tekgenesis.showcase.DisabledTable");
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

    /** Returns the value of the check_box(firstBool). */
    public boolean isFirstBool() { return f.get(Field.FIRST_BOOL, Boolean.class); }

    /** Sets the value of the check_box(firstBool). */
    @NotNull public DisabledTable setFirstBool(boolean firstBool) {
        f.set(Field.FIRST_BOOL, firstBool);
        return (DisabledTable) this;
    }

    /** Returns a {@link FormTable<FirstTableRow>} instance to handle FirstTable manipulation */
    @NotNull public final FormTable<FirstTableRow> getFirstTable() { return table(Field.FIRST_TABLE, FirstTableRow.class); }

    /** Method to allow table row class override */
    @NotNull final <T> FormTable<T> table(@NotNull FormFieldRef field, @NotNull Class<T> rowClass) { return f.table(field, rowClass, this); }

    /** Returns a {@link FormTable<SecondTableRow>} instance to handle SecondTable manipulation */
    @NotNull public final FormTable<SecondTableRow> getSecondTable() { return table(Field.SECOND_TABLE, SecondTableRow.class); }

    /** Register a form Listener */
    public static void addListener(@NotNull FormListenerType listenerType, @NotNull FormListener<DisabledTable> listener) { listeners.addListener(listenerType, listener); }

    /** Remove a form Listener */
    public static void removeListener(@NotNull FormListenerType listenerType, @NotNull FormListener<DisabledTable> listener) { listeners.removeListener(listenerType, listener); }

    @Override @NotNull public Logger logger() { return logger; }

    /** Returns utility methods to deal with {@link Action actions}. */
    @NotNull protected final Actions actions() { return actions; }

    //~ Fields ...................................................................................................................

    @NotNull private static FormListenerMap<DisabledTable> listeners = new FormListenerMap<>();
    @NotNull private static final Logger logger = Logger.getLogger(DisabledTable.class);

    //~ Inner Classes ............................................................................................................

    public abstract class FirstTableRowBase
        implements FormRowInstance<FirstTableRow>
    {

        //~ Fields ...................................................................................................................

        @SuppressWarnings({"InstanceVariableMayNotBeInitialized", "UnusedDeclaration", "NullableProblems"}) @NotNull private UiModelAccessor f;

        //~ Methods ..................................................................................................................

        /** Returns the value of the text_field(field1). */
        @NotNull public String getField1() { return f.get(Field.FIELD1, String.class); }

        /** Sets the value of the text_field(field1). */
        @NotNull public FirstTableRow setField1(@NotNull String field1) {
            f.set(Field.FIELD1, field1);
            return (FirstTableRow) this;
        }

        /** Returns the value of the text_field(field2). */
        @NotNull public String getField2() { return f.get(Field.FIELD2, String.class); }

        /** Sets the value of the text_field(field2). */
        @NotNull public FirstTableRow setField2(@NotNull String field2) {
            f.set(Field.FIELD2, field2);
            return (FirstTableRow) this;
        }

        /** Returns the value of the check_box(field3). */
        public boolean isField3() { return f.get(Field.FIELD3, Boolean.class); }

        /** Sets the value of the check_box(field3). */
        @NotNull public FirstTableRow setField3(boolean field3) {
            f.set(Field.FIELD3, field3);
            return (FirstTableRow) this;
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
        @NotNull public final FormTable<FirstTableRow> table() { return getFirstTable(); }

        /** Remove row from table. */
        @NotNull public Action remove() {
            table().remove((FirstTableRow) this);
            return actions().getDefault();
        }

        /** Returns a typed configuration for a given field. */
        @NotNull protected <T extends WidgetConfiguration> T configuration(@NotNull Field field) { return f.config(field); }

    }

    public abstract class SecondTableRowBase
        implements FormRowInstance<SecondTableRow>
    {

        //~ Fields ...................................................................................................................

        @SuppressWarnings({"InstanceVariableMayNotBeInitialized", "UnusedDeclaration", "NullableProblems"}) @NotNull private UiModelAccessor f;

        //~ Methods ..................................................................................................................

        /** Returns the value of the text_field(field4). */
        @NotNull public String getField4() { return f.get(Field.FIELD4, String.class); }

        /** Sets the value of the text_field(field4). */
        @NotNull public SecondTableRow setField4(@NotNull String field4) {
            f.set(Field.FIELD4, field4);
            return (SecondTableRow) this;
        }

        /** Returns the value of the text_field(field5). */
        @NotNull public String getField5() { return f.get(Field.FIELD5, String.class); }

        /** Sets the value of the text_field(field5). */
        @NotNull public SecondTableRow setField5(@NotNull String field5) {
            f.set(Field.FIELD5, field5);
            return (SecondTableRow) this;
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
        @NotNull public final FormTable<SecondTableRow> table() { return getSecondTable(); }

        /** Remove row from table. */
        @NotNull public Action remove() {
            table().remove((SecondTableRow) this);
            return actions().getDefault();
        }

        /** Returns a typed configuration for a given field. */
        @NotNull protected <T extends WidgetConfiguration> T configuration(@NotNull Field field) { return f.config(field); }

    }

    @SuppressWarnings("UnnecessarySemicolon")
    public enum Field
        implements tekgenesis.metadata.form.widget.FormFieldRef
    {
        FIRST_BOOL("firstBool"),
        FIRST_TABLE("firstTable"),
        FIELD1("field1"),
        FIELD2("field2"),
        FIELD3("field3"),
        $B0("$B0"),
        $B1("$B1"),
        SECOND_TABLE("secondTable"),
        FIELD4("field4"),
        FIELD5("field5"),
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
