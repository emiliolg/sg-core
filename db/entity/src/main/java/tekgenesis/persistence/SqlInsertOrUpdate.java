
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.persistence;

import java.util.List;

import tekgenesis.persistence.expr.Expr;

/**
 * Sql Update or Insert (Insert on conflict update ...
 */
public class SqlInsertOrUpdate extends SqlBaseStatement<SqlInsertOrUpdate> {

    //~ Instance Fields ..............................................................................................................................

    private Criteria[]               criteria;
    private final List<SetClause<?>> insertSetClauses;
    private final TableField<?>[]    keyColumns;

    //~ Constructors .................................................................................................................................

    SqlInsertOrUpdate(StoreHandler<?, ?> sh, List<SetClause<?>> insertSetClauses, TableField<?>[] keyColumns) {
        super(sh);
        this.insertSetClauses = insertSetClauses;
        this.keyColumns       = keyColumns;
        criteria              = Select.EMPTY_CRITERIA;
    }

    //~ Methods ......................................................................................................................................

    /** Execute the insert or update. */
    public int execute() {
        return sh.insertOrUpdate(insertSetClauses, keyColumns, setClauses, criteria);
    }

    /** Add where clause. */
    public SqlInsertOrUpdate where(Criteria... allOf) {
        criteria = allOf;
        return this;
    }

    //~ Inner Classes ................................................................................................................................

    /**
     * Builder for the update items of the Insert or Update.
     */
    public class Builder {
        /** Add a set clause. */
        public <T> SqlInsertOrUpdate set(TableField<T> field, Expr<T> value) {
            return SqlInsertOrUpdate.this.set(field, value);
        }
        /** Add a set clause. */
        public <T> SqlInsertOrUpdate set(TableField<T> field, T value) {
            return SqlInsertOrUpdate.this.set(field, value);
        }
    }

    /**
     * Builder for InsertOrUpdate.
     */
    public class OnConflict {
        /** On Conflict just ignore the insert. */
        public SqlInsertOrUpdate doNothing() {
            return SqlInsertOrUpdate.this;
        }
        /** On Conflict perform an update. */
        public Builder update() {
            return new Builder();
        }
    }
}  // end class SqlInsertOrUpdate
