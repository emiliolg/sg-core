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
import tekgenesis.form.UiModelAccessor;
import tekgenesis.form.configuration.WidgetConfiguration;

/** 
 * Generated base class for form: AddressForm.
 * Don't modify this as this is an auto generated class that's gets generated
 * every time the meta model file is modified. Use subclass instead.
 */
@SuppressWarnings({"DuplicateStringLiteralInspection", "WeakerAccess", "FieldMayBeFinal", "FieldCanBeLocal", "ConstantConditions", "ClassWithTooManyMethods", "ClassWithTooManyFields", "MagicNumber", "MethodOverridesStaticMethodOfSuperclass", "ParameterHidesMemberVariable", "FieldNameHidesFieldInSuperclass", "OverlyComplexClass", "RedundantCast", "UnusedReturnValue", "LocalVariableHidesMemberVariable", "UnusedParameters", "EmptyMethod"})
public abstract class AddressFormBase
    extends FormInstance<Address>
    implements LoggableInstance
{

    //~ Fields ...................................................................................................................

    @SuppressWarnings({"InstanceVariableMayNotBeInitialized", "UnusedDeclaration", "NullableProblems"}) @NotNull private UiModelAccessor f;

    //~ Methods ..................................................................................................................

    /** Invoked when creating a form instance */
    @NotNull public Action create() {
        final Address address = Address.create();
        copyTo(address);
        address.insert();
        setId(address.getId());
        return actions().getDefault();
    }

    /** Invoked when updating a form instance */
    @NotNull public Action update() {
        final Address address = find();
        copyTo(address);
        address.update();
        return actions().getDefault();
    }

    /** Invoked when deleting a form instance */
    @NotNull public Action delete() {
        find().delete();
        return actions().getDefault();
    }

    /** Invoked to find an entity instance */
    @NotNull public Address find() {
        final Address value = Address.find(keyAsString());
        if (value == null) {
        	throw new EntityInstanceNotFoundException(f.title(), keyAsString());
        }
        return value;
    }

    public void setPrimaryKey(@NotNull String key) { f.set(Field.ID, Conversions.toInt(key)); }

    @NotNull public String keyAsString() { return "" + getId(); }

    /** Invoked when populating a form instance */
    @NotNull public Address populate() {
        final Address address = find();

        setStreet(address.getStreet())
        	.setCity(address.getCity())
        	.setState(address.getState())
        	.setZip(address.getZip())
        	.setCountry(address.getCountry());

        return address;
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

    /** Copies field values to given address instance. */
    public void copyTo(@NotNull Address address) {
        address.setStreet(getStreet())
        	.setCity(getCity())
        	.setState(getState())
        	.setZip(getZip())
        	.setCountry(getCountry());
    }

    /** Returns the value of the internal(id). */
    @Nullable public Integer getId() { return f.get(Field.ID, Integer.class); }

    /** Sets the value of the internal(id). */
    @NotNull public AddressForm setId(@Nullable Integer id) {
        f.set(Field.ID, id);
        return (AddressForm) this;
    }

    /** Returns the value of the text_field(street). */
    @NotNull public String getStreet() { return f.get(Field.STREET, String.class); }

    /** Sets the value of the text_field(street). */
    @NotNull public AddressForm setStreet(@NotNull String street) {
        f.set(Field.STREET, street);
        return (AddressForm) this;
    }

    /** Returns the value of the text_field(city). */
    @NotNull public String getCity() { return f.get(Field.CITY, String.class); }

    /** Sets the value of the text_field(city). */
    @NotNull public AddressForm setCity(@NotNull String city) {
        f.set(Field.CITY, city);
        return (AddressForm) this;
    }

    /** Returns the value of the text_field(state). */
    @Nullable public String getState() { return f.get(Field.STATE, String.class); }

    /** Sets the value of the text_field(state). */
    @NotNull public AddressForm setState(@Nullable String state) {
        f.set(Field.STATE, state);
        return (AddressForm) this;
    }

    /** Returns the value of the text_field(zip). */
    @Nullable public String getZip() { return f.get(Field.ZIP, String.class); }

    /** Sets the value of the text_field(zip). */
    @NotNull public AddressForm setZip(@Nullable String zip) {
        f.set(Field.ZIP, zip);
        return (AddressForm) this;
    }

    /** Returns the value of the text_field(country). */
    @NotNull public String getCountry() { return f.get(Field.COUNTRY, String.class); }

    /** Sets the value of the text_field(country). */
    @NotNull public AddressForm setCountry(@NotNull String country) {
        f.set(Field.COUNTRY, country);
        return (AddressForm) this;
    }

    /** Register a form Listener */
    public static void addListener(@NotNull FormListenerType listenerType, @NotNull FormListener<AddressForm> listener) { listeners.addListener(listenerType, listener); }

    /** Remove a form Listener */
    public static void removeListener(@NotNull FormListenerType listenerType, @NotNull FormListener<AddressForm> listener) { listeners.removeListener(listenerType, listener); }

    @Override @NotNull public Logger logger() { return logger; }

    /** Returns utility methods to deal with {@link Action actions}. */
    @NotNull protected final Actions actions() { return actions; }

    //~ Fields ...................................................................................................................

    @NotNull private static FormListenerMap<AddressForm> listeners = new FormListenerMap<>();
    @NotNull private static final Logger logger = Logger.getLogger(AddressForm.class);

    //~ Inner Classes ............................................................................................................

    @SuppressWarnings("UnnecessarySemicolon")
    public enum Field
        implements tekgenesis.metadata.form.widget.FormFieldRef
    {
        $H0("$H0"),
        $M1("$M1"),
        $L2("$L2"),
        ID("id"),
        STREET("street"),
        CITY("city"),
        STATE("state"),
        ZIP("zip"),
        COUNTRY("country"),
        $F3("$F3"),
        $B4("$B4"),
        $B5("$B5"),
        $B6("$B6");

        //~ Fields ...................................................................................................................

        @Nullable private final String id;

        //~ Constructors .............................................................................................................

        Field(@Nullable final String id) { this.id = id; }

        //~ Methods ..................................................................................................................

        @NotNull public String id() { return id; }

    }
}
