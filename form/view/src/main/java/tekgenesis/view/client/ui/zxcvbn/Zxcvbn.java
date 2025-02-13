
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.view.client.ui.zxcvbn;

import com.google.gwt.core.client.JsArrayString;

/**
 * Dropbox's Zxcvbn GWT wrapper.
 */
public class Zxcvbn {

    //~ Instance Fields ..............................................................................................................................

    private boolean jsLoaded = false;

    //~ Constructors .................................................................................................................................

    private Zxcvbn() {}

    //~ Methods ......................................................................................................................................

    /** Ensures that library's js is loaded. */
    public void ensureLoaded() {
        if (!isLoaded()) loadJs();
    }

    /** Tests a given password. */
    public ZxcvbnResult testPassword(String password) {
        ensureLoaded();
        return doTestPassword(password, null);
    }

    /** Tests a given password, using user inputs as additional data. */
    public ZxcvbnResult testPassword(String password, String... userInputs) {
        ensureLoaded();
        final JsArrayString userInputsArray = JsArrayString.createArray().cast();
        if (userInputs != null && userInputs.length > 0) {
            for (final String userInput : userInputs)
                userInputsArray.push(userInput);
        }
        return doTestPassword(password, userInputsArray);
    }

    /** Tests if library's js is loaded. */
    public boolean isLoaded() {
        if (!jsLoaded)
        // Check again
        jsLoaded = checkFunction();

        return jsLoaded;
    }

    //J-
    private native ZxcvbnResult doTestPassword(String password, JsArrayString userInputsArray)  /*-{
        return $wnd.zxcvbn(password, userInputsArray);
    }-*/;
    //J+

    private void loadJs() {
        final String url = "/external/js/zxcvbn.js";
        addScript(url);
    }

    //~ Methods ......................................................................................................................................

    /**  */
    public static Zxcvbn getInstance() {
        return instance;
    }

    //J-
    private static native void addScript(String url)  /*-{
        var scripts = $doc.getElementsByTagName("script");

        for (var i = 0; i < scripts.length; i++) {
            if(scripts[i].src.indexOf("zxcvbn") != -1) return;
        }

        var elem = $doc.createElement("script");
        elem.setAttribute("src", url);
        $doc.getElementsByTagName("head")[0].appendChild(elem);
    }-*/;
    //J+

    private static native boolean checkFunction()  /*-{ return !!($wnd.zxcvbn && typeof $wnd.zxcvbn == 'function'); }-*/;

    //~ Static Fields ................................................................................................................................

    private static final Zxcvbn instance = new Zxcvbn();
}  // end class Zxcvbn
