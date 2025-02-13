
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.database;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;
import java.util.function.Predicate;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import tekgenesis.common.collections.ImmutableCollection;
import tekgenesis.common.collections.ImmutableSet;
import tekgenesis.common.collections.Seq;
import tekgenesis.common.core.Option;
import tekgenesis.common.core.QName;
import tekgenesis.common.env.Environment;
import tekgenesis.common.env.context.Context;
import tekgenesis.common.env.impl.MemoryEnvironment;
import tekgenesis.database.exception.DatabaseException;
import tekgenesis.database.exception.DatabaseSchemaDoesNotExistsException;
import tekgenesis.database.hikari.HikariDatabaseFactory;
import tekgenesis.database.introspect.*;
import tekgenesis.database.introspect.exception.IntrospectorException;
import tekgenesis.properties.SchemaProps;
import tekgenesis.transaction.ConnectionReference;
import tekgenesis.transaction.JDBCTransactionManager;
import tekgenesis.transaction.TransactionManager;

import static tekgenesis.common.Predefined.notNull;
import static tekgenesis.common.collections.Colls.immutable;
import static tekgenesis.transaction.Transaction.invokeInTransaction;
import static tekgenesis.transaction.Transaction.runInTransaction;

/**
 * A Database Introspector.
 */
public class DbIntrospector implements AutoCloseable {

    //~ Instance Fields ..............................................................................................................................

    private ImmutableSet<String> catalogNames;

    private final Connection connection;

    private final DatabaseType dbType;

    @NotNull private final String           identifierQuoteString;
    @NotNull private final DatabaseMetaData metaData;
    private final MetadataRetriever         retriever;
    private final String                    schemaPrefix;
    private Map<String, SchemaInfo>         schemas;
    private final boolean                   supportsCatalogs;
    private final boolean                   supportsSchemas;
    private EnumSet<TableType>              tableTypes;

    //~ Constructors .................................................................................................................................

    /** Introspector constructor. */
    private DbIntrospector(final Connection connection, final DatabaseType dbType, final String schemaPrefix)
        throws SQLException
    {
        this.connection       = connection;
        this.dbType           = dbType;
        this.schemaPrefix     = schemaPrefix;
        catalogNames          = null;
        schemas               = null;
        tableTypes            = null;
        metaData              = connection.getMetaData();
        supportsSchemas       = metaData.supportsSchemasInTableDefinitions();
        supportsCatalogs      = metaData.supportsCatalogsInTableDefinitions();
        identifierQuoteString = notNull(getMetaData().getIdentifierQuoteString());
        retriever             = MetadataRetriever.createRetriever(dbType, connection, metaData);
    }

    //~ Methods ......................................................................................................................................

    @Override public void close() {}

    /** List Schema Names. */
    @NotNull public Seq<String> listSchemaNames() {
        return getSchemas().map(m -> m == null ? "" : m.getName());
    }

    /** Returns true if the databases support catalogs. */
    public boolean supportsCatalogs() {
        return supportsCatalogs;
    }

    /** Returns true if the databases support schemas. */
    public boolean supportsSchemas() {
        return supportsSchemas;
    }

    /** Return the name of all databases catalogs. */
    public ImmutableSet<String> getCatalogNames() {
        if (catalogNames == null) catalogNames = retrieveAllCatalogs();
        return catalogNames;
    }

    /** Returns Databases Type. */
    public DatabaseType getDatabaseType() {
        return dbType;
    }

    /** Return the default catalog for this database. */
    public String getDefaultCatalog() {
        return dbType.getDefaultCatalog();
    }

    /** Returns the string used to quote identifiers. */
    @NotNull public String getIdentifierQuoteString() {
        return identifierQuoteString;
    }

    /** Get Metadata Retriever. */
    public MetadataRetriever getRetriever() {
        return retriever;
    }

    /** Create an Schema Info based on a name. */
    public SchemaInfo getSchema(@NotNull final String schemaName) {
        return getSchema("", schemaName);
    }

    /** Create an Schema Info based on a catalog and schema name. */
    public SchemaInfo getSchema(@NotNull final String catalogName, @NotNull final String schemaName) {
        final String     c = catalogName.isEmpty() && getCatalogNames().size() == 1 ? getCatalogNames().getFirst().get() : catalogName;
        final SchemaInfo s = new SchemaInfo(this, c, schemaName);
        if (schemas == null) schemas = new TreeMap<>();

        final SchemaInfo ret = schemas.get(s.getLookupKey());
        if (ret != null) return ret;

        schemas.put(s.getLookupKey(), s);
        return s;
    }

    /** Returns the schema prefix. */
    public String getSchemaPrefix() {
        return schemaPrefix;
    }

    /** Retrieve all Schemas. */
    @NotNull public ImmutableCollection<SchemaInfo> getSchemas() {
        if (schemas == null) schemas = retrieveAllSchemas();
        return immutable(schemas.values());
    }

    /** Get the table with the specified schema and table name. */
    public Option<TableInfo> getTable(@NotNull final QName table) {
        return getSchema(table.getQualification()).getTable(table.getName());
    }

    /** Retrieve Table Types. */
    @NotNull public EnumSet<TableType> getTableTypes() {
        if (tableTypes == null) tableTypes = retrieveTableTypes();
        return tableTypes;
    }

    Connection getConnection() {
        return connection;
    }

    @NotNull DatabaseMetaData getMetaData() {
        return metaData;
    }

    private void addSchema(final String catalogName, final String schemaName, final Map<String, SchemaInfo> result) {
        final SchemaInfo s = getSchema(catalogName, schemaName);
        result.put(s.getLookupKey(), s);
    }

    private void addSchemaForAllCatalogs(final String schemaName, final Map<String, SchemaInfo> map) {
        if (getCatalogNames().isEmpty()) addSchema("", schemaName, map);
        else for (final String catalogName : getCatalogNames())
            addSchema(catalogName, schemaName, map);
    }

    private ImmutableSet<String> retrieveAllCatalogs() {
        Set<String> result = null;
        if (!supportsCatalogs) {
            try(final ResultSet results = metaData.getCatalogs()) {
                result = new HashSet<>();
                while (results.next()) {
                    final String value = notNull(results.getString(1)).trim();
                    if (!value.isEmpty()) result.add(value);
                }
            }
            catch (final SQLException e) {
                throw new IntrospectorException(e);
            }
        }
        return immutable(result);
    }

    private Map<String, SchemaInfo> retrieveAllSchemas() {
        final Map<String, SchemaInfo> result = new TreeMap<>();
        if (!supportsSchemas) addSchemaForAllCatalogs("", result);
        else {
            for (final MdEntry e : retriever.getSchemas()) {
                final String catalogName = supportsCatalogs ? e.getString(MdColumn.S_CATALOG) : null;
                final String schemaName  = notNull(e.getString(MdColumn.S_NAME));

                if (catalogName == null) addSchemaForAllCatalogs(schemaName, result);
                else addSchema(catalogName, schemaName, result);
            }
        }
        return result;
    }  // end method retrieveAllSchemas

    private EnumSet<TableType> retrieveTableTypes() {
        final EnumSet<TableType> result = EnumSet.noneOf(TableType.class);

        try(final ResultSet rs = metaData.getTableTypes()) {
            while (rs.next()) {
                final String value = notNull(rs.getString(1)).trim();
                if (!value.isEmpty()) result.add(TableType.fromString(value));
            }
        }
        catch (final SQLException e) {
            throw new IntrospectorException(e);
        }
        return result;
    }

    //~ Methods ......................................................................................................................................

    /** Create An Introspector from an SqlConnection. */
    @NotNull public static DbIntrospector forConnection(Connection connection, DatabaseType databaseType) {
        try {
            return new DbIntrospector(connection, databaseType, "");
        }
        catch (final SQLException e) {
            throw new IntrospectorException(e);
        }
    }

    /** Create an introspector based on a Database. */
    public static DbIntrospector forDatabase(Database db) {
        final ConnectionReference<Connection> ref = db.getConnectionRef();
        try {
            return new DbIntrospector(ref.get(), db.getDatabaseType(), db.getSchemaPrefix()) {
                @Override public void close() {
                    ref.detach();
                }
            };
        }
        catch (final SQLException e) {
            ref.detach();
            throw new IntrospectorException(e);
        }
    }
    /**
     * Create the named schema in memory based on the definition in the specified file and
     * introspect the schema.
     */
    public static SchemaInfo introspectSchema(final String schemaName, Seq<File> resourcesDir, final File... sqlFiles) {
        return introspectSchema(schemaName, resourcesDir, false, sqlFiles);
    }

    /**
     * Create the named schema in memory based on the definition in the specified file and
     * introspect the schema. If you are introspecting current schema, you should pass the boolean
     * as true.
     */
    public static SchemaInfo introspectSchema(final String schemaName, Seq<File> resourcesDir, boolean current, final File... sqlFiles) {
        final Environment           env       = new MemoryEnvironment();
        final HikariDatabaseFactory dbFactory = new HikariDatabaseFactory(env, new JDBCTransactionManager());
        final Database              db        = dbFactory.open(DatabaseConstants.MEM);
        Context.getContext().setSingleton(TransactionManager.class, dbFactory.getTransactionManager());
        runInTransaction(() -> {
            final DatabaseType dbType = db.getDatabaseType();
            dbType.createDatabase(db);
            dbType.createSchema(db, schemaName, env.get(schemaName, SchemaProps.class).tableTablespace);
        });

        try {
            final List<String> schemas = new ArrayList<>();
            return invokeInTransaction(() -> {
                executeFiles(db, sqlFiles, resourcesDir, schemas, env);
                return schemaInfo(db, schemas, schemaName, current);
            });
        }
        finally {
            dbFactory.shutdown();
        }
    }

    private static void executeFiles(final Database db, final File[] sqlFiles, final Seq<File> resourcesDir, List<String> schemas, Environment env) {
        for (final File sqlFile : sqlFiles) {
            try(FileReader f = new FileReader(sqlFile)) {
                final File dbDir = extractDbDir(sqlFile);
                db.sqlStatement(f).executeScript(new HandleSchemaCreation(db, dbDir, schemas, resourcesDir, env));
            }
            catch (final IOException e) {
                throw new UncheckedIOException(e);
            }
        }
    }

    @Nullable private static File extractDbDir(final File sqlFile) {
        for (File dir = sqlFile.getParentFile(); dir != null; dir = dir.getParentFile())
            if ("db".equals(dir.getName())) return dir.getParentFile();
        return null;
    }

    @NotNull private static SchemaInfo schemaInfo(Database db, List<String> schemas, String schemaName, boolean current) {
        try(final DbIntrospector introspector = forDatabase(db)) {
            for (final String s : schemas)
                introspector.getSchema(s).loadAll();
            final SchemaInfo schema = introspector.getSchema(schemaName);
            if (current) schema.markCurrent();
            schema.loadAll();
            return schema;
        }
    }

    //~ Inner Classes ................................................................................................................................

    private static class HandleSchemaCreation implements Predicate<DatabaseException> {
        private final Database     db;
        private final Environment  env;
        private final List<File>   resourcesDir;
        private final List<String> schemas;

        HandleSchemaCreation(final Database db, @Nullable final File dbDir, final List<String> dependencies, Seq<File> resources, Environment env) {
            this.db      = db;
            resourcesDir = (dbDir == null ? resources : resources.append(dbDir)).toList();
            schemas      = dependencies;
            this.env     = env;
        }

        @Override public boolean test(@Nullable final DatabaseException e) {
            if (e == null) return false;
            if (e instanceof DatabaseSchemaDoesNotExistsException && !resourcesDir.isEmpty()) {
                final String schema = ((DatabaseSchemaDoesNotExistsException) e).getSchema();
                new SchemaDefinition(db, schema, env).ignoreErrors().createSchema(resourcesDir);
                schemas.add(schema);
                return true;
            }
            throw e;
        }
    }
}  // end class DbIntrospector
