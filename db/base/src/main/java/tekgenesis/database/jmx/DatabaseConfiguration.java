
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.database.jmx;

import com.zaxxer.hikari.HikariConfig;

import org.jetbrains.annotations.NotNull;

/**
 * Hikari DatabaseConfiguration implementation for JMX.
 */
public class DatabaseConfiguration implements DatabaseConfigurationMBean {

    //~ Instance Fields ..............................................................................................................................

    private final HikariConfig config;

    //~ Constructors .................................................................................................................................

    /** Constructs a HikariDatabaseConfiguration. */
    public DatabaseConfiguration(@NotNull HikariConfig config) {
        this.config = config;
    }

    //~ Methods ......................................................................................................................................

    @Override public boolean isLogStatementsEnabled() {
        return false;
    }

    @Override public String getJdbcUrl() {
        return config.getJdbcUrl();
    }

    @Override public String getUsername() {
        return config.getUsername();
    }
}
