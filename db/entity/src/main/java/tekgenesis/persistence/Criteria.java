
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.persistence;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.jetbrains.annotations.NotNull;

import tekgenesis.common.Predefined;
import tekgenesis.common.annotation.Pure;
import tekgenesis.common.collections.ImmutableList;
import tekgenesis.common.core.DateOnly;
import tekgenesis.common.core.DateTime;
import tekgenesis.common.core.Enumeration;
import tekgenesis.persistence.expr.*;
import tekgenesis.persistence.expr.Case.SearchedBuilder;

import static tekgenesis.persistence.expr.ExprOperator.*;

/**
 * A Boolean Expression.
 */
public interface Criteria extends Expr<Boolean> {

    //~ Instance Fields ..............................................................................................................................

    /**
     * EMPTY is used for chained construction of a Criteria. For Example:
     *
     * <blockquote>
     * <pre>
     Criteria c = Criteria.EMPTY;
     for (e : expressions)
     c = c.and(e);
     * </pre>
     * </blockquote>
     */
    Criteria EMPTY = new Empty();

    /** FALSE criteria. */
    Criteria FALSE = new Empty() {
            @Override public Criteria and(Criteria right) {
                return FALSE;
            }
            @Override public Criteria not() {
                return TRUE;
            }
            @Override public <Q> Q accept(final ExprVisitor<Q> visitor) {
                return Const.acceptBoolean(visitor, false);
            }
        };

    /** TRUE criteria. */
    Criteria TRUE = new Empty() {
            @Override public Criteria or(Criteria right) {
                return TRUE;
            }
            @Override public Criteria not() {
                return FALSE;
            }
            @Override public <Q> Q accept(final ExprVisitor<Q> visitor) {
                return Const.acceptBoolean(visitor, true);
            }
        };

    //~ Methods ......................................................................................................................................

    /** And 2 Criteria. */
    @Pure default Criteria and(Criteria right) {
        return right.isConst() ? right.and(this) : bool(AND, right);
    }

    /** Negate a Criteria. */
    @Pure default Criteria not() {
        return bool(NOT);
    }

    /** Or 2 Criteria. */
    @Pure default Criteria or(Criteria right) {
        return right.isConst() ? right.or(this) : bool(OR, right);
    }

    /** Create a searched case expression. */
    @Pure default <E extends Expr<T>, T> SearchedBuilder<E, T> then(E then) {
        return new SearchedBuilder<>(this, then);
    }

    /** Create a searched case expression for Decimal. */
    @Pure default SearchedBuilder<Expr.Decimal, BigDecimal> then(Expr.Decimal then) {
        return new SearchedBuilder<>(this, then);
    }

    /** Create a searched case expression for Int. */
    @Pure default SearchedBuilder<Expr.Int, Integer> then(Expr.Int then) {
        return new SearchedBuilder<>(this, then);
    }

    /** Create a searched case expression for Long. */
    @Pure default SearchedBuilder<Expr.Long, java.lang.Long> then(Expr.Long then) {
        return new SearchedBuilder<>(this, then);
    }

    /** Create a searched case expression for Long. */
    @Pure default SearchedBuilder<Expr.Real, Double> then(Expr.Real then) {
        return new SearchedBuilder<>(this, then);
    }

    /** Create a searched case expression for String. */
    @Pure default SearchedBuilder<Expr.Str, String> then(Expr.Str then) {
        return new SearchedBuilder<>(this, then);
    }

    /** Create a searched case expression for DateOnly. */
    @Pure default SearchedBuilder<Expr.Date, DateOnly> then(Expr.Date then) {
        return new SearchedBuilder<>(this, then);
    }

    /** Create a searched case expression for DateTime. */
    @Pure default SearchedBuilder<Expr.DTime, DateTime> then(Expr.DTime then) {
        return new SearchedBuilder<>(this, then);
    }

    /** Create a searched case expression for an Enum. */
    @Pure
    @SuppressWarnings("UnnecessaryFullyQualifiedName")
    default <T extends java.lang.Enum<T> & Enumeration<T, I>, I> SearchedBuilder<Expr.Enum<T, I>, T> then(Expr.Enum<T, I> then) {
        return new SearchedBuilder<>(this, then);
    }

    /** Create a searched case expression with a constant value. */
    @Pure default <E extends Expr<T>, T> SearchedBuilder<E, T> then(T then) {
        return then(Predefined.<E, Expr<T>>cast(Expr.constant(then)));
    }
    @Override default Expr<Boolean> unary(final ExprOperator operator) {
        return bool(operator, this);
    }

    /** Get a this == false expression. */
    @Pure default Criteria isFalse() {
        return eq(FALSE);
    }

    /** Get a this == true expression. */
    @Pure default Criteria isTrue() {
        return eq(TRUE);
    }

    /** Returns true if the Criteria is a constant one (EMPTY/TRUE/FALSE). */
    default boolean isConst() {
        return false;
    }
    @NotNull default Class<Boolean> getType() {
        return Boolean.class;
    }
    default Boolean getValueFromResultSet(ResultSet rs, int columnIndex)
        throws SQLException
    {
        final boolean b = rs.getBoolean(columnIndex);
        return rs.wasNull() ? null : b;
    }

    //~ Methods ......................................................................................................................................

    /** Create an And over a list of criteria. */
    static Criteria allOf(Iterable<Criteria> criteriaList) {
        return Operation.boolOperation(AND, criteriaList);
    }
    /** Create an And over a list of criteria. */
    static Criteria allOf(Criteria... criteriaList) {
        return Operation.boolOperation(AND, ImmutableList.fromArray(criteriaList));
    }

    /** Create an Or over a list of criteria. */
    static Criteria anyOf(Iterable<Criteria> criteriaList) {
        return Operation.boolOperation(OR, criteriaList);
    }
    /** Create an Or over a list of criteria. */
    static Criteria anyOf(Criteria... criteriaList) {
        return Operation.boolOperation(OR, ImmutableList.fromArray(criteriaList));
    }

    //~ Inner Classes ................................................................................................................................

    class Empty extends ExprImpl<Boolean> implements Criteria {
        @Override public <Q> Q accept(ExprVisitor<Q> visitor) {
            throw new IllegalStateException();
        }
        @Override public Criteria and(Criteria right) {
            return right;
        }
        @Override public Criteria not() {
            return this;
        }
        @Override public Criteria or(Criteria right) {
            return right;
        }
        @Override public final boolean isConst() {
            return true;
        }
    }
}  // end interface Criteria
