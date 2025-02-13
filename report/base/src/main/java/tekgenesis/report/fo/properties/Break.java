
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.report.fo.properties;

import tekgenesis.report.fo.Fo;
import tekgenesis.report.fo.components.FoBuilder;

/**
 * Break properties.
 */
public interface Break<T extends FoBuilder<T, ? extends Fo>> {

    //~ Methods ......................................................................................................................................

    /** Break after. */
    T breakAfter(String value);

    /** Break before. */
    T breakBefore(String value);

    //~ Inner Classes ................................................................................................................................

    class BreakHelper {
        private BreakHelper() {}

        /** Break after. */
        public static <T extends FoBuilder<T, ? extends Fo>> T breakAfter(T fo, String value) {
            return fo.addProperty("break-after", value);
        }

        /** Break before. */
        public static <T extends FoBuilder<T, ? extends Fo>> T breakBefore(T fo, String value) {
            return fo.addProperty("break-before", value);
        }
    }
}
