
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
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.EnumSet;
import java.util.concurrent.TimeUnit;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import tekgenesis.common.annotation.Pure;
import tekgenesis.common.core.DateOnly;
import tekgenesis.common.core.DateTime;
import tekgenesis.common.core.DateTimeBase;
import tekgenesis.common.core.Enumeration;
import tekgenesis.common.core.enumeration.Enumerations;
import tekgenesis.database.support.JdbcUtils;
import tekgenesis.persistence.Criteria;
import tekgenesis.persistence.OrderSpec;
import tekgenesis.persistence.Select;
import tekgenesis.persistence.expr.Case.SimpleCaseValue;

import static java.math.BigDecimal.ROUND_HALF_EVEN;

import static tekgenesis.common.Predefined.*;
import static tekgenesis.database.DbConstants.EMPTY_CHAR;
import static tekgenesis.persistence.Criteria.*;
import static tekgenesis.persistence.expr.ExprOperator.*;

/**
 * An Expression of type T.
 */
public interface Expr<T> extends OrderSpec<T> {

    //~ Instance Fields ..............................................................................................................................

    Long  COUNT_ALL    = new Operation.Long(ExprOperator.COUNT_ALL);
    DTime CURRENT_TIME = new Operation.DTime(ExprOperator.CURRENT_TIME);
    Null  NULL         = new Null();

    //~ Methods ......................................................................................................................................

    /** Accepts the visitor. */
    <Q> Q accept(ExprVisitor<Q> visitor);

    /** Create an alias for the expression. */
    @Pure default Expr<T> as(final String newName) {
        return new Alias<>(newName, this);
    }

    /** The Sql value of the Expression. */
    String asSql(boolean qualify);

    /** A Boolean Operation. */
    @NotNull @Pure default Criteria bool(final ExprOperator operator, final Expr<?>... args) {
        return new Operation.Bool(operator, this, args);
    }

    /** Returns an string with all the expressions concatenated. */
    @Pure default Str concat(Expr<?>... expressions) {
        return new Operation.Str(CONCAT, this, expressions);
    }

    /** Get a {@code count(this) expression.}. */
    @Pure default Long count() {
        return new Operation.Long(ExprOperator.COUNT, this);
    }
    @Override @Pure default OrderSpec<T> descending() {
        return new OrderSpecImpl<>(this, true, null, null);
    }

    /** Get a {@code this == right} expression. */
    @Pure default Criteria eq(Expr<? super T> right) {
        return bool(EQ, right);
    }

    /** Get a {@code this != right} expression. */
    @Pure default Criteria eq(@Nullable T right) {
        return right == null ? EMPTY : eq(constant(right));
    }

    /** Get a {@code this in values} expression. */
    @Pure default Criteria in(Iterable<T> values) {
        return isEmpty(values) ? FALSE : bool(IN_VALUES, constant(values));
    }

    /** Get a {@code this in (select ... )} expression. */
    @Pure default Criteria in(Select<T> nestedQuery) {
        return bool(IN, new NestedQuery<>(nestedQuery));
    }

    /** Expr: {@code this like value} */
    @Pure default Criteria like(@Nullable String value) {
        return value == null ? EMPTY : bool(LIKE, constant(value));
    }

    /** Expr: {@code this like expr} */
    @Pure default Criteria like(Expr<Str> expr) {
        return bool(LIKE, expr);
    }

    /** Get a {@code this != right} expression. */
    @Pure default Criteria ne(Expr<? super T> right) {
        return bool(NE, right);
    }

    /** Get a {@code this != right} expression. */
    @Pure default Criteria ne(@Nullable T right) {
        return right == null ? EMPTY : ne(constant(right));
    }
    @Override @Pure default OrderSpec<T> nlsSort(String sort) {
        return this;
    }

    /** Get a {@code this not in values} expression. */
    @Pure default Criteria notIn(Iterable<T> values) {
        return in(values).not();
    }

    /** Expr: {@code this not like value} */
    @Pure default Criteria notLike(@Nullable String value) {
        return value == null ? EMPTY : bool(NOT_LIKE, constant(value));
    }

    /** Expr: {@code this not like expr} */
    @Pure default Criteria notLike(Expr<Str> expr) {
        return bool(NOT_LIKE, expr);
    }
    @Override @Pure default OrderSpec<T> nullsFirst() {
        return new OrderSpecImpl<>(this, false, true, null);
    }
    @Override @Pure default OrderSpec<T> nullsLast() {
        return new OrderSpecImpl<>(this, false, false, null);
    }

    /** Create an unary operation. */
    @Pure default Expr<T> unary(ExprOperator operator) {
        throw notImplemented("unary");
    }

    /** Create a simple case expression. */
    @Pure default <E extends Expr<T>> SimpleCaseValue<E, T> when(T value) {
        final E constant = cast(constant(value));
        return new SimpleCaseValue<>(cast(this), constant);
    }

    /** Create a criteria that checks if the expression is not null. */
    @Pure default Criteria isNotNull() {
        return bool(IS_NOT_NULL);
    }

    /** Create a criteria that checks if the expression is null. */
    @Pure default Criteria isNull() {
        return bool(IS_NULL);
    }

    /** Return the name of the expression. */
    String getName();

    /** Get Expression type. */
    @NotNull Class<T> getType();

    /** Given a column index return the value from the result set. */
    @Nullable T getValueFromResultSet(ResultSet rs, int columnIndex)
        throws SQLException;

    //~ Methods ......................................................................................................................................

    /** Create a constant. */
    static <T> Expr<T> constant(T value) {
        return Const.make(value);
    }

    /** Create a constant for a number. */
    static <T extends Number & Comparable<T>> Expr.Num<T, ?> constant(T value) {
        return Const.make(value);
    }

    /** Create an integer constant. */
    static Expr.Int constant(Integer value) {
        return Const.make(value);
    }

    /** Create a long constant. */
    static Expr.Long constant(java.lang.Long value) {
        return Const.make(value);
    }

    /** Create a real constant. */
    static Expr.Real constant(Double value) {
        return Const.make(value);
    }

    /** Create a real constant. */
    static Expr.Decimal constant(BigDecimal value) {
        return Const.make(value);
    }

    /** Create a boolean constant. */
    static Criteria constant(Boolean value) {
        return Const.make(value);
    }

    /** Create a String constant. */
    static Expr.Str constant(String value) {
        return Const.make(value);
    }

    /** Create a DateTime constant. */
    static Expr.DTime constant(DateTime value) {
        return Const.make(value);
    }

    /** Create a DateOnly constant. */
    static Expr.Date constant(DateOnly value) {
        return Const.make(value);
    }

    /** Create a enumeration constant. */
    static <T extends java.lang.Enum<T> & Enumeration<T, I>, I> Expr.Enum<T, I> constant(T value) {
        return Const.make(value);
    }

    /** Create a enumeration set constant. */
    static <T extends java.lang.Enum<T> & Enumeration<T, I>, I> Expr.EnumerationSet<T, I> constant(EnumSet<T> value) {
        return Const.make(value);
    }

    //~ Inner Interfaces .............................................................................................................................

    /**
     * A comparable expression.
     */
    interface Comp<T extends Comparable<T>> extends Expr<T> {
        /** Get a {@code between from and to} expression. */
        @Pure default Criteria between(@NotNull T from, @NotNull T to) {
            return bool(BETWEEN, Expr.constant(from), Expr.constant(to));
        }

        /** Get a {@code between from and to} expression. */
        @Pure default Criteria between(@NotNull Expr<T> from, @NotNull Expr<T> to) {
            return bool(BETWEEN, from, to);
        }
        /** Get a {@code this &gt;= right} expression. */
        @Pure default Criteria ge(@Nullable T right) {
            return right == null ? EMPTY : ge(Expr.constant(right));
        }

        /** Get a {@code this &gt;= right} expression. */
        @Pure default Criteria ge(Expr<T> right) {
            return bool(GE, right);
        }

        /** Get a {@code this &gt; right} expression. */
        @Pure default Criteria gt(@Nullable T right) {
            return right == null ? EMPTY : gt(Expr.constant(right));
        }

        /** Get a {@code this &gt; right} expression. */
        @Pure default Criteria gt(Expr<T> right) {
            return bool(GT, right);
        }

        /** Get a {@code this &gt; right} expression. */
        @Pure default Criteria le(@Nullable T right) {
            return right == null ? EMPTY : le(Expr.constant(right));
        }

        /** Get a {@code this &gt; right} expression. */
        @Pure default Criteria le(Expr<T> right) {
            return bool(LE, right);
        }

        /** Get a {@code this &gt; right} expression. */
        @Pure default Criteria lt(@Nullable T right) {
            return right == null ? EMPTY : lt(Expr.constant(right));
        }

        /** Get a {@code this &gt; right} expression. */
        @Pure default Criteria lt(Expr<T> right) {
            return bool(LT, right);
        }

        /** Get a {@code max(this)} expression. */
        @Pure default Comp<T> max() {
            return unary(MAX);
        }

        /** Get a {@code min(this)} expression. */
        @Pure default Comp<T> min() {
            return unary(MIN);
        }

        /** Create an unary operation. */
        Comp<T> unary(final ExprOperator operator);
    }  // end interface Comp

    /**
     * A DateOnly Expression.
     */
    interface Date extends Temporal<DateOnly> {
        /** Get a {@code this + right} expression. */
        @Pure default Date add(int days) {
            return new Operation.Date(ExprOperator.ADD_SECONDS, this, Expr.constant(TimeUnit.DAYS.toSeconds(days)));
        }
        /** Between two DateTimeBase. */
        @Pure default Criteria between(@NotNull DateTimeBase<?> from, @NotNull DateTimeBase<?> to) {
            return between(from.toDateOnly(), to.toDateOnly());
        }
        /** Get a {@code this - right} expression. */
        @Pure default Date sub(int days) {
            return new Operation.Date(ExprOperator.SUB_SECONDS, this, Expr.constant(TimeUnit.DAYS.toSeconds(days)));
        }
        default Date unary(final ExprOperator operator) {
            return new Operation.Date(operator, this);
        }
        @NotNull @Override default Class<DateOnly> getType() {
            return DateOnly.class;
        }
        default DateOnly getValueFromResultSet(ResultSet rs, int columnIndex)
            throws SQLException
        {
            return JdbcUtils.fromSqlDate(rs.getDate(columnIndex));
        }
    }

    /**
     * A decimal expression.
     */
    interface Decimal extends Num<BigDecimal, Decimal> {
        @Pure default Decimal binary(final ExprOperator operator, Expr<?> right) {
            return new Operation.Decimal(operator, getDecimals(), this, right);
        }
        @Override @Pure default Expr.Decimal fromNumber(Number n) {
            return Expr.constant(n instanceof BigDecimal ? (BigDecimal) n : BigDecimal.valueOf(n.doubleValue()));
        }
        default Decimal unary(final ExprOperator operator) {
            return new Operation.Decimal(operator, getDecimals(), this);
        }
        /** THe number of decimal positions of the field. */
        int getDecimals();
        @NotNull @Override default Class<BigDecimal> getType() {
            return BigDecimal.class;
        }
        @Nullable @Override default BigDecimal getValueFromResultSet(ResultSet rs, int columnIndex)
            throws SQLException
        {
            final BigDecimal value = rs.getBigDecimal(columnIndex);
            return value == null ? null : value.setScale(getDecimals(), ROUND_HALF_EVEN);
        }
    }

    /**
     * A DateTime expression.
     */
    interface DTime extends Temporal<DateTime> {
        /** Get a {@code this + right} expression. */
        @Pure default DTime add(int n, TimeUnit timeUnit) {
            return new Operation.DTime(ExprOperator.ADD_SECONDS, this, Expr.constant(timeUnit.toSeconds(n)));
        }
        /** Between two DateTimeBase. */
        @Pure default Criteria between(@NotNull DateTimeBase<?> from, @NotNull DateTimeBase<?> to) {
            return between(from.toDateTime(), to.toDateTime());
        }

        /** Returns the hour part of the Date. */
        @Pure default Int hour() {
            return new Operation.Int(HOUR, this);
        }
        /** Returns the minute part of the Date. */
        @Pure default Int minute() {
            return new Operation.Int(MINUTE, this);
        }
        /** Returns the second part of the Date. */
        @Pure default Real second() {
            return new Operation.Real(SECOND, this);
        }
        /** Get a {@code this - right} expression. */
        @Pure default DTime sub(int n, TimeUnit timeUnit) {
            return new Operation.DTime(ExprOperator.SUB_SECONDS, this, Expr.constant(timeUnit.toSeconds(n)));
        }
        default DTime unary(final ExprOperator operator) {
            return new Operation.DTime(operator, this);
        }
        @NotNull @Override default Class<DateTime> getType() {
            return DateTime.class;
        }
        default DateTime getValueFromResultSet(ResultSet rs, int columnIndex)
            throws SQLException
        {
            final Timestamp timestamp = rs.getTimestamp(columnIndex);
            return JdbcUtils.fromTimestamp(timestamp);
        }
    }

    /**
     * An Enumeration expression.
     */
    interface Enum<T extends java.lang.Enum<T> & Enumeration<T, I>, I> extends Comp<T> {
        @Override default Enum<T, I> unary(final ExprOperator operator) {
            return new Operation.Enum<>(operator, getType(), this);
        }
        @Nullable @Override default T getValueFromResultSet(ResultSet rs, int columnIndex)
            throws SQLException
        {
            final Object key = rs.getObject(columnIndex);
            if (key == null) return null;
            return cast(Enumerations.enumerationValueOf(getType(), key));
        }
    }

    /**
     * An Enumeration Set expression.
     */
    interface EnumerationSet<T extends java.lang.Enum<T> & Enumeration<T, I>, I> extends Expr<EnumSet<T>> {
        /** Get a {@code this contains right} expression. */
        @Pure default Criteria contains(T value) {
            return bool(ExprOperator.ENUM_CONTAINS, Expr.constant(1 << value.ordinal()));
        }
        /** Get a {@code this contains all of values} expression. */
        @Pure default Criteria containsAllOf(EnumSet<T> values) {
            final EnumerationSet<T, I> vs = Expr.constant(values);
            return bool(ExprOperator.ENUM_CONTAINS_ALL, vs, vs);
        }
        /** Get a {@code this contains all of values} expression. */
        @Pure
        @SuppressWarnings("unchecked")
        default Criteria containsAllOf(T first, T... values) {
            return containsAllOf(EnumSet.of(first, values));
        }

        /** Get a {@code this contains any of values} expression. */
        @Pure default Criteria containsAnyOf(EnumSet<T> values) {
            return bool(ExprOperator.ENUM_CONTAINS, Expr.constant(values));
        }
        /** Get a {@code this contains any of values} expression. */
        @Pure
        @SuppressWarnings("unchecked")
        default Criteria containsAnyOf(T first, T... values) {
            return containsAnyOf(EnumSet.of(first, values));
        }

        /** Get a {@code this not contains right} expression. */
        @Pure default Criteria doesNotContain(T value) {
            return contains(value).not();
        }
        @Override default Expr<EnumSet<T>> unary(final ExprOperator operator) {
            return new Operation.EnumerationSet<>(operator, getEnumType(), this);
        }
        /** Get type of set elements. */
        @NotNull Class<T> getEnumType();
        @NotNull @Override default Class<EnumSet<T>> getType() {
            return cast(EnumSet.class);
        }
        @Nullable @Override default EnumSet<T> getValueFromResultSet(ResultSet rs, int columnIndex)
            throws SQLException
        {
            final long elements = rs.getLong(columnIndex);
            return Enumerations.longToSet(getEnumType(), rs.wasNull() ? null : elements);
        }
    }  // end interface EnumerationSet

    /**
     * An Integer expression.
     */
    interface Int extends Num<Integer, Int> {
        default Int binary(final ExprOperator operator, Expr<?> right) {
            return new Operation.Int(operator, this, right);
        }
        @Override default Expr.Int fromNumber(Number n) {
            return Expr.constant(n.intValue());
        }
        default Int unary(ExprOperator operator) {
            return new Operation.Int(operator, this);
        }
        @NotNull @Override default Class<Integer> getType() {
            return Integer.class;
        }
        @Override default Integer getValueFromResultSet(ResultSet rs, int columnIndex)
            throws SQLException
        {
            final int i = rs.getInt(columnIndex);
            return rs.wasNull() ? null : i;
        }
    }

    /**
     * A LONG INTEGER expression.
     */
    interface Long extends Num<java.lang.Long, Long> {
        default Long binary(final ExprOperator operator, Expr<?> right) {
            return new Operation.Long(operator, this, right);
        }
        @Override default Expr.Long fromNumber(Number n) {
            return Expr.constant(n.longValue());
        }
        default Long unary(ExprOperator operator) {
            return new Operation.Long(operator, this);
        }
        @NotNull @Override default Class<java.lang.Long> getType() {
            return java.lang.Long.class;
        }
        @Override default java.lang.Long getValueFromResultSet(ResultSet rs, int columnIndex)
            throws SQLException
        {
            final long l = rs.getLong(columnIndex);
            return rs.wasNull() ? null : l;
        }
    }

    /*interface Case<T extends Comparable<T>> extends Comp<T> {
     *
     *}*/

    /**
     * A NUMERIC EXPRESSION.
     */
    interface Num<T extends Number & Comparable<T>, ET extends Comp<T>> extends Comp<T> {
        /** Abs function. */
        @Pure default ET abs() {
            return unary(ABS);
        }
        /** Get a {@code this + right} expression. */
        @Pure default <N extends Number & Comparable<N>> ET add(Expr<N> right) {
            return binary(ExprOperator.ADD, right);
        }

        /** Get a {@code this + right} expression. */
        @Pure default <N extends Number & Comparable<N>> ET add(N right) {
            return binary(ExprOperator.ADD, Expr.constant(right));
        }

        /** Get a {@code avg(this)} expression. */
        @Pure default ET avg() {
            return unary(ExprOperator.AVG);
        }

        /** Create an arithmetic operation. */
        @Pure ET binary(final ExprOperator operator, Expr<?> right);
        /** Ceil function. */
        @Pure default ET ceil() {
            return unary(CEIL);
        }

        /** Get a {@code this / right} expression. */
        @Pure default <N extends Number & Comparable<N>> ET div(Expr<N> right) {
            return binary(DIV, right);
        }

        /** Get a {@code this / right} expression. */
        @Pure default <N extends Number & Comparable<N>> ET div(N right) {
            return binary(DIV, Expr.constant(right));
        }

        /** Get a {@code this &ge;= right} expression. */
        default Criteria eq(final Number right) {
            return bool(EQ, fromNumber(right));
        }

        /** Get a {@code exp(this)} expression. */
        @Pure default <N extends Number & Comparable<N>> ET exp() {
            return unary(EXP);
        }
        /** Ceil function. */
        @Pure default ET floor() {
            return unary(FLOOR);
        }

        /** return a constant value T based on a Number. */
        ET fromNumber(Number n);

        /** Get a {@code this &ge;= right} expression. */
        default Criteria ge(final Number right) {
            return bool(GE, fromNumber(right));
        }
        /** Get a {@code this &ge;= right} expression. */
        default Criteria gt(final Number right) {
            return bool(GT, fromNumber(right));
        }
        /** Get a {@code this &ge;= right} expression. */
        default Criteria le(final Number right) {
            return bool(LE, fromNumber(right));
        }

        /** Get a {@code ln(this)} expression. */
        @Pure default <N extends Number & Comparable<N>> ET ln() {
            return unary(LN);
        }
        /** Get a {@code this &ge;= right} expression. */
        default Criteria lt(final Number right) {
            return bool(LT, fromNumber(right));
        }
        @Override default ET max() {
            return unary(MAX);
        }
        @Override default ET min() {
            return unary(MIN);
        }

        /** Get a {@code this % right} expression. */
        @Pure default <N extends Number & Comparable<N>> ET mod(Expr<N> right) {
            return binary(ExprOperator.MOD, right);
        }

        /** Get a {@code this % right} expression. */
        @Pure default <N extends Number & Comparable<N>> ET mod(N right) {
            return binary(ExprOperator.MOD, Expr.constant(right));
        }

        /** Get a {@code this * right} expression. */
        @Pure default <N extends Number & Comparable<N>> ET mul(Expr<N> right) {
            return binary(ExprOperator.MUL, right);
        }

        /** Get a {@code this * right} expression. */
        @Pure default <N extends Number & Comparable<N>> ET mul(N right) {
            return binary(ExprOperator.MUL, Expr.constant(right));
        }

        /** Get a {@code this &ge;= right} expression. */
        default Criteria ne(final Number right) {
            return bool(NE, fromNumber(right));
        }

        /** Get a {@code - this} expression. */
        @Pure default ET negate() {
            return unary(ExprOperator.NEGATE);
        }

        /** Get a {@code pow(this, right)} expression. */
        @Pure default <N extends Number & Comparable<N>> ET pow(Expr<N> right) {
            return binary(POW, right);
        }
        /** Get a {@code pow(this, right)} expression. */
        @Pure default <N extends Number & Comparable<N>> ET pow(N right) {
            return binary(POW, Expr.constant(right));
        }
        /** Round function. */
        @Pure default ET round() {
            return binary(ROUND, Const.ZERO);
        }
        /** Round function. */
        @Pure default ET round(int precision) {
            return binary(ROUND, Expr.constant(precision));
        }

        /** Get a {@code this - right} expression. */
        @Pure default <N extends Number & Comparable<N>> ET sub(Expr<N> right) {
            return binary(ExprOperator.SUB, right);
        }

        /** Get a {@code this - right} expression. */
        @Pure default <N extends Number & Comparable<N>> ET sub(N right) {
            return binary(ExprOperator.SUB, Expr.constant(right));
        }

        /** Get a {@code sum(this)} expression. */
        @Pure default ET sum() {
            return unary(ExprOperator.SUM);
        }
        /** Trunc function. */
        @Pure default ET trunc() {
            return binary(TRUNC, Const.ZERO);
        }
        /** Trunc function. */
        @Pure default ET trunc(int precision) {
            return binary(TRUNC, Expr.constant(precision));
        }
        @Override ET unary(final ExprOperator operator);
    }  // end interface Num

    /**
     * A Real (floating point) expression.
     */
    interface Real extends Num<Double, Real> {
        default Real binary(final ExprOperator operator, Expr<?> right) {
            return new Operation.Real(operator, this, right);
        }
        @Override default Expr.Real fromNumber(Number n) {
            return Expr.constant(n.doubleValue());
        }
        @Override default Real unary(final ExprOperator operator) {
            return new Operation.Real(operator, this);
        }
        @NotNull @Override default Class<Double> getType() {
            return Double.class;
        }
        @Override default Double getValueFromResultSet(ResultSet rs, int columnIndex)
            throws SQLException
        {
            final double d = rs.getDouble(columnIndex);
            return rs.wasNull() ? null : d;
        }
    }

    /**
     * A String expression.
     */
    interface Str extends Comp<String> {
        /**
         * Expr: {@code this contains str} that is equivalent to {@code this like "%" + str + "%" }
         */
        @Pure default Criteria contains(@Nullable String str) {
            return str == null ? EMPTY : bool(LIKE, Expr.constant("%" + str + "%"));
        }
        /** Insensitive case comparison. */
        @Pure default Criteria equalsIgnoreCase(Str right) {
            return lower().eq(right.lower());
        }

        /** Insensitive case comparison. */
        @Pure default Criteria equalsIgnoreCase(String right) {
            return lower().eq(right);
        }
        /** Return the length of this String. */
        @Pure default Int length() {
            return new Operation.Int(STRING_LENGTH, this);
        }

        /** Lower case the expression. */
        @Pure default Str lower() {
            return unary(LOWER);
        }
        @Override default OrderSpec<String> nlsSort(String sort) {
            return new OrderSpecImpl<>(this, false, null, sort);
        }

        /** Expr: {@code this startsWith str} that is equivalent to {@code this like str + "%" } */
        @Pure default Criteria startsWith(@Nullable String str) {
            return str == null ? EMPTY : bool(LIKE, Expr.constant(str + "%"));
        }

        /** Returns a substring of this String. */
        @Pure default Str substr(int startingPosition) {
            return new Operation.Str(SUBSTR1, this, Expr.constant(startingPosition));
        }

        /** Returns a substring of this String. */
        @Pure default Str substr(Expr.Num<?, ?> startingPosition) {
            return new Operation.Str(SUBSTR1, this, startingPosition);
        }

        /** Returns a substring of this String. */
        @Pure default Str substr(int startingPosition, int length) {
            return new Operation.Str(SUBSTR, this, Expr.constant(startingPosition), Expr.constant(length));
        }

        /** Returns a substring of this String. */
        @Pure default Str substr(Expr.Num<?, ?> startingPosition, Expr.Num<?, ?> length) {
            return new Operation.Str(SUBSTR, this, startingPosition, length);
        }
        @Override default Str unary(final ExprOperator operator) {
            return new Operation.Str(operator, this);
        }

        /** Upper case the expression. */
        default Str upper() {
            return unary(UPPER);
        }
        @NotNull @Override default Class<String> getType() {
            return String.class;
        }
        @Nullable @Override default String getValueFromResultSet(ResultSet rs, int columnIndex)
            throws SQLException
        {
            final String string = rs.getString(columnIndex);
            return string != null && !string.isEmpty() && string.charAt(0) == EMPTY_CHAR ? "" : string;
        }

        /** Check that the String is null or Empty . */
        @Pure default Criteria isEmpty() {
            return isNull().or(eq(new Operation.Str(EMPTY_STRING)));
        }

        /** Check that the String is not null and not Empty . */
        @Pure default Criteria isNotEmpty() {
            return isNotNull().and(ne(new Operation.Str(EMPTY_STRING)));
        }
    }  // end interface Str

    /**
     * A temporal (DateOnly or DateTime) Expression.
     */
    interface Temporal<T extends DateTimeBase<T>> extends Comp<T> {
        /** Returns the day part of the Date. */
        @Pure default Int day() {
            return new Operation.Int(DAY, this);
        }
        /** Returns the month part of the Date. */
        @Pure default Int month() {
            return new Operation.Int(MONTH, this);
        }
        /** Returns the day part of the Date. */
        @Pure default Int year() {
            return new Operation.Int(YEAR, this);
        }
    }

    //~ Inner Classes ................................................................................................................................

    /**
     * A Null Expression.
     */
    class Null extends Operation<Object> {
        Null() {
            super(ExprOperator.NULL);
        }

        @NotNull @Override public Class<Object> getType() {
            return Object.class;
        }

        @Nullable @Override public Object getValueFromResultSet(ResultSet rs, int columnIndex) {
            return null;
        }
    }
}  // end interface Expr
