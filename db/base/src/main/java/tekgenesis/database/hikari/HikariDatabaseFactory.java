
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.database.hikari;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;

import com.zaxxer.hikari.HikariDataSource;

import org.jetbrains.annotations.NotNull;
import org.slf4j.MDC;

import tekgenesis.common.env.Environment;
import tekgenesis.database.AbstractDatabaseFactory;
import tekgenesis.database.DatabaseFactory;
import tekgenesis.transaction.DataSourceTransactionResource;
import tekgenesis.transaction.TransactionManager;
import tekgenesis.transaction.TransactionResource;

import static tekgenesis.common.Predefined.isEmpty;
import static tekgenesis.common.core.Constants.LIFECYCLE_KEY;
import static tekgenesis.common.core.Strings.truncate;
import static tekgenesis.database.DatabaseConstants.MEM_CONFIG;

/**
 * An implementation of {@link DatabaseFactory} base on Hikari.
 */
public class HikariDatabaseFactory extends AbstractDatabaseFactory<HikariDatabaseConfig> {

    //~ Constructors .................................................................................................................................

    /** Create A HikariDatabaseFactory. */
    public HikariDatabaseFactory(@NotNull Environment env, @NotNull TransactionManager tm) {
        super(env, tm, HikariDatabaseConfig.class, MEM_CONFIG);
    }

    //~ Methods ......................................................................................................................................

    @NotNull @Override protected HikariTR createResource(String name, HikariDatabaseConfig config, boolean system) {
        // Todo manage mutable configuration
        final HikariDataSource ds           = new HikariDataSource();
        final String           resourceName = name + (system ? "-sys" : "");
        ds.setPoolName(resourceName);
        config.configure(ds, system);
        if (config.jmxEnabled) ds.setRegisterMbeans(true);

        return new HikariTR(resourceName, ds, config.type.needsCommitOnClose(), config.type.supportsClientInfo() && config.clientInfoEnabled);
    }

    //~ Inner Classes ................................................................................................................................

    /**
     * A {@link TransactionResource} for a {@link HikariDataSource}.
     */
    class HikariTR extends DataSourceTransactionResource<HikariDataSource> {
        private final boolean supportsClientInfo;

        HikariTR(String name, HikariDataSource dataSource, boolean commitOnClose, boolean supportsClientInfo) {
            super(name, getTransactionManager(), dataSource, commitOnClose);
            this.supportsClientInfo = supportsClientInfo;
        }

        @Override public void close() {
            dataSource.close();
            removeDb(getName());
        }

        @NotNull @Override public Connection createConnection()
            throws SQLException
        {
            final Connection connection = super.createConnection();
            if (supportsClientInfo) {
                final Properties props = new Properties();
                final String     key   = MDC.get(LIFECYCLE_KEY);
                props.setProperty("OCSID.MODULE", truncate(Thread.currentThread().getName() + (isEmpty(key) ? "" : ":" + key), MAX_LENGTH));
                connection.setClientInfo(props);
            }
            return connection;
        }

        private static final int MAX_LENGTH = 48;  // Set Max length
    }                                              // end class HikariTR
}  // end class HikariDatabaseFactory
