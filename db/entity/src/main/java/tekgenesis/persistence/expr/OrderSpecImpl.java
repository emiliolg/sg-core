
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.persistence.expr;

import org.jetbrains.annotations.Nullable;

import tekgenesis.common.core.Option;
import tekgenesis.persistence.OrderSpec;

import static tekgenesis.database.DbMacro.NlsSort;

/**
 * Implementation of OrderSpec interface.
 */
class OrderSpecImpl<T> implements OrderSpec<T> {

    //~ Instance Fields ..............................................................................................................................

    private final boolean            descending;
    private final Option<String>     nlsSort;
    @Nullable private final Boolean  nullsFirst;
    private final Expr<T>            target;

    //~ Constructors .................................................................................................................................

    OrderSpecImpl(Expr<T> target, boolean descending, @Nullable Boolean nullsFirst, @Nullable String nlsSort) {
        this.target     = target;
        this.descending = descending;
        this.nullsFirst = nullsFirst;
        this.nlsSort    = Option.option(nlsSort);
    }

    //~ Methods ......................................................................................................................................

    @Override public String asSql(boolean qualify) {
        final String        sql = target.asSql(qualify);
        final StringBuilder s   = new StringBuilder(nlsSort.isPresent() ? NlsSort.id() + "(" + sql + "," + nlsSort.get() + ")" : sql);
        if (descending) s.append(" desc");
        if (nullsFirst != null) s.append(nullsFirst ? " nulls first" : " nulls last");
        return s.toString();
    }

    @Override public OrderSpec<T> descending() {
        return new OrderSpecImpl<>(target, true, nullsFirst, nlsSort.getOrNull());
    }

    @Override public OrderSpec<T> nlsSort(final String sort) {
        return new OrderSpecImpl<>(target, descending, nullsFirst, sort);
    }

    @Override public OrderSpec<T> nullsFirst() {
        return new OrderSpecImpl<>(target, descending, true, nlsSort.getOrNull());
    }

    @Override public OrderSpec<T> nullsLast() {
        return new OrderSpecImpl<>(target, descending, false, nlsSort.getOrNull());
    }
}  // end class OrderSpecImpl
