
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
 * Image properties.
 */
public interface Image<T extends FoBuilder<T, ? extends Fo>> {

    //~ Methods ......................................................................................................................................

    /** Content height. */
    T contentHeight(String value);

    /** Content width. */
    T contentWidth(String value);

    //~ Inner Classes ................................................................................................................................

    class ImageHelper {
        private ImageHelper() {}

        /** Content height. */
        public static <T extends FoBuilder<T, ? extends Fo>> T contentHeight(T fo, String value) {
            return fo.addProperty("content-height", value);
        }

        /** Content width. */
        public static <T extends FoBuilder<T, ? extends Fo>> T contentWidth(T fo, String value) {
            return fo.addProperty("content-width", value);
        }
    }
}
