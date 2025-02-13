
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
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;

import tekgenesis.common.logging.Logger;
import tekgenesis.websocket.server.http.HttpForwarderSocket;
import tekgenesis.websocket.util.WebSocketAdapter;

/**
 * Backend Socket.
 */
public class BackendSocket extends WebSocketAdapter {

    //~ Instance Fields ..............................................................................................................................

    private final Logger logger = Logger.getLogger(BackendSocket.class);

    private final AtomicLong receivedMessages = new AtomicLong();

    // Request buffer only for splitted messages
    private final Map<String, StringBuilder>    requestBuffers = new HashMap<>();
    private final AtomicLong                    sentMessages   = new AtomicLong();
    private final Map<String, WebSocketAdapter> sockets        = new HashMap<>();

    //~ Methods ......................................................................................................................................

    @Override public void onWebSocketText(String m) {
        String message = m;
        logger.debug(HttpForwarderSocket.RECEIVED_MESSAGE_ON_SERVER + message);

        receivedMessages.incrementAndGet();
        try {
            final int index     = message.indexOf(' ');
            String    routingId = message.substring(0, index);

            message = message.substring(index + 1);

            // Is is a message part ?
            final boolean messagePart    = routingId.contains("+");
            final boolean messagePartEnd = routingId.contains(".");

            if (messagePart || messagePartEnd) {
                StringBuilder buffer;
                final String  bufferKey = routingId.replace(".", "+");

                // Accumulate part in buffer
                synchronized (requestBuffers) {
                    buffer = requestBuffers.get(bufferKey);

                    if (buffer == null) {
                        buffer = new StringBuilder(message.length());
                        requestBuffers.put(bufferKey, buffer);
                    }

                    buffer.append(message);
                }

                if (messagePartEnd) {
                    synchronized (requestBuffers) {
                        requestBuffers.remove(bufferKey);
                    }

                    message = buffer.toString();
                }

                final int sep = routingId.indexOf(messagePart ? '+' : '.');
                routingId = routingId.substring(0, sep);
            }

            if (!messagePart) {
                final WebSocketAdapter socket = findSocket(routingId);

                if (socket != null) socket.send(message);
                else logger.debug("Dropped message for socket with routingId: [" + routingId + "]");
            }
        }
        catch (final IOException e) {
            e.printStackTrace(System.err);
        }
    }  // end method onWebSocketText

    /** register backend. */
    public void register(String routingId, WebSocketAdapter socket) {
        synchronized (sockets) {
            sockets.put(routingId, socket);
        }
    }

    /** Send message. */
    public void send(String routingId, String message)
        throws IOException
    {
        super.send(routingId + " " + message);
        sentMessages.incrementAndGet();
    }

    /** unregister. */
    public void unregister(String routingId) {
        synchronized (sockets) {
            sockets.remove(routingId);
        }

        synchronized (requestBuffers) {
            // Remove pending buffers that failed to complete

            requestBuffers.entrySet().removeIf(entry -> entry.getKey().startsWith(routingId + "+"));
        }
    }

    /** Return active connections. */
    public int getActiveConnections() {
        return sockets.size();
    }

    /** Return buffer count. */
    public int getBufferCount() {
        return requestBuffers.size();
    }
    /** Return received messages. */
    public long getReceivedMessages() {
        return receivedMessages.get();
    }

    /** Return sent messages. */
    public long getSentMessages() {
        return sentMessages.get();
    }

    private WebSocketAdapter findSocket(String routingId) {
        synchronized (sockets) {
            return sockets.get(routingId);
        }
    }
}  // end class BackendSocket
