
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.persistence;

import java.lang.reflect.Field;
import java.util.*;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import tekgenesis.common.collections.ImmutableCollection;
import tekgenesis.common.collections.ImmutableList;
import tekgenesis.common.collections.Seq;
import tekgenesis.common.core.DateTime;
import tekgenesis.common.core.QName;
import tekgenesis.database.Database;
import tekgenesis.database.Databases;

import static tekgenesis.common.Predefined.cast;
import static tekgenesis.common.collections.Colls.immutable;
import static tekgenesis.common.collections.Colls.map;
import static tekgenesis.common.core.Strings.truncate;
import static tekgenesis.common.util.Reflection.getNotNullFieldValue;
import static tekgenesis.database.DbConstants.SCHEMA_SG;
import static tekgenesis.index.IndexConstants.MAX_ENTITY_LENGTH;
import static tekgenesis.persistence.EntityListenerType.*;
import static tekgenesis.persistence.Select.EMPTY_EXPR;
import static tekgenesis.type.Modifier.OPTIMISTIC_LOCKING;

/**
 * Implementation of EntityTable.
 */
@SuppressWarnings({ "ClassWithTooManyMethods", "OverlyComplexMethod", "OverlyComplexClass" })
public class EntityTable<T extends EntityInstance<T, K>, K> {

    //~ Instance Fields ..............................................................................................................................

    // private final Field                  dataField;
    @NotNull private final DbTable<T, K> dbTable;

    @NotNull private final QName entityName;

    @NotNull private final LinkedHashMap<String, TableField<?>> fields;
    private final EntityListenerMap<T>                          listeners;
    private final TableMetadata<T, K>                           metadata;

    private StoreHandler<T, K> storeHandler;
    private boolean            supportsMerge;

    //~ Constructors .................................................................................................................................

    /** Create an EntityTable. */
    public EntityTable(@NotNull final DbTable<T, K> dbTable) {
        this.dbTable = dbTable;
        metadata     = dbTable.metadata();
        fields       = new LinkedHashMap<>(dbTable.metadata().fieldMap());
        entityName   = metadata.getTypeQName();
        storeHandler = null;
        listeners    = new EntityListenerMap<>();
    }

    //~ Methods ......................................................................................................................................

    /** Register a Listener. */
    public void addListener(EntityListenerType listenerType, EntityListener<T> listener) {
        listeners.add(listenerType, listener);
    }

    /**
     * Check a record by key and updateTime if the updateTime matches returns true and lock the
     * record, else returns false.
     */
    public boolean checkAndLock(@NotNull K key, DateTime updateTime) {
        return storeHandler.checkAndLock(key, updateTime);
    }

    /** Delete the specified instance. */
    public T delete(@NotNull T instance) {
        deleteInternal(instance, true);
        return instance;
    }
    /** Delete the specified keys. */
    public void deleteKeys(Iterable<K> keys) {
        storeHandler.delete(keys);
    }

    /** Find an item with the specified primary key. */
    @Nullable public T find(@NotNull K key) {
        return storeHandler.find(key);
    }

    /** Find an item with the specified alternative key. */
    @Nullable public T findByKey(int keyId, @NotNull Object key) {
        metadata.validateKeyId(keyId);
        return storeHandler.findByKey(keyId, key);
    }

    /** Find an item with the primary key specified as a String with elements separated by ":". */
    @Nullable public T findByString(@NotNull String key) {
        return find(metadata.keyFromString(key));
    }
    /**
     * Find an item with the specified primary key., if the items is not present create a new one.
     */
    public final T findOrCreate(@NotNull K key) {
        final T result = find(key);
        return result != null ? result : metadata.createInstance(key);
    }

    /**
     * Find an item with the primary key specified as a String with elements separated by ":". If
     * the items is not present create a new one.
     */
    @NotNull public T findOrCreateByString(@NotNull String key) {
        return findOrCreate(metadata.keyFromString(key));
    }

    /** Find an item with the specified primary key. Fail, if is not present. */
    @NotNull public T findOrFail(@NotNull K key) {
        return storeHandler.findOrFail(key);
    }

    /** Find the Persisted Object (ignore Session Cache). */
    @Nullable public T findPersisted(@NotNull K key) {
        return storeHandler.findPersisted(key);
    }

    /** Find the Persisted Object (ignore Session Cache). */
    @Nullable public T findPersistedOrFail(@NotNull K key) {
        return storeHandler.findPersistedOrFail(key);
    }

    /** Find an item that match the specified predicate. */
    @Nullable public T findWhere(@NotNull Criteria criteria) {
        return Sql.selectFrom(dbTable).where(criteria).get();
    }

    /** Insert or Update the Entity in the table. */
    public T forcePersist(@NotNull T instance) {
        persist(instance, false);
        return instance;
    }

    /** Update the specified Object (all fields). */
    public T forceUpdate(@NotNull final T instance) {
        return update(null, instance, false);
    }

    /** Initialize the table handlers. */
    public EntityTable<T, K> init(StoreHandler<?, ?> sh) {
        storeHandler  = cast(sh);
        supportsMerge = storeHandler.supportsMerge();
        return this;
    }

    /** Insert the Entity in the table. */
    public T insert(@NotNull T instance) {
        return doInsert(instance, metadata.hasGeneratedKey());
    }

    /** Insert the Entity. DO not generate the KEY. */
    public void insertDoNotGenerate(@NotNull T instance) {
        doInsert(instance, false);
    }

    /** Return a {@link Seq} to iterate over all the elements with the specified keys. */
    @NotNull public ImmutableList<T> list(Set<K> keys) {
        return storeHandler.list(keys);
    }

    /** Return a {@link Seq} to iterate over all the elements with the specified keys. */
    @NotNull public ImmutableList<T> listFromStringKeys(Iterable<String> keys) {
        return storeHandler.list(map(keys, metadata::keyFromString));
    }

    /** Merge the specified Object (all fields). */
    public T merge(@NotNull final T instance) {
        if (supportsMerge) {
            metadata.updateAuditFields(instance, true, false, false);
            storeHandler.merge(instance);
            resetModified(instance);
        }
        else persist(instance);
        return instance;
    }

    /** Insert or Update the Entity in the table. */
    public T persist(@NotNull T instance) {
        return persist(instance, metadata.hasModifier(OPTIMISTIC_LOCKING));
    }
    /** De Register a Listener. */
    public void removeListener(EntityListenerType listenerType, EntityListener<T> listener) {
        listeners.remove(listenerType, listener);
    }

    /** Reset the Identity counter to the max id value for this table. */
    public void resetIdentitySequence() {
        if (metadata.hasGeneratedKey()) getDatabase().resetIdentitySequence(metadata.getTableQName(), metadata.getSequenceName());
    }

    /** Create a Select statement over this EntityTable. */
    public Select<T> selectFrom() {
        return new Select.Builder<>(metadata.getType(), EMPTY_EXPR).from(dbTable);
    }

    @Override public String toString() {
        return getEntityName().toString();
    }

    /** Update the specified Object (all fields). */
    public T update(@NotNull final T instance) {
        return update(null, instance, metadata.hasModifier(OPTIMISTIC_LOCKING));
    }

    /** Update LAST_DELETED record. */
    public void updateLastDeleted(DateTime lastDeleted) {
        final DateTime last   = lastDeleted.addMilliseconds(1);
        final String   entity = truncate(getEntityName().getFullName(), MAX_ENTITY_LENGTH);
        final Integer  count  = getDatabase().sqlStatement("select count(*) from %s.LAST_DELETED where ENTITY = ?", SCHEMA_SG)
                                .onArgs(entity)
                                .getInt();
        if (count == null || count == 0)
            getDatabase().sqlStatement("insert into %s.LAST_DELETED values (?,?)", SCHEMA_SG).onArgs(entity, last).execute();
        else getDatabase().sqlStatement("update %s.LAST_DELETED set TS = ? where ENTITY = ?", SCHEMA_SG).onArgs(last, entity).execute();
    }

    /** Retrieve update time for key. */
    @Nullable public DateTime updateTime(@NotNull K key) {
        return storeHandler.updateTime(key);
    }

    /** Returns the DbTable for this EntityTable. */
    @NotNull public DbTable<T, K> getDbTable() {
        return dbTable;
    }

    /** The name of the Entity. */
    @NotNull public QName getEntityName() {
        return entityName;
    }

    /** Return the Table Fields. */
    public ImmutableCollection<TableField<?>> getFields() {
        return cast(immutable(fields.values()));
    }

    /** Return Inner instances for this instance. */
    public Map<String, InnerEntitySeq<?>> getInners(T instance) {
        final HashMap<String, InnerEntitySeq<?>> map = new HashMap<>();
        for (final Field inner : metadata.getInnerFields()) {
            final InnerEntitySeq<?> seq = getNotNullFieldValue(instance, inner);
            map.put(inner.getName(), seq);
        }
        return map;
    }

    /** Returns table metadata. */
    public TableMetadata<T, K> getMetadata() {
        return metadata;
    }

    void deleteInternal(T instance, boolean deleteInners) {
        if (listeners.apply(BEFORE_DELETE, instance)) {
            // audit(instance, false);
            if (deleteInners) persistInners(instance, true);
            storeHandler.delete(instance);
            listeners.apply(AFTER_DELETE, instance);
        }
    }

    T doInsert(final T instance, final boolean generateKey) {
        if (listeners.apply(BEFORE_PERSIST_OR_INSERT, instance)) {
            metadata.updateAuditFields(instance, true, false, false);
            storeHandler.insert(instance, generateKey);
            persistInners(instance, false);
            listeners.apply(AFTER_PERSIST_OR_INSERT, instance);
            resetModified(instance);
        }
        return instance;
    }

    void persistInners(T instance, boolean delete) {
        for (final Field inner : metadata.getInnerFields()) {
            final EntitySeq.Inner<?> seq = getNotNullFieldValue(instance, inner);
            if (delete) seq.deleteAll();
            else seq.persist();
        }
    }

    String storeType() {
        return "";
    }

    /** Return the Database this Table is associated to. */
    Database getDatabase() {
        return storeHandler.getDatabase();
    }

    /** Return the StoreHandler for this table. */
    StoreHandler<T, K> getStoreHandler() {
        return storeHandler;
    }

    private T persist(@NotNull T instance, boolean lock) {
        final K key = instance.keyObject();
        if (!(instance instanceof PersistableInstance)) throw new IllegalArgumentException();

        if (metadata.hasGeneratedKey()) {
            final PersistableInstance<T, K> p = cast(instance);
            if (key.equals(DEFAULT_EMPTY_KEY)) p.insert();
            else p.update();
        }
        else {
            // storeHandler.merge(instance);
            final T oldInstance = findPersisted(key);
            if (oldInstance == null) insert(instance);
            else update(oldInstance, instance, lock);
        }
        return instance;
    }

    private void resetModified(@NotNull T instance) {
        if (instance instanceof EntityInstanceBaseImpl) ((EntityInstanceBaseImpl<?, ?>) instance).resetModified();
    }

    private T update(@Nullable T oldInstance, @NotNull T newInstance, boolean lock) {
        final T old = oldInstance == null && listeners.hasUpdateListener() ? storeHandler.findPersistedOrFail(newInstance.keyObject()) : oldInstance;

        if (listeners.apply(BEFORE_PERSIST_OR_UPDATE, old, newInstance)) {
            metadata.updateAuditFields(newInstance, false, false, false);
            if (lock) storeHandler.updateLocking(newInstance);
            else storeHandler.update(newInstance);
            persistInners(newInstance, false);
            listeners.apply(AFTER_PERSIST_OR_UPDATE, old, newInstance);
            resetModified(newInstance);
        }
        return newInstance;
    }

    //~ Methods ......................................................................................................................................

    /** Find an instance given the entityName and the key as an String. */
    @Nullable public static <T extends EntityInstance<T, ?>> T findInstance(final String entityName, final String key) {
        return cast(forName(entityName).findByString(key));
    }

    /** Create an EntityTable based on the name of the Entity. */
    public static <T extends EntityInstance<T, K>, K> EntityTable<T, K> forName(String entityName) {
        final DbTable<T, K> dt = DbTable.forName(entityName);
        return dt.entityTable();
    }

    /** Get The EntityTable for a given DbTable. */
    public static <T extends EntityInstance<T, K>, K> EntityTable<T, K> forTable(final DbTable<T, K> dbTable) {
        return dbTable.entityTable();
    }

    /** Return lastDeleted time for this entity. */
    @Nullable public static DateTime lastDeletedTime(String entityNameFullName) {
        return Databases.openDefault()
               .sqlStatement("select max(TS) from QName(SG,LAST_DELETED) where ENTITY = '%s'", truncate(entityNameFullName, MAX_ENTITY_LENGTH))
               .get(DateTime.class);
    }

    //~ Static Fields ................................................................................................................................

    private static final EnumSet<EntityListenerType> BEFORE_PERSIST_OR_INSERT = EnumSet.of(BEFORE_PERSIST, BEFORE_INSERT);
    private static final EnumSet<EntityListenerType> AFTER_PERSIST_OR_INSERT  = EnumSet.of(AFTER_PERSIST, AFTER_INSERT);
    private static final EnumSet<EntityListenerType> BEFORE_PERSIST_OR_UPDATE = EnumSet.of(BEFORE_PERSIST, BEFORE_UPDATE);
    private static final EnumSet<EntityListenerType> AFTER_PERSIST_OR_UPDATE  = EnumSet.of(AFTER_PERSIST, AFTER_UPDATE);

    public static final Integer DEFAULT_EMPTY_KEY = -1;

    //~ Inner Interfaces .............................................................................................................................

    public interface DeleteListener<T> {
        /** Called after deletion. */
        void after(T instance);
        /** Called before deletion. */
        void before(T instance);
    }
}  // end class EntityTable
