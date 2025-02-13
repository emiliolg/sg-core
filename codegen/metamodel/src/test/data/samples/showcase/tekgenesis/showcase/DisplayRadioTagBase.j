package tekgenesis.showcase;

import tekgenesis.form.Action;
import tekgenesis.form.Actions;
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
import tekgenesis.common.Predefined;
import tekgenesis.form.UiModelAccessor;
import tekgenesis.form.configuration.WidgetConfiguration;

/** 
 * Generated base class for form: DisplayRadioTag.
 * Don't modify this as this is an auto generated class that's gets generated
 * every time the meta model file is modified. Use subclass instead.
 */
@SuppressWarnings({"DuplicateStringLiteralInspection", "WeakerAccess", "FieldMayBeFinal", "FieldCanBeLocal", "ConstantConditions", "ClassWithTooManyMethods", "ClassWithTooManyFields", "MagicNumber", "MethodOverridesStaticMethodOfSuperclass", "ParameterHidesMemberVariable", "FieldNameHidesFieldInSuperclass", "OverlyComplexClass", "RedundantCast", "UnusedReturnValue", "LocalVariableHidesMemberVariable", "UnusedParameters", "EmptyMethod"})
public abstract class DisplayRadioTagBase
    extends FormInstance<Void>
    implements LoggableInstance
{

    //~ Fields ...................................................................................................................

    @SuppressWarnings({"InstanceVariableMayNotBeInitialized", "UnusedDeclaration", "NullableProblems"}) @NotNull private UiModelAccessor f;

    //~ Methods ..................................................................................................................

    /** Invoked when populating a form instance */
    @NotNull public final Void populate() {
        throw new FormCannotBePopulatedException("tekgenesis.showcase.DisplayRadioTag");
    }

    /** Invoked when the form is loaded */
    public abstract void load();

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

    /** Invoked when check_box(checkBox) value ui changes */
    @NotNull public abstract Action changeStyles();

    /** Returns the value of the check_box(checkBox). */
    public boolean isCheckBox() { return f.get(Field.CHECK_BOX, Boolean.class); }

    /** Sets the value of the check_box(checkBox). */
    @NotNull public DisplayRadioTag setCheckBox(boolean checkBox) {
        f.set(Field.CHECK_BOX, checkBox);
        return (DisplayRadioTag) this;
    }

    /** Invoked when radio_group(radioTags) value changes */
    @NotNull public abstract Action changed();

    /** Returns the value of the radio_group(radioTags). */
    @NotNull public SimpleEntity getRadioTags() {
        return Predefined.ensureNotNull(SimpleEntity.find(getRadioTagsKey()), "'radioTags' not found");
    }

    /** Returns the key value of the radio_group(radioTags). */
    @NotNull public String getRadioTagsKey() { return f.get(Field.RADIO_TAGS, String.class); }

    /** Sets the value of the radio_group(radioTags). */
    @NotNull public DisplayRadioTag setRadioTags(@NotNull SimpleEntity radioTags) {
        f.set(Field.RADIO_TAGS, radioTags);
        return (DisplayRadioTag) this;
    }

    /** Sets the options of the radio_group(radioTags). */
    public void setRadioTagsOptions(@NotNull Iterable<? extends SimpleEntity> items) { f.opts(Field.RADIO_TAGS, items); }

    /** Sets the options of the radio_group(radioTags) with the given KeyMap. */
    public void setRadioTagsOptions(@NotNull KeyMap items) { f.opts(Field.RADIO_TAGS, items); }

    /** Register a form Listener */
    public static void addListener(@NotNull FormListenerType listenerType, @NotNull FormListener<DisplayRadioTag> listener) { listeners.addListener(listenerType, listener); }

    /** Remove a form Listener */
    public static void removeListener(@NotNull FormListenerType listenerType, @NotNull FormListener<DisplayRadioTag> listener) { listeners.removeListener(listenerType, listener); }

    @Override @NotNull public Logger logger() { return logger; }

    /** Returns utility methods to deal with {@link Action actions}. */
    @NotNull protected final Actions actions() { return actions; }

    //~ Fields ...................................................................................................................

    @NotNull private static FormListenerMap<DisplayRadioTag> listeners = new FormListenerMap<>();
    @NotNull private static final Logger logger = Logger.getLogger(DisplayRadioTag.class);

    //~ Inner Classes ............................................................................................................

    @SuppressWarnings("UnnecessarySemicolon")
    public enum Field
        implements tekgenesis.metadata.form.widget.FormFieldRef
    {
        $H0("$H0"),
        $M1("$M1"),
        VIEW_SOURCE("viewSource"),
        CHECK_BOX("checkBox"),
        RADIO_TAGS("radioTags");

        //~ Fields ...................................................................................................................

        @Nullable private final String id;

        //~ Constructors .............................................................................................................

        Field(@Nullable final String id) { this.id = id; }

        //~ Methods ..................................................................................................................

        @NotNull public String id() { return id; }

    }
}
