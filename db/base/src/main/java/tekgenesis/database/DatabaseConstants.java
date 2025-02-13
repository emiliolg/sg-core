
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.database;

import tekgenesis.database.hikari.HikariDatabaseConfig;

/**
 * Database Constants.
 */
public interface DatabaseConstants {

    //~ Instance Fields ..............................................................................................................................

    String HSQLDB_MEM_URL = "jdbc:hsqldb:mem:mem";

    /** A special database name. Use HSQLDB in memory */
    String               MEM        = "mem";
    @SuppressWarnings("DuplicateStringLiteralInspection")
    HikariDatabaseConfig MEM_CONFIG = new HikariDatabaseConfig(DatabaseType.HSQLDB, HSQLDB_MEM_URL);
}
