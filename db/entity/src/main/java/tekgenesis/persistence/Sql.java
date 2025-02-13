
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.persistence;

import tekgenesis.database.Database;
import tekgenesis.persistence.expr.Expr;

import static tekgenesis.persistence.EntityTable.forTable;

/**
 * Sql Factory and helper.
 */
public class Sql {

    //~ Constructors .................................................................................................................................

    private Sql() {}

    //~ Methods ......................................................................................................................................

    /** Database for the specified DbTable. */
    public static Database databaseFor(DbTable<?, ?> table) {
        return table.entityTable().getDatabase();
    }

    /** Create a Delete Statement. */
    public static SqlDelete deleteFrom(DbTable<?, ?> table) {
        return new SqlDelete(forTable(table).getStoreHandler());
    }

    /** Create an Insert Statement. */
    public static SqlInsert.Builder insertInto(DbTable<?, ?> table) {
        return new SqlInsert(forTable(table).getStoreHandler()).new Builder();
    }

    /** Create a Query Statement. */
    public static <T> Select.Builder<T> select(Expr<T> expr) {
        return new Select.Builder<>(expr.getType(), expr);
    }

    /** Create a Query Statement. Selecting all fields from the specified table. */
    public static <T> Select.Builder<T> select(TableLike<T> tableLike) {
        return new Select.Builder<>(tableLike.getType(), tableLike.getExpressions());
    }

    /** Create a Query Statement. */
    public static Select.Builder<QueryTuple> select(Expr<?> first, Expr<?>... rest) {
        final Expr<?>[] expr = new Expr<?>[rest.length + 1];
        expr[0] = first;
        System.arraycopy(rest, 0, expr, 1, rest.length);
        return new Select.Builder<>(QueryTuple.class, expr);
    }

    /** Create a Select Statement. */
    public static <T> Select<T> selectFrom(TableLike<T> tableLike) {
        return select(tableLike).from(tableLike);
    }

    /** Create an update Statement. */
    public static SqlUpdate.Builder update(DbTable<?, ?> table) {
        return new SqlUpdate(forTable(table).getStoreHandler()).new Builder();
    }
}  // end class Sql
