
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.admin.status;

/**
 * Memory Info.
 */
public class MemoryInfo {

    //~ Instance Fields ..............................................................................................................................

    private final long free;

    private final long max;
    private final long total;

    //~ Constructors .................................................................................................................................

    /** Create a MemoryInfo with current system memory info. */
    public MemoryInfo() {
        max   = Runtime.getRuntime().maxMemory();
        free  = Runtime.getRuntime().freeMemory();
        total = Runtime.getRuntime().totalMemory();
    }

    //~ Methods ......................................................................................................................................

    /** Free amount of memory. */
    public long getFree() {
        return free;
    }

    /** Max amount of memory. */
    public long getMax() {
        return max;
    }

    /** Total amount of memory. */
    public long getTotal() {
        return total;
    }
}
