
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

import tekgenesis.persistence.Select;

/**
 * A Nested query inside an expression.
 */
public class NestedQuery<T> extends ExprImpl<T> {

    //~ Instance Fields ..............................................................................................................................

    private final Select<T> select;

    //~ Constructors .................................................................................................................................

    NestedQuery(Select<T> select) {
        this.select = select;
    }

    //~ Methods ......................................................................................................................................

    @Override public <Q> Q accept(ExprVisitor<Q> visitor) {
        return visitor.visit(this);
    }

    /** Returns the nested query sql string. */
    public String asSql() {
        return select.asSql();
    }

    @Override public String asSql(final boolean qualify) {
        return "(" + asSql() + ")";
    }

    @NotNull @Override public Class<T> getType() {
        return select.getType();
    }

    @Override public T getValueFromResultSet(ResultSet rs, int columnIndex)
        throws SQLException
    {
        throw new IllegalStateException();
    }
}
