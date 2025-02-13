
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.form;

import java.io.IOException;
import java.io.OutputStream;

import org.jetbrains.annotations.NotNull;

import tekgenesis.common.media.Mime;

/**
 * A form download stream.
 */
@SuppressWarnings("UnusedReturnValue")  // chaining...
public interface Download {

    //~ Methods ......................................................................................................................................

    /** Attempt to open file rather than save. */
    Download inline();

    /** Attempt to print the file. */
    Download print();

    /** Adds the download charset encoding. */
    Download withCharsetEncoding(@NotNull String e);

    /** Adds the download content type. */
    Download withContentType(@NotNull final Mime mime);

    /** Adds the download filename. */
    Download withFileName(@NotNull final String message);

    /** Enclose stream as zip. */
    Download zipped();

    //~ Inner Interfaces .............................................................................................................................

    /**
     * Download writer.
     */
    interface DownloadWriter {
        /** Write download into given stream. */
        void into(@NotNull final OutputStream stream)
            throws IOException;
    }
}
