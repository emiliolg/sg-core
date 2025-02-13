package tekgenesis.sales.basic.g;

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
import tekgenesis.common.core.Reals;
import tekgenesis.database.RowMapper;
import tekgenesis.persistence.Select;
import tekgenesis.common.collections.Seq;
import java.util.Set;
import tekgenesis.sales.basic.Store;
import tekgenesis.common.core.Strings;
import static tekgenesis.sales.basic.g.StoreTable.STORE;
import static tekgenesis.common.util.Conversions.formatList;
import static tekgenesis.persistence.Sql.selectFrom;

/** 
 * Generated base class for entity: Store.
 * Don't modify this as this is an auto generated class that's gets generated
 * every time the meta model file is modified. Use subclass instead.
 */
@SuppressWarnings({"DuplicateStringLiteralInspection", "WeakerAccess", "FieldMayBeFinal", "FieldCanBeLocal", "ConstantConditions", "ClassWithTooManyMethods", "ClassWithTooManyFields", "MagicNumber", "MethodOverridesStaticMethodOfSuperclass", "ParameterHidesMemberVariable", "FieldNameHidesFieldInSuperclass", "OverlyComplexClass", "RedundantCast", "UnusedReturnValue"})
public abstract class StoreBase
    extends EntityInstanceImpl<Store,Integer>
    implements PersistableInstance<Store,Integer>
{

    //~ Fields ...................................................................................................................

    private int id = EntityTable.DEFAULT_EMPTY_KEY;
    @NotNull String name = "";
    @NotNull String address = "";
    double lat = 0.0;
    double lng = 0.0;
    @NotNull DateTime updateTime = DateTime.EPOCH;

    //~ Methods ..................................................................................................................

    /** Returns the Id. */
    public int getId() { return this.id; }

    @Override public boolean hasEmptyKey() { return id == EntityTable.DEFAULT_EMPTY_KEY; }

    /** Returns the Name. */
    @NotNull public String getName() { return this.name; }

    /** Sets the value of the Name. */
    @NotNull public Store setName(@NotNull String name) {
        markAsModified();
        this.name = Strings.truncate(name, 50);
        return (Store) this;
    }

    /** Returns the Address. */
    @NotNull public String getAddress() { return this.address; }

    /** Sets the value of the Address. */
    @NotNull public Store setAddress(@NotNull String address) {
        markAsModified();
        this.address = Strings.truncate(address, 150);
        return (Store) this;
    }

    /** Returns the Lat. */
    public double getLat() { return this.lat; }

    /** Sets the value of the Lat. */
    @NotNull public Store setLat(double lat) {
        markAsModified();
        this.lat = Reals.checkSigned("lat", lat, true);
        return (Store) this;
    }

    /** Returns the Lng. */
    public double getLng() { return this.lng; }

    /** Sets the value of the Lng. */
    @NotNull public Store setLng(double lng) {
        markAsModified();
        this.lng = Reals.checkSigned("lng", lng, true);
        return (Store) this;
    }

    /** Returns the Update Time. */
    @Override @NotNull public DateTime getUpdateTime() { return this.updateTime; }

    /** Creates a new {@link Store} instance. */
    @NotNull public static Store create() { return new Store(); }

    @NotNull private static EntityTable<Store,Integer> myEntityTable() { return EntityTable.forTable(STORE); }

    @NotNull public EntityTable<Store,Integer> et() { return myEntityTable(); }

    @Override @NotNull public DbTable<Store,Integer> table() { return STORE; }

    /** 
     * Try to finds an Object of type 'Store' in the database.
     * Identified by the primary key.
     * Returns <code>null</code> if is not present
     */
    @Nullable public static Store find(int id) { return myEntityTable().find(id); }

    /** 
     * Try to finds an Object of type 'Store' in the database.
     * Identified by the primary key.
     * Throws EntityNotFoundException if is not present
     */
    @NotNull public static Store findOrFail(int id) { return myEntityTable().findOrFail(id); }

    /** 
     * Try to finds an Object of type 'Store' in the database.
     * Identified by the primary key.
     * Ignoring caches and accessing the database
     * Returns <code>null</code> if is not present
     */
    @Nullable public static Store findPersisted(int id) { return myEntityTable().findPersisted(id); }

    /** 
     * Try to finds an Object of type 'Store' in the database.
     * Identified by the primary key.
     * Ignoring caches and accessing the database
     * Throws EntityNotFoundException if is not present
     */
    @NotNull public static Store findPersistedOrFail(int id) { return myEntityTable().findPersistedOrFail(id); }

    /** 
     * Try to finds an Object of type 'Store' in the database.
     * Identified by the primary key.
     * Returns <code>null</code> if is not present
     */
    @Nullable public static Store find(@NotNull String key) { return myEntityTable().findByString(key); }

    /** 
     * Try to finds an Object of type 'Store' in the database.
     * That verifies the specified condition.
     * Returns <code>null</code> if is not present
     */
    @Nullable public static Store findWhere(@NotNull Criteria... condition) { return selectFrom(STORE).where(condition).get(); }

    /** Create a selectFrom(STORE). */
    @NotNull public static Select<Store> list() { return selectFrom(STORE); }

    /** Performs the given action for each Store */
    public static void forEach(@Nullable Consumer<Store> consumer) { selectFrom(STORE).forEach(consumer); }

    /** List instances of 'Store' with the specified keys. */
    @NotNull public static ImmutableList<Store> list(@Nullable Set<Integer> keys) { return myEntityTable().list(keys); }

    /** List instances of 'Store' with the specified keys. */
    @NotNull public static ImmutableList<Store> list(@Nullable Iterable<String> keys) { return myEntityTable().listFromStringKeys(keys); }

    /** List the instances of 'Store' that verify the specified condition. */
    @NotNull public static Select<Store> listWhere(@NotNull Criteria condition) { return selectFrom(STORE).where(condition); }

    /** Insert specifying the primary key */
    public void insert(int key) {
        this.id = key;
        myEntityTable().insertDoNotGenerate((Store) this);
    }

    /** Register a Listener */
    public static void addListener(@NotNull EntityListenerType listenerType, @NotNull EntityListener<Store> listener) { myEntityTable().addListener(listenerType, listener); }

    /** Remove a Listener */
    public static void removeListener(@NotNull EntityListenerType listenerType, @NotNull EntityListener<Store> listener) { myEntityTable().removeListener(listenerType, listener); }

    @NotNull public String keyAsString() { return String.valueOf(id); }

    @NotNull public Integer keyObject() { return id; }

    @Override @NotNull public final Seq<String> describe() { return formatList(getName()); }

    @Override @NotNull public String toString() { return "" + getName(); }

    /** Gets mapper for SQL statements */
    @NotNull public static RowMapper<Store> rowMapper() { return STORE.metadata().getRowMapper(); }

}
