
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.lang.mm.highlight;

import java.lang.ref.Reference;
import java.lang.ref.SoftReference;
import java.util.ResourceBundle;

import com.intellij.CommonBundle;

/**
 * The Message bundle for the MM plugin.
 */
class MMBundle {

    //~ Constructors .................................................................................................................................

    private MMBundle() {}

    //~ Methods ......................................................................................................................................

    static String message(String key, Object... params) {
        return CommonBundle.message(getBundle(), key, params);
    }

    private static ResourceBundle getBundle() {
        ResourceBundle bundle = ourBundle != null ? ourBundle.get() : null;
        if (bundle == null) {
            bundle    = ResourceBundle.getBundle(BUNDLE);
            ourBundle = new SoftReference<>(bundle);
        }
        return bundle;
    }

    //~ Static Fields ................................................................................................................................

    private static Reference<ResourceBundle> ourBundle = null;
    private static final String              BUNDLE    = "colors.ColorsBundle";
}
