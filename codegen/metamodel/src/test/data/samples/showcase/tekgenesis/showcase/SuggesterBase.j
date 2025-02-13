package tekgenesis.showcase;

import tekgenesis.form.Action;
import tekgenesis.form.Actions;
import java.util.ArrayList;
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
import tekgenesis.common.Predefined;
import tekgenesis.form.Suggestion;
import tekgenesis.form.UiModelAccessor;
import tekgenesis.form.configuration.WidgetConfiguration;

/** 
 * Generated base class for form: Suggester.
 * Don't modify this as this is an auto generated class that's gets generated
 * every time the meta model file is modified. Use subclass instead.
 */
@SuppressWarnings({"DuplicateStringLiteralInspection", "WeakerAccess", "FieldMayBeFinal", "FieldCanBeLocal", "ConstantConditions", "ClassWithTooManyMethods", "ClassWithTooManyFields", "MagicNumber", "MethodOverridesStaticMethodOfSuperclass", "ParameterHidesMemberVariable", "FieldNameHidesFieldInSuperclass", "OverlyComplexClass", "RedundantCast", "UnusedReturnValue", "LocalVariableHidesMemberVariable", "UnusedParameters", "EmptyMethod"})
public abstract class SuggesterBase
    extends FormInstance<Void>
    implements LoggableInstance
{

    //~ Fields ...................................................................................................................

    @SuppressWarnings({"InstanceVariableMayNotBeInitialized", "UnusedDeclaration", "NullableProblems"}) @NotNull private UiModelAccessor f;

    //~ Methods ..................................................................................................................

    /** Invoked when populating a form instance */
    @NotNull public final Void populate() {
        throw new FormCannotBePopulatedException("tekgenesis.showcase.Suggester");
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

    /** Returns the value of the internal(date). */
    @NotNull public DateOnly getDate() {
        return DateOnly.fromMilliseconds(f.get(Field.DATE, Long.class));
    }

    /** Sets the value of the internal(date). */
    @NotNull public Suggester setDate(@NotNull DateOnly date) {
        f.set(Field.DATE, date);
        return (Suggester) this;
    }

    /** Invoked when the user type something on suggest_box(simpleSync) to create suggest list */
    @NotNull public abstract Iterable<SimpleEntity> suggestSimpleSync(@Nullable String query);

    /** Returns the value of the suggest_box(simpleSync). */
    @NotNull public SimpleEntity getSimpleSync() {
        return Predefined.ensureNotNull(SimpleEntity.find(getSimpleSyncKey()), "'simpleSync' not found");
    }

    /** Returns the key value of the suggest_box(simpleSync). */
    @NotNull public String getSimpleSyncKey() { return f.get(Field.SIMPLE_SYNC, String.class); }

    /** Sets the value of the suggest_box(simpleSync). */
    @NotNull public Suggester setSimpleSync(@NotNull SimpleEntity simpleSync) {
        f.set(Field.SIMPLE_SYNC, simpleSync);
        return (Suggester) this;
    }

    /** Returns the value of the suggest_box(simple). */
    @NotNull public SimpleEntity getSimple() {
        return Predefined.ensureNotNull(SimpleEntity.find(getSimpleKey()), "'simple' not found");
    }

    /** Returns the key value of the suggest_box(simple). */
    @NotNull public String getSimpleKey() { return f.get(Field.SIMPLE, String.class); }

    /** Sets the value of the suggest_box(simple). */
    @NotNull public Suggester setSimple(@NotNull SimpleEntity simple) {
        f.set(Field.SIMPLE, simple);
        return (Suggester) this;
    }

    /** Returns the value of the suggest_box(simpleParam). */
    @NotNull public SimpleEntity getSimpleParam() {
        return Predefined.ensureNotNull(SimpleEntity.find(getSimpleParamKey()), "'simpleParam' not found");
    }

    /** Returns the key value of the suggest_box(simpleParam). */
    @NotNull public String getSimpleParamKey() { return f.get(Field.SIMPLE_PARAM, String.class); }

    /** Sets the value of the suggest_box(simpleParam). */
    @NotNull public Suggester setSimpleParam(@NotNull SimpleEntity simpleParam) {
        f.set(Field.SIMPLE_PARAM, simpleParam);
        return (Suggester) this;
    }

    /** Invoked when the user type something on suggest_box(stringSync) to create suggest list */
    @NotNull public abstract Iterable<Suggestion> suggestStringSync(@Nullable String query);

    /** Returns the value of the suggest_box(stringSync). */
    @NotNull public String getStringSync() { return f.get(Field.STRING_SYNC, String.class); }

    /** Sets the value of the suggest_box(stringSync). */
    @NotNull public Suggester setStringSync(@NotNull String stringSync) {
        f.set(Field.STRING_SYNC, stringSync);
        return (Suggester) this;
    }

    /** Sets the value of the suggest_box(stringSync). */
    @NotNull public Suggester setStringSync(@NotNull Suggestion stringSync) {
        f.set(Field.STRING_SYNC, stringSync);
        return (Suggester) this;
    }

    /** Returns the value of the suggest_box(string). */
    @NotNull public String getString() { return f.get(Field.STRING, String.class); }

    /** Sets the value of the suggest_box(string). */
    @NotNull public Suggester setString(@NotNull String string) {
        f.set(Field.STRING, string);
        return (Suggester) this;
    }

    /** Sets the value of the suggest_box(string). */
    @NotNull public Suggester setString(@NotNull Suggestion string) {
        f.set(Field.STRING, string);
        return (Suggester) this;
    }

    /** Returns the value of the suggest_box(stringParam). */
    @NotNull public String getStringParam() { return f.get(Field.STRING_PARAM, String.class); }

    /** Sets the value of the suggest_box(stringParam). */
    @NotNull public Suggester setStringParam(@NotNull String stringParam) {
        f.set(Field.STRING_PARAM, stringParam);
        return (Suggester) this;
    }

    /** Sets the value of the suggest_box(stringParam). */
    @NotNull public Suggester setStringParam(@NotNull Suggestion stringParam) {
        f.set(Field.STRING_PARAM, stringParam);
        return (Suggester) this;
    }

    /** Register a form Listener */
    public static void addListener(@NotNull FormListenerType listenerType, @NotNull FormListener<Suggester> listener) { listeners.addListener(listenerType, listener); }

    /** Remove a form Listener */
    public static void removeListener(@NotNull FormListenerType listenerType, @NotNull FormListener<Suggester> listener) { listeners.removeListener(listenerType, listener); }

    @Override @NotNull public Logger logger() { return logger; }

    /** Returns utility methods to deal with {@link Action actions}. */
    @NotNull protected final Actions actions() { return actions; }

    //~ Fields ...................................................................................................................

    @NotNull private static FormListenerMap<Suggester> listeners = new FormListenerMap<>();
    @NotNull private static final Logger logger = Logger.getLogger(Suggester.class);

    //~ Inner Classes ............................................................................................................

    @SuppressWarnings("UnnecessarySemicolon")
    public enum Field
        implements tekgenesis.metadata.form.widget.FormFieldRef
    {
        $H0("$H0"),
        $M1("$M1"),
        DATE("date"),
        SIMPLE_SYNC("simpleSync"),
        SIMPLE("simple"),
        SIMPLE_PARAM("simpleParam"),
        STRING_SYNC("stringSync"),
        STRING("string"),
        STRING_PARAM("stringParam");

        //~ Fields ...................................................................................................................

        @Nullable private final String id;

        //~ Constructors .............................................................................................................

        Field(@Nullable final String id) { this.id = id; }

        //~ Methods ..................................................................................................................

        @NotNull public String id() { return id; }

    }
}
