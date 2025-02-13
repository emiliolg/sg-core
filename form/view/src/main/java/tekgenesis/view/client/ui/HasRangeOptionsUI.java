
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.view.client.ui;

import org.jetbrains.annotations.NotNull;

import tekgenesis.metadata.form.widget.Widget;
import tekgenesis.model.KeyMap;

/**
 * Interface for UI widgets that have range options.
 */
public interface HasRangeOptionsUI {

    //~ Methods ......................................................................................................................................

    /** Returns the meta model. */
    Widget getModel();

    /** Sets the input handler. */
    void setRangeOptions(@NotNull final KeyMap options);
}
