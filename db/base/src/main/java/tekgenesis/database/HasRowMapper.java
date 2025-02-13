
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
 * A marker for a class that provides a {@link RowMapper} The RowMapper will be retrieved by
 * reflection invoking the static method rowMapper.
 */
public interface HasRowMapper {

    //~ Instance Fields ..............................................................................................................................

    @SuppressWarnings("DuplicateStringLiteralInspection")
    String ROW_MAPPER = "rowMapper";
}
