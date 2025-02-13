package tekgenesis.showcase.g;

import tekgenesis.showcase.Address;
import tekgenesis.showcase.Client;
import java.util.function.Consumer;
import tekgenesis.persistence.Criteria;
import tekgenesis.common.core.DateTime;
import tekgenesis.persistence.DbTable;
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
import tekgenesis.common.core.Strings;
import static tekgenesis.showcase.g.AddressTable.ADDRESS;
import static tekgenesis.showcase.g.ClientTable.CLIENT;
import static tekgenesis.common.util.Conversions.formatList;
import static tekgenesis.persistence.Sql.selectFrom;

/** 
 * Generated base class for entity: Client.
 * Don't modify this as this is an auto generated class that's gets generated
 * every time the meta model file is modified. Use subclass instead.
 */
@SuppressWarnings({"DuplicateStringLiteralInspection", "WeakerAccess", "FieldMayBeFinal", "FieldCanBeLocal", "ConstantConditions", "ClassWithTooManyMethods", "ClassWithTooManyFields", "MagicNumber", "MethodOverridesStaticMethodOfSuperclass", "ParameterHidesMemberVariable", "FieldNameHidesFieldInSuperclass", "OverlyComplexClass", "RedundantCast", "UnusedReturnValue"})
public abstract class ClientBase
    extends EntityInstanceImpl<Client,Integer>
    implements PersistableInstance<Client,Integer>
{

    //~ Fields ...................................................................................................................

    private int id = EntityTable.DEFAULT_EMPTY_KEY;
    @NotNull String name = "";
    int addressId = 0;
    @NotNull EntityRef<Address,Integer> address = new EntityRef<>(ADDRESS);
    @NotNull DateTime updateTime = DateTime.EPOCH;

    //~ Methods ..................................................................................................................

    /** Returns the Id. */
    public int getId() { return this.id; }

    @Override public boolean hasEmptyKey() { return id == EntityTable.DEFAULT_EMPTY_KEY; }

    /** Returns the Name. */
    @NotNull public String getName() { return this.name; }

    /** Sets the value of the Name. */
    @NotNull public Client setName(@NotNull String name) {
        markAsModified();
        this.name = Strings.truncate(name, 50);
        return (Client) this;
    }

    /** Returns the Address Id. */
    public int getAddressId() { return this.addressId; }

    /** Returns the Address. */
    @NotNull public Address getAddress() { return address.solveOrFail(this.addressId); }

    /** Sets the value of the Address Id. */
    @NotNull public Client setAddressId(int addressId) {
        address.invalidate();
        this.addressId = addressId;
        return (Client) this;
    }

    /** Sets the value of the Address. */
    @NotNull public Client setAddress(@NotNull Address address) {
        this.address.set(address);
        this.addressId = address.getId();
        return (Client) this;
    }

    /** Returns the Update Time. */
    @Override @NotNull public DateTime getUpdateTime() { return this.updateTime; }

    /** Creates a new {@link Client} instance. */
    @NotNull public static Client create() { return new Client(); }

    @NotNull private static EntityTable<Client,Integer> myEntityTable() { return EntityTable.forTable(CLIENT); }

    @NotNull public EntityTable<Client,Integer> et() { return myEntityTable(); }

    @Override @NotNull public DbTable<Client,Integer> table() { return CLIENT; }

    /** 
     * Try to finds an Object of type 'Client' in the database.
     * Identified by the primary key.
     * Returns <code>null</code> if is not present
     */
    @Nullable public static Client find(int id) { return myEntityTable().find(id); }

    /** 
     * Try to finds an Object of type 'Client' in the database.
     * Identified by the primary key.
     * Throws EntityNotFoundException if is not present
     */
    @NotNull public static Client findOrFail(int id) { return myEntityTable().findOrFail(id); }

    /** 
     * Try to finds an Object of type 'Client' in the database.
     * Identified by the primary key.
     * Ignoring caches and accessing the database
     * Returns <code>null</code> if is not present
     */
    @Nullable public static Client findPersisted(int id) { return myEntityTable().findPersisted(id); }

    /** 
     * Try to finds an Object of type 'Client' in the database.
     * Identified by the primary key.
     * Ignoring caches and accessing the database
     * Throws EntityNotFoundException if is not present
     */
    @NotNull public static Client findPersistedOrFail(int id) { return myEntityTable().findPersistedOrFail(id); }

    /** 
     * Try to finds an Object of type 'Client' in the database.
     * Identified by the primary key.
     * Returns <code>null</code> if is not present
     */
    @Nullable public static Client find(@NotNull String key) { return myEntityTable().findByString(key); }

    /** 
     * Try to finds an Object of type 'Client' in the database.
     * That verifies the specified condition.
     * Returns <code>null</code> if is not present
     */
    @Nullable public static Client findWhere(@NotNull Criteria... condition) { return selectFrom(CLIENT).where(condition).get(); }

    /** Create a selectFrom(CLIENT). */
    @NotNull public static Select<Client> list() { return selectFrom(CLIENT); }

    /** Performs the given action for each Client */
    public static void forEach(@Nullable Consumer<Client> consumer) { selectFrom(CLIENT).forEach(consumer); }

    /** List instances of 'Client' with the specified keys. */
    @NotNull public static ImmutableList<Client> list(@Nullable Set<Integer> keys) { return myEntityTable().list(keys); }

    /** List instances of 'Client' with the specified keys. */
    @NotNull public static ImmutableList<Client> list(@Nullable Iterable<String> keys) { return myEntityTable().listFromStringKeys(keys); }

    /** List the instances of 'Client' that verify the specified condition. */
    @NotNull public static Select<Client> listWhere(@NotNull Criteria condition) { return selectFrom(CLIENT).where(condition); }

    /** Insert specifying the primary key */
    public void insert(int key) {
        this.id = key;
        myEntityTable().insertDoNotGenerate((Client) this);
    }

    /** Register a Listener */
    public static void addListener(@NotNull EntityListenerType listenerType, @NotNull EntityListener<Client> listener) { myEntityTable().addListener(listenerType, listener); }

    /** Remove a Listener */
    public static void removeListener(@NotNull EntityListenerType listenerType, @NotNull EntityListener<Client> listener) { myEntityTable().removeListener(listenerType, listener); }

    @NotNull public String keyAsString() { return String.valueOf(id); }

    @NotNull public Integer keyObject() { return id; }

    @Override @NotNull public final Seq<String> describe() { return formatList(getId()); }

    @Override @NotNull public String toString() { return "" + getId(); }

    /** Gets mapper for SQL statements */
    @NotNull public static RowMapper<Client> rowMapper() { return CLIENT.metadata().getRowMapper(); }

    @Override public void invalidate() { address.invalidate(); }

}
