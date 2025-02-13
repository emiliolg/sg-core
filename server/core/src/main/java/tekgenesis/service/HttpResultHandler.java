
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
import tekgenesis.common.service.HeaderNames;
import tekgenesis.common.service.Headers;
import tekgenesis.common.service.Status;

import static tekgenesis.service.ResultImpl.ObjectResult;
import static tekgenesis.service.ResultImpl.RedirectResult;

/**
 * Http result handler.
 */
public class HttpResultHandler extends AbstractResultHandler {

    //~ Instance Fields ..............................................................................................................................

    @NotNull private final HttpResponse response;

    //~ Constructors .................................................................................................................................

    /** HttpResponse constructor. */
    public HttpResultHandler(@NotNull final HttpResponse response) {
        this.response = response;
    }

    //~ Methods ......................................................................................................................................

    @Override void handleObject(@NotNull ObjectResult result, @NotNull Forwarder forwarder) {
        write(response, result);
    }

    @Override void handleRedirect(@NotNull final RedirectResult result) {
        final String location = response.raw().encodeRedirectURL(result.url);

        try {
            if (result.getStatus() == Status.FOUND) response.raw().sendRedirect(location);
            else response.raw().setHeader(HeaderNames.LOCATION, location);
        }
        catch (final IOException e) {
            logger.error(e);
        }
    }

    @Override void handleResponseHeaders(@NotNull Headers headers) {
        response.writeHeaders();
    }

    @Override void handleResponseStatus(@NotNull Status status) {
        response.setStatus(status);
    }

    //~ Static Fields ................................................................................................................................

    private static final Logger logger = Logger.getLogger(HttpResultHandler.class);
}  // end class HttpResultHandler
