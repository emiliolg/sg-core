
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
 * Border properties.
 */
public interface Border<T extends FoBuilder<T, ? extends Fo>> {

    //~ Methods ......................................................................................................................................

    /** Border before style. */
    T borderBeforeStyle(String value);

    /** Border before width. */
    T borderBeforeWidth(String value);

    /** Border bottom. */
    T borderBottom(String value);

    /** Border color. */
    T borderColor(String value);

    /** Border left. */
    T borderLeft(String value);

    /** Border right. */
    T borderRight(String value);

    /** Border style. */
    T borderStyle(String value);

    /** Border top. */
    T borderTop(String value);

    /** Border width. */
    T borderWidth(String value);

    //~ Inner Classes ................................................................................................................................

    class BorderHelper {
        private BorderHelper() {}

        /** Border before style. */
        public static <T extends FoBuilder<T, ? extends Fo>> T borderBeforeStyle(T fo, String value) {
            return fo.addProperty("border-before-style", value);
        }
        /** Border before width. */
        public static <T extends FoBuilder<T, ? extends Fo>> T borderBeforeWidth(T fo, String value) {
            return fo.addProperty("border-before-width", value);
        }

        /** Border bottom. */
        public static <T extends FoBuilder<T, ? extends Fo>> T borderBottom(T fo, String value) {
            return fo.addProperty("border-bottom", value);
        }

        /** Border color. */
        public static <T extends FoBuilder<T, ? extends Fo>> T borderColor(T fo, String value) {
            return fo.addProperty("border-color", value);
        }

        /** Border left. */
        public static <T extends FoBuilder<T, ? extends Fo>> T borderLeft(T fo, String value) {
            return fo.addProperty("border-left", value);
        }

        /** Border right. */
        public static <T extends FoBuilder<T, ? extends Fo>> T borderRight(T fo, String value) {
            return fo.addProperty("border-right", value);
        }

        /** Border style. */
        public static <T extends FoBuilder<T, ? extends Fo>> T borderStyle(T fo, String value) {
            return fo.addProperty(" border-style", value);
        }

        /** Border top. */
        public static <T extends FoBuilder<T, ? extends Fo>> T borderTop(T fo, String value) {
            return fo.addProperty("border-top", value);
        }

        /** Border width. */
        public static <T extends FoBuilder<T, ? extends Fo>> T borderWidth(T fo, String value) {
            return fo.addProperty("border-width", value);
        }
    }  // end class BorderHelper
}  // end interface Border
