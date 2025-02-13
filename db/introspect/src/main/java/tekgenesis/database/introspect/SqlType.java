
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.database.introspect;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import org.jetbrains.annotations.NotNull;

import tekgenesis.common.core.Constants;
import tekgenesis.common.core.Tuple4;

import static tekgenesis.common.core.Tuple.tuple;

/**
 * It represents an SqlType.
 */
public final class SqlType implements Serializable, Comparable<SqlType> {

    //~ Instance Fields ..............................................................................................................................

    private final String  name;
    private final int     precision;
    private final int     size;
    private final SqlKind sqlKind;

    //~ Constructors .................................................................................................................................

    private SqlType(final String name, final SqlKind kind, final int size, final int precision) {
        this.name      = name;
        sqlKind        = kind;
        this.size      = size;
        this.precision = precision;
    }

    //~ Methods ......................................................................................................................................

    @Override public int compareTo(@NotNull final SqlType that) {
        return name.compareTo(that.name);
    }

    @Override public boolean equals(final Object that) {
        return this == that || that instanceof SqlType && ((SqlType) that).name.equals(name);
    }

    /** Return the type formatted as an Sql specification. */
    public String format() {
        final String t  = sqlKind.toString();
        final int    n1;
        final int    n2;
        if (size <= 0 && precision > 0) {
            n1 = precision;
            n2 = 0;
        }
        else {
            n1 = size;
            n2 = precision;
        }
        //J-
        final boolean plain = n1 < 0 || n1 == 0 && !sqlKind.needsParameter();
        return plain ? t : n2 <= 0 ? String.format("%s(%d)", t, n1)
                     : String.format("%s(%d,%d)", t, n1, n2);
        //J+
    }

    @Override public int hashCode() {
        return name.hashCode();
    }

    /** returns true if the types have the same kind, precision and size. */
    public boolean sameAs(final SqlType type) {
        return sqlKind == type.sqlKind && precision == type.precision && size == type.size;
    }

    @Override public String toString() {
        return String.format("%s %s(%d,%d)", name, sqlKind, size, precision);
    }

    /** The name as returned from the database. */
    public String getName() {
        return name;
    }

    /** Returns the type precision. */
    public int getPrecision() {
        return precision;
    }

    /** Returns the type size. */
    public int getSize() {
        return size;
    }

    /** A Kind use to simplify treatment of types. */
    public SqlKind getSqlKind() {
        return sqlKind;
    }

    //~ Methods ......................................................................................................................................

    /** Find or create a type. */
    public static SqlType sqlType(final String name, final SqlKind kind) {
        return sqlType(name, kind, 0, 0);
    }

    /** Find or create a type. */
    public static SqlType sqlType(final String name, final SqlKind kind, final int size, final int precision) {
        final int                                       sz  = kind.fixSize(size);
        final int                                       p   = kind.fixPrecision(precision);
        final Tuple4<SqlKind, String, Integer, Integer> key = tuple(kind, name, sz, p);
        return types.computeIfAbsent(key, k -> new SqlType(name, kind, sz, p));
    }

    //~ Static Fields ................................................................................................................................

    private static final Map<Tuple4<SqlKind, String, Integer, Integer>, SqlType> types = new HashMap<>();

    private static final long serialVersionUID = 2614819974745473431L;

    /** Unknown SQL data type. */
    public static final SqlType UNKNOWN = sqlType("<UNKNOWN>", SqlKind.OTHER);
    public static final SqlType BOOLEAN = sqlType(Constants.BOOLEAN, SqlKind.BOOLEAN);
}  // end class JavaSqlType
