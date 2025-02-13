package tekgenesis.showcase;

import tekgenesis.form.Action;
import tekgenesis.form.Actions;
import tekgenesis.form.export.CsvTableExporter;
import tekgenesis.form.exception.FormCannotBePopulatedException;
import tekgenesis.metadata.form.widget.FormFieldRef;
import tekgenesis.form.FormInstance;
import tekgenesis.form.extension.FormListener;
import tekgenesis.form.extension.FormListenerMap;
import tekgenesis.form.extension.FormListenerType;
import tekgenesis.form.FormRowInstance;
import tekgenesis.form.FormTable;
import tekgenesis.form.export.FormTableExporter;
import tekgenesis.form.export.FormTableExporter.FormTableHeaderExporter;
import tekgenesis.form.export.FormTableExporter.FormTableRowExporter;
import tekgenesis.showcase.ValidateGroupForm.HorTableRow;
import java.io.IOException;
import tekgenesis.common.logging.LoggableInstance;
import tekgenesis.common.logging.Logger;
import tekgenesis.form.Message;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import java.io.OutputStream;
import tekgenesis.form.UiModelAccessor;
import tekgenesis.form.configuration.WidgetConfiguration;
import static tekgenesis.form.Download.DownloadWriter;
import static tekgenesis.common.media.Mime.TEXT_CSV;

/** 
 * Generated base class for form: ValidateGroupForm.
 * Don't modify this as this is an auto generated class that's gets generated
 * every time the meta model file is modified. Use subclass instead.
 */
@SuppressWarnings({"DuplicateStringLiteralInspection", "WeakerAccess", "FieldMayBeFinal", "FieldCanBeLocal", "ConstantConditions", "ClassWithTooManyMethods", "ClassWithTooManyFields", "MagicNumber", "MethodOverridesStaticMethodOfSuperclass", "ParameterHidesMemberVariable", "FieldNameHidesFieldInSuperclass", "OverlyComplexClass", "RedundantCast", "UnusedReturnValue", "LocalVariableHidesMemberVariable", "UnusedParameters", "EmptyMethod"})
public abstract class ValidateGroupFormBase
    extends FormInstance<Void>
    implements LoggableInstance
{

    //~ Fields ...................................................................................................................

    @SuppressWarnings({"InstanceVariableMayNotBeInitialized", "UnusedDeclaration", "NullableProblems"}) @NotNull private UiModelAccessor f;

    //~ Methods ..................................................................................................................

    /** Invoked when populating a form instance */
    @NotNull public final Void populate() {
        throw new FormCannotBePopulatedException("tekgenesis.showcase.ValidateGroupForm");
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

    /** Returns the value of the text_field(horInt). */
    public int getHorInt() { return f.get(Field.HOR_INT, Integer.class); }

    /** Sets the value of the text_field(horInt). */
    @NotNull public ValidateGroupForm setHorInt(int horInt) {
        f.set(Field.HOR_INT, horInt);
        return (ValidateGroupForm) this;
    }

    /** Returns the value of the mail_field(mail). */
    @Nullable public String getMail() { return f.get(Field.MAIL, String.class); }

    /** Sets the value of the mail_field(mail). */
    @NotNull public ValidateGroupForm setMail(@Nullable String mail) {
        f.set(Field.MAIL, mail);
        return (ValidateGroupForm) this;
    }

    /** Sets the options of the mail_field(mail). */
    public void setMailOptions(@NotNull Iterable<String> items) { f.opts(Field.MAIL, items); }

    /** Returns the value of the text_field(horVertInt). */
    public int getHorVertInt() { return f.get(Field.HOR_VERT_INT, Integer.class); }

    /** Sets the value of the text_field(horVertInt). */
    @NotNull public ValidateGroupForm setHorVertInt(int horVertInt) {
        f.set(Field.HOR_VERT_INT, horVertInt);
        return (ValidateGroupForm) this;
    }

    /** Returns a {@link FormTable<HorTableRow>} instance to handle HorTable manipulation */
    @NotNull public final FormTable<HorTableRow> getHorTable() { return table(Field.HOR_TABLE, HorTableRow.class); }

    /** Method to allow table row class override */
    @NotNull final <T> FormTable<T> table(@NotNull FormFieldRef field, @NotNull Class<T> rowClass) { return f.table(field, rowClass, this); }

    /** Called on exporting horTable. */
    @NotNull public Action exportCsvHorTable() {
        final Action result = actions().getDefault();
        result.withDownload(HorTableCsvDump.class).withFileName("exported-horTable.csv").withContentType(TEXT_CSV);
        return result;
    }

    /** Returns the value of the text_field(verInt). */
    public int getVerInt() { return f.get(Field.VER_INT, Integer.class); }

    /** Sets the value of the text_field(verInt). */
    @NotNull public ValidateGroupForm setVerInt(int verInt) {
        f.set(Field.VER_INT, verInt);
        return (ValidateGroupForm) this;
    }

    /** Returns the value of the text_field(footInt). */
    public int getFootInt() { return f.get(Field.FOOT_INT, Integer.class); }

    /** Sets the value of the text_field(footInt). */
    @NotNull public ValidateGroupForm setFootInt(int footInt) {
        f.set(Field.FOOT_INT, footInt);
        return (ValidateGroupForm) this;
    }

    /** Returns the value of the dialog(dialog). */
    public boolean isDialog() { return f.get(Field.DIALOG, Boolean.class); }

    /** Sets the value of the dialog(dialog). */
    @NotNull public ValidateGroupForm setDialog(boolean dialog) {
        f.set(Field.DIALOG, dialog);
        return (ValidateGroupForm) this;
    }

    /** Returns the value of the text_field(dialogInt). */
    public int getDialogInt() { return f.get(Field.DIALOG_INT, Integer.class); }

    /** Sets the value of the text_field(dialogInt). */
    @NotNull public ValidateGroupForm setDialogInt(int dialogInt) {
        f.set(Field.DIALOG_INT, dialogInt);
        return (ValidateGroupForm) this;
    }

    /** 
     * Invoked when button(horButt) is clicked
     * Invoked when button(verButt) is clicked
     * Invoked when button(footButt) is clicked
     * Invoked when button(dialogButt) is clicked
     * Invoked when button(allButt) is clicked
     */
    @NotNull public abstract Action getDefault();

    /** Register a form Listener */
    public static void addListener(@NotNull FormListenerType listenerType, @NotNull FormListener<ValidateGroupForm> listener) { listeners.addListener(listenerType, listener); }

    /** Remove a form Listener */
    public static void removeListener(@NotNull FormListenerType listenerType, @NotNull FormListener<ValidateGroupForm> listener) { listeners.removeListener(listenerType, listener); }

    @Override @NotNull public Logger logger() { return logger; }

    /** Returns utility methods to deal with {@link Action actions}. */
    @NotNull protected final Actions actions() { return actions; }

    //~ Fields ...................................................................................................................

    @NotNull private static FormListenerMap<ValidateGroupForm> listeners = new FormListenerMap<>();
    @NotNull private static final Logger logger = Logger.getLogger(ValidateGroupForm.class);

    //~ Inner Classes ............................................................................................................

    public abstract class HorTableRowBase
        implements FormRowInstance<HorTableRow>
    {

        //~ Fields ...................................................................................................................

        @SuppressWarnings({"InstanceVariableMayNotBeInitialized", "UnusedDeclaration", "NullableProblems"}) @NotNull private UiModelAccessor f;

        //~ Methods ..................................................................................................................

        /** Returns the value of the text_field(horTableInt). */
        public int getHorTableInt() { return f.get(Field.HOR_TABLE_INT, Integer.class); }

        /** Sets the value of the text_field(horTableInt). */
        @NotNull public HorTableRow setHorTableInt(int horTableInt) {
            f.set(Field.HOR_TABLE_INT, horTableInt);
            return (HorTableRow) this;
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
        @NotNull public final FormTable<HorTableRow> table() { return getHorTable(); }

        /** Remove row from table. */
        @NotNull public Action remove() {
            table().remove((HorTableRow) this);
            return actions().getDefault();
        }

        /** Returns a typed configuration for a given field. */
        @NotNull protected <T extends WidgetConfiguration> T configuration(@NotNull Field field) { return f.config(field); }

    }

    public class HorTableCsvDump
        implements DownloadWriter
    {

        //~ Methods ..................................................................................................................

        @Override public void into(@Nullable OutputStream stream) throws IOException {
            final FormTableExporter exporter = new CsvTableExporter(stream);

            exporter.title(label(Field.HOR_TABLE));

            writeHorTableHeader(exporter.header());

            for (final HorTableRow row : getHorTableRows()) {
            	writeHorTableRow(exporter.row(), row);
            }

            exporter.build();
            stream.flush();
        }

        @NotNull protected Iterable<HorTableRow> getHorTableRows() { return getHorTable(); }

        protected void writeHorTableHeader(@NotNull final FormTableHeaderExporter e) { e.addContent(label(Field.HOR_TABLE_INT)); }

        protected void writeHorTableRow(@NotNull final FormTableRowExporter e, @NotNull final HorTableRow row) { e.addContent(row.getHorTableInt()); }

    }

    @SuppressWarnings("UnnecessarySemicolon")
    public enum Field
        implements tekgenesis.metadata.form.widget.FormFieldRef
    {
        HOR("hor"),
        HOR_INT("horInt"),
        HOR_VERT("horVert"),
        MAIL("mail"),
        HOR_VERT_INT("horVertInt"),
        HOR_TABLE("horTable"),
        HOR_TABLE_INT("horTableInt"),
        ADD_BUTTON("addButton"),
        REMOVE_BUTTON("removeButton"),
        EXPORT_BUTTON("exportButton"),
        VER("ver"),
        VER_INT("verInt"),
        FOOTER("footer"),
        FOOT_INT("footInt"),
        DIALOG("dialog"),
        DIALOG_INT("dialogInt"),
        HOR_BUTT("horButt"),
        VER_BUTT("verButt"),
        FOOT_BUTT("footButt"),
        DIALOG_BUTT("dialogButt"),
        ALL_BUTT("allButt");

        //~ Fields ...................................................................................................................

        @Nullable private final String id;

        //~ Constructors .............................................................................................................

        Field(@Nullable final String id) { this.id = id; }

        //~ Methods ..................................................................................................................

        @NotNull public String id() { return id; }

    }
}
