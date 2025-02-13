
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.database;

import java.io.Writer;
import java.sql.Connection;
import java.util.*;

import org.jetbrains.annotations.NotNull;

import tekgenesis.common.collections.ImmutableList;
import tekgenesis.common.collections.Seq;
import tekgenesis.common.env.Environment;
import tekgenesis.common.logging.Logger;
import tekgenesis.database.exception.DatabaseNotFoundException;
import tekgenesis.database.exception.DatabaseSchemaDoesNotExistsException;
import tekgenesis.properties.SchemaProps;
import tekgenesis.transaction.ConnectionReference;
import tekgenesis.transaction.TransactionManager;
import tekgenesis.transaction.TransactionResource;

import static java.util.Arrays.asList;

import static tekgenesis.common.Predefined.max;
import static tekgenesis.common.Predefined.notNull;
import static tekgenesis.common.collections.Colls.emptyList;
import static tekgenesis.common.collections.Colls.immutable;
import static tekgenesis.common.core.Constants.SCHEMA_LIST_FILE;
import static tekgenesis.common.core.Strings.split;
import static tekgenesis.common.util.Resources.readResources;
import static tekgenesis.database.DatabaseConstants.MEM;
import static tekgenesis.database.DbConstants.SUIGENERIS_SCHEMA;
import static tekgenesis.database.SchemaDefinition.ChangeLevel;
import static tekgenesis.transaction.Transaction.runInTransaction;

/**
 * Default Abstract implementation of a Database Factory.
 */
public abstract class AbstractDatabaseFactory<T extends DatabaseConfig> implements DatabaseFactory<T> {

    //~ Instance Fields ..............................................................................................................................

    @NotNull private final Class<T> configClass;

    @NotNull private final Map<String, Database> databases;
    @NotNull private final Environment           env;
    private final T                              memConfig;
    @NotNull private final TransactionManager    tm;

    //~ Constructors .................................................................................................................................

    protected AbstractDatabaseFactory(@NotNull Environment env, @NotNull TransactionManager tm, @NotNull Class<T> configClass, T memConfig) {
        this.tm          = tm;
        this.env         = env;
        this.configClass = configClass;
        databases        = new HashMap<>();
        this.memConfig   = memConfig;
    }

    //~ Methods ......................................................................................................................................

    @Override public Database createSystemAlias(Database database) {
        final T config = configClass.cast(database.getConfiguration());
        return new Database(this, config, tm, createResource(database.getName(), config, true));
    }

    @Override public ChangeLevel dryInitialize(final String name, final T config, final boolean reset, @NotNull final Writer dryWriter) {
        final Collection<String> scs = retrieveSchemas();
        final Database           db  = openDatabase(name, config, reset, scs);
        return checkAndEvolveSchemas(db.asDry(dryWriter), scs);
    }

    @Override public Database forSchema(final String schemaName) {
        return open(env.get(schemaName, SchemaProps.class).database);
    }

    @Override public Database initialize(final String name, final T config, boolean reset, String... schemas) {
        final Collection<String> scs = schemas.length == 0 ? retrieveSchemas() : asList(schemas);

        final Database db = openDatabase(name, config, reset, scs);
        checkAndEvolveSchemas(db, scs);
        return db;
    }

    public Database open(@NotNull String dbName) {
        final Database db = findDatabase(dbName);
        return db == null ? open(dbName, getDbConfig(dbName)) : db;
    }

    @NotNull @Override public synchronized Database open(String name, @NotNull T config) {
        return databases.computeIfAbsent(name, k -> new Database(this, config, tm, createResource(name, config, false)));
    }

    @Override public Database openDefault() {
        return open(env.get(SchemaProps.class).database);
    }

    /** Retrieve all schemas topological sorted by dependency. */
    public Collection<String> retrieveSchemas() {
        final Map<String, Seq<String>> schemas = new LinkedHashMap<>();
        final ImmutableList<String>    empty   = emptyList();
        schemas.put(SUIGENERIS_SCHEMA.toLowerCase(), empty);
        for (final String line : readResources(SCHEMA_LIST_FILE)) {
            final int    pos          = line.indexOf(' ');
            final String schema       = pos == -1 ? line : line.substring(0, pos);
            final String dependencies = pos == -1 ? "" : line.substring(pos + 1);

            if (isLocal(schema)) schemas.put(schema, split(dependencies, ',').filter(this::isLocal));
        }

        return immutable(schemas.keySet()).topologicalSort(s -> notNull(schemas.get(s), empty));
    }

    @Override public void shutdown() {
        for (final Database db : new ArrayList<>(databases.values())) {
            runInTransaction(() -> db.getDatabaseType().shutdown(db));
            try {
                db.close();
            }
            catch (final Exception e) {
                logger.debug(e);
            }
        }
    }

    @Override public TransactionManager getTransactionManager() {
        return tm;
    }

    @NotNull protected abstract TransactionResource<Connection> createResource(String name, T config, boolean system);

    protected void removeDb(String name) {
        databases.remove(name);
    }

    private ChangeLevel checkAndEvolveSchemas(final Database db, final Collection<String> scs) {
        ChangeLevel changeLevel = ChangeLevel.NONE;
        for (final String schema : scs)
            changeLevel = max(changeLevel, new SchemaDefinition(db, schema, env).checkVersion());
        return changeLevel;
    }

    private Database findDatabase(String name) {
        return databases.get(name);
    }

    @NotNull private Database openDatabase(final String name, final T config, final boolean reset, final Collection<String> scs) {
        final Database db = open(name, config);
        if (reset)
        // Drop the current database
        tm.runInTransaction(t -> {                                  //
            final DatabaseType dbType = db.getDatabaseType();for (final String schema : scs)
                dbType.dropSchema(db, schema.toUpperCase(), true);  //
            dbType.dropDatabase(db, true);                          //
            dbType.createDatabase(db);                              //
        });
        else {
            try {
                tm.runInTransaction(t -> {
                    final ConnectionReference<Connection> c = db.getConnectionRef();
                    try {
                        c.get();
                    }
                    finally {
                        c.detach();
                    }
                });
            }
            catch (final DatabaseSchemaDoesNotExistsException e) {
                tm.runInTransaction(t -> db.getDatabaseType().createDatabase(db));
            }
        }
        return db;
    }                                                               // end method openDatabase

    private T getDbConfig(String name) {
        final T config = MEM.equals(name) ? memConfig : env.get(name, configClass);
        if (config.type == null) throw new DatabaseNotFoundException(name, config);
        return config;
    }

    private boolean isLocal(final String s) {
        return s != null && !env.get(s, SchemaProps.class).remote;
    }

    //~ Static Fields ................................................................................................................................

    private static final Logger logger = Logger.getLogger(AbstractDatabaseFactory.class);
}  // end class AbstractDatabaseFactory
