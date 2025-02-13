package tekgenesis.showcase;

import tekgenesis.form.Action;
import tekgenesis.form.Actions;
import tekgenesis.showcase.GroupShowcaseForm.BigTableRow;
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
import tekgenesis.showcase.GroupShowcaseForm.TableRow;
import tekgenesis.form.UiModelAccessor;
import tekgenesis.form.configuration.WidgetConfiguration;

/** 
 * Generated base class for form: GroupShowcaseForm.
 * Don't modify this as this is an auto generated class that's gets generated
 * every time the meta model file is modified. Use subclass instead.
 */
@SuppressWarnings({"DuplicateStringLiteralInspection", "WeakerAccess", "FieldMayBeFinal", "FieldCanBeLocal", "ConstantConditions", "ClassWithTooManyMethods", "ClassWithTooManyFields", "MagicNumber", "MethodOverridesStaticMethodOfSuperclass", "ParameterHidesMemberVariable", "FieldNameHidesFieldInSuperclass", "OverlyComplexClass", "RedundantCast", "UnusedReturnValue", "LocalVariableHidesMemberVariable", "UnusedParameters", "EmptyMethod"})
public abstract class GroupShowcaseFormBase
    extends FormInstance<Void>
    implements LoggableInstance
{

    //~ Fields ...................................................................................................................

    @SuppressWarnings({"InstanceVariableMayNotBeInitialized", "UnusedDeclaration", "NullableProblems"}) @NotNull private UiModelAccessor f;

    //~ Methods ..................................................................................................................

    /** Invoked when populating a form instance */
    @NotNull public final Void populate() {
        throw new FormCannotBePopulatedException("tekgenesis.showcase.GroupShowcaseForm");
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

    /** Returns a {@link FormTable<TableRow>} instance to handle Table manipulation */
    @NotNull public final FormTable<TableRow> getTable() { return table(Field.TABLE, TableRow.class); }

    /** Method to allow table row class override */
    @NotNull final <T> FormTable<T> table(@NotNull FormFieldRef field, @NotNull Class<T> rowClass) { return f.table(field, rowClass, this); }

    /** Returns a {@link FormTable<BigTableRow>} instance to handle BigTable manipulation */
    @NotNull public final FormTable<BigTableRow> getBigTable() { return table(Field.BIG_TABLE, BigTableRow.class); }

    /** Create and set a new ResizeSubForm instance */
    @NotNull public ResizeSubForm createSub() { return f.init(Field.SUB, ResizeSubForm.class); }

    /** Create and populates set a new ResizeSubForm instance with a pk */
    @NotNull public ResizeSubForm createSub(@NotNull String key) { return f.init(Field.SUB, ResizeSubForm.class, key); }

    /** 
     * Get the ResizeSubForm if defined, or null otherwise.
     * @see #createSub
     */
    @Nullable public ResizeSubForm getSub() { return f.subform(Field.SUB, ResizeSubForm.class); }

    /** Invoked when display($D51) is clicked */
    @NotNull public abstract Action doStuff();

    /** Register a form Listener */
    public static void addListener(@NotNull FormListenerType listenerType, @NotNull FormListener<GroupShowcaseForm> listener) { listeners.addListener(listenerType, listener); }

    /** Remove a form Listener */
    public static void removeListener(@NotNull FormListenerType listenerType, @NotNull FormListener<GroupShowcaseForm> listener) { listeners.removeListener(listenerType, listener); }

    @Override @NotNull public Logger logger() { return logger; }

    /** Returns utility methods to deal with {@link Action actions}. */
    @NotNull protected final Actions actions() { return actions; }

    //~ Fields ...................................................................................................................

    @NotNull private static FormListenerMap<GroupShowcaseForm> listeners = new FormListenerMap<>();
    @NotNull private static final Logger logger = Logger.getLogger(GroupShowcaseForm.class);

    //~ Inner Classes ............................................................................................................

    public abstract class TableRowBase
        implements FormRowInstance<TableRow>
    {

        //~ Fields ...................................................................................................................

        @SuppressWarnings({"InstanceVariableMayNotBeInitialized", "UnusedDeclaration", "NullableProblems"}) @NotNull private UiModelAccessor f;

        //~ Methods ..................................................................................................................

        /** Returns the value of the display(disp). */
        @NotNull public String getDisp() { return f.get(Field.DISP, String.class); }

        /** Sets the value of the display(disp). */
        @NotNull public TableRow setDisp(@NotNull String disp) {
            f.set(Field.DISP, disp);
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

    public abstract class BigTableRowBase
        implements FormRowInstance<BigTableRow>
    {

        //~ Fields ...................................................................................................................

        @SuppressWarnings({"InstanceVariableMayNotBeInitialized", "UnusedDeclaration", "NullableProblems"}) @NotNull private UiModelAccessor f;

        //~ Methods ..................................................................................................................

        /** Returns the value of the display(disp2). */
        @NotNull public String getDisp2() { return f.get(Field.DISP2, String.class); }

        /** Sets the value of the display(disp2). */
        @NotNull public BigTableRow setDisp2(@NotNull String disp2) {
            f.set(Field.DISP2, disp2);
            return (BigTableRow) this;
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
        @NotNull public final FormTable<BigTableRow> table() { return getBigTable(); }

        /** Remove row from table. */
        @NotNull public Action remove() {
            table().remove((BigTableRow) this);
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
        $I2("$I2"),
        $T3("$T3"),
        $T4("$T4"),
        $T5("$T5"),
        $T6("$T6"),
        $H7("$H7"),
        $T8("$T8"),
        $T9("$T9"),
        $H10("$H10"),
        $B11("$B11"),
        $B12("$B12"),
        $B13("$B13"),
        $B14("$B14"),
        $B15("$B15"),
        $B16("$B16"),
        $B17("$B17"),
        $B18("$B18"),
        $H19("$H19"),
        $L20("$L20"),
        $L21("$L21"),
        $L22("$L22"),
        $L23("$L23"),
        $L24("$L24"),
        $L25("$L25"),
        $L26("$L26"),
        $L27("$L27"),
        $H28("$H28"),
        $T29("$T29"),
        $T30("$T30"),
        $T31("$T31"),
        $V32("$V32"),
        $T33("$T33"),
        $T34("$T34"),
        $H35("$H35"),
        $T36("$T36"),
        $T37("$T37"),
        TABLE("table"),
        DISP("disp"),
        $T38("$T38"),
        $C39("$C39"),
        BIG_TABLE("bigTable"),
        DISP2("disp2"),
        $T40("$T40"),
        $C41("$C41"),
        $T42("$T42"),
        $T43("$T43"),
        $T44("$T44"),
        $T45("$T45"),
        $T46("$T46"),
        $T47("$T47"),
        $T48("$T48"),
        $T49("$T49"),
        $T50("$T50"),
        SUB("sub"),
        $D51("$D51");

        //~ Fields ...................................................................................................................

        @Nullable private final String id;

        //~ Constructors .............................................................................................................

        Field(@Nullable final String id) { this.id = id; }

        //~ Methods ..................................................................................................................

        @NotNull public String id() { return id; }

    }
}
