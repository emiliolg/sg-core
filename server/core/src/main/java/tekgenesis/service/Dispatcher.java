
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.service;

import java.util.List;

import org.jetbrains.annotations.NotNull;

import tekgenesis.common.core.Option;
import tekgenesis.common.service.Method;
import tekgenesis.common.service.server.Request;
import tekgenesis.metadata.handler.RouteMatch;

/**
 * Service request dispatcher.
 */
public interface Dispatcher {

    //~ Methods ......................................................................................................................................

    /** Request dispatch for given route match. */
    @NotNull Result<?> dispatch(@NotNull final RouteMatch match, @NotNull final Request request);

    /** Route path to optional route specifying if routing is performed for an external request. */
    @NotNull Option<RouteMatch> route(@NotNull Method method, @NotNull String path, boolean external);

    /**
     * Get tried routes for last route request. Must be used only after a route is not matched to
     * get any tried routes.
     */
    @NotNull List<String> getLastTriedRoutes();
}
