
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.websocket.server.proxy;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import tekgenesis.common.logging.Logger;
import tekgenesis.websocket.util.WebSocketAdapter;

/**
 * Proxy socket.
 */
public class ProxySocket extends WebSocketAdapter {

    //~ Instance Fields ..............................................................................................................................

    private final String                  id;
    private final Logger                  logger      = Logger.getLogger(ProxySocket.class);
    private final ConnectedBackendSockets sockets;
    private final Set<BackendSocket>      usedSockets = new HashSet<>();

    //~ Constructors .................................................................................................................................

    /** Constructor. */
    public ProxySocket(ConnectedBackendSockets sockets) {
        this.sockets = sockets;
        id           = UUID.randomUUID().toString().substring(0, END_INDEX);
    }

    //~ Methods ......................................................................................................................................

    @Override public void onWebSocketClose(int statusCode, String reason) {
        super.onWebSocketClose(statusCode, reason);

        for (final BackendSocket usedSocket : usedSockets)
            usedSocket.unregister(getId());
    }

    @Override public void onWebSocketText(String message) {
        logger.debug("Received message on server from client (sending to backend): " + message);

        for (int i = 0; i < MAX_RETRY; i++) {
            try {
                final BackendSocket backendSocket = sockets.pickRandom();
                final boolean       added         = usedSockets.add(backendSocket);

                if (added) backendSocket.register(getId(), this);

                backendSocket.send(id, message);
                break;
            }
            catch (final Exception e) {
                try {
                    session.getRemote().sendString("Service not available: " + e.getMessage());
                }
                catch (final IOException e1) {
                    e1.printStackTrace(System.err);
                }
            }
        }
    }

    /** Return id. */
    public String getId() {
        return id;
    }

    //~ Static Fields ................................................................................................................................

    public static final int END_INDEX = 12;

    private static final int MAX_RETRY = 3;
}  // end class ProxySocket
