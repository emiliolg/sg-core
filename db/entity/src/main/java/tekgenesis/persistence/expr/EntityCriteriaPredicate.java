
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.persistence.expr;

import java.util.function.Predicate;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import tekgenesis.common.collections.ImmutableList;
import tekgenesis.persistence.Criteria;
import tekgenesis.persistence.EntityInstance;
import tekgenesis.persistence.TableField;

import static tekgenesis.common.Predefined.cast;
import static tekgenesis.common.Predefined.equal;

/**
 * A Predicate that test a Criteria over an Instance.
 */
public class EntityCriteriaPredicate<T extends EntityInstance<T, K>, K> implements Predicate<T> {

    //~ Instance Fields ..............................................................................................................................

    private final ImmutableList<Criteria> criteriaSet;

    //~ Constructors .................................................................................................................................

    /** Create the Predicate. */
    public EntityCriteriaPredicate(Criteria... criteria) {
        criteriaSet = ImmutableList.fromArray(criteria);
    }

    //~ Methods ......................................................................................................................................

    @Override public boolean test(@Nullable T instance) {
        if (instance == null) return false;
        final ExprVisitor<Object> visitor = createVisitor(instance);
        return criteriaSet.forAll(c -> bool(c, visitor));
    }  // end method test

    @NotNull private ExprVisitor<Object> createVisitor(@NotNull final T instance) {
        return new ExprVisitor<Object>() {
            @Nullable @Override public Object visit(TableField<?> e) {
                return e.getValue(instance);
            }

            @Override public Object visit(Const<?> e) {
                return e.getValue();
            }

            @Override public boolean supportsLaziness() {
                return true;
            }

            @Override
            @SuppressWarnings("MethodWithMultipleReturnPoints")
            public Object visit(final ExprOperator operator, final Object[] operands) {
                switch (operator) {
                case AND:
                    for (final Object op : operands) {
                        if (!bool(op, this)) return false;
                    }
                    return true;
                case OR:
                    for (final Object op : operands) {
                        if (bool(op, this)) return true;
                    }
                    return false;
                case NOT:
                    return !Boolean.TRUE.equals(operands[0]);
                case EQ:
                    return equal(operands[0], operands[1]);
                case NE:
                    return !equal(operands[0], operands[1]);
                case GE:
                    return cmp(operands) >= 0;
                case GT:
                    return cmp(operands) > 0;
                case LE:
                    return cmp(operands) <= 0;
                case LT:
                    return cmp(operands) < 0;
                case IS_NULL:
                    return operands[0] == null;
                default:
                    throw new UnsupportedOperationException(operator.name() + " expression not supported.");
                }
            }

            private int cmp(Object[] op) {
                final Comparable<Object> a = cast(op[0]);
                final Comparable<Object> b = cast(op[1]);
                return a == b ? 0 : a == null ? -1 : b == null ? 1 : a.compareTo(b);
            }
        };
    }  // end method createVisitor

    //~ Methods ......................................................................................................................................

    private static boolean bool(Object o, ExprVisitor<Object> visitor) {
        return Boolean.TRUE.equals(((Expr<?>) o).accept(visitor));
    }
}  // end class EntityCriteriaPredicate
