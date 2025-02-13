package tekgenesis.showcase;

import tekgenesis.form.Action;
import tekgenesis.form.Actions;
import tekgenesis.common.core.DateOnly;
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
 * Generated base class for form: SampleIcon.
 * Don't modify this as this is an auto generated class that's gets generated
 * every time the meta model file is modified. Use subclass instead.
 */
@SuppressWarnings({"DuplicateStringLiteralInspection", "WeakerAccess", "FieldMayBeFinal", "FieldCanBeLocal", "ConstantConditions", "ClassWithTooManyMethods", "ClassWithTooManyFields", "MagicNumber", "MethodOverridesStaticMethodOfSuperclass", "ParameterHidesMemberVariable", "FieldNameHidesFieldInSuperclass", "OverlyComplexClass", "RedundantCast", "UnusedReturnValue", "LocalVariableHidesMemberVariable", "UnusedParameters", "EmptyMethod"})
public abstract class SampleIconBase
    extends FormInstance<Void>
    implements LoggableInstance
{

    //~ Fields ...................................................................................................................

    @SuppressWarnings({"InstanceVariableMayNotBeInitialized", "UnusedDeclaration", "NullableProblems"}) @NotNull private UiModelAccessor f;

    //~ Methods ..................................................................................................................

    /** Invoked when populating a form instance */
    @NotNull public final Void populate() {
        throw new FormCannotBePopulatedException("tekgenesis.showcase.SampleIcon");
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

    /** Invoked when text_field(textfield) value ui changes */
    @NotNull public abstract Action shortMessage();

    /** Returns the value of the text_field(textfield). */
    @NotNull public String getTextfield() { return f.get(Field.TEXTFIELD, String.class); }

    /** Sets the value of the text_field(textfield). */
    @NotNull public SampleIcon setTextfield(@NotNull String textfield) {
        f.set(Field.TEXTFIELD, textfield);
        return (SampleIcon) this;
    }

    /** Invoked when text_field(tfTooltip) value ui changes */
    @NotNull public abstract Action longMessage();

    /** Returns the value of the text_field(tfTooltip). */
    @NotNull public String getTfTooltip() { return f.get(Field.TF_TOOLTIP, String.class); }

    /** Sets the value of the text_field(tfTooltip). */
    @NotNull public SampleIcon setTfTooltip(@NotNull String tfTooltip) {
        f.set(Field.TF_TOOLTIP, tfTooltip);
        return (SampleIcon) this;
    }

    /** Returns the value of the text_field(iconField). */
    @NotNull public String getIconField() { return f.get(Field.ICON_FIELD, String.class); }

    /** Sets the value of the text_field(iconField). */
    @NotNull public SampleIcon setIconField(@NotNull String iconField) {
        f.set(Field.ICON_FIELD, iconField);
        return (SampleIcon) this;
    }

    /** Returns the value of the text_field(complete). */
    @NotNull public String getComplete() { return f.get(Field.COMPLETE, String.class); }

    /** Sets the value of the text_field(complete). */
    @NotNull public SampleIcon setComplete(@NotNull String complete) {
        f.set(Field.COMPLETE, complete);
        return (SampleIcon) this;
    }

    /** Returns the value of the mail_field(mailFiled). */
    @NotNull public String getMailFiled() { return f.get(Field.MAIL_FILED, String.class); }

    /** Sets the value of the mail_field(mailFiled). */
    @NotNull public SampleIcon setMailFiled(@NotNull String mailFiled) {
        f.set(Field.MAIL_FILED, mailFiled);
        return (SampleIcon) this;
    }

    /** Sets the options of the mail_field(mailFiled). */
    public void setMailFiledOptions(@NotNull Iterable<String> items) { f.opts(Field.MAIL_FILED, items); }

    /** Returns the value of the date_box(date). */
    @NotNull public DateOnly getDate() {
        return DateOnly.fromMilliseconds(f.get(Field.DATE, Long.class));
    }

    /** Sets the value of the date_box(date). */
    @NotNull public SampleIcon setDate(@NotNull DateOnly date) {
        f.set(Field.DATE, date);
        return (SampleIcon) this;
    }

    /** Register a form Listener */
    public static void addListener(@NotNull FormListenerType listenerType, @NotNull FormListener<SampleIcon> listener) { listeners.addListener(listenerType, listener); }

    /** Remove a form Listener */
    public static void removeListener(@NotNull FormListenerType listenerType, @NotNull FormListener<SampleIcon> listener) { listeners.removeListener(listenerType, listener); }

    @Override @NotNull public Logger logger() { return logger; }

    /** Returns utility methods to deal with {@link Action actions}. */
    @NotNull protected final Actions actions() { return actions; }

    //~ Fields ...................................................................................................................

    @NotNull private static FormListenerMap<SampleIcon> listeners = new FormListenerMap<>();
    @NotNull private static final Logger logger = Logger.getLogger(SampleIcon.class);

    //~ Inner Classes ............................................................................................................

    @SuppressWarnings("UnnecessarySemicolon")
    public enum Field
        implements tekgenesis.metadata.form.widget.FormFieldRef
    {
        TEXTFIELD("textfield"),
        TF_TOOLTIP("tfTooltip"),
        ICON_FIELD("iconField"),
        COMPLETE("complete"),
        MAIL_FILED("mailFiled"),
        DATE("date");

        //~ Fields ...................................................................................................................

        @Nullable private final String id;

        //~ Constructors .............................................................................................................

        Field(@Nullable final String id) { this.id = id; }

        //~ Methods ..................................................................................................................

        @NotNull public String id() { return id; }

    }
}
