
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.database;

import java.lang.reflect.Field;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jetbrains.annotations.NotNull;

import tekgenesis.database.annotations.DbColumn;
import tekgenesis.database.annotations.DbColumns;
import tekgenesis.database.support.JdbcUtils;

import static tekgenesis.common.Predefined.ensureNotNull;
import static tekgenesis.common.core.Strings.fromCamelCase;
import static tekgenesis.common.util.Reflection.construct;
import static tekgenesis.common.util.Reflection.getFields;
import static tekgenesis.common.util.Reflection.setFieldValue;

/**
 * A Mapper that uses reflection to guess the map from columns to field names.
 */
class ReflectiveMapper<T> implements RowMapper<T> {

    //~ Instance Fields ..............................................................................................................................

    @NotNull private final Class<T> clazz;
    private List<Field>             mapping;

    //~ Constructors .................................................................................................................................

    ReflectiveMapper(@NotNull Class<T> clazz) {
        this.clazz = clazz;
        mapping    = null;
    }

    //~ Methods ......................................................................................................................................

    @Override public T mapRow(@NotNull ResultSet rs)
        throws SQLException
    {
        if (mapping == null) mapping = initMap(ensureNotNull(rs.getMetaData(), "Cannot get Metadata"), clazz);
        final T result = construct(clazz);
        for (int i = 0; i < mapping.size(); i++) {
            final Field field = mapping.get(i);
            if (field != null) setFieldValue(result, field, JdbcUtils.getColValue(rs, i + 1, field.getType()));
        }
        return result;
    }

    //~ Methods ......................................................................................................................................

    private static List<Field> initMap(@NotNull ResultSetMetaData md, Class<?> clazz)
        throws SQLException
    {
        final int n = md.getColumnCount();
        // Create map  with fields

        final Map<String, Field> fields = new HashMap<>();
        for (final Field fld : getFields(clazz)) {
            final DbColumn column = fld.getAnnotation(DbColumn.class);
            if (column != null) fields.put(column.value(), fld);
            else {
                final DbColumns columns = fld.getAnnotation(DbColumns.class);
                if (columns != null) {
                    for (final DbColumn c : columns.value())
                        fields.put(c.value(), fld);
                }
                else {
                    final String name = fld.getName();
                    fields.put(name, fld);
                    fields.put(fromCamelCase(name), fld);
                }
            }
        }
        final List<Field> result = new ArrayList<>();
        for (int i = 1; i <= n; i++)
            result.add(fields.get(md.getColumnName(i)));
        return result;
    }  // end method initMap
}  // end class ReflectiveMapper
