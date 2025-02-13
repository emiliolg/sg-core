
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.common.tools.test;

import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.AppenderBase;

import org.assertj.core.api.Assertions;
import org.intellij.lang.annotations.PrintFormat;
import org.jetbrains.annotations.NotNull;
import org.junit.rules.TestRule;
import org.junit.runner.Description;
import org.junit.runners.model.Statement;

import tekgenesis.common.env.Environment;
import tekgenesis.common.env.context.Context;
import tekgenesis.common.env.impl.MemoryEnvironment;
import tekgenesis.common.env.logging.LogConfig;
import tekgenesis.common.env.security.Session;
import tekgenesis.database.Database;
import tekgenesis.database.DatabaseFactory;
import tekgenesis.database.DatabaseType;
import tekgenesis.database.SqlStatement;
import tekgenesis.database.hikari.HikariDatabaseConfig;
import tekgenesis.database.hikari.HikariDatabaseFactory;
import tekgenesis.properties.SchemaProps;
import tekgenesis.transaction.JDBCTransactionManager;
import tekgenesis.transaction.TransactionManager;

import static java.lang.Boolean.getBoolean;

import static tekgenesis.common.Predefined.ensureNotNull;
import static tekgenesis.common.Predefined.notNull;

/**
 * Rule to execute Database tests.
 */
@SuppressWarnings("DuplicateStringLiteralInspection")
public abstract class DatabaseRule implements TestRule {

    //~ Instance Fields ..............................................................................................................................

    protected Database database;

    @NotNull protected final HikariDatabaseFactory dbFactory;
    protected boolean                              debugJdbc;

    @NotNull protected final MemoryEnvironment     env;
    @NotNull private final CloseConnectionAppender closeConnectionAppender;
    @NotNull private final JDBCTransactionManager  tm;

    //~ Constructors .................................................................................................................................

    protected DatabaseRule() {
        env       = new MemoryEnvironment();
        tm        = new JDBCTransactionManager();
        dbFactory = new HikariDatabaseFactory(env, tm);
        LogConfig.start();
        LogConfig.bridgeJul();
        Context.bind(Session.class, TestSession.class);
        Context.getContext().setSingleton(Environment.class, env);
        Context.getContext().setSingleton(DatabaseFactory.class, dbFactory);
        Context.getContext().setSingleton(TransactionManager.class, tm);

        debugJdbc               = getBoolean("test.debugJdbc");
        closeConnectionAppender = new CloseConnectionAppender();

        database = null;
    }

    //~ Methods ......................................................................................................................................

    public Statement apply(@NotNull final Statement base, Description description) {
        return new Statement() {
            @Override public void evaluate()
                throws Throwable
            {
                closeConnectionAppender.start();
                before();
                try {
                    base.evaluate();
                }
                catch (final Exception e) {
                    e.printStackTrace(System.err);
                }
                finally {
                    myAfter();
                    closeConnectionAppender.stop();
                }
            }
        };
    }

    /** See Database#sqlStatement. */
    @NotNull public final SqlStatement sqlStatement(@NotNull String statement) {
        return getDatabase().sqlStatement(statement);
    }

    /** See Database#sqlStatement. */
    public final SqlStatement sqlStatement(@NotNull @PrintFormat String statement, @NotNull Object... args) {
        return getDatabase().sqlStatement(statement, args);
    }

    /** Return the underlying database. */
    @NotNull public Database getDatabase() {
        return ensureNotNull(database, "Database not initialized");
    }

    /** Returns the database type. */
    @NotNull public DatabaseType getDatabaseType() {
        return getDatabase().getDatabaseType();
    }

    /** Get the environment. */
    public Environment getEnv() {
        return env;
    }

    /** Returns the transaction manager. */
    public TransactionManager getTransactionManager() {
        return tm;
    }
    protected void after() {}
    protected abstract void before();

    protected void createDatabase(@NotNull String dbName) {
        openDatabase(dbName);
        doCreateDatabase();
    }

    protected void doCreateDatabase() {
        final DatabaseType t = getDatabaseType();
        t.dropDatabase(getDatabase(), true);
        t.createDatabase(getDatabase());
    }

    protected void openDatabase(final String dbName) {
        ensureNotNull(dbName, "Must specified configuration name in subclass");
        final HikariDatabaseConfig config = DbTests.configurationFor(dbName);
        config.logStatementsEnabled = debugJdbc;

        database = dbFactory.open(dbName, config);
        final SchemaProps sp = new SchemaProps();
        sp.database = dbName;
        env.put(sp);
    }
    private void myAfter() {
        try {
            after();
        }
        finally {
            final Database db = getDatabase();
            db.close();
            getDatabaseType().dropDatabase(db, false);
            env.dispose();
            Context.getContext().unbind(Session.class);
            closeConnectionAppender.stop();
            if (closeConnectionAppender.hasErrors()) Assertions.fail("Unclosed connections. Check output for stack trace");
        }
    }

    //~ Inner Classes ................................................................................................................................

    private static class CloseConnectionAppender extends AppenderBase<ILoggingEvent> {
        private int errors;

        public boolean hasErrors() {
            return errors > 0;
        }

        @Override protected void append(@NotNull ILoggingEvent eventObject) {
            if (notNull(eventObject.getMessage()).contains("was never closed")) errors++;
        }
    }
}  // end class DatabaseRule
