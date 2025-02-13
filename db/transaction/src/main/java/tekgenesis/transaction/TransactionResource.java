
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.transaction;

import java.io.Closeable;
import java.sql.Connection;
import java.sql.SQLException;

import org.jetbrains.annotations.NotNull;

import tekgenesis.common.logging.Logger;

/**
 * A Interface for Resources that will be enlisted in a Transaction (Typically a DataSource).
 */
public interface TransactionResource<T extends Connection> extends Closeable {

    //~ Methods ......................................................................................................................................

    @Override default void close() {}

    /** Mark the resource as the last to be closed. */
    default boolean closeLast() {
        return false;
    }

    /**
     * Commit the transaction when closing the connection. This is needed for postgres because of
     * some bug
     */
    default boolean commitOnClose() {
        return false;
    }

    /** Create a new Connection. */
    @NotNull T createConnection()
        throws SQLException;

    /** end batch for the specified connection in this TransactionResource. */
    default void endBatch(T connection) {}

    /** Return the symbolic name of Resource. */
    @NotNull String getName();

    //~ Inner Classes ................................................................................................................................

    abstract class Default<T extends Connection> implements TransactionResource<T> {
        @NotNull private final String    name;
        private final TransactionManager tm;

        protected Default(@NotNull String name, @NotNull TransactionManager tm) {
            this.name = name;
            this.tm   = tm;
        }

        @Override public String toString() {
            return name;
        }

        /** Get the current Connection. */
        public T getConnection(boolean startTransaction) {
            try {
                final ConnectionReference<T> cr = tm.getConnectionRef(this);
                if (startTransaction) cr.ensureTransactionStarted();
                final T t = cr.get();
                cr.detach();
                return t;
            }
            catch (final SQLException e) {
                Logger.getLogger(TransactionResource.class).error(e);
                throw new RuntimeException(e);
            }
        }

        @NotNull @Override public final String getName() {
            return name;
        }
    }
}  // end interface TransactionResource
