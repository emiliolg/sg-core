package tekgenesis.sales.basic;

import tekgenesis.form.Action;
import tekgenesis.form.Actions;
import java.math.BigDecimal;
import tekgenesis.common.util.Conversions;
import tekgenesis.common.core.Decimals;
import tekgenesis.form.exception.EntityInstanceNotFoundException;
import tekgenesis.form.FormInstance;
import tekgenesis.form.extension.FormListener;
import tekgenesis.form.extension.FormListenerMap;
import tekgenesis.form.extension.FormListenerType;
import tekgenesis.model.KeyMap;
import tekgenesis.common.logging.LoggableInstance;
import tekgenesis.common.logging.Logger;
import tekgenesis.form.Message;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import tekgenesis.form.UiModelAccessor;
import tekgenesis.form.configuration.WidgetConfiguration;
import static tekgenesis.common.core.Strings.splitToArray;

/** 
 * Generated base class for form: CustomerHistoryForm.
 * Don't modify this as this is an auto generated class that's gets generated
 * every time the meta model file is modified. Use subclass instead.
 */
@SuppressWarnings({"DuplicateStringLiteralInspection", "WeakerAccess", "FieldMayBeFinal", "FieldCanBeLocal", "ConstantConditions", "ClassWithTooManyMethods", "ClassWithTooManyFields", "MagicNumber", "MethodOverridesStaticMethodOfSuperclass", "ParameterHidesMemberVariable", "FieldNameHidesFieldInSuperclass", "OverlyComplexClass", "RedundantCast", "UnusedReturnValue", "LocalVariableHidesMemberVariable", "UnusedParameters", "EmptyMethod"})
public abstract class CustomerHistoryFormBase
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
    @NotNull public Customer populate() {
        final Customer customer = find();

        setFirstName(customer.getFirstName())
        	.setLastName(customer.getLastName());

        return customer;
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

    /** Copies field values to given customer instance. */
    public void copyTo(@NotNull Customer customer) {
        customer.setFirstName(getFirstName())
        	.setLastName(getLastName());
    }

    /** Returns the value of the combo_box(documentType). */
    @NotNull public DocType getDocumentType() {
        return DocType.valueOf(f.get(Field.DOCUMENT_TYPE, String.class));
    }

    /** Sets the value of the combo_box(documentType). */
    @NotNull public CustomerHistoryForm setDocumentType(@NotNull DocType documentType) {
        f.set(Field.DOCUMENT_TYPE, documentType);
        return (CustomerHistoryForm) this;
    }

    /** Sets the options of the combo_box(documentType). */
    public void setDocumentTypeOptions(@NotNull Iterable<DocType> items) { f.opts(Field.DOCUMENT_TYPE, items); }

    /** Sets the options of the combo_box(documentType) with the given KeyMap. */
    public void setDocumentTypeOptions(@NotNull KeyMap items) { f.opts(Field.DOCUMENT_TYPE, items); }

    /** Returns the value of the text_field(documentId). */
    @NotNull public BigDecimal getDocumentId() { return f.get(Field.DOCUMENT_ID, BigDecimal.class); }

    /** Sets the value of the text_field(documentId). */
    @NotNull public CustomerHistoryForm setDocumentId(@NotNull BigDecimal documentId) {
        f.set(Field.DOCUMENT_ID, Decimals.scaleAndCheck("documentId", documentId, false, 10, 0));
        return (CustomerHistoryForm) this;
    }

    /** Returns the value of the combo_box(sex). */
    @NotNull public Sex getSex() { return Sex.valueOf(f.get(Field.SEX, String.class)); }

    /** Sets the value of the combo_box(sex). */
    @NotNull public CustomerHistoryForm setSex(@NotNull Sex sex) {
        f.set(Field.SEX, sex);
        return (CustomerHistoryForm) this;
    }

    /** Sets the options of the combo_box(sex). */
    public void setSexOptions(@NotNull Iterable<Sex> items) { f.opts(Field.SEX, items); }

    /** Sets the options of the combo_box(sex) with the given KeyMap. */
    public void setSexOptions(@NotNull KeyMap items) { f.opts(Field.SEX, items); }

    /** Returns the value of the text_field(firstName). */
    @NotNull public String getFirstName() { return f.get(Field.FIRST_NAME, String.class); }

    /** Sets the value of the text_field(firstName). */
    @NotNull public CustomerHistoryForm setFirstName(@NotNull String firstName) {
        f.set(Field.FIRST_NAME, firstName);
        return (CustomerHistoryForm) this;
    }

    /** Returns the value of the text_field(lastName). */
    @NotNull public String getLastName() { return f.get(Field.LAST_NAME, String.class); }

    /** Sets the value of the text_field(lastName). */
    @NotNull public CustomerHistoryForm setLastName(@NotNull String lastName) {
        f.set(Field.LAST_NAME, lastName);
        return (CustomerHistoryForm) this;
    }

    /** Create and set a new CustomerInvoicesForm instance */
    @NotNull public CustomerInvoicesForm createInvoices() { return f.init(Field.INVOICES, CustomerInvoicesForm.class); }

    /** Create and populates set a new CustomerInvoicesForm instance with a pk */
    @NotNull public CustomerInvoicesForm createInvoices(@NotNull String key) {
        return f.init(Field.INVOICES, CustomerInvoicesForm.class, key);
    }

    /** 
     * Get the CustomerInvoicesForm if defined, or null otherwise.
     * @see #createInvoices
     */
    @Nullable public CustomerInvoicesForm getInvoices() {
        return f.subform(Field.INVOICES, CustomerInvoicesForm.class);
    }

    /** Invoked when label(edit) on subform(invoices) is clicked */
    @NotNull public abstract Action editInvoice();

    /** Register a form Listener */
    public static void addListener(@NotNull FormListenerType listenerType, @NotNull FormListener<CustomerHistoryForm> listener) { listeners.addListener(listenerType, listener); }

    /** Remove a form Listener */
    public static void removeListener(@NotNull FormListenerType listenerType, @NotNull FormListener<CustomerHistoryForm> listener) { listeners.removeListener(listenerType, listener); }

    @Override @NotNull public Logger logger() { return logger; }

    /** Returns utility methods to deal with {@link Action actions}. */
    @NotNull protected final Actions actions() { return actions; }

    //~ Fields ...................................................................................................................

    @NotNull private static FormListenerMap<CustomerHistoryForm> listeners = new FormListenerMap<>();
    @NotNull private static final Logger logger = Logger.getLogger(CustomerHistoryForm.class);

    //~ Inner Classes ............................................................................................................

    @SuppressWarnings("UnnecessarySemicolon")
    public enum Field
        implements tekgenesis.metadata.form.widget.FormFieldRef
    {
        DOCUMENT_TYPE("documentType"),
        DOCUMENT_ID("documentId"),
        SEX("sex"),
        FIRST_NAME("firstName"),
        LAST_NAME("lastName"),
        INVOICES("invoices");

        //~ Fields ...................................................................................................................

        @Nullable private final String id;

        //~ Constructors .............................................................................................................

        Field(@Nullable final String id) { this.id = id; }

        //~ Methods ..................................................................................................................

        @NotNull public String id() { return id; }

    }
}
