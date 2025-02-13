
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.metadata.link;

import java.util.LinkedHashMap;
import java.util.Locale;
import java.util.Map;

import org.jetbrains.annotations.NotNull;

import tekgenesis.common.env.i18n.I18nBundle;

/**
 * Link Localizer.
 */
public class LinkLocalizer {

    //~ Instance Fields ..............................................................................................................................

    private final I18nBundle bundle;
    private final Link       link;
    private final Locale     locale;

    //~ Constructors .................................................................................................................................

    /** Create a Link localizer. */
    public LinkLocalizer(Link link, Locale locale) {
        this.link   = link;
        this.locale = locale;
        bundle      = I18nBundle.getBundle(link.getFullName());
    }

    //~ Methods ......................................................................................................................................

    /** Localize only the label. */
    public Link localize() {
        if (bundle.existsFor(locale)) {
            link.setLabel(bundle.getString(link.getName(), locale));
            return link;
        }
        else return link;
    }

    //~ Methods ......................................................................................................................................

    /** Return a map with the strings to be localized for a specified link. */
    public static Map<String, String> stringsForLink(@NotNull Link link) {
        final Map<String, String> result = new LinkedHashMap<>();
        result.put(link.getName(), link.getLabel());
        return result;
    }
}  // end class LinkLocalizer
