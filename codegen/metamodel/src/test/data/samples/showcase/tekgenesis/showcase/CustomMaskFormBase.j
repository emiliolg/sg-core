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
 * Generated base class for form: CustomMaskForm.
 * Don't modify this as this is an auto generated class that's gets generated
 * every time the meta model file is modified. Use subclass instead.
 */
@SuppressWarnings({"DuplicateStringLiteralInspection", "WeakerAccess", "FieldMayBeFinal", "FieldCanBeLocal", "ConstantConditions", "ClassWithTooManyMethods", "ClassWithTooManyFields", "MagicNumber", "MethodOverridesStaticMethodOfSuperclass", "ParameterHidesMemberVariable", "FieldNameHidesFieldInSuperclass", "OverlyComplexClass", "RedundantCast", "UnusedReturnValue", "LocalVariableHidesMemberVariable", "UnusedParameters", "EmptyMethod"})
public abstract class CustomMaskFormBase
    extends FormInstance<Void>
    implements LoggableInstance
{

    //~ Fields ...................................................................................................................

    @SuppressWarnings({"InstanceVariableMayNotBeInitialized", "UnusedDeclaration", "NullableProblems"}) @NotNull private UiModelAccessor f;

    //~ Methods ..................................................................................................................

    /** Invoked when populating a form instance */
    @NotNull public final Void populate() {
        throw new FormCannotBePopulatedException("tekgenesis.showcase.CustomMaskForm");
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

    /** Returns the value of the text_field(patNew). */
    @NotNull public String getPatNew() { return f.get(Field.PAT_NEW, String.class); }

    /** Sets the value of the text_field(patNew). */
    @NotNull public CustomMaskForm setPatNew(@NotNull String patNew) {
        f.set(Field.PAT_NEW, patNew);
        return (CustomMaskForm) this;
    }

    /** Returns the value of the text_field(patNew3). */
    @NotNull public String getPatNew3() { return f.get(Field.PAT_NEW3, String.class); }

    /** Sets the value of the text_field(patNew3). */
    @NotNull public CustomMaskForm setPatNew3(@NotNull String patNew3) {
        f.set(Field.PAT_NEW3, patNew3);
        return (CustomMaskForm) this;
    }

    /** Returns the value of the internal(s1). */
    @NotNull public String getS1() { return f.get(Field.S1, String.class); }

    /** Sets the value of the internal(s1). */
    @NotNull public CustomMaskForm setS1(@NotNull String s1) {
        f.set(Field.S1, s1);
        return (CustomMaskForm) this;
    }

    /** Returns the value of the internal(s2). */
    @NotNull public String getS2() { return f.get(Field.S2, String.class); }

    /** Sets the value of the internal(s2). */
    @NotNull public CustomMaskForm setS2(@NotNull String s2) {
        f.set(Field.S2, s2);
        return (CustomMaskForm) this;
    }

    /** Returns the value of the text_field(lonely). */
    @NotNull public String getLonely() { return f.get(Field.LONELY, String.class); }

    /** Sets the value of the text_field(lonely). */
    @NotNull public CustomMaskForm setLonely(@NotNull String lonely) {
        f.set(Field.LONELY, lonely);
        return (CustomMaskForm) this;
    }

    /** Returns the value of the text_field(fields). */
    @NotNull public String getFields() { return f.get(Field.FIELDS, String.class); }

    /** Sets the value of the text_field(fields). */
    @NotNull public CustomMaskForm setFields(@NotNull String fields) {
        f.set(Field.FIELDS, fields);
        return (CustomMaskForm) this;
    }

    /** Returns the value of the text_field(patNew2). */
    @NotNull public String getPatNew2() { return f.get(Field.PAT_NEW2, String.class); }

    /** Sets the value of the text_field(patNew2). */
    @NotNull public CustomMaskForm setPatNew2(@NotNull String patNew2) {
        f.set(Field.PAT_NEW2, patNew2);
        return (CustomMaskForm) this;
    }

    /** Invoked when button(butt) is clicked */
    @NotNull public abstract Action validate();

    /** Register a form Listener */
    public static void addListener(@NotNull FormListenerType listenerType, @NotNull FormListener<CustomMaskForm> listener) { listeners.addListener(listenerType, listener); }

    /** Remove a form Listener */
    public static void removeListener(@NotNull FormListenerType listenerType, @NotNull FormListener<CustomMaskForm> listener) { listeners.removeListener(listenerType, listener); }

    @Override @NotNull public Logger logger() { return logger; }

    /** Returns utility methods to deal with {@link Action actions}. */
    @NotNull protected final Actions actions() { return actions; }

    //~ Fields ...................................................................................................................

    @NotNull private static FormListenerMap<CustomMaskForm> listeners = new FormListenerMap<>();
    @NotNull private static final Logger logger = Logger.getLogger(CustomMaskForm.class);

    //~ Inner Classes ............................................................................................................

    @SuppressWarnings("UnnecessarySemicolon")
    public enum Field
        implements tekgenesis.metadata.form.widget.FormFieldRef
    {
        PAT_NEW("patNew"),
        PAT_NEW3("patNew3"),
        S1("s1"),
        S2("s2"),
        LONELY("lonely"),
        FIELDS("fields"),
        PAT_NEW2("patNew2"),
        BUTT("butt");

        //~ Fields ...................................................................................................................

        @Nullable private final String id;

        //~ Constructors .............................................................................................................

        Field(@Nullable final String id) { this.id = id; }

        //~ Methods ..................................................................................................................

        @NotNull public String id() { return id; }

    }
}
