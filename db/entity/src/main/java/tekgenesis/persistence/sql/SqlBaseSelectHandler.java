
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.persistence.sql;

import java.util.function.Function;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import tekgenesis.common.collections.ImmutableList;
import tekgenesis.common.core.Option;
import tekgenesis.common.core.StepResult;
import tekgenesis.persistence.Select;
import tekgenesis.persistence.expr.Expr;

import static tekgenesis.persistence.sql.StatementBuilder.*;

/**
 * Base Handler used mainly for generating Sql as an String.
 */
public class SqlBaseSelectHandler<R> extends Select.Handler<R> {

    //~ Constructors .................................................................................................................................

    SqlBaseSelectHandler(Select<R> select) {
        super(select);
    }

    //~ Methods ......................................................................................................................................

    @Nullable protected String asSql(Expr<?>... es) {
        return asSql(selectItems(es));
    }

    @Override protected long count() {
        throw new IllegalStateException();
    }
    @Override protected boolean exists() {
        throw new IllegalStateException();
    }
    @Override protected <R1> Option<R1> forEachReturning(Function<? super R, StepResult<R1>> step, Option<R1> finalValue) {
        throw new IllegalStateException();
    }

    @Nullable @Override protected R get() {
        throw new IllegalStateException();
    }

    @Override protected ImmutableList<R> list() {
        throw new IllegalStateException();
    }

    @Nullable private String asSql(final String selectItems) {
        final boolean   qualify = qualify();
        final Expr<?>[] groupBy = getGroupBy();
        return buildSelect(selectItems,
            from(),
            getJoins(),
            getUnions(),
            criteriaToSql(getWhere(), qualify),
            convertToSql(qualify, false, groupBy),
            criteriaToSql(getHaving(), qualify),
            orderSpecToSql(getOrderBy(), qualify),
            getFlags());
    }

    @NotNull private String selectItems(final Expr<?>[] es) {
        return es.length == 0 ? fieldsToSql(fromFields(), qualify()) : convertToSql(qualify(), true, es);
    }

    //~ Methods ......................................................................................................................................

    /** Return the select as an String in neutral SQL. */
    @Nullable public static <T> String asSql(final Select<T> select) {
        final SqlBaseSelectHandler<T> handler     = new SqlBaseSelectHandler<>(select);
        final Expr<?>[]               expressions = handler.getExpressions();
        return handler.asSql(expressions);
    }
}  // end class SqlBaseSelectHandler
