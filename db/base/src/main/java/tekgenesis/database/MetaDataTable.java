
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.database;

/**
 * Metadata Table.
 */

public interface MetaDataTable {

    //~ Instance Fields ..............................................................................................................................

    String NAME               = "_METADATA";
    String OVERLAY_COL        = "OVERLAY";
    String SCHEMA_COL         = "SCHEMA";
    int    SHA_COL_LENGTH     = 128;
    int    VERSION_COL_LENGTH = 24;
}
