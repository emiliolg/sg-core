package tekgenesis.showcase;

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
        final Customer customer = Customer.create(getDocument());
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

    public void setPrimaryKey(@NotNull String key) { f.set(Field.DOCUMENT, Conversions.toInt(key)); }

    @NotNull public String keyAsString() { return "" + getDocument(); }

    /** Invoked when populating a form instance */
    @NotNull public Customer populate() {
        final Customer customer = find();

        setFirstName(customer.getFirstName())
        	.setLastName(customer.getLastName());

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
        customer.setFirstName(getFirstName())
        	.setLastName(getLastName());

        getHomeAddress().copyTo(customer.getHomeAddress());

        getWorkAddress().copyTo(customer.getWorkAddress(), AddressWidget::copyTo, customer::setWorkAddress);
    }

    /** Returns the value of the text_field(document). */
    public int getDocument() { return f.get(Field.DOCUMENT, Integer.class); }

    /** Sets the value of the text_field(document). */
    @NotNull public CustomerForm setDocument(int document) {
        f.set(Field.DOCUMENT, document);
        return (CustomerForm) this;
    }

    /** Returns the value of the text_field(firstName). */
    @NotNull public String getFirstName() { return f.get(Field.FIRST_NAME, String.class); }

    /** Sets the value of the text_field(firstName). */
    @NotNull public CustomerForm setFirstName(@NotNull String firstName) {
        f.set(Field.FIRST_NAME, firstName);
        return (CustomerForm) this;
    }

    /** Returns the value of the text_field(lastName). */
    @NotNull public String getLastName() { return f.get(Field.LAST_NAME, String.class); }

    /** Sets the value of the text_field(lastName). */
    @NotNull public CustomerForm setLastName(@NotNull String lastName) {
        f.set(Field.LAST_NAME, lastName);
        return (CustomerForm) this;
    }

    /** Returns the value of the display(home). */
    @NotNull public String getHome() { return f.get(Field.HOME, String.class); }

    /** Define {@link AddressWidget instance} to be used during interaction. */
    @NotNull abstract AddressWidget defineHomeAddress();

    /** Get widget {@link AddressWidget}. */
    @NotNull public AddressWidget getHomeAddress() { return f.widget(Field.HOME_ADDRESS, AddressWidget.class); }

    /** Returns the value of the display(work). */
    @NotNull public String getWork() { return f.get(Field.WORK, String.class); }

    /** Define {@link AddressWidget instance} to be used during interaction. */
    @NotNull abstract AddressWidget defineWorkAddress();

    /** Get widget {@link AddressWidget}. */
    @NotNull public OptionalWidget<AddressWidget> getWorkAddress() {
        return f.optionalWidget(Field.WORK_ADDRESS, AddressWidget.class);
    }

    /** Returns the value of the text_field(feedback). */
    @NotNull public String getFeedback() { return f.get(Field.FEEDBACK, String.class); }

    /** Sets the value of the text_field(feedback). */
    @NotNull public CustomerForm setFeedback(@NotNull String feedback) {
        f.set(Field.FEEDBACK, feedback);
        return (CustomerForm) this;
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
        $H0("$H0"),
        $M1("$M1"),
        DOCUMENT("document"),
        FIRST_NAME("firstName"),
        LAST_NAME("lastName"),
        HOME("home"),
        HOME_ADDRESS("homeAddress"),
        WORK("work"),
        WORK_ADDRESS("workAddress"),
        FEEDBACK("feedback"),
        $F2("$F2"),
        $B3("$B3"),
        $B4("$B4"),
        $B5("$B5");

        //~ Fields ...................................................................................................................

        @Nullable private final String id;

        //~ Constructors .............................................................................................................

        Field(@Nullable final String id) { this.id = id; }

        //~ Methods ..................................................................................................................

        @NotNull public String id() { return id; }

    }
}
