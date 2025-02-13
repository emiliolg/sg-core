
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.database.support;

import java.util.LinkedList;
import java.util.List;

/**
 * A Dummy connection that invoke a listener when it is closed.
 */
public class NotifyOnCloseConnection extends DummyConnection {

    //~ Instance Fields ..............................................................................................................................

    private final List<Runnable> listeners = new LinkedList<>();

    //~ Methods ......................................................................................................................................

    /** Add a listener. */
    public void addListener(Runnable runOnClose) {
        listeners.add(runOnClose);
    }

    @Override public void close() {
        listeners.forEach(Runnable::run);
    }
}
