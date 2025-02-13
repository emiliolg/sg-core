
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.view.client.ui.utils;

import com.google.gwt.dom.client.Element;
import com.google.gwt.user.client.Timer;

/**
 * Animation class.
 */
public class Animation {

    //~ Constructors .................................................................................................................................

    private Animation() {}

    //~ Methods ......................................................................................................................................

    /** Scrolls element to the left some pixel amount . */
    public static void scrollLeft(final Element e, final int amount) {
        new Timer() {
                @Override public void run() {
                    final int offsetWidth = e.getOffsetWidth();
                    final int scrollLeft  = e.getScrollLeft();
                    e.setScrollLeft(scrollLeft + amount);

                    if (scrollLeft <= 0 || scrollLeft + offsetWidth >= e.getScrollWidth()) cancel();
                }
            }.scheduleRepeating(10);
    }

    /** Scrolls element to the right some pixel amount . */
    public static void scrollRight(final Element e, final int amount) {
        final int scrollPosition = e.getScrollLeft();

        new Timer() {
                @Override public void run() {
                    final int offsetWidth = e.getOffsetWidth();
                    final int scrollLeft  = e.getScrollLeft();
                    e.setScrollLeft(scrollLeft + amount);

                    if (scrollLeft - scrollPosition >= offsetWidth || scrollLeft + offsetWidth >= e.getScrollWidth()) cancel();
                }
            }.scheduleRepeating(10);
    }

    /** Toggles element FullScreen. */
    public static native void toggleFullScreen(Element elem)  /*-{
                                    if (@tekgenesis.view.client.ui.utils.Animation::isNotFullScreen()()) {
                                        if (elem.requestFullscreen) elem.requestFullscreen();
                                        else if (elem.msRequestFullscreen) elem.msRequestFullscreen();
                                        else if (elem.mozRequestFullScreen) elem.mozRequestFullScreen();
                                        else if (elem.webkitRequestFullscreen) elem.webkitRequestFullscreen();
                                    } else {
                                        if ($doc.exitFullscreen) $doc.exitFullscreen();
                                        else if ($doc.msExitFullscreen) $doc.msExitFullscreen();
                                        else if ($doc.mozCancelFullScreen) $doc.mozCancelFullScreen();
                                        else if ($doc.webkitExitFullscreen) $doc.webkitExitFullscreen();
                                    }
    }-*/;

    /** Returns true if there is NO Fullscreen Elements. */
    public static native boolean isNotFullScreen()  /*-{
                                    return !$doc.fullscreenElement && !$doc.mozFullScreenElement && !$doc.webkitFullscreenElement && !$doc.msFullscreenElement;
    }-*/;
}  // end class Animation
