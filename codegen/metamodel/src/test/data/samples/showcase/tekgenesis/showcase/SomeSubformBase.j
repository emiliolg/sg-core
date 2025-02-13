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
 * Generated base class for form: SomeSubform.
 * Don't modify this as this is an auto generated class that's gets generated
 * every time the meta model file is modified. Use subclass instead.
 */
@SuppressWarnings({"DuplicateStringLiteralInspection", "WeakerAccess", "FieldMayBeFinal", "FieldCanBeLocal", "ConstantConditions", "ClassWithTooManyMethods", "ClassWithTooManyFields", "MagicNumber", "MethodOverridesStaticMethodOfSuperclass", "ParameterHidesMemberVariable", "FieldNameHidesFieldInSuperclass", "OverlyComplexClass", "RedundantCast", "UnusedReturnValue", "LocalVariableHidesMemberVariable", "UnusedParameters", "EmptyMethod"})
public abstract class SomeSubformBase
    extends FormInstance<Void>
    implements LoggableInstance
{

    //~ Fields ...................................................................................................................

    @SuppressWarnings({"InstanceVariableMayNotBeInitialized", "UnusedDeclaration", "NullableProblems"}) @NotNull private UiModelAccessor f;

    //~ Methods ..................................................................................................................

    /** Invoked when populating a form instance */
    @NotNull public final Void populate() {
        throw new FormCannotBePopulatedException("tekgenesis.showcase.SomeSubform");
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

    /** Returns the value of the check_box(someCheckbox). */
    public boolean isSomeCheckbox() { return f.get(Field.SOME_CHECKBOX, Boolean.class); }

    /** Sets the value of the check_box(someCheckbox). */
    @NotNull public SomeSubform setSomeCheckbox(boolean someCheckbox) {
        f.set(Field.SOME_CHECKBOX, someCheckbox);
        return (SomeSubform) this;
    }

    /** Returns the value of the check_box(someOtherCheckbox). */
    public boolean isSomeOtherCheckbox() { return f.get(Field.SOME_OTHER_CHECKBOX, Boolean.class); }

    /** Sets the value of the check_box(someOtherCheckbox). */
    @NotNull public SomeSubform setSomeOtherCheckbox(boolean someOtherCheckbox) {
        f.set(Field.SOME_OTHER_CHECKBOX, someOtherCheckbox);
        return (SomeSubform) this;
    }

    /** Returns the value of the text_field(someText). */
    @NotNull public String getSomeText() { return f.get(Field.SOME_TEXT, String.class); }

    /** Sets the value of the text_field(someText). */
    @NotNull public SomeSubform setSomeText(@NotNull String someText) {
        f.set(Field.SOME_TEXT, someText);
        return (SomeSubform) this;
    }

    /** Returns the value of the text_field(someOtherText). */
    @NotNull public String getSomeOtherText() { return f.get(Field.SOME_OTHER_TEXT, String.class); }

    /** Sets the value of the text_field(someOtherText). */
    @NotNull public SomeSubform setSomeOtherText(@NotNull String someOtherText) {
        f.set(Field.SOME_OTHER_TEXT, someOtherText);
        return (SomeSubform) this;
    }

    /** Returns the value of the text_field(someNumber). */
    public int getSomeNumber() { return f.get(Field.SOME_NUMBER, Integer.class); }

    /** Sets the value of the text_field(someNumber). */
    @NotNull public SomeSubform setSomeNumber(int someNumber) {
        f.set(Field.SOME_NUMBER, someNumber);
        return (SomeSubform) this;
    }

    /** Returns the value of the text_field(someDate). */
    @NotNull public String getSomeDate() { return f.get(Field.SOME_DATE, String.class); }

    /** Sets the value of the text_field(someDate). */
    @NotNull public SomeSubform setSomeDate(@NotNull String someDate) {
        f.set(Field.SOME_DATE, someDate);
        return (SomeSubform) this;
    }

    /** Register a form Listener */
    public static void addListener(@NotNull FormListenerType listenerType, @NotNull FormListener<SomeSubform> listener) { listeners.addListener(listenerType, listener); }

    /** Remove a form Listener */
    public static void removeListener(@NotNull FormListenerType listenerType, @NotNull FormListener<SomeSubform> listener) { listeners.removeListener(listenerType, listener); }

    @Override @NotNull public Logger logger() { return logger; }

    /** Returns utility methods to deal with {@link Action actions}. */
    @NotNull protected final Actions actions() { return actions; }

    //~ Fields ...................................................................................................................

    @NotNull private static FormListenerMap<SomeSubform> listeners = new FormListenerMap<>();
    @NotNull private static final Logger logger = Logger.getLogger(SomeSubform.class);

    //~ Inner Classes ............................................................................................................

    @SuppressWarnings("UnnecessarySemicolon")
    public enum Field
        implements tekgenesis.metadata.form.widget.FormFieldRef
    {
        SOME_CHECKBOX("someCheckbox"),
        SOME_OTHER_CHECKBOX("someOtherCheckbox"),
        SOME_TEXT("someText"),
        SOME_OTHER_TEXT("someOtherText"),
        SOME_NUMBER("someNumber"),
        SOME_DATE("someDate");

        //~ Fields ...................................................................................................................

        @Nullable private final String id;

        //~ Constructors .............................................................................................................

        Field(@Nullable final String id) { this.id = id; }

        //~ Methods ..................................................................................................................

        @NotNull public String id() { return id; }

    }
}
