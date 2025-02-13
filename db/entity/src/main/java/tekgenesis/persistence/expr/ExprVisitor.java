
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.persistence.expr;

import tekgenesis.persistence.TableField;

import static tekgenesis.common.Predefined.notImplemented;

/**
 * A visitor for Expressions.
 */
public interface ExprVisitor<T> {

    //~ Methods ......................................................................................................................................

    /** True if the Visitor handles laziness for logic operands. */
    default boolean supportsLaziness() {
        return false;
    }

    /** Visit a Field. */
    T visit(TableField<?> e);

    /** Visit a Constant. */
    T visit(Const<?> e);

    /** Visit a nested query. */
    default T visit(NestedQuery<?> nestedQuery) {
        throw notImplemented("Nested");
    }
    /** Visit a column alias. */
    default T visit(Column<?> c) {
        throw notImplemented("Column Alias");
    }

    /** Visit a case expression. */
    default <V extends Expr<?>, E extends Expr<C>, C> T visit(Case<V, E, C> c) {
        throw notImplemented("case");
    }

    /** Visit an operator with operands. */
    T visit(ExprOperator operator, Object[] operands);
}
