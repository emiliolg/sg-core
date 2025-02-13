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
 * Generated base class for form: TimePickerForm.
 * Don't modify this as this is an auto generated class that's gets generated
 * every time the meta model file is modified. Use subclass instead.
 */
@SuppressWarnings({"DuplicateStringLiteralInspection", "WeakerAccess", "FieldMayBeFinal", "FieldCanBeLocal", "ConstantConditions", "ClassWithTooManyMethods", "ClassWithTooManyFields", "MagicNumber", "MethodOverridesStaticMethodOfSuperclass", "ParameterHidesMemberVariable", "FieldNameHidesFieldInSuperclass", "OverlyComplexClass", "RedundantCast", "UnusedReturnValue", "LocalVariableHidesMemberVariable", "UnusedParameters", "EmptyMethod"})
public abstract class TimePickerFormBase
    extends FormInstance<Void>
    implements LoggableInstance
{

    //~ Fields ...................................................................................................................

    @SuppressWarnings({"InstanceVariableMayNotBeInitialized", "UnusedDeclaration", "NullableProblems"}) @NotNull private UiModelAccessor f;

    //~ Methods ..................................................................................................................

    /** Invoked when populating a form instance */
    @NotNull public final Void populate() {
        throw new FormCannotBePopulatedException("tekgenesis.showcase.TimePickerForm");
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

    /** Returns the value of the text_field(seconds). */
    @Nullable public Integer getSeconds() { return f.get(Field.SECONDS, Integer.class); }

    /** Sets the value of the text_field(seconds). */
    @NotNull public TimePickerForm setSeconds(@Nullable Integer seconds) {
        f.set(Field.SECONDS, seconds);
        return (TimePickerForm) this;
    }

    /** 
     * Invoked when time_picker(timePicker) value ui changes
     * Invoked when time_picker(timePickerStep5) value ui changes
     * Invoked when time_picker(timePickerStep15) value ui changes
     * Invoked when time_picker(from) value ui changes
     * Invoked when time_picker(to) value ui changes
     */
    @NotNull public abstract Action changed();

    /** Returns the value of the time_picker(timePicker). */
    public int getTimePicker() { return f.get(Field.TIME_PICKER, Integer.class); }

    /** Returns the value of the time_picker(timePickerStep5). */
    public int getTimePickerStep5() { return f.get(Field.TIME_PICKER_STEP5, Integer.class); }

    /** Sets the value of the time_picker(timePickerStep5). */
    @NotNull public TimePickerForm setTimePickerStep5(int timePickerStep5) {
        f.set(Field.TIME_PICKER_STEP5, timePickerStep5);
        return (TimePickerForm) this;
    }

    /** Returns the value of the time_picker(timePickerStep15). */
    public int getTimePickerStep15() { return f.get(Field.TIME_PICKER_STEP15, Integer.class); }

    /** Sets the value of the time_picker(timePickerStep15). */
    @NotNull public TimePickerForm setTimePickerStep15(int timePickerStep15) {
        f.set(Field.TIME_PICKER_STEP15, timePickerStep15);
        return (TimePickerForm) this;
    }

    /** Returns the value of the time_picker(from). */
    public int getFrom() { return f.get(Field.FROM, Integer.class); }

    /** Sets the value of the time_picker(from). */
    @NotNull public TimePickerForm setFrom(int from) {
        f.set(Field.FROM, from);
        return (TimePickerForm) this;
    }

    /** Returns the value of the time_picker(to). */
    public int getTo() { return f.get(Field.TO, Integer.class); }

    /** Sets the value of the time_picker(to). */
    @NotNull public TimePickerForm setTo(int to) {
        f.set(Field.TO, to);
        return (TimePickerForm) this;
    }

    /** Invoked when time_picker(withTz) value ui changes */
    @NotNull public abstract Action tzAwareValue();

    /** Returns the value of the time_picker(withTz). */
    public int getWithTz() { return f.get(Field.WITH_TZ, Integer.class); }

    /** Sets the value of the time_picker(withTz). */
    @NotNull public TimePickerForm setWithTz(int withTz) {
        f.set(Field.WITH_TZ, withTz);
        return (TimePickerForm) this;
    }

    /** Register a form Listener */
    public static void addListener(@NotNull FormListenerType listenerType, @NotNull FormListener<TimePickerForm> listener) { listeners.addListener(listenerType, listener); }

    /** Remove a form Listener */
    public static void removeListener(@NotNull FormListenerType listenerType, @NotNull FormListener<TimePickerForm> listener) { listeners.removeListener(listenerType, listener); }

    @Override @NotNull public Logger logger() { return logger; }

    /** Returns utility methods to deal with {@link Action actions}. */
    @NotNull protected final Actions actions() { return actions; }

    //~ Fields ...................................................................................................................

    @NotNull private static FormListenerMap<TimePickerForm> listeners = new FormListenerMap<>();
    @NotNull private static final Logger logger = Logger.getLogger(TimePickerForm.class);

    //~ Inner Classes ............................................................................................................

    @SuppressWarnings("UnnecessarySemicolon")
    public enum Field
        implements tekgenesis.metadata.form.widget.FormFieldRef
    {
        $H0("$H0"),
        $M1("$M1"),
        SECONDS("seconds"),
        TIME_PICKER("timePicker"),
        TIME_PICKER_STEP5("timePickerStep5"),
        TIME_PICKER_STEP15("timePickerStep15"),
        FROM("from"),
        TO("to"),
        WITH_TZ("withTz");

        //~ Fields ...................................................................................................................

        @Nullable private final String id;

        //~ Constructors .............................................................................................................

        Field(@Nullable final String id) { this.id = id; }

        //~ Methods ..................................................................................................................

        @NotNull public String id() { return id; }

    }
}
