
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.database.introspect;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import tekgenesis.common.collections.ext.BitSet;
import tekgenesis.database.introspect.exception.IntrospectorException;

import static tekgenesis.common.Predefined.notNull;

/**
 * An Entry form.
 */
public class MdEntry {

    //~ Instance Fields ..............................................................................................................................

    private final Map<String, Integer> columnMap;
    private final ResultSet            rs;
    private final BitSet               unReadColumns;

    //~ Constructors .................................................................................................................................

    MdEntry(final ResultSet rs, final Map<String, Integer> columnMap, final BitSet unReadColumns) {
        this.columnMap     = columnMap;
        this.rs            = rs;
        this.unReadColumns = unReadColumns;
    }

    //~ Methods ......................................................................................................................................

    /** Get The Entry as an String. */
    @Nullable public String getString(final MdColumn mdc) {
        final int c = readColumn(mdc);
        if (c == 0) return null;
        try {
            return rs.getString(c);
        }
        catch (final SQLException e) {
            throw new IntrospectorException(e);
        }
    }

    /** Gets unread columns to make them available as additional attributes. */
    Map<String, Object> getAttributes() {
        final Map<String, Object> map = new HashMap<>();
        for (final Map.Entry<String, Integer> e : columnMap.entrySet()) {
            final int column = e.getValue();
            if (unReadColumns.contains(column)) {
                try {
                    final Object value = rs.getObject(column);
                    if (value != null && !rs.wasNull()) map.put(e.getKey(), value);
                }
                catch (final Exception ignore) {}
            }
        }
        return map;
    }

    boolean getBoolean(final MdColumn mdc) {
        final int c = readColumn(mdc);
        if (c == 0) return false;
        try {
            return rs.getBoolean(c);
        }
        catch (final SQLException e) {
            throw new IntrospectorException(e);
        }
    }

    BigDecimal getDecimal(final MdColumn mdc) {
        final int c = readColumn(mdc);
        if (c == 0) return BigDecimal.ZERO;
        try {
            final BigDecimal value = rs.getBigDecimal(c);
            return rs.wasNull() ? BigDecimal.ZERO : value;
        }
        catch (final SQLException e) {
            throw new IntrospectorException(e);
        }
    }

    int getInt(MdColumn mdc) {
        return getInt(mdc, 0);
    }

    int getInt(final MdColumn mdc, final int defaultValue) {
        final int c = readColumn(mdc);
        if (c == 0) return defaultValue;
        try {
            final int value = rs.getInt(c);
            return rs.wasNull() ? defaultValue : value;
        }
        catch (final SQLException e) {
            throw new IntrospectorException(e);
        }
    }

    @NotNull String getString(final MdColumn mdc, String defaultValue) {
        return notNull(getString(mdc), defaultValue);
    }
    boolean getYesOrNo(final MdColumn mdc) {
        final int c = readColumn(mdc);
        if (c == 0) return false;
        try {
            final String s = notNull(rs.getString(c));
            return s.startsWith("Y");
        }
        catch (final SQLException e) {
            throw new IntrospectorException(e);
        }
    }

    /** Mark the column as read, returns the index or 0. */
    private int readColumn(final MdColumn c) {
        final int col = notNull(columnMap.get(c.getColumnName()), 0);
        unReadColumns.set(col, false);
        return col;
    }
}  // end class MdEntry
