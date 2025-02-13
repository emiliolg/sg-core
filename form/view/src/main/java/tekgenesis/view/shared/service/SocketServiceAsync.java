
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.view.shared.service;

import com.google.gwt.user.client.rpc.AsyncCallback;

import org.jetbrains.annotations.NotNull;

import tekgenesis.serializer.GwtSerializationWhiteList;
import tekgenesis.view.shared.response.FormModelResponse;
import tekgenesis.view.shared.response.Response;

/**
 * New service to be replaced by WebSockets one day.
 */
public interface SocketServiceAsync {

    //~ Methods ......................................................................................................................................

    /** Hack to add classes to the GWT serialization white list. */
    void _whiteList(GwtSerializationWhiteList o, AsyncCallback<GwtSerializationWhiteList> async);

    /** Syncs the form model after scheduled interval is triggered. */
    void onScheduleSync(final FormModelResponse model, @NotNull final AsyncCallback<Response<FormModelResponse>> async);
}
