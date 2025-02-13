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
import tekgenesis.showcase.TableGroupHideShowcase.TableRow;
import tekgenesis.form.UiModelAccessor;
import tekgenesis.form.configuration.WidgetConfiguration;

/** 
 * Generated base class for form: TableGroupHideShowcase.
 * Don't modify this as this is an auto generated class that's gets generated
 * every time the meta model file is modified. Use subclass instead.
 */
@SuppressWarnings({"DuplicateStringLiteralInspection", "WeakerAccess", "FieldMayBeFinal", "FieldCanBeLocal", "ConstantConditions", "ClassWithTooManyMethods", "ClassWithTooManyFields", "MagicNumber", "MethodOverridesStaticMethodOfSuperclass", "ParameterHidesMemberVariable", "FieldNameHidesFieldInSuperclass", "OverlyComplexClass", "RedundantCast", "UnusedReturnValue", "LocalVariableHidesMemberVariable", "UnusedParameters", "EmptyMethod"})
public abstract class TableGroupHideShowcaseBase
    extends FormInstance<Void>
    implements LoggableInstance
{

    //~ Fields ...................................................................................................................

    @SuppressWarnings({"InstanceVariableMayNotBeInitialized", "UnusedDeclaration", "NullableProblems"}) @NotNull private UiModelAccessor f;

    //~ Methods ..................................................................................................................

    /** Invoked when populating a form instance */
    @NotNull public final Void populate() {
        throw new FormCannotBePopulatedException("tekgenesis.showcase.TableGroupHideShowcase");
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

    /** Returns the value of the combo_box(external). */
    @NotNull public Type getExternal() { return Type.valueOf(f.get(Field.EXTERNAL, String.class)); }

    /** Sets the value of the combo_box(external). */
    @NotNull public TableGroupHideShowcase setExternal(@NotNull Type external) {
        f.set(Field.EXTERNAL, external);
        return (TableGroupHideShowcase) this;
    }

    /** Sets the options of the combo_box(external). */
    public void setExternalOptions(@NotNull Iterable<Type> items) { f.opts(Field.EXTERNAL, items); }

    /** Sets the options of the combo_box(external) with the given KeyMap. */
    public void setExternalOptions(@NotNull KeyMap items) { f.opts(Field.EXTERNAL, items); }

    /** Returns a {@link FormTable<TableRow>} instance to handle Table manipulation */
    @NotNull public final FormTable<TableRow> getTable() { return table(Field.TABLE, TableRow.class); }

    /** Method to allow table row class override */
    @NotNull final <T> FormTable<T> table(@NotNull FormFieldRef field, @NotNull Class<T> rowClass) { return f.table(field, rowClass, this); }

    /** Sets the options of the combo_box(type). */
    public void setTypeOptions(@NotNull Iterable<Type> items) { f.opts(Field.TYPE, items); }

    /** Sets the options of the combo_box(type) with the given KeyMap. */
    public void setTypeOptions(@NotNull KeyMap items) { f.opts(Field.TYPE, items); }

    /** Register a form Listener */
    public static void addListener(@NotNull FormListenerType listenerType, @NotNull FormListener<TableGroupHideShowcase> listener) { listeners.addListener(listenerType, listener); }

    /** Remove a form Listener */
    public static void removeListener(@NotNull FormListenerType listenerType, @NotNull FormListener<TableGroupHideShowcase> listener) { listeners.removeListener(listenerType, listener); }

    @Override @NotNull public Logger logger() { return logger; }

    /** Returns utility methods to deal with {@link Action actions}. */
    @NotNull protected final Actions actions() { return actions; }

    //~ Fields ...................................................................................................................

    @NotNull private static FormListenerMap<TableGroupHideShowcase> listeners = new FormListenerMap<>();
    @NotNull private static final Logger logger = Logger.getLogger(TableGroupHideShowcase.class);

    //~ Inner Classes ............................................................................................................

    public abstract class TableRowBase
        implements FormRowInstance<TableRow>
    {

        //~ Fields ...................................................................................................................

        @SuppressWarnings({"InstanceVariableMayNotBeInitialized", "UnusedDeclaration", "NullableProblems"}) @NotNull private UiModelAccessor f;

        //~ Methods ..................................................................................................................

        /** Returns the value of the combo_box(type). */
        @NotNull public Type getType() { return Type.valueOf(f.get(Field.TYPE, String.class)); }

        /** Sets the value of the combo_box(type). */
        @NotNull public TableRow setType(@NotNull Type type) {
            f.set(Field.TYPE, type);
            return (TableRow) this;
        }

        /** Sets the options of the combo_box(type). */
        public void setTypeOptions(@NotNull Iterable<Type> items) { f.opts(Field.TYPE, items); }

        /** Sets the options of the combo_box(type) with the given KeyMap. */
        public void setTypeOptions(@NotNull KeyMap items) { f.opts(Field.TYPE, items); }

        /** Returns the value of the display(a). */
        @Nullable public String getA() { return f.get(Field.A, String.class); }

        /** Sets the value of the display(a). */
        @NotNull public TableRow setA(@Nullable String a) {
            f.set(Field.A, a);
            return (TableRow) this;
        }

        /** Returns the value of the display(b). */
        @Nullable public String getB() { return f.get(Field.B, String.class); }

        /** Sets the value of the display(b). */
        @NotNull public TableRow setB(@Nullable String b) {
            f.set(Field.B, b);
            return (TableRow) this;
        }

        /** Returns the value of the display(c). */
        @Nullable public String getC() { return f.get(Field.C, String.class); }

        /** Sets the value of the display(c). */
        @NotNull public TableRow setC(@Nullable String c) {
            f.set(Field.C, c);
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
        EXTERNAL("external"),
        TABLE("table"),
        TYPE("type"),
        $H3("$H3"),
        A("a"),
        B("b"),
        C("c"),
        $H4("$H4"),
        X("x"),
        Y("y"),
        Z("z"),
        $H5("$H5"),
        ADD_ROW_BOTTOM("addRowBottom"),
        REMOVE_ROW_BOTTOM("removeRowBottom");

        //~ Fields ...................................................................................................................

        @Nullable private final String id;

        //~ Constructors .............................................................................................................

        Field(@Nullable final String id) { this.id = id; }

        //~ Methods ..................................................................................................................

        @NotNull public String id() { return id; }

    }
}
