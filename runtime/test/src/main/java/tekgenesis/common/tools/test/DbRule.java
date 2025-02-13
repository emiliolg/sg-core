
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.common.tools.test;

import java.io.Reader;

import org.jetbrains.annotations.NotNull;
import org.junit.rules.RuleChain;
import org.junit.rules.TestRule;

import tekgenesis.database.DatabaseType;
import tekgenesis.persistence.StoreHandler;
import tekgenesis.persistence.TableFactory;
import tekgenesis.properties.SchemaProps;
import tekgenesis.transaction.Transaction;

import static tekgenesis.common.tools.test.DbTests.loadSql;
import static tekgenesis.transaction.Transaction.runInTransaction;

/**
 * Testing of Database entities with no mm files.
 */
@SuppressWarnings("DuplicateStringLiteralInspection")
public abstract class DbRule extends DatabaseRule {

    //~ Instance Fields ..............................................................................................................................

    CounterHandlerFactory.Counter counters = null;

    private String dbName = null;

    private final SchemaSpec[] schemas;

    //~ Constructors .................................................................................................................................

    protected DbRule() {
        this(SG, BASIC_TEST, AUTHORIZATION);
    }

    protected DbRule(final SchemaSpec... schemas) {
        this.schemas = schemas;
    }

    //~ Methods ......................................................................................................................................

    /** Create a new {@link RuleChain}, which encloses the given rule with this {@link DbRule}. */
    public RuleChain around(TestRule enclosedRule) {
        return RuleChain.outerRule(this).around(enclosedRule);
    }

    @Override protected void after() {
        Transaction.getCurrent().ifPresent(Transaction::commit);
        runInTransaction(() -> {
            database.close();
            openDatabase(dbName);
            final DatabaseType dbType = database.getDatabaseType();
            for (final SchemaSpec schema : schemas)
                dbType.dropSchema(database, schema.name, true);
        });
    }

    @Override protected final void createDatabase(@NotNull final String databaseName) {
        openDatabase(databaseName);
        dbName = databaseName;
        final DatabaseType dbType = database.getDatabaseType();
        runInTransaction(() -> {
            for (final SchemaSpec schema : schemas)
                dbType.dropSchema(database, schema.name, true);
        });
        doCreateDatabase();

        for (final SchemaSpec schema : schemas) {
            dbType.createSchema(database, schema.name, env.get(schema.name, SchemaProps.class).tableTablespace);
            runInTransaction(() -> {
                runSql(schema.sql);
                if (!schema.ovl.isEmpty()) runSql(schema.ovl);
            });
        }

        initTableFactory();
        Transaction.getCurrent().ifPresent(Transaction::commit);
    }

    protected StoreHandler.Factory createStoreHandlerFactory() {
        final CounterHandlerFactory f = new CounterHandlerFactory(dbFactory);
        counters = f.getCounters();
        return f;
    }

    protected void initTableFactory() {
        TableFactory.setFactory(new TableFactory(createStoreHandlerFactory()));
    }

    @Override protected final void openDatabase(final String databaseName) {
        super.openDatabase(databaseName);
    }

    private void runSql(final String fileName) {
        final Reader ovl = loadSql(fileName);
        if (ovl != null) database.sqlStatement(ovl).executeScript();
    }

    //~ Static Fields ................................................................................................................................

    /** Constants as long as DbRule is not exposed to clients! */
    public static final SchemaSpec SG            = new SchemaSpec("SG", "db/current/sg.sql", "db/current/sg_ovl.sql");
    public static final SchemaSpec MAIL          = new SchemaSpec("MAIL", "db/current/mail.sql");
    public static final SchemaSpec BASIC         = new SchemaSpec("BASIC", "db/current/basic.sql");
    public static final SchemaSpec CART          = new SchemaSpec("CART", "db/current/cart.sql");
    public static final SchemaSpec AUTHORIZATION = new SchemaSpec("AUTHORIZATION", "db/current/authorization.sql");
    public static final SchemaSpec TEST          = new SchemaSpec("TEST", "db/current/test.sql");
    public static final SchemaSpec TASK_TEST     = new SchemaSpec("TASK_TEST", "db/current/task_test.sql");
    public static final SchemaSpec VIEWS         = new SchemaSpec("VIEWS", "db/current/views.sql");
    public static final SchemaSpec SHOWCASE      = new SchemaSpec("SHOWCASE", "db/current/showcase.sql");
    public static final SchemaSpec FORM          = new SchemaSpec("FORM", "db/current/form.sql");
    public static final SchemaSpec BASIC_TEST    = new SchemaSpec("BASIC_TEST", "db/current/basic_test.sql");
    public static final SchemaSpec ADVICE        = new SchemaSpec("ADVICE", "db/current/advice.sql");

    //~ Inner Classes ................................................................................................................................

    public static class SchemaSpec {
        private final String name;
        private final String ovl;
        private final String sql;

        /** Create an SchemaSpec. */
        public SchemaSpec(final String name, final String sql) {
            this(name, sql, "");
        }
        /** Create an SchemaSpec. */
        public SchemaSpec(final String name, final String sql, final String ovl) {
            this.name = name;
            this.sql  = sql;
            this.ovl  = ovl;
        }
    }
}  // end class NoMmDbRule
