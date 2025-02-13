
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.admin.status;

/**
 * Database connections info.
 */
public class DatabaseInfo {

    //~ Instance Fields ..............................................................................................................................

    private int active;
    private int idle;
    private int total;
    private int waiting;

    //~ Constructors .................................................................................................................................

    /** Empty constructor. */
    public DatabaseInfo() {}

    /** Constructor with values. */
    public DatabaseInfo(int total, int active, int idle, int waiting) {
        this.total   = total;
        this.active  = active;
        this.idle    = idle;
        this.waiting = waiting;
    }

    //~ Methods ......................................................................................................................................

    /** Active connections. */
    public int getActive() {
        return active;
    }

    /** Idle connections. */
    public int getIdle() {
        return idle;
    }
    /** Total connections. */
    public int getTotal() {
        return total;
    }

    /** Waiting threads for connections. */
    public int getWaiting() {
        return waiting;
    }
}  // end class DatabaseInfo
