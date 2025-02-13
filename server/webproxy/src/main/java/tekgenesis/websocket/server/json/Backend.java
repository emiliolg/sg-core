
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.websocket.server.json;

import java.io.Serializable;

import org.jetbrains.annotations.NotNull;

/**
 * Backend for json.
 */
public class Backend implements Serializable {

    //~ Instance Fields ..............................................................................................................................

    private int connections = 0;

    @NotNull private String name = "";

    private long receivedMessages = 0;

    private int requiredBuffers = 0;

    private long sentMessages = 0;

    //~ Methods ......................................................................................................................................

    /** Returns the Connections. */
    public int getConnections() {
        return connections;
    }

    /** Sets the value of the Connections. */
    public void setConnections(int connections) {
        this.connections = connections;
    }

    /** Returns the Name. */
    @NotNull public String getName() {
        return name;
    }

    /** Sets the value of the Name. */
    public void setName(@NotNull String name) {
        this.name = name;
    }

    /** Returns the Received Messages. */
    public long getReceivedMessages() {
        return receivedMessages;
    }

    /** Sets the value of the Received Messages. */
    public void setReceivedMessages(long receivedMessages) {
        this.receivedMessages = receivedMessages;
    }

    /** Returns the Required Buffers. */
    public int getRequiredBuffers() {
        return requiredBuffers;
    }

    /** Sets the value of the Required Buffers. */
    public void setRequiredBuffers(int requiredBuffers) {
        this.requiredBuffers = requiredBuffers;
    }

    /** Returns the Sent Messages. */
    public long getSentMessages() {
        return sentMessages;
    }

    /** Sets the value of the Sent Messages. */
    public void setSentMessages(long sentMessages) {
        this.sentMessages = sentMessages;
    }

    //~ Static Fields ................................................................................................................................

    private static final long serialVersionUID = 1449310910991872227L;
}  // end class Backend
