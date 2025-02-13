
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.view.client.service;

import tekgenesis.view.client.FormBox;
import tekgenesis.view.shared.response.FormModelResponse;
import tekgenesis.view.shared.service.SocketService;
import tekgenesis.view.shared.service.SocketServiceAsync;

/**
 * Wraps the SocketService to add feedback.
 */
public class ClientSocketService {

    //~ Instance Fields ..............................................................................................................................

    private final FormBox formBox;

    private final SocketServiceAsync service;

    //~ Constructors .................................................................................................................................

    /** Creates a ClientSocketService. */
    public ClientSocketService(FormBox formBox) {
        service      = SocketService.App.getInstance();
        this.formBox = formBox;
    }

    //~ Methods ......................................................................................................................................

    /** Syncs the form model after schedule interval is triggered. */
    public void onScheduleSync(FormModelResponse model, final DefaultCallback<FormModelResponse> callback) {
        service.onScheduleSync(model, new AsyncCallbackImpl(callback, formBox, false));
    }
}
