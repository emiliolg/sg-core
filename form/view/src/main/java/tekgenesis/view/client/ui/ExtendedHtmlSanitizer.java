
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.view.client.ui;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import com.google.gwt.safehtml.shared.HtmlSanitizer;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.safehtml.shared.SafeHtmlUtils;
import com.google.gwt.safehtml.shared.SimpleHtmlSanitizer;

/**
 * A complete HTML sanitizer, based on the GWT {@link SimpleHtmlSanitizer}. This one allows to
 * extend the white list of tags with all needed
 */
public final class ExtendedHtmlSanitizer implements HtmlSanitizer {

    //~ Constructors .................................................................................................................................

    // prevent external instantiation
    private ExtendedHtmlSanitizer() {}

    //~ Methods ......................................................................................................................................

    public SafeHtml sanitize(String html) {
        return sanitizeHtml(html);
    }

    //~ Methods ......................................................................................................................................

    /**
     * HTML-sanitizes a string.
     *
     * @param   html  the input String
     *
     * @return  a sanitized SafeHtml instance
     */
    public static SafeHtml sanitizeHtml(String html) {
        if (html == null) throw new NullPointerException("html is null");
        return new SafeHtmlString(simpleSanitize(html));
    }

    /**
     * Return a singleton ExtendedHtmlSanitizer instance.
     *
     * @return  the instance
     */
    public static ExtendedHtmlSanitizer getInstance() {
        return INSTANCE;
    }

    @SuppressWarnings("ContinueStatement")
    private static String simpleSanitize(String text) {
        final StringBuilder sanitized = new StringBuilder();

        boolean firstSegment = true;
        for (final String segment : text.split("<", -1)) {
            if (firstSegment) {
                /*
                 *  the first segment is never part of a valid tag; note that if the
                 *  input string starts with a tag, we will get an empty segment at the
                 *  beginning.
                 */
                firstSegment = false;
                sanitized.append(SafeHtmlUtils.htmlEscapeAllowEntities(segment));
                continue;
            }

            /*
             *  determine if the current segment is the start of an attribute-free tag
             *  or end-tag in our whitelist
             */
            int       tagStart   = 0;  // will be 1 if this turns out to be an end tag.
            final int tagEnd     = segment.indexOf('>');
            String    tag        = null;
            boolean   isValidTag = false;
            if (tagEnd > 0) {
                if (segment.charAt(0) == '/') tagStart = 1;
                tag = segment.substring(tagStart, tagEnd);
                if (TAG_WHITELIST.contains(tag)) isValidTag = true;
            }

            if (isValidTag) {
                // append the tag, not escaping it
                if (tagStart == 0) sanitized.append('<');
                else
                // we had seen an end-tag
                sanitized.append("</");
                sanitized.append(tag).append('>');

                // append the rest of the segment, escaping it
                sanitized.append(SafeHtmlUtils.htmlEscapeAllowEntities(segment.substring(tagEnd + 1)));
            }
            else
            // just escape the whole segment
            sanitized.append("&lt;").append(SafeHtmlUtils.htmlEscapeAllowEntities(segment));
        }
        return sanitized.toString();
    }  // end method simpleSanitize

    //~ Static Fields ................................................................................................................................

    private static final ExtendedHtmlSanitizer INSTANCE = new ExtendedHtmlSanitizer();

    private static final Set<String> TAG_WHITELIST = new HashSet<>(
            Arrays.asList("b", "em", "i", "h1", "h2", "h3", "h4", "h5", "h6", "hr", "ul", "ol", "li", "br", "p"));

    //~ Inner Classes ................................................................................................................................

    /**
     * A string wrapped as an object of type {@link SafeHtml}.
     */
    private static class SafeHtmlString implements SafeHtml {
        private String html = null;

        @SuppressWarnings("unused")
        private SafeHtmlString() {}

        SafeHtmlString(String html) {
            if (html == null) throw new NullPointerException("html is null");
            this.html = html;
        }

        public String asString() {
            return html;
        }

        @Override public boolean equals(Object obj) {
            return obj instanceof SafeHtml && html.equals(((SafeHtml) obj).asString());
        }

        @Override public int hashCode() {
            return html.hashCode();
        }

        private static final long serialVersionUID = -2478458642049715574L;
    }
}  // end class ExtendedHtmlSanitizer
