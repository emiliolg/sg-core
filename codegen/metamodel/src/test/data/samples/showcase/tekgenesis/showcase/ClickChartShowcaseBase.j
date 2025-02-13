package tekgenesis.showcase;

import tekgenesis.form.Action;
import tekgenesis.form.Actions;
import java.math.BigDecimal;
import tekgenesis.showcase.ClickChartShowcase.ColumnRow;
import tekgenesis.common.core.Decimals;
import tekgenesis.form.exception.FormCannotBePopulatedException;
import tekgenesis.metadata.form.widget.FormFieldRef;
import tekgenesis.form.FormInstance;
import tekgenesis.form.extension.FormListener;
import tekgenesis.form.extension.FormListenerMap;
import tekgenesis.form.extension.FormListenerType;
import tekgenesis.form.FormRowInstance;
import tekgenesis.form.FormTable;
import tekgenesis.showcase.ClickChartShowcase.LineRow;
import tekgenesis.common.logging.LoggableInstance;
import tekgenesis.common.logging.Logger;
import tekgenesis.form.Message;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import tekgenesis.form.UiModelAccessor;
import tekgenesis.form.configuration.WidgetConfiguration;

/** 
 * Generated base class for form: ClickChartShowcase.
 * Don't modify this as this is an auto generated class that's gets generated
 * every time the meta model file is modified. Use subclass instead.
 */
@SuppressWarnings({"DuplicateStringLiteralInspection", "WeakerAccess", "FieldMayBeFinal", "FieldCanBeLocal", "ConstantConditions", "ClassWithTooManyMethods", "ClassWithTooManyFields", "MagicNumber", "MethodOverridesStaticMethodOfSuperclass", "ParameterHidesMemberVariable", "FieldNameHidesFieldInSuperclass", "OverlyComplexClass", "RedundantCast", "UnusedReturnValue", "LocalVariableHidesMemberVariable", "UnusedParameters", "EmptyMethod"})
public abstract class ClickChartShowcaseBase
    extends FormInstance<Void>
    implements LoggableInstance
{

    //~ Fields ...................................................................................................................

    @SuppressWarnings({"InstanceVariableMayNotBeInitialized", "UnusedDeclaration", "NullableProblems"}) @NotNull private UiModelAccessor f;

    //~ Methods ..................................................................................................................

    /** Invoked when populating a form instance */
    @NotNull public final Void populate() {
        throw new FormCannotBePopulatedException("tekgenesis.showcase.ClickChartShowcase");
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

    /** Returns the value of the text_field(series1Label). */
    @NotNull public String getSeries1Label() { return f.get(Field.SERIES1_LABEL, String.class); }

    /** Sets the value of the text_field(series1Label). */
    @NotNull public ClickChartShowcase setSeries1Label(@NotNull String series1Label) {
        f.set(Field.SERIES1_LABEL, series1Label);
        return (ClickChartShowcase) this;
    }

    /** Returns the value of the text_field(series2Label). */
    @NotNull public String getSeries2Label() { return f.get(Field.SERIES2_LABEL, String.class); }

    /** Sets the value of the text_field(series2Label). */
    @NotNull public ClickChartShowcase setSeries2Label(@NotNull String series2Label) {
        f.set(Field.SERIES2_LABEL, series2Label);
        return (ClickChartShowcase) this;
    }

    /** Returns the value of the check_box(menOnOff). */
    public boolean isMenOnOff() { return f.get(Field.MEN_ON_OFF, Boolean.class); }

    /** Sets the value of the check_box(menOnOff). */
    @NotNull public ClickChartShowcase setMenOnOff(boolean menOnOff) {
        f.set(Field.MEN_ON_OFF, menOnOff);
        return (ClickChartShowcase) this;
    }

    /** Returns the value of the check_box(womenOnOff). */
    public boolean isWomenOnOff() { return f.get(Field.WOMEN_ON_OFF, Boolean.class); }

    /** Sets the value of the check_box(womenOnOff). */
    @NotNull public ClickChartShowcase setWomenOnOff(boolean womenOnOff) {
        f.set(Field.WOMEN_ON_OFF, womenOnOff);
        return (ClickChartShowcase) this;
    }

    /** Returns the value of the text_field(label). */
    @NotNull public String getLabel() { return f.get(Field.LABEL, String.class); }

    /** Sets the value of the text_field(label). */
    @NotNull public ClickChartShowcase setLabel(@NotNull String label) {
        f.set(Field.LABEL, label);
        return (ClickChartShowcase) this;
    }

    /** Invoked when chart(column) is clicked */
    @NotNull public abstract Action doClick(@NotNull Field field);

    /** Returns a {@link FormTable<ColumnRow>} instance to handle Column manipulation */
    @NotNull public final FormTable<ColumnRow> getColumn() { return table(Field.COLUMN, ColumnRow.class); }

    /** Method to allow table row class override */
    @NotNull final <T> FormTable<T> table(@NotNull FormFieldRef field, @NotNull Class<T> rowClass) { return f.table(field, rowClass, this); }

    /** Returns the value of the message(currentMonth). */
    @NotNull public String getCurrentMonth() { return f.get(Field.CURRENT_MONTH, String.class); }

    /** Sets the value of the message(currentMonth). */
    @NotNull public ClickChartShowcase setCurrentMonth(@NotNull String currentMonth) {
        f.set(Field.CURRENT_MONTH, currentMonth);
        return (ClickChartShowcase) this;
    }

    /** Invoked when button(reload) is clicked */
    @NotNull public abstract Action reload();

    /** Invoked when chart(line) is clicked */
    @NotNull public abstract Action lineClick(@NotNull Field field);

    /** Returns a {@link FormTable<LineRow>} instance to handle Line manipulation */
    @NotNull public final FormTable<LineRow> getLine() { return table(Field.LINE, LineRow.class); }

    /** Register a form Listener */
    public static void addListener(@NotNull FormListenerType listenerType, @NotNull FormListener<ClickChartShowcase> listener) { listeners.addListener(listenerType, listener); }

    /** Remove a form Listener */
    public static void removeListener(@NotNull FormListenerType listenerType, @NotNull FormListener<ClickChartShowcase> listener) { listeners.removeListener(listenerType, listener); }

    @Override @NotNull public Logger logger() { return logger; }

    /** Returns utility methods to deal with {@link Action actions}. */
    @NotNull protected final Actions actions() { return actions; }

    //~ Fields ...................................................................................................................

    @NotNull private static FormListenerMap<ClickChartShowcase> listeners = new FormListenerMap<>();
    @NotNull private static final Logger logger = Logger.getLogger(ClickChartShowcase.class);

    //~ Inner Classes ............................................................................................................

    public abstract class ColumnRowBase
        implements FormRowInstance<ColumnRow>
    {

        //~ Fields ...................................................................................................................

        @SuppressWarnings({"InstanceVariableMayNotBeInitialized", "UnusedDeclaration", "NullableProblems"}) @NotNull private UiModelAccessor f;

        //~ Methods ..................................................................................................................

        /** Returns the value of the text_field(month). */
        @NotNull public String getMonth() { return f.get(Field.MONTH, String.class); }

        /** Sets the value of the text_field(month). */
        @NotNull public ColumnRow setMonth(@NotNull String month) {
            f.set(Field.MONTH, month);
            return (ColumnRow) this;
        }

        /** Returns the value of the text_field(men). */
        public double getMen() { return f.get(Field.MEN, Double.class); }

        /** Sets the value of the text_field(men). */
        @NotNull public ColumnRow setMen(double men) {
            f.set(Field.MEN, men);
            return (ColumnRow) this;
        }

        /** Returns the value of the internal(key). */
        @NotNull public String getKey() { return f.get(Field.KEY, String.class); }

        /** Sets the value of the internal(key). */
        @NotNull public ColumnRow setKey(@NotNull String key) {
            f.set(Field.KEY, key);
            return (ColumnRow) this;
        }

        /** Returns the value of the text_field(women). */
        public double getWomen() { return f.get(Field.WOMEN, Double.class); }

        /** Sets the value of the text_field(women). */
        @NotNull public ColumnRow setWomen(double women) {
            f.set(Field.WOMEN, women);
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

    public abstract class LineRowBase
        implements FormRowInstance<LineRow>
    {

        //~ Fields ...................................................................................................................

        @SuppressWarnings({"InstanceVariableMayNotBeInitialized", "UnusedDeclaration", "NullableProblems"}) @NotNull private UiModelAccessor f;

        //~ Methods ..................................................................................................................

        /** Returns the value of the text_field(chartLabel). */
        @NotNull public String getChartLabel() { return f.get(Field.CHART_LABEL, String.class); }

        /** Sets the value of the text_field(chartLabel). */
        @NotNull public LineRow setChartLabel(@NotNull String chartLabel) {
            f.set(Field.CHART_LABEL, chartLabel);
            return (LineRow) this;
        }

        /** Returns the value of the text_field(serie1). */
        @Nullable public BigDecimal getSerie1() { return f.get(Field.SERIE1, BigDecimal.class); }

        /** Sets the value of the text_field(serie1). */
        @NotNull public LineRow setSerie1(@Nullable BigDecimal serie1) {
            f.set(Field.SERIE1, Decimals.scaleAndCheck("serie1", serie1, false, 20, 2));
            return (LineRow) this;
        }

        /** Returns the value of the text_field(serie2). */
        @Nullable public BigDecimal getSerie2() { return f.get(Field.SERIE2, BigDecimal.class); }

        /** Sets the value of the text_field(serie2). */
        @NotNull public LineRow setSerie2(@Nullable BigDecimal serie2) {
            f.set(Field.SERIE2, Decimals.scaleAndCheck("serie2", serie2, false, 20, 2));
            return (LineRow) this;
        }

        /** Returns the value of the text_field(serie3). */
        @Nullable public BigDecimal getSerie3() { return f.get(Field.SERIE3, BigDecimal.class); }

        /** Sets the value of the text_field(serie3). */
        @NotNull public LineRow setSerie3(@Nullable BigDecimal serie3) {
            f.set(Field.SERIE3, Decimals.scaleAndCheck("serie3", serie3, false, 20, 2));
            return (LineRow) this;
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
        @NotNull public final FormTable<LineRow> table() { return getLine(); }

        /** Remove row from table. */
        @NotNull public Action remove() {
            table().remove((LineRow) this);
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
        SERIES1_LABEL("series1Label"),
        SERIES2_LABEL("series2Label"),
        MEN_ON_OFF("menOnOff"),
        WOMEN_ON_OFF("womenOnOff"),
        LABEL("label"),
        COLUMN("column"),
        MONTH("month"),
        MEN("men"),
        KEY("key"),
        WOMEN("women"),
        CURRENT_MONTH("currentMonth"),
        RELOAD("reload"),
        LINE("line"),
        CHART_LABEL("chartLabel"),
        SERIE1("serie1"),
        SERIE2("serie2"),
        SERIE3("serie3");

        //~ Fields ...................................................................................................................

        @Nullable private final String id;

        //~ Constructors .............................................................................................................

        Field(@Nullable final String id) { this.id = id; }

        //~ Methods ..................................................................................................................

        @NotNull public String id() { return id; }

    }
}
