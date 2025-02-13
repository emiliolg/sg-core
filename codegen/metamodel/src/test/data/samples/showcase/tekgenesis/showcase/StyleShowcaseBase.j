package tekgenesis.showcase;

import tekgenesis.form.Action;
import tekgenesis.form.Actions;
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
import tekgenesis.showcase.StyleShowcase.Table1Row;
import tekgenesis.showcase.StyleShowcase.TableRow;
import tekgenesis.form.UiModelAccessor;
import tekgenesis.form.configuration.WidgetConfiguration;

/** 
 * Generated base class for form: StyleShowcase.
 * Don't modify this as this is an auto generated class that's gets generated
 * every time the meta model file is modified. Use subclass instead.
 */
@SuppressWarnings({"DuplicateStringLiteralInspection", "WeakerAccess", "FieldMayBeFinal", "FieldCanBeLocal", "ConstantConditions", "ClassWithTooManyMethods", "ClassWithTooManyFields", "MagicNumber", "MethodOverridesStaticMethodOfSuperclass", "ParameterHidesMemberVariable", "FieldNameHidesFieldInSuperclass", "OverlyComplexClass", "RedundantCast", "UnusedReturnValue", "LocalVariableHidesMemberVariable", "UnusedParameters", "EmptyMethod"})
public abstract class StyleShowcaseBase
    extends FormInstance<Void>
    implements LoggableInstance
{

    //~ Fields ...................................................................................................................

    @SuppressWarnings({"InstanceVariableMayNotBeInitialized", "UnusedDeclaration", "NullableProblems"}) @NotNull private UiModelAccessor f;

    //~ Methods ..................................................................................................................

    /** Invoked when populating a form instance */
    @NotNull public final Void populate() {
        throw new FormCannotBePopulatedException("tekgenesis.showcase.StyleShowcase");
    }

    /** Invoked when the form is loaded */
    public abstract void load();

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

    /** Returns the value of the text_field(inputMini). */
    @NotNull public String getInputMini() { return f.get(Field.INPUT_MINI, String.class); }

    /** Sets the value of the text_field(inputMini). */
    @NotNull public StyleShowcase setInputMini(@NotNull String inputMini) {
        f.set(Field.INPUT_MINI, inputMini);
        return (StyleShowcase) this;
    }

    /** Returns the value of the text_field(inputSmall). */
    @NotNull public String getInputSmall() { return f.get(Field.INPUT_SMALL, String.class); }

    /** Sets the value of the text_field(inputSmall). */
    @NotNull public StyleShowcase setInputSmall(@NotNull String inputSmall) {
        f.set(Field.INPUT_SMALL, inputSmall);
        return (StyleShowcase) this;
    }

    /** Returns the value of the text_field(inputMedium). */
    @NotNull public String getInputMedium() { return f.get(Field.INPUT_MEDIUM, String.class); }

    /** Sets the value of the text_field(inputMedium). */
    @NotNull public StyleShowcase setInputMedium(@NotNull String inputMedium) {
        f.set(Field.INPUT_MEDIUM, inputMedium);
        return (StyleShowcase) this;
    }

    /** Returns the value of the text_field(inputLarge). */
    @NotNull public String getInputLarge() { return f.get(Field.INPUT_LARGE, String.class); }

    /** Sets the value of the text_field(inputLarge). */
    @NotNull public StyleShowcase setInputLarge(@NotNull String inputLarge) {
        f.set(Field.INPUT_LARGE, inputLarge);
        return (StyleShowcase) this;
    }

    /** Returns the value of the text_field(inputXlarge). */
    @NotNull public String getInputXlarge() { return f.get(Field.INPUT_XLARGE, String.class); }

    /** Sets the value of the text_field(inputXlarge). */
    @NotNull public StyleShowcase setInputXlarge(@NotNull String inputXlarge) {
        f.set(Field.INPUT_XLARGE, inputXlarge);
        return (StyleShowcase) this;
    }

    /** Returns the value of the text_field(inputXxlarge). */
    @NotNull public String getInputXxlarge() { return f.get(Field.INPUT_XXLARGE, String.class); }

    /** Sets the value of the text_field(inputXxlarge). */
    @NotNull public StyleShowcase setInputXxlarge(@NotNull String inputXxlarge) {
        f.set(Field.INPUT_XXLARGE, inputXxlarge);
        return (StyleShowcase) this;
    }

    /** Returns the value of the text_field(inputFull). */
    @NotNull public String getInputFull() { return f.get(Field.INPUT_FULL, String.class); }

    /** Sets the value of the text_field(inputFull). */
    @NotNull public StyleShowcase setInputFull(@NotNull String inputFull) {
        f.set(Field.INPUT_FULL, inputFull);
        return (StyleShowcase) this;
    }

    /** Returns the value of the text_field(normalInput). */
    @NotNull public String getNormalInput() { return f.get(Field.NORMAL_INPUT, String.class); }

    /** Sets the value of the text_field(normalInput). */
    @NotNull public StyleShowcase setNormalInput(@NotNull String normalInput) {
        f.set(Field.NORMAL_INPUT, normalInput);
        return (StyleShowcase) this;
    }

    /** Returns the value of the text_field(noLabelInput). */
    @NotNull public String getNoLabelInput() { return f.get(Field.NO_LABEL_INPUT, String.class); }

    /** Sets the value of the text_field(noLabelInput). */
    @NotNull public StyleShowcase setNoLabelInput(@NotNull String noLabelInput) {
        f.set(Field.NO_LABEL_INPUT, noLabelInput);
        return (StyleShowcase) this;
    }

    /** Returns the value of the text_field(normalInput1). */
    @NotNull public String getNormalInput1() { return f.get(Field.NORMAL_INPUT1, String.class); }

    /** Sets the value of the text_field(normalInput1). */
    @NotNull public StyleShowcase setNormalInput1(@NotNull String normalInput1) {
        f.set(Field.NORMAL_INPUT1, normalInput1);
        return (StyleShowcase) this;
    }

    /** Returns the value of the text_field(formMargin1). */
    @NotNull public String getFormMargin1() { return f.get(Field.FORM_MARGIN1, String.class); }

    /** Sets the value of the text_field(formMargin1). */
    @NotNull public StyleShowcase setFormMargin1(@NotNull String formMargin1) {
        f.set(Field.FORM_MARGIN1, formMargin1);
        return (StyleShowcase) this;
    }

    /** Returns the value of the text_field(formMargin2). */
    @NotNull public String getFormMargin2() { return f.get(Field.FORM_MARGIN2, String.class); }

    /** Sets the value of the text_field(formMargin2). */
    @NotNull public StyleShowcase setFormMargin2(@NotNull String formMargin2) {
        f.set(Field.FORM_MARGIN2, formMargin2);
        return (StyleShowcase) this;
    }

    /** Returns the value of the text_field(formMargin3). */
    @NotNull public String getFormMargin3() { return f.get(Field.FORM_MARGIN3, String.class); }

    /** Sets the value of the text_field(formMargin3). */
    @NotNull public StyleShowcase setFormMargin3(@NotNull String formMargin3) {
        f.set(Field.FORM_MARGIN3, formMargin3);
        return (StyleShowcase) this;
    }

    /** Returns the value of the text_field(normalMargin). */
    @NotNull public String getNormalMargin() { return f.get(Field.NORMAL_MARGIN, String.class); }

    /** Sets the value of the text_field(normalMargin). */
    @NotNull public StyleShowcase setNormalMargin(@NotNull String normalMargin) {
        f.set(Field.NORMAL_MARGIN, normalMargin);
        return (StyleShowcase) this;
    }

    /** Returns a {@link FormTable<TableRow>} instance to handle Table manipulation */
    @NotNull public final FormTable<TableRow> getTable() { return table(Field.TABLE, TableRow.class); }

    /** Method to allow table row class override */
    @NotNull final <T> FormTable<T> table(@NotNull FormFieldRef field, @NotNull Class<T> rowClass) { return f.table(field, rowClass, this); }

    /** Returns a {@link FormTable<Table1Row>} instance to handle Table1 manipulation */
    @NotNull public final FormTable<Table1Row> getTable1() { return table(Field.TABLE1, Table1Row.class); }

    /** Returns the value of the image(image). */
    @NotNull public String getImage() { return f.get(Field.IMAGE, String.class); }

    /** Sets the value of the image(image). */
    @NotNull public StyleShowcase setImage(@NotNull String image) {
        f.set(Field.IMAGE, image);
        return (StyleShowcase) this;
    }

    /** Register a form Listener */
    public static void addListener(@NotNull FormListenerType listenerType, @NotNull FormListener<StyleShowcase> listener) { listeners.addListener(listenerType, listener); }

    /** Remove a form Listener */
    public static void removeListener(@NotNull FormListenerType listenerType, @NotNull FormListener<StyleShowcase> listener) { listeners.removeListener(listenerType, listener); }

    @Override @NotNull public Logger logger() { return logger; }

    /** Returns utility methods to deal with {@link Action actions}. */
    @NotNull protected final Actions actions() { return actions; }

    //~ Fields ...................................................................................................................

    @NotNull private static FormListenerMap<StyleShowcase> listeners = new FormListenerMap<>();
    @NotNull private static final Logger logger = Logger.getLogger(StyleShowcase.class);

    //~ Inner Classes ............................................................................................................

    public abstract class TableRowBase
        implements FormRowInstance<TableRow>
    {

        //~ Fields ...................................................................................................................

        @SuppressWarnings({"InstanceVariableMayNotBeInitialized", "UnusedDeclaration", "NullableProblems"}) @NotNull private UiModelAccessor f;

        //~ Methods ..................................................................................................................

        /** Returns the value of the display(firstName). */
        @NotNull public String getFirstName() { return f.get(Field.FIRST_NAME, String.class); }

        /** Sets the value of the display(firstName). */
        @NotNull public TableRow setFirstName(@NotNull String firstName) {
            f.set(Field.FIRST_NAME, firstName);
            return (TableRow) this;
        }

        /** Returns the value of the display(lastName). */
        @NotNull public String getLastName() { return f.get(Field.LAST_NAME, String.class); }

        /** Sets the value of the display(lastName). */
        @NotNull public TableRow setLastName(@NotNull String lastName) {
            f.set(Field.LAST_NAME, lastName);
            return (TableRow) this;
        }

        /** Returns the value of the display(username). */
        @NotNull public String getUsername() { return f.get(Field.USERNAME, String.class); }

        /** Sets the value of the display(username). */
        @NotNull public TableRow setUsername(@NotNull String username) {
            f.set(Field.USERNAME, username);
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

    public abstract class Table1RowBase
        implements FormRowInstance<Table1Row>
    {

        //~ Fields ...................................................................................................................

        @SuppressWarnings({"InstanceVariableMayNotBeInitialized", "UnusedDeclaration", "NullableProblems"}) @NotNull private UiModelAccessor f;

        //~ Methods ..................................................................................................................

        /** Returns the value of the display(firstName1). */
        @NotNull public String getFirstName1() { return f.get(Field.FIRST_NAME1, String.class); }

        /** Sets the value of the display(firstName1). */
        @NotNull public Table1Row setFirstName1(@NotNull String firstName1) {
            f.set(Field.FIRST_NAME1, firstName1);
            return (Table1Row) this;
        }

        /** Returns the value of the display(lastName1). */
        @NotNull public String getLastName1() { return f.get(Field.LAST_NAME1, String.class); }

        /** Sets the value of the display(lastName1). */
        @NotNull public Table1Row setLastName1(@NotNull String lastName1) {
            f.set(Field.LAST_NAME1, lastName1);
            return (Table1Row) this;
        }

        /** Returns the value of the display(username1). */
        @NotNull public String getUsername1() { return f.get(Field.USERNAME1, String.class); }

        /** Sets the value of the display(username1). */
        @NotNull public Table1Row setUsername1(@NotNull String username1) {
            f.set(Field.USERNAME1, username1);
            return (Table1Row) this;
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
        @NotNull public final FormTable<Table1Row> table() { return getTable1(); }

        /** Remove row from table. */
        @NotNull public Action remove() {
            table().remove((Table1Row) this);
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
        BUTTONS("buttons"),
        $H3("$H3"),
        BUTTON_DEFAULT("buttonDefault"),
        BUTTON_PRIMARY("buttonPrimary"),
        BUTTON_INFO("buttonInfo"),
        BUTTON_SUCCESS("buttonSuccess"),
        BUTTON_WARNING("buttonWarning"),
        BUTTON_DANGER("buttonDanger"),
        BUTTON_INVERSE("buttonInverse"),
        BUTTON_LINK("buttonLink"),
        BUTTON_SIZES("buttonSizes"),
        $H4("$H4"),
        BUTTON_LARGE("buttonLarge"),
        BUTTON_DEFAULT1("buttonDefault1"),
        BUTTON_SMALL("buttonSmall"),
        BUTTON_MINI("buttonMini"),
        PULLS("pulls"),
        $H5("$H5"),
        BUTTON_R("buttonR"),
        BUTTON_L("buttonL"),
        INPUT_SIZES("inputSizes"),
        $V6("$V6"),
        INPUT_MINI("inputMini"),
        INPUT_SMALL("inputSmall"),
        INPUT_MEDIUM("inputMedium"),
        INPUT_LARGE("inputLarge"),
        INPUT_XLARGE("inputXlarge"),
        INPUT_XXLARGE("inputXxlarge"),
        INPUT_FULL("inputFull"),
        NO_LABEL("noLabel"),
        $V7("$V7"),
        NORMAL_INPUT("normalInput"),
        NO_LABEL_INPUT("noLabelInput"),
        FORM_ALIGNED_MESSAGE("formAlignedMessage"),
        NORMAL_INPUT1("normalInput1"),
        FORM_MARGIN_LABEL("formMarginLabel"),
        $V8("$V8"),
        FORM_MARGIN1("formMargin1"),
        $V9("$V9"),
        FORM_MARGIN2("formMargin2"),
        $V10("$V10"),
        FORM_MARGIN3("formMargin3"),
        NORMAL_MARGIN("normalMargin"),
        MARGINS("margins"),
        $V11("$V11"),
        $H12("$H12"),
        $H13("$H13"),
        MARGIN_LEFT5("marginLeft5"),
        $H14("$H14"),
        MARGIN_LEFT10("marginLeft10"),
        $H15("$H15"),
        MARGIN_LEFT20("marginLeft20"),
        $H16("$H16"),
        MARGIN_LEFT30("marginLeft30"),
        $H17("$H17"),
        MARGIN_LEFT40("marginLeft40"),
        $H18("$H18"),
        $H19("$H19"),
        MARGIN_RIGHT5("marginRight5"),
        $H20("$H20"),
        MARGIN_RIGHT10("marginRight10"),
        $H21("$H21"),
        MARGIN_RIGHT20("marginRight20"),
        $H22("$H22"),
        MARGIN_RIGHT30("marginRight30"),
        $H23("$H23"),
        MARGIN_RIGHT40("marginRight40"),
        $H24("$H24"),
        $V25("$V25"),
        $H26("$H26"),
        MARGIN_TOP5("marginTop5"),
        $H27("$H27"),
        MARGIN_TOP10("marginTop10"),
        $H28("$H28"),
        MARGIN_TOP20("marginTop20"),
        $H29("$H29"),
        MARGIN_TOP30("marginTop30"),
        $H30("$H30"),
        MARGIN_TOP40("marginTop40"),
        $V31("$V31"),
        $H32("$H32"),
        MARGIN_BOTTOM5("marginBottom5"),
        $H33("$H33"),
        MARGIN_BOTTOM10("marginBottom10"),
        $H34("$H34"),
        MARGIN_BOTTOM20("marginBottom20"),
        $H35("$H35"),
        MARGIN_BOTTOM30("marginBottom30"),
        $H36("$H36"),
        MARGIN_BOTTOM40("marginBottom40"),
        $V37("$V37"),
        $H38("$H38"),
        MARGIN5("margin5"),
        $H39("$H39"),
        MARGIN10("margin10"),
        $H40("$H40"),
        MARGIN20("margin20"),
        $H41("$H41"),
        MARGIN30("margin30"),
        $H42("$H42"),
        MARGIN40("margin40"),
        CONDENSED("condensed"),
        TABLE("table"),
        FIRST_NAME("firstName"),
        LAST_NAME("lastName"),
        USERNAME("username"),
        NO_THEAD("noThead"),
        TABLE1("table1"),
        FIRST_NAME1("firstName1"),
        LAST_NAME1("lastName1"),
        USERNAME1("username1"),
        $H43("$H43"),
        BOLD("bold"),
        $H44("$H44"),
        MUTED("muted"),
        GRIDS("grids"),
        $H45("$H45"),
        MORE("more"),
        BOOT_LINK("bootLink"),
        $H46("$H46"),
        $H47("$H47"),
        L1("l1"),
        $H48("$H48"),
        L2("l2"),
        $H49("$H49"),
        L3("l3"),
        $H50("$H50"),
        L4("l4"),
        $H51("$H51"),
        L5("l5"),
        $H52("$H52"),
        L6("l6"),
        $H53("$H53"),
        L7("l7"),
        $H54("$H54"),
        L8("l8"),
        $H55("$H55"),
        L9("l9"),
        $H56("$H56"),
        L10("l10"),
        $H57("$H57"),
        L11("l11"),
        $H58("$H58"),
        L12("l12"),
        $H59("$H59"),
        $H60("$H60"),
        M1("m1"),
        $H61("$H61"),
        M2("m2"),
        $H62("$H62"),
        M3("m3"),
        $H63("$H63"),
        M4("m4"),
        $H64("$H64"),
        M5("m5"),
        $H65("$H65"),
        M6("m6"),
        $H66("$H66"),
        $H67("$H67"),
        N1("n1"),
        $H68("$H68"),
        N2("n2"),
        $H69("$H69"),
        N3("n3"),
        $H70("$H70"),
        N4("n4"),
        $H71("$H71"),
        $H72("$H72"),
        O1("o1"),
        $H73("$H73"),
        O2("o2"),
        $H74("$H74"),
        O3("o3"),
        $H75("$H75"),
        $H76("$H76"),
        P1("p1"),
        $H77("$H77"),
        P2("p2"),
        $H78("$H78"),
        $H79("$H79"),
        Q1("q1"),
        THUMBS("thumbs"),
        $H80("$H80"),
        IMAGE("image"),
        PALETTES("palettes"),
        $H81("$H81"),
        $H82("$H82"),
        $H83("$H83"),
        $H84("$H84"),
        $H85("$H85"),
        $H86("$H86"),
        $H87("$H87"),
        $H88("$H88"),
        $H89("$H89"),
        $H90("$H90"),
        $H91("$H91"),
        $H92("$H92"),
        $H93("$H93"),
        $H94("$H94"),
        $H95("$H95"),
        $H96("$H96"),
        $H97("$H97"),
        $H98("$H98");

        //~ Fields ...................................................................................................................

        @Nullable private final String id;

        //~ Constructors .............................................................................................................

        Field(@Nullable final String id) { this.id = id; }

        //~ Methods ..................................................................................................................

        @NotNull public String id() { return id; }

    }
}
