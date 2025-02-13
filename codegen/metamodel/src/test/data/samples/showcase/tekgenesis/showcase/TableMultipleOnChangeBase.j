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
import tekgenesis.showcase.TableMultipleOnChange.ItemsRow;
import tekgenesis.common.logging.LoggableInstance;
import tekgenesis.common.logging.Logger;
import tekgenesis.form.Message;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import tekgenesis.form.UiModelAccessor;
import tekgenesis.form.configuration.WidgetConfiguration;

/** 
 * Generated base class for form: TableMultipleOnChange.
 * Don't modify this as this is an auto generated class that's gets generated
 * every time the meta model file is modified. Use subclass instead.
 */
@SuppressWarnings({"DuplicateStringLiteralInspection", "WeakerAccess", "FieldMayBeFinal", "FieldCanBeLocal", "ConstantConditions", "ClassWithTooManyMethods", "ClassWithTooManyFields", "MagicNumber", "MethodOverridesStaticMethodOfSuperclass", "ParameterHidesMemberVariable", "FieldNameHidesFieldInSuperclass", "OverlyComplexClass", "RedundantCast", "UnusedReturnValue", "LocalVariableHidesMemberVariable", "UnusedParameters", "EmptyMethod"})
public abstract class TableMultipleOnChangeBase
    extends FormInstance<Void>
    implements LoggableInstance
{

    //~ Fields ...................................................................................................................

    @SuppressWarnings({"InstanceVariableMayNotBeInitialized", "UnusedDeclaration", "NullableProblems"}) @NotNull private UiModelAccessor f;

    //~ Methods ..................................................................................................................

    /** Invoked when populating a form instance */
    @NotNull public final Void populate() {
        throw new FormCannotBePopulatedException("tekgenesis.showcase.TableMultipleOnChange");
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

    /** Returns the value of the text_field(field). */
    public int getField() { return f.get(Field.FIELD, Integer.class); }

    /** Sets the value of the text_field(field). */
    @NotNull public TableMultipleOnChange setField(int field) {
        f.set(Field.FIELD, field);
        return (TableMultipleOnChange) this;
    }

    /** Invoked when text_field(twoTimesField) value changes */
    @NotNull public abstract Action bchanged();

    /** Returns the value of the text_field(twoTimesField). */
    public int getTwoTimesField() { return f.get(Field.TWO_TIMES_FIELD, Integer.class); }

    /** Sets the value of the text_field(twoTimesField). */
    @NotNull public TableMultipleOnChange setTwoTimesField(int twoTimesField) {
        f.set(Field.TWO_TIMES_FIELD, twoTimesField);
        return (TableMultipleOnChange) this;
    }

    /** Invoked when button(setField10) is clicked */
    @NotNull public abstract Action setField();

    /** Invoked when button(any) is clicked */
    @NotNull public abstract Action anyClick();

    /** Returns a {@link FormTable<ItemsRow>} instance to handle Items manipulation */
    @NotNull public final FormTable<ItemsRow> getItems() { return table(Field.ITEMS, ItemsRow.class); }

    /** Method to allow table row class override */
    @NotNull final <T> FormTable<T> table(@NotNull FormFieldRef field, @NotNull Class<T> rowClass) { return f.table(field, rowClass, this); }

    /** Invoked when text_field(sum) value changes */
    @NotNull public abstract Action sumchanged();

    /** Returns the value of the text_field(sum). */
    public int getSum() { return f.get(Field.SUM, Integer.class); }

    /** Sets the value of the text_field(sum). */
    @NotNull public TableMultipleOnChange setSum(int sum) {
        f.set(Field.SUM, sum);
        return (TableMultipleOnChange) this;
    }

    /** Invoked when text_field(otherField) value changes */
    @NotNull public abstract Action echanged();

    /** Returns the value of the text_field(otherField). */
    public int getOtherField() { return f.get(Field.OTHER_FIELD, Integer.class); }

    /** Sets the value of the text_field(otherField). */
    @NotNull public TableMultipleOnChange setOtherField(int otherField) {
        f.set(Field.OTHER_FIELD, otherField);
        return (TableMultipleOnChange) this;
    }

    /** Returns the value of the text_area(progressDebug). */
    @Nullable public String getProgressDebug() { return f.get(Field.PROGRESS_DEBUG, String.class); }

    /** Sets the value of the text_area(progressDebug). */
    @NotNull public TableMultipleOnChange setProgressDebug(@Nullable String progressDebug) {
        f.set(Field.PROGRESS_DEBUG, progressDebug);
        return (TableMultipleOnChange) this;
    }

    /** Register a form Listener */
    public static void addListener(@NotNull FormListenerType listenerType, @NotNull FormListener<TableMultipleOnChange> listener) { listeners.addListener(listenerType, listener); }

    /** Remove a form Listener */
    public static void removeListener(@NotNull FormListenerType listenerType, @NotNull FormListener<TableMultipleOnChange> listener) { listeners.removeListener(listenerType, listener); }

    @Override @NotNull public Logger logger() { return logger; }

    /** Returns utility methods to deal with {@link Action actions}. */
    @NotNull protected final Actions actions() { return actions; }

    //~ Fields ...................................................................................................................

    @NotNull private static FormListenerMap<TableMultipleOnChange> listeners = new FormListenerMap<>();
    @NotNull private static final Logger logger = Logger.getLogger(TableMultipleOnChange.class);

    //~ Inner Classes ............................................................................................................

    public abstract class ItemsRowBase
        implements FormRowInstance<ItemsRow>
    {

        //~ Fields ...................................................................................................................

        @SuppressWarnings({"InstanceVariableMayNotBeInitialized", "UnusedDeclaration", "NullableProblems"}) @NotNull private UiModelAccessor f;

        //~ Methods ..................................................................................................................

        /** Returns the value of the text_field(column1). */
        public int getColumn1() { return f.get(Field.COLUMN1, Integer.class); }

        /** Sets the value of the text_field(column1). */
        @NotNull public ItemsRow setColumn1(int column1) {
            f.set(Field.COLUMN1, column1);
            return (ItemsRow) this;
        }

        /** Invoked when text_field(column2) value changes */
        @NotNull public abstract Action changed();

        /** Returns the value of the text_field(column2). */
        public int getColumn2() { return f.get(Field.COLUMN2, Integer.class); }

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
        FIELD("field"),
        TWO_TIMES_FIELD("twoTimesField"),
        SET_FIELD10("setField10"),
        ANY("any"),
        ITEMS("items"),
        COLUMN1("column1"),
        COLUMN2("column2"),
        SAVE("save"),
        $H2("$H2"),
        ADD_ROW_BOTTOM("addRowBottom"),
        REMOVE_ROW_BOTTOM("removeRowBottom"),
        SUM("sum"),
        OTHER_FIELD("otherField"),
        PROGRESS_DEBUG("progressDebug");

        //~ Fields ...................................................................................................................

        @Nullable private final String id;

        //~ Constructors .............................................................................................................

        Field(@Nullable final String id) { this.id = id; }

        //~ Methods ..................................................................................................................

        @NotNull public String id() { return id; }

    }
}
