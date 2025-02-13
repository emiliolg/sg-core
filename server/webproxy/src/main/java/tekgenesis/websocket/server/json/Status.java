
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
import java.util.ArrayList;
import java.util.List;

import org.jetbrains.annotations.NotNull;

/**
 * Status class for json.
 */
public class Status implements Serializable {

    //~ Instance Fields ..............................................................................................................................

    @NotNull private List<Backend> backends = new ArrayList<>();

    @NotNull private List<PendingRequest> pendingRequests = new ArrayList<>();

    //~ Methods ......................................................................................................................................

    /** Returns the Backends. */
    @NotNull public List<Backend> getBackends() {
        return backends;
    }

    /** Sets the value of the Backends. */
    public void setBackends(@NotNull List<Backend> backends) {
        this.backends = backends;
    }

    /** Returns the Pending Requests. */
    @NotNull public List<PendingRequest> getPendingRequests() {
        return pendingRequests;
    }

    /** Sets the value of the Pending Requests. */
    public void setPendingRequests(@NotNull List<PendingRequest> pendingRequests) {
        this.pendingRequests = pendingRequests;
    }

    //~ Static Fields ................................................................................................................................

    private static final long serialVersionUID = 1449310910991872227L;
}
