package tekgenesis.showcase.g;

import tekgenesis.showcase.AnotherSimpleEntity;
import java.util.function.Consumer;
import tekgenesis.persistence.Criteria;
import tekgenesis.common.core.DateTime;
import tekgenesis.persistence.DbTable;
import tekgenesis.persistence.EntityInstanceImpl;
import tekgenesis.persistence.EntityListener;
import tekgenesis.persistence.EntityListenerType;
import tekgenesis.persistence.EntityTable;
import java.util.EnumSet;
import tekgenesis.common.collections.ImmutableList;
import tekgenesis.persistence.InnerEntitySeq;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import tekgenesis.showcase.Options;
import tekgenesis.persistence.PersistableInstance;
import tekgenesis.database.RowMapper;
import tekgenesis.persistence.Select;
import tekgenesis.common.collections.Seq;
import java.util.Set;
import tekgenesis.showcase.SimpleEntities;
import tekgenesis.common.core.Strings;
import static tekgenesis.showcase.g.AnotherSimpleEntityTable.ANOTHER_SIMPLE_ENTITY;
import static tekgenesis.showcase.g.SimpleEntitiesTable.SIMPLE_ENTITIES;
import static tekgenesis.persistence.EntitySeq.createInnerEntitySeq;
import static tekgenesis.common.util.Conversions.formatList;
import static tekgenesis.persistence.Sql.selectFrom;

/** 
 * Generated base class for entity: AnotherSimpleEntity.
 * Don't modify this as this is an auto generated class that's gets generated
 * every time the meta model file is modified. Use subclass instead.
 */
@SuppressWarnings({"DuplicateStringLiteralInspection", "WeakerAccess", "FieldMayBeFinal", "FieldCanBeLocal", "ConstantConditions", "ClassWithTooManyMethods", "ClassWithTooManyFields", "MagicNumber", "MethodOverridesStaticMethodOfSuperclass", "ParameterHidesMemberVariable", "FieldNameHidesFieldInSuperclass", "OverlyComplexClass", "RedundantCast", "UnusedReturnValue"})
public abstract class AnotherSimpleEntityBase
    extends EntityInstanceImpl<AnotherSimpleEntity,Integer>
    implements PersistableInstance<AnotherSimpleEntity,Integer>
{

    //~ Fields ...................................................................................................................

    private int id = EntityTable.DEFAULT_EMPTY_KEY;
    @NotNull String name = "";
    @NotNull EnumSet<Options> options = EnumSet.noneOf(Options.class);
    @NotNull private InnerEntitySeq<SimpleEntities> simpleEntities = createInnerEntitySeq(SIMPLE_ENTITIES, (AnotherSimpleEntity) this, c -> ((SimpleEntitiesBase)c).anotherSimpleEntity);
    @NotNull DateTime updateTime = DateTime.EPOCH;

    //~ Methods ..................................................................................................................

    /** Returns the Id. */
    public int getId() { return this.id; }

    @Override public boolean hasEmptyKey() { return id == EntityTable.DEFAULT_EMPTY_KEY; }

    /** Returns the Name. */
    @NotNull public String getName() { return this.name; }

    /** Sets the value of the Name. */
    @NotNull public AnotherSimpleEntity setName(@NotNull String name) {
        markAsModified();
        this.name = Strings.truncate(name, 30);
        return (AnotherSimpleEntity) this;
    }

    /** Returns the Options. */
    @NotNull public EnumSet<Options> getOptions() { return this.options; }

    /** Sets the value of the Options. */
    @NotNull public AnotherSimpleEntity setOptions(@NotNull EnumSet<Options> options) {
        markAsModified();
        this.options = options;
        return (AnotherSimpleEntity) this;
    }

    /** Returns the Simple Entities. */
    @NotNull public InnerEntitySeq<SimpleEntities> getSimpleEntities() { return simpleEntities; }

    /** Returns the Update Time. */
    @Override @NotNull public DateTime getUpdateTime() { return this.updateTime; }

    /** Creates a new {@link AnotherSimpleEntity} instance. */
    @NotNull public static AnotherSimpleEntity create() { return new AnotherSimpleEntity(); }

    @NotNull private static EntityTable<AnotherSimpleEntity,Integer> myEntityTable() { return EntityTable.forTable(ANOTHER_SIMPLE_ENTITY); }

    @NotNull public EntityTable<AnotherSimpleEntity,Integer> et() { return myEntityTable(); }

    @Override @NotNull public DbTable<AnotherSimpleEntity,Integer> table() { return ANOTHER_SIMPLE_ENTITY; }

    /** 
     * Try to finds an Object of type 'AnotherSimpleEntity' in the database.
     * Identified by the primary key.
     * Returns <code>null</code> if is not present
     */
    @Nullable public static AnotherSimpleEntity find(int id) { return myEntityTable().find(id); }

    /** 
     * Try to finds an Object of type 'AnotherSimpleEntity' in the database.
     * Identified by the primary key.
     * Throws EntityNotFoundException if is not present
     */
    @NotNull public static AnotherSimpleEntity findOrFail(int id) { return myEntityTable().findOrFail(id); }

    /** 
     * Try to finds an Object of type 'AnotherSimpleEntity' in the database.
     * Identified by the primary key.
     * Ignoring caches and accessing the database
     * Returns <code>null</code> if is not present
     */
    @Nullable public static AnotherSimpleEntity findPersisted(int id) { return myEntityTable().findPersisted(id); }

    /** 
     * Try to finds an Object of type 'AnotherSimpleEntity' in the database.
     * Identified by the primary key.
     * Ignoring caches and accessing the database
     * Throws EntityNotFoundException if is not present
     */
    @NotNull public static AnotherSimpleEntity findPersistedOrFail(int id) { return myEntityTable().findPersistedOrFail(id); }

    /** 
     * Try to finds an Object of type 'AnotherSimpleEntity' in the database.
     * Identified by the primary key.
     * Returns <code>null</code> if is not present
     */
    @Nullable public static AnotherSimpleEntity find(@NotNull String key) { return myEntityTable().findByString(key); }

    /** 
     * Try to finds an Object of type 'AnotherSimpleEntity' in the database.
     * That verifies the specified condition.
     * Returns <code>null</code> if is not present
     */
    @Nullable public static AnotherSimpleEntity findWhere(@NotNull Criteria... condition) {
        return selectFrom(ANOTHER_SIMPLE_ENTITY).where(condition).get();
    }

    /** Create a selectFrom(ANOTHER_SIMPLE_ENTITY). */
    @NotNull public static Select<AnotherSimpleEntity> list() { return selectFrom(ANOTHER_SIMPLE_ENTITY); }

    /** Performs the given action for each AnotherSimpleEntity */
    public static void forEach(@Nullable Consumer<AnotherSimpleEntity> consumer) { selectFrom(ANOTHER_SIMPLE_ENTITY).forEach(consumer); }

    /** List instances of 'AnotherSimpleEntity' with the specified keys. */
    @NotNull public static ImmutableList<AnotherSimpleEntity> list(@Nullable Set<Integer> keys) { return myEntityTable().list(keys); }

    /** List instances of 'AnotherSimpleEntity' with the specified keys. */
    @NotNull public static ImmutableList<AnotherSimpleEntity> list(@Nullable Iterable<String> keys) { return myEntityTable().listFromStringKeys(keys); }

    /** List the instances of 'AnotherSimpleEntity' that verify the specified condition. */
    @NotNull public static Select<AnotherSimpleEntity> listWhere(@NotNull Criteria condition) { return selectFrom(ANOTHER_SIMPLE_ENTITY).where(condition); }

    /** Insert specifying the primary key */
    public void insert(int key) {
        this.id = key;
        myEntityTable().insertDoNotGenerate((AnotherSimpleEntity) this);
    }

    /** Register a Listener */
    public static void addListener(@NotNull EntityListenerType listenerType, @NotNull EntityListener<AnotherSimpleEntity> listener) { myEntityTable().addListener(listenerType, listener); }

    /** Remove a Listener */
    public static void removeListener(@NotNull EntityListenerType listenerType, @NotNull EntityListener<AnotherSimpleEntity> listener) { myEntityTable().removeListener(listenerType, listener); }

    @NotNull public String keyAsString() { return String.valueOf(id); }

    @NotNull public Integer keyObject() { return id; }

    @Override @NotNull public final Seq<String> describe() { return formatList(getName()); }

    @Override @NotNull public String toString() { return "" + getName(); }

    /** Gets mapper for SQL statements */
    @NotNull public static RowMapper<AnotherSimpleEntity> rowMapper() { return ANOTHER_SIMPLE_ENTITY.metadata().getRowMapper(); }

    @Override public void invalidate() { simpleEntities.invalidate(); }

}
