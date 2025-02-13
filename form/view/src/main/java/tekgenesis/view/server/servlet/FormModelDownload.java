
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.view.server.servlet;

import java.io.Serializable;

import org.jetbrains.annotations.NotNull;

import tekgenesis.form.DownloadImpl;
import tekgenesis.metadata.form.model.FormModel;
import tekgenesis.metadata.form.widget.Form;

/**
 * Serialize object containing FormModel and Download data.
 */
public class FormModelDownload implements Serializable {

    //~ Instance Fields ..............................................................................................................................

    private DownloadImpl download = null;
    private FormModel    model    = null;

    //~ Constructors .................................................................................................................................

    /** Empty constructor for serialization. */
    public FormModelDownload() {}

    /** Returns a new FormModelDownload. */
    public FormModelDownload(FormModel model, DownloadImpl download) {
        this.download = download;
        this.model    = model;
    }

    //~ Methods ......................................................................................................................................

    /** Inits the form model given a form metadata. */
    public FormModel init(@NotNull final Form form) {
        return model.init(form);
    }

    /** Get download data. */
    public DownloadImpl getDownload() {
        return download;
    }

    //~ Static Fields ................................................................................................................................

    private static final long serialVersionUID = 2026977871431012014L;
}
