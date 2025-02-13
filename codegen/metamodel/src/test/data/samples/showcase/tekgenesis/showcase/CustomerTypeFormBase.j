package tekgenesis.showcase;

import tekgenesis.form.Action;
import tekgenesis.form.Actions;
import tekgenesis.form.exception.FormCannotBePopulatedException;
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
 * Generated base class for form: CustomerTypeForm.
 * Don't modify this as this is an auto generated class that's gets generated
 * every time the meta model file is modified. Use subclass instead.
 */
@SuppressWarnings({"DuplicateStringLiteralInspection", "WeakerAccess", "FieldMayBeFinal", "FieldCanBeLocal", "ConstantConditions", "ClassWithTooManyMethods", "ClassWithTooManyFields", "MagicNumber", "MethodOverridesStaticMethodOfSuperclass", "ParameterHidesMemberVariable", "FieldNameHidesFieldInSuperclass", "OverlyComplexClass", "RedundantCast", "UnusedReturnValue", "LocalVariableHidesMemberVariable", "UnusedParameters", "EmptyMethod"})
public abstract class CustomerTypeFormBase
    extends FormInstance<Void>
    implements LoggableInstance
{

    //~ Fields ...................................................................................................................

    @SuppressWarnings({"InstanceVariableMayNotBeInitialized", "UnusedDeclaration", "NullableProblems"}) @NotNull private UiModelAccessor f;

    //~ Methods ..................................................................................................................

    /** Invoked when populating a form instance */
    @NotNull public final Void populate() {
        throw new FormCannotBePopulatedException("tekgenesis.showcase.CustomerTypeForm");
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

    /** Returns the value of the text_field(document). */
    public int getDocument() { return f.get(Field.DOCUMENT, Integer.class); }

    /** Sets the value of the text_field(document). */
    @NotNull public CustomerTypeForm setDocument(int document) {
        f.set(Field.DOCUMENT, document);
        return (CustomerTypeForm) this;
    }

    /** Returns the value of the text_field(firstName). */
    @NotNull public String getFirstName() { return f.get(Field.FIRST_NAME, String.class); }

    /** Sets the value of the text_field(firstName). */
    @NotNull public CustomerTypeForm setFirstName(@NotNull String firstName) {
        f.set(Field.FIRST_NAME, firstName);
        return (CustomerTypeForm) this;
    }

    /** Returns the value of the text_field(lastName). */
    @NotNull public String getLastName() { return f.get(Field.LAST_NAME, String.class); }

    /** Sets the value of the text_field(lastName). */
    @NotNull public CustomerTypeForm setLastName(@NotNull String lastName) {
        f.set(Field.LAST_NAME, lastName);
        return (CustomerTypeForm) this;
    }

    /** Returns the value of the display(home). */
    @NotNull public String getHome() { return f.get(Field.HOME, String.class); }

    /** Define {@link AddressTypeWidget instance} to be used during interaction. */
    @NotNull abstract AddressTypeWidget defineHomeAddress();

    /** Get widget {@link AddressTypeWidget}. */
    @NotNull public AddressTypeWidget getHomeAddress() {
        return f.widget(Field.HOME_ADDRESS, AddressTypeWidget.class);
    }

    /** Invoked when button(createWorkAddress) is clicked */
    @NotNull public abstract Action reateWorkAddress();

    /** Returns the value of the display(work). */
    @NotNull public String getWork() { return f.get(Field.WORK, String.class); }

    /** Define {@link AddressTypeWidget instance} to be used during interaction. */
    @NotNull abstract AddressTypeWidget defineWorkAddress();

    /** Get widget {@link AddressTypeWidget}. */
    @NotNull public OptionalWidget<AddressTypeWidget> getWorkAddress() {
        return f.optionalWidget(Field.WORK_ADDRESS, AddressTypeWidget.class);
    }

    /** Returns the value of the text_field(feedback). */
    @NotNull public String getFeedback() { return f.get(Field.FEEDBACK, String.class); }

    /** Sets the value of the text_field(feedback). */
    @NotNull public CustomerTypeForm setFeedback(@NotNull String feedback) {
        f.set(Field.FEEDBACK, feedback);
        return (CustomerTypeForm) this;
    }

    /** Register a form Listener */
    public static void addListener(@NotNull FormListenerType listenerType, @NotNull FormListener<CustomerTypeForm> listener) { listeners.addListener(listenerType, listener); }

    /** Remove a form Listener */
    public static void removeListener(@NotNull FormListenerType listenerType, @NotNull FormListener<CustomerTypeForm> listener) { listeners.removeListener(listenerType, listener); }

    @Override @NotNull public Logger logger() { return logger; }

    /** Returns utility methods to deal with {@link Action actions}. */
    @NotNull protected final Actions actions() { return actions; }

    //~ Fields ...................................................................................................................

    @NotNull private static FormListenerMap<CustomerTypeForm> listeners = new FormListenerMap<>();
    @NotNull private static final Logger logger = Logger.getLogger(CustomerTypeForm.class);

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
        CREATE_WORK_ADDRESS("createWorkAddress"),
        WORK("work"),
        WORK_ADDRESS("workAddress"),
        FEEDBACK("feedback");

        //~ Fields ...................................................................................................................

        @Nullable private final String id;

        //~ Constructors .............................................................................................................

        Field(@Nullable final String id) { this.id = id; }

        //~ Methods ..................................................................................................................

        @NotNull public String id() { return id; }

    }
}
