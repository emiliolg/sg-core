
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.persistence;

import java.util.ArrayList;
import java.util.List;

import org.jetbrains.annotations.Nullable;

import tekgenesis.persistence.expr.Expr;

import static tekgenesis.common.Predefined.cast;

/**
 * Base Statement for instructions with set clauses.
 */
class SqlBaseStatement<S> {

    //~ Instance Fields ..............................................................................................................................

    protected final List<SetClause<?>> setClauses;
    protected final StoreHandler<?, ?> sh;

    //~ Constructors .................................................................................................................................

    SqlBaseStatement(StoreHandler<?, ?> storeHandler) {
        setClauses = new ArrayList<>();
        sh         = storeHandler;
    }

    //~ Methods ......................................................................................................................................

    /** Add a set clause. */

    public <T> S set(TableField<T> field, Expr<T> value) {
        return doSet(field, value);
    }

    /** Add a set clause. */
    public <T> S set(TableField<T> field, @Nullable T value) {
        return doSet(field, value == null ? null : Expr.constant(value));
    }

    /** Set as null. */
    public S setAsNull(TableField<?> field) {
        return doSet(field, null);
    }

    private <T> S doSet(TableField<T> field, @Nullable Expr<T> value) {
        setClauses.add(new SetClause<>(field, value));
        return cast(this);
    }
}
