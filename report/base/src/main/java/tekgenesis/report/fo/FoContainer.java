
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.report.fo;

import java.util.ArrayList;
import java.util.List;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

/**
 * Abstract class that represents a fo element who can contain children.
 */
public abstract class FoContainer extends Fo {

    //~ Instance Fields ..............................................................................................................................

    protected List<Fo> children;

    //~ Constructors .................................................................................................................................

    protected FoContainer(String node) {
        super(node);
        children = new ArrayList<>();
    }

    //~ Methods ......................................................................................................................................

    @Override public Element buildElement(Document doc) {
        final Element elem = super.buildElement(doc);
        for (final Element element : buildChildrenElements(doc))
            elem.appendChild(element);
        return elem;
    }

    protected List<Element> buildChildrenElements(Document doc) {
        final List<Element> result = new ArrayList<>();
        if (children == null) return result;
        for (final Fo child : children)
            result.add(child.buildElement(doc));
        return result;
    }
}  // end class FoContainer
