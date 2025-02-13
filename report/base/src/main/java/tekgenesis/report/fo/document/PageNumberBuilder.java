
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
 * Page Number Builder.
 */
public class PageNumberBuilder extends FoBuilder<PageNumberBuilder, PageNumber> {

    //~ Methods ......................................................................................................................................

    @Override public PageNumber build() {
        return new PageNumber();
    }

    //~ Methods ......................................................................................................................................

    /** @return  PageNumberBuilder. the current page number */
    public static PageNumberBuilder pageNumber() {
        return new PageNumberBuilder();
    }
}
