
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
 * Column properties.
 */
public interface Column<T extends FoBuilder<T, ? extends Fo>> {

    //~ Methods ......................................................................................................................................

    /** Column width. */
    T columnWidth(String value);

    //~ Inner Classes ................................................................................................................................

    class ColumnHelper {
        private ColumnHelper() {}

        /** Column width. */
        public static <T extends FoBuilder<T, ? extends Fo>> T columnWidth(T fo, String value) {
            return fo.addProperty("column-width", value);
        }
    }
}
