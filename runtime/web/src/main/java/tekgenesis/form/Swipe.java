
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.form;

import org.jetbrains.annotations.NotNull;

/**
 * Form swipe action.
 */
public interface Swipe extends Detail {

    //~ Methods ......................................................................................................................................

    /**
     * Sets this Swipe as a cyclic one, when user reaches the last form and swipes again it will
     * load the first one.
     */
    Swipe cyclic();

    /** Width and height of details popup. By default, we will center it. */
    @NotNull Swipe dimension(int width, int height);

    /** How many forms will be fetch. */
    @NotNull Swipe fetchSize(int fetchSize);

    /** If turned on, dimension will be ignored and popup will fill the screen. */
    @NotNull Swipe fullscreen();
}
