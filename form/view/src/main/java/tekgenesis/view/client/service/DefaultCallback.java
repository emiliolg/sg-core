
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

import tekgenesis.view.shared.response.ResponseError;

/**
 * Default callback.
 */
public interface DefaultCallback<T> extends AsyncCallback<T> {

    //~ Methods ......................................................................................................................................

    /** On Response error (server side). */
    void onError(final ResponseError error);

    /** On Response exception (server side). */
    void onException();
    @Override default void onFailure(final Throwable caught) {
        // use default error handling by default
    }

    /** Called when errorDialog login dialog is closed. */
    void onLoginClosed();
}
