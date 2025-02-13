
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.database.hikari;

import com.zaxxer.hikari.HikariDataSource;

import tekgenesis.common.core.Times;
import tekgenesis.database.DatabaseConfig;
import tekgenesis.database.DatabaseType;

import static tekgenesis.common.core.Times.MILLIS_SECOND;

/**
 * This class extends {@link DatabaseConfig }.
 */
@SuppressWarnings({ "MagicNumber", "WeakerAccess" })
public class HikariDatabaseConfig extends DatabaseConfig {

    //~ Constructors .................................................................................................................................

    //J-
    /**
     * Pool size
     */
    public int maxPoolSize = 50;

    /** Minimum idle connection. Default maxPoolSize/2*/
    public int minimunIdle = maxPoolSize /2;

    /**
     * Connection Timeout in seconds.
     */
    public int connectionTimeout = 120;

    /** Enables connection watch to look for unclosed connections. You must specified the time in seconds */
    public int leakDetectionThreshold = 10 * Times.SECONDS_MINUTE;
    /** Idle max age in seconds */
    public long idleMaxAge = 10 * 60;

    /** Idle max age in seconds */
    public long maxLifetime = 30 * 60;

    //J+

    /** Empty constructor. */
    public HikariDatabaseConfig() {}

    /** Simple constructor. */
    public HikariDatabaseConfig(DatabaseType type, String jdbcUrl) {
        this.type    = type;
        this.jdbcUrl = jdbcUrl;
    }

    //~ Methods ......................................................................................................................................

    void configure(HikariDataSource ds, boolean system) {
        if (system && systemUser.isEmpty()) throw new IllegalArgumentException("Trying to create a System connection");

        // Todo add new useful configuration parameters
        ds.setDriverClassName(getDriverClass());
        ds.setConnectionTimeout(connectionTimeout * MILLIS_SECOND);
        ds.setJdbcUrl(system ? getAdminUrl() : jdbcUrl);
        ds.setUsername(system ? systemUser : user);
        ds.setPassword(system ? systemPassword : password);
        ds.setRegisterMbeans(true);

        ds.setLeakDetectionThreshold(leakDetectionThreshold * MILLIS_SECOND);
        ds.setMaximumPoolSize(maxPoolSize);

        ds.setIdleTimeout(idleMaxAge * MILLIS_SECOND);
        ds.setMaxLifetime(maxLifetime * MILLIS_SECOND);
        ds.setMinimumIdle(minimunIdle);
        ds.setAutoCommit(false);
    }  // end method configure
}  // end class HikariDatabaseConfig
