
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.persistence.expr;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import tekgenesis.common.core.DateOnly;
import tekgenesis.common.core.DateTime;
import tekgenesis.common.core.Enumeration;
import tekgenesis.common.core.enumeration.Enumerations;
import tekgenesis.persistence.Criteria;

import static tekgenesis.common.Predefined.cast;
import static tekgenesis.database.SqlConstants.asSqlConstant;
import static tekgenesis.database.SqlConstants.sqlValue;

/**
 * A Constant.
 */
public class Const<T> extends ExprImpl<T> {

    //~ Instance Fields ..............................................................................................................................

    private final T value;

    //~ Constructors .................................................................................................................................

    private Const(T value) {
        this.value = value;
    }

    //~ Methods ......................................................................................................................................

    @Override public <Q> Q accept(ExprVisitor<Q> visitor) {
        return visitor.visit(this);
    }

    /** The constant in sql syntax. */
    @Override public String asSql(boolean qualify) {
        return sqlValue(value);
    }

    @Override public boolean equals(final Object obj) {
        return obj instanceof Const && ((Const<?>) obj).value.equals(value);
    }

    @Override public int hashCode() {
        return value.hashCode();
    }

    @Override public String toString() {
        return value.toString();
    }

    @NotNull @Override public Class<T> getType() {
        return typeFor(value);
    }

    /** Returns the const value. */
    public T getValue() {
        return value;
    }

    @Override public T getValueFromResultSet(final ResultSet rs, final int columnIndex) {
        return value;
    }

    //~ Methods ......................................................................................................................................

    /** Accept a boolean. */
    public static <Q> Q acceptBoolean(final ExprVisitor<Q> visitor, final boolean b) {
        return visitor.visit(new Const<>(b));
    }

    static <E extends Expr<T>, T> E make(final T value) {
        return cast(build(value));
    }

    @NotNull private static Expr<?> build(final Object v) {
        final Expr<Object> c = cached.get(v);
        if (c != null) return c;
        if (v instanceof String) return new Str((String) v);
        if (v instanceof Integer) return new Int((Integer) v);
        if (v instanceof Double) return new Real((Double) v);
        if (v instanceof BigDecimal) return new Decimal((BigDecimal) v);
        if (v instanceof Boolean) return (Boolean) v ? Criteria.TRUE : Criteria.FALSE;
        if (v instanceof java.lang.Long) return new Long((java.lang.Long) v);
        if (v instanceof DateOnly) return new Date((DateOnly) v);
        if (v instanceof DateTime) return new DTime((DateTime) v);
        if (v instanceof Enumeration) return new Enum<Enumerations.Void, Object>((Enumeration<?, ?>) v);
        if (v instanceof EnumSet) {
            final EnumSet<Enumerations.Void> v1 = cast(v);
            return new EnumerationSet<>(v1);
        }
        return new Const<>(v);
    }
    private static void cache(@Nullable final Object v) {
        cached.put(v, v == null ? Expr.NULL : make(v));
    }

    private static <T> Class<T> typeFor(final T value) {
        return cast(value.getClass());
    }

    //~ Static Fields ................................................................................................................................

    private static final Map<Object, Expr<Object>> cached = new HashMap<>();

    static {
        for (int i = 0; i <= 10; i++)
            cache(i);
        cache(BigDecimal.ONE);
        cache(BigDecimal.TEN);
        cache("");
        cache(null);
    }

    static final Expr.Int ZERO = make(0);

    //~ Inner Classes ................................................................................................................................

    static class Date extends Const<DateOnly> implements Expr.Date {
        Date(final DateOnly value) {
            super(value);
        }
    }

    static class Decimal extends Const<BigDecimal> implements Expr.Decimal {
        Decimal(final BigDecimal value) {
            super(value);
        }

        @Override public int getDecimals() {
            return getValue().scale();
        }
    }

    static class DTime extends Const<DateTime> implements Expr.DTime {
        DTime(final DateTime value) {
            super(value);
        }
    }

    static class Enum<T extends java.lang.Enum<T> & Enumeration<T, I>, I> extends Const<T> implements Expr.Enum<T, I> {
        Enum(final Enumeration<?, ?> value) {
            super(cast(value));
        }
    }

    static class EnumerationSet<T extends java.lang.Enum<T> & Enumeration<T, I>, I> extends Const<EnumSet<T>> implements Expr.EnumerationSet<T, I> {
        private final Class<T> enumType;

        EnumerationSet(final EnumSet<T> value) {
            super(value);
            enumType = Enumerations.typeOfSet(value);
        }

        @Override public String asSql(boolean qualify) {
            return asSqlConstant(getValue());
        }

        @NotNull @Override public Class<T> getEnumType() {
            return enumType;
        }
    }

    static class Int extends Const<Integer> implements Expr.Int {
        Int(final Integer value) {
            super(value);
        }
    }

    static class Long extends Const<java.lang.Long> implements Expr.Long {
        Long(final java.lang.Long value) {
            super(value);
        }
    }

    static class Real extends Const<Double> implements Expr.Real {
        Real(final Double value) {
            super(value);
        }
    }

    static class Str extends Const<String> implements Expr.Str {
        Str(final String value) {
            super(value);
        }
    }
}  // end class Const
