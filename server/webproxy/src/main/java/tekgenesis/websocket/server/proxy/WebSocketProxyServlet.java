
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.websocket.server.proxy;

import javax.servlet.annotation.WebServlet;

import org.eclipse.jetty.websocket.servlet.*;
import org.jetbrains.annotations.NotNull;

import static java.lang.System.out;

/**
 */
@WebServlet(name        = "WS Proxy Servlet", urlPatterns = "/ws")
public class WebSocketProxyServlet extends WebSocketServlet {

    //~ Methods ......................................................................................................................................

    @Override public void configure(WebSocketServletFactory factory) {
        factory.getPolicy().setIdleTimeout(IDLE_TIMEOUT);
        factory.setCreator((req, resp) -> createProxy());
    }

    @NotNull private Object createProxy() {
        try {
            return new ProxySocket(ConnectedBackendSockets.getInstance());
        }
        catch (final Exception e) {
            out.println("Service not available...");
            throw new RuntimeException(e);
        }
    }

    //~ Static Fields ................................................................................................................................

    private static final long serialVersionUID = 394054061406826318L;
    public static final int   IDLE_TIMEOUT     = 100000;
}
