
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.cache;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import tekgenesis.persistence.CacheStoreHandler;
import tekgenesis.transaction.TransactionManager;
import tekgenesis.transaction.TransactionResource;

/**
 * Base class for CacheManagers.
 */
public abstract class CacheManager extends TransactionResource.Default<CacheConnection> {

    //~ Instance Fields ..............................................................................................................................

    protected final Map<String, CacheStoreHandler<?, ?>> handlers;

    //~ Constructors .................................................................................................................................

    protected CacheManager(final String name, final TransactionManager tm) {
        super(name, tm);
        handlers = new HashMap<>();
    }

    //~ Methods ......................................................................................................................................

    /** Ensure that the transaction is started. */
    public void ensureTransactionStarted() {
        getConnection(true).beginTransaction();
    }

    /** Invalidate all caches. */
    public void invalidateAllCaches() {
        for (final CacheStoreHandler<?, ?> handler : handlers.values())
            handler.invalidateCache();
    }

    /** invalidate cache. */
    public void invalidateCache(String mapName) {
        final CacheStoreHandler<?, ?> handler = handlers.get(mapName);
        if (handler == null) clearNamedCache(mapName);
        else handler.invalidateCache();
    }

    protected abstract void clearNamedCache(String mapName);

    Collection<CacheStoreHandler<?, ?>> getHandlers() {
        return handlers.values();
    }
}
