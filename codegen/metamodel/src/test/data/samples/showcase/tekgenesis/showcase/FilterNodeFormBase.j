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
import tekgenesis.common.Predefined;
import tekgenesis.form.UiModelAccessor;
import tekgenesis.form.configuration.WidgetConfiguration;

/** 
 * Generated base class for form: FilterNodeForm.
 * Don't modify this as this is an auto generated class that's gets generated
 * every time the meta model file is modified. Use subclass instead.
 */
@SuppressWarnings({"DuplicateStringLiteralInspection", "WeakerAccess", "FieldMayBeFinal", "FieldCanBeLocal", "ConstantConditions", "ClassWithTooManyMethods", "ClassWithTooManyFields", "MagicNumber", "MethodOverridesStaticMethodOfSuperclass", "ParameterHidesMemberVariable", "FieldNameHidesFieldInSuperclass", "OverlyComplexClass", "RedundantCast", "UnusedReturnValue", "LocalVariableHidesMemberVariable", "UnusedParameters", "EmptyMethod"})
public abstract class FilterNodeFormBase
    extends FormInstance<Void>
    implements LoggableInstance
{

    //~ Fields ...................................................................................................................

    @SuppressWarnings({"InstanceVariableMayNotBeInitialized", "UnusedDeclaration", "NullableProblems"}) @NotNull private UiModelAccessor f;

    //~ Methods ..................................................................................................................

    /** Invoked when populating a form instance */
    @NotNull public final Void populate() {
        throw new FormCannotBePopulatedException("tekgenesis.showcase.FilterNodeForm");
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

    /** Returns the value of the text_field(filter1). */
    @NotNull public String getFilter1() { return f.get(Field.FILTER1, String.class); }

    /** Sets the value of the text_field(filter1). */
    @NotNull public FilterNodeForm setFilter1(@NotNull String filter1) {
        f.set(Field.FILTER1, filter1);
        return (FilterNodeForm) this;
    }

    /** Returns the value of the text_field(filter2). */
    @NotNull public String getFilter2() { return f.get(Field.FILTER2, String.class); }

    /** Sets the value of the text_field(filter2). */
    @NotNull public FilterNodeForm setFilter2(@NotNull String filter2) {
        f.set(Field.FILTER2, filter2);
        return (FilterNodeForm) this;
    }

    /** Returns the value of the suggest_box(box). */
    @NotNull public Node getBox() {
        return Predefined.ensureNotNull(Node.find(getBoxKey()), "'box' not found");
    }

    /** Returns the key value of the suggest_box(box). */
    @NotNull public String getBoxKey() { return f.get(Field.BOX, String.class); }

    /** Sets the value of the suggest_box(box). */
    @NotNull public FilterNodeForm setBox(@NotNull Node box) {
        f.set(Field.BOX, box);
        return (FilterNodeForm) this;
    }

    /** Returns the value of the internal(boxString). */
    @NotNull public String getBoxString() { return f.get(Field.BOX_STRING, String.class); }

    /** Returns the value of the suggest_box(box2). */
    @NotNull public Node getBox2() {
        return Predefined.ensureNotNull(Node.find(getBox2Key()), "'box2' not found");
    }

    /** Returns the key value of the suggest_box(box2). */
    @NotNull public String getBox2Key() { return f.get(Field.BOX2, String.class); }

    /** Sets the value of the suggest_box(box2). */
    @NotNull public FilterNodeForm setBox2(@NotNull Node box2) {
        f.set(Field.BOX2, box2);
        return (FilterNodeForm) this;
    }

    /** Register a form Listener */
    public static void addListener(@NotNull FormListenerType listenerType, @NotNull FormListener<FilterNodeForm> listener) { listeners.addListener(listenerType, listener); }

    /** Remove a form Listener */
    public static void removeListener(@NotNull FormListenerType listenerType, @NotNull FormListener<FilterNodeForm> listener) { listeners.removeListener(listenerType, listener); }

    @Override @NotNull public Logger logger() { return logger; }

    /** Returns utility methods to deal with {@link Action actions}. */
    @NotNull protected final Actions actions() { return actions; }

    //~ Fields ...................................................................................................................

    @NotNull private static FormListenerMap<FilterNodeForm> listeners = new FormListenerMap<>();
    @NotNull private static final Logger logger = Logger.getLogger(FilterNodeForm.class);

    //~ Inner Classes ............................................................................................................

    @SuppressWarnings("UnnecessarySemicolon")
    public enum Field
        implements tekgenesis.metadata.form.widget.FormFieldRef
    {
        $H0("$H0"),
        $M1("$M1"),
        FILTER1("filter1"),
        FILTER2("filter2"),
        BOX("box"),
        BOX_STRING("boxString"),
        BOX2("box2");

        //~ Fields ...................................................................................................................

        @Nullable private final String id;

        //~ Constructors .............................................................................................................

        Field(@Nullable final String id) { this.id = id; }

        //~ Methods ..................................................................................................................

        @NotNull public String id() { return id; }

    }
}
