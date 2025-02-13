
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.view.client.event;

import com.google.gwt.dom.client.Document;
import com.google.gwt.dom.client.NativeEvent;
import com.google.gwt.event.dom.client.DomEvent;
import com.google.gwt.event.shared.HasHandlers;

/**
 * Util class for client side events.
 */
public class Events {

    //~ Constructors .................................................................................................................................

    private Events() {}

    //~ Methods ......................................................................................................................................

    /** Fire native client side click event. */
    public static void fireNativeClickEvent(HasHandlers link) {
        final NativeEvent event = Document.get().createClickEvent(0, 0, 0, 0, 0, false, false, false, false);
        DomEvent.fireNativeEvent(event, link);
    }
}
