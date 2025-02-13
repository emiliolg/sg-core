
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.inbox;

import java.io.ByteArrayInputStream;
import java.io.IOException;

import org.jetbrains.annotations.NotNull;

import tekgenesis.common.core.Option;
import tekgenesis.common.env.context.Context;
import tekgenesis.form.*;
import tekgenesis.metadata.form.widget.Form;
import tekgenesis.metadata.routing.Route;
import tekgenesis.repository.ModelRepository;
import tekgenesis.service.Factory;
import tekgenesis.service.Result;

import static tekgenesis.common.Predefined.isEmpty;
import static tekgenesis.common.util.Reflection.invoke;
import static tekgenesis.transaction.Transaction.invokeInTransaction;

/**
 * User class for Handler: WorkItemHandler
 */
public class WorkItemHandler extends WorkItemHandlerBase {

    //~ Constructors .................................................................................................................................

    WorkItemHandler(@NotNull Factory factory) {
        super(factory);
    }

    //~ Methods ......................................................................................................................................

    @NotNull @Override public Result<String> process(@NotNull String name, @NotNull String id, @NotNull WorkItemMessage body) {
        final ModelRepository singleton = Context.getSingleton(ModelRepository.class);

        final Option<Form> model = singleton.getModel(name, Form.class);
        if (!model.isPresent()) return badRequest();

        final Route<Form> route = tekgenesis.metadata.routing.Routes.route(String.format("%s/%s", name, id));
        return doProcess(body, new HandlerImpl<>(route.getTarget(), route.getPath(), route.getKey()));
    }

    private Result<String> doProcess(@NotNull WorkItemMessage body, Handler<FormInstance<?>> handler) {
        return invokeInTransaction(t -> {
            final FormImporter importer = handler.importer();

            try {
                importer.from(new ByteArrayInputStream(body.getPayload().getBytes()));
            }
            catch (final IOException e) {
                return internalServerError();
            }

            final FormInstance<?> instance = handler.instance();

            final Action action = isEmpty(body.getAction()) ? handler.getRouteKey().isPresent() ? instance.update() : instance.create()
                                                            : invoke(instance, body.getAction());

            if (action == null || action.isError()) {
                final String message = action == null ? "Result of action invocation null." : handler.getMessage(action);
                t.abort();
                return internalServerError(message);
            }
            return ok();
        });
    }
}  // end class WorkItemHandler
