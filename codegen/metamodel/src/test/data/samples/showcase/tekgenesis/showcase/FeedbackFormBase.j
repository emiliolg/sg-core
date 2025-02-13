package tekgenesis.showcase;

import tekgenesis.form.Action;
import tekgenesis.form.Actions;
import tekgenesis.form.ExecutionFeedback;
import tekgenesis.form.exception.FormCannotBePopulatedException;
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
import tekgenesis.form.UiModelAccessor;
import tekgenesis.form.configuration.WidgetConfiguration;

/** 
 * Generated base class for form: FeedbackForm.
 * Don't modify this as this is an auto generated class that's gets generated
 * every time the meta model file is modified. Use subclass instead.
 */
@SuppressWarnings({"DuplicateStringLiteralInspection", "WeakerAccess", "FieldMayBeFinal", "FieldCanBeLocal", "ConstantConditions", "ClassWithTooManyMethods", "ClassWithTooManyFields", "MagicNumber", "MethodOverridesStaticMethodOfSuperclass", "ParameterHidesMemberVariable", "FieldNameHidesFieldInSuperclass", "OverlyComplexClass", "RedundantCast", "UnusedReturnValue", "LocalVariableHidesMemberVariable", "UnusedParameters", "EmptyMethod"})
public abstract class FeedbackFormBase
    extends FormInstance<Void>
    implements LoggableInstance
{

    //~ Fields ...................................................................................................................

    @SuppressWarnings({"InstanceVariableMayNotBeInitialized", "UnusedDeclaration", "NullableProblems"}) @NotNull private UiModelAccessor f;

    //~ Methods ..................................................................................................................

    /** Invoked when populating a form instance */
    @NotNull public final Void populate() {
        throw new FormCannotBePopulatedException("tekgenesis.showcase.FeedbackForm");
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

    /** Returns the value of the internal(innerMessage). */
    @Nullable public String getInnerMessage() { return f.get(Field.INNER_MESSAGE, String.class); }

    /** Sets the value of the internal(innerMessage). */
    @NotNull public FeedbackForm setInnerMessage(@Nullable String innerMessage) {
        f.set(Field.INNER_MESSAGE, innerMessage);
        return (FeedbackForm) this;
    }

    /** Invoked when button(button) is clicked */
    @NotNull public abstract Action feedbackAction(@NotNull ExecutionFeedback feedback);

    /** Invoked when button(button2) is clicked */
    @NotNull public abstract Action feedbackException(@NotNull ExecutionFeedback feedback);

    /** Invoked when button(asd) is clicked */
    @NotNull public abstract Action simpleClick();

    /** Returns the value of the combo_box(combo). */
    @NotNull public Options getCombo() { return Options.valueOf(f.get(Field.COMBO, String.class)); }

    /** Sets the value of the combo_box(combo). */
    @NotNull public FeedbackForm setCombo(@NotNull Options combo) {
        f.set(Field.COMBO, combo);
        return (FeedbackForm) this;
    }

    /** Sets the options of the combo_box(combo). */
    public void setComboOptions(@NotNull Iterable<Options> items) { f.opts(Field.COMBO, items); }

    /** Sets the options of the combo_box(combo) with the given KeyMap. */
    public void setComboOptions(@NotNull KeyMap items) { f.opts(Field.COMBO, items); }

    /** Register a form Listener */
    public static void addListener(@NotNull FormListenerType listenerType, @NotNull FormListener<FeedbackForm> listener) { listeners.addListener(listenerType, listener); }

    /** Remove a form Listener */
    public static void removeListener(@NotNull FormListenerType listenerType, @NotNull FormListener<FeedbackForm> listener) { listeners.removeListener(listenerType, listener); }

    @Override @NotNull public Logger logger() { return logger; }

    /** Returns utility methods to deal with {@link Action actions}. */
    @NotNull protected final Actions actions() { return actions; }

    //~ Fields ...................................................................................................................

    @NotNull private static FormListenerMap<FeedbackForm> listeners = new FormListenerMap<>();
    @NotNull private static final Logger logger = Logger.getLogger(FeedbackForm.class);

    //~ Inner Classes ............................................................................................................

    @SuppressWarnings("UnnecessarySemicolon")
    public enum Field
        implements tekgenesis.metadata.form.widget.FormFieldRef
    {
        $H0("$H0"),
        $M1("$M1"),
        INNER_MESSAGE("innerMessage"),
        $M2("$M2"),
        BUTTON("button"),
        BUTTON2("button2"),
        ASD("asd"),
        COMBO("combo");

        //~ Fields ...................................................................................................................

        @Nullable private final String id;

        //~ Constructors .............................................................................................................

        Field(@Nullable final String id) { this.id = id; }

        //~ Methods ..................................................................................................................

        @NotNull public String id() { return id; }

    }
}
