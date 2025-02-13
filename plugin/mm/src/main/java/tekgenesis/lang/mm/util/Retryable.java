
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.lang.mm.util;

/**
 * Possible to be tried again and stop in the process.
 */
public interface Retryable {

    //~ Methods ......................................................................................................................................

    /** True if should be tried again. */
    boolean shouldRetry();

    /** Allow no future retries. */
    void stopRetrying();
}
