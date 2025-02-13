
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.view.client.ui;

import org.jetbrains.annotations.Nullable;

/**
 * UI interface for scalar valued widgets.
 */
public interface HasScalarValueUI extends HasValueUI {

    //~ Methods ......................................................................................................................................

    /** Returns the value. */
    @Nullable Object getValue();

    /** Sets the value. */
    void setValue(@Nullable final Object value);

    /** Sets the value and fire the event. JUST FOR TESTING */
    void setValue(@Nullable final Object value, boolean fireEvents);
}
