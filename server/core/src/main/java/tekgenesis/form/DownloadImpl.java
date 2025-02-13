
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.form;

import java.io.Serializable;

import org.jetbrains.annotations.NotNull;

import tekgenesis.common.media.Mime;

import static tekgenesis.common.media.Mime.APPLICATION_OCTET_STREAM;

/**
 * Form Download implementation.
 */
public class DownloadImpl implements Download, Serializable {

    //~ Instance Fields ..............................................................................................................................

    @NotNull private Mime   contentType;
    @NotNull private String encoding;
    @NotNull private String filename;
    private boolean         inBrowser;
    private boolean         print;

    @NotNull private final Class<? extends DownloadWriter> writer;
    private boolean                                        zipped;

    //~ Constructors .................................................................................................................................

    /** Empty constructor for serialization. */
    @SuppressWarnings("ConstantConditions")
    public DownloadImpl() {
        writer      = null;
        filename    = null;
        contentType = null;
        encoding    = null;
    }

    /** Public for tests! */
    public DownloadImpl(@NotNull final Class<? extends DownloadWriter> writer) {
        this.writer = writer;
        // noinspection DuplicateStringLiteralInspection
        filename    = "download";
        contentType = APPLICATION_OCTET_STREAM;
        encoding    = "";
    }

    //~ Methods ......................................................................................................................................

    @Override public Download inline() {
        inBrowser = true;
        return this;
    }

    @Override public Download print() {
        inBrowser = true;
        print     = true;
        return this;
    }

    @Override public Download withCharsetEncoding(@NotNull final String e) {
        encoding = e;
        return this;
    }

    @Override public Download withContentType(@NotNull final Mime m) {
        contentType = m;
        return this;
    }

    @Override public Download withFileName(@NotNull final String f) {
        filename = f;
        return this;
    }

    @Override public Download zipped() {
        zipped = true;
        return this;
    }

    /** Returns this download character encoding. */
    @NotNull public String getCharacterEncoding() {
        return encoding;
    }

    /** Returns this download content type. */
    @NotNull public Mime getContentType() {
        return contentType;
    }

    /** True if this download should be wrapped as zip. */
    public boolean isZipped() {
        return zipped;
    }

    /** Returns this download file name. */
    @NotNull public String getFilename() {
        return filename;
    }

    /** True if this download should be opened in the browser. */
    public boolean isInBrowser() {
        return inBrowser;
    }

    /** True if this download should open the browsers print dialog. */
    public boolean isPrint() {
        return print;
    }

    /** Returns this download writer. */
    @NotNull public Class<? extends DownloadWriter> getWriter() {
        return writer;
    }

    //~ Static Fields ................................................................................................................................

    private static final long serialVersionUID = -5644917735556081984L;
}  // end class DownloadImpl
