
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.websocket.server;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import org.eclipse.jetty.server.*;
import org.eclipse.jetty.servlet.ServletHandler;

import tekgenesis.websocket.server.proxy.BackendServlet;
import tekgenesis.websocket.server.proxy.HttpServiceServlet;
import tekgenesis.websocket.server.proxy.WebSocketProxyServlet;

/**
 * Traffic exchange server.
 */
public class TrafficExchangeServer {

    //~ Instance Fields ..............................................................................................................................

    private final ServletHandler handler;
    private final int            port;

    private final Server server;

    //~ Constructors .................................................................................................................................

    /** Constructor with port. */
    public TrafficExchangeServer(int port) {
        this.port = port;
        server    = new Server();
        final HttpConfiguration http_config = new HttpConfiguration();
        final ServerConnector   http        = new ServerConnector(server, new HttpConnectionFactory(http_config));
        http.setPort(port);
        http.setIdleTimeout(IDLE_TIMEOUT);
        server.setConnectors(new Connector[] { http });

        handler = new ServletHandler();
        server.setHandler(handler);

        // Back-ends (WebSocket output)
        handler.addServletWithMapping(BackendServlet.class, BackendServlet.BACKEND);
        handler.addServletWithMapping(StatusServlet.class, BackendServlet.ROOT);
    }

    //~ Methods ......................................................................................................................................

    /** start. */
    public void start()
        throws Exception
    {
        server.start();
        server.join();

        System.out.println("Starting server on port" + port);
    }

    private void enableHttpInput() {
        handler.addServletWithMapping(HttpServiceServlet.class, "/http/*");
    }

    private void enableWebSocketInput() {
        handler.addServletWithMapping(WebSocketProxyServlet.class, "/ws");
    }

    //~ Methods ......................................................................................................................................

    /** Main method. */
    public static void main(String[] args)
        throws Exception
    {
        final List<String> argsList = Arrays.asList(args);
        main(argsList);
    }

    /** Main method. */
    static void main(List<String> argsList)
        throws Exception
    {
        int port = PORT;

        // Process parameters
        final Iterator<String> it = argsList.iterator();

        while (it.hasNext()) {
            final String param = it.next();
            if ("--port".equals(param)) port = Integer.parseInt(it.next());
            else {
                System.err.println("Invalid parameter: " + param);
                System.exit(1);
            }
        }

        final TrafficExchangeServer server = new TrafficExchangeServer(port);

        // Input
        server.enableWebSocketInput();
        server.enableHttpInput();

        server.start();
    }  // end method main

    //~ Static Fields ................................................................................................................................

    public static final int IDLE_TIMEOUT = 300000;

    public static final int PORT = 9090;
}
