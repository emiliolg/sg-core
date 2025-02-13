
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.report.fo.components;

import tekgenesis.report.fo.Fo;

/**
 * Inline fo element.
 */
public class Inline extends Fo {

    //~ Constructors .................................................................................................................................

    protected Inline(String content) {
        // noinspection DuplicateStringLiteralInspection
        super("inline");
        this.content = content;
    }
}
