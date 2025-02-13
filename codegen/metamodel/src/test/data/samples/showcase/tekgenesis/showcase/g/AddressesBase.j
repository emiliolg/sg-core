package tekgenesis.showcase.g;

import tekgenesis.showcase.Addresses;
import java.util.function.Consumer;
import tekgenesis.persistence.Criteria;
import tekgenesis.common.core.DateTime;
import tekgenesis.persistence.DbTable;
import tekgenesis.persistence.EntityInstanceImpl;
import tekgenesis.persistence.EntityListener;
import tekgenesis.persistence.EntityListenerType;
import tekgenesis.persistence.EntityTable;
import tekgenesis.common.collections.ImmutableList;
import tekgenesis.showcase.InnerAddress;
import tekgenesis.persistence.InnerEntitySeq;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import tekgenesis.persistence.PersistableInstance;
import tekgenesis.database.RowMapper;
import tekgenesis.persistence.Select;
import tekgenesis.common.collections.Seq;
import java.util.Set;
import static tekgenesis.showcase.g.AddressesTable.ADDRESSES;
import static tekgenesis.showcase.g.InnerAddressTable.INNER_ADDRESS;
import static tekgenesis.persistence.EntitySeq.createInnerEntitySeq;
import static tekgenesis.common.util.Conversions.formatList;
import static tekgenesis.persistence.Sql.selectFrom;

/** 
 * Generated base class for entity: Addresses.
 * Don't modify this as this is an auto generated class that's gets generated
 * every time the meta model file is modified. Use subclass instead.
 */
@SuppressWarnings({"DuplicateStringLiteralInspection", "WeakerAccess", "FieldMayBeFinal", "FieldCanBeLocal", "ConstantConditions", "ClassWithTooManyMethods", "ClassWithTooManyFields", "MagicNumber", "MethodOverridesStaticMethodOfSuperclass", "ParameterHidesMemberVariable", "FieldNameHidesFieldInSuperclass", "OverlyComplexClass", "RedundantCast", "UnusedReturnValue"})
public abstract class AddressesBase
    extends EntityInstanceImpl<Addresses,Integer>
    implements PersistableInstance<Addresses,Integer>
{

    //~ Fields ...................................................................................................................

    private int id = EntityTable.DEFAULT_EMPTY_KEY;
    @NotNull private InnerEntitySeq<InnerAddress> clientAddresses = createInnerEntitySeq(INNER_ADDRESS, (Addresses) this, c -> ((InnerAddressBase)c).addresses);
    @NotNull DateTime updateTime = DateTime.EPOCH;

    //~ Methods ..................................................................................................................

    /** Returns the Id. */
    public int getId() { return this.id; }

    @Override public boolean hasEmptyKey() { return id == EntityTable.DEFAULT_EMPTY_KEY; }

    /** Returns the Client Addresses. */
    @NotNull public InnerEntitySeq<InnerAddress> getClientAddresses() { return clientAddresses; }

    /** Returns the Update Time. */
    @Override @NotNull public DateTime getUpdateTime() { return this.updateTime; }

    /** Creates a new {@link Addresses} instance. */
    @NotNull public static Addresses create() { return new Addresses(); }

    @NotNull private static EntityTable<Addresses,Integer> myEntityTable() { return EntityTable.forTable(ADDRESSES); }

    @NotNull public EntityTable<Addresses,Integer> et() { return myEntityTable(); }

    @Override @NotNull public DbTable<Addresses,Integer> table() { return ADDRESSES; }

    /** 
     * Try to finds an Object of type 'Addresses' in the database.
     * Identified by the primary key.
     * Returns <code>null</code> if is not present
     */
    @Nullable public static Addresses find(int id) { return myEntityTable().find(id); }

    /** 
     * Try to finds an Object of type 'Addresses' in the database.
     * Identified by the primary key.
     * Throws EntityNotFoundException if is not present
     */
    @NotNull public static Addresses findOrFail(int id) { return myEntityTable().findOrFail(id); }

    /** 
     * Try to finds an Object of type 'Addresses' in the database.
     * Identified by the primary key.
     * Ignoring caches and accessing the database
     * Returns <code>null</code> if is not present
     */
    @Nullable public static Addresses findPersisted(int id) { return myEntityTable().findPersisted(id); }

    /** 
     * Try to finds an Object of type 'Addresses' in the database.
     * Identified by the primary key.
     * Ignoring caches and accessing the database
     * Throws EntityNotFoundException if is not present
     */
    @NotNull public static Addresses findPersistedOrFail(int id) { return myEntityTable().findPersistedOrFail(id); }

    /** 
     * Try to finds an Object of type 'Addresses' in the database.
     * Identified by the primary key.
     * Returns <code>null</code> if is not present
     */
    @Nullable public static Addresses find(@NotNull String key) { return myEntityTable().findByString(key); }

    /** 
     * Try to finds an Object of type 'Addresses' in the database.
     * That verifies the specified condition.
     * Returns <code>null</code> if is not present
     */
    @Nullable public static Addresses findWhere(@NotNull Criteria... condition) { return selectFrom(ADDRESSES).where(condition).get(); }

    /** Create a selectFrom(ADDRESSES). */
    @NotNull public static Select<Addresses> list() { return selectFrom(ADDRESSES); }

    /** Performs the given action for each Addresses */
    public static void forEach(@Nullable Consumer<Addresses> consumer) { selectFrom(ADDRESSES).forEach(consumer); }

    /** List instances of 'Addresses' with the specified keys. */
    @NotNull public static ImmutableList<Addresses> list(@Nullable Set<Integer> keys) { return myEntityTable().list(keys); }

    /** List instances of 'Addresses' with the specified keys. */
    @NotNull public static ImmutableList<Addresses> list(@Nullable Iterable<String> keys) { return myEntityTable().listFromStringKeys(keys); }

    /** List the instances of 'Addresses' that verify the specified condition. */
    @NotNull public static Select<Addresses> listWhere(@NotNull Criteria condition) { return selectFrom(ADDRESSES).where(condition); }

    /** Insert specifying the primary key */
    public void insert(int key) {
        this.id = key;
        myEntityTable().insertDoNotGenerate((Addresses) this);
    }

    /** Register a Listener */
    public static void addListener(@NotNull EntityListenerType listenerType, @NotNull EntityListener<Addresses> listener) { myEntityTable().addListener(listenerType, listener); }

    /** Remove a Listener */
    public static void removeListener(@NotNull EntityListenerType listenerType, @NotNull EntityListener<Addresses> listener) { myEntityTable().removeListener(listenerType, listener); }

    @NotNull public String keyAsString() { return String.valueOf(id); }

    @NotNull public Integer keyObject() { return id; }

    @Override @NotNull public final Seq<String> describe() { return formatList(getId()); }

    @Override @NotNull public String toString() { return "" + getId(); }

    /** Gets mapper for SQL statements */
    @NotNull public static RowMapper<Addresses> rowMapper() { return ADDRESSES.metadata().getRowMapper(); }

    @Override public void invalidate() { clientAddresses.invalidate(); }

}
