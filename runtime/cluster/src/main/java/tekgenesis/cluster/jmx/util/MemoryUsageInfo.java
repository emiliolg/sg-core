
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.cluster.jmx.util;

import java.io.Serializable;
import java.lang.management.ManagementFactory;
import java.lang.management.MemoryPoolMXBean;
import java.lang.management.MemoryUsage;
import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonIgnore;

import org.jetbrains.annotations.NotNull;

import tekgenesis.common.core.Option;

import static java.lang.management.MemoryType.HEAP;

import static tekgenesis.common.Predefined.cast;
import static tekgenesis.common.Predefined.notNull;
import static tekgenesis.common.collections.Colls.first;

/**
 * Memory Information.
 */
public final class MemoryUsageInfo implements Serializable {

    //~ Instance Fields ..............................................................................................................................

    private final Map<String, MemoryInfo> details = new HashMap<>();

    //~ Methods ......................................................................................................................................

    /**  */
    public MemoryInfo get(String sectionName) {
        return notNull(details.get(sectionName), new MemoryInfo());
    }

    /**  */
    public void put(String memorySectionName, MemoryInfo memoryInfo) {
        details.put(memorySectionName, memoryInfo);
    }

    /**  */
    public Map<String, MemoryInfo> getDetails() {
        return details;
    }

    /** Get Old Gen Memory Info. */
    @JsonIgnore public MemoryInfo getOldGen() {
        final Option<String> first = first(details.keySet(), s -> s != null && s.endsWith("Old Gen"));

        return first.isPresent() ? details.get(first.get()) : new MemoryInfo();
    }

    //~ Methods ......................................................................................................................................

    /**  */
    public static MemoryUsageInfo from(Map<String, Object> values) {
        final MemoryUsageInfo memoryUsageInfo = new MemoryUsageInfo();
        for (final String s : values.keySet()) {
            final MemoryInfo memoryInfo;
            final Object     v = values.get(s);
            if (v instanceof MemoryInfo) memoryInfo = (MemoryInfo) v;
            else {
                memoryInfo = new MemoryInfo();
                final Map<String, Integer> content = cast(v);
                // noinspection DuplicateStringLiteralInspection
                memoryInfo.setCommitted(content.get("committed"));
                memoryInfo.setInit(content.get("init"));
                memoryInfo.setMax(content.get("max"));
                memoryInfo.setUsed(content.get("used"));
            }

            memoryUsageInfo.put(s, memoryInfo);
        }
        return memoryUsageInfo;
    }  // end method from

    /** Returns the Memory Usage Info. */
    @NotNull public static MemoryUsageInfo getMemoryUsageInfo() {
        final MemoryUsageInfo memoryUsageInfo = new MemoryUsageInfo();

        for (final MemoryPoolMXBean mpBean : ManagementFactory.getMemoryPoolMXBeans()) {
            final MemoryUsage mpBeanUsage = mpBean.getUsage();

            final MemoryInfo memoryInfo = new MemoryInfo();
            memoryInfo.setCommitted(mpBeanUsage.getCommitted());
            memoryInfo.setInit(mpBeanUsage.getInit());
            memoryInfo.setMax(mpBeanUsage.getMax());
            memoryInfo.setUsed(mpBeanUsage.getUsed());

            if (mpBean.getType() == HEAP) {
                final String name = mpBean.getName();
                memoryUsageInfo.put(name, memoryInfo);
            }
        }

        return memoryUsageInfo;
    }

    //~ Static Fields ................................................................................................................................

    private static final long serialVersionUID = -1499493289655536373L;
}
