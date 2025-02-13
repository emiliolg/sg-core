
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
 * Margin properties.
 */
public interface Margin<T extends FoBuilder<T, ? extends Fo>> {

    //~ Methods ......................................................................................................................................

    /** Margin bottom. */
    T marginBottom(String value);

    /** Margin left. */
    T marginLeft(String value);

    /** Margin right. */
    T marginRight(String value);

    /** Margin top. */
    T marginTop(String value);

    //~ Inner Classes ................................................................................................................................

    class MarginHelper {
        private MarginHelper() {}

        /** Margin bottom. */
        public static <T extends FoBuilder<T, ? extends Fo>> T marginBottom(T fo, String value) {
            return fo.addProperty("margin-bottom", value);
        }

        /** Margin left. */
        public static <T extends FoBuilder<T, ? extends Fo>> T marginLeft(T fo, String value) {
            return fo.addProperty("margin-left", value);
        }

        /** Margin right. */
        public static <T extends FoBuilder<T, ? extends Fo>> T marginRight(T fo, String value) {
            return fo.addProperty("margin-right", value);
        }

        /** Margin top. */
        public static <T extends FoBuilder<T, ? extends Fo>> T marginTop(T fo, String value) {
            return fo.addProperty("margin-top", value);
        }
    }
}  // end interface Margin
