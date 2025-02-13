
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
import org.jetbrains.annotations.Nullable;

import tekgenesis.common.service.Headers;
import tekgenesis.common.service.Status;
import tekgenesis.service.ResultImpl.ObjectResult;
import tekgenesis.service.ResultImpl.RedirectResult;

/**
 * Mock result handler.
 */
class MockResultHandler extends AbstractResultHandler {

    //~ Instance Fields ..............................................................................................................................

    private RedirectResult redirect = null;

    @NotNull private final MockServerResponse response;

    //~ Constructors .................................................................................................................................

    MockResultHandler(@NotNull final MockServerResponse response) {
        this.response = response;
    }

    //~ Methods ......................................................................................................................................

    @Override void handleObject(@NotNull ObjectResult result, @NotNull Forwarder forwarder) {
        write(response, result);
    }

    @Override void handleRedirect(@NotNull RedirectResult r) {
        redirect = r;
    }

    @Override void handleResponseHeaders(@NotNull Headers h) {}

    @Override void handleResponseStatus(@NotNull Status s) {
        response.setStatus(s);
    }

    Headers getHeaders() {
        return response.getHeaders();
    }

    @Nullable RedirectResult getRedirect() {
        return redirect;
    }

    @NotNull MockServerResponse getResponse() {
        return response;
    }

    Status getStatus() {
        return response.getStatus();
    }
}  // end class MockResultHandler
