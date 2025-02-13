
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.view.shared.utils;

import java.io.UnsupportedEncodingException;
import java.util.Locale;

import com.google.gwt.core.shared.GWT;
import com.google.gwt.regexp.shared.RegExp;

import org.jetbrains.annotations.NotNull;

/**
 * URI Utils.
 */
public class UrlUtils {

    //~ Constructors .................................................................................................................................

    private UrlUtils() {}

    //~ Methods ......................................................................................................................................

    /** Returns percentage escaped string. */
    @NotNull public static String encode(@NotNull String uri) {
        final byte[] utf8bytes;
        try {
            utf8bytes = uri.getBytes("UTF-8");
        }
        catch (final UnsupportedEncodingException e) {
            // UTF-8 is guaranteed to be implemented, this code won't ever run.
            return uri;
        }
        final StringBuilder sb = new StringBuilder();
        for (final byte b : utf8bytes) {
            final int c = b & UTF8;
            // This works because characters that don't need encoding are all
            // expressed as a single UTF-8 byte
            if (('a' <= c && c <= 'z') || ('A' <= c && c <= 'Z') || ('0' <= c && c <= '9') || DONT_NEED_ENCODING.indexOf(c) != -1)
                sb.append((char) c);
            else {
                String hexByte = Integer.toHexString(c).toUpperCase(Locale.ROOT);
                if (hexByte.length() == 1) hexByte = "0" + hexByte;
                sb.append('%').append(hexByte);
            }
        }
        return sb.toString();
    }

    /** Returns the url prepared to be set as background image in css. */
    public static String urlStringForBackgroundImage(@NotNull String url) {
        return "url(\"" + url + "\")";
    }

    //~ Static Fields ................................................................................................................................

    static final String DONT_NEED_ENCODING = ";:@=$,"     // uriReserved
                                             +
                                             "-_.!~*'()"  // uriMark
                                             +
                                             "#" +
                                             "[]";        // could be used in IPv6 addresses

    // used in conditional code in encode()
    private static final RegExp ESCAPED_LBRACKET_RE = GWT.isScript() ? RegExp.compile("%5B", "g") : null;
    private static final RegExp ESCAPED_RBRACKET_RE = GWT.isScript() ? RegExp.compile("%5D", "g") : null;
    private static final int    UTF8                = 0xFF;
}
