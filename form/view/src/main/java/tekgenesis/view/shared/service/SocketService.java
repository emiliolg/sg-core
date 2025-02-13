
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.view.shared.service;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

import org.jetbrains.annotations.NotNull;

import tekgenesis.serializer.GwtSerializationWhiteList;
import tekgenesis.view.shared.response.FormModelResponse;
import tekgenesis.view.shared.response.Response;

/**
 * New service to be replaced by WebSockets one day.
 */
@RemoteServiceRelativePath("socket")
public interface SocketService extends RemoteService {

    //~ Methods ......................................................................................................................................

    GwtSerializationWhiteList _whiteList(GwtSerializationWhiteList o);
    Response<FormModelResponse> onScheduleSync(@NotNull final FormModelResponse model);

    //~ Inner Classes ................................................................................................................................

    class App {
        private App() {}

        /** Returns the instance. */
        public static synchronized SocketServiceAsync getInstance() {
            return ourInstance;
        }

        private static final SocketServiceAsync ourInstance = GWT.create(SocketService.class);
    }
}
