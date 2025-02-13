package tekgenesis.showcase;

import tekgenesis.form.Action;
import tekgenesis.form.Actions;
import tekgenesis.showcase.DrillDownChart.ColumnRow;
import tekgenesis.form.exception.FormCannotBePopulatedException;
import tekgenesis.metadata.form.widget.FormFieldRef;
import tekgenesis.form.FormInstance;
import tekgenesis.form.extension.FormListener;
import tekgenesis.form.extension.FormListenerMap;
import tekgenesis.form.extension.FormListenerType;
import tekgenesis.form.FormRowInstance;
import tekgenesis.form.FormTable;
import tekgenesis.common.logging.LoggableInstance;
import tekgenesis.common.logging.Logger;
import tekgenesis.form.Message;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import tekgenesis.form.UiModelAccessor;
import tekgenesis.form.configuration.WidgetConfiguration;

/** 
 * Generated base class for form: DrillDownChart.
 * Don't modify this as this is an auto generated class that's gets generated
 * every time the meta model file is modified. Use subclass instead.
 */
@SuppressWarnings({"DuplicateStringLiteralInspection", "WeakerAccess", "FieldMayBeFinal", "FieldCanBeLocal", "ConstantConditions", "ClassWithTooManyMethods", "ClassWithTooManyFields", "MagicNumber", "MethodOverridesStaticMethodOfSuperclass", "ParameterHidesMemberVariable", "FieldNameHidesFieldInSuperclass", "OverlyComplexClass", "RedundantCast", "UnusedReturnValue", "LocalVariableHidesMemberVariable", "UnusedParameters", "EmptyMethod"})
public abstract class DrillDownChartBase
    extends FormInstance<Void>
    implements LoggableInstance
{

    //~ Fields ...................................................................................................................

    @SuppressWarnings({"InstanceVariableMayNotBeInitialized", "UnusedDeclaration", "NullableProblems"}) @NotNull private UiModelAccessor f;

    //~ Methods ..................................................................................................................

    /** Invoked when populating a form instance */
    @NotNull public final Void populate() {
        throw new FormCannotBePopulatedException("tekgenesis.showcase.DrillDownChart");
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

    /** Returns the value of the internal(series1Label). */
    @NotNull public String getSeries1Label() { return f.get(Field.SERIES1_LABEL, String.class); }

    /** Sets the value of the internal(series1Label). */
    @NotNull public DrillDownChart setSeries1Label(@NotNull String series1Label) {
        f.set(Field.SERIES1_LABEL, series1Label);
        return (DrillDownChart) this;
    }

    /** Returns the value of the internal(series2Label). */
    @NotNull public String getSeries2Label() { return f.get(Field.SERIES2_LABEL, String.class); }

    /** Sets the value of the internal(series2Label). */
    @NotNull public DrillDownChart setSeries2Label(@NotNull String series2Label) {
        f.set(Field.SERIES2_LABEL, series2Label);
        return (DrillDownChart) this;
    }

    /** Invoked when button(back) is clicked */
    @NotNull public abstract Action back();

    /** Invoked when chart(column) is clicked */
    @NotNull public abstract Action drilldown(@NotNull Field field);

    /** Returns a {@link FormTable<ColumnRow>} instance to handle Column manipulation */
    @NotNull public final FormTable<ColumnRow> getColumn() { return table(Field.COLUMN, ColumnRow.class); }

    /** Method to allow table row class override */
    @NotNull final <T> FormTable<T> table(@NotNull FormFieldRef field, @NotNull Class<T> rowClass) { return f.table(field, rowClass, this); }

    /** Register a form Listener */
    public static void addListener(@NotNull FormListenerType listenerType, @NotNull FormListener<DrillDownChart> listener) { listeners.addListener(listenerType, listener); }

    /** Remove a form Listener */
    public static void removeListener(@NotNull FormListenerType listenerType, @NotNull FormListener<DrillDownChart> listener) { listeners.removeListener(listenerType, listener); }

    @Override @NotNull public Logger logger() { return logger; }

    /** Returns utility methods to deal with {@link Action actions}. */
    @NotNull protected final Actions actions() { return actions; }

    //~ Fields ...................................................................................................................

    @NotNull private static FormListenerMap<DrillDownChart> listeners = new FormListenerMap<>();
    @NotNull private static final Logger logger = Logger.getLogger(DrillDownChart.class);

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

        /** Returns the value of the text_field(series1). */
        public double getSeries1() { return f.get(Field.SERIES1, Double.class); }

        /** Sets the value of the text_field(series1). */
        @NotNull public ColumnRow setSeries1(double series1) {
            f.set(Field.SERIES1, series1);
            return (ColumnRow) this;
        }

        /** Returns the value of the text_field(series2). */
        public double getSeries2() { return f.get(Field.SERIES2, Double.class); }

        /** Sets the value of the text_field(series2). */
        @NotNull public ColumnRow setSeries2(double series2) {
            f.set(Field.SERIES2, series2);
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
        SERIES1_LABEL("series1Label"),
        SERIES2_LABEL("series2Label"),
        BACK("back"),
        COLUMN("column"),
        KEY("key"),
        LABEL("label"),
        SERIES1("series1"),
        SERIES2("series2");

        //~ Fields ...................................................................................................................

        @Nullable private final String id;

        //~ Constructors .............................................................................................................

        Field(@Nullable final String id) { this.id = id; }

        //~ Methods ..................................................................................................................

        @NotNull public String id() { return id; }

    }
}
