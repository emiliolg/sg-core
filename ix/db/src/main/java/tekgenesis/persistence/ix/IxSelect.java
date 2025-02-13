
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.persistence.ix;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import tekgenesis.common.core.DateTime;
import tekgenesis.persistence.*;
import tekgenesis.persistence.expr.Expr;

/**
 * Some extended operations for Ideafix.
 */
public class IxSelect<T> extends Select<T> {

    //~ Instance Fields ..............................................................................................................................

    private final DateTime newer;

    private final int pageSize;

    //~ Constructors .................................................................................................................................

    /** Create an IxSelect statement. */
    private IxSelect(final Select<T> s, @Nullable DateTime newer, int pageSize) {
        super(s, null, null);
        this.pageSize = pageSize;
        this.newer    = newer;
    }

    //~ Methods ......................................................................................................................................

    /** Returns a SubQuery. */
    public SubQuery<T> as(final String aliasName) {
        throw new UnsupportedOperationException();
    }

    /** Returns the statement as an Sql string. */
    public String asSql() {
        throw new UnsupportedOperationException();
    }

    /** Make the query return non-duplicate elements. */
    @NotNull public Select<T> distinct() {
        throw new UnsupportedOperationException();
    }

    /** Make the query lock for update. */
    @NotNull public Select<T> forUpdate() {
        throw new UnsupportedOperationException();
    }

    /** Group by the specified expressions. */
    @NotNull public Select<T> groupBy(Expr<?>... groupByExpressions) {
        throw new UnsupportedOperationException();
    }

    /** Defines the filters for aggregation. */
    @NotNull public Select<T> having(Criteria... havingCriteria) {
        throw new UnsupportedOperationException();
    }

    /** Join with the specified Table using the given Criteria. */
    @NotNull public Select<T> join(TableLike<?> table, Criteria... joinCondition) {
        throw new UnsupportedOperationException();
    }

    /** Left Outer Join with the specified Table using the given Criteria. */
    @NotNull public Select<T> leftOuterJoin(TableLike<?> table, Criteria... joinCondition) {
        throw new UnsupportedOperationException();
    }

    /** Order the query according to the given {@link OrderSpec}. */
    @NotNull public Select<T> orderBy(OrderSpec<?>... orderByExpressions) {
        throw new UnsupportedOperationException();
    }

    @Nullable DateTime getNewer() {
        return newer;
    }

    int getPageSize() {
        return pageSize;
    }

    //~ Methods ......................................................................................................................................

    /** Add a newer clause to the select statement. */
    public static <E> IxSelect<E> newer(Select<E> s, DateTime n) {
        return new IxSelect<>(s, n, s instanceof IxSelect ? ((IxSelect<?>) s).pageSize : 0);
    }

    /** Add a page-size clause to the select statement. */
    public static <E> IxSelect<E> pageSize(Select<E> s, int size) {
        return new IxSelect<>(s, s instanceof IxSelect ? ((IxSelect<?>) s).newer : null, size);
    }
}  // end class IxSelect
