
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.report.fo.components;

import java.util.List;

import tekgenesis.report.fo.Fo;
import tekgenesis.report.fo.FoContainer;

/**
 * Basic Link fo element.
 */
public class BasicLink extends FoContainer {

    //~ Constructors .................................................................................................................................

    protected BasicLink(String content, List<Fo> children) {
        super("basic-link");
        this.content  = content;
        this.children = children;
    }
}
