
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
 * Padding properties.
 */
public interface Padding<T extends FoBuilder<T, ? extends Fo>> {

    //~ Methods ......................................................................................................................................

    /** Padding. */
    T padding(String value);

    /** Padding before. */
    T paddingBefore(String value);

    /** Padding bottom. */
    T paddingBottom(String value);

    /** Padding end. */
    T paddingEnd(String value);

    /** Padding left. */
    T paddingLeft(String value);

    /** Padding right. */
    T paddingRight(String value);

    /** Padding start. */
    T paddingStart(String value);

    /** Padding top. */
    T paddingTop(String value);

    //~ Inner Classes ................................................................................................................................

    class PaddingHelper {
        private PaddingHelper() {}

        /** Padding. */
        public static <T extends FoBuilder<T, ? extends Fo>> T padding(T fo, String value) {
            return fo.addProperty("padding", value);
        }

        /** Padding before. */
        public static <T extends FoBuilder<T, ? extends Fo>> T paddingBefore(T fo, String value) {
            return fo.addProperty("padding-before", value);
        }

        /** Padding bottom. */
        public static <T extends FoBuilder<T, ? extends Fo>> T paddingBottom(T fo, String value) {
            return fo.addProperty("padding-bottom", value);
        }

        /** Padding end. */
        public static <T extends FoBuilder<T, ? extends Fo>> T paddingEnd(T fo, String value) {
            return fo.addProperty("padding-end", value);
        }

        /** Padding left. */
        public static <T extends FoBuilder<T, ? extends Fo>> T paddingLeft(T fo, String value) {
            return fo.addProperty("padding-left", value);
        }

        /** Padding right. */
        public static <T extends FoBuilder<T, ? extends Fo>> T paddingRight(T fo, String value) {
            return fo.addProperty("padding-right", value);
        }

        /** Padding start. */
        public static <T extends FoBuilder<T, ? extends Fo>> T paddingStart(T fo, String value) {
            return fo.addProperty("padding-start", value);
        }

        /** Padding top. */
        public static <T extends FoBuilder<T, ? extends Fo>> T paddingTop(T fo, String value) {
            return fo.addProperty("padding-top", value);
        }
    }  // end class PaddingHelper
}  // end interface Padding
