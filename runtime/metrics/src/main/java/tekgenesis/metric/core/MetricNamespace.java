
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
 * Metric namespace util.
 */
public final class MetricNamespace {

    //~ Constructors .................................................................................................................................

    private MetricNamespace() {}

    //~ Methods ......................................................................................................................................

    /** extract package and replace with _, just leaving the classname as namespace. */
    public static String resolveSource(String source) {
        final String[] split = source.split("\\.");
        String         ret   = "";

        for (int i = 0; i < split.length; i++) {
            if (i == split.length - 1) ret = ret + ".";
            else if (i != 0) ret = ret + "_";
            ret = ret + split[i];
        }
        return ret;
    }
}
