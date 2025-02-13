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
import tekgenesis.sales.basic.InvoiceForm.ItemsRow;
import tekgenesis.model.KeyMap;
import tekgenesis.common.logging.LoggableInstance;
import tekgenesis.common.logging.Logger;
import tekgenesis.form.Message;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import tekgenesis.sales.basic.InvoiceForm.PaymentsRow;
import tekgenesis.common.Predefined;
import tekgenesis.form.UiModelAccessor;
import tekgenesis.form.configuration.WidgetConfiguration;

/** 
 * Generated base class for form: InvoiceForm.
 * Don't modify this as this is an auto generated class that's gets generated
 * every time the meta model file is modified. Use subclass instead.
 */
@SuppressWarnings({"DuplicateStringLiteralInspection", "WeakerAccess", "FieldMayBeFinal", "FieldCanBeLocal", "ConstantConditions", "ClassWithTooManyMethods", "ClassWithTooManyFields", "MagicNumber", "MethodOverridesStaticMethodOfSuperclass", "ParameterHidesMemberVariable", "FieldNameHidesFieldInSuperclass", "OverlyComplexClass", "RedundantCast", "UnusedReturnValue", "LocalVariableHidesMemberVariable", "UnusedParameters", "EmptyMethod"})
public abstract class InvoiceFormBase
    extends FormInstance<Invoice>
    implements LoggableInstance
{

    //~ Fields ...................................................................................................................

    @SuppressWarnings({"InstanceVariableMayNotBeInitialized", "UnusedDeclaration", "NullableProblems"}) @NotNull private UiModelAccessor f;

    //~ Methods ..................................................................................................................

    /** Invoked when creating a form instance */
    @NotNull public Action create() {
        final Invoice invoice = Invoice.create(getIdKey());
        copyTo(invoice);
        createOrUpdateItems(invoice);
        createOrUpdatePayments(invoice);
        invoice.insert();
        return actions().getDefault();
    }

    /** Invoked when updating a form instance */
    @NotNull public Action update() {
        final Invoice invoice = find();
        copyTo(invoice);
        createOrUpdateItems(invoice);
        createOrUpdatePayments(invoice);
        invoice.update();
        return actions().getDefault();
    }

    /** Invoked when deleting a form instance */
    @NotNull public Action delete() {
        find().delete();
        return actions().getDefault();
    }

    /** Invoked to find an entity instance */
    @NotNull public Invoice find() {
        final Invoice value = Invoice.find(keyAsString());
        if (value == null) {
        	throw new EntityInstanceNotFoundException(f.title(), keyAsString());
        }
        return value;
    }

    public void setPrimaryKey(@NotNull String key) { f.set(Field.ID_KEY, Conversions.toInt(key)); }

    @NotNull public String keyAsString() { return "" + getIdKey(); }

    /** Invoked when populating a form instance */
    @NotNull public Invoice populate() {
        final Invoice invoice = find();

        setInvoiceDate(invoice.getInvoiceDate())
        	.setCustomer(invoice.getCustomer());

        getItems().populate(invoice.getItems(), ItemsRow::populate);

        getPayments().populate(invoice.getPayments(), PaymentsRow::populate);

        return invoice;
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

    /** Copies field values to given invoice instance. */
    public void copyTo(@NotNull Invoice invoice) {
        invoice.setInvoiceDate(getInvoiceDate())
        	.setCustomer(getCustomer());
    }

    /** Updates external references to items. */
    public void createOrUpdateItems(@NotNull Invoice invoice) {
        invoice.getItems().merge(getItems(), (item, row) -> row.copyTo(item));
    }

    /** Updates external references to payments. */
    public void createOrUpdatePayments(@NotNull Invoice invoice) {
        invoice.getPayments().merge(getPayments(), (payment, row) -> row.copyTo(payment));
    }

    /** Returns the value of the text_field(idKey). */
    public int getIdKey() { return f.get(Field.ID_KEY, Integer.class); }

    /** Sets the value of the text_field(idKey). */
    @NotNull public InvoiceForm setIdKey(int idKey) {
        f.set(Field.ID_KEY, idKey);
        return (InvoiceForm) this;
    }

    /** Returns the value of the date_box(invoiceDate). */
    @NotNull public DateOnly getInvoiceDate() {
        return DateOnly.fromMilliseconds(f.get(Field.INVOICE_DATE, Long.class));
    }

    /** Sets the value of the date_box(invoiceDate). */
    @NotNull public InvoiceForm setInvoiceDate(@NotNull DateOnly invoiceDate) {
        f.set(Field.INVOICE_DATE, invoiceDate);
        return (InvoiceForm) this;
    }

    /** Invoked when the 'Create new' options of the suggest_box(customer) is clicked */
    @NotNull public abstract Action createCustomer(@Nullable String text);

    /** Invoked when suggest_box(customer) value changes */
    @NotNull public abstract Action updateCustomer();

    /** Returns the value of the suggest_box(customer). */
    @NotNull public Customer getCustomer() {
        return Predefined.ensureNotNull(Customer.find(getCustomerKey()), "'customer' not found");
    }

    /** Returns the key value of the suggest_box(customer). */
    @NotNull public String getCustomerKey() { return f.get(Field.CUSTOMER, String.class); }

    /** Sets the value of the suggest_box(customer). */
    @NotNull public InvoiceForm setCustomer(@NotNull Customer customer) {
        f.set(Field.CUSTOMER, customer);
        return (InvoiceForm) this;
    }

    /** Returns the value of the internal(disc). */
    public int getDisc() { return f.get(Field.DISC, Integer.class); }

    /** Sets the value of the internal(disc). */
    @NotNull public InvoiceForm setDisc(int disc) {
        f.set(Field.DISC, disc);
        return (InvoiceForm) this;
    }

    /** Returns a {@link FormTable<ItemsRow>} instance to handle Items manipulation */
    @NotNull public final FormTable<ItemsRow> getItems() { return table(Field.ITEMS, ItemsRow.class); }

    /** Method to allow table row class override */
    @NotNull final <T> FormTable<T> table(@NotNull FormFieldRef field, @NotNull Class<T> rowClass) { return f.table(field, rowClass, this); }

    /** Returns the value of the internal(subtotal). */
    @Nullable public BigDecimal getSubtotal() { return f.get(Field.SUBTOTAL, BigDecimal.class); }

    /** Returns a {@link FormTable<PaymentsRow>} instance to handle Payments manipulation */
    @NotNull public final FormTable<PaymentsRow> getPayments() { return table(Field.PAYMENTS, PaymentsRow.class); }

    /** Sets the options of the combo_box(payment). */
    public void setPaymentOptions(@NotNull Iterable<? extends PaymentType> items) { f.opts(Field.PAYMENT, items); }

    /** Sets the options of the combo_box(payment) with the given KeyMap. */
    public void setPaymentOptions(@NotNull KeyMap items) { f.opts(Field.PAYMENT, items); }

    /** Returns the value of the text_field(received). */
    @Nullable public BigDecimal getReceived() { return f.get(Field.RECEIVED, BigDecimal.class); }

    /** Register a form Listener */
    public static void addListener(@NotNull FormListenerType listenerType, @NotNull FormListener<InvoiceForm> listener) { listeners.addListener(listenerType, listener); }

    /** Remove a form Listener */
    public static void removeListener(@NotNull FormListenerType listenerType, @NotNull FormListener<InvoiceForm> listener) { listeners.removeListener(listenerType, listener); }

    @Override @NotNull public Logger logger() { return logger; }

    /** Returns utility methods to deal with {@link Action actions}. */
    @NotNull protected final Actions actions() { return actions; }

    //~ Fields ...................................................................................................................

    @NotNull private static FormListenerMap<InvoiceForm> listeners = new FormListenerMap<>();
    @NotNull private static final Logger logger = Logger.getLogger(InvoiceForm.class);

    //~ Inner Classes ............................................................................................................

    public abstract class ItemsRowBase
        implements FormRowInstance<ItemsRow>
    {

        //~ Fields ...................................................................................................................

        @SuppressWarnings({"InstanceVariableMayNotBeInitialized", "UnusedDeclaration", "NullableProblems"}) @NotNull private UiModelAccessor f;

        //~ Methods ..................................................................................................................

        /** Invoked when the 'Create new' options of the suggest_box(product) is clicked */
        @NotNull public abstract Action createProduct(@Nullable String text);

        /** Invoked when suggest_box(product) value changes */
        @NotNull public abstract Action updateUnitPrice();

        /** Returns the value of the suggest_box(product). */
        @NotNull public Product getProduct() {
            return Predefined.ensureNotNull(Product.find(getProductKey()), "'product' not found");
        }

        /** Returns the key value of the suggest_box(product). */
        @NotNull public String getProductKey() { return f.get(Field.PRODUCT, String.class); }

        /** Sets the value of the suggest_box(product). */
        @NotNull public ItemsRow setProduct(@NotNull Product product) {
            f.set(Field.PRODUCT, product);
            return (ItemsRow) this;
        }

        /** Returns the value of the text_field(unit). */
        @NotNull public BigDecimal getUnit() { return f.get(Field.UNIT, BigDecimal.class); }

        /** Sets the value of the text_field(unit). */
        @NotNull public ItemsRow setUnit(@NotNull BigDecimal unit) {
            f.set(Field.UNIT, Decimals.scaleAndCheck("unit", unit, false, 10, 2));
            return (ItemsRow) this;
        }

        /** Returns the value of the text_field(quantity). */
        public int getQuantity() { return f.get(Field.QUANTITY, Integer.class); }

        /** Sets the value of the text_field(quantity). */
        @NotNull public ItemsRow setQuantity(int quantity) {
            f.set(Field.QUANTITY, quantity);
            return (ItemsRow) this;
        }

        /** Returns the value of the text_field(discount). */
        public int getDiscount() { return f.get(Field.DISCOUNT, Integer.class); }

        /** Sets the value of the text_field(discount). */
        @NotNull public ItemsRow setDiscount(int discount) {
            f.set(Field.DISCOUNT, discount);
            return (ItemsRow) this;
        }

        /** Returns the value of the text_field(total). */
        @NotNull public BigDecimal getTotal() { return f.get(Field.TOTAL, BigDecimal.class); }

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

        /** Populate field values with given item instance. */
        public void populate(@NotNull Item item) {
            setProduct(item.getProduct())
            	.setQuantity(item.getQuantity())
            	.setDiscount(item.getDiscount());
        }

        /** Copies field values to given item instance. */
        public void copyTo(@NotNull Item item) {
            item.setProduct(getProduct())
            	.setQuantity(getQuantity())
            	.setDiscount(getDiscount());
        }

    }

    public abstract class PaymentsRowBase
        implements FormRowInstance<PaymentsRow>
    {

        //~ Fields ...................................................................................................................

        @SuppressWarnings({"InstanceVariableMayNotBeInitialized", "UnusedDeclaration", "NullableProblems"}) @NotNull private UiModelAccessor f;

        //~ Methods ..................................................................................................................

        /** Invoked when suggest_box(type) value changes */
        @NotNull public abstract Action updatePayments();

        /** Returns the value of the suggest_box(type). */
        @NotNull public PaymentOption getType() {
            return PaymentOption.valueOf(f.get(Field.TYPE, String.class));
        }

        /** Sets the value of the suggest_box(type). */
        @NotNull public PaymentsRow setType(@NotNull PaymentOption type) {
            f.set(Field.TYPE, type);
            return (PaymentsRow) this;
        }

        /** Returns the value of the combo_box(payment). */
        @NotNull public PaymentType getPayment() {
            return Predefined.ensureNotNull(PaymentType.find(getPaymentKey()), "'payment' not found");
        }

        /** Returns the key value of the combo_box(payment). */
        @NotNull public String getPaymentKey() { return f.get(Field.PAYMENT, String.class); }

        /** Sets the value of the combo_box(payment). */
        @NotNull public PaymentsRow setPayment(@NotNull PaymentType payment) {
            f.set(Field.PAYMENT, payment);
            return (PaymentsRow) this;
        }

        /** Sets the options of the combo_box(payment). */
        public void setPaymentOptions(@NotNull Iterable<? extends PaymentType> items) { f.opts(Field.PAYMENT, items); }

        /** Sets the options of the combo_box(payment) with the given KeyMap. */
        public void setPaymentOptions(@NotNull KeyMap items) { f.opts(Field.PAYMENT, items); }

        /** Returns the value of the text_field(amount). */
        @NotNull public BigDecimal getAmount() { return f.get(Field.AMOUNT, BigDecimal.class); }

        /** Sets the value of the text_field(amount). */
        @NotNull public PaymentsRow setAmount(@NotNull BigDecimal amount) {
            f.set(Field.AMOUNT, Decimals.scaleAndCheck("amount", amount, false, 10, 2));
            return (PaymentsRow) this;
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
        @NotNull public final FormTable<PaymentsRow> table() { return getPayments(); }

        /** Remove row from table. */
        @NotNull public Action remove() {
            table().remove((PaymentsRow) this);
            return actions().getDefault();
        }

        /** Returns a typed configuration for a given field. */
        @NotNull protected <T extends WidgetConfiguration> T configuration(@NotNull Field field) { return f.config(field); }

        /** Populate field values with given payment instance. */
        public void populate(@NotNull Payment payment) {
            setPayment(payment.getPayment())
            	.setAmount(payment.getAmount());
        }

        /** Copies field values to given payment instance. */
        public void copyTo(@NotNull Payment payment) {
            payment.setPayment(getPayment())
            	.setAmount(getAmount());
        }

    }

    @SuppressWarnings("UnnecessarySemicolon")
    public enum Permission
        implements tekgenesis.type.permission.Permission
    {
        DRAFT;

    }

    @SuppressWarnings("UnnecessarySemicolon")
    public enum Field
        implements tekgenesis.metadata.form.widget.FormFieldRef
    {
        $H0("$H0"),
        $M1("$M1"),
        ID_KEY("idKey"),
        INVOICE_DATE("invoiceDate"),
        CUSTOMER("customer"),
        DISC("disc"),
        ITEMS("items"),
        PRODUCT("product"),
        UNIT("unit"),
        QUANTITY("quantity"),
        DISCOUNT("discount"),
        TOTAL("total"),
        $H2("$H2"),
        $B3("$B3"),
        $B4("$B4"),
        SUBTOTAL("subtotal"),
        PAYMENTS("payments"),
        TYPE("type"),
        PAYMENT("payment"),
        AMOUNT("amount"),
        $H5("$H5"),
        $B6("$B6"),
        $B7("$B7"),
        RECEIVED("received"),
        $F8("$F8"),
        $B9("$B9"),
        $B10("$B10"),
        $B11("$B11"),
        $B12("$B12");

        //~ Fields ...................................................................................................................

        @Nullable private final String id;

        //~ Constructors .............................................................................................................

        Field(@Nullable final String id) { this.id = id; }

        //~ Methods ..................................................................................................................

        @NotNull public String id() { return id; }

    }
}
