
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.view.client;

import org.jetbrains.annotations.Nullable;

import tekgenesis.view.shared.response.ResponseError;

/**
 * Listener to subscribe to a FormBox instance. Programmatically or by api.
 */
public interface FormBoxListener {

    //~ Methods ......................................................................................................................................

    /** Notifies when the box current form fires an error. */
    void onError(ResponseError error);

    /** Notifies when the box current url is set. */
    void onLoad(String url);

    /** Notifies when the box current form is set. */
    void onLoad(String fqn, @Nullable String pk, @Nullable String parameters);

    /** Notifies when the box current form is unloaded. */
    void onUnload();

    /** Notifies when the box current form is updated. */
    void onUpdate();
}
