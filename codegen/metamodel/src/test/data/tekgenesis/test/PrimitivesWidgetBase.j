package tekgenesis.test;

import tekgenesis.form.Action;
import tekgenesis.form.Actions;
import java.math.BigDecimal;
import tekgenesis.common.core.DateOnly;
import tekgenesis.common.core.DateTime;
import tekgenesis.common.core.Decimals;
import tekgenesis.common.logging.LoggableInstance;
import tekgenesis.common.logging.Logger;
import tekgenesis.form.Message;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import tekgenesis.form.UiModelAccessor;
import tekgenesis.form.configuration.WidgetConfiguration;
import tekgenesis.form.WidgetInstance;

/** 
 * Generated base class for widget: PrimitivesWidget.
 * Don't modify this as this is an auto generated class that's gets generated
 * every time the meta model file is modified. Use subclass instead.
 */
@SuppressWarnings({"DuplicateStringLiteralInspection", "WeakerAccess", "FieldMayBeFinal", "FieldCanBeLocal", "ConstantConditions", "ClassWithTooManyMethods", "ClassWithTooManyFields", "MagicNumber", "MethodOverridesStaticMethodOfSuperclass", "ParameterHidesMemberVariable", "FieldNameHidesFieldInSuperclass", "OverlyComplexClass", "RedundantCast", "UnusedReturnValue", "LocalVariableHidesMemberVariable", "UnusedParameters", "EmptyMethod"})
public abstract class PrimitivesWidgetBase
    extends WidgetInstance<Void>
    implements LoggableInstance
{

    //~ Fields ...................................................................................................................

    @SuppressWarnings({"InstanceVariableMayNotBeInitialized", "UnusedDeclaration", "NullableProblems"}) @NotNull private UiModelAccessor f;

    //~ Methods ..................................................................................................................

    /** Returns the value of the check_box(bool). */
    public boolean isBool() { return f.get(Field.BOOL, Boolean.class); }

    /** Sets the value of the check_box(bool). */
    @NotNull public PrimitivesWidget setBool(boolean bool) {
        f.set(Field.BOOL, bool);
        return (PrimitivesWidget) this;
    }

    /** Returns the value of the text_field(int). */
    public int getInt() { return f.get(Field.INT, Integer.class); }

    /** Sets the value of the text_field(int). */
    @NotNull public PrimitivesWidget setInt(int int) {
        f.set(Field.INT, int);
        return (PrimitivesWidget) this;
    }

    /** Returns the value of the text_field(intOpt). */
    @Nullable public Integer getIntOpt() { return f.get(Field.INT_OPT, Integer.class); }

    /** Sets the value of the text_field(intOpt). */
    @NotNull public PrimitivesWidget setIntOpt(@Nullable Integer intOpt) {
        f.set(Field.INT_OPT, intOpt);
        return (PrimitivesWidget) this;
    }

    /** Returns the value of the text_field(real). */
    public double getReal() { return f.get(Field.REAL, Double.class); }

    /** Sets the value of the text_field(real). */
    @NotNull public PrimitivesWidget setReal(double real) {
        f.set(Field.REAL, real);
        return (PrimitivesWidget) this;
    }

    /** Returns the value of the text_field(realOpt). */
    @Nullable public Double getRealOpt() { return f.get(Field.REAL_OPT, Double.class); }

    /** Sets the value of the text_field(realOpt). */
    @NotNull public PrimitivesWidget setRealOpt(@Nullable Double realOpt) {
        f.set(Field.REAL_OPT, realOpt);
        return (PrimitivesWidget) this;
    }

    /** Returns the value of the text_field(decimal). */
    @NotNull public BigDecimal getDecimal() { return f.get(Field.DECIMAL, BigDecimal.class); }

    /** Sets the value of the text_field(decimal). */
    @NotNull public PrimitivesWidget setDecimal(@NotNull BigDecimal decimal) {
        f.set(Field.DECIMAL, Decimals.scaleAndCheck("decimal", decimal, false, 10, 2));
        return (PrimitivesWidget) this;
    }

    /** Returns the value of the text_field(decimalOpt). */
    @Nullable public BigDecimal getDecimalOpt() { return f.get(Field.DECIMAL_OPT, BigDecimal.class); }

    /** Sets the value of the text_field(decimalOpt). */
    @NotNull public PrimitivesWidget setDecimalOpt(@Nullable BigDecimal decimalOpt) {
        f.set(Field.DECIMAL_OPT, Decimals.scaleAndCheck("decimalOpt", decimalOpt, false, 10, 2));
        return (PrimitivesWidget) this;
    }

    /** Returns the value of the text_field(string). */
    @NotNull public String getString() { return f.get(Field.STRING, String.class); }

    /** Sets the value of the text_field(string). */
    @NotNull public PrimitivesWidget setString(@NotNull String string) {
        f.set(Field.STRING, string);
        return (PrimitivesWidget) this;
    }

    /** Returns the value of the text_field(stringOpt). */
    @Nullable public String getStringOpt() { return f.get(Field.STRING_OPT, String.class); }

    /** Sets the value of the text_field(stringOpt). */
    @NotNull public PrimitivesWidget setStringOpt(@Nullable String stringOpt) {
        f.set(Field.STRING_OPT, stringOpt);
        return (PrimitivesWidget) this;
    }

    /** Returns the value of the date_box(date). */
    @NotNull public DateOnly getDate() {
        return DateOnly.fromMilliseconds(f.get(Field.DATE, Long.class));
    }

    /** Sets the value of the date_box(date). */
    @NotNull public PrimitivesWidget setDate(@NotNull DateOnly date) {
        f.set(Field.DATE, date);
        return (PrimitivesWidget) this;
    }

    /** Returns the value of the date_box(dateOpt). */
    @Nullable public DateOnly getDateOpt() {
        final Long val = f.get(Field.DATE_OPT, Long.class);
        return val == null ? null : DateOnly.fromMilliseconds(val);
    }

    /** Sets the value of the date_box(dateOpt). */
    @NotNull public PrimitivesWidget setDateOpt(@Nullable DateOnly dateOpt) {
        f.set(Field.DATE_OPT, dateOpt);
        return (PrimitivesWidget) this;
    }

    /** Returns the value of the date_time_box(datetime). */
    @NotNull public DateTime getDatetime() {
        return DateTime.fromMilliseconds(f.get(Field.DATETIME, Long.class));
    }

    /** Sets the value of the date_time_box(datetime). */
    @NotNull public PrimitivesWidget setDatetime(@NotNull DateTime datetime) {
        f.set(Field.DATETIME, datetime);
        return (PrimitivesWidget) this;
    }

    /** Returns the value of the date_time_box(datetimeOpt). */
    @Nullable public DateTime getDatetimeOpt() {
        final Long val = f.get(Field.DATETIME_OPT, Long.class);
        return val == null ? null : DateTime.fromMilliseconds(val);
    }

    /** Sets the value of the date_time_box(datetimeOpt). */
    @NotNull public PrimitivesWidget setDatetimeOpt(@Nullable DateTime datetimeOpt) {
        f.set(Field.DATETIME_OPT, datetimeOpt);
        return (PrimitivesWidget) this;
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

    @Override @NotNull public Logger logger() { return logger; }

    /** Returns utility methods to deal with {@link Action actions}. */
    @NotNull protected final Actions actions() { return actions; }

    //~ Fields ...................................................................................................................

    @NotNull private static final Logger logger = Logger.getLogger(PrimitivesWidget.class);

    //~ Inner Classes ............................................................................................................

    @SuppressWarnings("UnnecessarySemicolon")
    public enum Field
        implements tekgenesis.metadata.form.widget.FormFieldRef
    {
        BOOL("bool"),
        INT("int"),
        INT_OPT("intOpt"),
        REAL("real"),
        REAL_OPT("realOpt"),
        DECIMAL("decimal"),
        DECIMAL_OPT("decimalOpt"),
        STRING("string"),
        STRING_OPT("stringOpt"),
        DATE("date"),
        DATE_OPT("dateOpt"),
        DATETIME("datetime"),
        DATETIME_OPT("datetimeOpt");

        //~ Fields ...................................................................................................................

        @Nullable private final String id;

        //~ Constructors .............................................................................................................

        Field(@Nullable final String id) { this.id = id; }

        //~ Methods ..................................................................................................................

        @NotNull public String id() { return id; }

    }
}
