
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.report.fo;

import java.util.HashMap;
import java.util.Map;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import tekgenesis.common.Predefined;

/**
 * Abstract class that represents a fo element.
 */
@SuppressWarnings("WeakerAccess")  // API.
public abstract class Fo {

    //~ Instance Fields ..............................................................................................................................

    protected String content = "";
    protected String node;

    protected Map<String, String> properties;

    //~ Constructors .................................................................................................................................

    protected Fo(String node) {
        this.node  = node;
        properties = new HashMap<>();
    }

    //~ Methods ......................................................................................................................................

    /** Add property to the Fo element. */
    public <T extends Fo> T addProperty(String key, String value) {
        properties.put(key, value);
        return Predefined.cast(this);
    }

    /** Build document. */
    public Document buildDocument() {
        final Document doc  = FopBuilder.docBuilder.newDocument();
        final Element  elem = buildElement(doc);
        doc.appendChild(elem);
        return doc;
    }

    /** Add properties map to element. */
    public <T extends Fo> T withProperties(Map<String, String> props) {
        properties.putAll(props);
        return Predefined.cast(this);
    }

    /** @return  element's content */
    public String getContent() {
        return content;
    }

    protected Element buildElement(Document doc) {
        final Element elem = createElement(doc);
        elem.setTextContent(content);
        for (final Map.Entry<String, String> property : properties.entrySet())
            elem.setAttribute(property.getKey().trim(), property.getValue().trim());
        return elem;
    }

    private Element createElement(Document doc) {
        final Element elem = doc.createElementNS(URI, node);
        elem.setAttributeNS(XMLNS, PREFIX, URI);
        return elem;
    }

    //~ Static Fields ................................................................................................................................

    public static final String NUMBER_COLUMNS_SPANNED = "number-columns-spanned";

    public static final String URI    = "http://www.w3.org/1999/XSL/Format";
    public static final String XMLNS  = "http://www.w3.org/2000/xmlns/";
    public static final String PREFIX = "xmlns:fo";

    public static final String SCALE_TO_FIT = "scale-to-fit";
    public static final String AUTO         = "auto";
    public static final String DOTTED       = "dotted";
    public static final String THIN         = "thin";
}  // end class Fo
