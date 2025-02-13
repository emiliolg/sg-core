
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.persistence.sql;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jetbrains.annotations.Nullable;

import tekgenesis.common.collections.ImmutableList;
import tekgenesis.common.collections.Seq;
import tekgenesis.common.core.DateTime;
import tekgenesis.common.core.Tuple;
import tekgenesis.database.Database;
import tekgenesis.database.ResultHandler;
import tekgenesis.database.SqlStatement;
import tekgenesis.database.support.DummyConnection;
import tekgenesis.persistence.EntityInstance;
import tekgenesis.persistence.TableField;
import tekgenesis.transaction.TransactionManager;

import static tekgenesis.common.Predefined.cast;

/**
 * A Cache for prepared Statements.
 */
class StatementCache extends DummyConnection {

    //~ Instance Fields ..............................................................................................................................

    private final Map<String, SqlStatement.Prepared> cache;
    private final TransactionManager                 tm;

    //~ Constructors .................................................................................................................................

    /** Create a StatementCache. */
    StatementCache(final TransactionManager tm) {
        cache   = new HashMap<>();
        this.tm = tm;
    }

    //~ Methods ......................................................................................................................................

    @Override public void close() {
        for (final SqlStatement.Prepared stmt : cache.values())
            stmt.close();
        cache.clear();
    }

    @Override public void commit() {
        close();
    }

    @Override public void rollback() {
        close();
    }

    <K> boolean checkAndLock(Database db, Proto proto, K key, DateTime updateTime) {
        final SqlStatement.Prepared             stmt = getStatement(proto.sql, db, "");
        final ImmutableList<TableField<Object>> args = cast(proto.args.toList());
        args.get(0).setParameter(stmt, 1, updateTime);
        args.get(1).setParameter(stmt, 2, updateTime);
        setKeyParameters(stmt, args.subList(2, args.size()), 3, key);
        return stmt.executeDml() == 1;
    }

    /** End the current batch. */
    void endBatch() {
        for (final SqlStatement.Prepared stmt : cache.values())
            stmt.executeBatch();
    }

    <I extends EntityInstance<I, K>, K> int execute(final Database db, final I instance, final Proto proto) {
        final SqlStatement.Prepared stmt = getStatement(proto.sql, db, "");

        int i = 0;
        for (final TableField<?> field : proto.args)
            field.setParameterFromInstance(stmt, ++i, instance);

        if (tm.isBatchActive()) {
            stmt.batch();
            return 1;
        }
        else return stmt.executeDml();
    }

    <I extends EntityInstance<I, K>, K> void executeWithKey(final Database db, final I instance, final Proto proto,
                                                            @Nullable final TableField.Num<?, ?> keyField) {
        if (keyField == null) throw new UnsupportedOperationException("Insert with Key");

        final SqlStatement.Prepared stmt = getStatement(proto.sql, db, keyField.getName());

        int i = 0;
        for (final TableField<?> field : proto.args)
            field.setParameterFromInstance(stmt, ++i, instance);

        final Number k = stmt.insertWithKey();
        if (k != null) keyField.setValue(instance, k);
    }

    @Nullable <T, K> T find(Database db, Proto proto, K key, ResultHandler<T> resultHandler) {
        final SqlStatement.Prepared stmt = getStatement(proto.sql, db, "");
        setKeyParameters(stmt, cast(proto.args.toList()), 1, key);
        return stmt.run(resultHandler);
    }  // end method find

    private void setKeyParameters(SqlStatement.Prepared stmt, List<TableField<Object>> keyFields, int offset, Object key) {
        if (keyFields.size() == 1) keyFields.get(0).setParameter(stmt, offset, key);
        else {
            final ImmutableList<?> l = ((Tuple<?, ?>) key).asList();
            for (int i = 0; i < keyFields.size(); i++)
                keyFields.get(i).setParameter(stmt, i + offset, l.get(i));
        }
    }  // end method setKeyParameters

    /** Get a (possibly cached) Prepared statement. */
    private SqlStatement.Prepared getStatement(String sql, Database db, String keyFieldName) {
        return cache.computeIfAbsent(sql, k -> db.nativeSql(sql).returnKeys(keyFieldName).prepare());
    }

    //~ Inner Classes ................................................................................................................................

    /**
     * The sql for a Statement together with the list of fields for the arguments.
     */
    static class Proto {
        final Seq<TableField<?>> args;
        final String             sql;

        Proto(final String sql, final Seq<TableField<?>> args) {
            this.sql  = sql;
            this.args = args;
        }
    }
}  // end class StatementCache
