
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.websocket.server.proxy;

import java.util.*;

import javax.servlet.UnavailableException;

import static java.lang.System.out;

/**
 * Contains (and manages) a list of connected back-ends.
 */
public class ConnectedBackendSockets {

    //~ Instance Fields ..............................................................................................................................

    private final List<BackendSocket> sockets = new ArrayList<>();
    private final Timer               timer   = new Timer();

    //~ Methods ......................................................................................................................................

    /** Picks a random backend. */
    public BackendSocket pickRandom()
        throws UnavailableException
    {
        synchronized (sockets) {
            if (sockets.isEmpty()) throw new UnavailableException("No backend services (backendSockets: " + toString() + ")");
            final int index = new Random().nextInt(sockets.size());
            return sockets.get(index);
        }
    }

    /** Registers a new back-end socket. It is automatically removed if connection is lost; */
    public BackendSocket register(final BackendSocket socket) {
        synchronized (sockets) {
            sockets.add(socket);
        }

        final TimerTask task = new TimerTask() {
                @Override public void run() {
                    try {
                        socket.sendPing();
                    }
                    catch (final Exception e) {
                        out.println("[ConnectedBackendSockets] Ping failed, removing socket and cancelling timer.");
                        remove(socket);
                        cancel();
                    }
                }
            };

        timer.schedule(task, DELAY, PERIOD);

        out.println("[ConnectedBackendSockets] New backend added: " + socket + COUNT + sockets.size() + "]");
        return socket;
    }

    @Override public String toString() {
        final ClassLoader classLoader = getClass().getClassLoader();
        return "ConnectedBackendSockets(hash:" + hashCode() + ", classLoader.hash: " + classLoader.hashCode() + ", sockets.hash: " +
               System.identityHashCode(sockets) + ")";
    }

    /** Return all sockets. */
    public List<BackendSocket> getAll() {
        synchronized (sockets) {
            return new ArrayList<>(sockets);
        }
    }

    /** Removes a back-end socket. */
    private void remove(BackendSocket socket) {
        synchronized (sockets) {
            sockets.remove(socket);
        }

        out.println("[ConnectedBackendSockets] Backend removed: " + socket + COUNT + sockets.size() + "]");
    }

    //~ Methods ......................................................................................................................................

    /** Return instance. */
    public static ConnectedBackendSockets getInstance() {
        return instance;
    }

    //~ Static Fields ................................................................................................................................

    public static final int    DELAY  = 5000;
    public static final int    PERIOD = 5000;
    public static final String COUNT  = " [count: ";

    private static final ConnectedBackendSockets instance = new ConnectedBackendSockets();
}  // end class ConnectedBackendSockets
