
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.cluster.jmx.util;

import java.io.Serializable;

/**
 * Memory Info.
 */
public class MemoryInfo implements Serializable {

    //~ Instance Fields ..............................................................................................................................

    private long committed;
    private long init;
    private long max  = -1;
    private long used;

    //~ Methods ......................................................................................................................................

    /** committed.* */
    public long getCommitted() {
        return committed;
    }

    /**  */
    public void setCommitted(long committed) {
        this.committed = committed;
    }

    /** Init value.* */
    public long getInit() {
        return init;
    }

    /**  */
    public void setInit(long init) {
        this.init = init;
    }

    /** Max.* */
    public long getMax() {
        return max;
    }

    /**  */
    public void setMax(long max) {
        this.max = max;
    }

    /** Used.* */
    public long getUsed() {
        return used;
    }

    /**  */
    public void setUsed(long used) {
        this.used = used;
    }

    //~ Static Fields ................................................................................................................................

    private static final long serialVersionUID = 8918278101572863929L;
}  // end class MemoryInfo
