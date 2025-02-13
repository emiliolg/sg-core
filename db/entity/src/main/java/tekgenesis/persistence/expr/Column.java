
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

import org.jetbrains.annotations.NotNull;

import tekgenesis.common.core.DateOnly;
import tekgenesis.common.core.DateTime;
import tekgenesis.common.core.Enumeration;
import tekgenesis.persistence.Criteria;

import static tekgenesis.common.core.Constants.HASH_SALT;

/**
 * A Column of a SubQuery.
 */
public abstract class Column<E> extends ExprImpl<E> {

    //~ Instance Fields ..............................................................................................................................

    private final String alias;

    //~ Constructors .................................................................................................................................

    Column(final String alias, final String name) {
        super(name);
        this.alias = alias;
    }

    //~ Methods ......................................................................................................................................

    @Override public <Q> Q accept(final ExprVisitor<Q> visitor) {
        return visitor.visit(this);
    }

    @Override public String asSql(final boolean qualify) {
        return (qualify ? alias + "." : "") + getName();
    }

    @Override public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        final Column<?> that = (Column<?>) o;
        return alias.equals(that.alias) && getName().equals(that.getName());
    }

    @Override public int hashCode() {
        return alias.hashCode() + HASH_SALT * getName().hashCode();
    }

    //~ Inner Classes ................................................................................................................................

    public static class Bool extends Column<Boolean> implements Criteria {
        /** Boolean Column. */
        public Bool(final String alias, final String name) {
            super(alias, name);
        }
    }

    public static class Date extends Column<DateOnly> implements Expr.Date {
        /** Date Column. */
        public Date(final String alias, final String columnName) {
            super(alias, columnName);
        }
    }

    public static class Decimal extends Column<BigDecimal> implements Expr.Decimal {
        private final int decimals;

        /** Decimal Column. */
        public Decimal(final String alias, final String columnName, final int decimals) {
            super(alias, columnName);
            this.decimals = decimals;
        }

        @Override public int getDecimals() {
            return decimals;
        }
    }

    public static class DTime extends Column<DateTime> implements Expr.DTime {
        /** DateTime Column. */
        public DTime(final String alias, final String columnName) {
            super(alias, columnName);
        }
    }

    public static class Enum<T extends java.lang.Enum<T> & Enumeration<T, I>, I> extends Column<T> implements Expr.Enum<T, I> {
        private final Class<T> enumType;

        /** Enum Column. */
        public Enum(final String alias, final String columnName, Class<T> enumType) {
            super(alias, columnName);
            this.enumType = enumType;
        }

        @NotNull @Override public Class<T> getType() {
            return enumType;
        }
    }

    public static class Int extends Column<Integer> implements Expr.Int {
        /** Integer Column. */
        public Int(final String alias, final String name) {
            super(alias, name);
        }
    }

    public static class Long extends Column<java.lang.Long> implements Expr.Long {
        /** Long Column. */
        public Long(final String alias, final String name) {
            super(alias, name);
        }
    }

    public static class Real extends Column<Double> implements Expr.Real {
        /** Real column. */
        public Real(final String alias, final String columnName) {
            super(alias, columnName);
        }
    }

    public static class Str extends Column<String> implements Expr.Str {
        /** String column. */
        public Str(final String alias, final String columnName) {
            super(alias, columnName);
        }
    }
}  // end class Column
