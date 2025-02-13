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
import tekgenesis.common.core.Resource;
import tekgenesis.form.UiModelAccessor;
import tekgenesis.form.configuration.WidgetConfiguration;
import static tekgenesis.common.core.Strings.splitToArray;

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
        	.setLastName(customer.getLastName())
        	.setNickname(customer.getNickname())
        	.setPhoto(customer.getPhoto());

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
        	.setLastName(getLastName())
        	.setNickname(getNickname())
        	.setPhoto(getPhoto());
    }

    /** Returns the value of the combo_box(documentType). */
    @NotNull public DocType getDocumentType() {
        return DocType.valueOf(f.get(Field.DOCUMENT_TYPE, String.class));
    }

    /** Sets the value of the combo_box(documentType). */
    @NotNull public CustomerForm setDocumentType(@NotNull DocType documentType) {
        f.set(Field.DOCUMENT_TYPE, documentType);
        return (CustomerForm) this;
    }

    /** Sets the options of the combo_box(documentType). */
    public void setDocumentTypeOptions(@NotNull Iterable<DocType> items) { f.opts(Field.DOCUMENT_TYPE, items); }

    /** Sets the options of the combo_box(documentType) with the given KeyMap. */
    public void setDocumentTypeOptions(@NotNull KeyMap items) { f.opts(Field.DOCUMENT_TYPE, items); }

    /** Returns the value of the text_field(documentId). */
    @NotNull public BigDecimal getDocumentId() { return f.get(Field.DOCUMENT_ID, BigDecimal.class); }

    /** Sets the value of the text_field(documentId). */
    @NotNull public CustomerForm setDocumentId(@NotNull BigDecimal documentId) {
        f.set(Field.DOCUMENT_ID, Decimals.scaleAndCheck("documentId", documentId, false, 10, 0));
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

    /** Returns the value of the text_field(nickname). */
    @NotNull public String getNickname() { return f.get(Field.NICKNAME, String.class); }

    /** Sets the value of the text_field(nickname). */
    @NotNull public CustomerForm setNickname(@NotNull String nickname) {
        f.set(Field.NICKNAME, nickname);
        return (CustomerForm) this;
    }

    /** Returns the value of the combo_box(sex). */
    @NotNull public Sex getSex() { return Sex.valueOf(f.get(Field.SEX, String.class)); }

    /** Sets the value of the combo_box(sex). */
    @NotNull public CustomerForm setSex(@NotNull Sex sex) {
        f.set(Field.SEX, sex);
        return (CustomerForm) this;
    }

    /** Sets the options of the combo_box(sex). */
    public void setSexOptions(@NotNull Iterable<Sex> items) { f.opts(Field.SEX, items); }

    /** Sets the options of the combo_box(sex) with the given KeyMap. */
    public void setSexOptions(@NotNull KeyMap items) { f.opts(Field.SEX, items); }

    /** Returns the value of the upload(photo). */
    @Nullable public Resource getPhoto() { return f.get(Field.PHOTO, Resource.class); }

    /** Sets the value of the upload(photo). */
    @NotNull public CustomerForm setPhoto(@Nullable Resource photo) {
        f.set(Field.PHOTO, photo);
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
        $S2("$S2"),
        DOCUMENT_TYPE("documentType"),
        DOCUMENT_ID("documentId"),
        FIRST_NAME("firstName"),
        LAST_NAME("lastName"),
        NICKNAME("nickname"),
        SEX("sex"),
        PHOTO("photo"),
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
