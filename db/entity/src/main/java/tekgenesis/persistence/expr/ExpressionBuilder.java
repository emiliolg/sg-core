
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

class ExpressionBuilder implements ExprVisitor<String> {

    //~ Instance Fields ..............................................................................................................................

    private final boolean qualify;

    //~ Constructors .................................................................................................................................

    ExpressionBuilder(final boolean qualify) {
        this.qualify = qualify;
    }

    //~ Methods ......................................................................................................................................

    public String toSql(final Expr<?> e) {
        return e.asSql(qualify);
    }

    @Override public String visit(TableField<?> e) {
        return toSql(e);
    }

    @Override public String visit(Const<?> e) {
        return toSql(e);
    }

    @Override public String visit(NestedQuery<?> nestedQuery) {
        return toSql(nestedQuery);
    }

    @Override public String visit(final Column<?> column) {
        return toSql(column);
    }

    @Override public <V extends Expr<?>, E extends Expr<C>, C> String visit(Case<V, E, C> c) {
        return c.asSql(this);
    }

    @Override public String visit(final ExprOperator operator, final Object[] operands) {
        return operator.asSql(operands);
    }
}  // end class ExpressionBuilder
