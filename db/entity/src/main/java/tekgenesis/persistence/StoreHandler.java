
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

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import tekgenesis.common.collections.ImmutableList;
import tekgenesis.common.core.DateTime;
import tekgenesis.database.Database;
import tekgenesis.persistence.exception.EntityNotFoundException;

import static tekgenesis.common.Predefined.*;
import static tekgenesis.persistence.CachedEntityInstanceImpl.dataField;
import static tekgenesis.persistence.Sql.selectFrom;

/**
 * The Manager of persistence for an Entity.
 */
public abstract class StoreHandler<I extends EntityInstance<I, K>, K> {

    //~ Instance Fields ..............................................................................................................................

    private final DbTable<I, K> dbTable;

    private final EntityTable<I, K> entityTable;

    private final StoreHandler<I, K> parent;

    //~ Constructors .................................................................................................................................

    protected StoreHandler(@NotNull final DbTable<I, K> dbTable, @Nullable final StoreHandler<I, K> parent) {
        this.parent  = parent;
        this.dbTable = dbTable;
        entityTable  = dbTable.entityTable();
    }

    //~ Methods ......................................................................................................................................

    /**
     * Check a record by key and updateTime if the updateTime matches returns true and lock the
     * record, else returns false.
     */
    public boolean checkAndLock(K key, DateTime updateTime) {
        return getParent().checkAndLock(key, updateTime);
    }

    /** Deletes an instance by key. */
    public abstract void delete(I instance);
    /** Delete all the elements with the specified Keys. */
    public abstract void delete(Iterable<K> keys);

    /** Deletes using Criteria. */
    public abstract void deleteWhere(Criteria... condition);

    /** find by Key. */
    @Nullable public abstract I find(K key);

    /** Find an item with the specified alternative key. */
    @Nullable public I findByKey(final int keyId, final Object key) {
        return getParent().findByKey(keyId, key);
    }

    /** Find by Key, Fail if it is not there. */
    @NotNull public final I findOrFail(@NotNull K key) {
        final I result = find(key);
        if (result != null) return result;
        throw EntityNotFoundException.notFound(getEntityName(), key.toString());
    }

    /** Find the persisted object ignoring the Session Cache. */
    @Nullable public I findPersisted(K key) {
        return getParent().findPersisted(key);
    }

    /** Find persisted or fail if it is not there. */
    @NotNull public final I findPersistedOrFail(@NotNull K key) {
        final I result = findPersisted(key);
        if (result != null) return result;
        throw EntityNotFoundException.notFound(getEntityName(), key.toString());
    }

    /** Insert with set clauses. */
    public int insert(final List<SetClause<?>> setClauses) {
        throw notImplemented("insert-set-where");
    }

    /** Plain insert (No Key). */
    public abstract void insert(I instance, boolean generateKey);

    /** Insert or Update. */
    public int insertOrUpdate(List<SetClause<?>> insertSetClauses, TableField<?>[] keyColumns, final List<SetClause<?>> setClauses,
                              final Criteria[] criteria) {
        throw notImplemented("insertOrUpdate");
    }

    /** List all the elements with the specified Keys. */
    public abstract ImmutableList<I> list(Iterable<K> keys);

    /** Merge the instance (Insert or update). */
    public abstract void merge(@NotNull I instance);

    /** Create Implementation specific SelectHandler. */
    public <E> Select.Handler<E> select(final Select<E> select) {
        return getParent().select(select);
    }

    /** Supports merge. */
    public boolean supportsMerge() {
        return true;
    }

    public String toString() {
        return getEntityName();
    }

    /** Updates instance. */
    public abstract void update(I instance);

    /** Update with set clause and criteria. */
    public int update(final List<SetClause<?>> setClauses, final Criteria[] criteria) {
        throw notImplemented("update-set-where");
    }

    /** Update checking that the instance has not changed. */
    public abstract void updateLocking(I newInstance);

    /** Returns updateTime for Key. */
    @Nullable public DateTime updateTime(final K key) {
        return getParent().updateTime(key);
    }

    /** Get the database this StoreHandler is associated to. */
    public Database getDatabase() {
        return getParent().getDatabase();
    }

    /** Get the name of the entity. */
    @NotNull public String getEntityName() {
        return entityTable.getEntityName().getFullName();
    }

    /** Get Table Metadata. */
    public TableMetadata<I, K> getMetadata() {
        return dbTable.metadata();
    }

    /** Get the Parent Store Handler. */
    @NotNull public StoreHandler<I, K> getParent() {
        return ensureNotNull(parent, "Top level Store Handler");
    }

    /** The class of the instance. */
    public Class<I> getType() {
        return getMetadata().getType();
    }

    protected I cache(final I instance, boolean cached) {
        final CachedEntityInstanceImpl.AbstractData<I, K> data = dataField(instance);
        data.setCached(cached);
        data.onLoad(instance);
        return instance;
    }

    protected ImmutableList<I> listAll() {
        return select(selectFrom(dbTable)).list();
    }

    protected void setCached(CachedEntityInstanceImpl.AbstractData<I, K> data, boolean b) {
        data.setCached(b);
    }

    @NotNull protected final DbTable<I, K> getDbTable() {
        return dbTable;
    }

    /** Return EntityTable Implementation. */
    @NotNull protected final EntityTable<I, K> getEntityTable() {
        return entityTable;
    }

    //~ Static Fields ................................................................................................................................

    private static final Factory NULL_FACTORY = new Factory() {
            @Override public <T extends EntityInstance<T, L>, L> StoreHandler<T, L> createHandler(final String        storeType,
                                                                                                  final DbTable<T, L> table) {
                return null;
            }
        };

    //~ Inner Interfaces .............................................................................................................................

    /**
     * Factory for {@link StoreHandler}.
     */
    public interface Factory {
        /** Create a Store Handler. */
        @Nullable <I extends EntityInstance<I, K>, K> StoreHandler<I, K> createHandler(String storeType, DbTable<I, K> dbTable);
        /** Get the Parent Factory. */
        @NotNull default Factory getParentFactory() {
            return NULL_FACTORY;
        }
    }

    //~ Inner Classes ................................................................................................................................

    public abstract static class DefaultFactory implements Factory {
        private final Factory parentFactory;

        protected DefaultFactory(Factory parentFactory) {
            this.parentFactory = parentFactory;
        }

        @NotNull @Override public final StoreHandler.Factory getParentFactory() {
            return parentFactory;
        }
    }
}  // end interface StoreHandler
