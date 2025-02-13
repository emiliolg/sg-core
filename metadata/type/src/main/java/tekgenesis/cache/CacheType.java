
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.cache;

import tekgenesis.common.core.Times;

/**
 * Specification of the cache type.
 */
public class CacheType {

    //~ Instance Fields ..............................................................................................................................

    /** Lifespan in seconds. -1 means infinity */
    private final int     lifespan;
    private final boolean local;
    /** Max idle time in seconds. -1 means infinity */
    private final int maxIdle;
    /** Whether to preload the cache or not. */
    private final boolean preload;

    /** Cache has Replication. */
    private final boolean replicated;

    /** Number of instances in cache 0 means do not cache Integer.MAX_VALUE means FULL cache. */
    private final int size;
    /** Replication is synchronous. */
    private final boolean sync;

    //~ Constructors .................................................................................................................................

    private CacheType(int size) {
        this(size, false, -1, DEFAULT_MAX_IDLE, false, false, false);
    }

    @SuppressWarnings("ConstructorWithTooManyParameters")
    private CacheType(int size, boolean preload, int lifespan, int maxIdle, final boolean replicated, final boolean sync, final boolean local) {
        this.size       = size;
        this.preload    = preload;
        this.lifespan   = lifespan;
        this.maxIdle    = maxIdle;
        this.replicated = replicated;
        this.sync       = sync;
        this.local      = local;
    }

    //~ Methods ......................................................................................................................................

    /** Make cache local. */
    public CacheType local() {
        return local ? this : new CacheType(size, preload, lifespan, maxIdle, replicated, sync, true);
    }

    /** Preload the cache ? */
    public boolean preload() {
        return preload;
    }
    /** Modify replication mode. */
    public CacheType withAsyncReplication() {
        return replicated && !sync ? this : new CacheType(size, preload, lifespan, maxIdle, true, false, false);
    }

    /** Modify lifespan attribute. */
    public CacheType withLifespan(int n) {
        return n == lifespan ? this : new CacheType(size, preload, n, maxIdle, replicated, sync, local);
    }

    /** Modify maxidle attribute. */
    public CacheType withMaxIdle(int n) {
        return n == maxIdle ? this : new CacheType(size, preload, lifespan, n, replicated, sync, local);
    }

    /** Modify preload attribute. */
    public CacheType withPreload(boolean b) {
        return b == preload ? this : new CacheType(size, b, lifespan, maxIdle, replicated, sync, local);
    }

    /** Modify cache size. */
    public CacheType withSize(int sz) {
        return size == sz ? this : new CacheType(sz, preload, lifespan, maxIdle, replicated, sync, local);
    }

    /** Modify replication mode. */
    public CacheType withSyncReplication() {
        return replicated && sync ? this : new CacheType(size, preload, lifespan, maxIdle, true, true, false);
    }
    /** Returns true if the cache is Synchronous. */
    public boolean isSync() {
        return sync;
    }

    /** Returns true if the cache is defined. */
    public boolean isDefined() {
        return size > 0;
    }

    /** Returns true if the cache has replication. */
    public boolean isReplicated() {
        return replicated;
    }

    /** Returns true if the cache is full (contains all entries). */
    public boolean isFull() {
        return size == Integer.MAX_VALUE;
    }

    /** Returns true of the cache is a local one. */
    public boolean isLocal() {
        return local;
    }

    /** Get Lifespan in milliseconds. */
    public long getLifespan() {
        return lifespan == -1 ? lifespan : lifespan * Times.MILLIS_SECOND;
    }

    /** Get Max idle time in milliseconds. */
    public long getMaxIdle() {
        return isFull() ? -1 : maxIdle * Times.MILLIS_SECOND;
    }

    /** Get cache max size. */
    public int getSize() {
        return size;
    }

    //~ Static Fields ................................................................................................................................

    private static final int DEFAULT_MAX_IDLE = 5 * Times.SECONDS_MINUTE;

    private static final int      DEFAULT_SIZE = 1000;
    public static final CacheType DEFAULT      = new CacheType(DEFAULT_SIZE);
    public static final CacheType LOCAL        = DEFAULT.local();
    public static final CacheType NONE         = new CacheType(0);
    public static final CacheType FULL         = new CacheType(Integer.MAX_VALUE, true, -1, -1, false, false, false);
}  // end class CacheType
