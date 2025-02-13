
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.service;

import java.io.IOException;

import org.jetbrains.annotations.NotNull;

import tekgenesis.common.logging.Logger;
import tekgenesis.common.service.Headers;
import tekgenesis.common.service.OutboundMessageWriter;
import tekgenesis.common.service.Status;
import tekgenesis.common.service.server.Response;

import static tekgenesis.service.ResultImpl.*;
import static tekgenesis.service.ServiceDispatcher.defaultMediaTypeFor;

/**
 * Abstract response.
 */
abstract class AbstractResultHandler implements ResultHandler {

    //~ Methods ......................................................................................................................................

    @Override public final void handle(@NotNull Result<?> result, @NotNull Forwarder forwarder) {
        if (result instanceof ForwardResult) handlerForward((ForwardResult) result, forwarder);
        else {
            handleResponseStatus(result.getStatus());

            if (result instanceof ObjectResult) handleObject((ObjectResult) result, forwarder);
            else {
                handleResponseHeaders(result.getHeaders());
                if (result instanceof RedirectResult) handleRedirect((RedirectResult) result);
            }
        }
    }

    abstract void handleObject(@NotNull ObjectResult result, @NotNull Forwarder forwarder);
    abstract void handleRedirect(@NotNull RedirectResult result);
    abstract void handleResponseHeaders(@NotNull Headers result);
    abstract void handleResponseStatus(@NotNull Status status);

    void handlerForward(@NotNull ForwardResult result, Forwarder forwarder) {
        forwarder.forward(result.url, this, result.routing);
    }

    void write(@NotNull Response response, @NotNull ObjectResult result) {
        final OutboundMessageWriter writer = new OutboundMessageWriter();

        if (result.resultType != null) writer.setDefaultContentType(defaultMediaTypeFor(result.resultType));

        try {
            writer.write(response, result.converters, result.content);
        }
        catch (final IOException e) {
            logger.error(e);
        }
    }

    //~ Static Fields ................................................................................................................................

    private static final Logger logger = Logger.getLogger(AbstractResultHandler.class);
}
