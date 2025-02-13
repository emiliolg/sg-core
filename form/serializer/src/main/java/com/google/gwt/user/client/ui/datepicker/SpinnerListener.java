
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package com.google.gwt.user.client.ui.datepicker;

/**
 * Every widget that can be wired to {@link Spinner} must implement this interface.
 */
public interface SpinnerListener {

    //~ Methods ......................................................................................................................................

    /**
     * Implement this method to listen to spinner changes.
     *
     * @param  value  the current spinner value
     */
    void onSpinning(long value);
}
