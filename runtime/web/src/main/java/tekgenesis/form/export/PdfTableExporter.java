
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.form.export;

import java.io.IOException;
import java.io.OutputStream;

import org.jetbrains.annotations.NotNull;

import tekgenesis.report.fo.FopBuildException;
import tekgenesis.report.fo.FopBuilder;
import tekgenesis.report.fo.components.BlockB;
import tekgenesis.report.fo.components.TableB;
import tekgenesis.report.fo.components.TableRowB;
import tekgenesis.report.fo.document.PageSequenceBuilder;
import tekgenesis.report.fo.document.SimpleDocument;
import tekgenesis.report.fo.document.SimplePageMasterBuilder;

import static tekgenesis.report.fo.components.BlockB.block;
import static tekgenesis.report.fo.components.TableB.table;
import static tekgenesis.report.fo.components.TableCellB.tableCell;
import static tekgenesis.report.fo.components.TableColumnB.tableColumn;
import static tekgenesis.report.fo.components.TableRowB.tableRow;
import static tekgenesis.report.fo.document.PageSequenceBuilder.pageSequence;
import static tekgenesis.report.fo.document.SimpleDocumentBuilder.simpleDocument;
import static tekgenesis.report.fo.document.SimplePageMasterBuilder.simplePageMaster;

/**
 * Exports a form table to PDF.
 */
public class PdfTableExporter implements FormTableExporter {

    //~ Instance Fields ..............................................................................................................................

    private TableRowB          currentRow = null;
    private final TableRowB    headerRow;
    private BlockB             mainHeader = null;
    private final OutputStream out;
    private final TableB       table;

    //~ Constructors .................................................................................................................................

    /** Creates a Pdf Table Exporter. */
    public PdfTableExporter(@NotNull final OutputStream stream) {
        out = stream;

        headerRow = tableRow();
        table     = table();
        table.rows(headerRow);
    }

    //~ Methods ......................................................................................................................................

    public void build() {
        final PageSequenceBuilder pageSequence = pageSequence();
        pageSequence.withName(PDF_CONTENT).addChildren(mainHeader, table);

        final SimplePageMasterBuilder pageMaster = simplePageMaster();
        pageMaster.masterName(PDF_CONTENT).marginBottom("10mm").marginLeft("10mm").marginRight("10mm").marginTop("10mm");

        final SimpleDocument document = simpleDocument().withPageSequence(pageSequence).withSimplePageMaster(pageMaster).build();

        try {
            FopBuilder.build(document, out);
        }
        catch (final FopBuildException e) {
            throw new RuntimeException("Could not build PDF");
        }
        finally {
            try {
                out.close();
            }
            catch (final IOException ignore) {}
        }
    }

    @Override public FormTableHeaderExporter header() {
        return new PdfTableHeaderExporter(table, headerRow);
    }

    public FormTableRowExporter row() {
        currentRow = tableRow().borderTop("1px solid #4B4B4B");
        table.rows(currentRow);
        return new PdfTableRowExporter(currentRow, this);
    }

    @Override public void title(@NotNull String title) {
        mainHeader = block();
        mainHeader.content(title).marginBottom("20px").size("30px").family("Helvetica");
    }

    //~ Static Fields ................................................................................................................................

    private static final String PDF_CONTENT = "pdf-content";

    //~ Inner Classes ................................................................................................................................

    private static class PdfTableHeaderExporter implements FormTableHeaderExporter {
        private final TableRowB headerRow;
        private final TableB    table;

        public PdfTableHeaderExporter(@NotNull final TableB t, @NotNull final TableRowB h) {
            table     = t;
            headerRow = h;
        }

        @Override public FormTableHeaderExporter addContent(@NotNull String label) {
            table.columns(tableColumn());
            headerRow.children(tableCell().child(block().content(label)).bold());
            return this;
        }
    }

    private static class PdfTableRowExporter implements FormTableRowExporter {
        private final PdfTableExporter exporter;

        private final TableRowB row;

        public PdfTableRowExporter(TableRowB row, PdfTableExporter exporter) {
            this.row      = row;
            this.exporter = exporter;
        }

        @Override public FormTableRowExporter addContent(@NotNull String content) {
            if (exporter.currentRow != row) throw new IllegalStateException(CURRENT_ROW_WAS_CLOSED);
            row.children(tableCell().child(block().content(content)).padding("5px"));
            return this;
        }
    }
}  // end class PdfTableExporter
