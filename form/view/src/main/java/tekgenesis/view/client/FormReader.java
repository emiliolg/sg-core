
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.view.client;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;

import tekgenesis.common.logging.Logger;

import static tekgenesis.common.logging.Logger.getLogger;

/**
 * Entry point classes define <code>onModuleLoad().</code>
 */
@SuppressWarnings("WeakerAccess")
public class FormReader implements EntryPoint {

    //~ Methods ......................................................................................................................................

    /** This is the entry point method. */
    public void onModuleLoad() {
        SuiGenerisClientApi.register();

        GWT.setUncaughtExceptionHandler(LOGGER::error);
    }

    //~ Static Fields ................................................................................................................................

    public static final Logger LOGGER = getLogger(FormReader.class);
}
