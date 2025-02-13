
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.websocket.server;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import javax.servlet.AsyncContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.fasterxml.jackson.databind.ObjectMapper;

import tekgenesis.common.json.JsonMapping;
import tekgenesis.websocket.server.json.Backend;
import tekgenesis.websocket.server.json.PendingRequest;
import tekgenesis.websocket.server.json.Status;
import tekgenesis.websocket.server.proxy.BackendSocket;
import tekgenesis.websocket.server.proxy.ConnectedBackendSockets;
import tekgenesis.websocket.server.proxy.HttpServiceServlet;

/**
 * Status Servlet.
 */
public class StatusServlet extends HttpServlet {

    //~ Instance Fields ..............................................................................................................................

    private final ObjectMapper mapper = JsonMapping.shared();

    //~ Methods ......................................................................................................................................

    @Override protected void doGet(HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException
    {
        @SuppressWarnings("DuplicateStringLiteralInspection")
        final String  type       = request.getParameter("format");
        final boolean returnJson = "json".equals(type);

        final String result;
        if (returnJson) {
            response.setContentType("application/json");
            response.setStatus(HttpServletResponse.SC_OK);
            final Status status = new Status();
            backendServices(status);

            final HttpServiceServlet instance = HttpServiceServlet.INSTANCE;
            if (instance != null) {
                final ConcurrentHashMap<String, AsyncContext> requests = instance.getPendingRequests();
                pendingRequests(requests, status);
            }
            result = mapper.writeValueAsString(status);
        }
        else {
            response.setContentType("text/html");
            response.setStatus(HttpServletResponse.SC_OK);

            final StringBuilder builder = new StringBuilder();
            builder.append("<html><title>Status</title><body>");

            backendServices(builder);

            builder.append("</body></html>");

            final HttpServiceServlet instance = HttpServiceServlet.INSTANCE;
            if (instance != null) {
                final ConcurrentHashMap<String, AsyncContext> requests = instance.getPendingRequests();
                pendingRequests(requests, builder);
            }
            result = builder.toString();
        }

        response.getWriter().println(result);
    }  // end method doGet

    private void backendServices(Status status) {
        final List<Backend>           backends       = new ArrayList<>();
        final ConnectedBackendSockets backendSockets = ConnectedBackendSockets.getInstance();
        for (final BackendSocket backendSocket : backendSockets.getAll()) {
            final Backend backend = new Backend();
            backend.setConnections(backendSocket.getActiveConnections());
            backend.setName(backendSocket.toString());
            backend.setReceivedMessages(backendSocket.getReceivedMessages());
            backend.setRequiredBuffers(backendSocket.getBufferCount());
            backend.setSentMessages(backendSocket.getSentMessages());
            backends.add(backend);
        }
        status.setBackends(backends);
    }

    private void backendServices(StringBuilder builder) {
        final ConnectedBackendSockets backendSockets = ConnectedBackendSockets.getInstance();

        builder.append("<h3>Backend Servers</h3>");
        builder.append("<pre>Sockets: ").append(backendSockets).append("</pre>");

        builder.append("<table width='90%' border=1>\n");

        builder.append("<tr><th>Backend</th><th>Connections</th><th>Req. Buffers</th><th>Sent Msgs</th><th>Received Msgs</th></tr>");

        for (final BackendSocket socket : backendSockets.getAll()) {
            builder.append("<tr>");
            builder.append(td(socket.toString()));
            builder.append(td(socket.getActiveConnections()));
            builder.append(td(socket.getBufferCount()));
            builder.append(td(socket.getSentMessages()));
            builder.append(td(socket.getReceivedMessages()));
            builder.append("</tr>\n");
        }

        builder.append("</table>\n");
    }

    private void pendingRequests(ConcurrentHashMap<String, AsyncContext> requests, Status status) {
        final List<PendingRequest> pending = new ArrayList<>();
        for (final Map.Entry<String, AsyncContext> entry : requests.entrySet()) {
            final AsyncContext       value          = entry.getValue();
            final HttpServletRequest request        = (HttpServletRequest) value.getRequest();
            final PendingRequest     pendingRequest = new PendingRequest();
            pendingRequest.setClient(request.getRemoteHost());
            pendingRequest.setMethod(request.getMethod());

            final StringBuffer requestURL = request.getRequestURL();

            if (request.getQueryString() != null) requestURL.append("?").append(request.getQueryString());

            pendingRequest.setRequest(requestURL.toString());
            pending.add(pendingRequest);
        }

        status.setPendingRequests(pending);
    }

    private void pendingRequests(ConcurrentHashMap<String, AsyncContext> requests, StringBuilder builder) {
        builder.append("<h3>Pending Requests</h3>");

        builder.append("<table width='90%' border=1>\n");

        builder.append("<tr><th>Client</th><th>Method</th><th>Request</th></tr>");

        for (final Map.Entry<String, AsyncContext> entry : requests.entrySet()) {
            final AsyncContext       value   = entry.getValue();
            final HttpServletRequest request = (HttpServletRequest) value.getRequest();
            builder.append("<tr>");

            builder.append(td(request.getRemoteHost()));
            builder.append(td(request.getMethod()));
            final StringBuffer requestURL = request.getRequestURL();

            if (request.getQueryString() != null) requestURL.append("?").append(request.getQueryString());

            builder.append(td("<a href='" + requestURL.toString() + "'>" + requestURL.toString() + "</a>"));
            builder.append("</tr>");
        }
    }

    private String td(long value) {
        return td(String.valueOf(value));
    }

    private String td(String value) {
        return "<td>" + value + "</td>";
    }

    //~ Static Fields ................................................................................................................................

    private static final long serialVersionUID = 6756159419654059861L;
}  // end class StatusServlet
