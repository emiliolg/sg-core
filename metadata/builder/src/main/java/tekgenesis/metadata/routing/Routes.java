
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.metadata.routing;

import org.jetbrains.annotations.NotNull;

import tekgenesis.common.env.context.Context;
import tekgenesis.metadata.form.widget.Form;
import tekgenesis.repository.ModelRepository;

/**
 * Forms routing mechanism.
 */
public class Routes {

    //~ Constructors .................................................................................................................................

    private Routes() {}

    //~ Methods ......................................................................................................................................

    /** Route given path to optional form route (using context repository). */
    public static Route<Form> route(@NotNull final String path) {
        return RoutingHolder.instance.route(path);
    }

    /** Return a routing object using given repository. */
    public static Routing<Form> routing(@NotNull final ModelRepository repository) {
        final Routing<Form> routing = new Routing<>();
        for (final Form form : repository.getModels().filter(Form.class)) {
            routing.addRoute(form.getOnRoutePath(), form);
            routing.addRoute(form.getFullName(), form);
        }
        return routing;
    }

    //~ Inner Classes ................................................................................................................................

    /**
     * Context repository form routing holder.
     */
    private static class RoutingHolder {
        private RoutingHolder() {}

        private static final Routing<Form> instance = routing(Context.getSingleton(ModelRepository.class));
    }
}  // end class Routes
