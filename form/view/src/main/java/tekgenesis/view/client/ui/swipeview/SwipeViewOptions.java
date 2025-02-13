
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.view.client.ui.swipeview;

import tekgenesis.metadata.form.model.FormConstants;

/**
 * Options to customize swipe view.
 */
public class SwipeViewOptions {

    //~ Instance Fields ..............................................................................................................................

    private boolean hastyPageFlip;

    private boolean loop;
    private int     numberOfPages;

    private double snapThreshold;
    private int    width;

    //~ Constructors .................................................................................................................................

    /** Creates a SwipeViewOptions with defaults. */
    @SuppressWarnings("WeakerAccess")  // Options for Swipe component, they can be used.
    public SwipeViewOptions() {
        numberOfPages = 3;
        snapThreshold = 0D;
        hastyPageFlip = false;
        loop          = true;
        width         = FormConstants.DEFAULT_WIDTH;
    }

    //~ Methods ......................................................................................................................................

    /** Turns on fast page flip. */
    public SwipeViewOptions hastyPageFlip(boolean h) {
        hastyPageFlip = h;
        return this;
    }

    /** Sets a swipe view to switch to the start when reaching the end. */
    public SwipeViewOptions loop(boolean l) {
        loop = l;
        return this;
    }

    /** Sets the number of pages of this swipe view. */
    public SwipeViewOptions numberOfPages(int n) {
        numberOfPages = n;
        return this;
    }

    /** Sets snap threshold (how much to scroll till new swiped page). */
    public SwipeViewOptions snapThreshold(double s) {
        snapThreshold = s;
        return this;
    }

    /** Sets width. */
    public SwipeViewOptions width(int w) {
        width = w;
        return this;
    }

    /** Gets the number of pages. */
    public int getNumberOfPages() {
        return numberOfPages;
    }

    /** Returns true if hasty page flip is enabled. */
    public boolean isHastyPageFlip() {
        return hastyPageFlip;
    }

    /** Returns true if cyclic is enabled. */
    public boolean isLoop() {
        return loop;
    }

    /** Gets the snap threshold. */
    public double getSnapThreshold() {
        return snapThreshold;
    }

    /** Gets the swiper's width. */
    public int getWidth() {
        return width;
    }

    //~ Static Fields ................................................................................................................................

    public static final SwipeViewOptions DEFAULT = new SwipeViewOptions();
}  // end class SwipeViewOptions
