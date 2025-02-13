package tekgenesis.showcase;

import tekgenesis.form.Action;
import tekgenesis.form.Actions;
import java.math.BigDecimal;
import tekgenesis.showcase.ChartDataShowcase.ColumnRow;
import tekgenesis.common.core.Decimals;
import tekgenesis.form.exception.FormCannotBePopulatedException;
import tekgenesis.metadata.form.widget.FormFieldRef;
import tekgenesis.form.FormInstance;
import tekgenesis.form.extension.FormListener;
import tekgenesis.form.extension.FormListenerMap;
import tekgenesis.form.extension.FormListenerType;
import tekgenesis.form.FormRowInstance;
import tekgenesis.form.FormTable;
import tekgenesis.model.KeyMap;
import tekgenesis.common.logging.LoggableInstance;
import tekgenesis.common.logging.Logger;
import tekgenesis.form.Message;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import tekgenesis.form.UiModelAccessor;
import tekgenesis.form.configuration.WidgetConfiguration;

/** 
 * Generated base class for form: ChartDataShowcase.
 * Don't modify this as this is an auto generated class that's gets generated
 * every time the meta model file is modified. Use subclass instead.
 */
@SuppressWarnings({"DuplicateStringLiteralInspection", "WeakerAccess", "FieldMayBeFinal", "FieldCanBeLocal", "ConstantConditions", "ClassWithTooManyMethods", "ClassWithTooManyFields", "MagicNumber", "MethodOverridesStaticMethodOfSuperclass", "ParameterHidesMemberVariable", "FieldNameHidesFieldInSuperclass", "OverlyComplexClass", "RedundantCast", "UnusedReturnValue", "LocalVariableHidesMemberVariable", "UnusedParameters", "EmptyMethod"})
public abstract class ChartDataShowcaseBase
    extends FormInstance<Void>
    implements LoggableInstance
{

    //~ Fields ...................................................................................................................

    @SuppressWarnings({"InstanceVariableMayNotBeInitialized", "UnusedDeclaration", "NullableProblems"}) @NotNull private UiModelAccessor f;

    //~ Methods ..................................................................................................................

    /** Invoked when populating a form instance */
    @NotNull public final Void populate() {
        throw new FormCannotBePopulatedException("tekgenesis.showcase.ChartDataShowcase");
    }

    /** Invoked when the form is loaded */
    public abstract void data();

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

    /** Invoked when button(resolution3) is clicked */
    @NotNull public abstract Action size1280();

    /** Invoked when button(resolution2) is clicked */
    @NotNull public abstract Action size854();

    /** Invoked when button(resolution1) is clicked */
    @NotNull public abstract Action size640();

    /** Invoked when button(resolution0) is clicked */
    @NotNull public abstract Action size320();

    /** Invoked when text_field(barWidth) value ui changes */
    @NotNull public abstract Action changeWidth();

    /** Returns the value of the text_field(barWidth). */
    public double getBarWidth() { return f.get(Field.BAR_WIDTH, Double.class); }

    /** Sets the value of the text_field(barWidth). */
    @NotNull public ChartDataShowcase setBarWidth(double barWidth) {
        f.set(Field.BAR_WIDTH, barWidth);
        return (ChartDataShowcase) this;
    }

    /** Invoked when combo_box(seriesMode) value ui changes */
    @NotNull public abstract Action stack();

    /** Returns the value of the combo_box(seriesMode). */
    @NotNull public SampleSeriesMode getSeriesMode() {
        return SampleSeriesMode.valueOf(f.get(Field.SERIES_MODE, String.class));
    }

    /** Sets the value of the combo_box(seriesMode). */
    @NotNull public ChartDataShowcase setSeriesMode(@NotNull SampleSeriesMode seriesMode) {
        f.set(Field.SERIES_MODE, seriesMode);
        return (ChartDataShowcase) this;
    }

    /** Sets the options of the combo_box(seriesMode). */
    public void setSeriesModeOptions(@NotNull Iterable<SampleSeriesMode> items) { f.opts(Field.SERIES_MODE, items); }

    /** Sets the options of the combo_box(seriesMode) with the given KeyMap. */
    public void setSeriesModeOptions(@NotNull KeyMap items) { f.opts(Field.SERIES_MODE, items); }

    /** Invoked when toggle_button(hoverable) value ui changes */
    @NotNull public abstract Action hoverable();

    /** Returns the value of the toggle_button(hoverable). */
    public boolean isHoverable() { return f.get(Field.HOVERABLE, Boolean.class); }

    /** Sets the value of the toggle_button(hoverable). */
    @NotNull public ChartDataShowcase setHoverable(boolean hoverable) {
        f.set(Field.HOVERABLE, hoverable);
        return (ChartDataShowcase) this;
    }

    /** Invoked when toggle_button(legend) value ui changes */
    @NotNull public abstract Action legend();

    /** Returns the value of the toggle_button(legend). */
    public boolean isLegend() { return f.get(Field.LEGEND, Boolean.class); }

    /** Sets the value of the toggle_button(legend). */
    @NotNull public ChartDataShowcase setLegend(boolean legend) {
        f.set(Field.LEGEND, legend);
        return (ChartDataShowcase) this;
    }

    /** Invoked when toggle_button(showLabels) value ui changes */
    @NotNull public abstract Action showLabels();

    /** Returns the value of the toggle_button(showLabels). */
    public boolean isShowLabels() { return f.get(Field.SHOW_LABELS, Boolean.class); }

    /** Sets the value of the toggle_button(showLabels). */
    @NotNull public ChartDataShowcase setShowLabels(boolean showLabels) {
        f.set(Field.SHOW_LABELS, showLabels);
        return (ChartDataShowcase) this;
    }

    /** Invoked when toggle_button(verticalLabels) value ui changes */
    @NotNull public abstract Action toVerticalLabels();

    /** Returns the value of the toggle_button(verticalLabels). */
    public boolean isVerticalLabels() { return f.get(Field.VERTICAL_LABELS, Boolean.class); }

    /** Sets the value of the toggle_button(verticalLabels). */
    @NotNull public ChartDataShowcase setVerticalLabels(boolean verticalLabels) {
        f.set(Field.VERTICAL_LABELS, verticalLabels);
        return (ChartDataShowcase) this;
    }

    /** Invoked when toggle_button(stepped) value ui changes */
    @NotNull public abstract Action toStepped();

    /** Returns the value of the toggle_button(stepped). */
    public boolean isStepped() { return f.get(Field.STEPPED, Boolean.class); }

    /** Sets the value of the toggle_button(stepped). */
    @NotNull public ChartDataShowcase setStepped(boolean stepped) {
        f.set(Field.STEPPED, stepped);
        return (ChartDataShowcase) this;
    }

    /** Invoked when button(defaultColors) is clicked */
    @NotNull public abstract Action defaultColors();

    /** Invoked when button(red) is clicked */
    @NotNull public abstract Action red();

    /** Invoked when button(blue) is clicked */
    @NotNull public abstract Action blue();

    /** Invoked when button(green) is clicked */
    @NotNull public abstract Action green();

    /** Invoked when button(palette1) is clicked */
    @NotNull public abstract Action palette1();

    /** Invoked when button(palette2) is clicked */
    @NotNull public abstract Action palette2();

    /** Invoked when button(palette3) is clicked */
    @NotNull public abstract Action palette3();

    /** Returns the value of the check_box(showInt). */
    public boolean isShowInt() { return f.get(Field.SHOW_INT, Boolean.class); }

    /** Sets the value of the check_box(showInt). */
    @NotNull public ChartDataShowcase setShowInt(boolean showInt) {
        f.set(Field.SHOW_INT, showInt);
        return (ChartDataShowcase) this;
    }

    /** Returns the value of the check_box(showReal). */
    public boolean isShowReal() { return f.get(Field.SHOW_REAL, Boolean.class); }

    /** Sets the value of the check_box(showReal). */
    @NotNull public ChartDataShowcase setShowReal(boolean showReal) {
        f.set(Field.SHOW_REAL, showReal);
        return (ChartDataShowcase) this;
    }

    /** Returns the value of the check_box(showBigDecimal). */
    public boolean isShowBigDecimal() { return f.get(Field.SHOW_BIG_DECIMAL, Boolean.class); }

    /** Sets the value of the check_box(showBigDecimal). */
    @NotNull public ChartDataShowcase setShowBigDecimal(boolean showBigDecimal) {
        f.set(Field.SHOW_BIG_DECIMAL, showBigDecimal);
        return (ChartDataShowcase) this;
    }

    /** Invoked when chart(column) is clicked */
    @NotNull public abstract Action doClick(@NotNull Field field);

    /** Returns a {@link FormTable<ColumnRow>} instance to handle Column manipulation */
    @NotNull public final FormTable<ColumnRow> getColumn() { return table(Field.COLUMN, ColumnRow.class); }

    /** Method to allow table row class override */
    @NotNull final <T> FormTable<T> table(@NotNull FormFieldRef field, @NotNull Class<T> rowClass) { return f.table(field, rowClass, this); }

    /** Returns the value of the message(current). */
    @NotNull public String getCurrent() { return f.get(Field.CURRENT, String.class); }

    /** Sets the value of the message(current). */
    @NotNull public ChartDataShowcase setCurrent(@NotNull String current) {
        f.set(Field.CURRENT, current);
        return (ChartDataShowcase) this;
    }

    /** Invoked when button(data) is clicked */
    @NotNull public abstract Action reloadData();

    /** Register a form Listener */
    public static void addListener(@NotNull FormListenerType listenerType, @NotNull FormListener<ChartDataShowcase> listener) { listeners.addListener(listenerType, listener); }

    /** Remove a form Listener */
    public static void removeListener(@NotNull FormListenerType listenerType, @NotNull FormListener<ChartDataShowcase> listener) { listeners.removeListener(listenerType, listener); }

    @Override @NotNull public Logger logger() { return logger; }

    /** Returns utility methods to deal with {@link Action actions}. */
    @NotNull protected final Actions actions() { return actions; }

    //~ Fields ...................................................................................................................

    @NotNull private static FormListenerMap<ChartDataShowcase> listeners = new FormListenerMap<>();
    @NotNull private static final Logger logger = Logger.getLogger(ChartDataShowcase.class);

    //~ Inner Classes ............................................................................................................

    public abstract class ColumnRowBase
        implements FormRowInstance<ColumnRow>
    {

        //~ Fields ...................................................................................................................

        @SuppressWarnings({"InstanceVariableMayNotBeInitialized", "UnusedDeclaration", "NullableProblems"}) @NotNull private UiModelAccessor f;

        //~ Methods ..................................................................................................................

        /** Returns the value of the internal(key). */
        @NotNull public String getKey() { return f.get(Field.KEY, String.class); }

        /** Sets the value of the internal(key). */
        @NotNull public ColumnRow setKey(@NotNull String key) {
            f.set(Field.KEY, key);
            return (ColumnRow) this;
        }

        /** Returns the value of the text_field(label). */
        @NotNull public String getLabel() { return f.get(Field.LABEL, String.class); }

        /** Sets the value of the text_field(label). */
        @NotNull public ColumnRow setLabel(@NotNull String label) {
            f.set(Field.LABEL, label);
            return (ColumnRow) this;
        }

        /** Returns the value of the text_field(integer). */
        public int getInteger() { return f.get(Field.INTEGER, Integer.class); }

        /** Sets the value of the text_field(integer). */
        @NotNull public ColumnRow setInteger(int integer) {
            f.set(Field.INTEGER, integer);
            return (ColumnRow) this;
        }

        /** Returns the value of the text_field(real). */
        public double getReal() { return f.get(Field.REAL, Double.class); }

        /** Sets the value of the text_field(real). */
        @NotNull public ColumnRow setReal(double real) {
            f.set(Field.REAL, real);
            return (ColumnRow) this;
        }

        /** Returns the value of the text_field(bigDecimal). */
        @NotNull public BigDecimal getBigDecimal() { return f.get(Field.BIG_DECIMAL, BigDecimal.class); }

        /** Sets the value of the text_field(bigDecimal). */
        @NotNull public ColumnRow setBigDecimal(@NotNull BigDecimal bigDecimal) {
            f.set(Field.BIG_DECIMAL, Decimals.scaleAndCheck("bigDecimal", bigDecimal, false, 5, 2));
            return (ColumnRow) this;
        }

        /** Associates a new message to the specified field and returns it for any further configuration. */
        @NotNull public Message message(@NotNull Field field, @NotNull String msg) { return f.msg(field, msg); }

        /** Returns true if the field value is not null. */
        public boolean isDefined(@NotNull Field... fields) { return f.defined(fields); }

        /** Resets the given fields. */
        protected void reset(@NotNull Field... fields) { f.reset(fields); }

        /** Focuses given field. */
        protected void focus(@NotNull Field field) { f.focus(field); }

        /** Return associated table. */
        @NotNull public final FormTable<ColumnRow> table() { return getColumn(); }

        /** Remove row from table. */
        @NotNull public Action remove() {
            table().remove((ColumnRow) this);
            return actions().getDefault();
        }

        /** Returns a typed configuration for a given field. */
        @NotNull protected <T extends WidgetConfiguration> T configuration(@NotNull Field field) { return f.config(field); }

    }

    @SuppressWarnings("UnnecessarySemicolon")
    public enum Field
        implements tekgenesis.metadata.form.widget.FormFieldRef
    {
        $H0("$H0"),
        $M1("$M1"),
        $L2("$L2"),
        SIZE("size"),
        RESOLUTION3("resolution3"),
        RESOLUTION2("resolution2"),
        RESOLUTION1("resolution1"),
        RESOLUTION0("resolution0"),
        OPTIONS("options"),
        BAR_WIDTH("barWidth"),
        SERIES_MODE("seriesMode"),
        HOVERABLE("hoverable"),
        LEGEND("legend"),
        SHOW_LABELS("showLabels"),
        VERTICAL_LABELS("verticalLabels"),
        STEPPED("stepped"),
        COLORS("colors"),
        DEFAULT_COLORS("defaultColors"),
        RED("red"),
        BLUE("blue"),
        GREEN("green"),
        PALETTE1("palette1"),
        PALETTE2("palette2"),
        PALETTE3("palette3"),
        SERIES("series"),
        SHOW_INT("showInt"),
        SHOW_REAL("showReal"),
        SHOW_BIG_DECIMAL("showBigDecimal"),
        COLUMN("column"),
        KEY("key"),
        LABEL("label"),
        INTEGER("integer"),
        REAL("real"),
        BIG_DECIMAL("bigDecimal"),
        CURRENT("current"),
        DATA("data");

        //~ Fields ...................................................................................................................

        @Nullable private final String id;

        //~ Constructors .............................................................................................................

        Field(@Nullable final String id) { this.id = id; }

        //~ Methods ..................................................................................................................

        @NotNull public String id() { return id; }

    }
}
