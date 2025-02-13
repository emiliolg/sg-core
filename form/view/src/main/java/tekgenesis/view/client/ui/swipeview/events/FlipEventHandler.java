
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.view.client.ui.swipeview.events;

/**
 * Flip handler to handle flip events.
 */
public interface FlipEventHandler {

    //~ Methods ......................................................................................................................................

    /**
     * Called on flip.
     *
     * @param  rightDirection  the directio of the flip.
     */
    void onFlip(boolean rightDirection);
}
