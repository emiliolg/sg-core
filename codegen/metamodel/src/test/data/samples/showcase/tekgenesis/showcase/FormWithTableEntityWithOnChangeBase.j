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
import tekgenesis.common.Predefined;
import tekgenesis.type.permission.PredefinedPermission;
import tekgenesis.showcase.FormWithTableEntityWithOnChange.TableRow;
import tekgenesis.transaction.Transaction;
import tekgenesis.form.UiModelAccessor;
import tekgenesis.form.configuration.WidgetConfiguration;

/** 
 * Generated base class for form: FormWithTableEntityWithOnChange.
 * Don't modify this as this is an auto generated class that's gets generated
 * every time the meta model file is modified. Use subclass instead.
 */
@SuppressWarnings({"DuplicateStringLiteralInspection", "WeakerAccess", "FieldMayBeFinal", "FieldCanBeLocal", "ConstantConditions", "ClassWithTooManyMethods", "ClassWithTooManyFields", "MagicNumber", "MethodOverridesStaticMethodOfSuperclass", "ParameterHidesMemberVariable", "FieldNameHidesFieldInSuperclass", "OverlyComplexClass", "RedundantCast", "UnusedReturnValue", "LocalVariableHidesMemberVariable", "UnusedParameters", "EmptyMethod"})
public abstract class FormWithTableEntityWithOnChangeBase
    extends FormInstance<Void>
    implements LoggableInstance
{

    //~ Fields ...................................................................................................................

    @SuppressWarnings({"InstanceVariableMayNotBeInitialized", "UnusedDeclaration", "NullableProblems"}) @NotNull private UiModelAccessor f;

    //~ Methods ..................................................................................................................

    /** Invoked when populating a form instance */
    @NotNull public final Void populate() {
        throw new FormCannotBePopulatedException("tekgenesis.showcase.FormWithTableEntityWithOnChange");
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

    /** Returns a {@link FormTable<TableRow>} instance to handle Table manipulation */
    @NotNull public final FormTable<TableRow> getTable() { return table(Field.TABLE, TableRow.class); }

    /** Method to allow table row class override */
    @NotNull final <T> FormTable<T> table(@NotNull FormFieldRef field, @NotNull Class<T> rowClass) { return f.table(field, rowClass, this); }

    /** Called each time table(table) changes. */
    @NotNull public Action saveEntity() {
        if (forms.hasPermission(PredefinedPermission.CREATE)) {
        	final TableRow row = getTable().getCurrent();
        	final Integer primaryKey = row.getTableId();
        	final TableEntity currentTableEntity = primaryKey != null ? Predefined.ensureNotNull(TableEntity.find(primaryKey), "'row.getTableId()' not found") : TableEntity.create();
        	row.copyTo(currentTableEntity);
        	currentTableEntity.persist();
        	row.setTableId(currentTableEntity.getId());
        }
        return actions().getDefault();
    }

    /** Register a form Listener */
    public static void addListener(@NotNull FormListenerType listenerType, @NotNull FormListener<FormWithTableEntityWithOnChange> listener) { listeners.addListener(listenerType, listener); }

    /** Remove a form Listener */
    public static void removeListener(@NotNull FormListenerType listenerType, @NotNull FormListener<FormWithTableEntityWithOnChange> listener) { listeners.removeListener(listenerType, listener); }

    @Override @NotNull public Logger logger() { return logger; }

    /** Returns utility methods to deal with {@link Action actions}. */
    @NotNull protected final Actions actions() { return actions; }

    //~ Fields ...................................................................................................................

    @NotNull private static FormListenerMap<FormWithTableEntityWithOnChange> listeners = new FormListenerMap<>();
    @NotNull private static final Logger logger = Logger.getLogger(FormWithTableEntityWithOnChange.class);

    //~ Inner Classes ............................................................................................................

    public abstract class TableRowBase
        implements FormRowInstance<TableRow>
    {

        //~ Fields ...................................................................................................................

        @SuppressWarnings({"InstanceVariableMayNotBeInitialized", "UnusedDeclaration", "NullableProblems"}) @NotNull private UiModelAccessor f;

        //~ Methods ..................................................................................................................

        /** Returns the value of the internal(tableId). */
        @Nullable public Integer getTableId() { return f.get(Field.TABLE_ID, Integer.class); }

        /** Sets the value of the internal(tableId). */
        @NotNull public TableRow setTableId(@Nullable Integer tableId) {
            f.set(Field.TABLE_ID, tableId);
            return (TableRow) this;
        }

        /** Returns the value of the text_field(tableA). */
        @NotNull public String getTableA() { return f.get(Field.TABLE_A, String.class); }

        /** Sets the value of the text_field(tableA). */
        @NotNull public TableRow setTableA(@NotNull String tableA) {
            f.set(Field.TABLE_A, tableA);
            return (TableRow) this;
        }

        /** Returns the value of the text_field(tableB). */
        public int getTableB() { return f.get(Field.TABLE_B, Integer.class); }

        /** Sets the value of the text_field(tableB). */
        @NotNull public TableRow setTableB(int tableB) {
            f.set(Field.TABLE_B, tableB);
            return (TableRow) this;
        }

        /** Returns the value of the text_field(tableC). */
        public double getTableC() { return f.get(Field.TABLE_C, Double.class); }

        /** Sets the value of the text_field(tableC). */
        @NotNull public TableRow setTableC(double tableC) {
            f.set(Field.TABLE_C, tableC);
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

        /** Remove row from table and delete associated TableEntity instance. */
        @NotNull public Action remove() {
            Transaction.runInTransaction( () -> {
            	final TableEntity instance = isDefined(Field.TABLE_ID) ? TableEntity.find(keyAsString()) : null;
            	if (instance != null) instance.delete();
            });
            table().remove((TableRow) this);
            return actions().getDefault();
        }

        /** Returns a typed configuration for a given field. */
        @NotNull protected <T extends WidgetConfiguration> T configuration(@NotNull Field field) { return f.config(field); }

        /** Populate field values with given tableEntity instance. */
        public void populate(@NotNull TableEntity tableEntity) {
            setTableId(tableEntity.getId())
            	.setTableA(tableEntity.getMoe())
            	.setTableB(tableEntity.getLarry())
            	.setTableC(tableEntity.getCurly());
        }

        /** Copies field values to given tableEntity instance. */
        public void copyTo(@NotNull TableEntity tableEntity) {
            tableEntity.setMoe(getTableA())
            	.setLarry(getTableB())
            	.setCurly(getTableC());
        }

        /** Return primary key of bound {@link TableEntity} */
        @NotNull public String keyAsString() { return "" + getTableId(); }

    }

    @SuppressWarnings("UnnecessarySemicolon")
    public enum Field
        implements tekgenesis.metadata.form.widget.FormFieldRef
    {
        TABLE("table"),
        TABLE_ID("tableId"),
        TABLE_A("tableA"),
        TABLE_B("tableB"),
        TABLE_C("tableC");

        //~ Fields ...................................................................................................................

        @Nullable private final String id;

        //~ Constructors .............................................................................................................

        Field(@Nullable final String id) { this.id = id; }

        //~ Methods ..................................................................................................................

        @NotNull public String id() { return id; }

    }
}
