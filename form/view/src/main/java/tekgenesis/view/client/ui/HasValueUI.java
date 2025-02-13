
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.view.client.ui;

import com.google.gwt.event.logical.shared.ValueChangeHandler;

/**
 * Base UI interface for widget that have a value.
 */
public interface HasValueUI extends BaseWidgetUI {

    //~ Methods ......................................................................................................................................

    /** Registers a handler to listen to value changes. */
    void addChangeHandler(final ValueChangeHandler<Object> changeHandler);
}
