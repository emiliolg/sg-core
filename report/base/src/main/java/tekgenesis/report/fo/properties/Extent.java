
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
 * Extent properties.
 */
public interface Extent<T extends FoBuilder<T, ? extends Fo>> {

    //~ Methods ......................................................................................................................................

    /** Extent. */
    T extent(String value);

    //~ Inner Classes ................................................................................................................................

    class ExtentHelper {
        private ExtentHelper() {}

        /** Extent. */
        public static <T extends FoBuilder<T, ? extends Fo>> T extent(T fo, String value) {
            return fo.addProperty("extent", value);
        }
    }
}
