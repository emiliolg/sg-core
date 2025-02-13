
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.database.exception;

import tekgenesis.database.DatabaseConfig;

import static java.lang.String.format;

/**
 * Database exception thrown when a result set did not have the correct column count, for example
 * when expecting a single column but getting 0 or more than 1 columns.
 */
public class DatabaseNotFoundException extends DatabaseException {

    //~ Instance Fields ..............................................................................................................................

    private final DatabaseConfig config;
    private final String         name;

    //~ Constructors .................................................................................................................................

    /** Constructor for the Exception. */
    public DatabaseNotFoundException(String name, DatabaseConfig config) {
        super(format("Database: '%s' not found.", name), false);
        this.name   = name;
        this.config = config;
    }

    //~ Methods ......................................................................................................................................

    /** Returns the Configuration causing the Exception. */
    public DatabaseConfig getConfig() {
        return config;
    }

    /** Returns the name of the database causing the Exception. */
    public String getName() {
        return name;
    }

    //~ Static Fields ................................................................................................................................

    private static final long serialVersionUID = -1119437178992621464L;
}
