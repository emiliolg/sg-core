
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.report.fo.document;

import tekgenesis.report.fo.components.FoBuilder;

/**
 * Last Page Number Builder.
 */
public class LastPageNumberBuilder extends FoBuilder<LastPageNumberBuilder, LastPageNumber> {

    //~ Methods ......................................................................................................................................

    @Override public LastPageNumber build() {
        return new LastPageNumber();
    }

    //~ Methods ......................................................................................................................................

    /** @return  LastPageNumberBuilder. The last page number */
    public static LastPageNumberBuilder lastPageNumber() {
        return new LastPageNumberBuilder();
    }
}
