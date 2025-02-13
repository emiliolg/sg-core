
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.cluster.jmx.util;

import javax.management.openmbean.CompositeData;

import org.jetbrains.annotations.NotNull;

import static tekgenesis.cluster.jmx.util.MemoryUtils.calculateMemoryUsage;
import static tekgenesis.cluster.jmx.util.MemoryUtils.getMemorySummary;

/**
 * Memory Summary.
 */
public final class MemorySummary {

    //~ Instance Fields ..............................................................................................................................

    private final Long max;
    private final Long used;

    //~ Constructors .................................................................................................................................

    /** Default constructor. */
    public MemorySummary(@NotNull CompositeData memory) {
        max  = (Long) memory.get("max");
        used = (Long) memory.get("used");
    }

    /**
     * @param  max   Max memory
     * @param  used  used memory
     */
    public MemorySummary(@NotNull Long max, @NotNull Long used) {
        this.max  = max;
        this.used = used;
    }

    //~ Methods ......................................................................................................................................

    /** @return  An string representation of the memory summary */
    @Override public String toString() {
        return getMemorySummary(max, used);
    }

    /** @return  the memory usage in percentage */
    public double getMemoryUsage() {
        return calculateMemoryUsage(max, used);
    }

    /** @return  The total memory in bytes */
    public long getTotalMemory() {
        return max;
    }

    /** @return  The used memory in bytes */
    public long getUsedMemory() {
        return used;
    }
}  // end class MemorySummary
