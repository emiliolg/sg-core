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
 * Generated base class for form: CustomMaskTestForm.
 * Don't modify this as this is an auto generated class that's gets generated
 * every time the meta model file is modified. Use subclass instead.
 */
@SuppressWarnings({"DuplicateStringLiteralInspection", "WeakerAccess", "FieldMayBeFinal", "FieldCanBeLocal", "ConstantConditions", "ClassWithTooManyMethods", "ClassWithTooManyFields", "MagicNumber", "MethodOverridesStaticMethodOfSuperclass", "ParameterHidesMemberVariable", "FieldNameHidesFieldInSuperclass", "OverlyComplexClass", "RedundantCast", "UnusedReturnValue", "LocalVariableHidesMemberVariable", "UnusedParameters", "EmptyMethod"})
public abstract class CustomMaskTestFormBase
    extends FormInstance<Void>
    implements LoggableInstance
{

    //~ Fields ...................................................................................................................

    @SuppressWarnings({"InstanceVariableMayNotBeInitialized", "UnusedDeclaration", "NullableProblems"}) @NotNull private UiModelAccessor f;

    //~ Methods ..................................................................................................................

    /** Invoked when populating a form instance */
    @NotNull public final Void populate() {
        throw new FormCannotBePopulatedException("tekgenesis.showcase.CustomMaskTestForm");
    }

    /** Invoked when the form is loaded */
    public abstract void onLoad();

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

    /** Returns the value of the text_field(uniqueMask). */
    @NotNull public String getUniqueMask() { return f.get(Field.UNIQUE_MASK, String.class); }

    /** Sets the value of the text_field(uniqueMask). */
    @NotNull public CustomMaskTestForm setUniqueMask(@NotNull String uniqueMask) {
        f.set(Field.UNIQUE_MASK, uniqueMask);
        return (CustomMaskTestForm) this;
    }

    /** Returns the value of the text_field(threeMasks). */
    @NotNull public String getThreeMasks() { return f.get(Field.THREE_MASKS, String.class); }

    /** Sets the value of the text_field(threeMasks). */
    @NotNull public CustomMaskTestForm setThreeMasks(@NotNull String threeMasks) {
        f.set(Field.THREE_MASKS, threeMasks);
        return (CustomMaskTestForm) this;
    }

    /** Returns the value of the internal(s1). */
    @NotNull public String getS1() { return f.get(Field.S1, String.class); }

    /** Sets the value of the internal(s1). */
    @NotNull public CustomMaskTestForm setS1(@NotNull String s1) {
        f.set(Field.S1, s1);
        return (CustomMaskTestForm) this;
    }

    /** Returns the value of the internal(s2). */
    @NotNull public String getS2() { return f.get(Field.S2, String.class); }

    /** Sets the value of the internal(s2). */
    @NotNull public CustomMaskTestForm setS2(@NotNull String s2) {
        f.set(Field.S2, s2);
        return (CustomMaskTestForm) this;
    }

    /** Returns the value of the text_field(fromFieldMask). */
    @NotNull public String getFromFieldMask() { return f.get(Field.FROM_FIELD_MASK, String.class); }

    /** Sets the value of the text_field(fromFieldMask). */
    @NotNull public CustomMaskTestForm setFromFieldMask(@NotNull String fromFieldMask) {
        f.set(Field.FROM_FIELD_MASK, fromFieldMask);
        return (CustomMaskTestForm) this;
    }

    /** Returns the value of the text_field(fromFieldMasks). */
    @NotNull public String getFromFieldMasks() { return f.get(Field.FROM_FIELD_MASKS, String.class); }

    /** Sets the value of the text_field(fromFieldMasks). */
    @NotNull public CustomMaskTestForm setFromFieldMasks(@NotNull String fromFieldMasks) {
        f.set(Field.FROM_FIELD_MASKS, fromFieldMasks);
        return (CustomMaskTestForm) this;
    }

    /** Register a form Listener */
    public static void addListener(@NotNull FormListenerType listenerType, @NotNull FormListener<CustomMaskTestForm> listener) { listeners.addListener(listenerType, listener); }

    /** Remove a form Listener */
    public static void removeListener(@NotNull FormListenerType listenerType, @NotNull FormListener<CustomMaskTestForm> listener) { listeners.removeListener(listenerType, listener); }

    @Override @NotNull public Logger logger() { return logger; }

    /** Returns utility methods to deal with {@link Action actions}. */
    @NotNull protected final Actions actions() { return actions; }

    //~ Fields ...................................................................................................................

    @NotNull private static FormListenerMap<CustomMaskTestForm> listeners = new FormListenerMap<>();
    @NotNull private static final Logger logger = Logger.getLogger(CustomMaskTestForm.class);

    //~ Inner Classes ............................................................................................................

    @SuppressWarnings("UnnecessarySemicolon")
    public enum Field
        implements tekgenesis.metadata.form.widget.FormFieldRef
    {
        UNIQUE_MASK("uniqueMask"),
        THREE_MASKS("threeMasks"),
        S1("s1"),
        S2("s2"),
        FROM_FIELD_MASK("fromFieldMask"),
        FROM_FIELD_MASKS("fromFieldMasks");

        //~ Fields ...................................................................................................................

        @Nullable private final String id;

        //~ Constructors .............................................................................................................

        Field(@Nullable final String id) { this.id = id; }

        //~ Methods ..................................................................................................................

        @NotNull public String id() { return id; }

    }
}
