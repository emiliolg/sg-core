
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.persistence;

import org.jetbrains.annotations.NotNull;

import tekgenesis.common.core.Enumeration;
import tekgenesis.persistence.expr.Column;
import tekgenesis.persistence.expr.Expr;

/**
 * A SubQuery over a select statement.
 */
public class SubQuery<T> extends Select<T> {

    //~ Instance Fields ..............................................................................................................................

    private final String alias;

    //~ Constructors .................................................................................................................................

    SubQuery(@NotNull Select<T> s, @NotNull String alias) {
        super(s, null, null);
        this.alias = alias;
    }

    //~ Methods ......................................................................................................................................

    /** Create an Boolean column. */
    public Criteria boolColumn(final String columnName) {
        return new Column.Bool(alias, columnName);
    }

    /** Create a DateOnly column. */
    public Expr.Date dateColumn(final String columnName) {
        return new Column.Date(alias, columnName);
    }

    /** Create a DateTime column. */
    public Expr.DTime dateTimeColumn(final String columnName) {
        return new Column.DTime(alias, columnName);
    }

    /** Create a Decimal column. */
    public Expr.Decimal decimalColumn(final String columnName, int decimals) {
        return new Column.Decimal(alias, columnName, decimals);
    }

    /** Create a Enum column. */
    public <E extends Enum<E> & Enumeration<E, I>, I> Expr.Enum<E, I> enumColumn(String columnName, Class<E> enumType) {
        return new Column.Enum<>(alias, columnName, enumType);
    }

    /** Create an integer column. */
    public Expr.Int intColumn(final String columnName) {
        return new Column.Int(alias, columnName);
    }

    /** Create a long column. */
    public Expr.Long longColumn(final String columnName) {
        return new Column.Long(alias, columnName);
    }

    /** Create a Real column. */
    public Expr.Real realColumn(final String columnName) {
        return new Column.Real(alias, columnName);
    }

    /** Create an String column. */
    public Expr.Str strColumn(final String columnName) {
        return new Column.Str(alias, columnName);
    }

    @Override String alias() {
        return alias;
    }
}  // end class SubQuery
