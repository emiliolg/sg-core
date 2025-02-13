
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.report.fo.document;

import tekgenesis.report.fo.Fo;

/**
 * Last page number fo element.
 */
@SuppressWarnings("DuplicateStringLiteralInspection")
public class LastPageNumber extends Fo {

    //~ Constructors .................................................................................................................................

    /** Default constructor. */
    public LastPageNumber() {
        super("page-number-citation");
        properties.put("ref-id", "last-block");
    }
}
