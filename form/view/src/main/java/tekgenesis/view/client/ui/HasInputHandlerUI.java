
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.view.client.ui;

import tekgenesis.view.client.formatter.InputHandler;

/**
 * Interface for UI widgets that have an input handler.
 */
public interface HasInputHandlerUI {

    //~ Methods ......................................................................................................................................

    /** Sets the input handler. */
    void setInputHandler(InputHandler<?> inputHandler);
}
