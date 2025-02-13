
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.metric.core;

/**
 * Metric.
 */
public interface Metric extends AutoCloseable {

    //~ Methods ......................................................................................................................................

    /** Stop metric collector. */
    default void close() {
        stop();
    }

    /** Start metric collector. */
    default Metric start() {
        return this;
    }

    /** Stop metric collector. */
    default void stop() {}
}
