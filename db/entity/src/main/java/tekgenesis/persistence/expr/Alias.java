
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.persistence.expr;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.jetbrains.annotations.NotNull;

/**
 * An alias for an expression.
 */
public class Alias<T> extends ExprImpl<T> {

    //~ Instance Fields ..............................................................................................................................

    private final Expr<T> target;

    //~ Constructors .................................................................................................................................

    protected Alias(final String name, final Expr<T> target) {
        super(name);
        this.target = target;
    }

    //~ Methods ......................................................................................................................................

    @Override public <Q> Q accept(final ExprVisitor<Q> visitor) {
        return target.accept(visitor);
    }

    @Override public Expr<T> unary(ExprOperator operator) {
        return target.unary(operator);
    }

    @NotNull @Override public Class<T> getType() {
        return target.getType();
    }

    @Override public T getValueFromResultSet(final ResultSet rs, final int columnIndex)
        throws SQLException
    {
        return target.getValueFromResultSet(rs, columnIndex);
    }
}
