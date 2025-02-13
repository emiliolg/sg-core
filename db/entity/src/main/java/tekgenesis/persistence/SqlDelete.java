
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

/**
 * An Sql Delete Statement.
 */
public class SqlDelete {

    //~ Instance Fields ..............................................................................................................................

    private final Criteria[]         cs;
    private final StoreHandler<?, ?> sh;

    //~ Constructors .................................................................................................................................

    SqlDelete(final StoreHandler<?, ?> storeHandler) {
        sh = storeHandler;
        cs = EMPTY_CS;
    }

    private SqlDelete(SqlDelete d, Criteria[] cs) {
        this.cs = cs;
        sh      = d.sh;
    }

    //~ Methods ......................................................................................................................................

    /** Execute the delete. */
    public void execute() {
        sh.deleteWhere(cs);
    }

    /** Add where condition. */
    @NotNull public SqlDelete where(final Criteria... condition) {
        return condition.length == 0 ? this : new SqlDelete(this, condition);
    }

    //~ Static Fields ................................................................................................................................

    private static final Criteria[] EMPTY_CS = new Criteria[0];
}
