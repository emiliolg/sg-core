package tekgenesis.showcase;

import tekgenesis.form.Action;
import tekgenesis.form.Actions;
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
import tekgenesis.form.UiModelAccessor;
import tekgenesis.form.configuration.WidgetConfiguration;

/** 
 * Generated base class for form: ValidationShowcase.
 * Don't modify this as this is an auto generated class that's gets generated
 * every time the meta model file is modified. Use subclass instead.
 */
@SuppressWarnings({"DuplicateStringLiteralInspection", "WeakerAccess", "FieldMayBeFinal", "FieldCanBeLocal", "ConstantConditions", "ClassWithTooManyMethods", "ClassWithTooManyFields", "MagicNumber", "MethodOverridesStaticMethodOfSuperclass", "ParameterHidesMemberVariable", "FieldNameHidesFieldInSuperclass", "OverlyComplexClass", "RedundantCast", "UnusedReturnValue", "LocalVariableHidesMemberVariable", "UnusedParameters", "EmptyMethod"})
public abstract class ValidationShowcaseBase
    extends FormInstance<Void>
    implements LoggableInstance
{

    //~ Fields ...................................................................................................................

    @SuppressWarnings({"InstanceVariableMayNotBeInitialized", "UnusedDeclaration", "NullableProblems"}) @NotNull private UiModelAccessor f;

    //~ Methods ..................................................................................................................

    /** Invoked when populating a form instance */
    @NotNull public final Void populate() {
        throw new FormCannotBePopulatedException("tekgenesis.showcase.ValidationShowcase");
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

    /** Returns the value of the text_field(requiredField). */
    @NotNull public String getRequiredField() { return f.get(Field.REQUIRED_FIELD, String.class); }

    /** Sets the value of the text_field(requiredField). */
    @NotNull public ValidationShowcase setRequiredField(@NotNull String requiredField) {
        f.set(Field.REQUIRED_FIELD, requiredField);
        return (ValidationShowcase) this;
    }

    /** Returns the value of the text_field(optionalField). */
    @Nullable public String getOptionalField() { return f.get(Field.OPTIONAL_FIELD, String.class); }

    /** Sets the value of the text_field(optionalField). */
    @NotNull public ValidationShowcase setOptionalField(@Nullable String optionalField) {
        f.set(Field.OPTIONAL_FIELD, optionalField);
        return (ValidationShowcase) this;
    }

    /** Returns the value of the check_box(optionalCheck). */
    public boolean isOptionalCheck() { return f.get(Field.OPTIONAL_CHECK, Boolean.class); }

    /** Sets the value of the check_box(optionalCheck). */
    @NotNull public ValidationShowcase setOptionalCheck(boolean optionalCheck) {
        f.set(Field.OPTIONAL_CHECK, optionalCheck);
        return (ValidationShowcase) this;
    }

    /** Returns the value of the text_field(optionalCheckField). */
    @Nullable public String getOptionalCheckField() { return f.get(Field.OPTIONAL_CHECK_FIELD, String.class); }

    /** Sets the value of the text_field(optionalCheckField). */
    @NotNull public ValidationShowcase setOptionalCheckField(@Nullable String optionalCheckField) {
        f.set(Field.OPTIONAL_CHECK_FIELD, optionalCheckField);
        return (ValidationShowcase) this;
    }

    /** Returns the value of the date_box(fromField). */
    @NotNull public DateOnly getFromField() {
        return DateOnly.fromMilliseconds(f.get(Field.FROM_FIELD, Long.class));
    }

    /** Sets the value of the date_box(fromField). */
    @NotNull public ValidationShowcase setFromField(@NotNull DateOnly fromField) {
        f.set(Field.FROM_FIELD, fromField);
        return (ValidationShowcase) this;
    }

    /** Returns the value of the date_box(toField). */
    @NotNull public DateOnly getToField() {
        return DateOnly.fromMilliseconds(f.get(Field.TO_FIELD, Long.class));
    }

    /** Sets the value of the date_box(toField). */
    @NotNull public ValidationShowcase setToField(@NotNull DateOnly toField) {
        f.set(Field.TO_FIELD, toField);
        return (ValidationShowcase) this;
    }

    /** Returns the value of the text_field(input1). */
    public int getInput1() { return f.get(Field.INPUT1, Integer.class); }

    /** Sets the value of the text_field(input1). */
    @NotNull public ValidationShowcase setInput1(int input1) {
        f.set(Field.INPUT1, input1);
        return (ValidationShowcase) this;
    }

    /** Returns the value of the text_field(input2). */
    public int getInput2() { return f.get(Field.INPUT2, Integer.class); }

    /** Sets the value of the text_field(input2). */
    @NotNull public ValidationShowcase setInput2(int input2) {
        f.set(Field.INPUT2, input2);
        return (ValidationShowcase) this;
    }

    /** Returns the value of the text_field(input3). */
    public int getInput3() { return f.get(Field.INPUT3, Integer.class); }

    /** Sets the value of the text_field(input3). */
    @NotNull public ValidationShowcase setInput3(int input3) {
        f.set(Field.INPUT3, input3);
        return (ValidationShowcase) this;
    }

    /** Returns the value of the text_field(input4). */
    public int getInput4() { return f.get(Field.INPUT4, Integer.class); }

    /** Sets the value of the text_field(input4). */
    @NotNull public ValidationShowcase setInput4(int input4) {
        f.set(Field.INPUT4, input4);
        return (ValidationShowcase) this;
    }

    /** Returns the value of the text_field(input5). */
    public int getInput5() { return f.get(Field.INPUT5, Integer.class); }

    /** Sets the value of the text_field(input5). */
    @NotNull public ValidationShowcase setInput5(int input5) {
        f.set(Field.INPUT5, input5);
        return (ValidationShowcase) this;
    }

    /** Returns the value of the text_field(input6). */
    public int getInput6() { return f.get(Field.INPUT6, Integer.class); }

    /** Sets the value of the text_field(input6). */
    @NotNull public ValidationShowcase setInput6(int input6) {
        f.set(Field.INPUT6, input6);
        return (ValidationShowcase) this;
    }

    /** Register a form Listener */
    public static void addListener(@NotNull FormListenerType listenerType, @NotNull FormListener<ValidationShowcase> listener) { listeners.addListener(listenerType, listener); }

    /** Remove a form Listener */
    public static void removeListener(@NotNull FormListenerType listenerType, @NotNull FormListener<ValidationShowcase> listener) { listeners.removeListener(listenerType, listener); }

    @Override @NotNull public Logger logger() { return logger; }

    /** Returns utility methods to deal with {@link Action actions}. */
    @NotNull protected final Actions actions() { return actions; }

    //~ Fields ...................................................................................................................

    @NotNull private static FormListenerMap<ValidationShowcase> listeners = new FormListenerMap<>();
    @NotNull private static final Logger logger = Logger.getLogger(ValidationShowcase.class);

    //~ Inner Classes ............................................................................................................

    @SuppressWarnings("UnnecessarySemicolon")
    public enum Field
        implements tekgenesis.metadata.form.widget.FormFieldRef
    {
        $H0("$H0"),
        $M1("$M1"),
        $L2("$L2"),
        REQUIRED_FIELD("requiredField"),
        OPTIONAL_FIELD("optionalField"),
        OPTIONAL_CHECK("optionalCheck"),
        OPTIONAL_CHECK_FIELD("optionalCheckField"),
        FROM_FIELD("fromField"),
        TO_FIELD("toField"),
        $V3("$V3"),
        INPUT1("input1"),
        INPUT2("input2"),
        INPUT3("input3"),
        INPUT4("input4"),
        INPUT5("input5"),
        INPUT6("input6"),
        $F4("$F4"),
        $B5("$B5");

        //~ Fields ...................................................................................................................

        @Nullable private final String id;

        //~ Constructors .............................................................................................................

        Field(@Nullable final String id) { this.id = id; }

        //~ Methods ..................................................................................................................

        @NotNull public String id() { return id; }

    }
}
