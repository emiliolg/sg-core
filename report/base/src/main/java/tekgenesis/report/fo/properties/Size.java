
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
 * Size properties.
 */
public interface Size<T extends FoBuilder<T, ? extends Fo>> {

    //~ Methods ......................................................................................................................................

    /** Height. */
    T height(String value);

    /** Width. */
    T width(String value);

    //~ Inner Classes ................................................................................................................................

    @SuppressWarnings("DuplicateStringLiteralInspection")
    class SizeHelper {
        private SizeHelper() {}

        /** Height. */
        public static <T extends FoBuilder<T, ? extends Fo>> T height(T fo, String value) {
            return fo.addProperty("height", value);
        }

        /** Width. */
        public static <T extends FoBuilder<T, ? extends Fo>> T width(T fo, String value) {
            return fo.addProperty("width", value);
        }
    }
}
