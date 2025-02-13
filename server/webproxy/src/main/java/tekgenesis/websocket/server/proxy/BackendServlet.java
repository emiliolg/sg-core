
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

/**
 */
@WebServlet(name        = "Proxy Servlet", urlPatterns = BackendServlet.BACKEND)
public class BackendServlet extends WebSocketServlet {

    //~ Methods ......................................................................................................................................

    @Override public void configure(WebSocketServletFactory factory) {
        factory.getPolicy().setIdleTimeout(IDLE_TIMEOUT);

        factory.setCreator((req, resp) -> {
            try {
                final BackendSocket socket = new BackendSocket();
                ConnectedBackendSockets.getInstance().register(socket);

                return socket;
            }
            catch (final Exception e) {
                e.printStackTrace(System.err);
                throw new RuntimeException(e);
            }
        });
    }

    //~ Static Fields ................................................................................................................................

    private static final long  serialVersionUID = -4269556005274108726L;
    public static final int    IDLE_TIMEOUT     = 30000;
    public static final String BACKEND          = "/backend";
    public static final String ROOT             = "/";
}
