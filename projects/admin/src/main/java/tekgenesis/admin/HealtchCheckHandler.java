
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.admin;

import org.jetbrains.annotations.NotNull;

import tekgenesis.common.service.Status;
import tekgenesis.service.Factory;
import tekgenesis.service.Result;

import static tekgenesis.admin.StatusHandler.services;

/**
 * User class for Handler: HealtchCheckHandler
 */
public class HealtchCheckHandler extends HealtchCheckHandlerBase {

    //~ Constructors .................................................................................................................................

    HealtchCheckHandler(@NotNull Factory factory) {
        super(factory);
    }

    //~ Methods ......................................................................................................................................

    /** Invoked for route "/sg/health_check". */
    @NotNull @Override public Result<String> healthCheck() {
        final boolean ok = services.stream().allMatch(StatusService::check);
        if (ok) return ok("Up & running");
        else return status(Status.SERVICE_UNAVAILABLE, "Not running");
    }
}
