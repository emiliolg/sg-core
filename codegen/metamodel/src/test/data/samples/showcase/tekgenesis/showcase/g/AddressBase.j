package tekgenesis.showcase.g;

import tekgenesis.showcase.Address;
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
import java.util.Set;
import tekgenesis.common.core.Strings;
import static tekgenesis.showcase.g.AddressTable.ADDRESS;
import static tekgenesis.common.util.Conversions.formatList;
import static tekgenesis.persistence.Sql.selectFrom;

/** 
 * Generated base class for entity: Address.
 * Don't modify this as this is an auto generated class that's gets generated
 * every time the meta model file is modified. Use subclass instead.
 */
@SuppressWarnings({"DuplicateStringLiteralInspection", "WeakerAccess", "FieldMayBeFinal", "FieldCanBeLocal", "ConstantConditions", "ClassWithTooManyMethods", "ClassWithTooManyFields", "MagicNumber", "MethodOverridesStaticMethodOfSuperclass", "ParameterHidesMemberVariable", "FieldNameHidesFieldInSuperclass", "OverlyComplexClass", "RedundantCast", "UnusedReturnValue"})
public abstract class AddressBase
    extends EntityInstanceImpl<Address,Integer>
    implements PersistableInstance<Address,Integer>
{

    //~ Fields ...................................................................................................................

    private int id = EntityTable.DEFAULT_EMPTY_KEY;
    @NotNull String street = "";
    @NotNull String city = "";
    @Nullable String state = null;
    @Nullable String zip = null;
    @NotNull String country = "";
    @NotNull DateTime updateTime = DateTime.EPOCH;

    //~ Methods ..................................................................................................................

    /** Returns the Id. */
    public int getId() { return this.id; }

    @Override public boolean hasEmptyKey() { return id == EntityTable.DEFAULT_EMPTY_KEY; }

    /** Returns the Street. */
    @NotNull public String getStreet() { return this.street; }

    /** Sets the value of the Street. */
    @NotNull public Address setStreet(@NotNull String street) {
        markAsModified();
        this.street = Strings.truncate(street, 60);
        return (Address) this;
    }

    /** Returns the City. */
    @NotNull public String getCity() { return this.city; }

    /** Sets the value of the City. */
    @NotNull public Address setCity(@NotNull String city) {
        markAsModified();
        this.city = Strings.truncate(city, 40);
        return (Address) this;
    }

    /** Returns the State. */
    @Nullable public String getState() { return this.state; }

    /** Sets the value of the State. */
    @NotNull public Address setState(@Nullable String state) {
        markAsModified();
        this.state = Strings.truncate(state, 40);
        return (Address) this;
    }

    /** Returns the Zip. */
    @Nullable public String getZip() { return this.zip; }

    /** Sets the value of the Zip. */
    @NotNull public Address setZip(@Nullable String zip) {
        markAsModified();
        this.zip = Strings.truncate(zip, 10);
        return (Address) this;
    }

    /** Returns the Country. */
    @NotNull public String getCountry() { return this.country; }

    /** Sets the value of the Country. */
    @NotNull public Address setCountry(@NotNull String country) {
        markAsModified();
        this.country = Strings.truncate(country, 30);
        return (Address) this;
    }

    /** Returns the Update Time. */
    @Override @NotNull public DateTime getUpdateTime() { return this.updateTime; }

    /** Creates a new {@link Address} instance. */
    @NotNull public static Address create() { return new Address(); }

    @NotNull private static EntityTable<Address,Integer> myEntityTable() { return EntityTable.forTable(ADDRESS); }

    @NotNull public EntityTable<Address,Integer> et() { return myEntityTable(); }

    @Override @NotNull public DbTable<Address,Integer> table() { return ADDRESS; }

    /** 
     * Try to finds an Object of type 'Address' in the database.
     * Identified by the primary key.
     * Returns <code>null</code> if is not present
     */
    @Nullable public static Address find(int id) { return myEntityTable().find(id); }

    /** 
     * Try to finds an Object of type 'Address' in the database.
     * Identified by the primary key.
     * Throws EntityNotFoundException if is not present
     */
    @NotNull public static Address findOrFail(int id) { return myEntityTable().findOrFail(id); }

    /** 
     * Try to finds an Object of type 'Address' in the database.
     * Identified by the primary key.
     * Ignoring caches and accessing the database
     * Returns <code>null</code> if is not present
     */
    @Nullable public static Address findPersisted(int id) { return myEntityTable().findPersisted(id); }

    /** 
     * Try to finds an Object of type 'Address' in the database.
     * Identified by the primary key.
     * Ignoring caches and accessing the database
     * Throws EntityNotFoundException if is not present
     */
    @NotNull public static Address findPersistedOrFail(int id) { return myEntityTable().findPersistedOrFail(id); }

    /** 
     * Try to finds an Object of type 'Address' in the database.
     * Identified by the primary key.
     * Returns <code>null</code> if is not present
     */
    @Nullable public static Address find(@NotNull String key) { return myEntityTable().findByString(key); }

    /** 
     * Try to finds an Object of type 'Address' in the database.
     * That verifies the specified condition.
     * Returns <code>null</code> if is not present
     */
    @Nullable public static Address findWhere(@NotNull Criteria... condition) { return selectFrom(ADDRESS).where(condition).get(); }

    /** Create a selectFrom(ADDRESS). */
    @NotNull public static Select<Address> list() { return selectFrom(ADDRESS); }

    /** Performs the given action for each Address */
    public static void forEach(@Nullable Consumer<Address> consumer) { selectFrom(ADDRESS).forEach(consumer); }

    /** List instances of 'Address' with the specified keys. */
    @NotNull public static ImmutableList<Address> list(@Nullable Set<Integer> keys) { return myEntityTable().list(keys); }

    /** List instances of 'Address' with the specified keys. */
    @NotNull public static ImmutableList<Address> list(@Nullable Iterable<String> keys) { return myEntityTable().listFromStringKeys(keys); }

    /** List the instances of 'Address' that verify the specified condition. */
    @NotNull public static Select<Address> listWhere(@NotNull Criteria condition) { return selectFrom(ADDRESS).where(condition); }

    /** Insert specifying the primary key */
    public void insert(int key) {
        this.id = key;
        myEntityTable().insertDoNotGenerate((Address) this);
    }

    /** Register a Listener */
    public static void addListener(@NotNull EntityListenerType listenerType, @NotNull EntityListener<Address> listener) { myEntityTable().addListener(listenerType, listener); }

    /** Remove a Listener */
    public static void removeListener(@NotNull EntityListenerType listenerType, @NotNull EntityListener<Address> listener) { myEntityTable().removeListener(listenerType, listener); }

    @NotNull public String keyAsString() { return String.valueOf(id); }

    @NotNull public Integer keyObject() { return id; }

    @Override @NotNull public final Seq<String> describe() { return formatList(getId()); }

    @Override @NotNull public String toString() { return "" + getId(); }

    /** Gets mapper for SQL statements */
    @NotNull public static RowMapper<Address> rowMapper() { return ADDRESS.metadata().getRowMapper(); }

}
