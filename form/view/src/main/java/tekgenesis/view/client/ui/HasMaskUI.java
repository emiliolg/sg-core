
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

public interface HasMaskUI {

    //~ Methods ......................................................................................................................................

    /** Set input handler. */
    void setInputHandler(InputHandler<?> inputHandler);

    /** Set input length. */
    void setLength(int length, boolean expand);

    /** Set placeholder. */
    void setPlaceholder(String inputMask);

    /** Get model value. */
    Object getValue();
}
