
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.persistence;

import tekgenesis.common.core.Arrays;
import tekgenesis.persistence.expr.Expr;

/**
 * Sql Insert statement.
 */
public class SqlInsert extends SqlBaseStatement<SqlInsert> {

    //~ Constructors .................................................................................................................................

    SqlInsert(StoreHandler<?, ?> storeHandler) {
        super(storeHandler);
    }

    //~ Methods ......................................................................................................................................

    /** Execute the update. */
    public int execute() {
        return sh.insert(setClauses);
    }

    /** In case a record with the given key columns already exists then ... */
    public SqlInsertOrUpdate.OnConflict onConflict(TableField<?> keyColumn, TableField<?>... keyColumns) {
        return new SqlInsertOrUpdate(sh, setClauses, Arrays.arrayOf(keyColumn, keyColumns)).new OnConflict();
    }

    //~ Inner Classes ................................................................................................................................

    /**
     * Insert Builder.
     */
    public class Builder {
        /** Add a set clause. */
        public <T> SqlInsert set(TableField<T> field, Expr<T> value) {
            return SqlInsert.this.set(field, value);
        }
        /** Add a set clause. */
        public <T> SqlInsert set(TableField<T> field, T value) {
            return SqlInsert.this.set(field, value);
        }
    }
}  // end class SqlInsert
