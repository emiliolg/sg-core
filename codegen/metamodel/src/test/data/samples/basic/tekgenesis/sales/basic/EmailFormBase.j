package tekgenesis.sales.basic;

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
import tekgenesis.form.UiModelAccessor;
import tekgenesis.form.configuration.WidgetConfiguration;

/** 
 * Generated base class for form: EmailForm.
 * Don't modify this as this is an auto generated class that's gets generated
 * every time the meta model file is modified. Use subclass instead.
 */
@SuppressWarnings({"DuplicateStringLiteralInspection", "WeakerAccess", "FieldMayBeFinal", "FieldCanBeLocal", "ConstantConditions", "ClassWithTooManyMethods", "ClassWithTooManyFields", "MagicNumber", "MethodOverridesStaticMethodOfSuperclass", "ParameterHidesMemberVariable", "FieldNameHidesFieldInSuperclass", "OverlyComplexClass", "RedundantCast", "UnusedReturnValue", "LocalVariableHidesMemberVariable", "UnusedParameters", "EmptyMethod"})
public abstract class EmailFormBase
    extends FormInstance<Void>
    implements LoggableInstance
{

    //~ Fields ...................................................................................................................

    @SuppressWarnings({"InstanceVariableMayNotBeInitialized", "UnusedDeclaration", "NullableProblems"}) @NotNull private UiModelAccessor f;

    //~ Methods ..................................................................................................................

    /** Invoked when populating a form instance */
    @NotNull public final Void populate() {
        throw new FormCannotBePopulatedException("tekgenesis.sales.basic.EmailForm");
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

    /** Returns the value of the text_field(host). */
    @NotNull public String getHost() { return f.get(Field.HOST, String.class); }

    /** Sets the value of the text_field(host). */
    @NotNull public EmailForm setHost(@NotNull String host) {
        f.set(Field.HOST, host);
        return (EmailForm) this;
    }

    /** Returns the value of the text_field(port). */
    public int getPort() { return f.get(Field.PORT, Integer.class); }

    /** Sets the value of the text_field(port). */
    @NotNull public EmailForm setPort(int port) {
        f.set(Field.PORT, port);
        return (EmailForm) this;
    }

    /** Returns the value of the text_field(username). */
    @NotNull public String getUsername() { return f.get(Field.USERNAME, String.class); }

    /** Sets the value of the text_field(username). */
    @NotNull public EmailForm setUsername(@NotNull String username) {
        f.set(Field.USERNAME, username);
        return (EmailForm) this;
    }

    /** Returns the value of the password_field(password). */
    @NotNull public String getPassword() { return f.get(Field.PASSWORD, String.class); }

    /** Sets the value of the password_field(password). */
    @NotNull public EmailForm setPassword(@NotNull String password) {
        f.set(Field.PASSWORD, password);
        return (EmailForm) this;
    }

    /** Returns the value of the text_field(from). */
    @NotNull public String getFrom() { return f.get(Field.FROM, String.class); }

    /** Sets the value of the text_field(from). */
    @NotNull public EmailForm setFrom(@NotNull String from) {
        f.set(Field.FROM, from);
        return (EmailForm) this;
    }

    /** Returns the value of the text_field(to). */
    @NotNull public String getTo() { return f.get(Field.TO, String.class); }

    /** Sets the value of the text_field(to). */
    @NotNull public EmailForm setTo(@NotNull String to) {
        f.set(Field.TO, to);
        return (EmailForm) this;
    }

    /** Returns the value of the text_field(cc). */
    @NotNull public String getCc() { return f.get(Field.CC, String.class); }

    /** Sets the value of the text_field(cc). */
    @NotNull public EmailForm setCc(@NotNull String cc) {
        f.set(Field.CC, cc);
        return (EmailForm) this;
    }

    /** Returns the value of the text_field(bcc). */
    @NotNull public String getBcc() { return f.get(Field.BCC, String.class); }

    /** Sets the value of the text_field(bcc). */
    @NotNull public EmailForm setBcc(@NotNull String bcc) {
        f.set(Field.BCC, bcc);
        return (EmailForm) this;
    }

    /** Returns the value of the text_field(subject). */
    @NotNull public String getSubject() { return f.get(Field.SUBJECT, String.class); }

    /** Sets the value of the text_field(subject). */
    @NotNull public EmailForm setSubject(@NotNull String subject) {
        f.set(Field.SUBJECT, subject);
        return (EmailForm) this;
    }

    /** Returns the value of the text_area(body). */
    @NotNull public String getBody() { return f.get(Field.BODY, String.class); }

    /** Sets the value of the text_area(body). */
    @NotNull public EmailForm setBody(@NotNull String body) {
        f.set(Field.BODY, body);
        return (EmailForm) this;
    }

    /** Invoked when button(send) is clicked */
    @NotNull public abstract Action sendEmail();

    /** Register a form Listener */
    public static void addListener(@NotNull FormListenerType listenerType, @NotNull FormListener<EmailForm> listener) { listeners.addListener(listenerType, listener); }

    /** Remove a form Listener */
    public static void removeListener(@NotNull FormListenerType listenerType, @NotNull FormListener<EmailForm> listener) { listeners.removeListener(listenerType, listener); }

    @Override @NotNull public Logger logger() { return logger; }

    /** Returns utility methods to deal with {@link Action actions}. */
    @NotNull protected final Actions actions() { return actions; }

    //~ Fields ...................................................................................................................

    @NotNull private static FormListenerMap<EmailForm> listeners = new FormListenerMap<>();
    @NotNull private static final Logger logger = Logger.getLogger(EmailForm.class);

    //~ Inner Classes ............................................................................................................

    @SuppressWarnings("UnnecessarySemicolon")
    public enum Field
        implements tekgenesis.metadata.form.widget.FormFieldRef
    {
        $V0("$V0"),
        HOST("host"),
        PORT("port"),
        USERNAME("username"),
        PASSWORD("password"),
        $V1("$V1"),
        FROM("from"),
        TO("to"),
        CC("cc"),
        BCC("bcc"),
        SUBJECT("subject"),
        BODY("body"),
        $F2("$F2"),
        SEND("send");

        //~ Fields ...................................................................................................................

        @Nullable private final String id;

        //~ Constructors .............................................................................................................

        Field(@Nullable final String id) { this.id = id; }

        //~ Methods ..................................................................................................................

        @NotNull public String id() { return id; }

    }
}
