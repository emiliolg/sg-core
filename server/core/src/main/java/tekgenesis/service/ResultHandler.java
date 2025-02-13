
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.service;

import org.jetbrains.annotations.NotNull;

/**
 * Internal service response.
 */
public interface ResultHandler {

    //~ Methods ......................................................................................................................................

    /** Handle given result. */
    void handle(@NotNull final Result<?> r, @NotNull final Forwarder forwarder);
}
