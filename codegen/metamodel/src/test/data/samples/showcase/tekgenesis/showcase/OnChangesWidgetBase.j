package tekgenesis.showcase;

import tekgenesis.form.Action;
import tekgenesis.form.Actions;
import tekgenesis.model.KeyMap;
import tekgenesis.common.logging.LoggableInstance;
import tekgenesis.common.logging.Logger;
import tekgenesis.form.Message;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import tekgenesis.form.UiModelAccessor;
import tekgenesis.form.configuration.WidgetConfiguration;
import tekgenesis.form.WidgetInstance;

/** 
 * Generated base class for widget: OnChangesWidget.
 * Don't modify this as this is an auto generated class that's gets generated
 * every time the meta model file is modified. Use subclass instead.
 */
@SuppressWarnings({"DuplicateStringLiteralInspection", "WeakerAccess", "FieldMayBeFinal", "FieldCanBeLocal", "ConstantConditions", "ClassWithTooManyMethods", "ClassWithTooManyFields", "MagicNumber", "MethodOverridesStaticMethodOfSuperclass", "ParameterHidesMemberVariable", "FieldNameHidesFieldInSuperclass", "OverlyComplexClass", "RedundantCast", "UnusedReturnValue", "LocalVariableHidesMemberVariable", "UnusedParameters", "EmptyMethod"})
public abstract class OnChangesWidgetBase
    extends WidgetInstance<Void>
    implements LoggableInstance
{

    //~ Fields ...................................................................................................................

    @SuppressWarnings({"InstanceVariableMayNotBeInitialized", "UnusedDeclaration", "NullableProblems"}) @NotNull private UiModelAccessor f;

    //~ Methods ..................................................................................................................

    /** Returns the value of the text_field(a). */
    public int getA() { return f.get(Field.A, Integer.class); }

    /** Sets the value of the text_field(a). */
    @NotNull public OnChangesWidget setA(int a) {
        f.set(Field.A, a);
        return (OnChangesWidget) this;
    }

    /** Invoked when text_field(b) value changes */
    @NotNull public abstract Action bValueChanged();

    /** Returns the value of the text_field(b). */
    public int getB() { return f.get(Field.B, Integer.class); }

    /** Invoked when text_field(c) value changes */
    @NotNull public abstract Action cValueChanged();

    /** Returns the value of the text_field(c). */
    public int getC() { return f.get(Field.C, Integer.class); }

    /** Returns the value of the combo_box(opts). */
    @NotNull public Options getOpts() { return Options.valueOf(f.get(Field.OPTS, String.class)); }

    /** Sets the value of the combo_box(opts). */
    @NotNull public OnChangesWidget setOpts(@NotNull Options opts) {
        f.set(Field.OPTS, opts);
        return (OnChangesWidget) this;
    }

    /** Sets the options of the combo_box(opts). */
    public void setOptsOptions(@NotNull Iterable<Options> items) { f.opts(Field.OPTS, items); }

    /** Sets the options of the combo_box(opts) with the given KeyMap. */
    public void setOptsOptions(@NotNull KeyMap items) { f.opts(Field.OPTS, items); }

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

    @Override @NotNull public Logger logger() { return logger; }

    /** Returns utility methods to deal with {@link Action actions}. */
    @NotNull protected final Actions actions() { return actions; }

    //~ Fields ...................................................................................................................

    @NotNull private static final Logger logger = Logger.getLogger(OnChangesWidget.class);

    //~ Inner Classes ............................................................................................................

    @SuppressWarnings("UnnecessarySemicolon")
    public enum Field
        implements tekgenesis.metadata.form.widget.FormFieldRef
    {
        A("a"),
        B("b"),
        C("c"),
        OPTS("opts");

        //~ Fields ...................................................................................................................

        @Nullable private final String id;

        //~ Constructors .............................................................................................................

        Field(@Nullable final String id) { this.id = id; }

        //~ Methods ..................................................................................................................

        @NotNull public String id() { return id; }

    }
}
