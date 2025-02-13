
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
import java.util.Arrays;
import java.util.EnumSet;

import org.jetbrains.annotations.NotNull;

import tekgenesis.common.collections.Seq;
import tekgenesis.common.core.DateOnly;
import tekgenesis.common.core.DateTime;
import tekgenesis.common.core.Enumeration;
import tekgenesis.persistence.Criteria;

import static tekgenesis.common.Predefined.hashCodeAll;
import static tekgenesis.common.collections.Colls.immutable;
import static tekgenesis.persistence.Criteria.EMPTY;
import static tekgenesis.persistence.Criteria.TRUE;

/**
 * An Expression Operation.
 */
public abstract class Operation<T> extends ExprImpl<T> {

    //~ Instance Fields ..............................................................................................................................

    private final Expr<?>[]    operands;
    private final ExprOperator operator;

    //~ Constructors .................................................................................................................................

    /** Create an Operation. */
    Operation(ExprOperator operator, Expr<?>... operands) {
        this.operator = operator;
        this.operands = operands;
    }

    /** Create an Operation. */
    Operation(ExprOperator operator, Expr<?> first, Expr<?>... rest) {
        this.operator = operator;
        operands      = new Expr<?>[rest.length + 1];
        operands[0]   = first;
        System.arraycopy(rest, 0, operands, 1, rest.length);
    }

    //~ Methods ......................................................................................................................................

    @Override public <Q> Q accept(ExprVisitor<Q> visitor) {
        return visitor.visit(operator, visitOperands(visitor));
    }

    @Override public boolean equals(final Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof Operation)) return false;
        final Operation<?> op = (Operation<?>) obj;
        return operator == op.operator && Arrays.equals(operands, op.operands);
    }

    @Override public int hashCode() {
        return hashCodeAll(operator, (Object[]) operands);
    }

    /** Visit the operands. */
    @NotNull public <Q> Object[] visitOperands(final ExprVisitor<Q> visitor) {
        final int      n   = operator.validateArity(operands);
        final int      m   = operator.operandsToVisit(n);
        final Object[] ops = new Object[n];
        for (int i = 0; i < n; i++)
            ops[i] = i < m && (operator.isStrict() || !visitor.supportsLaziness()) ? operands[i].accept(visitor) : operands[i];
        return ops;
    }

    /** Returns the operands of this Operation. */
    public Expr<?>[] getOperands() {
        return operands;
    }

    /** Returns the Operator of this Operation. */
    public ExprOperator getOperator() {
        return operator;
    }

    /** Override operand in unary operations only. */
    void overrideOperand(Expr<T> operand) {
        if (operands.length == 1) operands[0] = operand;
    }

    //~ Methods ......................................................................................................................................

    /** Create a Bool operation over a list of criteria. */
    public static Criteria boolOperation(ExprOperator operator, Iterable<Criteria> criteriaList) {
        final Seq<Criteria> cs = immutable(criteriaList).filter(c -> c != null && c != EMPTY);
        return cs.getFirst()                                                                //
               .map(first -> boolOperation(operator, first, cs.drop(1).toArray(Criteria[]::new)))  //
               .orElse(TRUE);
    }
    /** Create a Bool operation over a list of criteria. */
    public static Criteria boolOperation(ExprOperator operator, Criteria first, Criteria[] rest) {
        // noinspection ConfusingArgumentToVarargsMethod
        return rest.length == 0 ? first : first.bool(operator, (Expr<?>[]) rest);
    }

    //~ Inner Classes ................................................................................................................................

    static class Bool extends Operation<Boolean> implements Criteria {
        /** Create an Operation. */
        Bool(ExprOperator operator, Expr<?> first, Expr<?>[] expr) {
            super(operator, first, expr);
        }
    }

    static class Date extends Operation<DateOnly> implements Expr.Date {
        Date(ExprOperator operator, Expr<?>... operands) {
            super(operator, operands);
        }
    }

    static class Decimal extends Operation<BigDecimal> implements Expr.Decimal {
        private final int decimals;

        /** Create an Operation. */
        Decimal(ExprOperator operator, int decimals, Expr<?>... operands) {
            super(operator, operands);
            this.decimals = decimals;
        }

        @Override public int getDecimals() {
            return decimals;
        }
    }

    static class DTime extends Operation<DateTime> implements Expr.DTime {
        DTime(ExprOperator operator, Expr<?>... operands) {
            super(operator, operands);
        }
    }

    static class Enum<T extends java.lang.Enum<T> & Enumeration<T, I>, I> extends Operation<T> implements Expr.Enum<T, I> {
        private final Class<T> enumType;

        /** Create an Operation. */
        Enum(ExprOperator operator, Class<T> enumType, Expr<?>... operands) {
            super(operator, operands);
            this.enumType = enumType;
        }

        @NotNull @Override public Class<T> getType() {
            return enumType;
        }
    }

    static class EnumerationSet<T extends java.lang.Enum<T> & Enumeration<T, I>, I> extends Operation<EnumSet<T>>
        implements Expr.EnumerationSet<T, I>
    {
        private final Class<T> enumType;

        /** Create an Operation. */
        EnumerationSet(ExprOperator operator, Class<T> enumType, Expr<?>... operands) {
            super(operator, operands);
            this.enumType = enumType;
        }

        @NotNull @Override public Class<T> getEnumType() {
            return enumType;
        }
    }

    static class Int extends Operation<Integer> implements Expr.Int {
        /** Create an Operation. */
        Int(ExprOperator operator, Expr<?>... operands) {
            super(operator, operands);
        }
    }

    static class Long extends Operation<java.lang.Long> implements Expr.Long {
        Long(final ExprOperator operator, final Expr<?>... operands) {
            super(operator, operands);
        }
    }

    static class Real extends Operation<Double> implements Expr.Real {
        /** Create an Operation. */
        Real(ExprOperator operator, Expr<?>... operands) {
            super(operator, operands);
        }
    }

    static class Str extends Operation<String> implements Expr.Str {
        Str(final ExprOperator operator) {
            super(operator);
        }
        Str(final ExprOperator operator, final Expr<?> first, final Expr<?>... rest) {
            super(operator, first, rest);
        }
    }
}  // end class Operation
