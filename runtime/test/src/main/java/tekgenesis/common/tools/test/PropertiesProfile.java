
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.common.tools.test;

import java.io.File;
import java.net.InetAddress;
import java.net.UnknownHostException;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import tekgenesis.app.properties.ApplicationProps;
import tekgenesis.authorization.shiro.ShiroProps;
import tekgenesis.common.core.Constants;
import tekgenesis.common.env.Environment;
import tekgenesis.database.DatabaseConfig;
import tekgenesis.database.DatabaseType;
import tekgenesis.database.hikari.HikariDatabaseConfig;

import static java.lang.Math.min;

/**
 * Profiles to run tests.
 */
@SuppressWarnings("DuplicateStringLiteralInspection")
public enum PropertiesProfile {

    //~ Enum constants ...............................................................................................................................

    TEST_HSQLDB {
        @Override public DatabaseConfig getDbConfig(@NotNull String runDir) {
            final File   dbDir = new File(runDir, "db");
            final String url   = "jdbc:hsqldb:file:" + dbDir.getAbsolutePath();

            return createDbConfig(DatabaseType.HSQLDB_NOSEQ, url, "GENESIS", "SA", "");
        }},
    TEST_ORACLE {
        @Override public DatabaseConfig getDbConfig(String runDir) {
            // noinspection SpellCheckingInspection
            final String url   = System.getProperty("test.oracleUrl", "jdbc:oracle:thin:@oracle.tekgenesis.com:1521:oracle");
            final String user  = "TEST_" + getHostName().toUpperCase();
            final String admin = "system";

            return createDbConfig(DatabaseType.ORACLE, url, user, admin, "");
        }},
    TEST_POSTGRES {
        @Override public DatabaseConfig getDbConfig(String runDir) {
            // noinspection SpellCheckingInspection
            final String baseUrl  = "jdbc:postgresql://postgredb.tekgenesis.com:5432/";
            final String url      = baseUrl + getHostName() + "_postgres";
            final String user     = "test_" + getHostName().toLowerCase();
            final String admin    = "postgres";
            final String adminUrl = baseUrl + "template1";

            return createDbConfig(DatabaseType.POSTGRES, url, user, admin, adminUrl);
        }},
    NONE { @Nullable @Override DatabaseConfig getDbConfig(String runDir) { return null; } };

    //~ Methods ......................................................................................................................................

    /** Setup the profile in the current Context. */
    @SuppressWarnings("ResultOfMethodCallIgnored")
    public void setup(Environment env, boolean resetDb, String runDir) {
        if (this == NONE) return;

        if (this == TEST_HSQLDB && resetDb) {
            final String url       = getDbConfig(runDir).jdbcUrl;
            final int    lastIndex = url.lastIndexOf(":");
            final String dbDir     = url.substring(lastIndex + 1);
            final File   dbFile    = new File(dbDir);
            if (dbFile.exists()) dbFile.delete();
            dbFile.mkdirs();
        }

        final DatabaseConfig dbConfig = getDbConfig(runDir);
        env.put(dbConfig);

        env.get(ShiroProps.class).adminUserConfig = "password";

        final ApplicationProps applicationProps = env.get(ApplicationProps.class);
        applicationProps.resetDB   = resetDb;
        applicationProps.seedDB    = resetDb;
        applicationProps.adminUser = "admin";
        applicationProps.runDir    = runDir;
        applicationProps.noCluster = true;
        env.put(applicationProps);

        System.out.println("SET ENVIRONMENT ON " + dbConfig.type);
    }

    /** Returns the DatabaseConfig for the profile. */
    abstract DatabaseConfig getDbConfig(String runDir);

    //~ Methods ......................................................................................................................................

    private static DatabaseConfig createDbConfig(DatabaseType type, String url, String user, String admin, String adminUrl) {
        final DatabaseConfig dbConfig = new HikariDatabaseConfig(type, url);
        dbConfig.schemaPrefix   = getHostName().toUpperCase() + "_";
        dbConfig.user           = user;
        dbConfig.password       = "password";
        dbConfig.systemUser     = admin;
        dbConfig.systemPassword = "password";
        dbConfig.setAdminUrl(adminUrl);

        return dbConfig;
    }

    private static int index(String s, Character chr) {
        final int n = s.indexOf(chr);
        return n <= 0 ? s.length() : n;
    }

    private static String getHostName() {
        try {
            final String host = InetAddress.getLocalHost().getHostName();
            return host.substring(0, min(index(host, '.'), min(index(host, '_'), index(host, '-'))));
        }
        catch (final UnknownHostException e) {
            return Constants.LOCALHOST;
        }
    }
}
