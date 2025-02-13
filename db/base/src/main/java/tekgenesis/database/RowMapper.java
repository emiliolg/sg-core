
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.database;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import tekgenesis.common.util.Reflection;
import tekgenesis.database.support.JdbcUtils;

/**
 * An interface used by for mapping rows of a {@link ResultSet} on a per-row basis.
 */
@FunctionalInterface public interface RowMapper<T> {

    //~ Instance Fields ..............................................................................................................................

    RowMapper<Object[]> ARRAY_ROW_MAPPER = new ObjectArrayRowMapper<Object[]>() {
            @Override public Object[] mapRow(final ResultSet rs)
                throws SQLException
            {
                return super.resultAsArray(rs);
            }
        };

    RowMapper<Map<String, Object>> MAP_ROW_MAPPER = new RowMapper<Map<String, Object>>() {
            List<String> columns = null;

            @Override public Map<String, Object> mapRow(final ResultSet rs)
                throws SQLException
            {
                if (columns == null) {
                    columns = new ArrayList<>();
                    final ResultSetMetaData metaData = rs.getMetaData();
                    if (metaData != null) {
                        final int n = metaData.getColumnCount();
                        for (int i = 1; i <= n; i++)
                            columns.add(metaData.getColumnName(i));
                    }
                }
                final Map<String, Object> map = new LinkedHashMap<>();
                for (int i = 0; i < columns.size(); i++)
                    map.put(columns.get(i), rs.getObject(i + 1));
                return map;
            }
        };

    //~ Methods ......................................................................................................................................

    /**
     * Implementations must implement this method to map each row of data in the ResultSet. This
     * method should not call {@code next()} on the ResultSet; it is only supposed to map values of
     * the current row.
     */
    @Nullable T mapRow(ResultSet rs)
        throws SQLException;

    //~ Methods ......................................................................................................................................

    /** A Generic mapper build over a function from object array to T. */
    static <T> RowMapper<T> functionalMapper(@NotNull final Function<Object[], T> f) {
        return new ObjectArrayRowMapper<T>() {
            @Override public T mapRow(final ResultSet rs)
                throws SQLException
            {
                return f.apply(resultAsArray(rs));
            }
        };
    }

    /**
     * A Generic mapper thar is built based on delegation to {@link SingleColumnRowMapper} for
     * simple types or reflection for complex types.
     */
    static <T> RowMapper<T> reflectiveMapper(@NotNull Class<T> expectedType) {
        if (JdbcUtils.isScalarType(expectedType))
            return rs -> {
                       // Validate column count.
                       final ResultSetMetaData md          = rs.getMetaData();
                       final int               nrOfColumns = md.getColumnCount();
                       // if (nrOfColumns != 1) throw new ResultSetColumnCountException(1, nrOfColumns);
                       return JdbcUtils.getColValue(rs, 1, expectedType);
                   };
        if (HasRowMapper.class.isAssignableFrom(expectedType)) return Reflection.invokeStatic(expectedType, HasRowMapper.ROW_MAPPER);

        return new ReflectiveMapper<>(expectedType);
    }

    //~ Inner Classes ................................................................................................................................

    abstract class ObjectArrayRowMapper<T> implements RowMapper<T> {
        int columnCount = -1;

        Object[] resultAsArray(final ResultSet rs)
            throws SQLException
        {
            if (columnCount == -1) {
                final ResultSetMetaData metaData = rs.getMetaData();
                columnCount = metaData == null ? 0 : metaData.getColumnCount();
            }
            final Object[] r = new Object[columnCount];
            for (int i = 1; i <= columnCount; i++)
                r[i - 1] = rs.getObject(i);
            return r;
        }
    }
}  // end interface RowMapper
