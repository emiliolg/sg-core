
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
 * Indent properties.
 */
public interface Indent<T extends FoBuilder<T, ? extends Fo>> {

    //~ Methods ......................................................................................................................................

    /** End indent. */
    T endIndent(String value);

    /** Start indent. */
    T startIndent(String value);

    //~ Inner Classes ................................................................................................................................

    class IndentHelper {
        private IndentHelper() {}

        /** End indent. */
        public static <T extends FoBuilder<T, ? extends Fo>> T endIndent(T fo, String value) {
            return fo.addProperty("end-indent", value);
        }

        /** Start indent. */
        public static <T extends FoBuilder<T, ? extends Fo>> T startIndent(T fo, String value) {
            return fo.addProperty("start-indent", value);
        }
    }
}
