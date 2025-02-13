package tekgenesis.showcase;

import tekgenesis.form.Action;
import tekgenesis.form.Actions;
import java.math.BigDecimal;
import tekgenesis.common.util.Conversions;
import tekgenesis.common.core.Decimals;
import tekgenesis.form.exception.EntityInstanceNotFoundException;
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
import tekgenesis.showcase.TextFieldShowcaseForm.TableRow;
import tekgenesis.form.UiModelAccessor;
import tekgenesis.form.configuration.WidgetConfiguration;

/** 
 * Generated base class for form: TextFieldShowcaseForm.
 * Don't modify this as this is an auto generated class that's gets generated
 * every time the meta model file is modified. Use subclass instead.
 */
@SuppressWarnings({"DuplicateStringLiteralInspection", "WeakerAccess", "FieldMayBeFinal", "FieldCanBeLocal", "ConstantConditions", "ClassWithTooManyMethods", "ClassWithTooManyFields", "MagicNumber", "MethodOverridesStaticMethodOfSuperclass", "ParameterHidesMemberVariable", "FieldNameHidesFieldInSuperclass", "OverlyComplexClass", "RedundantCast", "UnusedReturnValue", "LocalVariableHidesMemberVariable", "UnusedParameters", "EmptyMethod"})
public abstract class TextFieldShowcaseFormBase
    extends FormInstance<TextFieldShowcase>
    implements LoggableInstance
{

    //~ Fields ...................................................................................................................

    @SuppressWarnings({"InstanceVariableMayNotBeInitialized", "UnusedDeclaration", "NullableProblems"}) @NotNull private UiModelAccessor f;

    //~ Methods ..................................................................................................................

    /** Invoked when creating a form instance */
    @NotNull public Action create() {
        final TextFieldShowcase textFieldShowcase = TextFieldShowcase.create(getIdKey());
        copyTo(textFieldShowcase);
        textFieldShowcase.insert();
        return actions().getDefault();
    }

    /** Invoked when updating a form instance */
    @NotNull public Action update() {
        final TextFieldShowcase textFieldShowcase = find();
        copyTo(textFieldShowcase);
        textFieldShowcase.update();
        return actions().getDefault();
    }

    /** Invoked when deleting a form instance */
    @NotNull public Action delete() {
        find().delete();
        return actions().getDefault();
    }

    /** Invoked to find an entity instance */
    @NotNull public TextFieldShowcase find() {
        final TextFieldShowcase value = TextFieldShowcase.find(keyAsString());
        if (value == null) {
        	throw new EntityInstanceNotFoundException(f.title(), keyAsString());
        }
        return value;
    }

    public void setPrimaryKey(@NotNull String key) { f.set(Field.ID_KEY, Conversions.toInt(key)); }

    @NotNull public String keyAsString() { return "" + getIdKey(); }

    /** Invoked when populating a form instance */
    @NotNull public TextFieldShowcase populate() {
        final TextFieldShowcase textFieldShowcase = find();

        setF1(textFieldShowcase.getF1())
        	.setF2(textFieldShowcase.getF2())
        	.setF3(textFieldShowcase.getF3())
        	.setF4(textFieldShowcase.getF4())
        	.setPatente(textFieldShowcase.getPatente())
        	.setT1(textFieldShowcase.getT1())
        	.setT2(textFieldShowcase.getT2())
        	.setT3(textFieldShowcase.getT3())
        	.setT4(textFieldShowcase.getT4())
        	.setA1(textFieldShowcase.getA1())
        	.setA2(textFieldShowcase.getA2())
        	.setA3(textFieldShowcase.getA3())
        	.setA4(textFieldShowcase.getA4())
        	.setHtml(textFieldShowcase.getHtml());

        return textFieldShowcase;
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

    /** Copies field values to given textFieldShowcase instance. */
    public void copyTo(@NotNull TextFieldShowcase textFieldShowcase) {
        textFieldShowcase.setF1(getF1())
        	.setF2(getF2())
        	.setF3(getF3())
        	.setF4(getF4())
        	.setPatente(getPatente())
        	.setT1(getT1())
        	.setT2(getT2())
        	.setT3(getT3())
        	.setT4(getT4())
        	.setA1(getA1())
        	.setA2(getA2())
        	.setA3(getA3())
        	.setA4(getA4())
        	.setHtml(getHtml());
    }

    /** Returns the value of the text_field(idKey). */
    public int getIdKey() { return f.get(Field.ID_KEY, Integer.class); }

    /** Sets the value of the text_field(idKey). */
    @NotNull public TextFieldShowcaseForm setIdKey(int idKey) {
        f.set(Field.ID_KEY, idKey);
        return (TextFieldShowcaseForm) this;
    }

    /** Returns the value of the rich_text_area(html). */
    @Nullable public String getHtml() { return f.get(Field.HTML, String.class); }

    /** Sets the value of the rich_text_area(html). */
    @NotNull public TextFieldShowcaseForm setHtml(@Nullable String html) {
        f.set(Field.HTML, html);
        return (TextFieldShowcaseForm) this;
    }

    /** Returns the value of the text_field(textField25). */
    @NotNull public String getTextField25() { return f.get(Field.TEXT_FIELD25, String.class); }

    /** Sets the value of the text_field(textField25). */
    @NotNull public TextFieldShowcaseForm setTextField25(@NotNull String textField25) {
        f.set(Field.TEXT_FIELD25, textField25);
        return (TextFieldShowcaseForm) this;
    }

    /** Returns the value of the text_field(string40). */
    @NotNull public String getString40() { return f.get(Field.STRING40, String.class); }

    /** Sets the value of the text_field(string40). */
    @NotNull public TextFieldShowcaseForm setString40(@NotNull String string40) {
        f.set(Field.STRING40, string40);
        return (TextFieldShowcaseForm) this;
    }

    /** Returns the value of the text_field(f1). */
    @Nullable public String getF1() { return f.get(Field.F1, String.class); }

    /** Sets the value of the text_field(f1). */
    @NotNull public TextFieldShowcaseForm setF1(@Nullable String f1) {
        f.set(Field.F1, f1);
        return (TextFieldShowcaseForm) this;
    }

    /** Returns the value of the text_field(f2). */
    @Nullable public String getF2() { return f.get(Field.F2, String.class); }

    /** Sets the value of the text_field(f2). */
    @NotNull public TextFieldShowcaseForm setF2(@Nullable String f2) {
        f.set(Field.F2, f2);
        return (TextFieldShowcaseForm) this;
    }

    /** Returns the value of the text_field(f3). */
    @Nullable public String getF3() { return f.get(Field.F3, String.class); }

    /** Sets the value of the text_field(f3). */
    @NotNull public TextFieldShowcaseForm setF3(@Nullable String f3) {
        f.set(Field.F3, f3);
        return (TextFieldShowcaseForm) this;
    }

    /** Returns the value of the text_field(f4). */
    @Nullable public String getF4() { return f.get(Field.F4, String.class); }

    /** Sets the value of the text_field(f4). */
    @NotNull public TextFieldShowcaseForm setF4(@Nullable String f4) {
        f.set(Field.F4, f4);
        return (TextFieldShowcaseForm) this;
    }

    /** Returns the value of the text_field(t1). */
    @Nullable public BigDecimal getT1() { return f.get(Field.T1, BigDecimal.class); }

    /** Sets the value of the text_field(t1). */
    @NotNull public TextFieldShowcaseForm setT1(@Nullable BigDecimal t1) {
        f.set(Field.T1, Decimals.scaleAndCheck("t1", t1, false, 4, 2));
        return (TextFieldShowcaseForm) this;
    }

    /** Returns the value of the text_field(t2). */
    @Nullable public BigDecimal getT2() { return f.get(Field.T2, BigDecimal.class); }

    /** Sets the value of the text_field(t2). */
    @NotNull public TextFieldShowcaseForm setT2(@Nullable BigDecimal t2) {
        f.set(Field.T2, Decimals.scaleAndCheck("t2", t2, false, 4, 2));
        return (TextFieldShowcaseForm) this;
    }

    /** Returns the value of the text_field(t3). */
    @Nullable public BigDecimal getT3() { return f.get(Field.T3, BigDecimal.class); }

    /** Sets the value of the text_field(t3). */
    @NotNull public TextFieldShowcaseForm setT3(@Nullable BigDecimal t3) {
        f.set(Field.T3, Decimals.scaleAndCheck("t3", t3, false, 4, 2));
        return (TextFieldShowcaseForm) this;
    }

    /** Returns the value of the text_field(t4). */
    @Nullable public BigDecimal getT4() { return f.get(Field.T4, BigDecimal.class); }

    /** Sets the value of the text_field(t4). */
    @NotNull public TextFieldShowcaseForm setT4(@Nullable BigDecimal t4) {
        f.set(Field.T4, Decimals.scaleAndCheck("t4", t4, false, 4, 2));
        return (TextFieldShowcaseForm) this;
    }

    /** Returns the value of the text_field(t5). */
    @NotNull public BigDecimal getT5() { return f.get(Field.T5, BigDecimal.class); }

    /** Sets the value of the text_field(t5). */
    @NotNull public TextFieldShowcaseForm setT5(@NotNull BigDecimal t5) {
        f.set(Field.T5, Decimals.scaleAndCheck("t5", t5, true, 4, 2));
        return (TextFieldShowcaseForm) this;
    }

    /** Returns the value of the text_field(t6). */
    @NotNull public BigDecimal getT6() { return f.get(Field.T6, BigDecimal.class); }

    /** Sets the value of the text_field(t6). */
    @NotNull public TextFieldShowcaseForm setT6(@NotNull BigDecimal t6) {
        f.set(Field.T6, Decimals.scaleAndCheck("t6", t6, true, 4, 2));
        return (TextFieldShowcaseForm) this;
    }

    /** Returns the value of the text_field(a1). */
    @Nullable public Integer getA1() { return f.get(Field.A1, Integer.class); }

    /** Sets the value of the text_field(a1). */
    @NotNull public TextFieldShowcaseForm setA1(@Nullable Integer a1) {
        f.set(Field.A1, a1);
        return (TextFieldShowcaseForm) this;
    }

    /** Returns the value of the text_field(a11). */
    public int getA11() { return f.get(Field.A11, Integer.class); }

    /** Sets the value of the text_field(a11). */
    @NotNull public TextFieldShowcaseForm setA11(int a11) {
        f.set(Field.A11, a11);
        return (TextFieldShowcaseForm) this;
    }

    /** Returns the value of the text_field(a12). */
    public int getA12() { return f.get(Field.A12, Integer.class); }

    /** Sets the value of the text_field(a12). */
    @NotNull public TextFieldShowcaseForm setA12(int a12) {
        f.set(Field.A12, a12);
        return (TextFieldShowcaseForm) this;
    }

    /** Returns the value of the text_field(a2). */
    @Nullable public Integer getA2() { return f.get(Field.A2, Integer.class); }

    /** Sets the value of the text_field(a2). */
    @NotNull public TextFieldShowcaseForm setA2(@Nullable Integer a2) {
        f.set(Field.A2, a2);
        return (TextFieldShowcaseForm) this;
    }

    /** Returns the value of the text_field(a3). */
    @Nullable public Integer getA3() { return f.get(Field.A3, Integer.class); }

    /** Sets the value of the text_field(a3). */
    @NotNull public TextFieldShowcaseForm setA3(@Nullable Integer a3) {
        f.set(Field.A3, a3);
        return (TextFieldShowcaseForm) this;
    }

    /** Returns the value of the text_field(a4). */
    @Nullable public Integer getA4() { return f.get(Field.A4, Integer.class); }

    /** Sets the value of the text_field(a4). */
    @NotNull public TextFieldShowcaseForm setA4(@Nullable Integer a4) {
        f.set(Field.A4, a4);
        return (TextFieldShowcaseForm) this;
    }

    /** Returns the value of the text_field(patente). */
    @Nullable public String getPatente() { return f.get(Field.PATENTE, String.class); }

    /** Sets the value of the text_field(patente). */
    @NotNull public TextFieldShowcaseForm setPatente(@Nullable String patente) {
        f.set(Field.PATENTE, patente);
        return (TextFieldShowcaseForm) this;
    }

    /** Returns the value of the text_field(m1). */
    @NotNull public BigDecimal getM1() { return f.get(Field.M1, BigDecimal.class); }

    /** Sets the value of the text_field(m1). */
    @NotNull public TextFieldShowcaseForm setM1(@NotNull BigDecimal m1) {
        f.set(Field.M1, Decimals.scaleAndCheck("m1", m1, false, 2, 1));
        return (TextFieldShowcaseForm) this;
    }

    /** Returns the value of the text_field(m2). */
    @NotNull public BigDecimal getM2() { return f.get(Field.M2, BigDecimal.class); }

    /** Sets the value of the text_field(m2). */
    @NotNull public TextFieldShowcaseForm setM2(@NotNull BigDecimal m2) {
        f.set(Field.M2, Decimals.scaleAndCheck("m2", m2, false, 10, 2));
        return (TextFieldShowcaseForm) this;
    }

    /** Returns the value of the text_field(m3). */
    public int getM3() { return f.get(Field.M3, Integer.class); }

    /** Sets the value of the text_field(m3). */
    @NotNull public TextFieldShowcaseForm setM3(int m3) {
        f.set(Field.M3, m3);
        return (TextFieldShowcaseForm) this;
    }

    /** Returns the value of the text_field(m4). */
    public int getM4() { return f.get(Field.M4, Integer.class); }

    /** Sets the value of the text_field(m4). */
    @NotNull public TextFieldShowcaseForm setM4(int m4) {
        f.set(Field.M4, m4);
        return (TextFieldShowcaseForm) this;
    }

    /** Returns the value of the display(patente2). */
    @NotNull public String getPatente2() { return f.get(Field.PATENTE2, String.class); }

    /** Sets the value of the display(patente2). */
    @NotNull public TextFieldShowcaseForm setPatente2(@NotNull String patente2) {
        f.set(Field.PATENTE2, patente2);
        return (TextFieldShowcaseForm) this;
    }

    /** Returns the value of the display(m1d). */
    @NotNull public BigDecimal getM1d() { return f.get(Field.M1D, BigDecimal.class); }

    /** Sets the value of the display(m1d). */
    @NotNull public TextFieldShowcaseForm setM1d(@NotNull BigDecimal m1d) {
        f.set(Field.M1D, Decimals.scaleAndCheck("m1d", m1d, false, 2, 1));
        return (TextFieldShowcaseForm) this;
    }

    /** Returns the value of the display(m2d). */
    @NotNull public BigDecimal getM2d() { return f.get(Field.M2D, BigDecimal.class); }

    /** Sets the value of the display(m2d). */
    @NotNull public TextFieldShowcaseForm setM2d(@NotNull BigDecimal m2d) {
        f.set(Field.M2D, Decimals.scaleAndCheck("m2d", m2d, false, 10, 2));
        return (TextFieldShowcaseForm) this;
    }

    /** Returns the value of the display(m3d). */
    public int getM3d() { return f.get(Field.M3D, Integer.class); }

    /** Sets the value of the display(m3d). */
    @NotNull public TextFieldShowcaseForm setM3d(int m3d) {
        f.set(Field.M3D, m3d);
        return (TextFieldShowcaseForm) this;
    }

    /** Returns the value of the display(m4d). */
    public int getM4d() { return f.get(Field.M4D, Integer.class); }

    /** Sets the value of the display(m4d). */
    @NotNull public TextFieldShowcaseForm setM4d(int m4d) {
        f.set(Field.M4D, m4d);
        return (TextFieldShowcaseForm) this;
    }

    /** Returns the value of the text_field(i1). */
    @NotNull public String getI1() { return f.get(Field.I1, String.class); }

    /** Sets the value of the text_field(i1). */
    @NotNull public TextFieldShowcaseForm setI1(@NotNull String i1) {
        f.set(Field.I1, i1);
        return (TextFieldShowcaseForm) this;
    }

    /** Returns the value of the text_field(i2). */
    @NotNull public String getI2() { return f.get(Field.I2, String.class); }

    /** Sets the value of the text_field(i2). */
    @NotNull public TextFieldShowcaseForm setI2(@NotNull String i2) {
        f.set(Field.I2, i2);
        return (TextFieldShowcaseForm) this;
    }

    /** 
     * Invoked when text_field(changeText) value changes
     * Invoked when text_field(uiChangeText) value ui changes
     * Invoked when text_field(changeTextDelay) value changes
     * Invoked when text_field(uiChangeTextDelay) value ui changes
     */
    @NotNull public abstract Action changedText();

    /** Returns the value of the text_field(changeText). */
    @Nullable public String getChangeText() { return f.get(Field.CHANGE_TEXT, String.class); }

    /** Sets the value of the text_field(changeText). */
    @NotNull public TextFieldShowcaseForm setChangeText(@Nullable String changeText) {
        f.set(Field.CHANGE_TEXT, changeText);
        return (TextFieldShowcaseForm) this;
    }

    /** Returns the value of the text_field(uiChangeText). */
    @Nullable public String getUiChangeText() { return f.get(Field.UI_CHANGE_TEXT, String.class); }

    /** Sets the value of the text_field(uiChangeText). */
    @NotNull public TextFieldShowcaseForm setUiChangeText(@Nullable String uiChangeText) {
        f.set(Field.UI_CHANGE_TEXT, uiChangeText);
        return (TextFieldShowcaseForm) this;
    }

    /** Returns the value of the text_field(changeTextDelay). */
    @Nullable public String getChangeTextDelay() { return f.get(Field.CHANGE_TEXT_DELAY, String.class); }

    /** Sets the value of the text_field(changeTextDelay). */
    @NotNull public TextFieldShowcaseForm setChangeTextDelay(@Nullable String changeTextDelay) {
        f.set(Field.CHANGE_TEXT_DELAY, changeTextDelay);
        return (TextFieldShowcaseForm) this;
    }

    /** Returns the value of the text_field(uiChangeTextDelay). */
    @Nullable public String getUiChangeTextDelay() { return f.get(Field.UI_CHANGE_TEXT_DELAY, String.class); }

    /** Sets the value of the text_field(uiChangeTextDelay). */
    @NotNull public TextFieldShowcaseForm setUiChangeTextDelay(@Nullable String uiChangeTextDelay) {
        f.set(Field.UI_CHANGE_TEXT_DELAY, uiChangeTextDelay);
        return (TextFieldShowcaseForm) this;
    }

    /** Returns the value of the text_field(c1). */
    @Nullable public BigDecimal getC1() { return f.get(Field.C1, BigDecimal.class); }

    /** Sets the value of the text_field(c1). */
    @NotNull public TextFieldShowcaseForm setC1(@Nullable BigDecimal c1) {
        f.set(Field.C1, Decimals.scaleAndCheck("c1", c1, true, 10, 2));
        return (TextFieldShowcaseForm) this;
    }

    /** Returns the value of the text_field(c2). */
    @Nullable public BigDecimal getC2() { return f.get(Field.C2, BigDecimal.class); }

    /** Sets the value of the text_field(c2). */
    @NotNull public TextFieldShowcaseForm setC2(@Nullable BigDecimal c2) {
        f.set(Field.C2, Decimals.scaleAndCheck("c2", c2, true, 4, 2));
        return (TextFieldShowcaseForm) this;
    }

    /** Returns the value of the text_field(c3). */
    @Nullable public BigDecimal getC3() { return f.get(Field.C3, BigDecimal.class); }

    /** Sets the value of the text_field(c3). */
    @NotNull public TextFieldShowcaseForm setC3(@Nullable BigDecimal c3) {
        f.set(Field.C3, Decimals.scaleAndCheck("c3", c3, false, 4, 2));
        return (TextFieldShowcaseForm) this;
    }

    /** Returns the value of the text_field(c4). */
    @Nullable public Integer getC4() { return f.get(Field.C4, Integer.class); }

    /** Sets the value of the text_field(c4). */
    @NotNull public TextFieldShowcaseForm setC4(@Nullable Integer c4) {
        f.set(Field.C4, c4);
        return (TextFieldShowcaseForm) this;
    }

    /** Returns the value of the text_field(c5). */
    @Nullable public Integer getC5() { return f.get(Field.C5, Integer.class); }

    /** Sets the value of the text_field(c5). */
    @NotNull public TextFieldShowcaseForm setC5(@Nullable Integer c5) {
        f.set(Field.C5, c5);
        return (TextFieldShowcaseForm) this;
    }

    /** Returns the value of the text_field(c6). */
    @Nullable public Double getC6() { return f.get(Field.C6, Double.class); }

    /** Sets the value of the text_field(c6). */
    @NotNull public TextFieldShowcaseForm setC6(@Nullable Double c6) {
        f.set(Field.C6, c6);
        return (TextFieldShowcaseForm) this;
    }

    /** Returns the value of the text_field(c7). */
    @Nullable public Double getC7() { return f.get(Field.C7, Double.class); }

    /** Sets the value of the text_field(c7). */
    @NotNull public TextFieldShowcaseForm setC7(@Nullable Double c7) {
        f.set(Field.C7, c7);
        return (TextFieldShowcaseForm) this;
    }

    /** 
     * Invoked when table(table) is clicked
     * Invoked when button(add) is clicked
     */
    @NotNull public abstract Action createNewRow();

    /** Returns a {@link FormTable<TableRow>} instance to handle Table manipulation */
    @NotNull public final FormTable<TableRow> getTable() { return table(Field.TABLE, TableRow.class); }

    /** Method to allow table row class override */
    @NotNull final <T> FormTable<T> table(@NotNull FormFieldRef field, @NotNull Class<T> rowClass) { return f.table(field, rowClass, this); }

    /** Register a form Listener */
    public static void addListener(@NotNull FormListenerType listenerType, @NotNull FormListener<TextFieldShowcaseForm> listener) { listeners.addListener(listenerType, listener); }

    /** Remove a form Listener */
    public static void removeListener(@NotNull FormListenerType listenerType, @NotNull FormListener<TextFieldShowcaseForm> listener) { listeners.removeListener(listenerType, listener); }

    @Override @NotNull public Logger logger() { return logger; }

    /** Returns utility methods to deal with {@link Action actions}. */
    @NotNull protected final Actions actions() { return actions; }

    //~ Fields ...................................................................................................................

    @NotNull private static FormListenerMap<TextFieldShowcaseForm> listeners = new FormListenerMap<>();
    @NotNull private static final Logger logger = Logger.getLogger(TextFieldShowcaseForm.class);

    //~ Inner Classes ............................................................................................................

    public abstract class TableRowBase
        implements FormRowInstance<TableRow>
    {

        //~ Fields ...................................................................................................................

        @SuppressWarnings({"InstanceVariableMayNotBeInitialized", "UnusedDeclaration", "NullableProblems"}) @NotNull private UiModelAccessor f;

        //~ Methods ..................................................................................................................

        /** Returns the value of the text_field(calc). */
        @Nullable public Integer getCalc() { return f.get(Field.CALC, Integer.class); }

        /** Sets the value of the text_field(calc). */
        @NotNull public TableRow setCalc(@Nullable Integer calc) {
            f.set(Field.CALC, calc);
            return (TableRow) this;
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
        @NotNull public final FormTable<TableRow> table() { return getTable(); }

        /** Remove row from table. */
        @NotNull public Action remove() {
            table().remove((TableRow) this);
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
        ID_KEY("idKey"),
        HTML("html"),
        TEXT_FIELD25("textField25"),
        STRING40("string40"),
        $V3("$V3"),
        F1("f1"),
        F2("f2"),
        F3("f3"),
        F4("f4"),
        $V4("$V4"),
        T1("t1"),
        T2("t2"),
        T3("t3"),
        T4("t4"),
        T5("t5"),
        T6("t6"),
        $V5("$V5"),
        A1("a1"),
        A11("a11"),
        A12("a12"),
        A2("a2"),
        A3("a3"),
        A4("a4"),
        $V6("$V6"),
        PATENTE("patente"),
        M1("m1"),
        M2("m2"),
        M3("m3"),
        M4("m4"),
        $V7("$V7"),
        PATENTE2("patente2"),
        M1D("m1d"),
        M2D("m2d"),
        M3D("m3d"),
        M4D("m4d"),
        $V8("$V8"),
        I1("i1"),
        I2("i2"),
        $V9("$V9"),
        CHANGE_TEXT("changeText"),
        UI_CHANGE_TEXT("uiChangeText"),
        CHANGE_TEXT_DELAY("changeTextDelay"),
        UI_CHANGE_TEXT_DELAY("uiChangeTextDelay"),
        $V10("$V10"),
        C1("c1"),
        C2("c2"),
        C3("c3"),
        C4("c4"),
        C5("c5"),
        C6("c6"),
        C7("c7"),
        TABLE("table"),
        CALC("calc"),
        ADD("add");

        //~ Fields ...................................................................................................................

        @Nullable private final String id;

        //~ Constructors .............................................................................................................

        Field(@Nullable final String id) { this.id = id; }

        //~ Methods ..................................................................................................................

        @NotNull public String id() { return id; }

    }
}
