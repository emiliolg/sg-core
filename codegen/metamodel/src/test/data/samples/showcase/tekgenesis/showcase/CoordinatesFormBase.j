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
 * Generated base class for form: CoordinatesForm.
 * Don't modify this as this is an auto generated class that's gets generated
 * every time the meta model file is modified. Use subclass instead.
 */
@SuppressWarnings({"DuplicateStringLiteralInspection", "WeakerAccess", "FieldMayBeFinal", "FieldCanBeLocal", "ConstantConditions", "ClassWithTooManyMethods", "ClassWithTooManyFields", "MagicNumber", "MethodOverridesStaticMethodOfSuperclass", "ParameterHidesMemberVariable", "FieldNameHidesFieldInSuperclass", "OverlyComplexClass", "RedundantCast", "UnusedReturnValue", "LocalVariableHidesMemberVariable", "UnusedParameters", "EmptyMethod"})
public abstract class CoordinatesFormBase
    extends FormInstance<Void>
    implements LoggableInstance
{

    //~ Fields ...................................................................................................................

    @SuppressWarnings({"InstanceVariableMayNotBeInitialized", "UnusedDeclaration", "NullableProblems"}) @NotNull private UiModelAccessor f;

    //~ Methods ..................................................................................................................

    /** Invoked when populating a form instance */
    @NotNull public final Void populate() {
        throw new FormCannotBePopulatedException("tekgenesis.showcase.CoordinatesForm");
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

    /** Define {@link CoordinateWidget instance} to be used during interaction. */
    @NotNull abstract CoordinateWidget defineFrom();

    /** Get widget {@link CoordinateWidget}. */
    @NotNull public CoordinateWidget getFrom() { return f.widget(Field.FROM, CoordinateWidget.class); }

    /** Define {@link CoordinateWidget instance} to be used during interaction. */
    @NotNull abstract CoordinateWidget defineTo();

    /** Get widget {@link CoordinateWidget}. */
    @NotNull public CoordinateWidget getTo() { return f.widget(Field.TO, CoordinateWidget.class); }

    /** Returns the value of the display(latDiff). */
    public int getLatDiff() { return f.get(Field.LAT_DIFF, Integer.class); }

    /** Returns the value of the display(lngDiff). */
    public int getLngDiff() { return f.get(Field.LNG_DIFF, Integer.class); }

    /** Returns the value of the display(fromCount). */
    public int getFromCount() { return f.get(Field.FROM_COUNT, Integer.class); }

    /** Sets the value of the display(fromCount). */
    @NotNull public CoordinatesForm setFromCount(int fromCount) {
        f.set(Field.FROM_COUNT, fromCount);
        return (CoordinatesForm) this;
    }

    /** Returns the value of the display(toCount). */
    public int getToCount() { return f.get(Field.TO_COUNT, Integer.class); }

    /** Sets the value of the display(toCount). */
    @NotNull public CoordinatesForm setToCount(int toCount) {
        f.set(Field.TO_COUNT, toCount);
        return (CoordinatesForm) this;
    }

    /** Register a form Listener */
    public static void addListener(@NotNull FormListenerType listenerType, @NotNull FormListener<CoordinatesForm> listener) { listeners.addListener(listenerType, listener); }

    /** Remove a form Listener */
    public static void removeListener(@NotNull FormListenerType listenerType, @NotNull FormListener<CoordinatesForm> listener) { listeners.removeListener(listenerType, listener); }

    @Override @NotNull public Logger logger() { return logger; }

    /** Returns utility methods to deal with {@link Action actions}. */
    @NotNull protected final Actions actions() { return actions; }

    //~ Fields ...................................................................................................................

    @NotNull private static FormListenerMap<CoordinatesForm> listeners = new FormListenerMap<>();
    @NotNull private static final Logger logger = Logger.getLogger(CoordinatesForm.class);

    //~ Inner Classes ............................................................................................................

    @SuppressWarnings("UnnecessarySemicolon")
    public enum Field
        implements tekgenesis.metadata.form.widget.FormFieldRef
    {
        FROM("from"),
        TO("to"),
        LAT_DIFF("latDiff"),
        LNG_DIFF("lngDiff"),
        FROM_COUNT("fromCount"),
        TO_COUNT("toCount");

        //~ Fields ...................................................................................................................

        @Nullable private final String id;

        //~ Constructors .............................................................................................................

        Field(@Nullable final String id) { this.id = id; }

        //~ Methods ..................................................................................................................

        @NotNull public String id() { return id; }

    }
}
