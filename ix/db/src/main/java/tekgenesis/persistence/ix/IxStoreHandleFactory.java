
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.persistence.ix;

import tekgenesis.persistence.*;

import static tekgenesis.common.Predefined.cast;

/**
 * The Factory for IxlStoreHandler.
 */
public class IxStoreHandleFactory implements StoreHandler.Factory {

    //~ Methods ......................................................................................................................................

    /** Create a store handler. */
    @Override public <I extends EntityInstance<I, K>, K> StoreHandler<I, K> createHandler(final String storeType, final DbTable<I, K> dbTable) {
        final DbTable<? extends IxInstance<?, K>, K>      ixEt    = cast(dbTable);
        final StoreHandler<? extends IxInstance<?, K>, K> handler = new IxStoreHandler<>(ixEt);
        return cast(handler);
    }
}
