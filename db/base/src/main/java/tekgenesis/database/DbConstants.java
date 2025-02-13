
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.database;

import org.jetbrains.annotations.NonNls;

/**
 * Constants for DB.
 */
public interface DbConstants {

    //~ Instance Fields ..............................................................................................................................

    @NonNls String CANNOT_GET_REVERSE_FIELD_VALUE = "Cannot get reverse field value";

    char           EMPTY_CHAR = '\u00A0';
    @NonNls String QNAME      = "QName(%s, %s)";

    String SCHEMA_SG = "Schema(SG)";

    // Macros

    @NonNls String SQL_INDEX_TABLE_SPACE = "IndexTableSpace";

    @NonNls String SQL_USER_PASSWORD = "$USER_PASSWORD";

    String         SUIGENERIS_SCHEMA = "SG";
    @NonNls
    @SuppressWarnings("DuplicateStringLiteralInspection")
    String         UNDEFINED = "undefined";
}  // end class DbConstants
