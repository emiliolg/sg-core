
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.websocket.server.proxy;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

import javax.servlet.AsyncContext;
import javax.servlet.AsyncEvent;
import javax.servlet.AsyncListener;
import javax.servlet.ServletException;
import javax.servlet.ServletInputStream;
import javax.servlet.UnavailableException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.bind.DatatypeConverter;

import tekgenesis.common.logging.Logger;
import tekgenesis.common.util.Files;
import tekgenesis.websocket.server.http.protocol.HttpRequest;
import tekgenesis.websocket.server.http.protocol.HttpResponse;
import tekgenesis.websocket.server.http.protocol.JsonProtocol;
import tekgenesis.websocket.util.WebSocketAdapter;

import static tekgenesis.common.core.Constants.KILO;
import static tekgenesis.websocket.server.http.HttpForwarderSocket.UTF8;

/**
 */
public class HttpServiceServlet extends HttpServlet {

    //~ Instance Fields ..............................................................................................................................

    private final WebSocketAdapter                        callback;
    private final ConcurrentHashMap<String, AsyncContext> pendingRequests = new ConcurrentHashMap<>();
    private final JsonProtocol                            protocol        = new JsonProtocol();
    private final String                                  routingId       = String.valueOf(new Random().nextInt(Integer.MAX_VALUE));

    //~ Constructors .................................................................................................................................

    /**  */
    public HttpServiceServlet() {
        callback = new WebSocketAdapter() {
                @Override public synchronized void send(String message) {
                    onMessage(message);
                }
            };
        // noinspection AssignmentToStaticFieldFromInstanceMethod
        INSTANCE = this;
    }

    //~ Methods ......................................................................................................................................

    public void doGet(HttpServletRequest request, HttpServletResponse response)
        throws UnavailableException, IOException
    {
        doOperation(request, response);
    }  // end method doGet

    /** Return pending requests. */
    public ConcurrentHashMap<String, AsyncContext> getPendingRequests() {
        return pendingRequests;
    }

    @Override protected void doPost(HttpServletRequest request, HttpServletResponse resp)
        throws ServletException, IOException
    {
        doOperation(request, resp);
    }

    @Override protected void doPut(HttpServletRequest request, HttpServletResponse resp)
        throws ServletException, IOException
    {
        doOperation(request, resp);
    }

    private void doOperation(final HttpServletRequest request, HttpServletResponse response)
        throws IOException
    {
        final String requestId = String.valueOf(new Random().nextInt(Integer.MAX_VALUE));

        // Start async response

        final AsyncContext asyncContext = request.startAsync(request, response);
        asyncContext.setTimeout(TIMEOUT);
        pendingRequests.put(requestId, asyncContext);

        // Remove from pendingRequests when completed
        asyncContext.addListener(new AsyncListener() {
                public void onComplete(AsyncEvent event) {
                    pendingRequests.remove(requestId);
                }

                public void onTimeout(AsyncEvent event) {
                    logger.error("Timeout event for requestId: " + requestId);
                    final AsyncContext        context         = pendingRequests.remove(requestId);
                    final HttpServletResponse servletResponse = (HttpServletResponse) event.getAsyncContext().getResponse();

                    // Copy status
                    try {
                        servletResponse.sendError(HttpServletResponse.SC_GATEWAY_TIMEOUT, "Timeout forwarding: " + request.getRequestURI());
                    }
                    catch (final IOException e) {
                        // ignore
                    }
                    if (context != null) context.complete();
                }

                public void onError(AsyncEvent event) {
                    final AsyncContext        context         = pendingRequests.remove(requestId);
                    final HttpServletResponse servletResponse = (HttpServletResponse) event.getAsyncContext().getResponse();

                    // Copy status
                    try {
                        // noinspection ThrowableResultOfMethodCallIgnored
                        servletResponse.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR,
                            "Error forwarding: " + request.getRequestURI() + " " + event.getThrowable());
                    }
                    catch (final IOException e) {
                        // ignore
                    }
                    if (context != null) context.complete();
                    final Throwable throwable = event.getThrowable();
                    if (throwable != null) logger.error(throwable);
                }

                public void onStartAsync(AsyncEvent event) {}
            });

        // Forward request through webSocket
        String requestURI = request.getRequestURI();

        if (request.getQueryString() != null) requestURI += "?" + request.getQueryString();

        // Remove prefix
        assert requestURI.startsWith(DEFAULT_PREFIX) : requestURI;
        final String                          partialURI  = requestURI.substring(DEFAULT_PREFIX.length());
        final HttpRequest                     httpRequest = new HttpRequest(routingId, requestId, request.getMethod(), partialURI);
        final Map<String, Collection<String>> headers     = httpRequest.getHeaders();

        final ServletInputStream stream = request.getInputStream();
        if (stream.available() > 0) {
            final String body = Files.readInput(new BufferedReader(new InputStreamReader(stream, UTF8), 4 * KILO));
            Files.close(stream);
            httpRequest.setBody(body);
        }
        final Enumeration<String> names = request.getHeaderNames();

        while (names.hasMoreElements()) {
            final String              name   = names.nextElement();
            final Enumeration<String> values = request.getHeaders(name);
            headers.put(name, Collections.list(values));
        }

        try {
            final BackendSocket socket = ConnectedBackendSockets.getInstance().pickRandom();
            socket.register(routingId, callback);
            socket.send(routingId, protocol.write(httpRequest));
        }
        catch (final UnavailableException e) {
            response.sendError(UNAVAILABLE, e.getMessage());
            pendingRequests.remove(requestId);
        }
    }  // end method doOperation

    private void onMessage(String message) {
        try {
            final HttpResponse response = protocol.read(message, HttpResponse.class);

            final String       id           = response.getId();
            final AsyncContext asyncContext = pendingRequests.get(id);

            if (asyncContext != null) {
                final HttpServletResponse servletResponse = (HttpServletResponse) asyncContext.getResponse();

                if (servletResponse.getStatus() >= ERROR_5XX)
                    logger.warning("Ups. Servlet already failed with status " + servletResponse.getStatus());
                else {
                    // Copy status
                    servletResponse.setStatus(response.getStatus());

                    // Copy headers to response
                    final Map<String, List<String>> headers = response.getHeaders();

                    for (final Map.Entry<String, List<String>> entry : headers.entrySet()) {
                        final String       key   = entry.getKey();
                        final List<String> value = entry.getValue();

                        servletResponse.setHeader(key, value.get(0));
                    }

                    final String body = response.getBody();

                    if (response.isText()) servletResponse.getWriter().write(body);
                    else servletResponse.getOutputStream().write(DatatypeConverter.parseBase64Binary(body));
                }

                asyncContext.complete();
            }
        }
        catch (final IOException e) {
            System.out.println("Cannot complete request: " + e);
        }
    }  // end method onMessage

    //~ Static Fields ................................................................................................................................

    private static final int ERROR_5XX = 500;

    private static final long TIMEOUT = 270000L;

    private static final Logger logger = Logger.getLogger(HttpServiceServlet.class);

    private static final int UNAVAILABLE = 503;

    public static HttpServiceServlet INSTANCE = null;

    private static final long serialVersionUID = 8850362709469660294L;

    public static final String DEFAULT_PREFIX = "/http";
}  // end class HttpServiceServlet
