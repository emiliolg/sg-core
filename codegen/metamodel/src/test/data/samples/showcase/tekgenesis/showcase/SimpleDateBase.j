package tekgenesis.showcase;

import tekgenesis.form.Action;
import tekgenesis.form.Actions;
import tekgenesis.common.core.DateOnly;
import tekgenesis.common.core.DateTime;
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
 * Generated base class for form: SimpleDate.
 * Don't modify this as this is an auto generated class that's gets generated
 * every time the meta model file is modified. Use subclass instead.
 */
@SuppressWarnings({"DuplicateStringLiteralInspection", "WeakerAccess", "FieldMayBeFinal", "FieldCanBeLocal", "ConstantConditions", "ClassWithTooManyMethods", "ClassWithTooManyFields", "MagicNumber", "MethodOverridesStaticMethodOfSuperclass", "ParameterHidesMemberVariable", "FieldNameHidesFieldInSuperclass", "OverlyComplexClass", "RedundantCast", "UnusedReturnValue", "LocalVariableHidesMemberVariable", "UnusedParameters", "EmptyMethod"})
public abstract class SimpleDateBase
    extends FormInstance<Void>
    implements LoggableInstance
{

    //~ Fields ...................................................................................................................

    @SuppressWarnings({"InstanceVariableMayNotBeInitialized", "UnusedDeclaration", "NullableProblems"}) @NotNull private UiModelAccessor f;

    //~ Methods ..................................................................................................................

    /** Invoked when populating a form instance */
    @NotNull public final Void populate() {
        throw new FormCannotBePopulatedException("tekgenesis.showcase.SimpleDate");
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

    /** Returns the value of the date_box(dateBox). */
    @NotNull public DateOnly getDateBox() {
        return DateOnly.fromMilliseconds(f.get(Field.DATE_BOX, Long.class));
    }

    /** Sets the value of the date_box(dateBox). */
    @NotNull public SimpleDate setDateBox(@NotNull DateOnly dateBox) {
        f.set(Field.DATE_BOX, dateBox);
        return (SimpleDate) this;
    }

    /** Returns the value of the double_date_box(doubleDateBox). */
    @NotNull public DateOnly getDoubleDateBox() {
        return DateOnly.fromMilliseconds(f.get(Field.DOUBLE_DATE_BOX, Long.class));
    }

    /** Sets the value of the double_date_box(doubleDateBox). */
    @NotNull public SimpleDate setDoubleDateBox(@NotNull DateOnly doubleDateBox) {
        f.set(Field.DOUBLE_DATE_BOX, doubleDateBox);
        return (SimpleDate) this;
    }

    /** Returns the value of the date_time_box(dateTimeBox). */
    @NotNull public DateTime getDateTimeBox() {
        return DateTime.fromMilliseconds(f.get(Field.DATE_TIME_BOX, Long.class));
    }

    /** Sets the value of the date_time_box(dateTimeBox). */
    @NotNull public SimpleDate setDateTimeBox(@NotNull DateTime dateTimeBox) {
        f.set(Field.DATE_TIME_BOX, dateTimeBox);
        return (SimpleDate) this;
    }

    /** Returns the value of the date_time_box(dateTimeBoxReset). */
    @NotNull public DateTime getDateTimeBoxReset() {
        return DateTime.fromMilliseconds(f.get(Field.DATE_TIME_BOX_RESET, Long.class));
    }

    /** Sets the value of the date_time_box(dateTimeBoxReset). */
    @NotNull public SimpleDate setDateTimeBoxReset(@NotNull DateTime dateTimeBoxReset) {
        f.set(Field.DATE_TIME_BOX_RESET, dateTimeBoxReset);
        return (SimpleDate) this;
    }

    /** Register a form Listener */
    public static void addListener(@NotNull FormListenerType listenerType, @NotNull FormListener<SimpleDate> listener) { listeners.addListener(listenerType, listener); }

    /** Remove a form Listener */
    public static void removeListener(@NotNull FormListenerType listenerType, @NotNull FormListener<SimpleDate> listener) { listeners.removeListener(listenerType, listener); }

    @Override @NotNull public Logger logger() { return logger; }

    /** Returns utility methods to deal with {@link Action actions}. */
    @NotNull protected final Actions actions() { return actions; }

    //~ Fields ...................................................................................................................

    @NotNull private static FormListenerMap<SimpleDate> listeners = new FormListenerMap<>();
    @NotNull private static final Logger logger = Logger.getLogger(SimpleDate.class);

    //~ Inner Classes ............................................................................................................

    @SuppressWarnings("UnnecessarySemicolon")
    public enum Field
        implements tekgenesis.metadata.form.widget.FormFieldRef
    {
        $H0("$H0"),
        $M1("$M1"),
        VIEW_SOURCE("viewSource"),
        DATE_BOX("dateBox"),
        DOUBLE_DATE_BOX("doubleDateBox"),
        DATE_TIME_BOX("dateTimeBox"),
        DATE_TIME_BOX_RESET("dateTimeBoxReset");

        //~ Fields ...................................................................................................................

        @Nullable private final String id;

        //~ Constructors .............................................................................................................

        Field(@Nullable final String id) { this.id = id; }

        //~ Methods ..................................................................................................................

        @NotNull public String id() { return id; }

    }
}
