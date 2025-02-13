
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.database.introspect;

import java.sql.*;
import java.util.*;

import org.intellij.lang.annotations.PrintFormat;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import tekgenesis.common.collections.ImmutableIterator;
import tekgenesis.common.collections.ext.BitSet;
import tekgenesis.common.core.QName;
import tekgenesis.database.DatabaseType;
import tekgenesis.database.DbMacro;
import tekgenesis.database.introspect.exception.IntrospectorException;
import tekgenesis.database.support.JdbcUtils;

import static java.lang.Integer.parseInt;
import static java.lang.String.format;
import static java.sql.DatabaseMetaData.columnNullable;

import static tekgenesis.common.Predefined.notNull;
import static tekgenesis.common.collections.Colls.emptyIterable;
import static tekgenesis.common.core.Strings.unquote;
import static tekgenesis.database.introspect.MdColumn.*;
import static tekgenesis.database.introspect.SqlKind.kindFor;
import static tekgenesis.database.introspect.SqlType.sqlType;
import static tekgenesis.database.introspect.TableInfo.*;

/**
 * Retriever of Metadata from Database.
 */
@SuppressWarnings("DuplicateStringLiteralInspection")
public class MetadataRetriever {

    //~ Instance Fields ..............................................................................................................................

    final DatabaseMetaData metaData;

    private final Connection connection;

    //~ Constructors .................................................................................................................................

    MetadataRetriever(final Connection connection, final DatabaseMetaData metaData) {
        this.connection = connection;
        this.metaData   = metaData;
    }

    //~ Methods ......................................................................................................................................

    public Iterable<MdEntry> getSchemas() {
        try {
            return iterableFrom(metaData.getSchemas());
        }
        catch (final SQLException e) {
            throw new IntrospectorException(e);
        }
    }

    protected ResultSet execute(@PrintFormat final String sql, final Object[] params) {
        final ResultSet resultSet;
        try {
            final Statement stmt   = connection.createStatement();
            final String    format = format(sql, params);
            stmt.execute(format);
            resultSet = stmt.getResultSet();
        }
        catch (final SQLException e) {
            throw new IntrospectorException(e);
        }
        return resultSet;
    }

    @Nullable protected String translateDefault(@Nullable final String value, final SqlKind sqlKind) {
        if (value == null || "NULL".equalsIgnoreCase(value)) return null;
        switch (sqlKind) {
        case BOOLEAN:
            return checkMacros(value, DbMacro.False, DbMacro.True);
        case DOUBLE:
            return value.endsWith("E0") ? value.substring(0, value.length() - 2) : value.replace('E', 'e');
        case VARCHAR:
        case NVARCHAR:
            return "''".equals(value) ? DbMacro.EmptyString.name() : value;
        case DATE:
            return checkMacro(value, DbMacro.DbCurrentDate, DbMacro.CurrentDate);
        case DATETIME:
            return checkMacro(value, DbMacro.DbCurrentTime, DbMacro.CurrentTime);
        default:
            return value;
        }
    }

    Iterable<MdEntry> iterableFrom(@PrintFormat final String sql, Object... params) {
        return iterableFrom(execute(sql, params));
    }

    Map<String, ? extends TableInfo.TableObject<?>> retrieve(final TableInfo tableInfo, final Element element) {
        switch (element) {
        case COLUMN:
            return retrieveColumns(tableInfo);
        case CHECK:
            return retrieveChecks(tableInfo);
        case FOREIGN_KEY:
            return retrieveForeignKeys(tableInfo);
        case INDEX:
            final LinkedHashMap<String, TableInfo.Index> indices = new LinkedHashMap<>();
            for (final TableInfo.Index index : retrieveIndices(tableInfo)) {
                if (!index.isSystem()) indices.put(index.getName(), index);
            }
            return indices;
        case UNIQUE:
            return retrieveUniques(tableInfo);
        }
        throw new IllegalStateException("Invalid Element" + element);
    }

    Map<String, TableInfo.Column> retrieveColumns(final TableInfo table) {
        final Map<String, TableInfo.Column> result = new LinkedHashMap<>();
        final SchemaInfo                    schema = table.getSchema();
        for (final MdEntry e : getColumns(schema, table.getName())) {
            // Get the "COLUMN_DEF" value first as it the Oracle drivers don't handle it properly otherwise.
            final String defaultValue = e.getString(C_DEFAULT);
            //
            final String schemaName = e.getString(C_SCHEMA);
            final String tableName  = e.getString(C_TABLE);
            final String columnName = e.getString(C_NAME);
            // Just to be sure that '_' are not being mistaken by wildcards */
            if (columnName != null && schema.getName().equals(schemaName) && table.getName().equals(tableName)) {
                final String           typeName = e.getString(C_TYPE_NAME);
                final SqlType          type     = typeName == null
                                                  ? SqlType.UNKNOWN
                                                  : retrieveType(typeName, e.getInt(C_DATA_TYPE), e.getInt(C_SIZE), e.getInt(C_DECIMAL_DIGITS));
                final TableInfo.Column c        = createColumn(columnName,
                        type,
                        e.getInt(C_POSITION),
                        e.getInt(C_NULLABLE) == columnNullable,
                        translateDefault(defaultValue, type.getSqlKind()),
                        e.getYesOrNo(C_AUTO_INCREMENT),
                        e.getString(C_SEQUENCE, ""),
                        table);
                result.put(c.getName(), c);
            }
        }
        return result;
    }

    List<TableInfo.Index> retrieveIndices(final TableInfo tableInfo) {
        final List<TableInfo.Index>     result     = new ArrayList<>();
        final SchemaInfo                schema     = tableInfo.getSchema();
        final Map<Integer, IndexColumn> idxColumns = new TreeMap<>();
        final TableInfo.PrimaryKey      pk         = tableInfo.getPrimaryKey();

        // Todo
        // Custom retriever for Oracle
        // select * from all_indexes... all_ind_columns I left outer join dba_ind_expressions D on D.INDEX_NAME = I.INDEX_NAME
        //
        String  lastIndexName = "";
        boolean unique        = false;
        for (final MdEntry e : getIndices(schema, tableInfo.getName())) {
            final String indexName = notNull(e.getString(IX_NAME));
            final int    type      = e.getInt(IX_TYPE);

            if (!indexName.isEmpty() && !pk.isPrimaryKey(indexName) && type != 0 && tableInfo.getUnique(indexName) == null) {
                if (!indexName.equals(lastIndexName)) {
                    tableInfo.addIndex(result, lastIndexName, unique, idxColumns);
                    lastIndexName = indexName;
                }
                unique = !e.getBoolean(IX_NON_UNIQUE);

                final String columnName = unquote(notNull(e.getString(IX_COLUMN_NAME)));
                if (!columnName.isEmpty()) idxColumns.put(e.getInt(IX_POSITION), new IndexColumn(columnName, "D".equals(e.getString(IX_SORT))));
            }
        }
        tableInfo.addIndex(result, lastIndexName, unique, idxColumns);
        return result;
    }

    TableInfo.PrimaryKey retrievePrimaryKey(final TableInfo tableInfo) {
        final SchemaInfo                     schema         = tableInfo.getSchema();
        final Map<Integer, TableInfo.Column> pkColumns      = new TreeMap<>();
        String                               primaryKeyName = null;
        for (final MdEntry rs : getPrimaryKeyCols(schema, tableInfo.getName())) {
            primaryKeyName = rs.getString(PK_NAME);
            final String columnName = rs.getString(PK_COLUMN_NAME);
            if (columnName != null) {
                final int seq = parseInt(notNull(rs.getString(PK_COL_SEQ), "0"));
                pkColumns.put(seq, tableInfo.getColumn(columnName));
            }
        }
        return tableInfo.new PrimaryKey(notNull(primaryKeyName), pkColumns.values());
    }

    SqlType retrieveType(final String typeName, final int dataType, final int size, final int precision) {
        return sqlType(typeName, kindFor(dataType), size, precision);
    }

    Iterable<MdEntry> getChecks(final SchemaInfo schema, String tableName) {
        return emptyIterable();
    }

    Iterable<MdEntry> getColumns(SchemaInfo schema, String tableName) {
        try {
            return iterableFrom(metaData.getColumns(schema.getCatalogName(), schema.getName(), tableName, null));
        }
        catch (final SQLException e) {
            throw new IntrospectorException(e);
        }
    }

    Iterable<MdEntry> getForeignKeys(final SchemaInfo schema, final String tableName) {
        try {
            return iterableFrom(metaData.getImportedKeys(schema.getCatalogName(), schema.getName(), tableName));
        }
        catch (final SQLException e) {
            throw new IntrospectorException(e);
        }
    }

    Iterable<MdEntry> getIndices(final SchemaInfo schema, final String tableName) {
        try {
            return iterableFrom(metaData.getIndexInfo(schema.getCatalogName(), schema.getName(), tableName, false, true));
        }
        catch (final SQLException e) {
            throw new IntrospectorException(e);
        }
    }

    Iterable<MdEntry> getPrimaryKeyCols(SchemaInfo schema, String tableName) {
        try {
            return iterableFrom(metaData.getPrimaryKeys(schema.getCatalogName(), schema.getName(), tableName));
        }
        catch (final SQLException e) {
            throw new IntrospectorException(e);
        }
    }

    Iterable<MdEntry> getSequences(final String schema) {
        return iterableFrom(
            "select  " +
            "   START_WITH    as SEQ_START," +
            "   SEQUENCE_NAME as SEQ_NAME," +
            "   MINIMUM_VALUE as SEQ_MIN," +
            "   MAXIMUM_VALUE as SEQ_MAX," +
            "   INCREMENT     as SEQ_INC," +
            "   CYCLE_OPTION  as SEQ_CYCLE," +
            "   1             as SEQ_CACHE " +
            "from INFORMATION_SCHEMA.SEQUENCES " +
            "where SEQUENCE_SCHEMA = '%s' " +
            "order by SEQUENCE_NAME ",
            schema);
    }

    Iterable<MdEntry> getTables(final String catalog, final String schemaName, final String tablePattern, final EnumSet<TableType> types) {
        try {
            return iterableFrom(metaData.getTables(catalog, schemaName, tablePattern, TableType.setToArray(types)));
        }
        catch (final SQLException e) {
            throw new IntrospectorException(e);
        }
    }

    Iterable<MdEntry> getUniques(final SchemaInfo schema, final String tableName) {
        return iterableFrom(
            "select   T.CONSTRAINT_NAME," +
            "         C.COLUMN_NAME," +
            "         C.ORDINAL_POSITION " +
            "from INFORMATION_SCHEMA.TABLE_CONSTRAINTS T " +
            "join INFORMATION_SCHEMA.KEY_COLUMN_USAGE C on (" +
            "   T.CONSTRAINT_NAME   = C.CONSTRAINT_NAME and" +
            "   T.CONSTRAINT_SCHEMA = C.CONSTRAINT_SCHEMA and" +
            "   T.CONSTRAINT_CATALOG = C.CONSTRAINT_CATALOG) " +
            "where T.TABLE_CATALOG = '%s' and T.TABLE_SCHEMA = '%s' and T.TABLE_NAME = '%s' and T.CONSTRAINT_TYPE = 'UNIQUE'",
            schema.getCatalogName(),
            schema.getName(),
            tableName);
    }

    String getViewSql(final SchemaInfo schema, String name) {
        try {
            final ResultSet execute = execute(
                    "select COMMENT from INFORMATION_SCHEMA.SYSTEM_COMMENTS where OBJECT_CATALOG = '%s' and OBJECT_SCHEMA='%s' and OBJECT_NAME = '%s'",
                    new Object[] { schema.getCatalogName(), schema.getName(), name });
            while (execute.next())
                return execute.getString(1);
            return "";
        }
        catch (final SQLException e) {
            throw new IntrospectorException(e);
        }
    }

    private TableInfo.Column createColumn(final String name, final SqlType type, final int position, final boolean nullable,
                                          @Nullable final String defaultValue, final boolean autoIncremented, final String sequenceName,
                                          final TableInfo table) {
        return table.new Column(name, type, position, nullable, autoIncremented, sequenceName, defaultValue);
    }

    private Map<String, TableInfo.Check> retrieveChecks(final TableInfo tableInfo) {
        final Map<String, TableInfo.Check> result = new LinkedHashMap<>();
        TableInfo.Check                    check  = null;
        for (final MdEntry e : getChecks(tableInfo.getSchema(), tableInfo.getName())) {
            final String name = e.getString(CONSTRAINT_NAME, "");
            if (check == null || !check.getName().equals(name)) {
                check = tableInfo.new Check(name, e.getString(CHECK_CONDITION, ""), e.getYesOrNo(CONSTRAINT_ENABLED));
                result.put(name, check);
            }
            check.cols.add(e.getString(C_NAME));
        }

        return result;
    }

    private Map<String, TableInfo.ForeignKey> retrieveForeignKeys(final TableInfo tableInfo) {
        final Map<String, TableInfo.ForeignKey> result    = new LinkedHashMap<>();
        final SchemaInfo                        schema    = tableInfo.getSchema();
        final Map<Integer, FkColumn>            fkColumns = new TreeMap<>();

        String lastFkName = "";
        QName  references = QName.EMPTY;
        for (final MdEntry rs : getForeignKeys(schema, tableInfo.getName())) {
            final String fkName = notNull(rs.getString(FK_NAME));
            if (!fkName.isEmpty()) {
                if (!fkName.equals(lastFkName)) {
                    tableInfo.addFk(result, lastFkName, references, fkColumns);
                    lastFkName = fkName;
                    references = QName.createQName(notNull(rs.getString(FK_PK_SCHEMA)), notNull(rs.getString(FK_PK_TABLE)));
                }

                final String columnName   = unquote(notNull(rs.getString(FK_COLUMN_NAME)));
                final String pkColumnName = unquote(notNull(rs.getString(FK_PK_COLUMN_NAME)));
                if (!columnName.isEmpty() && !pkColumnName.isEmpty()) fkColumns.put(rs.getInt(PK_COL_SEQ), new FkColumn(columnName, pkColumnName));
            }
        }
        tableInfo.addFk(result, lastFkName, references, fkColumns);
        return result;
    }

    private Map<String, TableInfo.Index> retrieveUniques(final TableInfo tableInfo) {
        final Map<String, TableInfo.Index> result     = new LinkedHashMap<>();
        final Map<Integer, IndexColumn>    idxColumns = new TreeMap<>();

        String lastName = "";
        for (final MdEntry e : getUniques(tableInfo.getSchema(), tableInfo.getName())) {
            final String name = notNull(e.getString(CONSTRAINT_NAME));

            if (!name.isEmpty()) {
                if (!name.equals(lastName)) {
                    addUnique(result, tableInfo, lastName, idxColumns);
                    lastName = name;
                }
                final String columnName = unquote(notNull(e.getString(C_NAME)));
                if (!columnName.isEmpty()) idxColumns.put(e.getInt(C_POSITION), new IndexColumn(columnName, false));
            }
        }
        addUnique(result, tableInfo, lastName, idxColumns);

        return result;
    }

    //~ Methods ......................................................................................................................................

    /** Create MetadataRetriever. */
    public static MetadataRetriever createRetriever(DatabaseType dbType, final Connection connection, final DatabaseMetaData metaData) {
        switch (dbType) {
        case ORACLE:
            return new OracleMetadataRetriever(connection, metaData);
        case HSQLDB:
        case HSQLDB_NOSEQ:
            return new HsqlDbMetadataRetriever(connection, metaData);
        case POSTGRES:
            return new PostgresMetadataRetriever(connection, metaData);
        default:
            return new MetadataRetriever(connection, metaData);
        }
    }

    static String checkMacro(String value, DbMacro macroToTest, DbMacro macroToReturn) {
        return macroToTest.getStringValue().equalsIgnoreCase(value) ? macroToReturn.name() : value;
    }

    static String checkMacros(String value, DbMacro... macros) {
        for (final DbMacro macro : macros) {
            if (macro.getStringValue().equalsIgnoreCase(value)) return macro.name();
        }
        return value;
    }

    static Iterable<MdEntry> iterableFrom(@NotNull final ResultSet rs) {
        return new MdEntryIterable(rs);
    }

    private static void addUnique(final Map<String, TableInfo.Index> result, final TableInfo tableInfo, final String name,
                                  final Map<Integer, IndexColumn> idxColumns) {
        if (!name.isEmpty()) result.put(name, tableInfo.new Index(name, true, idxColumns.values()));
        idxColumns.clear();
    }

    //~ Inner Classes ................................................................................................................................

    private static class MdEntryIterable implements Iterable<MdEntry> {
        private final Map<String, Integer> columnMap;
        private final MdEntry              mdEntry;
        private final ResultSet            rs;
        private final BitSet               unReadColumns;

        public MdEntryIterable(@NotNull ResultSet rs) {
            try {
                /** A hint to the driver */
                // noinspection MagicNumber
                rs.setFetchSize(20);
            }
            catch (final NullPointerException | SQLException ignore) {}
            this.rs       = rs;
            columnMap     = JdbcUtils.loadColumns(rs, true);
            unReadColumns = new BitSet(columnMap.size() + 1);
            mdEntry       = new MdEntry(rs, columnMap, unReadColumns);
        }

        @Override public Iterator<MdEntry> iterator() {
            return new ImmutableIterator<MdEntry>() {
                @Override public boolean hasNext() {
                    try {
                        return rs.next();
                    }
                    catch (final SQLException e) {
                        return false;
                    }
                }

                @Override public MdEntry next() {
                    return mdEntry;
                }
            };
        }
    }
}  // end class MetadataRetriever
