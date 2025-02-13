
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
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import tekgenesis.database.DbMacro;
import tekgenesis.database.introspect.exception.IntrospectorException;

import static tekgenesis.common.Predefined.notNull;
import static tekgenesis.common.collections.Colls.emptyIterable;
import static tekgenesis.database.DatabaseType.*;
import static tekgenesis.database.SqlConstants.CURRENT_TIMESTAMP;
import static tekgenesis.database.introspect.MdColumn.*;
import static tekgenesis.database.introspect.SqlKind.*;
import static tekgenesis.database.introspect.SqlType.sqlType;

/**
 * Oracle MetadataRetriever.
 */
@SuppressWarnings("DuplicateStringLiteralInspection")
class OracleMetadataRetriever extends MetadataRetriever {

    //~ Constructors .................................................................................................................................

    public OracleMetadataRetriever(final Connection connection, final DatabaseMetaData metaData) {
        super(connection, metaData);
    }

    //~ Methods ......................................................................................................................................

    @Override public String getViewSql(final SchemaInfo schema, String name) {
        try {
            final ResultSet execute = execute("select COMMENTS from ALL_TAB_COMMENTS where OWNER = '%s' and TABLE_NAME = '%s'",
                    new Object[] { schema.getName(), name });
            while (execute.next())
                return execute.getString(1);
            return "";
        }
        catch (final SQLException e) {
            throw new IntrospectorException(e);
        }
    }

    @Override protected String translateDefault(@Nullable String v, final SqlKind sqlKind) {
        if (v == null) return null;

        final String value = v.trim();
        if (sqlKind == BOOLEAN) return ("0".equals(value) ? DbMacro.False : DbMacro.True).name();
        if (CURRENT_TIMESTAMP.equalsIgnoreCase(value)) return DbMacro.CurrentTime.name();
        if (ORACLE_CURRENT_DATE.equalsIgnoreCase(value)) return DbMacro.CurrentDate.name();
        if (ORACLE_EMPTY_STRING.equalsIgnoreCase(value)) return DbMacro.EmptyString.name();

        return super.translateDefault(value, sqlKind);
    }

    @Override Map<String, TableInfo.Column> retrieveColumns(final TableInfo table) {
        final Map<String, TableInfo.Column> result = new LinkedHashMap<>();
        final SchemaInfo                    schema = table.getSchema();

        for (final MdEntry e : oraGetColumns(schema, table.getName())) {
            final String           columnName = e.getString(C_NAME);
            final TableInfo.Column column     = createColumn(table, columnName, e);
            if (column != null) result.put(columnName, column);
        }
        return result;
    }

    @Override Iterable<MdEntry> getChecks(final SchemaInfo schema, String tableName) {
        return getConstraints(schema, tableName, "C");
    }

    @Override Iterable<MdEntry> getColumns(final SchemaInfo schema, final String tableName) {
        throw new IllegalStateException();
    }

    @Override Iterable<MdEntry> getIndices(final SchemaInfo schema, final String tableName) {
        try {
            // Hack to avoid problem in oracle
            // Todo  use the native query
            // Custom retriever for Oracle
            // select * from all_indexes... all_ind_columns I left outer join dba_ind_expressions D on D.INDEX_NAME = I.INDEX_NAME
            //
            if (tableName.startsWith("_")) return emptyIterable();
            return iterableFrom(metaData.getIndexInfo(schema.getCatalogName(), schema.getName(), tableName, false, true));
        }
        catch (final SQLException e) {
            throw new IntrospectorException(e);
        }
    }

    @Override Iterable<MdEntry> getSequences(final String schema) {
        return iterableFrom(
            "SELECT  SEQUENCE_NAME as SEQ_NAME," +
            "  MIN_VALUE     as SEQ_START," +
            "  MIN_VALUE     as SEQ_MIN," +
            "  MAX_VALUE     as SEQ_MAX," +
            "  INCREMENT_BY  as SEQ_INC," +
            "  ORDER_FLAG    as SEQ_ORDER," +
            "  CACHE_SIZE    as SEQ_CACHE," +
            "  LAST_NUMBER   as SEQ_LAST," +
            "  case when CYCLE_FLAG = 'Y' then 'YES' else 'NO' end as SEQ_CYCLE " +
            "from ALL_SEQUENCES " +
            "where SEQUENCE_OWNER = '%s' " +
            "order by SEQUENCE_NAME",
            schema);
    }

    @Override Iterable<MdEntry> getUniques(final SchemaInfo schema, final String tableName) {
        return getConstraints(schema, tableName, "U");
    }

    private boolean checkBoolean(final TableInfo table, @NotNull final String columnName) {
        for (final TableInfo.Check check : table.getChecks()) {
            if (check.hasColumn(columnName) && check.getCondition().endsWith(" in (0,1)")) return true;
        }
        return false;
    }

    @Nullable
    @SuppressWarnings("MethodWithTooManyParameters")
    private TableInfo.Column createColumn(final TableInfo table, @Nullable final String columnName, final MdEntry e) {
        if (columnName == null) return null;
        final String defaultValue = e.getString(C_DEFAULT);  // retrieve first because Oracle is crazy

        SqlType       sqlType = findType(table, columnName, e);
        final SqlKind kind    = sqlType.getSqlKind();

        // Check serial attribute
        boolean serial       = false;
        String  sequenceName = "";
        if (kind == INT) {
            final String comments = e.getString(C_REMARKS, "").trim();
            serial = comments.contains("Serial(");
            if (serial) {
                if (comments.startsWith("BigSerial")) sqlType = sqlType(sqlType.getName(), BIGINT);
                final String s = comments.substring(comments.indexOf(',') + 1);
                sequenceName = s.endsWith(")") ? s.substring(0, s.length() - 1) : s;
            }
        }

        return table
               .new Column(columnName,
                sqlType,
                e.getInt(C_POSITION),
                e.getYesOrNo(C_NULLABLE),
                serial,
                sequenceName,
                translateDefault(defaultValue, kind));
    }  // end method createColumn

    private SqlType findType(final TableInfo table, final String columnName, final MdEntry e) {
        final String typeName      = e.getString(C_TYPE_NAME, "");
        final int    precision     = e.getInt(ORA_PRECISION, -1);
        final int    decimalDigits = e.getInt(C_DECIMAL_DIGITS);

        final int     p    = typeName.indexOf('(');
        final SqlKind kind = notNull(kindMap.get(p == -1 ? typeName : typeName.substring(0, p)), OTHER);

        SqlType sqlType = sqlType(typeName, kind, precision, decimalDigits);

        if (kind == VARCHAR || kind == NVARCHAR) sqlType = sqlType(typeName, kind, e.getInt(C_SIZE), 0);
        else if (kind == DECIMAL && decimalDigits == 0) {
            if (precision == -1) sqlType = sqlType(typeName, INT);
            else if (precision == 1 && checkBoolean(table, columnName)) sqlType = SqlType.BOOLEAN;
        }
        return sqlType;
    }  // end method findType

    private Iterable<MdEntry> oraGetColumns(final SchemaInfo schema, final String tableName) {
        return iterableFrom(
            "select C.COLUMN_NAME," +
            " C.DATA_TYPE TYPE_NAME," +
            " C.COLUMN_ID ORDINAL_POSITION," +
            " C.CHAR_LENGTH COLUMN_SIZE," +
            " C.DATA_PRECISION ORA_PRECISION," +
            " C.DATA_SCALE DECIMAL_DIGITS," +
            " C.NULLABLE," +
            " C.DATA_DEFAULT COLUMN_DEF," +
            " COM.COMMENTS REMARKS " +
            "from ALL_TAB_COLUMNS C " +
            "left outer join ALL_COL_COMMENTS COM " +
            "on (C.OWNER = COM.OWNER and C.TABLE_NAME = COM.TABLE_NAME and C.COLUMN_NAME = COM.COLUMN_NAME)" +
            "where C.OWNER = '%s' and C.TABLE_NAME = '%s'",
            schema,
            tableName);
    }

    private Iterable<MdEntry> getConstraints(final SchemaInfo schema, final String tableName, final String type) {
        return iterableFrom(
            "select   C.CONSTRAINT_NAME," +
            "         CC.COLUMN_NAME," +
            "         CC.POSITION                                          as ORDINAL_POSITION," +
            "         C.SEARCH_CONDITION                                   as CHECK_CONDITION," +
            "         case when C.STATUS = 'ENABLED' then 'Y' else 'N' end as CONSTRAINT_ENABLED " +
            "from ALL_CONSTRAINTS C " +
            "join ALL_CONS_COLUMNS CC on (" +
            "     CC.TABLE_NAME      = C.TABLE_NAME and " +
            "     CC.OWNER           = C.OWNER and " +
            "     CC.CONSTRAINT_NAME = C.CONSTRAINT_NAME)" +
            "where C.OWNER = '%s' and C.TABLE_NAME = '%s' and C.CONSTRAINT_TYPE = '%s'",
            schema.getName(),
            tableName,
            type);
    }

    //~ Static Fields ................................................................................................................................

    private static final int INT_PRECISION = -127;

    private static final Map<String, SqlKind> kindMap = new HashMap<>();
    private static final T[]                  ts      = {
        new T(VARCHAR, "VARCHAR2", "VARCHAR", "CHAR"),
        new T(NVARCHAR, "NVARCHAR2", "NVARCHAR", "NCHAR"),
        new T(DATETIME, "TIMESTAMP"),
        new T(DATE, "DATE"),
        new T(DECIMAL, "NUMBER"),
        new T(BINARY, "RAW", "LONG RAW"),
        new T(CLOB, "CLOB", "NCLOB", "LONG"),
        new T(BLOB, "BLOB"),
        new T(XML, "XMLType"),
        new T(ROW_ID, "ROWID"),
        new T(DOUBLE, "FLOAT", "BINARY_DOUBLE", "BINARY_FLOAT"),
    };

    static {
        for (final T t : ts) {
            for (final String name : t.names)
                kindMap.put(name, t.k);
        }
    }

    //~ Inner Classes ................................................................................................................................

    private static class T {
        final SqlKind  k;
        final String[] names;

        T(final SqlKind k, final String... names) {
            this.k     = k;
            this.names = names;
        }
    }
}  // end class OracleMetadataRetriever
