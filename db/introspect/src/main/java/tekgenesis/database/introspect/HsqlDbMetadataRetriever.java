
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.database.introspect;

import java.sql.Connection;
import java.sql.DatabaseMetaData;

import static tekgenesis.database.introspect.SqlKind.*;
import static tekgenesis.database.introspect.SqlType.sqlType;

/**
 * Oracle MetadataRetriever.
 */
class HsqlDbMetadataRetriever extends MetadataRetriever {

    //~ Constructors .................................................................................................................................

    public HsqlDbMetadataRetriever(final Connection connection, final DatabaseMetaData metaData) {
        super(connection, metaData);
    }

    //~ Methods ......................................................................................................................................

    @Override SqlType retrieveType(final String typeName, final int dataType, final int size, final int precision) {
        final SqlKind kind = kindFor(dataType);
        if (kind == VARCHAR) return sqlType(typeName, NVARCHAR, size, 0);
        if (kind == DATETIME) return sqlType(typeName, DATETIME, 0, size - DATETIME_BASE);
        return sqlType(typeName, kind, size, precision);
    }

    // @Override Iterable<MdEntry> getUniques(final SchemaInfo schema, String tableName) {
    // return iterableFrom("select CONSTRAINT_NAME, COLUMN_NAME, ORDINAL_POSITION" +
    // " from INFORMATION_SCHEMA.TABLE_CONSTRAINTS T" +
    // " join INFORMATION_SCHEMA.KEY_COLUMN_USAGE C on C.CONSTRAINT_NAME = T.CONSTRAINT_NAME" +
    // " where TABLE_CATALOG = '%s' and TABLE_SCHEMA = '%s' and TABLE_NAME = '%s' and CONSTRAINT_TYPE = 'UNIQUE' ",
    // schema.getCatalogName(),
    // schema.getName(),
    // tableName);
    // }

    @Override Iterable<MdEntry> getColumns(final SchemaInfo schema, final String tableName) {
        return iterableFrom(
            "select TABLE_CAT," +
            " TABLE_SCHEM," +
            " TABLE_NAME," +
            " COLUMN_NAME," +
            " DATA_TYPE," +
            " TYPE_NAME," +
            " COLUMN_SIZE," +
            " DECIMAL_DIGITS," +
            " ORDINAL_POSITION," +
            " NULLABLE," +
            " COLUMN_DEF," +
            " IS_AUTOINCREMENT," +
            " IS_GENERATEDCOLUMN," +
            " S.SEQUENCE_NAME " +
            "from INFORMATION_SCHEMA.SYSTEM_COLUMNS C " +
            "left outer join INFORMATION_SCHEMA.SYSTEM_COLUMN_SEQUENCE_USAGE S " +
            "on C.TABLE_CAT = S.TABLE_CATALOG and C.TABLE_SCHEM = S.TABLE_SCHEMA and C.TABLE_NAME = S.TABLE_NAME and S.COLUMN_NAME = C.COLUMN_NAME " +
            "where C.TABLE_SCHEM = '%s' and C.TABLE_NAME = '%s'",
            schema,
            tableName);
    }

    //~ Static Fields ................................................................................................................................

    private static final int DATETIME_BASE = 20;
}  // end class HsqlDbMetadataRetriever
