package tekgenesis.showcase;

import tekgenesis.form.Action;
import tekgenesis.form.Actions;
import java.math.BigDecimal;
import tekgenesis.common.core.Decimals;
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
import tekgenesis.common.Predefined;
import tekgenesis.showcase.FormD.TableRow;
import tekgenesis.form.UiModelAccessor;
import tekgenesis.form.configuration.WidgetConfiguration;

/** 
 * Generated base class for form: FormD.
 * Don't modify this as this is an auto generated class that's gets generated
 * every time the meta model file is modified. Use subclass instead.
 */
@SuppressWarnings({"DuplicateStringLiteralInspection", "WeakerAccess", "FieldMayBeFinal", "FieldCanBeLocal", "ConstantConditions", "ClassWithTooManyMethods", "ClassWithTooManyFields", "MagicNumber", "MethodOverridesStaticMethodOfSuperclass", "ParameterHidesMemberVariable", "FieldNameHidesFieldInSuperclass", "OverlyComplexClass", "RedundantCast", "UnusedReturnValue", "LocalVariableHidesMemberVariable", "UnusedParameters", "EmptyMethod"})
public abstract class FormDBase
    extends FormInstance<Void>
    implements LoggableInstance
{

    //~ Fields ...................................................................................................................

    @SuppressWarnings({"InstanceVariableMayNotBeInitialized", "UnusedDeclaration", "NullableProblems"}) @NotNull private UiModelAccessor f;

    //~ Methods ..................................................................................................................

    /** Invoked when populating a form instance */
    @NotNull public final Void populate() {
        throw new FormCannotBePopulatedException("tekgenesis.showcase.FormD");
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

    /** Invoked when the 'Create new' options of the suggest_box(a) is clicked */
    @NotNull public abstract Action some(@Nullable String text);

    /** Returns the value of the suggest_box(a). */
    @NotNull public TypeA getA() {
        return Predefined.ensureNotNull(TypeA.find(getAKey()), "'a' not found");
    }

    /** Returns the key value of the suggest_box(a). */
    @NotNull public String getAKey() { return f.get(Field.A, String.class); }

    /** Sets the value of the suggest_box(a). */
    @NotNull public FormD setA(@NotNull TypeA a) {
        f.set(Field.A, a);
        return (FormD) this;
    }

    /** Returns the value of the suggest_box(b). */
    @NotNull public TypeB getB() {
        return Predefined.ensureNotNull(TypeB.find(getBKey()), "'b' not found");
    }

    /** Returns the key value of the suggest_box(b). */
    @NotNull public String getBKey() { return f.get(Field.B, String.class); }

    /** Sets the value of the suggest_box(b). */
    @NotNull public FormD setB(@NotNull TypeB b) {
        f.set(Field.B, b);
        return (FormD) this;
    }

    /** Returns the value of the suggest_box(c). */
    @NotNull public TypeC getC() {
        return Predefined.ensureNotNull(TypeC.find(getCKey()), "'c' not found");
    }

    /** Returns the key value of the suggest_box(c). */
    @NotNull public String getCKey() { return f.get(Field.C, String.class); }

    /** Sets the value of the suggest_box(c). */
    @NotNull public FormD setC(@NotNull TypeC c) {
        f.set(Field.C, c);
        return (FormD) this;
    }

    /** Returns the value of the text_field(s). */
    @NotNull public String getS() { return f.get(Field.S, String.class); }

    /** Sets the value of the text_field(s). */
    @NotNull public FormD setS(@NotNull String s) {
        f.set(Field.S, s);
        return (FormD) this;
    }

    /** Returns a {@link FormTable<TableRow>} instance to handle Table manipulation */
    @NotNull public final FormTable<TableRow> getTable() { return table(Field.TABLE, TableRow.class); }

    /** Method to allow table row class override */
    @NotNull final <T> FormTable<T> table(@NotNull FormFieldRef field, @NotNull Class<T> rowClass) { return f.table(field, rowClass, this); }

    /** Register a form Listener */
    public static void addListener(@NotNull FormListenerType listenerType, @NotNull FormListener<FormD> listener) { listeners.addListener(listenerType, listener); }

    /** Remove a form Listener */
    public static void removeListener(@NotNull FormListenerType listenerType, @NotNull FormListener<FormD> listener) { listeners.removeListener(listenerType, listener); }

    @Override @NotNull public Logger logger() { return logger; }

    /** Returns utility methods to deal with {@link Action actions}. */
    @NotNull protected final Actions actions() { return actions; }

    //~ Fields ...................................................................................................................

    @NotNull private static FormListenerMap<FormD> listeners = new FormListenerMap<>();
    @NotNull private static final Logger logger = Logger.getLogger(FormD.class);

    //~ Inner Classes ............................................................................................................

    public abstract class TableRowBase
        implements FormRowInstance<TableRow>
    {

        //~ Fields ...................................................................................................................

        @SuppressWarnings({"InstanceVariableMayNotBeInitialized", "UnusedDeclaration", "NullableProblems"}) @NotNull private UiModelAccessor f;

        //~ Methods ..................................................................................................................

        /** Returns the value of the text_field(d). */
        @NotNull public BigDecimal getD() { return f.get(Field.D, BigDecimal.class); }

        /** Sets the value of the text_field(d). */
        @NotNull public TableRow setD(@NotNull BigDecimal d) {
            f.set(Field.D, Decimals.scaleAndCheck("d", d, false, 10, 2));
            return (TableRow) this;
        }

        /** Returns the value of the text_field(i). */
        public int getI() { return f.get(Field.I, Integer.class); }

        /** Sets the value of the text_field(i). */
        @NotNull public TableRow setI(int i) {
            f.set(Field.I, i);
            return (TableRow) this;
        }

        /** Returns the value of the text_field(r). */
        public double getR() { return f.get(Field.R, Double.class); }

        /** Sets the value of the text_field(r). */
        @NotNull public TableRow setR(double r) {
            f.set(Field.R, r);
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

        /** Populate field values with given typeA instance. */
        public void populate(@NotNull TypeA typeA) {
            setD(typeA.getD())
            	.setI(typeA.getI())
            	.setR(typeA.getR());
        }

        /** Copies field values to given typeA instance. */
        public void copyTo(@NotNull TypeA typeA) {
            typeA.setD(getD())
            	.setI(getI())
            	.setR(getR());
        }

    }

    @SuppressWarnings("UnnecessarySemicolon")
    public enum Field
        implements tekgenesis.metadata.form.widget.FormFieldRef
    {
        A("a"),
        B("b"),
        C("c"),
        S("s"),
        TABLE("table"),
        D("d"),
        I("i"),
        R("r"),
        $H0("$H0"),
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
