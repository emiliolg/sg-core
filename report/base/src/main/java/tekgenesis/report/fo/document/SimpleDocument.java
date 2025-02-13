
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.report.fo.document;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import tekgenesis.report.fo.Fo;

/**
 * Simple Document fo element.
 */
public class SimpleDocument extends Fo {

    //~ Instance Fields ..............................................................................................................................

    Layout       layout;
    PageSequence page;

    //~ Constructors .................................................................................................................................

    protected SimpleDocument(SimplePageMaster simplePageMaster, PageSequence page) {
        super("root");
        layout    = new Layout(simplePageMaster);
        this.page = page;
        properties.put(PREFIX, URI);
    }

    //~ Methods ......................................................................................................................................

    @Override public Element buildElement(Document doc) {
        final Element elem = super.buildElement(doc);
        elem.appendChild(layout.buildElement(doc));
        elem.appendChild(page.buildElement(doc));
        elem.setAttributeNS(XMLNS, PREFIX, URI);
        return elem;
    }
}
