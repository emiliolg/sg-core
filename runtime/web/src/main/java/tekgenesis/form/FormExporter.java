
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.form;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.jetbrains.annotations.NotNull;

/**
 * Exports form data into different formats.
 */
public interface FormExporter extends FormEtl<FormExporter> {

    //~ Methods ......................................................................................................................................

    /** Specified form fields. */
    FormExporter fields(@NotNull Enum<?>... fields);

    /** Specified form fields. */
    FormExporter fields(@NotNull String... fs);

    /** Specified form fields from request header. */
    FormExporter fields(@NotNull HttpServletRequest req);

    /** Dump form fields into response writer. Response writer is not closed. */
    void into(@NotNull final HttpServletResponse resp)
        throws IOException;

    /** Dump form fields into the specified file. */
    void into(@NotNull final File file)
        throws IOException;

    /** Dump form fields into the specified stream. Given stream is not closed. */
    void into(@NotNull final OutputStream stream)
        throws IOException;
}
