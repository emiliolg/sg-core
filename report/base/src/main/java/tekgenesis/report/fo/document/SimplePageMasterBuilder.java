
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
import tekgenesis.report.fo.properties.Margin;
import tekgenesis.report.fo.properties.Size;

/**
 * Simple Page Master Builder.
 */
public class SimplePageMasterBuilder extends FoBuilder<SimplePageMasterBuilder, SimplePageMaster>
    implements Margin<SimplePageMasterBuilder>, Size<SimplePageMasterBuilder>
{

    //~ Instance Fields ..............................................................................................................................

    RegionAfter  after  = null;
    RegionBefore before = null;

    RegionBody body       = null;
    String     masterName = "";

    //~ Methods ......................................................................................................................................

    public SimplePageMaster build() {
        if (body == null) body = new RegionBody();
        if (before == null) before = new RegionBefore();
        if (after == null) after = new RegionAfter();
        return new SimplePageMaster(masterName, body, before, after).withProperties(getProperties());
    }

    @Override public SimplePageMasterBuilder height(String value) {
        return addProperty("page-height", value);
    }

    @Override public SimplePageMasterBuilder marginBottom(String value) {
        return MarginHelper.marginBottom(this, value);
    }

    @Override public SimplePageMasterBuilder marginLeft(String value) {
        return MarginHelper.marginLeft(this, value);
    }

    @Override public SimplePageMasterBuilder marginRight(String value) {
        return MarginHelper.marginRight(this, value);
    }

    @Override public SimplePageMasterBuilder marginTop(String value) {
        return MarginHelper.marginTop(this, value);
    }

    /** Set name of the master page. */
    public SimplePageMasterBuilder masterName(String name) {
        masterName = name;
        return this;
    }

    @Override public SimplePageMasterBuilder width(String value) {
        return addProperty("page-width", value);
    }

    //~ Methods ......................................................................................................................................

    /** @return  a new simple page master */
    public static SimplePageMasterBuilder simplePageMaster() {
        return new SimplePageMasterBuilder();
    }
}  // end class SimplePageMasterBuilder
