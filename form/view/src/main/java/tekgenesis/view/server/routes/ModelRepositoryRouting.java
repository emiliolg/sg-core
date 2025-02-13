
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.view.server.routes;

import java.util.Collection;
import java.util.Map;

import org.jetbrains.annotations.NotNull;

import tekgenesis.common.collections.MultiMap;
import tekgenesis.common.env.context.Context;
import tekgenesis.common.logging.Logger;
import tekgenesis.common.service.Method;
import tekgenesis.metadata.handler.*;
import tekgenesis.repository.ModelRepository;

import static tekgenesis.common.service.Method.*;

/**
 * Model Repository Routing.
 */
public class ModelRepositoryRouting {

    //~ Constructors .................................................................................................................................

    private ModelRepositoryRouting() {}

    //~ Methods ......................................................................................................................................

    private static Map<Method, Collection<Route>> routesByMethod(@NotNull final ModelRepository repository) {
        final MultiMap<Method, Route> map = new MultiMap.Builder<Method, Route>().withSortedKeys().build();

        for (final Handler h : repository.getModels().filter(Handler.class)) {
            for (final Route route : h.getChildren())
                map.put(route.getHttpMethod(), route);
        }

        return map.asMap();
    }

    /** Return a routing object using given repository. */
    private static Routing routing(@NotNull final ModelRepository repository) {
        final Routing result = Routing.create();

        final Map<Method, Collection<Route>> routes = routesByMethod(repository);
        for (final Method method : routes.keySet()) {
            routes.get(method).forEach(result::addRoute);

            // default HEAD method to GET, some routes may be overridden by HEAD routes latter
            if (method == GET) routes.get(GET).forEach(r -> result.addRoute(r, HEAD));
        }

        Logger.getLogger(ModelRepositoryRouting.class)
            .debug(String.format("Initialized routing for model repository, defining: %s", result.toString()));
        return result;
    }

    //~ Static Fields ................................................................................................................................

    public static final Routing routing = routing(Context.getSingleton(ModelRepository.class));
}  // end class ModelRepositoryRouting
