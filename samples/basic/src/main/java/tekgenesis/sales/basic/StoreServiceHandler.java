
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.sales.basic;

import org.jetbrains.annotations.NotNull;

import tekgenesis.common.collections.Seq;
import tekgenesis.service.Factory;
import tekgenesis.service.Result;

import static tekgenesis.persistence.Sql.selectFrom;
import static tekgenesis.sales.basic.g.StoreTable.STORE;

/**
 * User class for Handler: StoreHandler
 */
public class StoreServiceHandler extends StoreServiceHandlerBase {

    //~ Constructors .................................................................................................................................

    StoreServiceHandler(@NotNull Factory factory) {
        super(factory);
    }

    //~ Methods ......................................................................................................................................

    // #getKey
    @NotNull @Override public Result<StoreType> get(@NotNull String key) {
        final Store store = Store.find(key);
        if (store == null) return notFound();
        return ok(toStoreType(store));
    }
    // #getKey

    //~ Methods ......................................................................................................................................

    @NotNull static Seq<StoreType> stores() {
        return selectFrom(STORE).map(StoreServiceHandler::toStoreType).toList();
    }

    // #toStoreType
    @NotNull private static StoreType toStoreType(Store s) {
        return new StoreType(s.getName(), s.getAddress(), s.getLat(), s.getLng());
    }
    // #toStoreType
}
