
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.database.jmx;

/**
 * Database Config MBean.
 */
@SuppressWarnings("WeakerAccess")
public interface DatabaseConfigurationMBean {

    //~ Methods ......................................................................................................................................

    /**
     * Returns true if SQL logging is currently enabled, false otherwise.
     *
     * @return  the logStatementsEnabled status
     */
    boolean isLogStatementsEnabled();

    /**
     * Gets the configured JDBC URL.
     *
     * @return  jdbcUrl
     */
    String getJdbcUrl();

    /**
     * Gets username to use for the connections.
     *
     * @return  username
     */
    String getUsername();
}
