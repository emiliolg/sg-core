
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
 * Block fo element.
 */
@SuppressWarnings("ClassWithTooManyMethods")
public class Block extends FoContainer {

    //~ Constructors .................................................................................................................................

    protected Block(String content, List<Fo> children) {
        super(NODE);
        this.content  = content;
        this.children = children;
    }

    //~ Static Fields ................................................................................................................................

    public static final String NODE = "block";
}
