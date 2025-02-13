
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.cache;

import tekgenesis.database.support.DummyConnection;
import tekgenesis.persistence.CacheStoreHandler;

import static tekgenesis.common.collections.Colls.toList;

/**
 * Infinispan Connection.
 */
public class CacheConnection extends DummyConnection {

    //~ Instance Fields ..............................................................................................................................

    private final CacheManager cacheManager;

    @SuppressWarnings("BooleanVariableAlwaysNegated")
    private boolean txStarted = false;

    //~ Constructors .................................................................................................................................

    /** Constructor with manager. */
    public CacheConnection(CacheManager cacheManager) {
        this.cacheManager = cacheManager;
    }

    //~ Methods ......................................................................................................................................

    /** Begin transaction. */
    public void beginTransaction() {
        if (!txStarted) {
            toList(cacheManager.getHandlers()).forEach(CacheStoreHandler::cleanEviction);
            txStarted = true;
        }
    }

    @Override public void close() {
        toList(cacheManager.getHandlers()).forEach(CacheStoreHandler::cleanEviction);
        txStarted = false;
    }

    @Override public void commit() {
        toList(cacheManager.getHandlers()).forEach(CacheStoreHandler::evictPending);
    }

    @Override public void rollback() {
        toList(cacheManager.getHandlers()).forEach(CacheStoreHandler::evictPending);
    }

    @Override public boolean isClosed() {
        return !txStarted;
    }
}  // end class CacheConnection
