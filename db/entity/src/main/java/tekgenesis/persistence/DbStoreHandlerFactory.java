
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.persistence;

import java.util.HashMap;
import java.util.Map;

/**
 * A Factory that delegates on different ones based on the StoreType.
 */
public class DbStoreHandlerFactory implements StoreHandler.Factory {

    //~ Instance Fields ..............................................................................................................................

    private final StoreHandler.Factory              defaultFactory;
    private final Map<String, StoreHandler.Factory> factoryMap;

    //~ Constructors .................................................................................................................................

    /** Create the Factory with a default one. */
    public DbStoreHandlerFactory(StoreHandler.Factory defaultFactory) {
        this.defaultFactory = defaultFactory;
        factoryMap          = new HashMap<>();
    }

    //~ Methods ......................................................................................................................................

    @Override public <I extends EntityInstance<I, K>, K> StoreHandler<I, K> createHandler(final String storeType, final DbTable<I, K> dbTable) {
        return findFactory(storeType).createHandler(storeType, dbTable);
    }

    /** Register a StoreHandler Factory. */
    public void register(String type, StoreHandler.Factory factory) {
        factoryMap.put(type, factory);
    }

    private StoreHandler.Factory findFactory(String storeType) {
        final StoreHandler.Factory f = factoryMap.get(storeType);
        return f == null ? defaultFactory : f;
    }
}
