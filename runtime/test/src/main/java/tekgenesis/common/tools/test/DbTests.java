
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.common.tools.test;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.Collection;
import java.util.Map;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import tekgenesis.common.collections.Maps;
import tekgenesis.common.collections.Seq;
import tekgenesis.common.core.Strings;
import tekgenesis.common.core.Tuple;
import tekgenesis.database.Database;
import tekgenesis.database.DatabaseConstants;
import tekgenesis.database.DatabaseType;
import tekgenesis.database.hikari.HikariDatabaseConfig;
import tekgenesis.database.hikari.HikariDatabaseFactory;

import static java.lang.String.format;
import static java.lang.System.getProperty;

import static tekgenesis.common.core.Tuple.tuple;
import static tekgenesis.database.DatabaseType.*;

/**
 * Utilities for database testing.
 */
@SuppressWarnings("DuplicateStringLiteralInspection")
public class DbTests {

    //~ Constructors .................................................................................................................................

    private DbTests() {}

    //~ Methods ......................................................................................................................................

    /** Return the Default configuration based on the database name. */
    @NotNull public static HikariDatabaseConfig configurationFor(String dbName) {
        final HikariDatabaseConfig r = DEFAULT_DATABASES.get(dbName);
        if (r == null) throw new IllegalArgumentException("Not DB Configuration for: " + dbName);
        return r;
    }

    /** Clean and Create database. */
    public static Database createDatabase(final HikariDatabaseFactory dbFactory, final String dbName, final String... schemas) {
        final HikariDatabaseConfig config = configurationFor(dbName);
        config.autoUpgrade = true;

        final Database     db     = dbFactory.open(dbName, config);
        final DatabaseType dbType = db.getDatabaseType();
        for (final String schema : schemas)
            dbType.dropSchema(db, schema, true);
        dbType.dropDatabase(db, true);
        dbType.createDatabase(db);
        return db;
    }

    /** Return the databases as a List of Strings. */
    @NotNull public static Collection<String> findDatabases() {
        final String dbs = getProperty(DATABASE_LIST_PROP, DatabaseConstants.MEM);
        return "*".equals(dbs) ? DEFAULT_DATABASES.keySet() : Strings.split(dbs, ',');
    }

    /**
     * Return A list of Database Objects based on the "test.databases" property using "mem" as the
     * default.
     */
    @NotNull public static Seq<Object[]> listDatabases() {
        final Collection<String> dbs = findDatabases();
        return listDatabases(dbs.toArray(new String[dbs.size()]));
    }

    /** Return A list of Database Objects based on the specified names. */
    @NotNull public static Seq<Object[]> listDatabases(String... dbNames) {
        return Tests.wrapForParameters(dbNames);
    }

    /** Load Sql file. */
    @Nullable public static Reader loadSql(final String fileName) {
        final InputStream stream = Thread.currentThread().getContextClassLoader().getResourceAsStream(fileName);
        return stream == null ? null : new InputStreamReader(stream);
    }

    /**
     * Allows to inject a map of custom databases configurations, overriding the default one.
     * Returns the old map
     */
    public static Map<String, HikariDatabaseConfig> setDatabases(@NotNull final Map<String, HikariDatabaseConfig> dbs) {
        final Map<String, HikariDatabaseConfig> old = DEFAULT_DATABASES;
        DEFAULT_DATABASES = dbs;
        return old;
    }

    @SuppressWarnings("MagicNumber")
    private static Tuple<String, HikariDatabaseConfig> config(DatabaseType type, String defaultUrl, String system, String adminUrl) {
        final String dbName   = type.name().toLowerCase();
        final String hostName = Tests.getHostName();
        final String userName = USER_NAME_PREFIX + hostName.toUpperCase();

        final String               url = getProperty("test." + dbName + "Url", format(defaultUrl, hostName));
        final HikariDatabaseConfig r   = new HikariDatabaseConfig(type, url);
        r.schemaPrefix   = hostName.toUpperCase();
        r.user           = type.defaultsToLowerCase() ? userName.toLowerCase() : userName;
        r.password       = DEFAULT_PASSWORD;
        r.systemPassword = DEFAULT_PASSWORD;
        r.systemUser     = system;
        r.maxPoolSize    = type.getType() == HSQLDB ? 25 : 5;
        r.idleMaxAge     = 10;
        r.minimunIdle    = 5;
        r.setAdminUrl(adminUrl);

        return tuple(dbName, r);
    }

    //~ Static Fields ................................................................................................................................

    private static final String DATABASE_LIST_PROP = "test.databases";
    private static final String DEFAULT_PASSWORD   = "password";
    private static final String USER_NAME_PREFIX   = "TEST_";

    private static final String DEFAULT_SYSTEM_USER  = "system";
    private static final String POSTGRES_SYSTEM_USER = "postgres";
    private static final String MEM_AUTOINCREMENT    = "mem-ai";

    private static final String                      POSTGRESS_URL     = "jdbc:postgresql://postgredb.tekgenesis.com:5432/";
    private static Map<String, HikariDatabaseConfig> DEFAULT_DATABASES = Maps.linkedHashMap(
            tuple(DatabaseConstants.MEM, DatabaseConstants.MEM_CONFIG),
            tuple(MEM_AUTOINCREMENT, new HikariDatabaseConfig(HSQLDB_NOSEQ, DatabaseConstants.HSQLDB_MEM_URL)),
            config(ORACLE, "jdbc:oracle:thin:@oracle.tekgenesis.com:1521:oracle", DEFAULT_SYSTEM_USER, ""),
            config(POSTGRES, POSTGRESS_URL + "%s_postgres", POSTGRES_SYSTEM_USER, POSTGRESS_URL + "template1"));
}  // end class DbTests
