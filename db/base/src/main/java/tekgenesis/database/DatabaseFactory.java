
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.database;

import java.io.Writer;

import org.jetbrains.annotations.NotNull;

import tekgenesis.transaction.TransactionManager;

/**
 * The DatabaseFactory this is used to create a configured database.
 */
public interface DatabaseFactory<T extends DatabaseConfig> {

    //~ Methods ......................................................................................................................................

    /** Create an alias of this database with system credentials. */
    Database createSystemAlias(Database database);

    /** Dry run the database initialization. */
    SchemaDefinition.ChangeLevel dryInitialize(final String name, T config, boolean reset, @NotNull Writer dryWriter);

    /** Open the databases for the specified schema. */
    Database forSchema(String schemaName);

    /** Initialize the Default database with the specified configuration. */
    Database initialize(final String name, T config, boolean reset, String... schemas);

    /** Open the database with the specified name and configuration. */
    Database open(String name);

    /** Open the database with the specified name and configuration. */
    @NotNull Database open(String name, T config);

    /** Open the Default database. */
    Database openDefault();

    /** Shutdown the Factory. */
    void shutdown();

    /** Get the transaction Manager. */
    TransactionManager getTransactionManager();
}  // end interface DatabaseFactory
