
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.database;

import java.util.EnumSet;

import org.jetbrains.annotations.NonNls;

import tekgenesis.common.collections.ImmutableList;
import tekgenesis.common.core.DateOnly;
import tekgenesis.common.core.DateTime;
import tekgenesis.common.core.Enumeration;
import tekgenesis.common.core.enumeration.Enumerations;

import static java.lang.String.format;

import static tekgenesis.common.core.Constants.GENERATED;
import static tekgenesis.common.core.Constants.NULL_TO_STRING;

/**
 * Sql Constants.
 */
public interface SqlConstants {

    //~ Instance Fields ..............................................................................................................................

    @NonNls String AND = "and";

    @NonNls String CURRENT_DATE      = "current_date";
    @NonNls String CURRENT_TIMESTAMP = "current_timestamp";
    @NonNls String DELETE            = "delete from ";
    @NonNls String DESC              = "desc";
    @NonNls String FROM              = "from ";
    @NonNls String GROUP_BY          = "group by ";
    @NonNls String HAVING            = "having ";
    @NonNls String JOIN              = " join ";
    @NonNls String LEFT_OUTER_JOIN   = "left outer join";
    @NonNls String ON                = "on ";
    @NonNls String ORDER_BY          = "order by ";
    @NonNls String SELECT            = "select ";
    @NonNls String UNION             = " union ";
    @NonNls String UNION_ALL         = " union all ";
    @NonNls String UPDATE            = "update ";
    @NonNls String WHERE             = "where ";

    //~ Methods ......................................................................................................................................

    /** The constant in sql syntax. */
    static String asSqlConstant(final String value) {
        if (value.isEmpty()) return DbMacro.EmptyString.name();
        final StringBuilder s = new StringBuilder();
        s.append("n'");
        for (int i = 0; i < value.length(); i++) {
            final char c = value.charAt(i);
            if (c == '\'') s.append("''");
            else s.append(c);
        }
        s.append('\'');
        return s.toString();
    }

    /** The constant in sql syntax. */
    static String asSqlConstant(final Enumeration<?, ?> value) {
        final Object key = value.key();
        return key instanceof String ? "n'" + key + "'" : key.toString();
    }
    /** The constant in sql syntax. */
    static String asSqlConstant(final EnumSet<?> value) {
        return String.valueOf(Enumerations.asLong(value));
    }

    /** The constant in sql syntax. */
    static String asSqlConstant(final DateOnly value) {
        return String.format("date '%s'", value);
    }

    /** The constant in sql syntax. */
    static String asSqlConstant(final DateTime value) {
        if (value.isGreaterOrEqualTo(DateTime.ZERO)) return format("timestamp '%s'", value.format("yyyy-MM-dd HH:mm:ss.SSS"));
        if (value.equals(DateTime.MIN_VALUE)) return DbMacro.MinDateTime.toString();
        return format("to_timestamp( '%s', '%s')", value.format("yyyy-MM-dd HH:mm:ss.SSS GG"), "YYYY-MM-DD HH24:MI:SS.FF3 BC");
    }

    /** The constant in sql syntax. */
    static String asSqlConstant(final Boolean value) {
        return String.valueOf(value != null && value ? DbMacro.True : DbMacro.False);
    }

    /** The constant in sql syntax. */
    static String asSqlConstant(final Double value) {
        return value.toString().replace('E', 'e');
    }

    /** Return true if lines contains @Generated. */
    static boolean hasGeneratedMark(final ImmutableList<String> versionLines) {
        for (final String v : versionLines.getFirst()) {
            if (v.contains(GENERATED)) return true;
        }
        return false;
    }

    /** The constant in sql syntax. */
    static String sqlValue(final Object value) {
        if (value == null) return NULL_TO_STRING;

        if (value instanceof String) return asSqlConstant((String) value);

        if (value instanceof Enumeration) return asSqlConstant((Enumeration<?, ?>) value);

        if (value instanceof DateOnly) return asSqlConstant((DateOnly) value);

        if (value instanceof DateTime) return asSqlConstant((DateTime) value);

        if (value instanceof Boolean) return asSqlConstant((Boolean) value);
        if (value instanceof Double) return asSqlConstant((Double) value);
        if (value instanceof EnumSet) return asSqlConstant((EnumSet<?>) value);
        return value.toString();
    }
}  // end class SqlConstants
