
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.form.export;

import java.io.OutputStream;

import org.jetbrains.annotations.NotNull;

import tekgenesis.common.io.CsvOutput;

/**
 * Exports a form table to CSV.
 */
public class CsvTableExporter implements FormTableExporter {

    //~ Instance Fields ..............................................................................................................................

    private final CsvOutput csv;
    private int             rows = 0;

    //~ Constructors .................................................................................................................................

    /** Creates a Csv Table Exporter. */
    public CsvTableExporter(OutputStream stream) {
        csv = new CsvOutput(stream);
    }

    //~ Methods ......................................................................................................................................

    @Override public void build() {
        csv.writeLine();
        csv.close();
    }

    @Override public FormTableHeaderExporter header() {
        return new CsvTableHeaderExporter(csv);
    }

    @Override public FormTableRowExporter row() {
        rows++;
        return new CsvTableRowExporter(rows, csv, this);
    }

    @Override public void title(@NotNull final String label) {}

    //~ Inner Classes ................................................................................................................................

    private static class CsvTableHeaderExporter implements FormTableHeaderExporter {
        private final CsvOutput csv;

        public CsvTableHeaderExporter(@NotNull final CsvOutput c) {
            csv = c;
        }

        @Override public FormTableHeaderExporter addContent(@NotNull String label) {
            csv.outField(label);
            return this;
        }
    }

    private static class CsvTableRowExporter implements FormTableRowExporter {
        private final CsvOutput        csv;
        private final CsvTableExporter exporter;

        private final int rows;

        public CsvTableRowExporter(final int rs, @NotNull final CsvOutput c, @NotNull final CsvTableExporter e) {
            rows     = rs;
            csv      = c;
            exporter = e;
            c.writeLine();
        }

        @Override public FormTableRowExporter addContent(@NotNull String content) {
            if (exporter.rows != rows) throw new IllegalStateException(CURRENT_ROW_WAS_CLOSED);
            csv.outField(content);
            return this;
        }
    }
}  // end class CsvTableExporter
