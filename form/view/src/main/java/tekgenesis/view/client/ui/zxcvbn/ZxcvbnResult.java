
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.view.client.ui.zxcvbn;

import com.google.gwt.core.client.JavaScriptObject;

/**
 * Wrapper of Zxcvbn test result.
 */
public final class ZxcvbnResult extends JavaScriptObject {

    //~ Constructors .................................................................................................................................

    // Constructor in overlay types must be protected.
    @SuppressWarnings("ProtectedMemberInFinalClass")
    protected ZxcvbnResult() {
        // JSO constructor
    }

    //~ Methods ......................................................................................................................................

    /** How long it took to calculate an answer, in milliseconds. Usually only a few ms. */
    public native int getCalcTime()  /*-{ return this.calc_time; }-*/;

    /** Estimation of actual crack time, in seconds. */
    public native double getCrackTime()  /*-{ return this.crack_time; }-*/;

    /** Same crack time, as a friendlier string: "instant", "6 minutes", "centuries", etc. */
    public native String getCrackTimeDisplay()  /*-{ return this.crack_time_display; }-*/;

    /** Entropy in 'bits'. */
    public native double getEntropy()  /*-{ return this.entropy; }-*/;

    /**
     * [0,1,2,3,4] if crack time is less than [10**2, 10**4, 10**6, 10**8, Infinity]. (Useful for
     * implementing a strength bar.)
     */
    public native int getScore()  /*-{ return this.score; }-*/;
}
