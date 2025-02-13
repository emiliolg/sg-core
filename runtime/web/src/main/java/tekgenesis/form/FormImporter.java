
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
import java.io.InputStream;

import javax.servlet.http.HttpServletRequest;

import org.jetbrains.annotations.NotNull;

/**
 * Exports form data into different formats.
 */
public interface FormImporter extends FormEtl<FormImporter> {

    //~ Methods ......................................................................................................................................

    /** Load form fields from the specified file. */
    void from(@NotNull final File file)
        throws IOException;

    /** Load form fields from the specified stream. Given stream is closed. */
    void from(@NotNull final InputStream stream)
        throws IOException;

    /** Load form fields from the request stream. Given stream is closed. */
    void from(@NotNull final HttpServletRequest req)
        throws IOException;
}
