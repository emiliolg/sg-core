
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
 * Simple Page Master fo element.
 */
public class SimplePageMaster extends FoContainer {

    //~ Constructors .................................................................................................................................

    protected SimplePageMaster(String masterName, RegionBody body, RegionBefore regionBefore, RegionAfter regionAfter) {
        super(SIMPLE_PAGE_MASTER);
        properties.put(MASTER_NAME, masterName);
        children.add(body);
        children.add(regionBefore);
        children.add(regionAfter);
    }

    //~ Static Fields ................................................................................................................................

    public static final String SIMPLE_PAGE_MASTER = "simple-page-master";
    public static final String MASTER_NAME        = "master-name";
}
