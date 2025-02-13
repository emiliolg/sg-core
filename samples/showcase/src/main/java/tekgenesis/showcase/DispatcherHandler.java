
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.showcase;

import java.io.IOException;

import org.jetbrains.annotations.NotNull;

import tekgenesis.form.Handler;

/**
 * User class for handler tekgenesis.showcase.DispatcherHandler on form: Dispatcher
 */
public class DispatcherHandler extends DispatcherHandlerBase {

    //~ Methods ......................................................................................................................................

    @Override public void handleGet(@NotNull Handler<Dispatcher> handler)
        throws IOException
    {
        // final View.Context context = View.Context.create();
        // context.put("baby", handler.getRouteKey().getOrElse("Jose"));
        // html.forward("/dispatcher", context, req, resp);

        // final String baby = handler.getRouteKey().getOrElse("Jose");
        // Views.template(baby, Views.main(baby)).forward(req, resp);
    }
}
