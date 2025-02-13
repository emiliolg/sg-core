
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.report.fo.components;

/**
 * Basic Link Builder.
 */
public class BasicLinkB extends FoContainerB<BasicLinkB, BasicLink> {

    //~ Methods ......................................................................................................................................

    public BasicLink build() {
        return new BasicLink(content, buildChildren()).withProperties(getProperties());
    }

    //~ Methods ......................................................................................................................................

    /** Returns new basic link. */
    public static BasicLinkB basicLink() {
        return new BasicLinkB();
    }
}
