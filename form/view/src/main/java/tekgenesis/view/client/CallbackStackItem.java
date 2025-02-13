
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.view.client;

import org.jetbrains.annotations.Nullable;

import tekgenesis.metadata.form.model.FormModel;
import tekgenesis.metadata.form.widget.Form;
import tekgenesis.view.shared.response.SyncFormResponse;

class CallbackStackItem {

    //~ Instance Fields ..............................................................................................................................

    private String          callback;
    private final Form      form;
    private final FormModel model;
    private final String    parameters;
    private final String    pk;

    //~ Constructors .................................................................................................................................

    CallbackStackItem(final Form form, final FormModel model, final String callback, @Nullable final String pk, @Nullable final String parameters) {
        this.form       = form;
        this.model      = model;
        this.callback   = callback;
        this.pk         = pk;
        this.parameters = parameters;
    }

    //~ Methods ......................................................................................................................................

    /** Sync with response. */
    public void sync(SyncFormResponse sync) {
        sync.syncModel(form, model);
        callback = null;
    }

    public String getParameters() {
        return parameters;
    }

    String getCallback() {
        return callback;
    }

    Form getForm() {
        return form;
    }

    FormModel getModel() {
        return model;
    }

    String getPk() {
        return pk;
    }
}  // end class CallbackStackItem
