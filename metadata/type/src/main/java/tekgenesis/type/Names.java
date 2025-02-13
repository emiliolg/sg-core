
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.type;

import org.jetbrains.annotations.NotNull;

import static tekgenesis.common.core.Constants.MAX_DB_ID_LENGTH;
import static tekgenesis.common.core.QName.extractName;
import static tekgenesis.common.core.Strings.fromCamelCase;
import static tekgenesis.common.core.Strings.truncate;

/**
 * Resolve names of sequences, tables, columns, etc.
 */
public class Names {

    //~ Constructors .................................................................................................................................

    private Names() {}

    //~ Methods ......................................................................................................................................

    /** Return the completename of a table(might me truncated if length is too long). */
    public static String longTableName(String entityName) {
        return fromCamelCase(extractName(entityName));
    }

    /** Return the name of a table given the entity name. */
    public static String tableName(String entityName) {
        return truncate(longTableName(entityName), MAX_DB_ID_LENGTH);
    }

    /** Validates an schema Id given the package name. */
    @NotNull public static String validateSchemaId(@NotNull String schemaId, @NotNull String pkg) {
        return fromCamelCase(schemaId.isEmpty() ? extractName(pkg) : schemaId);
    }
}  // end class Names
