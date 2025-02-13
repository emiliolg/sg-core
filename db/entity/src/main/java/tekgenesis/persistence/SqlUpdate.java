
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.persistence;

import tekgenesis.persistence.expr.Expr;

/**
 * Sql Insert statement.
 */
public class SqlUpdate extends SqlBaseStatement<SqlUpdate> {

    //~ Instance Fields ..............................................................................................................................

    private Criteria[] criteria;

    //~ Constructors .................................................................................................................................

    SqlUpdate(StoreHandler<?, ?> storeHandler) {
        super(storeHandler);
        criteria = Select.EMPTY_CRITERIA;
    }

    //~ Methods ......................................................................................................................................

    /** Execute the update. */
    public int execute() {
        return sh.update(setClauses, criteria);
    }

    /** Add where clause. */
    public SqlUpdate where(Criteria... allOf) {
        criteria = allOf;
        return this;
    }

    //~ Inner Classes ................................................................................................................................

    /**
     * Builder for SqlUpdate.
     */
    public class Builder {
        /** Add a set clause. */
        public <T> SqlUpdate set(TableField<T> field, Expr<T> value) {
            return SqlUpdate.this.set(field, value);
        }
        /** Add a set clause. */
        public <T> SqlUpdate set(TableField<T> field, T value) {
            return SqlUpdate.this.set(field, value);
        }
    }
}  // end class SqlUpdate
