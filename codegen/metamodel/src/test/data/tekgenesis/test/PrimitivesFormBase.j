package tekgenesis.test;

import tekgenesis.form.Action;
import tekgenesis.form.Actions;
import java.math.BigDecimal;
import tekgenesis.common.core.DateOnly;
import tekgenesis.common.core.Decimals;
import tekgenesis.form.exception.FormCannotBePopulatedException;
import tekgenesis.form.FormInstance;
import tekgenesis.form.extension.FormListener;
import tekgenesis.form.extension.FormListenerMap;
import tekgenesis.form.extension.FormListenerType;
import tekgenesis.model.KeyMap;
import tekgenesis.common.logging.LoggableInstance;
import tekgenesis.common.logging.Logger;
import tekgenesis.form.Message;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import tekgenesis.form.UiModelAccessor;
import tekgenesis.form.configuration.WidgetConfiguration;

/** 
 * Generated base class for form: PrimitivesForm.
 * Don't modify this as this is an auto generated class that's gets generated
 * every time the meta model file is modified. Use subclass instead.
 */
@SuppressWarnings({"DuplicateStringLiteralInspection", "WeakerAccess", "FieldMayBeFinal", "FieldCanBeLocal", "ConstantConditions", "ClassWithTooManyMethods", "ClassWithTooManyFields", "MagicNumber", "MethodOverridesStaticMethodOfSuperclass", "ParameterHidesMemberVariable", "FieldNameHidesFieldInSuperclass", "OverlyComplexClass", "RedundantCast", "UnusedReturnValue", "LocalVariableHidesMemberVariable", "UnusedParameters", "EmptyMethod"})
public abstract class PrimitivesFormBase
    extends FormInstance<Void>
    implements LoggableInstance
{

    //~ Fields ...................................................................................................................

    @SuppressWarnings({"InstanceVariableMayNotBeInitialized", "UnusedDeclaration", "NullableProblems"}) @NotNull private UiModelAccessor f;

    //~ Methods ..................................................................................................................

    /** Invoked when populating a form instance */
    @NotNull public final Void populate() {
        throw new FormCannotBePopulatedException("tekgenesis.test.PrimitivesForm");
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

    /** Returns the value of the text_field(myInt). */
    public int getMyInt() { return f.get(Field.MY_INT, Integer.class); }

    /** Sets the value of the text_field(myInt). */
    @NotNull public PrimitivesForm setMyInt(int myInt) {
        f.set(Field.MY_INT, myInt);
        return (PrimitivesForm) this;
    }

    /** Returns the value of the text_field(myReal). */
    public double getMyReal() { return f.get(Field.MY_REAL, Double.class); }

    /** Sets the value of the text_field(myReal). */
    @NotNull public PrimitivesForm setMyReal(double myReal) {
        f.set(Field.MY_REAL, myReal);
        return (PrimitivesForm) this;
    }

    /** Returns the value of the text_field(myDecimal). */
    @NotNull public BigDecimal getMyDecimal() { return f.get(Field.MY_DECIMAL, BigDecimal.class); }

    /** Sets the value of the text_field(myDecimal). */
    @NotNull public PrimitivesForm setMyDecimal(@NotNull BigDecimal myDecimal) {
        f.set(Field.MY_DECIMAL, Decimals.scaleAndCheck("myDecimal", myDecimal, false, 10, 2));
        return (PrimitivesForm) this;
    }

    /** Returns the value of the text_field(myString). */
    @NotNull public String getMyString() { return f.get(Field.MY_STRING, String.class); }

    /** Sets the value of the text_field(myString). */
    @NotNull public PrimitivesForm setMyString(@NotNull String myString) {
        f.set(Field.MY_STRING, myString);
        return (PrimitivesForm) this;
    }

    /** Returns the value of the date_box(myDate). */
    @NotNull public DateOnly getMyDate() {
        return DateOnly.fromMilliseconds(f.get(Field.MY_DATE, Long.class));
    }

    /** Sets the value of the date_box(myDate). */
    @NotNull public PrimitivesForm setMyDate(@NotNull DateOnly myDate) {
        f.set(Field.MY_DATE, myDate);
        return (PrimitivesForm) this;
    }

    /** Returns the value of the combo_box(myCombo). */
    @NotNull public String getMyCombo() { return f.get(Field.MY_COMBO, String.class); }

    /** Sets the value of the combo_box(myCombo). */
    @NotNull public PrimitivesForm setMyCombo(@NotNull String myCombo) {
        f.set(Field.MY_COMBO, myCombo);
        return (PrimitivesForm) this;
    }

    /** Sets the options of the combo_box(myCombo). */
    public void setMyComboOptions(@NotNull Iterable<String> items) { f.opts(Field.MY_COMBO, items); }

    /** Sets the options of the combo_box(myCombo) with the given KeyMap. */
    public void setMyComboOptions(@NotNull KeyMap items) { f.opts(Field.MY_COMBO, items); }

    /** Register a form Listener */
    public static void addListener(@NotNull FormListenerType listenerType, @NotNull FormListener<PrimitivesForm> listener) { listeners.addListener(listenerType, listener); }

    /** Remove a form Listener */
    public static void removeListener(@NotNull FormListenerType listenerType, @NotNull FormListener<PrimitivesForm> listener) { listeners.removeListener(listenerType, listener); }

    @Override @NotNull public Logger logger() { return logger; }

    /** Returns utility methods to deal with {@link Action actions}. */
    @NotNull protected final Actions actions() { return actions; }

    //~ Fields ...................................................................................................................

    @NotNull private static FormListenerMap<PrimitivesForm> listeners = new FormListenerMap<>();
    @NotNull private static final Logger logger = Logger.getLogger(PrimitivesForm.class);

    //~ Inner Classes ............................................................................................................

    @SuppressWarnings("UnnecessarySemicolon")
    public enum Field
        implements tekgenesis.metadata.form.widget.FormFieldRef
    {
        MY_INT("myInt"),
        MY_REAL("myReal"),
        MY_DECIMAL("myDecimal"),
        MY_STRING("myString"),
        MY_DATE("myDate"),
        MY_COMBO("myCombo");

        //~ Fields ...................................................................................................................

        @Nullable private final String id;

        //~ Constructors .............................................................................................................

        Field(@Nullable final String id) { this.id = id; }

        //~ Methods ..................................................................................................................

        @NotNull public String id() { return id; }

    }
}
