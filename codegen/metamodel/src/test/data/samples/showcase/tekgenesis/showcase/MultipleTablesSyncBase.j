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
import tekgenesis.showcase.MultipleTablesSync.PendingsRow;
import tekgenesis.form.UiModelAccessor;
import tekgenesis.form.configuration.WidgetConfiguration;

/** 
 * Generated base class for form: MultipleTablesSync.
 * Don't modify this as this is an auto generated class that's gets generated
 * every time the meta model file is modified. Use subclass instead.
 */
@SuppressWarnings({"DuplicateStringLiteralInspection", "WeakerAccess", "FieldMayBeFinal", "FieldCanBeLocal", "ConstantConditions", "ClassWithTooManyMethods", "ClassWithTooManyFields", "MagicNumber", "MethodOverridesStaticMethodOfSuperclass", "ParameterHidesMemberVariable", "FieldNameHidesFieldInSuperclass", "OverlyComplexClass", "RedundantCast", "UnusedReturnValue", "LocalVariableHidesMemberVariable", "UnusedParameters", "EmptyMethod"})
public abstract class MultipleTablesSyncBase
    extends FormInstance<Void>
    implements LoggableInstance
{

    //~ Fields ...................................................................................................................

    @SuppressWarnings({"InstanceVariableMayNotBeInitialized", "UnusedDeclaration", "NullableProblems"}) @NotNull private UiModelAccessor f;

    //~ Methods ..................................................................................................................

    /** Invoked when populating a form instance */
    @NotNull public final Void populate() {
        throw new FormCannotBePopulatedException("tekgenesis.showcase.MultipleTablesSync");
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

    /** Invoked when text_field(rows) value changes */
    @NotNull public abstract Action loadTables();

    /** Returns the value of the text_field(rows). */
    public int getRows() { return f.get(Field.ROWS, Integer.class); }

    /** Sets the value of the text_field(rows). */
    @NotNull public MultipleTablesSync setRows(int rows) {
        f.set(Field.ROWS, rows);
        return (MultipleTablesSync) this;
    }

    /** Returns the value of the combo_box(printer). */
    @Nullable public Boolean isPrinter() { return f.get(Field.PRINTER, Boolean.class); }

    /** Sets the value of the combo_box(printer). */
    @NotNull public MultipleTablesSync setPrinter(@Nullable Boolean printer) {
        f.set(Field.PRINTER, printer);
        return (MultipleTablesSync) this;
    }

    /** Sets the options of the combo_box(printer). */
    public void setPrinterOptions(@NotNull Iterable<Boolean> items) { f.opts(Field.PRINTER, items); }

    /** Sets the options of the combo_box(printer) with the given KeyMap. */
    public void setPrinterOptions(@NotNull KeyMap items) { f.opts(Field.PRINTER, items); }

    /** Returns the value of the internal(downloader). */
    public boolean isDownloader() { return f.get(Field.DOWNLOADER, Boolean.class); }

    /** Sets the value of the internal(downloader). */
    @NotNull public MultipleTablesSync setDownloader(boolean downloader) {
        f.set(Field.DOWNLOADER, downloader);
        return (MultipleTablesSync) this;
    }

    /** Returns the value of the tabs(tabs). */
    public int getTabs() { return f.get(Field.TABS, Integer.class); }

    /** Sets the value of the tabs(tabs). */
    @NotNull public MultipleTablesSync setTabs(int tabs) {
        f.set(Field.TABS, tabs);
        return (MultipleTablesSync) this;
    }

    /** Returns the value of the display(summary). */
    @NotNull public String getSummary() { return f.get(Field.SUMMARY, String.class); }

    /** Returns a {@link FormTable<PendingsRow>} instance to handle Pendings manipulation */
    @NotNull public final FormTable<PendingsRow> getPendings() { return table(Field.PENDINGS, PendingsRow.class); }

    /** Method to allow table row class override */
    @NotNull final <T> FormTable<T> table(@NotNull FormFieldRef field, @NotNull Class<T> rowClass) { return f.table(field, rowClass, this); }

    /** Register a form Listener */
    public static void addListener(@NotNull FormListenerType listenerType, @NotNull FormListener<MultipleTablesSync> listener) { listeners.addListener(listenerType, listener); }

    /** Remove a form Listener */
    public static void removeListener(@NotNull FormListenerType listenerType, @NotNull FormListener<MultipleTablesSync> listener) { listeners.removeListener(listenerType, listener); }

    @Override @NotNull public Logger logger() { return logger; }

    /** Returns utility methods to deal with {@link Action actions}. */
    @NotNull protected final Actions actions() { return actions; }

    //~ Fields ...................................................................................................................

    @NotNull private static FormListenerMap<MultipleTablesSync> listeners = new FormListenerMap<>();
    @NotNull private static final Logger logger = Logger.getLogger(MultipleTablesSync.class);

    //~ Inner Classes ............................................................................................................

    public abstract class PendingsRowBase
        implements FormRowInstance<PendingsRow>
    {

        //~ Fields ...................................................................................................................

        @SuppressWarnings({"InstanceVariableMayNotBeInitialized", "UnusedDeclaration", "NullableProblems"}) @NotNull private UiModelAccessor f;

        //~ Methods ..................................................................................................................

        /** Returns the value of the text_field(moe). */
        @NotNull public String getMoe() { return f.get(Field.MOE, String.class); }

        /** Sets the value of the text_field(moe). */
        @NotNull public PendingsRow setMoe(@NotNull String moe) {
            f.set(Field.MOE, moe);
            return (PendingsRow) this;
        }

        /** Returns the value of the text_field(larry). */
        @NotNull public String getLarry() { return f.get(Field.LARRY, String.class); }

        /** Sets the value of the text_field(larry). */
        @NotNull public PendingsRow setLarry(@NotNull String larry) {
            f.set(Field.LARRY, larry);
            return (PendingsRow) this;
        }

        /** Returns the value of the text_field(curly). */
        @NotNull public String getCurly() { return f.get(Field.CURLY, String.class); }

        /** Sets the value of the text_field(curly). */
        @NotNull public PendingsRow setCurly(@NotNull String curly) {
            f.set(Field.CURLY, curly);
            return (PendingsRow) this;
        }

        /** Invoked when button(print) is clicked */
        @NotNull public abstract Action print();

        /** Invoked when button(download) is clicked */
        @NotNull public abstract Action export();

        /** Associates a new message to the specified field and returns it for any further configuration. */
        @NotNull public Message message(@NotNull Field field, @NotNull String msg) { return f.msg(field, msg); }

        /** Returns true if the field value is not null. */
        public boolean isDefined(@NotNull Field... fields) { return f.defined(fields); }

        /** Resets the given fields. */
        protected void reset(@NotNull Field... fields) { f.reset(fields); }

        /** Focuses given field. */
        protected void focus(@NotNull Field field) { f.focus(field); }

        /** Return associated table. */
        @NotNull public final FormTable<PendingsRow> table() { return getPendings(); }

        /** Remove row from table. */
        @NotNull public Action remove() {
            table().remove((PendingsRow) this);
            return actions().getDefault();
        }

        /** Returns a typed configuration for a given field. */
        @NotNull protected <T extends WidgetConfiguration> T configuration(@NotNull Field field) { return f.config(field); }

    }

    @SuppressWarnings("UnnecessarySemicolon")
    public enum Field
        implements tekgenesis.metadata.form.widget.FormFieldRef
    {
        ROWS("rows"),
        PRINTER("printer"),
        DOWNLOADER("downloader"),
        TABS("tabs"),
        LABEL_TAB("labelTab"),
        SUMMARY("summary"),
        PENDINGS_TAB("pendingsTab"),
        PENDINGS("pendings"),
        MOE("moe"),
        LARRY("larry"),
        CURLY("curly"),
        $H0("$H0"),
        PRINT("print"),
        DOWNLOAD("download");

        //~ Fields ...................................................................................................................

        @Nullable private final String id;

        //~ Constructors .............................................................................................................

        Field(@Nullable final String id) { this.id = id; }

        //~ Methods ..................................................................................................................

        @NotNull public String id() { return id; }

    }
}
