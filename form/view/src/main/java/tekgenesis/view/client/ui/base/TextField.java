
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.view.client.ui.base;

import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.ui.TextBox;

/**
 * Extension of GWT TextBox to support non paste.
 */
public class TextField extends TextBox implements DisablePasteBox {

    //~ Instance Fields ..............................................................................................................................

    private boolean disablePaste = false;

    //~ Constructors .................................................................................................................................

    /** Creates a TextField. */
    public TextField() {
        sinkEvents(Event.ONPASTE);
    }

    //~ Methods ......................................................................................................................................

    @Override public void onBrowserEvent(Event event) {
        super.onBrowserEvent(event);

        if (event.getTypeInt() == Event.ONPASTE && disablePaste) {
            event.stopPropagation();
            event.preventDefault();
        }
    }

    /** Effectively disables paste. */
    public void setDisablePaste(boolean disablePaste) {
        this.disablePaste = disablePaste;
    }
}
