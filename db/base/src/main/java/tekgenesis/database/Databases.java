
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.database;

import tekgenesis.common.env.context.Context;
import tekgenesis.transaction.Transaction;
import tekgenesis.transaction.TransactionManager;

import static tekgenesis.common.Predefined.cast;

/**
 * Transaction and Database Utility methods.
 */
public class Databases {

    //~ Constructors .................................................................................................................................

    private Databases() {}

    //~ Methods ......................................................................................................................................

    /** Closes the current transaction. */
    @Deprecated public static void closeTransaction() {
        Transaction.getCurrent().ifPresent(Transaction::close);
    }
    /** Commit the current transaction. */
    @Deprecated public static void commitTransaction() {
        Transaction.getCurrent().ifPresent(Transaction::commit);
    }

    /** Returns the database for the specified schema. */
    public static Database forSchema(String schema) {
        return databaseFactory().forSchema(schema);
    }

    /** Open the specified database. */
    public static Database open(String name) {
        return databaseFactory().open(name);
    }

    /** Returns the default database. */
    public static Database openDefault() {
        return databaseFactory().openDefault();
    }
    /** Rollback the current transaction. */
    @Deprecated public static void rollbackTransaction() {
        Transaction.getCurrent().ifPresent(Transaction::rollback);
    }

    private static DatabaseFactory<DatabaseConfig> databaseFactory() {
        return cast(Context.getSingleton(DatabaseFactory.class));
    }

    /** Get the current Transaction Manager. */
    private static TransactionManager getTransactionManager() {
        return Context.getSingleton(TransactionManager.class);
    }
}  // end class Databases
