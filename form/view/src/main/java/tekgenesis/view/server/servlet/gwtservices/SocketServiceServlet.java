
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.view.server.servlet.gwtservices;

import javax.servlet.http.HttpServletRequest;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;

import org.jetbrains.annotations.NotNull;

import tekgenesis.form.Action;
import tekgenesis.metadata.form.model.FormModel;
import tekgenesis.serializer.GwtSerializationWhiteList;
import tekgenesis.view.shared.response.FormModelResponse;
import tekgenesis.view.shared.response.Response;
import tekgenesis.view.shared.service.SocketService;

import static tekgenesis.common.core.Constants.NEWRELIC_AGENT_TRANSACTION_NAME;
import static tekgenesis.form.exprs.ServerExpressions.execOnSchedule;
import static tekgenesis.transaction.Transaction.invokeInTransaction;
import static tekgenesis.view.server.servlet.gwtservices.FormServiceServlet.builderSync;
import static tekgenesis.view.server.servlet.gwtservices.FormServiceServletProxy.doInvoke;

/**
 * Socket Service servlet.
 */
public class SocketServiceServlet extends RemoteServiceServlet implements SocketService {

    //~ Methods ......................................................................................................................................

    @Override public GwtSerializationWhiteList _whiteList(GwtSerializationWhiteList o) {
        return o;
    }

    @Override public Response<FormModelResponse> onScheduleSync(@NotNull final FormModelResponse response) {
        return doInvoke(() ->
                builderSync(new FormMethod() {
                        @Override public Action apply(@NotNull FormModel model) {
                            setTransactionName(model.getFormFullName(), "onSchedule");
                            return invokeInTransaction(() -> execOnSchedule(model));
                        }
                    }, response).build());
    }

    private void setTransactionName(String formFullName, String method) {
        final HttpServletRequest request = getThreadLocalRequest();
        if (request != null) request.setAttribute(NEWRELIC_AGENT_TRANSACTION_NAME, formFullName + "/" + method);
    }

    //~ Static Fields ................................................................................................................................

    private static final long serialVersionUID = 552531656046052555L;
}
