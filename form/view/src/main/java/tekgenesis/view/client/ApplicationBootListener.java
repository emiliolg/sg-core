
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.view.client;

import tekgenesis.view.shared.response.BootResponse;

/**
 * Application Boot Listener.
 */
@SuppressWarnings("WeakerAccess")
public interface ApplicationBootListener {

    //~ Methods ......................................................................................................................................

    /** Called on application boot. */
    void onBoot(BootResponse bootResponse);
}
