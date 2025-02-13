package tekgenesis.showcase;

import tekgenesis.form.Action;
import tekgenesis.form.Actions;
import tekgenesis.showcase.DynamicTableForm.ClientAddressesRow;
import tekgenesis.common.util.Conversions;
import tekgenesis.form.exception.EntityInstanceNotFoundException;
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
import tekgenesis.form.UiModelAccessor;
import tekgenesis.form.configuration.WidgetConfiguration;

/** 
 * Generated base class for form: DynamicTableForm.
 * Don't modify this as this is an auto generated class that's gets generated
 * every time the meta model file is modified. Use subclass instead.
 */
@SuppressWarnings({"DuplicateStringLiteralInspection", "WeakerAccess", "FieldMayBeFinal", "FieldCanBeLocal", "ConstantConditions", "ClassWithTooManyMethods", "ClassWithTooManyFields", "MagicNumber", "MethodOverridesStaticMethodOfSuperclass", "ParameterHidesMemberVariable", "FieldNameHidesFieldInSuperclass", "OverlyComplexClass", "RedundantCast", "UnusedReturnValue", "LocalVariableHidesMemberVariable", "UnusedParameters", "EmptyMethod"})
public abstract class DynamicTableFormBase
    extends FormInstance<Addresses>
    implements LoggableInstance
{

    //~ Fields ...................................................................................................................

    @SuppressWarnings({"InstanceVariableMayNotBeInitialized", "UnusedDeclaration", "NullableProblems"}) @NotNull private UiModelAccessor f;

    //~ Methods ..................................................................................................................

    /** Invoked when creating a form instance */
    @NotNull public Action create() {
        final Addresses addresses = Addresses.create();
        copyTo(addresses);
        createOrUpdateClientAddresses(addresses);
        addresses.insert();
        setId(addresses.getId());
        return actions().getDefault();
    }

    /** Invoked when updating a form instance */
    @NotNull public Action update() {
        final Addresses addresses = find();
        copyTo(addresses);
        createOrUpdateClientAddresses(addresses);
        addresses.update();
        return actions().getDefault();
    }

    /** Invoked when deleting a form instance */
    @NotNull public Action delete() {
        find().delete();
        return actions().getDefault();
    }

    /** Invoked to find an entity instance */
    @NotNull public Addresses find() {
        final Addresses value = Addresses.find(keyAsString());
        if (value == null) {
        	throw new EntityInstanceNotFoundException(f.title(), keyAsString());
        }
        return value;
    }

    public void setPrimaryKey(@NotNull String key) { f.set(Field.ID, Conversions.toInt(key)); }

    @NotNull public String keyAsString() { return "" + getId(); }

    /** Invoked when populating a form instance */
    @NotNull public Addresses populate() {
        final Addresses addresses = find();


        getClientAddresses().populate(addresses.getClientAddresses(), ClientAddressesRow::populate);

        return addresses;
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

    /** Copies field values to given addresses instance. */
    public void copyTo(@NotNull Addresses addresses) { }

    /** Updates external references to clientAddresses. */
    public void createOrUpdateClientAddresses(@NotNull Addresses addresses) {
        addresses.getClientAddresses().merge(getClientAddresses(), (innerAddress, row) -> row.copyTo(innerAddress));
    }

    /** Returns the value of the message(title). */
    @NotNull public String getTitle() { return f.get(Field.TITLE, String.class); }

    /** Sets the value of the message(title). */
    @NotNull public DynamicTableForm setTitle(@NotNull String title) {
        f.set(Field.TITLE, title);
        return (DynamicTableForm) this;
    }

    /** Returns the value of the internal(id). */
    @Nullable public Integer getId() { return f.get(Field.ID, Integer.class); }

    /** Sets the value of the internal(id). */
    @NotNull public DynamicTableForm setId(@Nullable Integer id) {
        f.set(Field.ID, id);
        return (DynamicTableForm) this;
    }

    /** Returns a {@link FormTable<ClientAddressesRow>} instance to handle ClientAddresses manipulation */
    @NotNull public final FormTable<ClientAddressesRow> getClientAddresses() {
        return table(Field.CLIENT_ADDRESSES, ClientAddressesRow.class);
    }

    /** Method to allow table row class override */
    @NotNull final <T> FormTable<T> table(@NotNull FormFieldRef field, @NotNull Class<T> rowClass) { return f.table(field, rowClass, this); }

    /** Register a form Listener */
    public static void addListener(@NotNull FormListenerType listenerType, @NotNull FormListener<DynamicTableForm> listener) { listeners.addListener(listenerType, listener); }

    /** Remove a form Listener */
    public static void removeListener(@NotNull FormListenerType listenerType, @NotNull FormListener<DynamicTableForm> listener) { listeners.removeListener(listenerType, listener); }

    @Override @NotNull public Logger logger() { return logger; }

    /** Returns utility methods to deal with {@link Action actions}. */
    @NotNull protected final Actions actions() { return actions; }

    //~ Fields ...................................................................................................................

    @NotNull private static FormListenerMap<DynamicTableForm> listeners = new FormListenerMap<>();
    @NotNull private static final Logger logger = Logger.getLogger(DynamicTableForm.class);

    //~ Inner Classes ............................................................................................................

    public abstract class ClientAddressesRowBase
        implements FormRowInstance<ClientAddressesRow>
    {

        //~ Fields ...................................................................................................................

        @SuppressWarnings({"InstanceVariableMayNotBeInitialized", "UnusedDeclaration", "NullableProblems"}) @NotNull private UiModelAccessor f;

        //~ Methods ..................................................................................................................

        /** Returns the value of the text_field(country). */
        @NotNull public String getCountry() { return f.get(Field.COUNTRY, String.class); }

        /** Sets the value of the text_field(country). */
        @NotNull public ClientAddressesRow setCountry(@NotNull String country) {
            f.set(Field.COUNTRY, country);
            return (ClientAddressesRow) this;
        }

        /** Returns the value of the text_field(state). */
        @Nullable public String getState() { return f.get(Field.STATE, String.class); }

        /** Sets the value of the text_field(state). */
        @NotNull public ClientAddressesRow setState(@Nullable String state) {
            f.set(Field.STATE, state);
            return (ClientAddressesRow) this;
        }

        /** Returns the value of the text_field(city). */
        @NotNull public String getCity() { return f.get(Field.CITY, String.class); }

        /** Sets the value of the text_field(city). */
        @NotNull public ClientAddressesRow setCity(@NotNull String city) {
            f.set(Field.CITY, city);
            return (ClientAddressesRow) this;
        }

        /** Returns the value of the text_field(street). */
        @NotNull public String getStreet() { return f.get(Field.STREET, String.class); }

        /** Sets the value of the text_field(street). */
        @NotNull public ClientAddressesRow setStreet(@NotNull String street) {
            f.set(Field.STREET, street);
            return (ClientAddressesRow) this;
        }

        /** Returns the value of the text_field(zip). */
        @Nullable public String getZip() { return f.get(Field.ZIP, String.class); }

        /** Sets the value of the text_field(zip). */
        @NotNull public ClientAddressesRow setZip(@Nullable String zip) {
            f.set(Field.ZIP, zip);
            return (ClientAddressesRow) this;
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
        @NotNull public final FormTable<ClientAddressesRow> table() { return getClientAddresses(); }

        /** Remove row from table. */
        @NotNull public Action remove() {
            table().remove((ClientAddressesRow) this);
            return actions().getDefault();
        }

        /** Returns a typed configuration for a given field. */
        @NotNull protected <T extends WidgetConfiguration> T configuration(@NotNull Field field) { return f.config(field); }

        /** Populate field values with given innerAddress instance. */
        public void populate(@NotNull InnerAddress innerAddress) {
            setStreet(innerAddress.getStreet())
            	.setCity(innerAddress.getCity())
            	.setState(innerAddress.getState())
            	.setZip(innerAddress.getZip())
            	.setCountry(innerAddress.getCountry());
        }

        /** Copies field values to given innerAddress instance. */
        public void copyTo(@NotNull InnerAddress innerAddress) {
            innerAddress.setStreet(getStreet())
            	.setCity(getCity())
            	.setState(getState())
            	.setZip(getZip())
            	.setCountry(getCountry());
        }

    }

    @SuppressWarnings("UnnecessarySemicolon")
    public enum Field
        implements tekgenesis.metadata.form.widget.FormFieldRef
    {
        $H0("$H0"),
        TITLE("title"),
        ID("id"),
        CLIENT_ADDRESSES("clientAddresses"),
        COUNTRY("country"),
        STATE("state"),
        CITY("city"),
        STREET("street"),
        ZIP("zip"),
        $H1("$H1"),
        ADD_ROW_BOTTOM("addRowBottom"),
        REMOVE_ROW_BOTTOM("removeRowBottom"),
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
