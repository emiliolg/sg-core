
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.persistence.sql;

import org.jetbrains.annotations.NotNull;

import tekgenesis.database.DatabaseFactory;
import tekgenesis.persistence.DbTable;
import tekgenesis.persistence.EntityInstance;
import tekgenesis.persistence.StoreHandler;
import tekgenesis.transaction.TransactionResource;

/**
 * The Factory for QueryDslStoreHandler.
 */
public class SqlStoreHandlerFactory extends TransactionResource.Default<StatementCache> implements StoreHandler.Factory {

    //~ Instance Fields ..............................................................................................................................

    @NotNull private final DatabaseFactory<?> databaseFactory;

    //~ Constructors .................................................................................................................................

    /** Create a Factory. */
    public SqlStoreHandlerFactory(@NotNull DatabaseFactory<?> databaseFactory) {
        super("SQL", databaseFactory.getTransactionManager());
        this.databaseFactory = databaseFactory;
    }

    //~ Methods ......................................................................................................................................

    @NotNull @Override
    @SuppressWarnings("ClassEscapesDefinedScope")
    public StatementCache createConnection() {
        return new StatementCache(databaseFactory.getTransactionManager());
    }

    @Override public <I extends EntityInstance<I, K>, K> StoreHandler<I, K> createHandler(final String storeType, final DbTable<I, K> dbTable) {
        return new SqlStoreHandler<>(this, databaseFactory.forSchema(dbTable.metadata().getSchemaName()), dbTable);
    }

    @Override public void endBatch(final StatementCache statementCache) {
        statementCache.endBatch();
    }
}
