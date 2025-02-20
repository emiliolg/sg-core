package tekgenesis.showcase.g;

import java.util.function.Consumer;
import tekgenesis.persistence.Criteria;
import tekgenesis.showcase.DateEntity;
import tekgenesis.common.core.DateOnly;
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
import java.util.Set;
import static tekgenesis.showcase.g.DateEntityTable.DATE_ENTITY;
import static tekgenesis.common.util.Conversions.formatList;
import static tekgenesis.persistence.Sql.selectFrom;

/** 
 * Generated base class for entity: DateEntity.
 * Don't modify this as this is an auto generated class that's gets generated
 * every time the meta model file is modified. Use subclass instead.
 */
@SuppressWarnings({"DuplicateStringLiteralInspection", "WeakerAccess", "FieldMayBeFinal", "FieldCanBeLocal", "ConstantConditions", "ClassWithTooManyMethods", "ClassWithTooManyFields", "MagicNumber", "MethodOverridesStaticMethodOfSuperclass", "ParameterHidesMemberVariable", "FieldNameHidesFieldInSuperclass", "OverlyComplexClass", "RedundantCast", "UnusedReturnValue"})
public abstract class DateEntityBase
    extends EntityInstanceImpl<DateEntity,Integer>
    implements PersistableInstance<DateEntity,Integer>
{

    //~ Fields ...................................................................................................................

    private int id = EntityTable.DEFAULT_EMPTY_KEY;
    @NotNull DateOnly date = DateOnly.EPOCH;
    @NotNull DateTime updateTime = DateTime.EPOCH;

    //~ Methods ..................................................................................................................

    /** Returns the Id. */
    public int getId() { return this.id; }

    @Override public boolean hasEmptyKey() { return id == EntityTable.DEFAULT_EMPTY_KEY; }

    /** Returns the Date. */
    @NotNull public DateOnly getDate() { return this.date; }

    /** Sets the value of the Date. */
    @NotNull public DateEntity setDate(@NotNull DateOnly date) {
        markAsModified();
        this.date = date;
        return (DateEntity) this;
    }

    /** Returns the Update Time. */
    @Override @NotNull public DateTime getUpdateTime() { return this.updateTime; }

    /** Creates a new {@link DateEntity} instance. */
    @NotNull public static DateEntity create() { return new DateEntity(); }

    @NotNull private static EntityTable<DateEntity,Integer> myEntityTable() { return EntityTable.forTable(DATE_ENTITY); }

    @NotNull public EntityTable<DateEntity,Integer> et() { return myEntityTable(); }

    @Override @NotNull public DbTable<DateEntity,Integer> table() { return DATE_ENTITY; }

    /** 
     * Try to finds an Object of type 'DateEntity' in the database.
     * Identified by the primary key.
     * Returns <code>null</code> if is not present
     */
    @Nullable public static DateEntity find(int id) { return myEntityTable().find(id); }

    /** 
     * Try to finds an Object of type 'DateEntity' in the database.
     * Identified by the primary key.
     * Throws EntityNotFoundException if is not present
     */
    @NotNull public static DateEntity findOrFail(int id) { return myEntityTable().findOrFail(id); }

    /** 
     * Try to finds an Object of type 'DateEntity' in the database.
     * Identified by the primary key.
     * Ignoring caches and accessing the database
     * Returns <code>null</code> if is not present
     */
    @Nullable public static DateEntity findPersisted(int id) { return myEntityTable().findPersisted(id); }

    /** 
     * Try to finds an Object of type 'DateEntity' in the database.
     * Identified by the primary key.
     * Ignoring caches and accessing the database
     * Throws EntityNotFoundException if is not present
     */
    @NotNull public static DateEntity findPersistedOrFail(int id) { return myEntityTable().findPersistedOrFail(id); }

    /** 
     * Try to finds an Object of type 'DateEntity' in the database.
     * Identified by the primary key.
     * Returns <code>null</code> if is not present
     */
    @Nullable public static DateEntity find(@NotNull String key) { return myEntityTable().findByString(key); }

    /** 
     * Try to finds an Object of type 'DateEntity' in the database.
     * That verifies the specified condition.
     * Returns <code>null</code> if is not present
     */
    @Nullable public static DateEntity findWhere(@NotNull Criteria... condition) { return selectFrom(DATE_ENTITY).where(condition).get(); }

    /** Create a selectFrom(DATE_ENTITY). */
    @NotNull public static Select<DateEntity> list() { return selectFrom(DATE_ENTITY); }

    /** Performs the given action for each DateEntity */
    public static void forEach(@Nullable Consumer<DateEntity> consumer) { selectFrom(DATE_ENTITY).forEach(consumer); }

    /** List instances of 'DateEntity' with the specified keys. */
    @NotNull public static ImmutableList<DateEntity> list(@Nullable Set<Integer> keys) { return myEntityTable().list(keys); }

    /** List instances of 'DateEntity' with the specified keys. */
    @NotNull public static ImmutableList<DateEntity> list(@Nullable Iterable<String> keys) { return myEntityTable().listFromStringKeys(keys); }

    /** List the instances of 'DateEntity' that verify the specified condition. */
    @NotNull public static Select<DateEntity> listWhere(@NotNull Criteria condition) { return selectFrom(DATE_ENTITY).where(condition); }

    /** Insert specifying the primary key */
    public void insert(int key) {
        this.id = key;
        myEntityTable().insertDoNotGenerate((DateEntity) this);
    }

    /** Register a Listener */
    public static void addListener(@NotNull EntityListenerType listenerType, @NotNull EntityListener<DateEntity> listener) { myEntityTable().addListener(listenerType, listener); }

    /** Remove a Listener */
    public static void removeListener(@NotNull EntityListenerType listenerType, @NotNull EntityListener<DateEntity> listener) { myEntityTable().removeListener(listenerType, listener); }

    @NotNull public String keyAsString() { return String.valueOf(id); }

    @NotNull public Integer keyObject() { return id; }

    @Override @NotNull public final Seq<String> describe() { return formatList(getId()); }

    @Override @NotNull public String toString() { return "" + getId(); }

    /** Gets mapper for SQL statements */
    @NotNull public static RowMapper<DateEntity> rowMapper() { return DATE_ENTITY.metadata().getRowMapper(); }

}
