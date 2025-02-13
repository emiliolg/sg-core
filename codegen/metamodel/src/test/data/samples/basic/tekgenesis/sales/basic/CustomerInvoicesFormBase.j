package tekgenesis.sales.basic;

import tekgenesis.form.Action;
import tekgenesis.form.Actions;
import java.math.BigDecimal;
import tekgenesis.common.util.Conversions;
import tekgenesis.common.core.DateOnly;
import tekgenesis.common.core.Decimals;
import tekgenesis.form.exception.EntityInstanceNotFoundException;
import tekgenesis.metadata.form.widget.FormFieldRef;
import tekgenesis.form.FormInstance;
import tekgenesis.form.extension.FormListener;
import tekgenesis.form.extension.FormListenerMap;
import tekgenesis.form.extension.FormListenerType;
import tekgenesis.form.FormRowInstance;
import tekgenesis.form.FormTable;
import tekgenesis.sales.basic.CustomerInvoicesForm.InvoicesRow;
import tekgenesis.model.KeyMap;
import tekgenesis.common.logging.LoggableInstance;
import tekgenesis.common.logging.Logger;
import tekgenesis.form.Message;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import tekgenesis.transaction.Transaction;
import tekgenesis.form.UiModelAccessor;
import tekgenesis.form.configuration.WidgetConfiguration;
import static tekgenesis.common.core.Strings.splitToArray;

/** 
 * Generated base class for form: CustomerInvoicesForm.
 * Don't modify this as this is an auto generated class that's gets generated
 * every time the meta model file is modified. Use subclass instead.
 */
@SuppressWarnings({"DuplicateStringLiteralInspection", "WeakerAccess", "FieldMayBeFinal", "FieldCanBeLocal", "ConstantConditions", "ClassWithTooManyMethods", "ClassWithTooManyFields", "MagicNumber", "MethodOverridesStaticMethodOfSuperclass", "ParameterHidesMemberVariable", "FieldNameHidesFieldInSuperclass", "OverlyComplexClass", "RedundantCast", "UnusedReturnValue", "LocalVariableHidesMemberVariable", "UnusedParameters", "EmptyMethod"})
public abstract class CustomerInvoicesFormBase
    extends FormInstance<Customer>
    implements LoggableInstance
{

    //~ Fields ...................................................................................................................

    @SuppressWarnings({"InstanceVariableMayNotBeInitialized", "UnusedDeclaration", "NullableProblems"}) @NotNull private UiModelAccessor f;

    //~ Methods ..................................................................................................................

    /** Invoked when creating a form instance */
    @NotNull public Action create() {
        final Customer customer = Customer.create(getDocumentType(), getDocumentId(), getSex());
        copyTo(customer);
        customer.insert();
        return actions().getDefault();
    }

    /** Invoked when updating a form instance */
    @NotNull public Action update() {
        final Customer customer = find();
        copyTo(customer);
        customer.update();
        return actions().getDefault();
    }

    /** Invoked when deleting a form instance */
    @NotNull public Action delete() {
        find().delete();
        return actions().getDefault();
    }

    /** Invoked to find an entity instance */
    @NotNull public Customer find() {
        final Customer value = Customer.find(keyAsString());
        if (value == null) {
        	throw new EntityInstanceNotFoundException(f.title(), keyAsString());
        }
        return value;
    }

    public final void setPrimaryKey(@NotNull String key) {
        final String[] parts = splitToArray(key, 3);
        f.set(Field.DOCUMENT_TYPE, DocType.valueOf(parts[0]));
        f.set(Field.DOCUMENT_ID, Conversions.toDecimal(parts[1]));
        f.set(Field.SEX, Sex.valueOf(parts[2]));
    }

    @NotNull public String keyAsString() {
        return "" + getDocumentType() + ":" + getDocumentId() + ":" + getSex();
    }

    /** Invoked when populating a form instance */
    @NotNull public Customer populate() { return find(); }

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

    /** Copies field values to given customer instance. */
    public void copyTo(@NotNull Customer customer) { }

    /** Returns the value of the combo_box(documentType). */
    @NotNull public DocType getDocumentType() {
        return DocType.valueOf(f.get(Field.DOCUMENT_TYPE, String.class));
    }

    /** Sets the value of the combo_box(documentType). */
    @NotNull public CustomerInvoicesForm setDocumentType(@NotNull DocType documentType) {
        f.set(Field.DOCUMENT_TYPE, documentType);
        return (CustomerInvoicesForm) this;
    }

    /** Sets the options of the combo_box(documentType). */
    public void setDocumentTypeOptions(@NotNull Iterable<DocType> items) { f.opts(Field.DOCUMENT_TYPE, items); }

    /** Sets the options of the combo_box(documentType) with the given KeyMap. */
    public void setDocumentTypeOptions(@NotNull KeyMap items) { f.opts(Field.DOCUMENT_TYPE, items); }

    /** Returns the value of the text_field(documentId). */
    @NotNull public BigDecimal getDocumentId() { return f.get(Field.DOCUMENT_ID, BigDecimal.class); }

    /** Sets the value of the text_field(documentId). */
    @NotNull public CustomerInvoicesForm setDocumentId(@NotNull BigDecimal documentId) {
        f.set(Field.DOCUMENT_ID, Decimals.scaleAndCheck("documentId", documentId, false, 10, 0));
        return (CustomerInvoicesForm) this;
    }

    /** Returns the value of the combo_box(sex). */
    @NotNull public Sex getSex() { return Sex.valueOf(f.get(Field.SEX, String.class)); }

    /** Sets the value of the combo_box(sex). */
    @NotNull public CustomerInvoicesForm setSex(@NotNull Sex sex) {
        f.set(Field.SEX, sex);
        return (CustomerInvoicesForm) this;
    }

    /** Sets the options of the combo_box(sex). */
    public void setSexOptions(@NotNull Iterable<Sex> items) { f.opts(Field.SEX, items); }

    /** Sets the options of the combo_box(sex) with the given KeyMap. */
    public void setSexOptions(@NotNull KeyMap items) { f.opts(Field.SEX, items); }

    /** Returns a {@link FormTable<InvoicesRow>} instance to handle Invoices manipulation */
    @NotNull public final FormTable<InvoicesRow> getInvoices() { return table(Field.INVOICES, InvoicesRow.class); }

    /** Method to allow table row class override */
    @NotNull final <T> FormTable<T> table(@NotNull FormFieldRef field, @NotNull Class<T> rowClass) { return f.table(field, rowClass, this); }

    /** Register a form Listener */
    public static void addListener(@NotNull FormListenerType listenerType, @NotNull FormListener<CustomerInvoicesForm> listener) { listeners.addListener(listenerType, listener); }

    /** Remove a form Listener */
    public static void removeListener(@NotNull FormListenerType listenerType, @NotNull FormListener<CustomerInvoicesForm> listener) { listeners.removeListener(listenerType, listener); }

    @Override @NotNull public Logger logger() { return logger; }

    /** Returns utility methods to deal with {@link Action actions}. */
    @NotNull protected final Actions actions() { return actions; }

    //~ Fields ...................................................................................................................

    @NotNull private static FormListenerMap<CustomerInvoicesForm> listeners = new FormListenerMap<>();
    @NotNull private static final Logger logger = Logger.getLogger(CustomerInvoicesForm.class);

    //~ Inner Classes ............................................................................................................

    public abstract class InvoicesRowBase
        implements FormRowInstance<InvoicesRow>
    {

        //~ Fields ...................................................................................................................

        @SuppressWarnings({"InstanceVariableMayNotBeInitialized", "UnusedDeclaration", "NullableProblems"}) @NotNull private UiModelAccessor f;

        //~ Methods ..................................................................................................................

        /** Returns the value of the display(idKey). */
        public int getIdKey() { return f.get(Field.ID_KEY, Integer.class); }

        /** Sets the value of the display(idKey). */
        @NotNull public InvoicesRow setIdKey(int idKey) {
            f.set(Field.ID_KEY, idKey);
            return (InvoicesRow) this;
        }

        /** Returns the value of the display(invoiceDate). */
        @NotNull public DateOnly getInvoiceDate() {
            return DateOnly.fromMilliseconds(f.get(Field.INVOICE_DATE, Long.class));
        }

        /** Sets the value of the display(invoiceDate). */
        @NotNull public InvoicesRow setInvoiceDate(@NotNull DateOnly invoiceDate) {
            f.set(Field.INVOICE_DATE, invoiceDate);
            return (InvoicesRow) this;
        }

        /** Returns the value of the text_field(items). */
        public int getItems() { return f.get(Field.ITEMS, Integer.class); }

        /** Sets the value of the text_field(items). */
        @NotNull public InvoicesRow setItems(int items) {
            f.set(Field.ITEMS, items);
            return (InvoicesRow) this;
        }

        /** Returns the value of the text_field(total). */
        @NotNull public BigDecimal getTotal() { return f.get(Field.TOTAL, BigDecimal.class); }

        /** Sets the value of the text_field(total). */
        @NotNull public InvoicesRow setTotal(@NotNull BigDecimal total) {
            f.set(Field.TOTAL, Decimals.scaleAndCheck("total", total, false, 10, 2));
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

        /** Remove row from table and delete associated Invoice instance. */
        @NotNull public Action remove() {
            Transaction.runInTransaction( () -> {
            	final Invoice instance = isDefined(Field.ID_KEY) ? Invoice.find(keyAsString()) : null;
            	if (instance != null) instance.delete();
            });
            table().remove((InvoicesRow) this);
            return actions().getDefault();
        }

        /** Returns a typed configuration for a given field. */
        @NotNull protected <T extends WidgetConfiguration> T configuration(@NotNull Field field) { return f.config(field); }

        /** Populate field values with given invoice instance. */
        public void populate(@NotNull Invoice invoice) {
            setInvoiceDate(invoice.getInvoiceDate());

            populateProtectedFields(invoice);
        }

        /** Invoked from {@link #populate(Invoice) method} to handle protected fields. */
        public abstract void populateProtectedFields(@NotNull Invoice invoice);

        /** Copies field values to given invoice instance. */
        public void copyTo(@NotNull Invoice invoice) { invoice.setInvoiceDate(getInvoiceDate()); }

        /** Return primary key of bound {@link Invoice} */
        @NotNull public String keyAsString() { return "" + getIdKey(); }

    }

    @SuppressWarnings("UnnecessarySemicolon")
    public enum Field
        implements tekgenesis.metadata.form.widget.FormFieldRef
    {
        DOCUMENT_TYPE("documentType"),
        DOCUMENT_ID("documentId"),
        SEX("sex"),
        INVOICES("invoices"),
        ID_KEY("idKey"),
        INVOICE_DATE("invoiceDate"),
        ITEMS("items"),
        TOTAL("total"),
        EDIT("edit");

        //~ Fields ...................................................................................................................

        @Nullable private final String id;

        //~ Constructors .............................................................................................................

        Field(@Nullable final String id) { this.id = id; }

        //~ Methods ..................................................................................................................

        @NotNull public String id() { return id; }

    }
}
