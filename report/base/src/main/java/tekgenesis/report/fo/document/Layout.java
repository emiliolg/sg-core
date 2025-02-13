
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.report.fo.document;

import tekgenesis.report.fo.FoContainer;

/**
 * Layout fo element.
 */
public class Layout extends FoContainer {

    //~ Instance Fields ..............................................................................................................................

    SimplePageMaster simplePageMaster;

    //~ Constructors .................................................................................................................................

    protected Layout(SimplePageMaster simplePageMaster) {
        super("layout-master-set");
        this.simplePageMaster = simplePageMaster;
        children.add(simplePageMaster);
    }
}
