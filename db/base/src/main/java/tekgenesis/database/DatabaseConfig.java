
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.database;

import javax.inject.Named;

import tekgenesis.common.core.Constants;
import tekgenesis.common.env.Properties;

import static tekgenesis.common.Predefined.isEmpty;

/**
 * Basic Configuration values for a {@link Database} Usually this will be inherit for the different
 * implementations.
 */
@Named("database")
@SuppressWarnings({ "WeakerAccess", "FieldCanBeLocal", "FieldMayBeFinal", "MagicNumber", "DuplicateStringLiteralInspection" })
public class DatabaseConfig implements Properties {

    //~ Instance Fields ..............................................................................................................................

    /** Sets if the current schema will be upgrated automatically. */
    public boolean autoUpgrade = false;
    /** Enable ClientInfo in jdbc connection. */
    public boolean clientInfoEnabled = true;

    /** JDBC Driver. Empty means using the default one for the specified DatabaseType */
    public String driverClass = "";

    /** Sets if the current schema will be check with the last version schema. */
    public boolean enforceVersion = true;

    /** The Database Type. */
    public DatabaseType type = DatabaseType.HSQLDB;

    @SuppressWarnings("DuplicateStringLiteralInspection")
    private final String HSQL_URL = System.getProperty(Constants.APPLICATION_RUN_DIR, "").isEmpty()
                                    ? "jdbc:hsqldb:file:suigenDb"
                                    : "jdbc:hsqldb:file:" + System.getProperty(Constants.APPLICATION_RUN_DIR) + "/suigenDB";

    /** Database URL. */
    public String jdbcUrl = HSQL_URL;

    /** Enables the JMX management bean for database configuration. */
    public boolean jmxEnabled = true;
    /** If enabled, log SQL statements being executed. */
    public boolean logStatementsEnabled = false;
    /**
     * The maximum connection time in mi`nutes. If the database support it the user will be logged of
     * after this time expires 0 means unlimited. Beware, this is used by the createDatabase method
     * so if the database is already created it will not be changed
     */
    public int maxConnectionTime = 0;

    /** Password. */
    public String password = "";

    /** The string to prepend to the schema name. */
    public String schemaPrefix = "";

    /** Select queries cache enabled. */
    public boolean selectCacheEnabled = true;

    /** The size of the local, per session, cache. */
    public int sessionCacheSize = 5_000;

    /**
     * The number of seconds the driver will wait for a <code>Statement</code> to execute, 0 means
     * no timeout.
     */
    public int statementTimeout = 0;                                          // 10 * 60;

    /** The system password. */
    public String systemPassword = "";

    /** The system user (Useful for testing, to create/destroy databases). */
    public String systemUser = "SA";

    /** User Name. */
    public String user = "SA";

    /** The Administration URL, if different (For example the template database for postgres). */
    private String adminUrl = "";

    //~ Methods ......................................................................................................................................

    /** Get the admin url. */
    public String getAdminUrl() {
        return isEmpty(adminUrl) ? jdbcUrl : adminUrl;
    }

    /** Set the admin url. */
    public void setAdminUrl(String adminUrl) {
        this.adminUrl = adminUrl;
    }
    /** Get the driver Class. */
    protected String getDriverClass() {
        return isEmpty(driverClass) ? type.getDefaultDriverClassName() : driverClass;
    }
}  // end class DatabaseConfig
