
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
import java.util.Map;

import tekgenesis.common.collections.Maps;
import tekgenesis.database.DbMacro;
import tekgenesis.database.introspect.exception.IntrospectorException;

import static tekgenesis.common.core.Tuple.tuple;
import static tekgenesis.database.introspect.SqlKind.*;
import static tekgenesis.database.introspect.SqlType.sqlType;

/**
 * Oracle MetadataRetriever.
 */
@SuppressWarnings("DuplicateStringLiteralInspection")
class PostgresMetadataRetriever extends MetadataRetriever {

    //~ Constructors .................................................................................................................................

    public PostgresMetadataRetriever(final Connection connection, final DatabaseMetaData metaData) {
        super(connection, metaData);
    }

    //~ Methods ......................................................................................................................................

    @Override public String getViewSql(SchemaInfo schema, String name) {
        try {
            final ResultSet execute = execute(
                    "select description from pg_description\n" +
                    "join pg_class on pg_description.objoid = pg_class.oid\n" +
                    "join pg_namespace on pg_class.relnamespace = pg_namespace.oid\n" +
                    "where relname = '%s' and nspname='%s'",
                    new Object[] { name, schema.getName() });
            while (execute.next())
                return execute.getString(1);
            return "";
        }
        catch (final SQLException e) {
            throw new IntrospectorException(e);
        }
    }

    @Override protected String translateDefault(String v, final SqlKind sqlKind) {
        if (v == null) return null;

        final String value = v.trim();

        if (v.startsWith("nextval")) return null;
        if ("now()".equalsIgnoreCase(value)) return DbMacro.CurrentTime.name();
        if ("('now'::text)::date".equalsIgnoreCase(value)) return DbMacro.CurrentDate.name();
        if ("''::character varying".equalsIgnoreCase(value)) return DbMacro.EmptyString.name();

        return super.translateDefault(value, sqlKind);
    }

    @Override SqlType retrieveType(final String typeName, final int dataType, final int size, final int precision) {
        SqlKind kind = kindMap.get(typeName);
        kind = kind != null ? kind : SqlKind.kindFor(dataType);
        if (kind == VARCHAR) return sqlType(typeName, NVARCHAR, size, 0);
        return SqlType.sqlType(typeName, kind, size, precision);
    }
    Iterable<MdEntry> getSequences(final String schema) {
        return iterableFrom(
            "select  " +
            "   START_VALUE   as SEQ_START," +
            "   SEQUENCE_NAME as SEQ_NAME," +
            "   MINIMUM_VALUE as SEQ_MIN," +
            "   MAXIMUM_VALUE as SEQ_MAX," +
            "   INCREMENT     as SEQ_INC," +
            "   CYCLE_OPTION  as SEQ_CYCLE," +
            "   1             as SEQ_CACHE " +
            "from INFORMATION_SCHEMA.SEQUENCES " +
            "where SEQUENCE_SCHEMA = '%s' and SEQUENCE_NAME not like 'SEQUENCER_%%_seq' " +
            "order by SEQUENCE_NAME ",
            schema);
    }

    //~ Static Fields ................................................................................................................................

    private static final Map<String, SqlKind> kindMap = Maps.hashMap(tuple("text", CLOB));
}  // end class PostgresMetadataRetriever
