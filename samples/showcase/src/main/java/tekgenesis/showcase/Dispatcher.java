
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.showcase;

import org.jetbrains.annotations.NotNull;

/**
 * User class for Form: Dispatcher
 */
public class Dispatcher extends DispatcherBase {

    //~ Methods ......................................................................................................................................

    /** Invoked when populating a form instance. */
    @NotNull @Override public Object populate() {
        return getName();
    }
}
