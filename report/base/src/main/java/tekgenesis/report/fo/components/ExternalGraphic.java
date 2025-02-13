
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
 * External Graphic fo element.
 */
public class ExternalGraphic extends Fo {

    //~ Constructors .................................................................................................................................

    protected ExternalGraphic(String src) {
        super("external-graphic");
        addProperty("src", src);
    }
}
