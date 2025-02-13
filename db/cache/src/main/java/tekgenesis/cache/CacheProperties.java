
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.cache;

import javax.inject.Named;

import tekgenesis.common.env.Mutable;
import tekgenesis.common.env.Properties;

/**
 * Cache Map Override properties. Null values mean DO NOT override
 */
@Mutable
@Named("cacheOverride")
@SuppressWarnings("WeakerAccess")
public class CacheProperties implements Properties {

    //~ Instance Fields ..............................................................................................................................

    /** Disable caching. */
    public boolean disable = false;
    /** Use full cache. Instances are preloaded and never looked up in the database afterwards */
    public Boolean full = null;
    /** Seconds an instance will be in the cache. -1 means infinite. 0 means do not use default */
    public Integer lifespan = null;
    /**
     * Max idle time in seconds an instance will be in the cache. -1 means infinite , 0 means do not
     * override
     */
    public Integer maxIdle = null;
    /** Whether to preload cache with every instance or not. */
    public Boolean preload = null;
    /** Max cache size. */
    public Integer size = null;

    //~ Methods ......................................................................................................................................

    /** Returns a new cache type after applying the properties. */
    public CacheType applyProperties(final CacheType original) {
        if (disable) return CacheType.NONE;
        if (full == Boolean.TRUE) return CacheType.FULL;

        CacheType overridden = size != null ? original.withSize(size) : full != null && original.isFull() ? CacheType.DEFAULT : original;

        if (preload != null) overridden = overridden.withPreload(preload);
        if (lifespan != null) overridden = overridden.withLifespan(lifespan);
        if (maxIdle != null) overridden = overridden.withMaxIdle(maxIdle);
        return overridden;
    }
}
