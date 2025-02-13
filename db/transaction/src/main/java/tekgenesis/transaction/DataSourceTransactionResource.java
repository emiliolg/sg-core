
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.transaction;

import java.sql.Connection;
import java.sql.SQLException;

import javax.sql.DataSource;

import org.jetbrains.annotations.NotNull;

/**
 * A DataSource based Transaction Resource.
 */
public abstract class DataSourceTransactionResource<T extends DataSource> extends TransactionResource.Default<Connection> {

    //~ Instance Fields ..............................................................................................................................

    @NotNull protected final T dataSource;
    private final boolean      commitOnClose;

    //~ Constructors .................................................................................................................................

    protected DataSourceTransactionResource(@NotNull String name, final TransactionManager tm, @NotNull T dataSource, boolean commitOnClose) {
        super(name, tm);
        this.dataSource = dataSource;

        this.commitOnClose = commitOnClose;
    }

    //~ Methods ......................................................................................................................................

    @Override public boolean closeLast() {
        return true;
    }

    public boolean commitOnClose() {
        return commitOnClose;
    }

    @NotNull @Override public Connection createConnection()
        throws SQLException
    {
        return dataSource.getConnection();
    }
}
