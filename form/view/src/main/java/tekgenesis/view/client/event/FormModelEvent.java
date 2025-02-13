
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.view.client.event;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;

import tekgenesis.metadata.form.model.FormModel;

/**
 * Event that holds a FormModel.
 */
public abstract class FormModelEvent<H extends EventHandler> extends GwtEvent<H> {

    //~ Instance Fields ..............................................................................................................................

    private final FormModel model;

    //~ Constructors .................................................................................................................................

    /** Creates save event with a valid model. */
    FormModelEvent(final FormModel model) {
        this.model = model;
    }

    //~ Methods ......................................................................................................................................

    /** Returns the event hold model. */
    public FormModel getModel() {
        return model;
    }
}
