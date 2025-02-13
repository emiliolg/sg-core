package tekgenesis.showcase.g;

import tekgenesis.showcase.AnotherDeprecableEntity;
import java.util.function.Consumer;
import tekgenesis.persistence.Criteria;
import tekgenesis.common.core.DateTime;
import tekgenesis.persistence.DbTable;
import tekgenesis.showcase.DeprecableEntity;
import tekgenesis.persistence.DeprecableInstance;
import tekgenesis.persistence.EntityInstanceImpl;
import tekgenesis.persistence.EntityListener;
import tekgenesis.persistence.EntityListenerType;
import tekgenesis.persistence.EntityRef;
import tekgenesis.persistence.EntityTable;
import tekgenesis.common.collections.ImmutableList;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import tekgenesis.persistence.PersistableInstance;
import tekgenesis.database.RowMapper;
import tekgenesis.persistence.Select;
import tekgenesis.common.collections.Seq;
import java.util.Set;
import tekgenesis.showcase.SimpleEntity;
import tekgenesis.common.core.Strings;
import static tekgenesis.showcase.g.AnotherDeprecableEntityTable.ANOTHER_DEPRECABLE_ENTITY;
import static tekgenesis.showcase.g.DeprecableEntityTable.DEPRECABLE_ENTITY;
import static tekgenesis.showcase.g.SimpleEntityTable.SIMPLE_ENTITY;
import static tekgenesis.common.util.Conversions.formatList;
import static tekgenesis.persistence.Sql.selectFrom;

/** 
 * Generated base class for entity: DeprecableEntity.
 * Don't modify this as this is an auto generated class that's gets generated
 * every time the meta model file is modified. Use subclass instead.
 */
@SuppressWarnings({"DuplicateStringLiteralInspection", "WeakerAccess", "FieldMayBeFinal", "FieldCanBeLocal", "ConstantConditions", "ClassWithTooManyMethods", "ClassWithTooManyFields", "MagicNumber", "MethodOverridesStaticMethodOfSuperclass", "ParameterHidesMemberVariable", "FieldNameHidesFieldInSuperclass", "OverlyComplexClass", "RedundantCast", "UnusedReturnValue"})
public abstract class DeprecableEntityBase
    extends EntityInstanceImpl<DeprecableEntity,Integer>
    implements PersistableInstance<DeprecableEntity,Integer>, DeprecableInstance<DeprecableEntity,Integer>
{

    //~ Fields ...................................................................................................................

    private int id = EntityTable.DEFAULT_EMPTY_KEY;
    @NotNull String name = "";
    @NotNull String simpleEntityName = "";
    @NotNull EntityRef<SimpleEntity,String> simpleEntity = new EntityRef<>(SIMPLE_ENTITY);
    int anotherDeprecableEntityId = 0;
    @NotNull EntityRef<AnotherDeprecableEntity,Integer> anotherDeprecableEntity = new EntityRef<>(ANOTHER_DEPRECABLE_ENTITY);
    @NotNull DateTime updateTime = DateTime.EPOCH;
    @Nullable DateTime deprecationTime = null;
    @Nullable String deprecationUser = null;

    //~ Methods ..................................................................................................................

    /** Returns the Id. */
    public int getId() { return this.id; }

    @Override public boolean hasEmptyKey() { return id == EntityTable.DEFAULT_EMPTY_KEY; }

    /** Returns the Name. */
    @NotNull public String getName() { return this.name; }

    /** Sets the value of the Name. */
    @NotNull public DeprecableEntity setName(@NotNull String name) {
        markAsModified();
        this.name = Strings.truncate(name, 20);
        return (DeprecableEntity) this;
    }

    /** Returns the Simple Entity Name. */
    @NotNull public String getSimpleEntityName() { return this.simpleEntityName; }

    /** Returns the Simple Entity. */
    @NotNull public SimpleEntity getSimpleEntity() { return simpleEntity.solveOrFail(this.simpleEntityName); }

    /** Sets the value of the Simple Entity Name. */
    @NotNull public DeprecableEntity setSimpleEntityName(@NotNull String simpleEntityName) {
        simpleEntity.invalidate();
        this.simpleEntityName = simpleEntityName;
        return (DeprecableEntity) this;
    }

    /** Sets the value of the Simple Entity. */
    @NotNull public DeprecableEntity setSimpleEntity(@NotNull SimpleEntity simpleEntity) {
        this.simpleEntity.set(simpleEntity);
        this.simpleEntityName = simpleEntity.getName();
        return (DeprecableEntity) this;
    }

    /** Returns the Another Deprecable Entity Id. */
    public int getAnotherDeprecableEntityId() { return this.anotherDeprecableEntityId; }

    /** Returns the Another Deprecable Entity. */
    @NotNull public AnotherDeprecableEntity getAnotherDeprecableEntity() {
        return anotherDeprecableEntity.solveOrFail(this.anotherDeprecableEntityId);
    }

    /** Sets the value of the Another Deprecable Entity Id. */
    @NotNull public DeprecableEntity setAnotherDeprecableEntityId(int anotherDeprecableEntityId) {
        anotherDeprecableEntity.invalidate();
        this.anotherDeprecableEntityId = anotherDeprecableEntityId;
        return (DeprecableEntity) this;
    }

    /** Sets the value of the Another Deprecable Entity. */
    @NotNull public DeprecableEntity setAnotherDeprecableEntity(@NotNull AnotherDeprecableEntity anotherDeprecableEntity) {
        this.anotherDeprecableEntity.set(anotherDeprecableEntity);
        this.anotherDeprecableEntityId = anotherDeprecableEntity.getId();
        return (DeprecableEntity) this;
    }

    /** Returns the Update Time. */
    @Override @NotNull public DateTime getUpdateTime() { return this.updateTime; }

    /** Returns the Deprecation Time. */
    @Nullable public DateTime getDeprecationTime() { return this.deprecationTime; }

    /** Returns the Deprecation User. */
    @Nullable public String getDeprecationUser() { return this.deprecationUser; }

    /** Creates a new {@link DeprecableEntity} instance. */
    @NotNull public static DeprecableEntity create() { return new DeprecableEntity(); }

    @NotNull private static EntityTable<DeprecableEntity,Integer> myEntityTable() { return EntityTable.forTable(DEPRECABLE_ENTITY); }

    @NotNull public EntityTable<DeprecableEntity,Integer> et() { return myEntityTable(); }

    @Override @NotNull public DbTable<DeprecableEntity,Integer> table() { return DEPRECABLE_ENTITY; }

    /** 
     * Try to finds an Object of type 'DeprecableEntity' in the database.
     * Identified by the primary key.
     * Returns <code>null</code> if is not present
     */
    @Nullable public static DeprecableEntity find(int id) { return myEntityTable().find(id); }

    /** 
     * Try to finds an Object of type 'DeprecableEntity' in the database.
     * Identified by the primary key.
     * Throws EntityNotFoundException if is not present
     */
    @NotNull public static DeprecableEntity findOrFail(int id) { return myEntityTable().findOrFail(id); }

    /** 
     * Try to finds an Object of type 'DeprecableEntity' in the database.
     * Identified by the primary key.
     * Ignoring caches and accessing the database
     * Returns <code>null</code> if is not present
     */
    @Nullable public static DeprecableEntity findPersisted(int id) { return myEntityTable().findPersisted(id); }

    /** 
     * Try to finds an Object of type 'DeprecableEntity' in the database.
     * Identified by the primary key.
     * Ignoring caches and accessing the database
     * Throws EntityNotFoundException if is not present
     */
    @NotNull public static DeprecableEntity findPersistedOrFail(int id) { return myEntityTable().findPersistedOrFail(id); }

    /** 
     * Try to finds an Object of type 'DeprecableEntity' in the database.
     * Identified by the primary key.
     * Returns <code>null</code> if is not present
     */
    @Nullable public static DeprecableEntity find(@NotNull String key) { return myEntityTable().findByString(key); }

    /** 
     * Try to finds an Object of type 'DeprecableEntity' in the database.
     * That verifies the specified condition.
     * Returns <code>null</code> if is not present
     */
    @Nullable public static DeprecableEntity findWhere(@NotNull Criteria... condition) { return selectFrom(DEPRECABLE_ENTITY).where(condition).get(); }

    /** Create a selectFrom(DEPRECABLE_ENTITY). */
    @NotNull public static Select<DeprecableEntity> list() { return selectFrom(DEPRECABLE_ENTITY); }

    /** Performs the given action for each DeprecableEntity */
    public static void forEach(@Nullable Consumer<DeprecableEntity> consumer) { selectFrom(DEPRECABLE_ENTITY).forEach(consumer); }

    /** List instances of 'DeprecableEntity' with the specified keys. */
    @NotNull public static ImmutableList<DeprecableEntity> list(@Nullable Set<Integer> keys) { return myEntityTable().list(keys); }

    /** List instances of 'DeprecableEntity' with the specified keys. */
    @NotNull public static ImmutableList<DeprecableEntity> list(@Nullable Iterable<String> keys) { return myEntityTable().listFromStringKeys(keys); }

    /** List the instances of 'DeprecableEntity' that verify the specified condition. */
    @NotNull public static Select<DeprecableEntity> listWhere(@NotNull Criteria condition) { return selectFrom(DEPRECABLE_ENTITY).where(condition); }

    /** Insert specifying the primary key */
    public void insert(int key) {
        this.id = key;
        myEntityTable().insertDoNotGenerate((DeprecableEntity) this);
    }

    /** Register a Listener */
    public static void addListener(@NotNull EntityListenerType listenerType, @NotNull EntityListener<DeprecableEntity> listener) { myEntityTable().addListener(listenerType, listener); }

    /** Remove a Listener */
    public static void removeListener(@NotNull EntityListenerType listenerType, @NotNull EntityListener<DeprecableEntity> listener) { myEntityTable().removeListener(listenerType, listener); }

    @NotNull public String keyAsString() { return String.valueOf(id); }

    @NotNull public Integer keyObject() { return id; }

    @Override @NotNull public final Seq<String> describe() { return formatList(getName()); }

    @Override @NotNull public String toString() { return "" + getName(); }

    /** Gets mapper for SQL statements */
    @NotNull public static RowMapper<DeprecableEntity> rowMapper() { return DEPRECABLE_ENTITY.metadata().getRowMapper(); }

    @Override public void invalidate() {
        simpleEntity.invalidate();
        anotherDeprecableEntity.invalidate();
    }

}
