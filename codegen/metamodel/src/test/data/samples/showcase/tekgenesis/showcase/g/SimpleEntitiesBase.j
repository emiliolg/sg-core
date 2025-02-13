package tekgenesis.showcase.g;

import tekgenesis.showcase.AnotherSimpleEntity;
import java.util.function.Consumer;
import tekgenesis.common.util.Conversions;
import tekgenesis.persistence.Criteria;
import tekgenesis.common.core.DateTime;
import tekgenesis.persistence.DbTable;
import tekgenesis.persistence.EntityInstanceImpl;
import tekgenesis.persistence.EntityListener;
import tekgenesis.persistence.EntityListenerType;
import tekgenesis.persistence.EntityRef;
import tekgenesis.persistence.EntityTable;
import tekgenesis.common.collections.ImmutableList;
import tekgenesis.persistence.InnerEntitySeq;
import tekgenesis.persistence.InnerInstance;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import tekgenesis.database.RowMapper;
import tekgenesis.persistence.Select;
import tekgenesis.common.collections.Seq;
import java.util.Set;
import tekgenesis.showcase.SimpleEntities;
import tekgenesis.showcase.SimpleEntity;
import tekgenesis.common.core.Tuple;
import static tekgenesis.showcase.g.AnotherSimpleEntityTable.ANOTHER_SIMPLE_ENTITY;
import static tekgenesis.showcase.g.SimpleEntitiesTable.SIMPLE_ENTITIES;
import static tekgenesis.showcase.g.SimpleEntityTable.SIMPLE_ENTITY;
import static tekgenesis.common.util.Conversions.formatList;
import static tekgenesis.persistence.Sql.selectFrom;

/** 
 * Generated base class for entity: SimpleEntities.
 * Don't modify this as this is an auto generated class that's gets generated
 * every time the meta model file is modified. Use subclass instead.
 */
@SuppressWarnings({"DuplicateStringLiteralInspection", "WeakerAccess", "FieldMayBeFinal", "FieldCanBeLocal", "ConstantConditions", "ClassWithTooManyMethods", "ClassWithTooManyFields", "MagicNumber", "MethodOverridesStaticMethodOfSuperclass", "ParameterHidesMemberVariable", "FieldNameHidesFieldInSuperclass", "OverlyComplexClass", "RedundantCast", "UnusedReturnValue"})
public abstract class SimpleEntitiesBase
    extends EntityInstanceImpl<SimpleEntities,Tuple<Integer,Integer>>
    implements InnerInstance<SimpleEntities,Tuple<Integer,Integer>,AnotherSimpleEntity,Integer>
{

    //~ Fields ...................................................................................................................

    int anotherSimpleEntityId = 0;
    @NotNull EntityRef<AnotherSimpleEntity,Integer> anotherSimpleEntity = new EntityRef<>(ANOTHER_SIMPLE_ENTITY, AnotherSimpleEntity::getSimpleEntities);
    int seqId = 0;
    @NotNull String simpleEntityName = "";
    @NotNull EntityRef<SimpleEntity,String> simpleEntity = new EntityRef<>(SIMPLE_ENTITY);
    @NotNull DateTime updateTime = DateTime.EPOCH;

    //~ Methods ..................................................................................................................

    /** Returns the Another Simple Entity Id. */
    public int getAnotherSimpleEntityId() { return this.anotherSimpleEntityId; }

    /** Returns the Another Simple Entity. */
    @NotNull public AnotherSimpleEntity getAnotherSimpleEntity() {
        return anotherSimpleEntity.solveOrFail(this.anotherSimpleEntityId);
    }

    /** Returns the Seq Id. */
    public int getSeqId() { return this.seqId; }

    @Override @NotNull public EntityRef<AnotherSimpleEntity,Integer> parent() { return anotherSimpleEntity; }

    @Override @NotNull public InnerEntitySeq<SimpleEntities> siblings() { return getAnotherSimpleEntity().getSimpleEntities(); }

    @Override public int seqId() { return getSeqId(); }

    /** Returns the Simple Entity Name. */
    @NotNull public String getSimpleEntityName() { return this.simpleEntityName; }

    /** Returns the Simple Entity. */
    @NotNull public SimpleEntity getSimpleEntity() { return simpleEntity.solveOrFail(this.simpleEntityName); }

    /** Sets the value of the Simple Entity Name. */
    @NotNull public SimpleEntities setSimpleEntityName(@NotNull String simpleEntityName) {
        simpleEntity.invalidate();
        this.simpleEntityName = simpleEntityName;
        return (SimpleEntities) this;
    }

    /** Sets the value of the Simple Entity. */
    @NotNull public SimpleEntities setSimpleEntity(@NotNull SimpleEntity simpleEntity) {
        this.simpleEntity.set(simpleEntity);
        this.simpleEntityName = simpleEntity.getName();
        return (SimpleEntities) this;
    }

    /** Returns the Update Time. */
    @Override @NotNull public DateTime getUpdateTime() { return this.updateTime; }

    @NotNull private static EntityTable<SimpleEntities,Tuple<Integer,Integer>> myEntityTable() { return EntityTable.forTable(SIMPLE_ENTITIES); }

    @NotNull public EntityTable<SimpleEntities,Tuple<Integer,Integer>> et() { return myEntityTable(); }

    @Override @NotNull public DbTable<SimpleEntities,Tuple<Integer,Integer>> table() { return SIMPLE_ENTITIES; }

    /** 
     * Try to finds an Object of type 'SimpleEntities' in the database.
     * Identified by the primary key.
     * Returns <code>null</code> if is not present
     */
    @Nullable public static SimpleEntities find(@NotNull Tuple<Integer,Integer> key) { return myEntityTable().find(key); }

    /** 
     * Try to finds an Object of type 'SimpleEntities' in the database.
     * Identified by the primary key.
     * Throws EntityNotFoundException if is not present
     */
    @NotNull public static SimpleEntities findOrFail(@NotNull Tuple<Integer,Integer> key) { return myEntityTable().findOrFail(key); }

    /** 
     * Try to finds an Object of type 'SimpleEntities' in the database.
     * Identified by the primary key.
     * Ignoring caches and accessing the database
     * Returns <code>null</code> if is not present
     */
    @Nullable public static SimpleEntities findPersisted(@NotNull Tuple<Integer,Integer> key) { return myEntityTable().findPersisted(key); }

    /** 
     * Try to finds an Object of type 'SimpleEntities' in the database.
     * Identified by the primary key.
     * Ignoring caches and accessing the database
     * Throws EntityNotFoundException if is not present
     */
    @NotNull public static SimpleEntities findPersistedOrFail(@NotNull Tuple<Integer,Integer> key) { return myEntityTable().findPersistedOrFail(key); }

    /** 
     * Try to finds an Object of type 'SimpleEntities' in the database.
     * Identified by the primary key.
     * Returns <code>null</code> if is not present
     */
    @Nullable public static SimpleEntities find(@NotNull String key) { return myEntityTable().findByString(key); }

    /** 
     * Try to finds an Object of type 'SimpleEntities' in the database.
     * Identified by the primary key.
     * Returns <code>null</code> if is not present
     */
    @Nullable public static SimpleEntities find(int anotherSimpleEntityId, int seqId) { return find(Tuple.tuple2(anotherSimpleEntityId, seqId)); }

    /** 
     * Try to finds an Object of type 'SimpleEntities' in the database.
     * Identified by the primary key.
     * Returns <code>null</code> if is not present
     * Based on String key for Entities
     */
    @Nullable public static SimpleEntities find(@NotNull String anotherSimpleEntity, int seqId) { return find(Conversions.toInt(anotherSimpleEntity), seqId); }

    /** 
     * Try to finds an Object of type 'SimpleEntities' in the database.
     * That verifies the specified condition.
     * Returns <code>null</code> if is not present
     */
    @Nullable public static SimpleEntities findWhere(@NotNull Criteria... condition) { return selectFrom(SIMPLE_ENTITIES).where(condition).get(); }

    /** Create a selectFrom(SIMPLE_ENTITIES). */
    @NotNull public static Select<SimpleEntities> list() { return selectFrom(SIMPLE_ENTITIES); }

    /** Performs the given action for each SimpleEntities */
    public static void forEach(@Nullable Consumer<SimpleEntities> consumer) { selectFrom(SIMPLE_ENTITIES).forEach(consumer); }

    /** List instances of 'SimpleEntities' with the specified keys. */
    @NotNull public static ImmutableList<SimpleEntities> list(@Nullable Set<Tuple<Integer,Integer>> keys) { return myEntityTable().list(keys); }

    /** List instances of 'SimpleEntities' with the specified keys. */
    @NotNull public static ImmutableList<SimpleEntities> list(@Nullable Iterable<String> keys) { return myEntityTable().listFromStringKeys(keys); }

    /** List the instances of 'SimpleEntities' that verify the specified condition. */
    @NotNull public static Select<SimpleEntities> listWhere(@NotNull Criteria condition) { return selectFrom(SIMPLE_ENTITIES).where(condition); }

    @Override @NotNull public final SimpleEntities update() { return InnerInstance.super.update(); }

    @Override @NotNull public final SimpleEntities insert() { return InnerInstance.super.insert(); }

    /** Register a Listener */
    public static void addListener(@NotNull EntityListenerType listenerType, @NotNull EntityListener<SimpleEntities> listener) { myEntityTable().addListener(listenerType, listener); }

    /** Remove a Listener */
    public static void removeListener(@NotNull EntityListenerType listenerType, @NotNull EntityListener<SimpleEntities> listener) { myEntityTable().removeListener(listenerType, listener); }

    @NotNull public String keyAsString() { return anotherSimpleEntityId + ":" + seqId; }

    @NotNull public Tuple<Integer,Integer> keyObject() { return Tuple.tuple2(anotherSimpleEntityId, seqId); }

    @Override @NotNull public final Seq<String> describe() { return formatList(getAnotherSimpleEntity(), getSeqId()); }

    @Override @NotNull public String toString() { return "" + getAnotherSimpleEntity() + " " + getSeqId(); }

    /** Gets mapper for SQL statements */
    @NotNull public static RowMapper<SimpleEntities> rowMapper() { return SIMPLE_ENTITIES.metadata().getRowMapper(); }

    @Override public void invalidate() {
        simpleEntity.invalidate();
        anotherSimpleEntity.invalidate();
    }

}
