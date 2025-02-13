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
import tekgenesis.showcase.InsideScreenForm.Tab1Row;
import tekgenesis.showcase.InsideScreenForm.Tab2Row;
import tekgenesis.form.UiModelAccessor;
import tekgenesis.form.configuration.WidgetConfiguration;

/** 
 * Generated base class for form: InsideScreenForm.
 * Don't modify this as this is an auto generated class that's gets generated
 * every time the meta model file is modified. Use subclass instead.
 */
@SuppressWarnings({"DuplicateStringLiteralInspection", "WeakerAccess", "FieldMayBeFinal", "FieldCanBeLocal", "ConstantConditions", "ClassWithTooManyMethods", "ClassWithTooManyFields", "MagicNumber", "MethodOverridesStaticMethodOfSuperclass", "ParameterHidesMemberVariable", "FieldNameHidesFieldInSuperclass", "OverlyComplexClass", "RedundantCast", "UnusedReturnValue", "LocalVariableHidesMemberVariable", "UnusedParameters", "EmptyMethod"})
public abstract class InsideScreenFormBase
    extends FormInstance<Void>
    implements LoggableInstance
{

    //~ Fields ...................................................................................................................

    @SuppressWarnings({"InstanceVariableMayNotBeInitialized", "UnusedDeclaration", "NullableProblems"}) @NotNull private UiModelAccessor f;

    //~ Methods ..................................................................................................................

    /** Invoked when populating a form instance */
    @NotNull public final Void populate() {
        throw new FormCannotBePopulatedException("tekgenesis.showcase.InsideScreenForm");
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

    /** Returns a {@link FormTable<Tab1Row>} instance to handle Tab1 manipulation */
    @NotNull public final FormTable<Tab1Row> getTab1() { return table(Field.TAB1, Tab1Row.class); }

    /** Method to allow table row class override */
    @NotNull final <T> FormTable<T> table(@NotNull FormFieldRef field, @NotNull Class<T> rowClass) { return f.table(field, rowClass, this); }

    /** Returns a {@link FormTable<Tab2Row>} instance to handle Tab2 manipulation */
    @NotNull public final FormTable<Tab2Row> getTab2() { return table(Field.TAB2, Tab2Row.class); }

    /** Returns the value of the dialog(some). */
    public boolean isSome() { return f.get(Field.SOME, Boolean.class); }

    /** Sets the value of the dialog(some). */
    @NotNull public InsideScreenForm setSome(boolean some) {
        f.set(Field.SOME, some);
        return (InsideScreenForm) this;
    }

    /** Register a form Listener */
    public static void addListener(@NotNull FormListenerType listenerType, @NotNull FormListener<InsideScreenForm> listener) { listeners.addListener(listenerType, listener); }

    /** Remove a form Listener */
    public static void removeListener(@NotNull FormListenerType listenerType, @NotNull FormListener<InsideScreenForm> listener) { listeners.removeListener(listenerType, listener); }

    @Override @NotNull public Logger logger() { return logger; }

    /** Returns utility methods to deal with {@link Action actions}. */
    @NotNull protected final Actions actions() { return actions; }

    //~ Fields ...................................................................................................................

    @NotNull private static FormListenerMap<InsideScreenForm> listeners = new FormListenerMap<>();
    @NotNull private static final Logger logger = Logger.getLogger(InsideScreenForm.class);

    //~ Inner Classes ............................................................................................................

    public abstract class Tab1RowBase
        implements FormRowInstance<Tab1Row>
    {

        //~ Fields ...................................................................................................................

        @SuppressWarnings({"InstanceVariableMayNotBeInitialized", "UnusedDeclaration", "NullableProblems"}) @NotNull private UiModelAccessor f;

        //~ Methods ..................................................................................................................

        /** Returns the value of the text_field(fst). */
        @NotNull public String getFst() { return f.get(Field.FST, String.class); }

        /** Sets the value of the text_field(fst). */
        @NotNull public Tab1Row setFst(@NotNull String fst) {
            f.set(Field.FST, fst);
            return (Tab1Row) this;
        }

        /** Returns the value of the text_field(snd). */
        @NotNull public String getSnd() { return f.get(Field.SND, String.class); }

        /** Sets the value of the text_field(snd). */
        @NotNull public Tab1Row setSnd(@NotNull String snd) {
            f.set(Field.SND, snd);
            return (Tab1Row) this;
        }

        /** Returns the value of the text_field(tth). */
        @NotNull public String getTth() { return f.get(Field.TTH, String.class); }

        /** Sets the value of the text_field(tth). */
        @NotNull public Tab1Row setTth(@NotNull String tth) {
            f.set(Field.TTH, tth);
            return (Tab1Row) this;
        }

        /** Returns the value of the text_field(fth). */
        @NotNull public String getFth() { return f.get(Field.FTH, String.class); }

        /** Sets the value of the text_field(fth). */
        @NotNull public Tab1Row setFth(@NotNull String fth) {
            f.set(Field.FTH, fth);
            return (Tab1Row) this;
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
        @NotNull public final FormTable<Tab1Row> table() { return getTab1(); }

        /** Remove row from table. */
        @NotNull public Action remove() {
            table().remove((Tab1Row) this);
            return actions().getDefault();
        }

        /** Returns a typed configuration for a given field. */
        @NotNull protected <T extends WidgetConfiguration> T configuration(@NotNull Field field) { return f.config(field); }

    }

    public abstract class Tab2RowBase
        implements FormRowInstance<Tab2Row>
    {

        //~ Fields ...................................................................................................................

        @SuppressWarnings({"InstanceVariableMayNotBeInitialized", "UnusedDeclaration", "NullableProblems"}) @NotNull private UiModelAccessor f;

        //~ Methods ..................................................................................................................

        /** Returns the value of the text_field(fst2). */
        @NotNull public String getFst2() { return f.get(Field.FST2, String.class); }

        /** Sets the value of the text_field(fst2). */
        @NotNull public Tab2Row setFst2(@NotNull String fst2) {
            f.set(Field.FST2, fst2);
            return (Tab2Row) this;
        }

        /** Returns the value of the text_field(snd2). */
        @NotNull public String getSnd2() { return f.get(Field.SND2, String.class); }

        /** Sets the value of the text_field(snd2). */
        @NotNull public Tab2Row setSnd2(@NotNull String snd2) {
            f.set(Field.SND2, snd2);
            return (Tab2Row) this;
        }

        /** Returns the value of the text_field(tth2). */
        @NotNull public String getTth2() { return f.get(Field.TTH2, String.class); }

        /** Sets the value of the text_field(tth2). */
        @NotNull public Tab2Row setTth2(@NotNull String tth2) {
            f.set(Field.TTH2, tth2);
            return (Tab2Row) this;
        }

        /** Returns the value of the text_field(fth2). */
        @NotNull public String getFth2() { return f.get(Field.FTH2, String.class); }

        /** Sets the value of the text_field(fth2). */
        @NotNull public Tab2Row setFth2(@NotNull String fth2) {
            f.set(Field.FTH2, fth2);
            return (Tab2Row) this;
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
        @NotNull public final FormTable<Tab2Row> table() { return getTab2(); }

        /** Remove row from table. */
        @NotNull public Action remove() {
            table().remove((Tab2Row) this);
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
        $P2("$P2"),
        $H3("$H3"),
        $L4("$L4"),
        $T5("$T5"),
        $T6("$T6"),
        TAB1("tab1"),
        FST("fst"),
        SND("snd"),
        TTH("tth"),
        FTH("fth"),
        FFTH("ffth"),
        L1("l1"),
        L2("l2"),
        L3("l3"),
        $H7("$H7"),
        $B8("$B8"),
        $B9("$B9"),
        TAB2("tab2"),
        FST2("fst2"),
        SND2("snd2"),
        TTH2("tth2"),
        FTH2("fth2"),
        $P10("$P10"),
        $H11("$H11"),
        $L12("$L12"),
        $T13("$T13"),
        $T14("$T14"),
        $H15("$H15"),
        $B16("$B16"),
        $B17("$B17"),
        SOME("some"),
        $T18("$T18"),
        $C19("$C19"),
        $H20("$H20"),
        $T21("$T21"),
        $T22("$T22"),
        $P23("$P23"),
        $H24("$H24"),
        $L25("$L25"),
        $T26("$T26"),
        $T27("$T27"),
        $H28("$H28"),
        $T29("$T29"),
        $T30("$T30"),
        $P31("$P31"),
        $H32("$H32"),
        $L33("$L33"),
        $T34("$T34"),
        $T35("$T35"),
        $P36("$P36"),
        $H37("$H37"),
        $L38("$L38"),
        $T39("$T39"),
        $T40("$T40"),
        $P41("$P41"),
        $H42("$H42"),
        $L43("$L43"),
        $T44("$T44"),
        $T45("$T45"),
        $P46("$P46"),
        $H47("$H47"),
        $L48("$L48"),
        $T49("$T49"),
        $T50("$T50"),
        $T51("$T51"),
        $H52("$H52"),
        $T53("$T53"),
        $T54("$T54"),
        $V55("$V55"),
        $T56("$T56"),
        $T57("$T57"),
        $T58("$T58"),
        $T59("$T59"),
        $T60("$T60"),
        $F61("$F61"),
        $B62("$B62"),
        $B63("$B63");

        //~ Fields ...................................................................................................................

        @Nullable private final String id;

        //~ Constructors .............................................................................................................

        Field(@Nullable final String id) { this.id = id; }

        //~ Methods ..................................................................................................................

        @NotNull public String id() { return id; }

    }
}
