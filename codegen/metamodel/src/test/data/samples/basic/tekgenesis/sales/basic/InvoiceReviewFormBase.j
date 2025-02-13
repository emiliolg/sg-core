package tekgenesis.sales.basic;

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
import tekgenesis.sales.basic.InvoiceReviewForm.InvoicesRow;
import tekgenesis.common.logging.LoggableInstance;
import tekgenesis.common.logging.Logger;
import tekgenesis.form.Message;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import tekgenesis.form.UiModelAccessor;
import tekgenesis.form.configuration.WidgetConfiguration;

/** 
 * Generated base class for form: InvoiceReviewForm.
 * Don't modify this as this is an auto generated class that's gets generated
 * every time the meta model file is modified. Use subclass instead.
 */
@SuppressWarnings({"DuplicateStringLiteralInspection", "WeakerAccess", "FieldMayBeFinal", "FieldCanBeLocal", "ConstantConditions", "ClassWithTooManyMethods", "ClassWithTooManyFields", "MagicNumber", "MethodOverridesStaticMethodOfSuperclass", "ParameterHidesMemberVariable", "FieldNameHidesFieldInSuperclass", "OverlyComplexClass", "RedundantCast", "UnusedReturnValue", "LocalVariableHidesMemberVariable", "UnusedParameters", "EmptyMethod"})
public abstract class InvoiceReviewFormBase
    extends FormInstance<Void>
    implements LoggableInstance
{

    //~ Fields ...................................................................................................................

    @SuppressWarnings({"InstanceVariableMayNotBeInitialized", "UnusedDeclaration", "NullableProblems"}) @NotNull private UiModelAccessor f;

    //~ Methods ..................................................................................................................

    /** Invoked when populating a form instance */
    @NotNull public final Void populate() {
        throw new FormCannotBePopulatedException("tekgenesis.sales.basic.InvoiceReviewForm");
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

    /** Returns a {@link FormTable<InvoicesRow>} instance to handle Invoices manipulation */
    @NotNull public final FormTable<InvoicesRow> getInvoices() { return table(Field.INVOICES, InvoicesRow.class); }

    /** Method to allow table row class override */
    @NotNull final <T> FormTable<T> table(@NotNull FormFieldRef field, @NotNull Class<T> rowClass) { return f.table(field, rowClass, this); }

    /** Register a form Listener */
    public static void addListener(@NotNull FormListenerType listenerType, @NotNull FormListener<InvoiceReviewForm> listener) { listeners.addListener(listenerType, listener); }

    /** Remove a form Listener */
    public static void removeListener(@NotNull FormListenerType listenerType, @NotNull FormListener<InvoiceReviewForm> listener) { listeners.removeListener(listenerType, listener); }

    @Override @NotNull public Logger logger() { return logger; }

    /** Returns utility methods to deal with {@link Action actions}. */
    @NotNull protected final Actions actions() { return actions; }

    //~ Fields ...................................................................................................................

    @NotNull private static FormListenerMap<InvoiceReviewForm> listeners = new FormListenerMap<>();
    @NotNull private static final Logger logger = Logger.getLogger(InvoiceReviewForm.class);

    //~ Inner Classes ............................................................................................................

    public abstract class InvoicesRowBase
        implements FormRowInstance<InvoicesRow>
    {

        //~ Fields ...................................................................................................................

        @SuppressWarnings({"InstanceVariableMayNotBeInitialized", "UnusedDeclaration", "NullableProblems"}) @NotNull private UiModelAccessor f;

        //~ Methods ..................................................................................................................

        /** Invoked when display(id) is clicked */
        @NotNull public abstract Action navigate();

        /** Returns the value of the display(id). */
        public int getId() { return f.get(Field.ID, Integer.class); }

        /** Sets the value of the display(id). */
        @NotNull public InvoicesRow setId(int id) {
            f.set(Field.ID, id);
            return (InvoicesRow) this;
        }

        /** Create and set a new ItemsForm instance */
        @NotNull public ItemsForm createItems() { return f.init(Field.ITEMS, ItemsForm.class); }

        /** Create and populates set a new ItemsForm instance with a pk */
        @NotNull public ItemsForm createItems(@NotNull String key) { return f.init(Field.ITEMS, ItemsForm.class, key); }

        /** 
         * Get the ItemsForm if defined, or null otherwise.
         * @see #createItems
         */
        @Nullable public ItemsForm getItems() { return f.subform(Field.ITEMS, ItemsForm.class); }

        /** Create and set a new PaymentsForm instance */
        @NotNull public PaymentsForm createPayments() { return f.init(Field.PAYMENTS, PaymentsForm.class); }

        /** Create and populates set a new PaymentsForm instance with a pk */
        @NotNull public PaymentsForm createPayments(@NotNull String key) { return f.init(Field.PAYMENTS, PaymentsForm.class, key); }

        /** 
         * Get the PaymentsForm if defined, or null otherwise.
         * @see #createPayments
         */
        @Nullable public PaymentsForm getPayments() { return f.subform(Field.PAYMENTS, PaymentsForm.class); }

        /** Create and set a new CustomerReviewForm instance */
        @NotNull public CustomerReviewForm createCustomer() { return f.init(Field.CUSTOMER, CustomerReviewForm.class); }

        /** Create and populates set a new CustomerReviewForm instance with a pk */
        @NotNull public CustomerReviewForm createCustomer(@NotNull String key) {
            return f.init(Field.CUSTOMER, CustomerReviewForm.class, key);
        }

        /** 
         * Get the CustomerReviewForm if defined, or null otherwise.
         * @see #createCustomer
         */
        @Nullable public CustomerReviewForm getCustomer() { return f.subform(Field.CUSTOMER, CustomerReviewForm.class); }

        /** Returns the value of the date_box(date). */
        @NotNull public DateOnly getDate() {
            return DateOnly.fromMilliseconds(f.get(Field.DATE, Long.class));
        }

        /** Sets the value of the date_box(date). */
        @NotNull public InvoicesRow setDate(@NotNull DateOnly date) {
            f.set(Field.DATE, date);
            return (InvoicesRow) this;
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
        @NotNull public final FormTable<InvoicesRow> table() { return getInvoices(); }

        /** Remove row from table. */
        @NotNull public Action remove() {
            table().remove((InvoicesRow) this);
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
        INVOICES("invoices"),
        ID("id"),
        ITEMS("items"),
        PAYMENTS("payments"),
        CUSTOMER("customer"),
        DATE("date");

        //~ Fields ...................................................................................................................

        @Nullable private final String id;

        //~ Constructors .............................................................................................................

        Field(@Nullable final String id) { this.id = id; }

        //~ Methods ..................................................................................................................

        @NotNull public String id() { return id; }

    }
}
