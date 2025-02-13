package tekgenesis.sales.basic;

import tekgenesis.form.Action;
import tekgenesis.form.Actions;
import tekgenesis.common.util.Conversions;
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
import tekgenesis.common.Predefined;
import tekgenesis.form.UiModelAccessor;
import tekgenesis.form.configuration.WidgetConfiguration;

/** 
 * Generated base class for form: PreferencesForm.
 * Don't modify this as this is an auto generated class that's gets generated
 * every time the meta model file is modified. Use subclass instead.
 */
@SuppressWarnings({"DuplicateStringLiteralInspection", "WeakerAccess", "FieldMayBeFinal", "FieldCanBeLocal", "ConstantConditions", "ClassWithTooManyMethods", "ClassWithTooManyFields", "MagicNumber", "MethodOverridesStaticMethodOfSuperclass", "ParameterHidesMemberVariable", "FieldNameHidesFieldInSuperclass", "OverlyComplexClass", "RedundantCast", "UnusedReturnValue", "LocalVariableHidesMemberVariable", "UnusedParameters", "EmptyMethod"})
public abstract class PreferencesFormBase
    extends FormInstance<Preferences>
    implements LoggableInstance
{

    //~ Fields ...................................................................................................................

    @SuppressWarnings({"InstanceVariableMayNotBeInitialized", "UnusedDeclaration", "NullableProblems"}) @NotNull private UiModelAccessor f;

    //~ Methods ..................................................................................................................

    /** Invoked when creating a form instance */
    @NotNull public Action create() {
        final Preferences preferences = Preferences.create();
        copyTo(preferences);
        preferences.insert();
        setId(preferences.getId());
        return actions().getDefault();
    }

    /** Invoked when updating a form instance */
    @NotNull public Action update() {
        final Preferences preferences = find();
        copyTo(preferences);
        preferences.update();
        return actions().getDefault();
    }

    /** Invoked when deleting a form instance */
    @NotNull public Action delete() {
        find().delete();
        return actions().getDefault();
    }

    /** Invoked to find an entity instance */
    @NotNull public Preferences find() {
        final Preferences value = Preferences.find(keyAsString());
        if (value == null) {
        	throw new EntityInstanceNotFoundException(f.title(), keyAsString());
        }
        return value;
    }

    public void setPrimaryKey(@NotNull String key) { f.set(Field.ID, Conversions.toInt(key)); }

    @NotNull public String keyAsString() { return "" + getId(); }

    /** Invoked when populating a form instance */
    @NotNull public Preferences populate() {
        final Preferences preferences = find();

        setCustomer(preferences.getCustomer())
        	.setMail(preferences.getMail())
        	.setTwitter(preferences.getTwitter())
        	.setDigest(preferences.getDigest());

        return preferences;
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

    /** Copies field values to given preferences instance. */
    public void copyTo(@NotNull Preferences preferences) {
        preferences.setCustomer(getCustomer())
        	.setMail(getMail())
        	.setTwitter(getTwitter())
        	.setDigest(getDigest());
    }

    /** Returns the value of the internal(id). */
    @Nullable public Integer getId() { return f.get(Field.ID, Integer.class); }

    /** Sets the value of the internal(id). */
    @NotNull public PreferencesForm setId(@Nullable Integer id) {
        f.set(Field.ID, id);
        return (PreferencesForm) this;
    }

    /** Returns the value of the suggest_box(customer). */
    @NotNull public Customer getCustomer() {
        return Predefined.ensureNotNull(Customer.find(getCustomerKey()), "'customer' not found");
    }

    /** Returns the key value of the suggest_box(customer). */
    @NotNull public String getCustomerKey() { return f.get(Field.CUSTOMER, String.class); }

    /** Sets the value of the suggest_box(customer). */
    @NotNull public PreferencesForm setCustomer(@NotNull Customer customer) {
        f.set(Field.CUSTOMER, customer);
        return (PreferencesForm) this;
    }

    /** Returns the value of the text_field(mail). */
    @NotNull public String getMail() { return f.get(Field.MAIL, String.class); }

    /** Sets the value of the text_field(mail). */
    @NotNull public PreferencesForm setMail(@NotNull String mail) {
        f.set(Field.MAIL, mail);
        return (PreferencesForm) this;
    }

    /** Returns the value of the combo_box(digest). */
    @NotNull public MailDigest getDigest() {
        return MailDigest.valueOf(f.get(Field.DIGEST, String.class));
    }

    /** Sets the value of the combo_box(digest). */
    @NotNull public PreferencesForm setDigest(@NotNull MailDigest digest) {
        f.set(Field.DIGEST, digest);
        return (PreferencesForm) this;
    }

    /** Sets the options of the combo_box(digest). */
    public void setDigestOptions(@NotNull Iterable<MailDigest> items) { f.opts(Field.DIGEST, items); }

    /** Sets the options of the combo_box(digest) with the given KeyMap. */
    public void setDigestOptions(@NotNull KeyMap items) { f.opts(Field.DIGEST, items); }

    /** Invoked when text_field(twitter) value changes */
    @NotNull public abstract Action checkUser();

    /** Returns the value of the text_field(twitter). */
    @NotNull public String getTwitter() { return f.get(Field.TWITTER, String.class); }

    /** Sets the value of the text_field(twitter). */
    @NotNull public PreferencesForm setTwitter(@NotNull String twitter) {
        f.set(Field.TWITTER, twitter);
        return (PreferencesForm) this;
    }

    /** Returns the value of the image(image). */
    @NotNull public String getImage() { return f.get(Field.IMAGE, String.class); }

    /** Sets the value of the image(image). */
    @NotNull public PreferencesForm setImage(@NotNull String image) {
        f.set(Field.IMAGE, image);
        return (PreferencesForm) this;
    }

    /** Register a form Listener */
    public static void addListener(@NotNull FormListenerType listenerType, @NotNull FormListener<PreferencesForm> listener) { listeners.addListener(listenerType, listener); }

    /** Remove a form Listener */
    public static void removeListener(@NotNull FormListenerType listenerType, @NotNull FormListener<PreferencesForm> listener) { listeners.removeListener(listenerType, listener); }

    @Override @NotNull public Logger logger() { return logger; }

    /** Returns utility methods to deal with {@link Action actions}. */
    @NotNull protected final Actions actions() { return actions; }

    //~ Fields ...................................................................................................................

    @NotNull private static FormListenerMap<PreferencesForm> listeners = new FormListenerMap<>();
    @NotNull private static final Logger logger = Logger.getLogger(PreferencesForm.class);

    //~ Inner Classes ............................................................................................................

    @SuppressWarnings("UnnecessarySemicolon")
    public enum Field
        implements tekgenesis.metadata.form.widget.FormFieldRef
    {
        $H0("$H0"),
        $M1("$M1"),
        ID("id"),
        CUSTOMER("customer"),
        MAIL("mail"),
        DIGEST("digest"),
        TWITTER("twitter"),
        IMAGE("image"),
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
