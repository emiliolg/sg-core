
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.persistence.sql;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import tekgenesis.common.collections.ImmutableList;
import tekgenesis.common.core.DateTime;
import tekgenesis.database.*;
import tekgenesis.database.exception.UniqueViolationException;
import tekgenesis.database.support.JdbcUtils;
import tekgenesis.persistence.*;
import tekgenesis.persistence.exception.InstanceVersionMismatchException;

import static tekgenesis.common.Predefined.cast;
import static tekgenesis.common.Predefined.ensureNotNull;
import static tekgenesis.common.util.Reflection.construct;
import static tekgenesis.database.DbConstants.SCHEMA_SG;
import static tekgenesis.database.DbMacro.DbCurrentTime;
import static tekgenesis.database.ResultHandler.singleRowHandler;
import static tekgenesis.type.Modifier.REMOTABLE;

/**
 * The QueryDSL Entity for a given EntityTable.
 */
public class SqlStoreHandler<T extends EntityInstance<T, K>, K> extends StoreHandler<T, K> {

    //~ Instance Fields ..............................................................................................................................

    @NotNull private final Database db;

    @NotNull private final RowMapper<T>            entityRowMapper;
    @NotNull private final SqlStoreHandlerFactory  factory;

    @NotNull private final ResultHandler<T>       findResultHandler;
    @Nullable private final TableField.Num<?, ?>  keyField;
    @NotNull private final StatementBuilder       stmtBuilder;
    private final ResultHandler<DateTime>         updateTimeRetriever;

    //~ Constructors .................................................................................................................................

    /** Create a QueryDSLEntity. */
    public SqlStoreHandler(@NotNull SqlStoreHandlerFactory f, @NotNull final Database db, @NotNull final DbTable<T, K> dbTable) {
        super(dbTable, null);
        final TableMetadata<T, K> metadata = dbTable.metadata();
        final TableField<?>       b        = metadata.hasGeneratedKey() ? metadata.getPrimaryKey().get(0) : null;
        keyField            = cast(b);
        this.db             = db;
        stmtBuilder         = new StatementBuilder(db, dbTable);
        findResultHandler   = singleRowHandler(this::doMapRow);
        updateTimeRetriever = singleRowHandler(rs -> JdbcUtils.fromTimestamp(rs.getTimestamp(1)));
        entityRowMapper     = this::doMapRow;
        factory             = f;
    }

    //~ Methods ......................................................................................................................................

    /**
     * Check a record by key and updateTime if the updateTime matches returns true and lock the
     * record, else returns false.
     */
    public boolean checkAndLock(K key, DateTime updateTime) {
        return getStatementCache().checkAndLock(db, ensureNotNull(stmtBuilder.checkAndLock().get()), key, updateTime);
    }

    /** Deletes an instance by key. */

    public void delete(T instance) {
        executeStatement(stmtBuilder.delete(), instance);
        updateLastDeleted();
        insertDeletion(instance);
    }  // end method delete
    @Override public void delete(Iterable<K> keys) {
        final List<K> keySet = new ArrayList<>();
        for (final K key : keys) {
            keySet.add(key);
            if (keySet.size() == KEY_SET_CHUNK) {
                stmtBuilder.delete(keySet).execute();
                keySet.clear();
            }
        }
        if (!keySet.isEmpty()) stmtBuilder.delete(keySet).execute();
    }

    /** Deletes based on criteria. */
    public void deleteWhere(Criteria... condition) {
        insertDeletions(condition);
        stmtBuilder.delete(condition);
        updateLastDeleted();
    }  // end method deleteWhere

    /** Returns a T given its key. */
    public T find(K key) {
        return getStatementCache().find(db, ensureNotNull(stmtBuilder.find().get()), key, findResultHandler);
    }

    @Nullable @Override public T findByKey(final int keyId, final Object key) {
        return getStatementCache().find(db, stmtBuilder.findByKey(keyId), key, findResultHandler);
    }

    @Override public T findPersisted(K key) {
        return find(key);
    }

    /** Insert with set clause. */
    @Override public int insert(final List<SetClause<?>> setClauses) {
        return stmtBuilder.insert(setClauses);
    }

    /** Inserts an instance. */
    @Override public void insert(T instance, final boolean generateKey) {
        incrementVersion(instance);
        if (generateKey) getStatementCache().executeWithKey(db, instance, ensureNotNull(stmtBuilder.insertWithKey().get()), keyField);
        else executeStatement(stmtBuilder.insert(), instance);
    }  // end method insert

    @Override public int insertOrUpdate(List<SetClause<?>> insertSetClauses, TableField<?>[] keyColumns, List<SetClause<?>> setClauses,
                                        Criteria[] criteria) {
        return stmtBuilder.insertOrUpdate(insertSetClauses, keyColumns, setClauses, criteria);
    }

    @Override public ImmutableList<T> list(Iterable<K> keys) {
        return ImmutableList.build(b -> {
            final List<K> keySet = new ArrayList<>();
            for (final K key : keys) {
                keySet.add(key);
                if (keySet.size() == KEY_SET_CHUNK) {
                    b.addAll(stmtBuilder.listByKeys(keySet).list(entityRowMapper));
                    keySet.clear();
                }
            }
            if (!keySet.isEmpty()) b.addAll(stmtBuilder.listByKeys(keySet).list(entityRowMapper));
        });
    }

    /** Insert or update the specified instance. */
    public void merge(@NotNull T instance) {
        executeStatement(stmtBuilder.merge(), instance);
    }

    @Override public <R> Select.Handler<R> select(final Select<R> select) {
        return new SqlSelectHandler<>(select, db, entityRowMapper);
    }  // end method select

    @Override public boolean supportsMerge() {
        return getDatabase().getDatabaseType().supportsMerge();
    }

    @Override public void update(T instance) {
        incrementVersion(instance);
        executeStatement(stmtBuilder.update(), instance);
    }

    /** Update with set clause and criteria. */
    @Override public int update(final List<SetClause<?>> setClauses, final Criteria[] criteria) {
        return stmtBuilder.update(setClauses, criteria);
    }

    @Override public void updateLocking(T instance) {
        final long currentVersion = incrementVersion(instance);
        final int  updated        = executeStatement(stmtBuilder.locking(currentVersion), instance);
        if (updated != 1) throw InstanceVersionMismatchException.mismatch(instance.metadata().getTypeName(), instance.keyAsString());
    }

    @Nullable @Override public DateTime updateTime(final K key) {
        return getStatementCache().find(db, ensureNotNull(stmtBuilder.updateTime().get()), key, updateTimeRetriever);
    }

    @Override public Database getDatabase() {
        return db;
    }

    private T doMapRow(ResultSet rs) {
        final T result = construct(getType());
        int     i      = 0;
        for (final TableField<?> field : getEntityTable().getFields())
            field.setFromResultSet(result, rs, ++i);
        return result;
    }

    private int executeStatement(final Supplier<StatementCache.Proto> statement, final T instance) {
        return getStatementCache().execute(db, instance, ensureNotNull(statement.get()));
    }

    private long incrementVersion(T instance) {
        final TableField.LongFld versionField = getMetadata().getVersionField();
        if (versionField == null) return 0;
        final Long v      = versionField.getValue(instance);
        final long result = v == null ? 0L : v;
        versionField.setValue(instance, result + 1);
        return result;
    }

    private void insertDeletion(T instance) {
        if (getMetadata().isRemotable()) {
            final SqlStatement sqlStatement = getDatabase().sqlStatement(
                    getDatabase().getDatabaseType() == DatabaseType.POSTGRES
                        ? "insert into %s.DELETED_INSTANCES values (?,?,%s) on conflict (ENTITY,DELETED_KEY,TS) do nothing"
                        : "insert into %s.DELETED_INSTANCES values (?,?,%s)",
                    SCHEMA_SG,
                    DbCurrentTime.name());

            try {
                sqlStatement.onArgs(getEntityName(), instance.keyAsString()).execute();
            }
            catch (final UniqueViolationException e) {
                // ignore
            }
        }
    }

    private void insertDeletions(Criteria[] condition) {
        if (getMetadata().isRemotable()) {
            final ImmutableList<T> toDelete = Sql.selectFrom(getDbTable()).where(condition).list();
            for (final T t : toDelete)
                insertDeletion(t);
        }
    }

    private void updateLastDeleted() {
        if (getMetadata().getSearcher().isPresent() || getMetadata().hasModifier(REMOTABLE)) getEntityTable().updateLastDeleted(db.currentTime());
    }

    private StatementCache getStatementCache() {
        return factory.getConnection(false);
    }

    //~ Static Fields ................................................................................................................................

    private static final int KEY_SET_CHUNK = 1000;
}  // end class SqlStoreHandler
