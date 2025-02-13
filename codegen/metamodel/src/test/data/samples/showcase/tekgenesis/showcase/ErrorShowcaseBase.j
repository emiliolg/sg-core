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
import tekgenesis.form.UiModelAccessor;
import tekgenesis.form.configuration.WidgetConfiguration;

/** 
 * Generated base class for form: ErrorShowcase.
 * Don't modify this as this is an auto generated class that's gets generated
 * every time the meta model file is modified. Use subclass instead.
 */
@SuppressWarnings({"DuplicateStringLiteralInspection", "WeakerAccess", "FieldMayBeFinal", "FieldCanBeLocal", "ConstantConditions", "ClassWithTooManyMethods", "ClassWithTooManyFields", "MagicNumber", "MethodOverridesStaticMethodOfSuperclass", "ParameterHidesMemberVariable", "FieldNameHidesFieldInSuperclass", "OverlyComplexClass", "RedundantCast", "UnusedReturnValue", "LocalVariableHidesMemberVariable", "UnusedParameters", "EmptyMethod"})
public abstract class ErrorShowcaseBase
    extends FormInstance<Void>
    implements LoggableInstance
{

    //~ Fields ...................................................................................................................

    @SuppressWarnings({"InstanceVariableMayNotBeInitialized", "UnusedDeclaration", "NullableProblems"}) @NotNull private UiModelAccessor f;

    //~ Methods ..................................................................................................................

    /** Invoked when populating a form instance */
    @NotNull public final Void populate() {
        throw new FormCannotBePopulatedException("tekgenesis.showcase.ErrorShowcase");
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

    /** Invoked when button(persistentMessage) is clicked */
    @NotNull public abstract Action persistentMessage();

    /** Invoked when button(autoCloseMessage) is clicked */
    @NotNull public abstract Action autoCloseMessage();

    /** Invoked when button(errorWithSuccess) is clicked */
    @NotNull public abstract Action errorSuccessMessage();

    /** Invoked when button(errorWithWarning) is clicked */
    @NotNull public abstract Action errorWarningMessage();

    /** Invoked when button(errorAutoClose) is clicked */
    @NotNull public abstract Action errorAutoClose();

    /** Invoked when button(defaultError) is clicked */
    @NotNull public abstract Action defaultWithErrorAutoclosing();

    /** Invoked when button(defaultWarning) is clicked */
    @NotNull public abstract Action defaultWarningPersistent();

    /** Invoked when button(navigateNormal) is clicked */
    @NotNull public abstract Action navigateNormal();

    /** Invoked when button(navigateError) is clicked */
    @NotNull public abstract Action navigatewithError();

    /** Invoked when button(navigateWarning) is clicked */
    @NotNull public abstract Action navigatewithWarning();

    /** Invoked when button(button) is clicked */
    @NotNull public abstract Action error();

    /** Invoked when button(logout) is clicked */
    @NotNull public abstract Action killSession();

    /** Register a form Listener */
    public static void addListener(@NotNull FormListenerType listenerType, @NotNull FormListener<ErrorShowcase> listener) { listeners.addListener(listenerType, listener); }

    /** Remove a form Listener */
    public static void removeListener(@NotNull FormListenerType listenerType, @NotNull FormListener<ErrorShowcase> listener) { listeners.removeListener(listenerType, listener); }

    @Override @NotNull public Logger logger() { return logger; }

    /** Returns utility methods to deal with {@link Action actions}. */
    @NotNull protected final Actions actions() { return actions; }

    //~ Fields ...................................................................................................................

    @NotNull private static FormListenerMap<ErrorShowcase> listeners = new FormListenerMap<>();
    @NotNull private static final Logger logger = Logger.getLogger(ErrorShowcase.class);

    //~ Inner Classes ............................................................................................................

    @SuppressWarnings("UnnecessarySemicolon")
    public enum Field
        implements tekgenesis.metadata.form.widget.FormFieldRef
    {
        $H0("$H0"),
        $M1("$M1"),
        $V2("$V2"),
        PERSISTENT_MESSAGE("persistentMessage"),
        AUTO_CLOSE_MESSAGE("autoCloseMessage"),
        ERROR_WITH_SUCCESS("errorWithSuccess"),
        ERROR_WITH_WARNING("errorWithWarning"),
        ERROR_AUTO_CLOSE("errorAutoClose"),
        DEFAULT_ERROR("defaultError"),
        DEFAULT_WARNING("defaultWarning"),
        $V3("$V3"),
        NAVIGATE_NORMAL("navigateNormal"),
        NAVIGATE_ERROR("navigateError"),
        NAVIGATE_WARNING("navigateWarning"),
        BUTTON("button"),
        LOGOUT("logout"),
        $B4("$B4");

        //~ Fields ...................................................................................................................

        @Nullable private final String id;

        //~ Constructors .............................................................................................................

        Field(@Nullable final String id) { this.id = id; }

        //~ Methods ..................................................................................................................

        @NotNull public String id() { return id; }

    }
}
