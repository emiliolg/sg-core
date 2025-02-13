package tekgenesis.showcase.g;

import tekgenesis.showcase.AnotherDeprecableEntity;
import java.util.function.Consumer;
import tekgenesis.persistence.Criteria;
import tekgenesis.common.core.DateTime;
import tekgenesis.persistence.DbTable;
import tekgenesis.persistence.DeprecableInstance;
import tekgenesis.persistence.EntityInstanceImpl;
import tekgenesis.persistence.EntityListener;
import tekgenesis.persistence.EntityListenerType;
import tekgenesis.persistence.EntityTable;
import tekgenesis.common.collections.ImmutableList;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import tekgenesis.persistence.PersistableInstance;
import tekgenesis.database.RowMapper;
import tekgenesis.persistence.Select;
import tekgenesis.common.collections.Seq;
import java.util.Set;
import tekgenesis.common.core.Strings;
import static tekgenesis.showcase.g.AnotherDeprecableEntityTable.ANOTHER_DEPRECABLE_ENTITY;
import static tekgenesis.common.util.Conversions.formatList;
import static tekgenesis.persistence.Sql.selectFrom;

/** 
 * Generated base class for entity: AnotherDeprecableEntity.
 * Don't modify this as this is an auto generated class that's gets generated
 * every time the meta model file is modified. Use subclass instead.
 */
@SuppressWarnings({"DuplicateStringLiteralInspection", "WeakerAccess", "FieldMayBeFinal", "FieldCanBeLocal", "ConstantConditions", "ClassWithTooManyMethods", "ClassWithTooManyFields", "MagicNumber", "MethodOverridesStaticMethodOfSuperclass", "ParameterHidesMemberVariable", "FieldNameHidesFieldInSuperclass", "OverlyComplexClass", "RedundantCast", "UnusedReturnValue"})
public abstract class AnotherDeprecableEntityBase
    extends EntityInstanceImpl<AnotherDeprecableEntity,Integer>
    implements PersistableInstance<AnotherDeprecableEntity,Integer>, DeprecableInstance<AnotherDeprecableEntity,Integer>
{

    //~ Fields ...................................................................................................................

    private int id = EntityTable.DEFAULT_EMPTY_KEY;
    @NotNull String name = "";
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
    @NotNull public AnotherDeprecableEntity setName(@NotNull String name) {
        markAsModified();
        this.name = Strings.truncate(name, 20);
        return (AnotherDeprecableEntity) this;
    }

    /** Returns the Update Time. */
    @Override @NotNull public DateTime getUpdateTime() { return this.updateTime; }

    /** Returns the Deprecation Time. */
    @Nullable public DateTime getDeprecationTime() { return this.deprecationTime; }

    /** Returns the Deprecation User. */
    @Nullable public String getDeprecationUser() { return this.deprecationUser; }

    /** Creates a new {@link AnotherDeprecableEntity} instance. */
    @NotNull public static AnotherDeprecableEntity create() { return new AnotherDeprecableEntity(); }

    @NotNull private static EntityTable<AnotherDeprecableEntity,Integer> myEntityTable() { return EntityTable.forTable(ANOTHER_DEPRECABLE_ENTITY); }

    @NotNull public EntityTable<AnotherDeprecableEntity,Integer> et() { return myEntityTable(); }

    @Override @NotNull public DbTable<AnotherDeprecableEntity,Integer> table() { return ANOTHER_DEPRECABLE_ENTITY; }

    /** 
     * Try to finds an Object of type 'AnotherDeprecableEntity' in the database.
     * Identified by the primary key.
     * Returns <code>null</code> if is not present
     */
    @Nullable public static AnotherDeprecableEntity find(int id) { return myEntityTable().find(id); }

    /** 
     * Try to finds an Object of type 'AnotherDeprecableEntity' in the database.
     * Identified by the primary key.
     * Throws EntityNotFoundException if is not present
     */
    @NotNull public static AnotherDeprecableEntity findOrFail(int id) { return myEntityTable().findOrFail(id); }

    /** 
     * Try to finds an Object of type 'AnotherDeprecableEntity' in the database.
     * Identified by the primary key.
     * Ignoring caches and accessing the database
     * Returns <code>null</code> if is not present
     */
    @Nullable public static AnotherDeprecableEntity findPersisted(int id) { return myEntityTable().findPersisted(id); }

    /** 
     * Try to finds an Object of type 'AnotherDeprecableEntity' in the database.
     * Identified by the primary key.
     * Ignoring caches and accessing the database
     * Throws EntityNotFoundException if is not present
     */
    @NotNull public static AnotherDeprecableEntity findPersistedOrFail(int id) { return myEntityTable().findPersistedOrFail(id); }

    /** 
     * Try to finds an Object of type 'AnotherDeprecableEntity' in the database.
     * Identified by the primary key.
     * Returns <code>null</code> if is not present
     */
    @Nullable public static AnotherDeprecableEntity find(@NotNull String key) { return myEntityTable().findByString(key); }

    /** 
     * Try to finds an Object of type 'AnotherDeprecableEntity' in the database.
     * That verifies the specified condition.
     * Returns <code>null</code> if is not present
     */
    @Nullable public static AnotherDeprecableEntity findWhere(@NotNull Criteria... condition) {
        return selectFrom(ANOTHER_DEPRECABLE_ENTITY).where(condition).get();
    }

    /** Create a selectFrom(ANOTHER_DEPRECABLE_ENTITY). */
    @NotNull public static Select<AnotherDeprecableEntity> list() { return selectFrom(ANOTHER_DEPRECABLE_ENTITY); }

    /** Performs the given action for each AnotherDeprecableEntity */
    public static void forEach(@Nullable Consumer<AnotherDeprecableEntity> consumer) { selectFrom(ANOTHER_DEPRECABLE_ENTITY).forEach(consumer); }

    /** List instances of 'AnotherDeprecableEntity' with the specified keys. */
    @NotNull public static ImmutableList<AnotherDeprecableEntity> list(@Nullable Set<Integer> keys) { return myEntityTable().list(keys); }

    /** List instances of 'AnotherDeprecableEntity' with the specified keys. */
    @NotNull public static ImmutableList<AnotherDeprecableEntity> list(@Nullable Iterable<String> keys) { return myEntityTable().listFromStringKeys(keys); }

    /** List the instances of 'AnotherDeprecableEntity' that verify the specified condition. */
    @NotNull public static Select<AnotherDeprecableEntity> listWhere(@NotNull Criteria condition) {
        return selectFrom(ANOTHER_DEPRECABLE_ENTITY).where(condition);
    }

    /** Insert specifying the primary key */
    public void insert(int key) {
        this.id = key;
        myEntityTable().insertDoNotGenerate((AnotherDeprecableEntity) this);
    }

    /** Register a Listener */
    public static void addListener(@NotNull EntityListenerType listenerType, @NotNull EntityListener<AnotherDeprecableEntity> listener) { myEntityTable().addListener(listenerType, listener); }

    /** Remove a Listener */
    public static void removeListener(@NotNull EntityListenerType listenerType, @NotNull EntityListener<AnotherDeprecableEntity> listener) { myEntityTable().removeListener(listenerType, listener); }

    @NotNull public String keyAsString() { return String.valueOf(id); }

    @NotNull public Integer keyObject() { return id; }

    @Override @NotNull public final Seq<String> describe() { return formatList(getName()); }

    @Override @NotNull public String toString() { return "" + getName(); }

    /** Gets mapper for SQL statements */
    @NotNull public static RowMapper<AnotherDeprecableEntity> rowMapper() { return ANOTHER_DEPRECABLE_ENTITY.metadata().getRowMapper(); }

}
