
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.cluster.jmx.util;

import org.jetbrains.annotations.NotNull;

import static java.lang.String.format;

/**
 * Format Util.
 */
public final class Format {

    //~ Constructors .................................................................................................................................

    private Format() {}

    //~ Methods ......................................................................................................................................

    /** Format time as '%d Days, %d hour, %d min, %d sec'. */
    @SuppressWarnings({ "MagicNumber", "Duplicates" })
    public static String timeAsString(@NotNull Long t) {
        final long[] times = new long[4];
        long         tmp   = t / 1000;
        times[0] = tmp % 60;
        tmp      /= 60;
        times[1] = tmp % 60;
        tmp      /= 60;
        times[2] = tmp % 24;
        times[3] = tmp / 24;

        return format("%d Days, %d hour, %d min, %d sec", times[3], times[2], times[1], times[0]);
    }

    /**
     * @param   bytes  the bytes
     *
     * @return  Returns a String representation in Kib,Mib,Gib,Tib,etc..
     */
    @SuppressWarnings("MagicNumber")
    public static String toSizeString(long bytes) {
        final int unit = 1024;
        if (bytes < unit) return bytes + " B";
        final int exp = (int) (Math.log(bytes) / Math.log(unit));
        return String.format("%.2f%sB", bytes / Math.pow(unit, exp), "KMGTPE".charAt(exp - 1));
    }
}
