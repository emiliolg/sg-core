package tekgenesis.showcase.g;

import java.util.function.Consumer;
import tekgenesis.persistence.Criteria;
import tekgenesis.common.core.DateTime;
import tekgenesis.persistence.DbTable;
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
import tekgenesis.showcase.SimpleEntity;
import tekgenesis.common.core.Strings;
import static tekgenesis.showcase.g.SimpleEntityTable.SIMPLE_ENTITY;
import static tekgenesis.common.util.Conversions.formatList;
import static tekgenesis.persistence.Sql.selectFrom;

/** 
 * Generated base class for entity: SimpleEntity.
 * Don't modify this as this is an auto generated class that's gets generated
 * every time the meta model file is modified. Use subclass instead.
 */
@SuppressWarnings({"DuplicateStringLiteralInspection", "WeakerAccess", "FieldMayBeFinal", "FieldCanBeLocal", "ConstantConditions", "ClassWithTooManyMethods", "ClassWithTooManyFields", "MagicNumber", "MethodOverridesStaticMethodOfSuperclass", "ParameterHidesMemberVariable", "FieldNameHidesFieldInSuperclass", "OverlyComplexClass", "RedundantCast", "UnusedReturnValue"})
public abstract class SimpleEntityBase
    extends EntityInstanceImpl<SimpleEntity,String>
    implements PersistableInstance<SimpleEntity,String>
{

    //~ Fields ...................................................................................................................

    @NotNull String name = "";
    @NotNull String description = "";
    @NotNull DateTime updateTime = DateTime.EPOCH;

    //~ Methods ..................................................................................................................

    /** Returns the Name. */
    @NotNull public String getName() { return this.name; }

    /** Returns the Description. */
    @NotNull public String getDescription() { return this.description; }

    /** Sets the value of the Description. */
    @NotNull public SimpleEntity setDescription(@NotNull String description) {
        markAsModified();
        this.description = Strings.truncate(description, 30);
        return (SimpleEntity) this;
    }

    /** Returns the Update Time. */
    @Override @NotNull public DateTime getUpdateTime() { return this.updateTime; }

    /** Creates a new {@link SimpleEntity} instance. */
    @NotNull public static SimpleEntity create(@NotNull String name) {
        final SimpleEntity result = new SimpleEntity();
        ((SimpleEntityBase) result).name = Strings.truncate(name, 255);
        return result;
    }

    /** 
     * Find (or create if not present) a 'SimpleEntity' in the database.
     * Identified by the primary key.
     */
    @NotNull public static SimpleEntity findOrCreate(@NotNull String name) { return myEntityTable().findOrCreate(name); }

    @NotNull private static EntityTable<SimpleEntity,String> myEntityTable() { return EntityTable.forTable(SIMPLE_ENTITY); }

    @NotNull public EntityTable<SimpleEntity,String> et() { return myEntityTable(); }

    @Override @NotNull public DbTable<SimpleEntity,String> table() { return SIMPLE_ENTITY; }

    /** 
     * Try to finds an Object of type 'SimpleEntity' in the database.
     * Identified by the primary key.
     * Returns <code>null</code> if is not present
     */
    @Nullable public static SimpleEntity find(@NotNull String name) { return myEntityTable().find(name); }

    /** 
     * Try to finds an Object of type 'SimpleEntity' in the database.
     * Identified by the primary key.
     * Throws EntityNotFoundException if is not present
     */
    @NotNull public static SimpleEntity findOrFail(@NotNull String name) { return myEntityTable().findOrFail(name); }

    /** 
     * Try to finds an Object of type 'SimpleEntity' in the database.
     * Identified by the primary key.
     * Ignoring caches and accessing the database
     * Returns <code>null</code> if is not present
     */
    @Nullable public static SimpleEntity findPersisted(@NotNull String name) { return myEntityTable().findPersisted(name); }

    /** 
     * Try to finds an Object of type 'SimpleEntity' in the database.
     * Identified by the primary key.
     * Ignoring caches and accessing the database
     * Throws EntityNotFoundException if is not present
     */
    @NotNull public static SimpleEntity findPersistedOrFail(@NotNull String name) { return myEntityTable().findPersistedOrFail(name); }

    /** 
     * Try to finds an Object of type 'SimpleEntity' in the database.
     * That verifies the specified condition.
     * Returns <code>null</code> if is not present
     */
    @Nullable public static SimpleEntity findWhere(@NotNull Criteria... condition) { return selectFrom(SIMPLE_ENTITY).where(condition).get(); }

    /** Create a selectFrom(SIMPLE_ENTITY). */
    @NotNull public static Select<SimpleEntity> list() { return selectFrom(SIMPLE_ENTITY); }

    /** Performs the given action for each SimpleEntity */
    public static void forEach(@Nullable Consumer<SimpleEntity> consumer) { selectFrom(SIMPLE_ENTITY).forEach(consumer); }

    /** List instances of 'SimpleEntity' with the specified keys. */
    @NotNull public static ImmutableList<SimpleEntity> list(@Nullable Iterable<String> keys) { return myEntityTable().listFromStringKeys(keys); }

    /** List the instances of 'SimpleEntity' that verify the specified condition. */
    @NotNull public static Select<SimpleEntity> listWhere(@NotNull Criteria condition) { return selectFrom(SIMPLE_ENTITY).where(condition); }

    @Override @NotNull public final SimpleEntity update() { return PersistableInstance.super.update(); }

    @Override @NotNull public final SimpleEntity insert() { return PersistableInstance.super.insert(); }

    /** Register a Listener */
    public static void addListener(@NotNull EntityListenerType listenerType, @NotNull EntityListener<SimpleEntity> listener) { myEntityTable().addListener(listenerType, listener); }

    /** Remove a Listener */
    public static void removeListener(@NotNull EntityListenerType listenerType, @NotNull EntityListener<SimpleEntity> listener) { myEntityTable().removeListener(listenerType, listener); }

    @NotNull public String keyAsString() { return name; }

    @NotNull public String keyObject() { return name; }

    @Override @NotNull public final Seq<String> describe() { return formatList(getName(), getDescription()); }

    @Override @NotNull public String toString() { return "" + getName() + " " + getDescription(); }

    /** Gets mapper for SQL statements */
    @NotNull public static RowMapper<SimpleEntity> rowMapper() { return SIMPLE_ENTITY.metadata().getRowMapper(); }

}
