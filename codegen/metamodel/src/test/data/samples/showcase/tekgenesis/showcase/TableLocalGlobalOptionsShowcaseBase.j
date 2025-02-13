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
import tekgenesis.common.collections.Seq;
import tekgenesis.showcase.TableLocalGlobalOptionsShowcase.TableRow;
import tekgenesis.form.UiModelAccessor;
import tekgenesis.form.configuration.WidgetConfiguration;

/** 
 * Generated base class for form: TableLocalGlobalOptionsShowcase.
 * Don't modify this as this is an auto generated class that's gets generated
 * every time the meta model file is modified. Use subclass instead.
 */
@SuppressWarnings({"DuplicateStringLiteralInspection", "WeakerAccess", "FieldMayBeFinal", "FieldCanBeLocal", "ConstantConditions", "ClassWithTooManyMethods", "ClassWithTooManyFields", "MagicNumber", "MethodOverridesStaticMethodOfSuperclass", "ParameterHidesMemberVariable", "FieldNameHidesFieldInSuperclass", "OverlyComplexClass", "RedundantCast", "UnusedReturnValue", "LocalVariableHidesMemberVariable", "UnusedParameters", "EmptyMethod"})
public abstract class TableLocalGlobalOptionsShowcaseBase
    extends FormInstance<Void>
    implements LoggableInstance
{

    //~ Fields ...................................................................................................................

    @SuppressWarnings({"InstanceVariableMayNotBeInitialized", "UnusedDeclaration", "NullableProblems"}) @NotNull private UiModelAccessor f;

    //~ Methods ..................................................................................................................

    /** Invoked when populating a form instance */
    @NotNull public final Void populate() {
        throw new FormCannotBePopulatedException("tekgenesis.showcase.TableLocalGlobalOptionsShowcase");
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

    /** Invoked when button(resetCombos) is clicked */
    @NotNull public abstract Action resetCombos();

    /** Returns the value of the combo_box(oneOptionCombo). */
    public int getOneOptionCombo() { return f.get(Field.ONE_OPTION_COMBO, Integer.class); }

    /** Sets the value of the combo_box(oneOptionCombo). */
    @NotNull public TableLocalGlobalOptionsShowcase setOneOptionCombo(int oneOptionCombo) {
        f.set(Field.ONE_OPTION_COMBO, oneOptionCombo);
        return (TableLocalGlobalOptionsShowcase) this;
    }

    /** Sets the options of the combo_box(oneOptionCombo). */
    public void setOneOptionComboOptions(@NotNull Iterable<Integer> items) { f.opts(Field.ONE_OPTION_COMBO, items); }

    /** Sets the options of the combo_box(oneOptionCombo) with the given KeyMap. */
    public void setOneOptionComboOptions(@NotNull KeyMap items) { f.opts(Field.ONE_OPTION_COMBO, items); }

    /** Returns the value of the combo_box(threeOptions). */
    public int getThreeOptions() { return f.get(Field.THREE_OPTIONS, Integer.class); }

    /** Sets the value of the combo_box(threeOptions). */
    @NotNull public TableLocalGlobalOptionsShowcase setThreeOptions(int threeOptions) {
        f.set(Field.THREE_OPTIONS, threeOptions);
        return (TableLocalGlobalOptionsShowcase) this;
    }

    /** Sets the options of the combo_box(threeOptions). */
    public void setThreeOptionsOptions(@NotNull Iterable<Integer> items) { f.opts(Field.THREE_OPTIONS, items); }

    /** Sets the options of the combo_box(threeOptions) with the given KeyMap. */
    public void setThreeOptionsOptions(@NotNull KeyMap items) { f.opts(Field.THREE_OPTIONS, items); }

    /** Returns the value of the combo_box(combo). */
    @NotNull public Type getCombo() { return Type.valueOf(f.get(Field.COMBO, String.class)); }

    /** Sets the value of the combo_box(combo). */
    @NotNull public TableLocalGlobalOptionsShowcase setCombo(@NotNull Type combo) {
        f.set(Field.COMBO, combo);
        return (TableLocalGlobalOptionsShowcase) this;
    }

    /** Sets the options of the combo_box(combo). */
    public void setComboOptions(@NotNull Iterable<Type> items) { f.opts(Field.COMBO, items); }

    /** Sets the options of the combo_box(combo) with the given KeyMap. */
    public void setComboOptions(@NotNull KeyMap items) { f.opts(Field.COMBO, items); }

    /** Returns the value of the combo_box(intCombos). */
    public int getIntCombos() { return f.get(Field.INT_COMBOS, Integer.class); }

    /** Sets the value of the combo_box(intCombos). */
    @NotNull public TableLocalGlobalOptionsShowcase setIntCombos(int intCombos) {
        f.set(Field.INT_COMBOS, intCombos);
        return (TableLocalGlobalOptionsShowcase) this;
    }

    /** Sets the options of the combo_box(intCombos). */
    public void setIntCombosOptions(@NotNull Iterable<Integer> items) { f.opts(Field.INT_COMBOS, items); }

    /** Sets the options of the combo_box(intCombos) with the given KeyMap. */
    public void setIntCombosOptions(@NotNull KeyMap items) { f.opts(Field.INT_COMBOS, items); }

    /** Returns the value of the combo_box(intCombosOptional). */
    @Nullable public Integer getIntCombosOptional() { return f.get(Field.INT_COMBOS_OPTIONAL, Integer.class); }

    /** Sets the value of the combo_box(intCombosOptional). */
    @NotNull public TableLocalGlobalOptionsShowcase setIntCombosOptional(@Nullable Integer intCombosOptional) {
        f.set(Field.INT_COMBOS_OPTIONAL, intCombosOptional);
        return (TableLocalGlobalOptionsShowcase) this;
    }

    /** Sets the options of the combo_box(intCombosOptional). */
    public void setIntCombosOptionalOptions(@NotNull Iterable<Integer> items) { f.opts(Field.INT_COMBOS_OPTIONAL, items); }

    /** Sets the options of the combo_box(intCombosOptional) with the given KeyMap. */
    public void setIntCombosOptionalOptions(@NotNull KeyMap items) { f.opts(Field.INT_COMBOS_OPTIONAL, items); }

    /** Returns the value of the combo_box(stringOptional). */
    @Nullable public String getStringOptional() { return f.get(Field.STRING_OPTIONAL, String.class); }

    /** Sets the value of the combo_box(stringOptional). */
    @NotNull public TableLocalGlobalOptionsShowcase setStringOptional(@Nullable String stringOptional) {
        f.set(Field.STRING_OPTIONAL, stringOptional);
        return (TableLocalGlobalOptionsShowcase) this;
    }

    /** Sets the options of the combo_box(stringOptional). */
    public void setStringOptionalOptions(@NotNull Iterable<String> items) { f.opts(Field.STRING_OPTIONAL, items); }

    /** Sets the options of the combo_box(stringOptional) with the given KeyMap. */
    public void setStringOptionalOptions(@NotNull KeyMap items) { f.opts(Field.STRING_OPTIONAL, items); }

    /** Returns the value of the combo_box(stringRequired). */
    @NotNull public String getStringRequired() { return f.get(Field.STRING_REQUIRED, String.class); }

    /** Sets the value of the combo_box(stringRequired). */
    @NotNull public TableLocalGlobalOptionsShowcase setStringRequired(@NotNull String stringRequired) {
        f.set(Field.STRING_REQUIRED, stringRequired);
        return (TableLocalGlobalOptionsShowcase) this;
    }

    /** Sets the options of the combo_box(stringRequired). */
    public void setStringRequiredOptions(@NotNull Iterable<String> items) { f.opts(Field.STRING_REQUIRED, items); }

    /** Sets the options of the combo_box(stringRequired) with the given KeyMap. */
    public void setStringRequiredOptions(@NotNull KeyMap items) { f.opts(Field.STRING_REQUIRED, items); }

    /** Returns the value of the combo_box(stringCombos). */
    @NotNull public String getStringCombos() { return f.get(Field.STRING_COMBOS, String.class); }

    /** Sets the value of the combo_box(stringCombos). */
    @NotNull public TableLocalGlobalOptionsShowcase setStringCombos(@NotNull String stringCombos) {
        f.set(Field.STRING_COMBOS, stringCombos);
        return (TableLocalGlobalOptionsShowcase) this;
    }

    /** Sets the options of the combo_box(stringCombos). */
    public void setStringCombosOptions(@NotNull Iterable<String> items) { f.opts(Field.STRING_COMBOS, items); }

    /** Sets the options of the combo_box(stringCombos) with the given KeyMap. */
    public void setStringCombosOptions(@NotNull KeyMap items) { f.opts(Field.STRING_COMBOS, items); }

    /** Returns the value of the tags_combo_box(tagsCombo). */
    @NotNull public Seq<Type> getTagsCombo() { return f.getArray(Field.TAGS_COMBO, Type.class); }

    /** Sets the value of the tags_combo_box(tagsCombo). */
    @NotNull public TableLocalGlobalOptionsShowcase setTagsCombo(@NotNull Iterable<Type> tagsCombo) {
        f.setArray(Field.TAGS_COMBO, tagsCombo);
        return (TableLocalGlobalOptionsShowcase) this;
    }

    /** Sets the options of the tags_combo_box(tagsCombo). */
    public void setTagsComboOptions(@NotNull Iterable<Type> items) { f.opts(Field.TAGS_COMBO, items); }

    /** Sets the options of the tags_combo_box(tagsCombo) with the given KeyMap. */
    public void setTagsComboOptions(@NotNull KeyMap items) { f.opts(Field.TAGS_COMBO, items); }

    /** Invoked when button(comboButton) is clicked */
    @NotNull public abstract Action setE();

    /** Invoked when button(comboTagsButton) is clicked */
    @NotNull public abstract Action setTagsE();

    /** Returns a {@link FormTable<TableRow>} instance to handle Table manipulation */
    @NotNull public final FormTable<TableRow> getTable() { return table(Field.TABLE, TableRow.class); }

    /** Method to allow table row class override */
    @NotNull final <T> FormTable<T> table(@NotNull FormFieldRef field, @NotNull Class<T> rowClass) { return f.table(field, rowClass, this); }

    /** Sets the options of the combo_box(b). */
    public void setBOptions(@NotNull Iterable<Type> items) { f.opts(Field.B, items); }

    /** Sets the options of the combo_box(b) with the given KeyMap. */
    public void setBOptions(@NotNull KeyMap items) { f.opts(Field.B, items); }

    /** Sets the options of the combo_box(b1). */
    public void setB1Options(@NotNull Iterable<Type> items) { f.opts(Field.B1, items); }

    /** Sets the options of the combo_box(b1) with the given KeyMap. */
    public void setB1Options(@NotNull KeyMap items) { f.opts(Field.B1, items); }

    /** Sets the options of the combo_box(c). */
    public void setCOptions(@NotNull Iterable<Type> items) { f.opts(Field.C, items); }

    /** Sets the options of the combo_box(c) with the given KeyMap. */
    public void setCOptions(@NotNull KeyMap items) { f.opts(Field.C, items); }

    /** Sets the options of the combo_box(d). */
    public void setDOptions(@NotNull Iterable<Type> items) { f.opts(Field.D, items); }

    /** Sets the options of the combo_box(d) with the given KeyMap. */
    public void setDOptions(@NotNull KeyMap items) { f.opts(Field.D, items); }

    /** Invoked when button(change) is clicked */
    @NotNull public abstract Action changeOptions();

    /** Register a form Listener */
    public static void addListener(@NotNull FormListenerType listenerType, @NotNull FormListener<TableLocalGlobalOptionsShowcase> listener) { listeners.addListener(listenerType, listener); }

    /** Remove a form Listener */
    public static void removeListener(@NotNull FormListenerType listenerType, @NotNull FormListener<TableLocalGlobalOptionsShowcase> listener) { listeners.removeListener(listenerType, listener); }

    @Override @NotNull public Logger logger() { return logger; }

    /** Returns utility methods to deal with {@link Action actions}. */
    @NotNull protected final Actions actions() { return actions; }

    //~ Fields ...................................................................................................................

    @NotNull private static FormListenerMap<TableLocalGlobalOptionsShowcase> listeners = new FormListenerMap<>();
    @NotNull private static final Logger logger = Logger.getLogger(TableLocalGlobalOptionsShowcase.class);

    //~ Inner Classes ............................................................................................................

    public abstract class TableRowBase
        implements FormRowInstance<TableRow>
    {

        //~ Fields ...................................................................................................................

        @SuppressWarnings({"InstanceVariableMayNotBeInitialized", "UnusedDeclaration", "NullableProblems"}) @NotNull private UiModelAccessor f;

        //~ Methods ..................................................................................................................

        /** Returns the value of the display(a). */
        @NotNull public String getA() { return f.get(Field.A, String.class); }

        /** Sets the value of the display(a). */
        @NotNull public TableRow setA(@NotNull String a) {
            f.set(Field.A, a);
            return (TableRow) this;
        }

        /** Returns the value of the combo_box(b). */
        @NotNull public Type getB() { return Type.valueOf(f.get(Field.B, String.class)); }

        /** Sets the value of the combo_box(b). */
        @NotNull public TableRow setB(@NotNull Type b) {
            f.set(Field.B, b);
            return (TableRow) this;
        }

        /** Sets the options of the combo_box(b). */
        public void setBOptions(@NotNull Iterable<Type> items) { f.opts(Field.B, items); }

        /** Sets the options of the combo_box(b) with the given KeyMap. */
        public void setBOptions(@NotNull KeyMap items) { f.opts(Field.B, items); }

        /** Returns the value of the combo_box(b1). */
        @NotNull public Type getB1() { return Type.valueOf(f.get(Field.B1, String.class)); }

        /** Sets the value of the combo_box(b1). */
        @NotNull public TableRow setB1(@NotNull Type b1) {
            f.set(Field.B1, b1);
            return (TableRow) this;
        }

        /** Sets the options of the combo_box(b1). */
        public void setB1Options(@NotNull Iterable<Type> items) { f.opts(Field.B1, items); }

        /** Sets the options of the combo_box(b1) with the given KeyMap. */
        public void setB1Options(@NotNull KeyMap items) { f.opts(Field.B1, items); }

        /** Returns the value of the combo_box(c). */
        @NotNull public Type getC() { return Type.valueOf(f.get(Field.C, String.class)); }

        /** Sets the value of the combo_box(c). */
        @NotNull public TableRow setC(@NotNull Type c) {
            f.set(Field.C, c);
            return (TableRow) this;
        }

        /** Sets the options of the combo_box(c). */
        public void setCOptions(@NotNull Iterable<Type> items) { f.opts(Field.C, items); }

        /** Sets the options of the combo_box(c) with the given KeyMap. */
        public void setCOptions(@NotNull KeyMap items) { f.opts(Field.C, items); }

        /** Returns the value of the combo_box(d). */
        @NotNull public Type getD() { return Type.valueOf(f.get(Field.D, String.class)); }

        /** Sets the value of the combo_box(d). */
        @NotNull public TableRow setD(@NotNull Type d) {
            f.set(Field.D, d);
            return (TableRow) this;
        }

        /** Sets the options of the combo_box(d). */
        public void setDOptions(@NotNull Iterable<Type> items) { f.opts(Field.D, items); }

        /** Sets the options of the combo_box(d) with the given KeyMap. */
        public void setDOptions(@NotNull KeyMap items) { f.opts(Field.D, items); }

        /** Invoked when button(button) is clicked */
        @NotNull public abstract Action setAtoB();

        /** Returns the value of the display(comment). */
        @NotNull public String getComment() { return f.get(Field.COMMENT, String.class); }

        /** Sets the value of the display(comment). */
        @NotNull public TableRow setComment(@NotNull String comment) {
            f.set(Field.COMMENT, comment);
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
        RESET_COMBOS("resetCombos"),
        $H3("$H3"),
        ONE_OPTION_COMBO("oneOptionCombo"),
        THREE_OPTIONS("threeOptions"),
        OPTION("option"),
        COMBO("combo"),
        INT_COMBOS("intCombos"),
        INT_COMBOS_OPTIONAL("intCombosOptional"),
        STRING_OPTIONAL("stringOptional"),
        STRING_REQUIRED("stringRequired"),
        STRING_COMBOS("stringCombos"),
        TAGS_COMBO("tagsCombo"),
        COMBO_BUTTON("comboButton"),
        COMBO_TAGS_BUTTON("comboTagsButton"),
        TABLE("table"),
        A("a"),
        B("b"),
        B1("b1"),
        C("c"),
        D("d"),
        BUTTON("button"),
        COMMENT("comment"),
        CHANGE("change");

        //~ Fields ...................................................................................................................

        @Nullable private final String id;

        //~ Constructors .............................................................................................................

        Field(@Nullable final String id) { this.id = id; }

        //~ Methods ..................................................................................................................

        @NotNull public String id() { return id; }

    }
}
