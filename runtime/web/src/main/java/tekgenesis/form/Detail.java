
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
 * Form details action.
 */
public interface Detail extends Action {

    //~ Methods ......................................................................................................................................

    /** Width and height of details popup. By default, we will center it. */
    @NotNull Detail dimension(int width, int height);

    /** If turned on, dimension will be ignored and popup will fill the screen. */
    @NotNull Detail fullscreen();

    /** Margin top of the dialog in pixels. */
    @NotNull Detail marginTop(int margin);
}
