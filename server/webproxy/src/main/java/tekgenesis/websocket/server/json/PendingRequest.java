
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
 * Pending requests for json status.
 */
public class PendingRequest implements Serializable {

    //~ Instance Fields ..............................................................................................................................

    @NotNull private String client = "";

    @NotNull private String method = "";

    @NotNull private String request = "";

    //~ Methods ......................................................................................................................................

    /** Returns the Client. */
    @NotNull public String getClient() {
        return client;
    }

    /** Sets the value of the Client. */
    public void setClient(@NotNull String client) {
        this.client = client;
    }

    /** Returns the Method. */
    @NotNull public String getMethod() {
        return method;
    }

    /** Sets the value of the Method. */
    public void setMethod(@NotNull String method) {
        this.method = method;
    }

    /** Returns the Request. */
    @NotNull public String getRequest() {
        return request;
    }

    /** Sets the value of the Request. */
    public void setRequest(@NotNull String request) {
        this.request = request;
    }

    //~ Static Fields ................................................................................................................................

    private static final long serialVersionUID = 1449310910991872227L;
}  // end class PendingRequest
