package tekgenesis.test;

import tekgenesis.form.Action;
import tekgenesis.form.Actions;
import tekgenesis.common.util.Conversions;
import tekgenesis.form.exception.EntityInstanceNotFoundException;
import tekgenesis.form.FormInstance;
import tekgenesis.form.extension.FormListener;
import tekgenesis.form.extension.FormListenerMap;
import tekgenesis.form.extension.FormListenerType;
import tekgenesis.common.logging.LoggableInstance;
import tekgenesis.common.logging.Logger;
import tekgenesis.form.Message;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import tekgenesis.form.OptionalWidget;
import tekgenesis.form.UiModelAccessor;
import tekgenesis.form.configuration.WidgetConfiguration;

/** 
 * Generated base class for form: CustomerForm.
 * Don't modify this as this is an auto generated class that's gets generated
 * every time the meta model file is modified. Use subclass instead.
 */
@SuppressWarnings({"DuplicateStringLiteralInspection", "WeakerAccess", "FieldMayBeFinal", "FieldCanBeLocal", "ConstantConditions", "ClassWithTooManyMethods", "ClassWithTooManyFields", "MagicNumber", "MethodOverridesStaticMethodOfSuperclass", "ParameterHidesMemberVariable", "FieldNameHidesFieldInSuperclass", "OverlyComplexClass", "RedundantCast", "UnusedReturnValue", "LocalVariableHidesMemberVariable", "UnusedParameters", "EmptyMethod"})
public abstract class CustomerFormBase
    extends FormInstance<Customer>
    implements LoggableInstance
{

    //~ Fields ...................................................................................................................

    @SuppressWarnings({"InstanceVariableMayNotBeInitialized", "UnusedDeclaration", "NullableProblems"}) @NotNull private UiModelAccessor f;

    //~ Methods ..................................................................................................................

    /** Invoked when creating a form instance */
    @NotNull public Action create() {
        final Customer customer = Customer.create(getDoc());
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

    public void setPrimaryKey(@NotNull String key) { f.set(Field.DOC, Conversions.toInt(key)); }

    @NotNull public String keyAsString() { return "" + getDoc(); }

    /** Invoked when populating a form instance */
    @NotNull public Customer populate() {
        final Customer customer = find();

        setFirst(customer.getFirst())
        	.setLast(customer.getLast());

        getHomeAddress().populate(customer.getHomeAddress());

        getWorkAddress().populate(customer.getWorkAddress(), AddressWidget::populate);

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
        customer.setFirst(getFirst())
        	.setLast(getLast());

        getHomeAddress().copyTo(customer.getHomeAddress());

        getWorkAddress().copyTo(customer.getWorkAddress(), AddressWidget::copyTo, customer::setWorkAddress);
    }

    /** Returns the value of the internal(doc). */
    public int getDoc() { return f.get(Field.DOC, Integer.class); }

    /** Sets the value of the internal(doc). */
    @NotNull public CustomerForm setDoc(int doc) {
        f.set(Field.DOC, doc);
        return (CustomerForm) this;
    }

    /** Returns the value of the text_field(first). */
    @NotNull public String getFirst() { return f.get(Field.FIRST, String.class); }

    /** Sets the value of the text_field(first). */
    @NotNull public CustomerForm setFirst(@NotNull String first) {
        f.set(Field.FIRST, first);
        return (CustomerForm) this;
    }

    /** Returns the value of the text_field(last). */
    @NotNull public String getLast() { return f.get(Field.LAST, String.class); }

    /** Sets the value of the text_field(last). */
    @NotNull public CustomerForm setLast(@NotNull String last) {
        f.set(Field.LAST, last);
        return (CustomerForm) this;
    }

    /** Define {@link AddressWidget instance} to be used during interaction. */
    @NotNull AddressWidget defineHomeAddress() { return new AddressWidget(); }

    /** Get widget {@link AddressWidget}. */
    @NotNull public AddressWidget getHomeAddress() { return f.widget(Field.HOME_ADDRESS, AddressWidget.class); }

    /** Define {@link AddressWidget instance} to be used during interaction. */
    @NotNull AddressWidget defineWorkAddress() { return new AddressWidget(); }

    /** Get widget {@link AddressWidget}. */
    @NotNull public OptionalWidget<AddressWidget> getWorkAddress() {
        return f.optionalWidget(Field.WORK_ADDRESS, AddressWidget.class);
    }

    /** Register a form Listener */
    public static void addListener(@NotNull FormListenerType listenerType, @NotNull FormListener<CustomerForm> listener) { listeners.addListener(listenerType, listener); }

    /** Remove a form Listener */
    public static void removeListener(@NotNull FormListenerType listenerType, @NotNull FormListener<CustomerForm> listener) { listeners.removeListener(listenerType, listener); }

    @Override @NotNull public Logger logger() { return logger; }

    /** Returns utility methods to deal with {@link Action actions}. */
    @NotNull protected final Actions actions() { return actions; }

    //~ Fields ...................................................................................................................

    @NotNull private static FormListenerMap<CustomerForm> listeners = new FormListenerMap<>();
    @NotNull private static final Logger logger = Logger.getLogger(CustomerForm.class);

    //~ Inner Classes ............................................................................................................

    @SuppressWarnings("UnnecessarySemicolon")
    public enum Field
        implements tekgenesis.metadata.form.widget.FormFieldRef
    {
        DOC("doc"),
        FIRST("first"),
        LAST("last"),
        HOME_ADDRESS("homeAddress"),
        WORK_ADDRESS("workAddress");

        //~ Fields ...................................................................................................................

        @Nullable private final String id;

        //~ Constructors .............................................................................................................

        Field(@Nullable final String id) { this.id = id; }

        //~ Methods ..................................................................................................................

        @NotNull public String id() { return id; }

    }
}
