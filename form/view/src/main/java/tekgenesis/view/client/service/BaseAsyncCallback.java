
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.view.client.service;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.rpc.IncompatibleRemoteServiceException;
import com.google.gwt.user.client.rpc.InvocationException;
import com.google.gwt.user.client.rpc.StatusCodeException;

import org.jetbrains.annotations.NotNull;

import tekgenesis.common.logging.Logger;
import tekgenesis.view.shared.exception.GwtRPCSerializableException;

import static tekgenesis.common.Predefined.notEmpty;
import static tekgenesis.common.Predefined.notNull;
import static tekgenesis.common.logging.Logger.getLogger;
import static tekgenesis.view.client.FormViewMessages.MSGS;

/**
 * Async callback base class.
 */
public abstract class BaseAsyncCallback<T> implements AsyncCallback<T> {

    //~ Methods ......................................................................................................................................

    @Override public final void onFailure(Throwable caught) {
        onFailure(resolveMessage(caught), caught);
    }

    /** Handle unexpected errors. */
    public void onFailure(@NotNull final String msg, @NotNull final Throwable caught) {
        logger.error(msg, caught);
    }

    //~ Methods ......................................................................................................................................

    static String resolveMessage(Throwable caught) {
        final String caughtMessage = notNull(caught.getMessage());

        // this client is not compatible with the server; cleanup and refresh the browser
        if (caught instanceof IncompatibleRemoteServiceException) return MSGS.appOutOfDate();

        // the call didn't complete cleanly, but we have status code.
        if (caught instanceof StatusCodeException) return MSGS.errorWithStatusCode(notEmpty(caughtMessage, "N/A"));

        // the call didn't complete cleanly
        if (caught instanceof InvocationException) return MSGS.errorReachingServer(caughtMessage);

        // the call end up on an exception in a Filter or something...
        if (caught instanceof GwtRPCSerializableException) return MSGS.oops() + " " + caughtMessage;

        // last resort -- a very unexpected exception
        return MSGS.unexpectedError(caughtMessage);
    }

    //~ Static Fields ................................................................................................................................

    private static final Logger logger = getLogger(BaseAsyncCallback.class);
}
