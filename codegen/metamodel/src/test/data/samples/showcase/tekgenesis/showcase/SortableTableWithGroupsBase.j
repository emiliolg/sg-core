package tekgenesis.showcase;

import tekgenesis.form.Action;
import tekgenesis.form.Actions;
import java.math.BigDecimal;
import tekgenesis.common.core.DateTime;
import tekgenesis.common.core.Decimals;
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
import tekgenesis.showcase.SortableTableWithGroups.SalesRow;
import tekgenesis.form.UiModelAccessor;
import tekgenesis.form.configuration.WidgetConfiguration;

/** 
 * Generated base class for form: SortableTableWithGroups.
 * Don't modify this as this is an auto generated class that's gets generated
 * every time the meta model file is modified. Use subclass instead.
 */
@SuppressWarnings({"DuplicateStringLiteralInspection", "WeakerAccess", "FieldMayBeFinal", "FieldCanBeLocal", "ConstantConditions", "ClassWithTooManyMethods", "ClassWithTooManyFields", "MagicNumber", "MethodOverridesStaticMethodOfSuperclass", "ParameterHidesMemberVariable", "FieldNameHidesFieldInSuperclass", "OverlyComplexClass", "RedundantCast", "UnusedReturnValue", "LocalVariableHidesMemberVariable", "UnusedParameters", "EmptyMethod"})
public abstract class SortableTableWithGroupsBase
    extends FormInstance<Void>
    implements LoggableInstance
{

    //~ Fields ...................................................................................................................

    @SuppressWarnings({"InstanceVariableMayNotBeInitialized", "UnusedDeclaration", "NullableProblems"}) @NotNull private UiModelAccessor f;

    //~ Methods ..................................................................................................................

    /** Invoked when populating a form instance */
    @NotNull public final Void populate() {
        throw new FormCannotBePopulatedException("tekgenesis.showcase.SortableTableWithGroups");
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

    /** Returns a {@link FormTable<SalesRow>} instance to handle Sales manipulation */
    @NotNull public final FormTable<SalesRow> getSales() { return table(Field.SALES, SalesRow.class); }

    /** Method to allow table row class override */
    @NotNull final <T> FormTable<T> table(@NotNull FormFieldRef field, @NotNull Class<T> rowClass) { return f.table(field, rowClass, this); }

    /** Register a form Listener */
    public static void addListener(@NotNull FormListenerType listenerType, @NotNull FormListener<SortableTableWithGroups> listener) { listeners.addListener(listenerType, listener); }

    /** Remove a form Listener */
    public static void removeListener(@NotNull FormListenerType listenerType, @NotNull FormListener<SortableTableWithGroups> listener) { listeners.removeListener(listenerType, listener); }

    @Override @NotNull public Logger logger() { return logger; }

    /** Returns utility methods to deal with {@link Action actions}. */
    @NotNull protected final Actions actions() { return actions; }

    //~ Fields ...................................................................................................................

    @NotNull private static FormListenerMap<SortableTableWithGroups> listeners = new FormListenerMap<>();
    @NotNull private static final Logger logger = Logger.getLogger(SortableTableWithGroups.class);

    //~ Inner Classes ............................................................................................................

    public abstract class SalesRowBase
        implements FormRowInstance<SalesRow>
    {

        //~ Fields ...................................................................................................................

        @SuppressWarnings({"InstanceVariableMayNotBeInitialized", "UnusedDeclaration", "NullableProblems"}) @NotNull private UiModelAccessor f;

        //~ Methods ..................................................................................................................

        /** Returns the value of the display(id). */
        public int getId() { return f.get(Field.ID, Integer.class); }

        /** Sets the value of the display(id). */
        @NotNull public SalesRow setId(int id) {
            f.set(Field.ID, id);
            return (SalesRow) this;
        }

        /** Returns the value of the display(session). */
        @NotNull public String getSession() { return f.get(Field.SESSION, String.class); }

        /** Sets the value of the display(session). */
        @NotNull public SalesRow setSession(@NotNull String session) {
            f.set(Field.SESSION, session);
            return (SalesRow) this;
        }

        /** Returns the value of the display(first). */
        @NotNull public String getFirst() { return f.get(Field.FIRST, String.class); }

        /** Sets the value of the display(first). */
        @NotNull public SalesRow setFirst(@NotNull String first) {
            f.set(Field.FIRST, first);
            return (SalesRow) this;
        }

        /** Returns the value of the display(last). */
        @NotNull public String getLast() { return f.get(Field.LAST, String.class); }

        /** Sets the value of the display(last). */
        @NotNull public SalesRow setLast(@NotNull String last) {
            f.set(Field.LAST, last);
            return (SalesRow) this;
        }

        /** Returns the value of the display(email). */
        @NotNull public String getEmail() { return f.get(Field.EMAIL, String.class); }

        /** Sets the value of the display(email). */
        @NotNull public SalesRow setEmail(@NotNull String email) {
            f.set(Field.EMAIL, email);
            return (SalesRow) this;
        }

        /** Returns the value of the display(fullfillment). */
        @NotNull public Options getFullfillment() {
            return Options.valueOf(f.get(Field.FULLFILLMENT, String.class));
        }

        /** Sets the value of the display(fullfillment). */
        @NotNull public SalesRow setFullfillment(@NotNull Options fullfillment) {
            f.set(Field.FULLFILLMENT, fullfillment);
            return (SalesRow) this;
        }

        /** Returns the value of the display(amount). */
        @NotNull public BigDecimal getAmount() { return f.get(Field.AMOUNT, BigDecimal.class); }

        /** Sets the value of the display(amount). */
        @NotNull public SalesRow setAmount(@NotNull BigDecimal amount) {
            f.set(Field.AMOUNT, Decimals.scaleAndCheck("amount", amount, false, 9, 2));
            return (SalesRow) this;
        }

        /** Returns the value of the display(sync). */
        public boolean isSync() { return f.get(Field.SYNC, Boolean.class); }

        /** Sets the value of the display(sync). */
        @NotNull public SalesRow setSync(boolean sync) {
            f.set(Field.SYNC, sync);
            return (SalesRow) this;
        }

        /** Returns the value of the display(updateTime). */
        @NotNull public DateTime getUpdateTime() {
            return DateTime.fromMilliseconds(f.get(Field.UPDATE_TIME, Long.class));
        }

        /** Sets the value of the display(updateTime). */
        @NotNull public SalesRow setUpdateTime(@NotNull DateTime updateTime) {
            f.set(Field.UPDATE_TIME, updateTime);
            return (SalesRow) this;
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
        @NotNull public final FormTable<SalesRow> table() { return getSales(); }

        /** Remove row from table. */
        @NotNull public Action remove() {
            table().remove((SalesRow) this);
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
        SALES("sales"),
        SALE_ID("saleId"),
        ID("id"),
        SESSION("session"),
        USER("user"),
        FIRST("first"),
        LAST("last"),
        EMAIL("email"),
        FULLFILLMENT("fullfillment"),
        AMOUNT("amount"),
        SYNC("sync"),
        UPDATE_TIME("updateTime");

        //~ Fields ...................................................................................................................

        @Nullable private final String id;

        //~ Constructors .............................................................................................................

        Field(@Nullable final String id) { this.id = id; }

        //~ Methods ..................................................................................................................

        @NotNull public String id() { return id; }

    }
}
