
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.report.fo.document;

import java.util.ArrayList;
import java.util.List;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import tekgenesis.report.fo.Fo;
import tekgenesis.report.fo.FoContainer;
import tekgenesis.report.fo.components.BlockB;

/**
 * Page Sequence fo element.
 */
@SuppressWarnings("DuplicateStringLiteralInspection")
public class PageSequence extends Fo {

    //~ Instance Fields ..............................................................................................................................

    Flow          flow;
    StaticContent flowFooter = null;
    StaticContent flowHeader = null;
    List<Fo>      footer;
    List<Fo>      header;

    //~ Constructors .................................................................................................................................

    protected PageSequence(String name, List<Fo> children, List<Fo> header, List<Fo> footer) {
        super("page-sequence");
        this.header = header;
        this.footer = footer;

        properties.put("master-reference", name);
        flow       = new Flow(addLastBlock(children));
        flowHeader = new StaticContent(header, "xsl-region-before");
        flowFooter = new StaticContent(footer, "xsl-region-after");
    }

    //~ Methods ......................................................................................................................................

    @Override public Element buildElement(Document doc) {
        final Element elem = super.buildElement(doc);
        if (header != null && !header.isEmpty()) elem.appendChild(flowHeader.buildElement(doc));
        if (footer != null && !footer.isEmpty()) elem.appendChild(flowFooter.buildElement(doc));
        elem.appendChild(flow.buildElement(doc));
        return elem;
    }

    private List<Fo> addLastBlock(List<Fo> children) {
        final List<Fo> childrenWithLastBlock = new ArrayList<>(children);
        childrenWithLastBlock.add(BlockB.block().addProperty("id", "last-block").build());
        return childrenWithLastBlock;
    }

    //~ Inner Classes ................................................................................................................................

    class Flow extends FoContainer {
        Flow(List<Fo> children) {
            super("flow");
            properties.put("flow-name", "xsl-region-body");
            this.children = children;
        }
    }

    class StaticContent extends FoContainer {
        StaticContent(List<Fo> children, String name) {
            super("static-content");
            properties.put("flow-name", name);
            this.children = children;
        }
    }
}  // end class PageSequence
