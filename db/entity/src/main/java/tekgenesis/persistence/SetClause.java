
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.persistence;

import org.jetbrains.annotations.Nullable;

import tekgenesis.persistence.expr.Expr;

/**
 * Insert/Update Set Clause.
 */
public class SetClause<T> {

    //~ Instance Fields ..............................................................................................................................

    private final TableField<T> field;
    private final Expr<T>       value;

    //~ Constructors .................................................................................................................................

    SetClause(final TableField<T> field, @Nullable final Expr<T> value) {
        this.field = field;
        this.value = value;
    }

    //~ Methods ......................................................................................................................................

    /** Return the sql representation of the set clause. */
    public String asSql() {
        return getFieldAsSql() + " = " + getValueAsSql();
    }

    /** Return the sql representation of the field clause. */
    public String getFieldAsSql() {
        return field.asSql(false);
    }

    /** Return the sql representation of the value clause. */
    public String getValueAsSql() {
        return value == null ? "null" : value.asSql(true);
    }
}
