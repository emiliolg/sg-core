
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.view.client.ui.swipeview;

import static java.lang.Character.toUpperCase;

/**
 * Utility class to analyze browser capabilities.
 */
class BrowserCapabilitiesUtil {

    //~ Instance Fields ..............................................................................................................................

    private final String  cssVendor;
    private final boolean hasTouch;
    private final String  translateZ;

    private final String vendor;

    //~ Constructors .................................................................................................................................

    /** Create a Browser Capabilities util. */
    private BrowserCapabilitiesUtil() {
        vendor    = findVendor();
        cssVendor = !"".equals(vendor) ? "-" + vendor.toLowerCase() + "-" : "";

        // Browser capabilities
        final boolean has3d = hasProperty(prefixStyle("perspective"));
        hasTouch = windowHasProperty("ontouchstart");

        // Helpers
        translateZ = has3d ? " translateZ(0)" : "";
    }

    //~ Methods ......................................................................................................................................

    /** Returns true if browser has touch capabilities. */
    public boolean hasTouch() {
        return hasTouch;
    }

    /** Get CSS vendor style name. */
    public String getCssVendor() {
        return cssVendor;
    }

    /** Get transform style name. */
    public String getTransform() {
        return cssVendor + "transform";
    }

    /** Get transition duration style name. */
    public String getTransitionDuration() {
        return cssVendor + "transition-duration";
    }

    /** Get translate Z style name. */
    public String getTranslateZ() {
        return translateZ;
    }

    //J-
    /** Finds vendor. */
    private native String findVendor()  /*-{
        var dummyStyle = document.createElement('div').style;

        var vendors = 't,webkitT,MozT,msT,OT'.split(','),
            t,
            i = 0,
            l = vendors.length;

        for ( ; i < l; i++ ) {
            t = vendors[i] + 'ransform';
            if ( t in dummyStyle ) {
                return vendors[i].substr(0, vendors[i].length - 1);
            }
        }
        return 'false';
    }-*/;
    //J+

    //J-
    /** True if an Style Declaration has certain css property. */
    private native boolean hasProperty(String cssProperty)  /*-{
        return cssProperty in document.createElement('div').style;
    }-*/;
    //J+

    private String prefixStyle(final String style) {
        if ("".equals(vendor)) return style;
        return vendor + toUpperCase(style.charAt(0)) + style.substring(1);
    }

    /** True if a Window supports touch. */
    private native boolean windowHasProperty(String property)  /*-{ return property in $wnd; }-*/;

    //~ Methods ......................................................................................................................................

    /** Gets Browser Capabiltiies Util. */
    public static BrowserCapabilitiesUtil getBrowserCapabilities() {
        return BROWSER_CAPABILITIES_UTIL;
    }

    //~ Static Fields ................................................................................................................................

    private static final BrowserCapabilitiesUtil BROWSER_CAPABILITIES_UTIL = new BrowserCapabilitiesUtil();
}  // end class BrowserCapabilitiesUtil
