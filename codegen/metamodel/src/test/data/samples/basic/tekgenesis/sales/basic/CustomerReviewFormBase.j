package tekgenesis.sales.basic;

import tekgenesis.form.Action;
import tekgenesis.form.Actions;
import java.math.BigDecimal;
import tekgenesis.common.core.Decimals;
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
 * Generated base class for form: CustomerReviewForm.
 * Don't modify this as this is an auto generated class that's gets generated
 * every time the meta model file is modified. Use subclass instead.
 */
@SuppressWarnings({"DuplicateStringLiteralInspection", "WeakerAccess", "FieldMayBeFinal", "FieldCanBeLocal", "ConstantConditions", "ClassWithTooManyMethods", "ClassWithTooManyFields", "MagicNumber", "MethodOverridesStaticMethodOfSuperclass", "ParameterHidesMemberVariable", "FieldNameHidesFieldInSuperclass", "OverlyComplexClass", "RedundantCast", "UnusedReturnValue", "LocalVariableHidesMemberVariable", "UnusedParameters", "EmptyMethod"})
public abstract class CustomerReviewFormBase
    extends FormInstance<Customer>
    implements LoggableInstance
{

    //~ Fields ...................................................................................................................

    @SuppressWarnings({"InstanceVariableMayNotBeInitialized", "UnusedDeclaration", "NullableProblems"}) @NotNull private UiModelAccessor f;

    //~ Methods ..................................................................................................................

    /** Invoked when creating a form instance */
    @NotNull public abstract Action create();

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
    @NotNull public abstract Customer find();

    /** Invoked when populating a form instance */
    @NotNull public Customer populate() {
        final Customer customer = find();

        setFirstName(customer.getFirstName())
        	.setLastName(customer.getLastName())
        	.setNickname(customer.getNickname());

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
        	.setNickname(getNickname());
    }

    /** Returns the value of the text_field(documentType). */
    @NotNull public String getDocumentType() { return f.get(Field.DOCUMENT_TYPE, String.class); }

    /** Sets the value of the text_field(documentType). */
    @NotNull public CustomerReviewForm setDocumentType(@NotNull String documentType) {
        f.set(Field.DOCUMENT_TYPE, documentType);
        return (CustomerReviewForm) this;
    }

    /** Returns the value of the text_field(documentId). */
    @NotNull public BigDecimal getDocumentId() { return f.get(Field.DOCUMENT_ID, BigDecimal.class); }

    /** Sets the value of the text_field(documentId). */
    @NotNull public CustomerReviewForm setDocumentId(@NotNull BigDecimal documentId) {
        f.set(Field.DOCUMENT_ID, Decimals.scaleAndCheck("documentId", documentId, false, 10, 0));
        return (CustomerReviewForm) this;
    }

    /** Returns the value of the text_field(firstName). */
    @NotNull public String getFirstName() { return f.get(Field.FIRST_NAME, String.class); }

    /** Sets the value of the text_field(firstName). */
    @NotNull public CustomerReviewForm setFirstName(@NotNull String firstName) {
        f.set(Field.FIRST_NAME, firstName);
        return (CustomerReviewForm) this;
    }

    /** Returns the value of the text_field(lastName). */
    @NotNull public String getLastName() { return f.get(Field.LAST_NAME, String.class); }

    /** Sets the value of the text_field(lastName). */
    @NotNull public CustomerReviewForm setLastName(@NotNull String lastName) {
        f.set(Field.LAST_NAME, lastName);
        return (CustomerReviewForm) this;
    }

    /** Returns the value of the text_field(nickname). */
    @NotNull public String getNickname() { return f.get(Field.NICKNAME, String.class); }

    /** Sets the value of the text_field(nickname). */
    @NotNull public CustomerReviewForm setNickname(@NotNull String nickname) {
        f.set(Field.NICKNAME, nickname);
        return (CustomerReviewForm) this;
    }

    /** Returns the value of the text_field(sex). */
    @NotNull public String getSex() { return f.get(Field.SEX, String.class); }

    /** Sets the value of the text_field(sex). */
    @NotNull public CustomerReviewForm setSex(@NotNull String sex) {
        f.set(Field.SEX, sex);
        return (CustomerReviewForm) this;
    }

    /** Register a form Listener */
    public static void addListener(@NotNull FormListenerType listenerType, @NotNull FormListener<CustomerReviewForm> listener) { listeners.addListener(listenerType, listener); }

    /** Remove a form Listener */
    public static void removeListener(@NotNull FormListenerType listenerType, @NotNull FormListener<CustomerReviewForm> listener) { listeners.removeListener(listenerType, listener); }

    @Override @NotNull public Logger logger() { return logger; }

    /** Returns utility methods to deal with {@link Action actions}. */
    @NotNull protected final Actions actions() { return actions; }

    //~ Fields ...................................................................................................................

    @NotNull private static FormListenerMap<CustomerReviewForm> listeners = new FormListenerMap<>();
    @NotNull private static final Logger logger = Logger.getLogger(CustomerReviewForm.class);

    //~ Inner Classes ............................................................................................................

    @SuppressWarnings("UnnecessarySemicolon")
    public enum Field
        implements tekgenesis.metadata.form.widget.FormFieldRef
    {
        $H0("$H0"),
        $M1("$M1"),
        DOCUMENT_TYPE("documentType"),
        DOCUMENT_ID("documentId"),
        FIRST_NAME("firstName"),
        LAST_NAME("lastName"),
        NICKNAME("nickname"),
        SEX("sex");

        //~ Fields ...................................................................................................................

        @Nullable private final String id;

        //~ Constructors .............................................................................................................

        Field(@Nullable final String id) { this.id = id; }

        //~ Methods ..................................................................................................................

        @NotNull public String id() { return id; }

    }
}
