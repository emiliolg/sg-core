package tekgenesis.showcase;

import tekgenesis.form.Action;
import tekgenesis.form.Actions;
import java.math.BigDecimal;
import tekgenesis.common.core.Decimals;
import tekgenesis.form.exception.EntityInstanceNotFoundException;
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
 * Generated base class for form: NumbersForm.
 * Don't modify this as this is an auto generated class that's gets generated
 * every time the meta model file is modified. Use subclass instead.
 */
@SuppressWarnings({"DuplicateStringLiteralInspection", "WeakerAccess", "FieldMayBeFinal", "FieldCanBeLocal", "ConstantConditions", "ClassWithTooManyMethods", "ClassWithTooManyFields", "MagicNumber", "MethodOverridesStaticMethodOfSuperclass", "ParameterHidesMemberVariable", "FieldNameHidesFieldInSuperclass", "OverlyComplexClass", "RedundantCast", "UnusedReturnValue", "LocalVariableHidesMemberVariable", "UnusedParameters", "EmptyMethod"})
public abstract class NumbersFormBase
    extends FormInstance<Numbers>
    implements LoggableInstance
{

    //~ Fields ...................................................................................................................

    @SuppressWarnings({"InstanceVariableMayNotBeInitialized", "UnusedDeclaration", "NullableProblems"}) @NotNull private UiModelAccessor f;

    //~ Methods ..................................................................................................................

    /** Invoked when creating a form instance */
    @NotNull public Action create() {
        final Numbers numbers = Numbers.create(getName());
        copyTo(numbers);
        numbers.insert();
        return actions().getDefault();
    }

    /** Invoked when updating a form instance */
    @NotNull public Action update() {
        final Numbers numbers = find();
        copyTo(numbers);
        numbers.update();
        return actions().getDefault();
    }

    /** Invoked when deleting a form instance */
    @NotNull public Action delete() {
        find().delete();
        return actions().getDefault();
    }

    /** Invoked to find an entity instance */
    @NotNull public Numbers find() {
        final Numbers value = Numbers.find(keyAsString());
        if (value == null) {
        	throw new EntityInstanceNotFoundException(f.title(), keyAsString());
        }
        return value;
    }

    public void setPrimaryKey(@NotNull String key) { f.set(Field.NAME, key); }

    @NotNull public String keyAsString() { return "" + getName(); }

    /** Invoked when populating a form instance */
    @NotNull public Numbers populate() {
        final Numbers numbers = find();

        setUnsignedInt4(numbers.getUnsignedInt4())
        	.setSignedInt5(numbers.getSignedInt5())
        	.setSignedToBeUnsigned(numbers.getSignedToBeUnsigned())
        	.setUnsignedInteger(numbers.getUnsignedInteger())
        	.setSignedInteger(numbers.getSignedInteger())
        	.setUnsignedDecimal52(numbers.getUnsignedDecimal52())
        	.setSignedDecimal52(numbers.getSignedDecimal52())
        	.setUnsignedReal(numbers.getUnsignedReal())
        	.setSignedReal(numbers.getSignedReal());

        return numbers;
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

    /** Copies field values to given numbers instance. */
    public void copyTo(@NotNull Numbers numbers) {
        numbers.setUnsignedInt4(getUnsignedInt4())
        	.setSignedInt5(getSignedInt5())
        	.setSignedToBeUnsigned(getSignedToBeUnsigned())
        	.setUnsignedInteger(getUnsignedInteger())
        	.setSignedInteger(getSignedInteger())
        	.setUnsignedDecimal52(getUnsignedDecimal52())
        	.setSignedDecimal52(getSignedDecimal52())
        	.setUnsignedReal(getUnsignedReal())
        	.setSignedReal(getSignedReal());
    }

    /** Returns the value of the text_field(name). */
    @NotNull public String getName() { return f.get(Field.NAME, String.class); }

    /** Sets the value of the text_field(name). */
    @NotNull public NumbersForm setName(@NotNull String name) {
        f.set(Field.NAME, name);
        return (NumbersForm) this;
    }

    /** Returns the value of the text_field(unsignedInt4). */
    public int getUnsignedInt4() { return f.get(Field.UNSIGNED_INT4, Integer.class); }

    /** Sets the value of the text_field(unsignedInt4). */
    @NotNull public NumbersForm setUnsignedInt4(int unsignedInt4) {
        f.set(Field.UNSIGNED_INT4, unsignedInt4);
        return (NumbersForm) this;
    }

    /** Returns the value of the text_field(signedInt5). */
    public int getSignedInt5() { return f.get(Field.SIGNED_INT5, Integer.class); }

    /** Sets the value of the text_field(signedInt5). */
    @NotNull public NumbersForm setSignedInt5(int signedInt5) {
        f.set(Field.SIGNED_INT5, signedInt5);
        return (NumbersForm) this;
    }

    /** Returns the value of the text_field(unsignedInteger). */
    public int getUnsignedInteger() { return f.get(Field.UNSIGNED_INTEGER, Integer.class); }

    /** Sets the value of the text_field(unsignedInteger). */
    @NotNull public NumbersForm setUnsignedInteger(int unsignedInteger) {
        f.set(Field.UNSIGNED_INTEGER, unsignedInteger);
        return (NumbersForm) this;
    }

    /** Returns the value of the text_field(signedInteger). */
    public int getSignedInteger() { return f.get(Field.SIGNED_INTEGER, Integer.class); }

    /** Sets the value of the text_field(signedInteger). */
    @NotNull public NumbersForm setSignedInteger(int signedInteger) {
        f.set(Field.SIGNED_INTEGER, signedInteger);
        return (NumbersForm) this;
    }

    /** Returns the value of the text_field(unsignedDecimal52). */
    @NotNull public BigDecimal getUnsignedDecimal52() { return f.get(Field.UNSIGNED_DECIMAL52, BigDecimal.class); }

    /** Sets the value of the text_field(unsignedDecimal52). */
    @NotNull public NumbersForm setUnsignedDecimal52(@NotNull BigDecimal unsignedDecimal52) {
        f.set(Field.UNSIGNED_DECIMAL52, Decimals.scaleAndCheck("unsignedDecimal52", unsignedDecimal52, false, 5, 2));
        return (NumbersForm) this;
    }

    /** Returns the value of the text_field(signedDecimal52). */
    @NotNull public BigDecimal getSignedDecimal52() { return f.get(Field.SIGNED_DECIMAL52, BigDecimal.class); }

    /** Sets the value of the text_field(signedDecimal52). */
    @NotNull public NumbersForm setSignedDecimal52(@NotNull BigDecimal signedDecimal52) {
        f.set(Field.SIGNED_DECIMAL52, Decimals.scaleAndCheck("signedDecimal52", signedDecimal52, true, 5, 2));
        return (NumbersForm) this;
    }

    /** Returns the value of the text_field(unsignedReal). */
    public double getUnsignedReal() { return f.get(Field.UNSIGNED_REAL, Double.class); }

    /** Sets the value of the text_field(unsignedReal). */
    @NotNull public NumbersForm setUnsignedReal(double unsignedReal) {
        f.set(Field.UNSIGNED_REAL, unsignedReal);
        return (NumbersForm) this;
    }

    /** Returns the value of the text_field(signedReal). */
    public double getSignedReal() { return f.get(Field.SIGNED_REAL, Double.class); }

    /** Sets the value of the text_field(signedReal). */
    @NotNull public NumbersForm setSignedReal(double signedReal) {
        f.set(Field.SIGNED_REAL, signedReal);
        return (NumbersForm) this;
    }

    /** Returns the value of the text_field(signedToBeUnsigned). */
    public int getSignedToBeUnsigned() { return f.get(Field.SIGNED_TO_BE_UNSIGNED, Integer.class); }

    /** Sets the value of the text_field(signedToBeUnsigned). */
    @NotNull public NumbersForm setSignedToBeUnsigned(int signedToBeUnsigned) {
        f.set(Field.SIGNED_TO_BE_UNSIGNED, signedToBeUnsigned);
        return (NumbersForm) this;
    }

    /** Register a form Listener */
    public static void addListener(@NotNull FormListenerType listenerType, @NotNull FormListener<NumbersForm> listener) { listeners.addListener(listenerType, listener); }

    /** Remove a form Listener */
    public static void removeListener(@NotNull FormListenerType listenerType, @NotNull FormListener<NumbersForm> listener) { listeners.removeListener(listenerType, listener); }

    @Override @NotNull public Logger logger() { return logger; }

    /** Returns utility methods to deal with {@link Action actions}. */
    @NotNull protected final Actions actions() { return actions; }

    //~ Fields ...................................................................................................................

    @NotNull private static FormListenerMap<NumbersForm> listeners = new FormListenerMap<>();
    @NotNull private static final Logger logger = Logger.getLogger(NumbersForm.class);

    //~ Inner Classes ............................................................................................................

    @SuppressWarnings("UnnecessarySemicolon")
    public enum Field
        implements tekgenesis.metadata.form.widget.FormFieldRef
    {
        $H0("$H0"),
        $M1("$M1"),
        $S2("$S2"),
        NAME("name"),
        UNSIGNED_INT4("unsignedInt4"),
        SIGNED_INT5("signedInt5"),
        UNSIGNED_INTEGER("unsignedInteger"),
        SIGNED_INTEGER("signedInteger"),
        UNSIGNED_DECIMAL52("unsignedDecimal52"),
        SIGNED_DECIMAL52("signedDecimal52"),
        UNSIGNED_REAL("unsignedReal"),
        SIGNED_REAL("signedReal"),
        SIGNED_TO_BE_UNSIGNED("signedToBeUnsigned"),
        $F3("$F3"),
        SAVE("save"),
        $B4("$B4"),
        $B5("$B5");

        //~ Fields ...................................................................................................................

        @Nullable private final String id;

        //~ Constructors .............................................................................................................

        Field(@Nullable final String id) { this.id = id; }

        //~ Methods ..................................................................................................................

        @NotNull public String id() { return id; }

    }
}
