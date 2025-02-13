
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
import java.util.Map;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import tekgenesis.common.Predefined;
import tekgenesis.common.annotation.Pure;
import tekgenesis.common.core.DateOnly;
import tekgenesis.common.core.DateTime;
import tekgenesis.common.core.Enumeration;
import tekgenesis.common.core.Option;
import tekgenesis.persistence.Criteria;

import static tekgenesis.common.Predefined.cast;
import static tekgenesis.common.collections.Maps.linkedHashMap;
import static tekgenesis.common.core.Option.some;
import static tekgenesis.common.core.Tuple.tuple;
import static tekgenesis.persistence.expr.ExprOperator.CASE;

/**
 * Case Builder (if-then-else).
 */
public class Case<V extends Expr<?>, E extends Expr<T>, T> extends ExprImpl<T> {

    //~ Instance Fields ..............................................................................................................................

    private final Map<V, E> cases;
    private final Option<V> column;
    private final Option<E> otherwise;
    private final Class<T>  type;

    //~ Constructors .................................................................................................................................

    Case(Class<T> type, @NotNull Map<V, E> cases, @NotNull Option<E> otherwise, @NotNull Option<V> column) {
        this.cases     = cases;
        this.otherwise = otherwise;
        this.column    = column;
        this.type      = type;
    }

    //~ Methods ......................................................................................................................................

    @Override public <Q> Q accept(ExprVisitor<Q> visitor) {
        return visitor.visit(this);
    }

    @NotNull @Override public Class<T> getType() {
        return type;
    }

    @Nullable @Override public T getValueFromResultSet(ResultSet rs, int columnIndex)
        throws SQLException
    {
        return null;
    }

    String asSql(ExpressionBuilder b) {
        final StringBuilder s = new StringBuilder();
        if (column.isPresent()) s.append(column.get().accept(b));
        for (final Map.Entry<V, E> e : cases.entrySet()) {
            s.append(" when ").append(e.getKey().accept(b));
            s.append(" then ").append(e.getValue().accept(b));
        }
        if (otherwise.isPresent()) s.append(" else ").append(otherwise.get().accept(b));
        s.append(" end");
        return s.toString();
    }

    //~ Inner Classes ................................................................................................................................

    public abstract static class CaseBuilder<V extends Expr<?>, E extends Expr<T>, T> {
        private final Map<V, E> cases;
        private final E         firstThen;
        private Option<E>       otherwise = Option.empty();

        /** Abstract Case Builder constructor. */
        private CaseBuilder(V when, E then) {
            firstThen = then;
            cases     = linkedHashMap(tuple(when, then));
        }

        /** End case expression. Use when 'else null' wants to be used */
        @Pure public E end() {
            return overrideAndGetReturnOperation();
        }

        /** Case 'else' statement with an expression. */
        @Pure public E otherwise(E o) {
            otherwise = some(o);
            return overrideAndGetReturnOperation();
        }

        /** Case 'else' statement with a constant. */
        @Pure public E otherwise(T o) {
            return otherwise(Predefined.<E, Expr<T>>cast(Expr.constant(o)));
        }

        @NotNull Option<V> column() {
            return Option.empty();
        }

        void put(V when, E then) {
            cases.put(when, then);
        }

        @NotNull final Case<V, E, T> getCaseExpression() {
            return new Case<>(firstThen.getType(), cases, otherwise, column());
        }

        Map<V, E> getCases() {
            return cases;
        }

        @NotNull private E overrideAndGetReturnOperation() {
            final Operation<T> op = cast(firstThen.unary(CASE));
            op.overrideOperand(getCaseExpression());
            return cast(op);
        }
    }  // end class CaseBuilder

    public static class SearchedBuilder<E extends Expr<T>, T> extends CaseBuilder<Criteria, E, T> {
        /** Concrete Case Builder for Searched Case. */
        public SearchedBuilder(Criteria when, E then) {
            super(when, then);
        }

        /** Case when value creator with a condition. */
        @Pure public TypedCaseValue<Criteria, E, T, SearchedBuilder<E, T>> elseIf(Criteria value) {
            return new TypedCaseValue<>(this, value);
        }
    }

    public static class SimpleBuilder<V extends Expr<C>, C, E extends Expr<T>, T> extends CaseBuilder<V, E, T> {
        private final V column;

        /** Concrete Case Builder for Simple Case. */
        SimpleBuilder(V column, V when, E then) {
            super(when, then);
            this.column = column;
        }

        /** Case when value creator with a constant. */
        @Pure public TypedCaseValue<V, E, T, SimpleBuilder<V, C, E, T>> when(C value) {
            final V constant = cast(Expr.constant(value));
            return new TypedCaseValue<>(this, constant);
        }

        @NotNull Option<V> column() {
            return some(column);
        }
    }

    @SuppressWarnings("UnnecessaryFullyQualifiedName")
    public static class SimpleCaseValue<V extends Expr<C>, C> {
        private final V column;
        private final V when;

        /** Simple Case Value Constructor. */
        public SimpleCaseValue(V column, V when) {
            this.column = column;
            this.when   = when;
        }

        /** Case then creator with a Decimal. */
        @Pure public SimpleBuilder<V, C, Expr.Decimal, BigDecimal> then(Expr.Decimal then) {
            return build(then);
        }

        /** Case then creator with an Int. */
        @Pure public SimpleBuilder<V, C, Expr.Int, Integer> then(Expr.Int then) {
            return build(then);
        }

        /** Case then creator with an Int. */
        @Pure public SimpleBuilder<V, C, Expr.Real, Double> then(Expr.Real then) {
            return build(then);
        }

        /** Case then creator with an Long. */
        @Pure public SimpleBuilder<V, C, Expr.Long, java.lang.Long> then(Expr.Long then) {
            return build(then);
        }

        /** Case then creator with a Date. */
        @Pure public SimpleBuilder<V, C, Expr.Date, DateOnly> then(Expr.Date then) {
            return build(then);
        }

        /** Case then creator with a DateTime. */
        @Pure public SimpleBuilder<V, C, Expr.DTime, DateTime> then(Expr.DTime then) {
            return build(then);
        }

        /** Case then creator with a String. */
        @Pure public SimpleBuilder<V, C, Expr.Str, String> then(Expr.Str then) {
            return build(then);
        }

        /** Case then creator with an Enum. */
        @Pure public <T extends java.lang.Enum<T> & Enumeration<T, I>, I> SimpleBuilder<V, C, Expr.Enum<T, I>, T> then(Expr.Enum<T, I> then) {
            return build(then);
        }
        /** Case then creator with a Constant Decimal. */
        @Pure public SimpleBuilder<V, C, Expr.Decimal, BigDecimal> then(BigDecimal then) {
            return build(Expr.constant(then));
        }

        /** Case then creator with an Constant Int. */
        @Pure public SimpleBuilder<V, C, Expr.Int, Integer> then(Integer then) {
            return build(then);
        }

        /** Case then creator with a Double. */
        @Pure public SimpleBuilder<V, C, Expr.Real, Double> then(Double then) {
            return build(then);
        }

        /** Case then creator with a Long. */
        @Pure public SimpleBuilder<V, C, Expr.Long, java.lang.Long> then(java.lang.Long then) {
            return build(then);
        }

        /** Case then creator with a Date. */
        @Pure public SimpleBuilder<V, C, Expr.Date, DateOnly> then(DateOnly then) {
            return build(then);
        }

        /** Case then creator with a DateTime. */
        @Pure public SimpleBuilder<V, C, Expr.DTime, DateTime> then(DateTime then) {
            return build(then);
        }

        /** Case then creator with a String. */
        @Pure public SimpleBuilder<V, C, Expr.Str, String> then(String then) {
            return build(then);
        }

        /** Case then creator with a String. */
        @Pure public <T extends java.lang.Enum<T> & Enumeration<T, I>, I> SimpleBuilder<V, C, Expr.Enum<T, I>, T> then(T then) {
            return build(then);
        }

        @NotNull @Pure private <E extends Expr<T>, T> SimpleBuilder<V, C, E, T> build(final E then) {
            return new SimpleBuilder<>(column, when, then);
        }
        @NotNull @Pure private <E extends Expr<T>, T> SimpleBuilder<V, C, E, T> build(final T then) {
            final E constant = cast(Expr.constant(then));
            return build(constant);
        }
    }  // end class SimpleCaseValue

    public static class TypedCaseValue<V extends Expr<?>, E extends Expr<T>, T, B extends CaseBuilder<V, E, T>> {
        private final B builder;
        private final V when;

        TypedCaseValue(B builder, V when) {
            this.builder = builder;
            this.when    = when;
        }

        /** Case then creator with an expression. */
        @Pure public B then(E then) {
            builder.put(when, then);
            return builder;
        }

        /** Case then creator with a constant. */
        @Pure public B then(T then) {
            return then(Predefined.<E, Expr<T>>cast(Expr.constant(then)));
        }
    }
}  // end class Case
