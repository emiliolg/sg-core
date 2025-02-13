
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.view.client.service;

import java.io.Serializable;

import com.google.gwt.core.shared.SerializableThrowable;
import com.google.gwt.user.client.rpc.IncompatibleRemoteServiceException;

import org.jetbrains.annotations.NotNull;

import tekgenesis.common.logging.Logger;
import tekgenesis.view.client.Application;
import tekgenesis.view.client.FormBox;
import tekgenesis.view.shared.exception.GwtRPCSerializableException;
import tekgenesis.view.shared.response.Response;

import static tekgenesis.common.logging.Logger.getLogger;
import static tekgenesis.view.client.Application.messages;
import static tekgenesis.view.client.FormViewMessages.MSGS;

/**
 * Base Callback. It only centralized the 'onFailure' behaviour.
 */
abstract class BaseCallback<T extends Serializable> extends BaseAsyncCallback<Response<T>> {

    //~ Instance Fields ..............................................................................................................................

    final DefaultCallback<T> callback;
    final FormBox            formBox;
    private boolean          updateServerAccessTime = false;

    //~ Constructors .................................................................................................................................

    BaseCallback(final DefaultCallback<T> c, FormBox formBox, boolean update) {
        callback               = c;
        this.formBox           = formBox;
        updateServerAccessTime = update;
    }

    //~ Methods ......................................................................................................................................

    @Override public void onFailure(@NotNull final String message, @NotNull final Throwable caught) {
        if (caught instanceof IncompatibleRemoteServiceException) messages().info(message, formBox);
        else if (caught instanceof SerializableThrowable) {
            messages().info(message, formBox);
            logger.warning(message, caught);
        }
        else if (caught instanceof GwtRPCSerializableException) {
            messages().error(message, formBox);
            logger.warning(message, caught);
        }
        else {
            super.onFailure(message, caught);
            messages().error(MSGS.oops() + " " + message, formBox);
        }
        callback.onFailure(caught);
    }

    @Override public void onSuccess(Response<T> result) {
        if (updateServerAccessTime) Application.getInstance().updateLastKnownServerConnection();
    }

    //~ Static Fields ................................................................................................................................

    private static final Logger logger = getLogger(BaseCallback.class);
}
