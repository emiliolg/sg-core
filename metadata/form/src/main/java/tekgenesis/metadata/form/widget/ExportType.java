
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.metadata.form.widget;

import tekgenesis.common.media.Mime;

import static tekgenesis.common.media.Mime.APPLICATION_PDF;
import static tekgenesis.common.media.Mime.TEXT_CSV;

/**
 * Export table types.
 */
public enum ExportType {

    //~ Enum constants ...............................................................................................................................

    CSV("Csv", TEXT_CSV, "CsvTableExporter", ".csv"), PDF("Pdf", APPLICATION_PDF, "PdfTableExporter", ".pdf");

    //~ Instance Fields ..............................................................................................................................

    private final String className;
    private final String extension;
    private final Mime   mime;

    private final String name;

    //~ Constructors .................................................................................................................................

    ExportType(String n, Mime m, String cn, String ext) {
        name      = n;
        mime      = m;
        className = cn;
        extension = ext;
    }

    //~ Methods ......................................................................................................................................

    /** Returns associated class name. */
    public String getClassName() {
        return "tekgenesis.form.export." + className;
    }

    /** Returns exported file extension. */
    public String getExtension() {
        return extension;
    }

    /** Returns associated Mime. */
    public Mime getMime() {
        return mime;
    }

    /** Returns name. */
    public String getName() {
        return name;
    }
}
