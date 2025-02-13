
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.report.fo.document;

import java.util.HashMap;

import tekgenesis.report.fo.Fo;

/**
 * Page number fo element.
 */
public class PageNumber extends Fo {

    //~ Constructors .................................................................................................................................

    /** Defaul constructor. */
    public PageNumber() {
        super("page-number");
        properties = new HashMap<>();
    }
}
