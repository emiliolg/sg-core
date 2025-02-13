
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
 * Color properties.
 */
public interface Color<T extends FoBuilder<T, ? extends Fo>> {

    //~ Methods ......................................................................................................................................

    /** Background. */
    T background(String value);

    /** Color. */
    T color(String value);

    //~ Inner Classes ................................................................................................................................

    class ColorHelper {
        private ColorHelper() {}

        /** Background. */
        public static <T extends FoBuilder<T, ? extends Fo>> T background(T fo, String value) {
            return fo.addProperty("background-color", value);
        }

        /** Color. */
        @SuppressWarnings("DuplicateStringLiteralInspection")
        public static <T extends FoBuilder<T, ? extends Fo>> T color(T fo, String value) {
            return fo.addProperty("color", value);
        }
    }
}
