
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.transaction;

import java.io.PrintWriter;
import java.sql.Connection;

import org.jetbrains.annotations.Nullable;

/**
 * A reference to a Connection.
 */
public interface ConnectionReference<T extends Connection> {

    //~ Methods ......................................................................................................................................

    /** Detach from current connection (decrements reference count. */
    default void detach() {}

    /** Detach a registered object (decrements reference count. */
    default void detach(AutoCloseable resource) {}

    /**
     * Return a Writer to output the statements to be executed, for 'dry' connections. It returs
     * null for 'real' connections
     */
    @Nullable default PrintWriter dryWriter() {
        return null;
    }

    /** Ensure a transaction is started. */
    default void ensureTransactionStarted() {}

    /** Get the connection. And increments the reference count */
    T get();

    /** Get the connection. And register the Object that is using it */
    default T get(AutoCloseable resource) {
        return get();
    }
}
