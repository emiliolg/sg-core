package tekgenesis.showcase;

import tekgenesis.form.Action;
import tekgenesis.form.Actions;
import tekgenesis.showcase.ChartShowcase.BarRow;
import tekgenesis.showcase.ChartShowcase.ColumnRow;
import tekgenesis.form.exception.FormCannotBePopulatedException;
import tekgenesis.metadata.form.widget.FormFieldRef;
import tekgenesis.form.FormInstance;
import tekgenesis.form.extension.FormListener;
import tekgenesis.form.extension.FormListenerMap;
import tekgenesis.form.extension.FormListenerType;
import tekgenesis.form.FormRowInstance;
import tekgenesis.form.FormTable;
import tekgenesis.showcase.ChartShowcase.LineRow;
import tekgenesis.common.logging.LoggableInstance;
import tekgenesis.common.logging.Logger;
import tekgenesis.form.Message;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import tekgenesis.showcase.ChartShowcase.PieRow;
import tekgenesis.form.UiModelAccessor;
import tekgenesis.form.configuration.WidgetConfiguration;

/** 
 * Generated base class for form: ChartShowcase.
 * Don't modify this as this is an auto generated class that's gets generated
 * every time the meta model file is modified. Use subclass instead.
 */
@SuppressWarnings({"DuplicateStringLiteralInspection", "WeakerAccess", "FieldMayBeFinal", "FieldCanBeLocal", "ConstantConditions", "ClassWithTooManyMethods", "ClassWithTooManyFields", "MagicNumber", "MethodOverridesStaticMethodOfSuperclass", "ParameterHidesMemberVariable", "FieldNameHidesFieldInSuperclass", "OverlyComplexClass", "RedundantCast", "UnusedReturnValue", "LocalVariableHidesMemberVariable", "UnusedParameters", "EmptyMethod"})
public abstract class ChartShowcaseBase
    extends FormInstance<Void>
    implements LoggableInstance
{

    //~ Fields ...................................................................................................................

    @SuppressWarnings({"InstanceVariableMayNotBeInitialized", "UnusedDeclaration", "NullableProblems"}) @NotNull private UiModelAccessor f;

    //~ Methods ..................................................................................................................

    /** Invoked when populating a form instance */
    @NotNull public final Void populate() {
        throw new FormCannotBePopulatedException("tekgenesis.showcase.ChartShowcase");
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

    /** Invoked when button($B3) is clicked */
    @NotNull public abstract Action more();

    /** Returns the value of the text_field(ref). */
    public double getRef() { return f.get(Field.REF, Double.class); }

    /** Sets the value of the text_field(ref). */
    @NotNull public ChartShowcase setRef(double ref) {
        f.set(Field.REF, ref);
        return (ChartShowcase) this;
    }

    /** Returns the value of the text_field(div). */
    public double getDiv() { return f.get(Field.DIV, Double.class); }

    /** Sets the value of the text_field(div). */
    @NotNull public ChartShowcase setDiv(double div) {
        f.set(Field.DIV, div);
        return (ChartShowcase) this;
    }

    /** Invoked when toggle_button(stack) value changes */
    @NotNull public abstract Action stack();

    /** Returns the value of the toggle_button(stack). */
    public boolean isStack() { return f.get(Field.STACK, Boolean.class); }

    /** Sets the value of the toggle_button(stack). */
    @NotNull public ChartShowcase setStack(boolean stack) {
        f.set(Field.STACK, stack);
        return (ChartShowcase) this;
    }

    /** Returns a {@link FormTable<LineRow>} instance to handle Line manipulation */
    @NotNull public final FormTable<LineRow> getLine() { return table(Field.LINE, LineRow.class); }

    /** Method to allow table row class override */
    @NotNull final <T> FormTable<T> table(@NotNull FormFieldRef field, @NotNull Class<T> rowClass) { return f.table(field, rowClass, this); }

    /** Returns a {@link FormTable<ColumnRow>} instance to handle Column manipulation */
    @NotNull public final FormTable<ColumnRow> getColumn() { return table(Field.COLUMN, ColumnRow.class); }

    /** Returns the value of the text_field(label). */
    @NotNull public String getLabel() { return f.get(Field.LABEL, String.class); }

    /** Sets the value of the text_field(label). */
    @NotNull public ChartShowcase setLabel(@NotNull String label) {
        f.set(Field.LABEL, label);
        return (ChartShowcase) this;
    }

    /** Returns a {@link FormTable<BarRow>} instance to handle Bar manipulation */
    @NotNull public final FormTable<BarRow> getBar() { return table(Field.BAR, BarRow.class); }

    /** Invoked when button($B5) is clicked */
    @NotNull public abstract Action nextRnds();

    /** Returns a {@link FormTable<PieRow>} instance to handle Pie manipulation */
    @NotNull public final FormTable<PieRow> getPie() { return table(Field.PIE, PieRow.class); }

    /** Invoked when button(resolution3) is clicked */
    @NotNull public abstract Action size1280();

    /** Invoked when button(resolution2) is clicked */
    @NotNull public abstract Action size854();

    /** Invoked when button(resolution1) is clicked */
    @NotNull public abstract Action size640();

    /** Invoked when button(resolution0) is clicked */
    @NotNull public abstract Action sizeAuto();

    /** Register a form Listener */
    public static void addListener(@NotNull FormListenerType listenerType, @NotNull FormListener<ChartShowcase> listener) { listeners.addListener(listenerType, listener); }

    /** Remove a form Listener */
    public static void removeListener(@NotNull FormListenerType listenerType, @NotNull FormListener<ChartShowcase> listener) { listeners.removeListener(listenerType, listener); }

    @Override @NotNull public Logger logger() { return logger; }

    /** Returns utility methods to deal with {@link Action actions}. */
    @NotNull protected final Actions actions() { return actions; }

    //~ Fields ...................................................................................................................

    @NotNull private static FormListenerMap<ChartShowcase> listeners = new FormListenerMap<>();
    @NotNull private static final Logger logger = Logger.getLogger(ChartShowcase.class);

    //~ Inner Classes ............................................................................................................

    public abstract class LineRowBase
        implements FormRowInstance<LineRow>
    {

        //~ Fields ...................................................................................................................

        @SuppressWarnings({"InstanceVariableMayNotBeInitialized", "UnusedDeclaration", "NullableProblems"}) @NotNull private UiModelAccessor f;

        //~ Methods ..................................................................................................................

        /** Returns the value of the text_field(a). */
        @NotNull public String getA() { return f.get(Field.A, String.class); }

        /** Sets the value of the text_field(a). */
        @NotNull public LineRow setA(@NotNull String a) {
            f.set(Field.A, a);
            return (LineRow) this;
        }

        /** Returns the value of the text_field(beavis). */
        public double getBeavis() { return f.get(Field.BEAVIS, Double.class); }

        /** Sets the value of the text_field(beavis). */
        @NotNull public LineRow setBeavis(double beavis) {
            f.set(Field.BEAVIS, beavis);
            return (LineRow) this;
        }

        /** Returns the value of the text_field(butthead). */
        public double getButthead() { return f.get(Field.BUTTHEAD, Double.class); }

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

    public abstract class ColumnRowBase
        implements FormRowInstance<ColumnRow>
    {

        //~ Fields ...................................................................................................................

        @SuppressWarnings({"InstanceVariableMayNotBeInitialized", "UnusedDeclaration", "NullableProblems"}) @NotNull private UiModelAccessor f;

        //~ Methods ..................................................................................................................

        /** Returns the value of the text_field(b). */
        @NotNull public String getB() { return f.get(Field.B, String.class); }

        /** Sets the value of the text_field(b). */
        @NotNull public ColumnRow setB(@NotNull String b) {
            f.set(Field.B, b);
            return (ColumnRow) this;
        }

        /** Returns the value of the text_field(cain). */
        public double getCain() { return f.get(Field.CAIN, Double.class); }

        /** Returns the value of the text_field(abel). */
        public double getAbel() { return f.get(Field.ABEL, Double.class); }

        /** Sets the value of the text_field(abel). */
        @NotNull public ColumnRow setAbel(double abel) {
            f.set(Field.ABEL, abel);
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

    public abstract class BarRowBase
        implements FormRowInstance<BarRow>
    {

        //~ Fields ...................................................................................................................

        @SuppressWarnings({"InstanceVariableMayNotBeInitialized", "UnusedDeclaration", "NullableProblems"}) @NotNull private UiModelAccessor f;

        //~ Methods ..................................................................................................................

        /** Returns the value of the text_field(c). */
        @NotNull public String getC() { return f.get(Field.C, String.class); }

        /** Returns the value of the text_field(tom). */
        public double getTom() { return f.get(Field.TOM, Double.class); }

        /** Sets the value of the text_field(tom). */
        @NotNull public BarRow setTom(double tom) {
            f.set(Field.TOM, tom);
            return (BarRow) this;
        }

        /** Returns the value of the text_field(jerry). */
        public double getJerry() { return f.get(Field.JERRY, Double.class); }

        /** Sets the value of the text_field(jerry). */
        @NotNull public BarRow setJerry(double jerry) {
            f.set(Field.JERRY, jerry);
            return (BarRow) this;
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
        @NotNull public final FormTable<BarRow> table() { return getBar(); }

        /** Remove row from table. */
        @NotNull public Action remove() {
            table().remove((BarRow) this);
            return actions().getDefault();
        }

        /** Returns a typed configuration for a given field. */
        @NotNull protected <T extends WidgetConfiguration> T configuration(@NotNull Field field) { return f.config(field); }

    }

    public abstract class PieRowBase
        implements FormRowInstance<PieRow>
    {

        //~ Fields ...................................................................................................................

        @SuppressWarnings({"InstanceVariableMayNotBeInitialized", "UnusedDeclaration", "NullableProblems"}) @NotNull private UiModelAccessor f;

        //~ Methods ..................................................................................................................

        /** Returns the value of the text_field(d). */
        @NotNull public String getD() { return f.get(Field.D, String.class); }

        /** Returns the value of the text_field(superman). */
        public double getSuperman() { return f.get(Field.SUPERMAN, Double.class); }

        /** Sets the value of the text_field(superman). */
        @NotNull public PieRow setSuperman(double superman) {
            f.set(Field.SUPERMAN, superman);
            return (PieRow) this;
        }

        /** Returns the value of the internal(key). */
        @NotNull public String getKey() { return f.get(Field.KEY, String.class); }

        /** Sets the value of the internal(key). */
        @NotNull public PieRow setKey(@NotNull String key) {
            f.set(Field.KEY, key);
            return (PieRow) this;
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
        @NotNull public final FormTable<PieRow> table() { return getPie(); }

        /** Remove row from table. */
        @NotNull public Action remove() {
            table().remove((PieRow) this);
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
        $B3("$B3"),
        REF("ref"),
        DIV("div"),
        STACK("stack"),
        $H4("$H4"),
        LINE("line"),
        A("a"),
        BEAVIS("beavis"),
        BUTTHEAD("butthead"),
        COLUMN("column"),
        B("b"),
        CAIN("cain"),
        ABEL("abel"),
        LABEL("label"),
        BAR("bar"),
        C("c"),
        TOM("tom"),
        JERRY("jerry"),
        $B5("$B5"),
        PIE("pie"),
        D("d"),
        SUPERMAN("superman"),
        KEY("key"),
        $H6("$H6"),
        RESOLUTION3("resolution3"),
        RESOLUTION2("resolution2"),
        RESOLUTION1("resolution1"),
        RESOLUTION0("resolution0");

        //~ Fields ...................................................................................................................

        @Nullable private final String id;

        //~ Constructors .............................................................................................................

        Field(@Nullable final String id) { this.id = id; }

        //~ Methods ..................................................................................................................

        @NotNull public String id() { return id; }

    }
}
