package tekgenesis.test;

import tekgenesis.form.Action;
import tekgenesis.form.exception.FormCannotBePopulatedException;
import tekgenesis.common.FormFieldRef;
import tekgenesis.form.FormImplementation;
import tekgenesis.form.FormInstance;
import tekgenesis.form.extension.FormListener;
import tekgenesis.form.extension.FormListenerMap;
import tekgenesis.form.extension.FormListenerType;
import tekgenesis.form.FormTable;
import tekgenesis.model.KeyMap;
import tekgenesis.form.Message;
import tekgenesis.test.TableWithCallbaFormListenerTypecksForm.MyItemRow;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import tekgenesis.form.configuration.WidgetConfiguration;

/** 
 * Generated base class for form: TableWithCallbaFormListenerTypecksForm.
 * Don't modify this as this is an auto generated class that's gets generated
 * every time the meta model file is modified. Use subclass instead.
 */
@SuppressWarnings({"DuplicateStringLiteralInspection", "WeakerAccess", "FieldMayBeFinal", "FieldCanBeLocal", "ConstantConditions", "ClassWithTooManyMethods", "ClassWithTooManyFields", "MagicNumber", "MethodOverridesStaticMethodOfSuperclass", "ParameterHidesMemberVariable", "FieldNameHidesFieldInSuperclass", "OverlyComplexClass", "RedundantCast", "UnusedReturnValue", "LocalVariableHidesMemberVariable"})
public abstract class TableWithCallbaFormListenerTypecksFormBase
    extends FormInstance<Void>
{

    //~ Fields ...................................................................................................................

    @SuppressWarnings({"InstanceVariableMayNotBeInitialized", "UnusedDeclaration", "NullableProblems"}) @NotNull private FormImplementation f;

    //~ Methods ..................................................................................................................

    /** Invoked when populating a form instance */
    @NotNull public Void populate() {
        throw new FormCannotBePopulatedException("tekgenesis.test.TableWithCallbaFormListenerTypecksForm");
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

    /** Invoked when text_field(outInt) value changes */
    @NotNull public abstract Action outerIntChange();

    /** Returns the value of the text_field(outInt). */
    public int getOutInt() { return f.get(Field.OUT_INT, Integer.class); }

    /** Sets the value of the text_field(outInt). */
    public void setOutInt(int outInt) { f.set(Field.OUT_INT, outInt); }

    /** Returns a {@link FormTable<MyItemRow>} instance to handle MyItem manipulation */
    @NotNull public final FormTable<MyItemRow> getMyItem() { return table(Field.MY_ITEM, MyItemRow.class); }

    /** Method to allow table row class override */
    @NotNull final <T> FormTable<T> table(@NotNull FormFieldRef field, @NotNull Class<T> rowClass) { return f.table(field, rowClass, this); }

    /** Sets the options of the combo_box(myCombo). */
    public void setMyComboOptions(@NotNull Iterable<String> items) { f.opts(Field.MY_COMBO, items); }

    /** Sets the options of the combo_box(myCombo) with the given KeyMap. */
    public void setMyComboOptions(@NotNull KeyMap items) { f.opts(Field.MY_COMBO, items); }

    /** Register a form Listener */
    public static void addListener(@NotNull FormListenerType listenerType, @NotNull FormListener<TableWithCallbaFormListenerTypecksForm> listener) { listeners.addListener(listenerType, listener); }

    /** Remove a form Listener */
    public static void removeListener(@NotNull FormListenerType listenerType, @NotNull FormListener<TableWithCallbaFormListenerTypecksForm> listener) { listeners.removeListener(listenerType, listener); }

    //~ Fields ...................................................................................................................

    @Nullable private static FormListenerMap<TableWithCallbaFormListenerTypecksForm> listeners = new FormListenerMap<>();

    //~ Inner Classes ............................................................................................................

    public abstract class MyItemRowBase
    {

        //~ Fields ...................................................................................................................

        @SuppressWarnings({"InstanceVariableMayNotBeInitialized", "UnusedDeclaration", "NullableProblems"}) @NotNull private FormImplementation f;

        //~ Methods ..................................................................................................................

        /** Invoked when text_field(myInt) value changes */
        @NotNull public abstract Action tableIntChange();

        /** Returns the value of the text_field(myInt). */
        public int getMyInt() { return f.get(Field.MY_INT, Integer.class); }

        /** Sets the value of the text_field(myInt). */
        public void setMyInt(int myInt) { f.set(Field.MY_INT, myInt); }

        /** Invoked when text_field(myString) value changes */
        @NotNull public abstract Action tableStringChange();

        /** Returns the value of the text_field(myString). */
        @NotNull public String getMyString() { return f.get(Field.MY_STRING, String.class); }

        /** Sets the value of the text_field(myString). */
        public void setMyString(@NotNull String myString) { f.set(Field.MY_STRING, myString); }

        /** Returns the value of the combo_box(myCombo). */
        @NotNull public String getMyCombo() { return f.get(Field.MY_COMBO, String.class); }

        /** Sets the value of the combo_box(myCombo). */
        public void setMyCombo(@NotNull String myCombo) { f.set(Field.MY_COMBO, myCombo); }

        /** Sets the options of the combo_box(myCombo). */
        public void setMyComboOptions(@NotNull Iterable<String> items) { f.opts(Field.MY_COMBO, items); }

        /** Sets the options of the combo_box(myCombo) with the given KeyMap. */
        public void setMyComboOptions(@NotNull KeyMap items) { f.opts(Field.MY_COMBO, items); }

        /** Associates a new message to the specified field and returns it for any further configuration. */
        @NotNull public Message message(@NotNull Field field, @NotNull String msg) { return f.msg(field, msg); }

        /** Returns true if the field value is not null. */
        public boolean isDefined(@NotNull Field... fields) { return f.defined(fields); }

        /** Resets the given fields. */
        protected void reset(@NotNull Field... fields) { f.reset(fields); }

        /** Focuses given field. */
        protected void focus(@NotNull Field field) { f.focus(field); }

        /** Return associated table. */
        @NotNull public final FormTable<MyItemRow> table() { return getMyItem(); }

        /** Remove row from table. */
        @NotNull public Action remove() {
            table().remove((MyItemRow) this);
            return actions.getDefault();
        }

        /** Returns a typed configuration for a given field. */
        @NotNull protected <T extends WidgetConfiguration> T configuration(@NotNull Field field) { return f.config(field); }

    }

    @SuppressWarnings("UnnecessarySemicolon")
    public enum Field
        implements tekgenesis.common.core.FormFieldRef
    {
        OUT_INT("outInt"),
        MY_ITEM("myItem"),
        MY_INT("myInt"),
        MY_STRING("myString"),
        MY_COMBO("myCombo");

        //~ Fields ...................................................................................................................

        @Nullable private final String id;

        //~ Constructors .............................................................................................................

        Field(@Nullable final String id) { this.id = id; }

        //~ Methods ..................................................................................................................

        @NotNull public String id() { return id; }

    }
}
