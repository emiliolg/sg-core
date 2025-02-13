package tekgenesis.showcase;

import tekgenesis.form.Action;
import tekgenesis.form.Actions;
import tekgenesis.form.export.CsvTableExporter;
import tekgenesis.common.core.DateOnly;
import tekgenesis.common.core.DateTime;
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
import java.io.IOException;
import tekgenesis.common.logging.LoggableInstance;
import tekgenesis.common.logging.Logger;
import tekgenesis.form.Message;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import tekgenesis.showcase.ExportTable.OrdersRow;
import java.io.OutputStream;
import tekgenesis.form.export.PdfTableExporter;
import tekgenesis.form.UiModelAccessor;
import tekgenesis.form.configuration.WidgetConfiguration;
import static tekgenesis.common.media.Mime.APPLICATION_PDF;
import static tekgenesis.form.Download.DownloadWriter;
import static tekgenesis.common.media.Mime.TEXT_CSV;

/** 
 * Generated base class for form: ExportTable.
 * Don't modify this as this is an auto generated class that's gets generated
 * every time the meta model file is modified. Use subclass instead.
 */
@SuppressWarnings({"DuplicateStringLiteralInspection", "WeakerAccess", "FieldMayBeFinal", "FieldCanBeLocal", "ConstantConditions", "ClassWithTooManyMethods", "ClassWithTooManyFields", "MagicNumber", "MethodOverridesStaticMethodOfSuperclass", "ParameterHidesMemberVariable", "FieldNameHidesFieldInSuperclass", "OverlyComplexClass", "RedundantCast", "UnusedReturnValue", "LocalVariableHidesMemberVariable", "UnusedParameters", "EmptyMethod"})
public abstract class ExportTableBase
    extends FormInstance<Void>
    implements LoggableInstance
{

    //~ Fields ...................................................................................................................

    @SuppressWarnings({"InstanceVariableMayNotBeInitialized", "UnusedDeclaration", "NullableProblems"}) @NotNull private UiModelAccessor f;

    //~ Methods ..................................................................................................................

    /** Invoked when populating a form instance */
    @NotNull public final Void populate() {
        throw new FormCannotBePopulatedException("tekgenesis.showcase.ExportTable");
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

    /** Returns a {@link FormTable<OrdersRow>} instance to handle Orders manipulation */
    @NotNull public final FormTable<OrdersRow> getOrders() { return table(Field.ORDERS, OrdersRow.class); }

    /** Method to allow table row class override */
    @NotNull final <T> FormTable<T> table(@NotNull FormFieldRef field, @NotNull Class<T> rowClass) { return f.table(field, rowClass, this); }

    /** Called on exporting orders. */
    @NotNull public Action exportPdfOrders() {
        final Action result = actions().getDefault();
        result.withDownload(OrdersPdfDump.class).withFileName("exported-orders.pdf").withContentType(APPLICATION_PDF);
        return result;
    }

    /** Called on exporting orders. */
    @NotNull public Action exportCsvOrders() {
        final Action result = actions().getDefault();
        result.withDownload(OrdersCsvDump.class).withFileName("exported-orders.csv").withContentType(TEXT_CSV);
        return result;
    }

    /** Called on exporting orders. */
    @NotNull public Action inline() {
        final Action result = actions().getDefault();
        result.withDownload(OrdersPdfDump.class).withFileName("exported-orders.pdf").withContentType(APPLICATION_PDF);
        return result;
    }

    /** Called on exporting orders. */
    @NotNull public Action print() {
        final Action result = actions().getDefault();
        result.withDownload(OrdersPdfDump.class).withFileName("exported-orders.pdf").withContentType(APPLICATION_PDF);
        return result;
    }

    /** Register a form Listener */
    public static void addListener(@NotNull FormListenerType listenerType, @NotNull FormListener<ExportTable> listener) { listeners.addListener(listenerType, listener); }

    /** Remove a form Listener */
    public static void removeListener(@NotNull FormListenerType listenerType, @NotNull FormListener<ExportTable> listener) { listeners.removeListener(listenerType, listener); }

    @Override @NotNull public Logger logger() { return logger; }

    /** Returns utility methods to deal with {@link Action actions}. */
    @NotNull protected final Actions actions() { return actions; }

    //~ Fields ...................................................................................................................

    @NotNull private static FormListenerMap<ExportTable> listeners = new FormListenerMap<>();
    @NotNull private static final Logger logger = Logger.getLogger(ExportTable.class);

    //~ Inner Classes ............................................................................................................

    public abstract class OrdersRowBase
        implements FormRowInstance<OrdersRow>
    {

        //~ Fields ...................................................................................................................

        @SuppressWarnings({"InstanceVariableMayNotBeInitialized", "UnusedDeclaration", "NullableProblems"}) @NotNull private UiModelAccessor f;

        //~ Methods ..................................................................................................................

        /** Returns the value of the display(strCol). */
        @NotNull public String getStrCol() { return f.get(Field.STR_COL, String.class); }

        /** Sets the value of the display(strCol). */
        @NotNull public OrdersRow setStrCol(@NotNull String strCol) {
            f.set(Field.STR_COL, strCol);
            return (OrdersRow) this;
        }

        /** Returns the value of the display(intCol). */
        public int getIntCol() { return f.get(Field.INT_COL, Integer.class); }

        /** Sets the value of the display(intCol). */
        @NotNull public OrdersRow setIntCol(int intCol) {
            f.set(Field.INT_COL, intCol);
            return (OrdersRow) this;
        }

        /** Returns the value of the display(dateCol). */
        @NotNull public DateOnly getDateCol() {
            return DateOnly.fromMilliseconds(f.get(Field.DATE_COL, Long.class));
        }

        /** Sets the value of the display(dateCol). */
        @NotNull public OrdersRow setDateCol(@NotNull DateOnly dateCol) {
            f.set(Field.DATE_COL, dateCol);
            return (OrdersRow) this;
        }

        /** Returns the value of the display(dateTimeCol). */
        @NotNull public DateTime getDateTimeCol() {
            return DateTime.fromMilliseconds(f.get(Field.DATE_TIME_COL, Long.class));
        }

        /** Sets the value of the display(dateTimeCol). */
        @NotNull public OrdersRow setDateTimeCol(@NotNull DateTime dateTimeCol) {
            f.set(Field.DATE_TIME_COL, dateTimeCol);
            return (OrdersRow) this;
        }

        /** Invoked when button(iconInTable) is clicked */
        @NotNull public abstract Action clicked();

        /** Associates a new message to the specified field and returns it for any further configuration. */
        @NotNull public Message message(@NotNull Field field, @NotNull String msg) { return f.msg(field, msg); }

        /** Returns true if the field value is not null. */
        public boolean isDefined(@NotNull Field... fields) { return f.defined(fields); }

        /** Resets the given fields. */
        protected void reset(@NotNull Field... fields) { f.reset(fields); }

        /** Focuses given field. */
        protected void focus(@NotNull Field field) { f.focus(field); }

        /** Return associated table. */
        @NotNull public final FormTable<OrdersRow> table() { return getOrders(); }

        /** Remove row from table. */
        @NotNull public Action remove() {
            table().remove((OrdersRow) this);
            return actions().getDefault();
        }

        /** Returns a typed configuration for a given field. */
        @NotNull protected <T extends WidgetConfiguration> T configuration(@NotNull Field field) { return f.config(field); }

    }

    public class OrdersPdfDump
        implements DownloadWriter
    {

        //~ Methods ..................................................................................................................

        @Override public void into(@Nullable OutputStream stream) throws IOException {
            final FormTableExporter exporter = new PdfTableExporter(stream);

            exporter.title(label(Field.ORDERS));

            writeOrdersHeader(exporter.header());

            for (final OrdersRow row : getOrdersRows()) {
            	writeOrdersRow(exporter.row(), row);
            }

            exporter.build();
            stream.flush();
        }

        @NotNull protected Iterable<OrdersRow> getOrdersRows() { return getOrders(); }

        protected void writeOrdersHeader(@NotNull final FormTableHeaderExporter e) {
            e.addContent(label(Field.STR_COL));
            e.addContent(label(Field.INT_COL));
            e.addContent(label(Field.DATE_COL));
            e.addContent(label(Field.DATE_TIME_COL));
        }

        protected void writeOrdersRow(@NotNull final FormTableRowExporter e, @NotNull final OrdersRow row) {
            e.addContent(row.getStrCol());
            e.addContent(row.getIntCol());
            e.addContent(row.getDateCol());
            e.addContent(row.getDateTimeCol());
        }

    }

    public class OrdersCsvDump
        implements DownloadWriter
    {

        //~ Methods ..................................................................................................................

        @Override public void into(@Nullable OutputStream stream) throws IOException {
            final FormTableExporter exporter = new CsvTableExporter(stream);

            exporter.title(label(Field.ORDERS));

            writeOrdersHeader(exporter.header());

            for (final OrdersRow row : getOrdersRows()) {
            	writeOrdersRow(exporter.row(), row);
            }

            exporter.build();
            stream.flush();
        }

        @NotNull protected Iterable<OrdersRow> getOrdersRows() { return getOrders(); }

        protected void writeOrdersHeader(@NotNull final FormTableHeaderExporter e) {
            e.addContent(label(Field.STR_COL));
            e.addContent(label(Field.INT_COL));
            e.addContent(label(Field.DATE_COL));
            e.addContent(label(Field.DATE_TIME_COL));
        }

        protected void writeOrdersRow(@NotNull final FormTableRowExporter e, @NotNull final OrdersRow row) {
            e.addContent(row.getStrCol());
            e.addContent(row.getIntCol());
            e.addContent(row.getDateCol());
            e.addContent(row.getDateTimeCol());
        }

    }

    @SuppressWarnings("UnnecessarySemicolon")
    public enum Field
        implements tekgenesis.metadata.form.widget.FormFieldRef
    {
        $H0("$H0"),
        $M1("$M1"),
        VIEW_SOURCE("viewSource"),
        ORDERS("orders"),
        STR_COL("strCol"),
        INT_COL("intCol"),
        DATE_COL("dateCol"),
        DATE_TIME_COL("dateTimeCol"),
        REMOVE_IN_ROW("removeInRow"),
        ICON_IN_TABLE("iconInTable"),
        $H2("$H2"),
        DOWNLOAD_PDF("downloadPdf"),
        DOWNLOAD_CSV("downloadCsv"),
        INLINE("inline"),
        PRINT("print");

        //~ Fields ...................................................................................................................

        @Nullable private final String id;

        //~ Constructors .............................................................................................................

        Field(@Nullable final String id) { this.id = id; }

        //~ Methods ..................................................................................................................

        @NotNull public String id() { return id; }

    }
}
