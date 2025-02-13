
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.common.tools.test;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

import org.junit.rules.TestRule;
import org.junit.runner.Description;
import org.junit.runners.model.Statement;

import tekgenesis.common.env.Environment;
import tekgenesis.common.env.context.Context;
import tekgenesis.common.env.impl.MemoryEnvironment;
import tekgenesis.common.util.Diff;
import tekgenesis.common.util.VersionString;
import tekgenesis.database.Database;
import tekgenesis.database.DbIntrospector;
import tekgenesis.database.SchemaDefinition;
import tekgenesis.database.SqlStatement;
import tekgenesis.database.hikari.HikariDatabaseFactory;
import tekgenesis.transaction.JDBCTransactionManager;
import tekgenesis.transaction.TransactionManager;

import static java.lang.String.format;

import static org.assertj.core.api.Assertions.fail;

import static tekgenesis.common.collections.Colls.immutable;
import static tekgenesis.common.util.VersionString.VERSION_ZERO;
import static tekgenesis.common.util.VersionString.valueOf;

/**
 * A Rule to test Schema evolution.
 */
public class DbEvolutionRule implements TestRule {

    //~ Instance Fields ..............................................................................................................................

    private Database              db        = null;
    private HikariDatabaseFactory dbFactory = null;
    private MemoryEnvironment     env       = null;
    private final File            outputDir;

    private List<String>        schemaNames    = null;
    private List<VersionString> schemaVersions = null;

    //~ Constructors .................................................................................................................................

    /**
     * Create the Rule. Using quick avoid retrieving the foreign keys in Oracle (pretty time
     * consuming)
     */
    public DbEvolutionRule(final File outputDir) {
        this.outputDir = outputDir;
    }

    //~ Methods ......................................................................................................................................

    @Override public Statement apply(final Statement base, final Description description) {
        final MuteRule mute = new MuteRule(SchemaDefinition.class, SqlStatement.class);
        return mute.apply(new Statement() {
                @Override public void evaluate()
                    throws Throwable
                {
                    init();
                    try {
                        base.evaluate();
                    }
                    finally {
                        if (db != null) {
                            immutable(schemaNames).revert().forEach(schemaName ->
                                    db.getTransactionManager().runInTransaction(t ->
                                            db.getDatabaseType()
                                              .dropSchema(db, schemaName.toUpperCase(), true)));
                            db.close();
                        }
                        env.dispose();
                    }
                }
            }, description);
    }

    /** Evolve and check. */
    public void evolveAndCheck(final String dbName) {
        db = DbTests.createDatabase(dbFactory, dbName, schemaNames.toArray(new String[schemaNames.size()]));
        for (int i = 0; i < schemaNames.size(); i++)
            evolveAndCheck(dbName, schemaNames.get(i), schemaVersions.get(i));
    }  // end method evolveAndCheck

    /** Add Schema to be tested. */
    public DbEvolutionRule schema(String schemaName) {
        return schema(schemaName, "");
    }

    /** Add Schema to be tested. */
    public DbEvolutionRule schema(String schemaName, String firstVersion) {
        schemaNames.add(schemaName.toUpperCase());
        schemaVersions.add(valueOf(firstVersion));
        return this;
    }

    private void evolveAndCheck(final String dbName, final String schemaName, final VersionString firstVersion) {
        db = dbFactory.open(dbName, DbTests.configurationFor(dbName));

        final SchemaDefinition schemaDefinition = new SchemaDefinition(db, schemaName, env);
        final VersionString    version          = firstVersion.equals(VERSION_ZERO) ? schemaDefinition.firstVersion() : firstVersion;

        final TransactionManager tm = db.getTransactionManager();
        Context.getContext().setSingleton(TransactionManager.class, tm);
        tm.runInTransaction(t -> schemaDefinition.createSchema(version));

        reopen(dbName);
        tm.runInTransaction(t -> new SchemaDefinition(db, schemaName, env).checkVersion());

        reopen(dbName);
        final File evolved = new File(outputDir, schemaName + "_evolved.sql");
        tm.runInTransaction(t -> introspectSchema(db, schemaName, evolved));

        reopen(dbName);
        tm.runInTransaction(t -> db.getDatabaseType().dropSchema(db, schemaName.toUpperCase(), false));

        reopen(dbName);
        tm.runInTransaction(t -> new SchemaDefinition(db, schemaName, env).checkVersion());

        reopen(dbName);
        final File created = new File(outputDir, schemaName + "_created.sql");
        tm.runInTransaction(t -> introspectSchema(db, schemaName, created));

        db.close();
        check(created, evolved, schemaName, schemaDefinition, version);
    }

    private void init() {
        env = new MemoryEnvironment();
        Context.getContext().setSingleton(Environment.class, env);
        dbFactory      = new HikariDatabaseFactory(env, new JDBCTransactionManager());
        schemaNames    = new ArrayList<>();
        schemaVersions = new ArrayList<>();
        db             = null;
    }

    private void reopen(String dbName) {
        db.close();
        db = dbFactory.open(dbName, DbTests.configurationFor(dbName));
    }

    //~ Methods ......................................................................................................................................

    private static void check(final File created, final File evolved, final String schemaName, final SchemaDefinition schemaDefinition,
                              final VersionString version) {
        try(final FileReader a = new FileReader(created);
            final FileReader b = new FileReader(evolved))
        {
            final List<Diff.Delta<String>> diffs = Diff.ignoreAllSpace().diff(a, b);
            if (!diffs.isEmpty()) {
                final String message = format("\nEvolving %s from %s to %s Failed." +
                        "\ndiff -y -W 150 %s %s\n%s",
                        schemaName,
                        version,
                        schemaDefinition.lastVersion(),
                        created,
                        evolved,
                        Diff.makeString(diffs));
                fail(message);
            }
        }
        catch (final IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    private static void introspectSchema(final Database db, String schema, File file) {
        file.getParentFile().mkdirs();
        try(final FileWriter writer = new FileWriter(file);
            final DbIntrospector dbIntrospector = DbIntrospector.forDatabase(db))
        {
            dbIntrospector.getSchema(schema).dumpSql(writer, true);
        }
        catch (final IOException e) {
            throw new UncheckedIOException(e);
        }
    }
}  // end class DbEvolutionRule
