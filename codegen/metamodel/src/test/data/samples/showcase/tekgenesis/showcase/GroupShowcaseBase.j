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
import tekgenesis.model.KeyMap;
import tekgenesis.common.logging.LoggableInstance;
import tekgenesis.common.logging.Logger;
import tekgenesis.form.Message;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import tekgenesis.showcase.GroupShowcase.SectRow;
import tekgenesis.form.UiModelAccessor;
import tekgenesis.form.configuration.WidgetConfiguration;

/** 
 * Generated base class for form: GroupShowcase.
 * Don't modify this as this is an auto generated class that's gets generated
 * every time the meta model file is modified. Use subclass instead.
 */
@SuppressWarnings({"DuplicateStringLiteralInspection", "WeakerAccess", "FieldMayBeFinal", "FieldCanBeLocal", "ConstantConditions", "ClassWithTooManyMethods", "ClassWithTooManyFields", "MagicNumber", "MethodOverridesStaticMethodOfSuperclass", "ParameterHidesMemberVariable", "FieldNameHidesFieldInSuperclass", "OverlyComplexClass", "RedundantCast", "UnusedReturnValue", "LocalVariableHidesMemberVariable", "UnusedParameters", "EmptyMethod"})
public abstract class GroupShowcaseBase
    extends FormInstance<Void>
    implements LoggableInstance
{

    //~ Fields ...................................................................................................................

    @SuppressWarnings({"InstanceVariableMayNotBeInitialized", "UnusedDeclaration", "NullableProblems"}) @NotNull private UiModelAccessor f;

    //~ Methods ..................................................................................................................

    /** Invoked when populating a form instance */
    @NotNull public final Void populate() {
        throw new FormCannotBePopulatedException("tekgenesis.showcase.GroupShowcase");
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

    /** Invoked when popover(pop2) value changes */
    @NotNull public abstract Action popChange();

    /** Returns the value of the popover(pop2). */
    public boolean isPop2() { return f.get(Field.POP2, Boolean.class); }

    /** Sets the value of the popover(pop2). */
    @NotNull public GroupShowcase setPop2(boolean pop2) {
        f.set(Field.POP2, pop2);
        return (GroupShowcase) this;
    }

    /** Returns a {@link FormTable<SectRow>} instance to handle Sect manipulation */
    @NotNull public final FormTable<SectRow> getSect() { return table(Field.SECT, SectRow.class); }

    /** Method to allow table row class override */
    @NotNull final <T> FormTable<T> table(@NotNull FormFieldRef field, @NotNull Class<T> rowClass) { return f.table(field, rowClass, this); }

    /** Returns the value of the combo_box(combo). */
    @NotNull public Options getCombo() { return Options.valueOf(f.get(Field.COMBO, String.class)); }

    /** Sets the value of the combo_box(combo). */
    @NotNull public GroupShowcase setCombo(@NotNull Options combo) {
        f.set(Field.COMBO, combo);
        return (GroupShowcase) this;
    }

    /** Sets the options of the combo_box(combo). */
    public void setComboOptions(@NotNull Iterable<Options> items) { f.opts(Field.COMBO, items); }

    /** Sets the options of the combo_box(combo) with the given KeyMap. */
    public void setComboOptions(@NotNull KeyMap items) { f.opts(Field.COMBO, items); }

    /** Returns the value of the combo_box(combo2). */
    @NotNull public Options getCombo2() { return Options.valueOf(f.get(Field.COMBO2, String.class)); }

    /** Sets the value of the combo_box(combo2). */
    @NotNull public GroupShowcase setCombo2(@NotNull Options combo2) {
        f.set(Field.COMBO2, combo2);
        return (GroupShowcase) this;
    }

    /** Sets the options of the combo_box(combo2). */
    public void setCombo2Options(@NotNull Iterable<Options> items) { f.opts(Field.COMBO2, items); }

    /** Sets the options of the combo_box(combo2) with the given KeyMap. */
    public void setCombo2Options(@NotNull KeyMap items) { f.opts(Field.COMBO2, items); }

    /** Invoked when tabs(tab1) value changes */
    @NotNull public abstract Action tabChanged();

    /** Returns the value of the tabs(tab1). */
    public int getTab1() { return f.get(Field.TAB1, Integer.class); }

    /** Sets the value of the tabs(tab1). */
    @NotNull public GroupShowcase setTab1(int tab1) {
        f.set(Field.TAB1, tab1);
        return (GroupShowcase) this;
    }

    /** Returns the value of the text_field(tf1). */
    @NotNull public String getTf1() { return f.get(Field.TF1, String.class); }

    /** Sets the value of the text_field(tf1). */
    @NotNull public GroupShowcase setTf1(@NotNull String tf1) {
        f.set(Field.TF1, tf1);
        return (GroupShowcase) this;
    }

    /** Returns the value of the text_field(tf2). */
    @NotNull public String getTf2() { return f.get(Field.TF2, String.class); }

    /** Sets the value of the text_field(tf2). */
    @NotNull public GroupShowcase setTf2(@NotNull String tf2) {
        f.set(Field.TF2, tf2);
        return (GroupShowcase) this;
    }

    /** Returns the value of the text_field(tf3). */
    @NotNull public String getTf3() { return f.get(Field.TF3, String.class); }

    /** Sets the value of the text_field(tf3). */
    @NotNull public GroupShowcase setTf3(@NotNull String tf3) {
        f.set(Field.TF3, tf3);
        return (GroupShowcase) this;
    }

    /** Returns the value of the text_field(tf4). */
    @NotNull public String getTf4() { return f.get(Field.TF4, String.class); }

    /** Sets the value of the text_field(tf4). */
    @NotNull public GroupShowcase setTf4(@NotNull String tf4) {
        f.set(Field.TF4, tf4);
        return (GroupShowcase) this;
    }

    /** Register a form Listener */
    public static void addListener(@NotNull FormListenerType listenerType, @NotNull FormListener<GroupShowcase> listener) { listeners.addListener(listenerType, listener); }

    /** Remove a form Listener */
    public static void removeListener(@NotNull FormListenerType listenerType, @NotNull FormListener<GroupShowcase> listener) { listeners.removeListener(listenerType, listener); }

    @Override @NotNull public Logger logger() { return logger; }

    /** Returns utility methods to deal with {@link Action actions}. */
    @NotNull protected final Actions actions() { return actions; }

    //~ Fields ...................................................................................................................

    @NotNull private static FormListenerMap<GroupShowcase> listeners = new FormListenerMap<>();
    @NotNull private static final Logger logger = Logger.getLogger(GroupShowcase.class);

    //~ Inner Classes ............................................................................................................

    public abstract class SectRowBase
        implements FormRowInstance<SectRow>
    {

        //~ Fields ...................................................................................................................

        @SuppressWarnings({"InstanceVariableMayNotBeInitialized", "UnusedDeclaration", "NullableProblems"}) @NotNull private UiModelAccessor f;

        //~ Methods ..................................................................................................................

        /** Associates a new message to the specified field and returns it for any further configuration. */
        @NotNull public Message message(@NotNull Field field, @NotNull String msg) { return f.msg(field, msg); }

        /** Returns true if the field value is not null. */
        public boolean isDefined(@NotNull Field... fields) { return f.defined(fields); }

        /** Resets the given fields. */
        protected void reset(@NotNull Field... fields) { f.reset(fields); }

        /** Focuses given field. */
        protected void focus(@NotNull Field field) { f.focus(field); }

        /** Return associated table. */
        @NotNull public final FormTable<SectRow> table() { return getSect(); }

        /** Remove row from table. */
        @NotNull public Action remove() {
            table().remove((SectRow) this);
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
        $H3("$H3"),
        $T4("$T4"),
        $T5("$T5"),
        $V6("$V6"),
        $T7("$T7"),
        $T8("$T8"),
        POP2("pop2"),
        $H9("$H9"),
        $L10("$L10"),
        $V11("$V11"),
        $T12("$T12"),
        $T13("$T13"),
        $H14("$H14"),
        $T15("$T15"),
        $T16("$T16"),
        $V17("$V17"),
        $I18("$I18"),
        $T19("$T19"),
        $T20("$T20"),
        $I21("$I21"),
        $T22("$T22"),
        $T23("$T23"),
        $T24("$T24"),
        $I25("$I25"),
        $T26("$T26"),
        $T27("$T27"),
        $V28("$V28"),
        $T29("$T29"),
        $T30("$T30"),
        $T31("$T31"),
        $T32("$T32"),
        $T33("$T33"),
        $H34("$H34"),
        $T35("$T35"),
        $T36("$T36"),
        $H37("$H37"),
        $V38("$V38"),
        $H39("$H39"),
        $T40("$T40"),
        $T41("$T41"),
        $H42("$H42"),
        $T43("$T43"),
        $T44("$T44"),
        $V45("$V45"),
        $T46("$T46"),
        $T47("$T47"),
        $T48("$T48"),
        $T49("$T49"),
        $T50("$T50"),
        $I51("$I51"),
        $T52("$T52"),
        $T53("$T53"),
        $H54("$H54"),
        $T55("$T55"),
        $T56("$T56"),
        $I57("$I57"),
        $T58("$T58"),
        $T59("$T59"),
        $T60("$T60"),
        SECT("sect"),
        $V61("$V61"),
        $T62("$T62"),
        $T63("$T63"),
        $B64("$B64"),
        $H65("$H65"),
        COMBO("combo"),
        COMBO2("combo2"),
        $H66("$H66"),
        $C67("$C67"),
        $C68("$C68"),
        TAB1("tab1"),
        T1("t1"),
        TF1("tf1"),
        TF2("tf2"),
        T2("t2"),
        TF3("tf3"),
        TF4("tf4");

        //~ Fields ...................................................................................................................

        @Nullable private final String id;

        //~ Constructors .............................................................................................................

        Field(@Nullable final String id) { this.id = id; }

        //~ Methods ..................................................................................................................

        @NotNull public String id() { return id; }

    }
}
