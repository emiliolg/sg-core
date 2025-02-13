
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.persistence.nomm.test;

import java.util.ArrayList;
import java.util.List;

import org.jetbrains.annotations.NotNull;

import tekgenesis.common.tools.test.DatabaseRule;
import tekgenesis.database.Database;
import tekgenesis.database.DatabaseFactory;
import tekgenesis.database.DatabaseType;
import tekgenesis.persistence.*;
import tekgenesis.persistence.sql.SqlStoreHandler;
import tekgenesis.persistence.sql.SqlStoreHandlerFactory;

import static tekgenesis.common.Predefined.ensureNotNull;
import static tekgenesis.common.tools.test.DbTests.loadSql;
import static tekgenesis.transaction.Transaction.runInTransaction;

/**
 * Testing of Database entities with no mm files.
 */
public abstract class NoMmDbRule extends DatabaseRule {

    //~ Instance Fields ..............................................................................................................................

    private String dbName = null;

    private final List<String> schemas = new ArrayList<>();

    //~ Methods ......................................................................................................................................

    @Override protected void after() {
        database.close();
        runInTransaction(() -> {
            openDatabase(dbName);
            final DatabaseType dbType = database.getDatabaseType();
            for (final String schema : schemas)
                dbType.dropSchema(database, schema, true);
        });
    }

    @Override protected final void createDatabase(@NotNull final String databaseName) {
        super.createDatabase(databaseName);
        TableFactory.setFactory(new TableFactory(new MyStoreHandlerFactory(dbFactory)));
        dbName = databaseName;

        final DatabaseType dbType = database.getDatabaseType();
        final Database     dbs    = database.asSystem();

        runInTransaction(() -> dbType.dropSchema(dbs, schemaName, true));
        runInTransaction(() -> dbType.dropSchema(dbs, "SG", true));
        runInTransaction(() -> dbType.createSchema(dbs, schemaName, ""));
        runInTransaction(() -> dbType.createSchema(dbs, "SG", ""));
        runInTransaction(() -> database.sqlStatement(ensureNotNull(loadSql(TEST_SQL))).executeScript());
        schemas.clear();
        schemas.add(schemaName);
        schemas.add("SG");
        schemas.add(TEST_SQL);
    }

    //~ Static Fields ................................................................................................................................

    @SuppressWarnings("DuplicateStringLiteralInspection")
    private static final String schemaName = "MODEL";
    @SuppressWarnings("DuplicateStringLiteralInspection")
    private static final String TEST_SQL = "db/current/TestDatabase.sql";

    //~ Inner Classes ................................................................................................................................

    class MyStoreHandlerFactory extends SqlStoreHandlerFactory {
        public MyStoreHandlerFactory(@NotNull final DatabaseFactory<?> databaseFactory) {
            super(databaseFactory);
        }

        @Override public <I extends EntityInstance<I, K>, K> StoreHandler<I, K> createHandler(final String storeType, final DbTable<I, K> dbTable) {
            return new SqlStoreHandler<>(this, database, dbTable);
        }
    }
}  // end class NoMmDbRule
