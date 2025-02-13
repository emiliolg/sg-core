
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

import static tekgenesis.cluster.jmx.util.Format.toSizeString;

/**
 * Memory utils.
 */
public final class MemoryUtils {

    //~ Constructors .................................................................................................................................

    private MemoryUtils() {}

    //~ Methods ......................................................................................................................................

    /**
     * @param   heapMemoryUsage  CompositeData
     *
     * @return  The memory usage in %
     */
    public static double calculateMemoryUsage(@NotNull final CompositeData heapMemoryUsage) {
        return calculateMemoryUsage((Long) heapMemoryUsage.get("max"), (Long) heapMemoryUsage.get("used"));
    }

    /**
     * @param   max   max memory
     * @param   used  used memory
     *
     * @return  The memory usage in %
     */
    public static double calculateMemoryUsage(@NotNull final Long max, @NotNull final Long used) {
        return used * 100 / max;
    }

    /**
     * @param   max   max memory
     * @param   used  used memory
     *
     * @return  Returns a Memory summary used/max
     */
    public static String getMemorySummary(@NotNull Long max, @NotNull Long used) {
        return toSizeString(used) + " / " + toSizeString(max);
    }
}  // end class MemoryUtils
