package tekgenesis.showcase.g;

import tekgenesis.showcase.Addresses;
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
import tekgenesis.showcase.InnerAddress;
import tekgenesis.persistence.InnerEntitySeq;
import tekgenesis.persistence.InnerInstance;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import tekgenesis.database.RowMapper;
import tekgenesis.persistence.Select;
import tekgenesis.common.collections.Seq;
import java.util.Set;
import tekgenesis.common.core.Strings;
import tekgenesis.common.core.Tuple;
import static tekgenesis.showcase.g.AddressesTable.ADDRESSES;
import static tekgenesis.showcase.g.InnerAddressTable.INNER_ADDRESS;
import static tekgenesis.common.util.Conversions.formatList;
import static tekgenesis.persistence.Sql.selectFrom;

/** 
 * Generated base class for entity: InnerAddress.
 * Don't modify this as this is an auto generated class that's gets generated
 * every time the meta model file is modified. Use subclass instead.
 */
@SuppressWarnings({"DuplicateStringLiteralInspection", "WeakerAccess", "FieldMayBeFinal", "FieldCanBeLocal", "ConstantConditions", "ClassWithTooManyMethods", "ClassWithTooManyFields", "MagicNumber", "MethodOverridesStaticMethodOfSuperclass", "ParameterHidesMemberVariable", "FieldNameHidesFieldInSuperclass", "OverlyComplexClass", "RedundantCast", "UnusedReturnValue"})
public abstract class InnerAddressBase
    extends EntityInstanceImpl<InnerAddress,Tuple<Integer,Integer>>
    implements InnerInstance<InnerAddress,Tuple<Integer,Integer>,Addresses,Integer>
{

    //~ Fields ...................................................................................................................

    int addressesId = 0;
    @NotNull EntityRef<Addresses,Integer> addresses = new EntityRef<>(ADDRESSES, Addresses::getClientAddresses);
    int seqId = 0;
    @NotNull String street = "";
    @NotNull String city = "";
    @Nullable String state = null;
    @Nullable String zip = null;
    @NotNull String country = "";
    @NotNull DateTime updateTime = DateTime.EPOCH;

    //~ Methods ..................................................................................................................

    /** Returns the Addresses Id. */
    public int getAddressesId() { return this.addressesId; }

    /** Returns the Addresses. */
    @NotNull public Addresses getAddresses() { return addresses.solveOrFail(this.addressesId); }

    /** Returns the Seq Id. */
    public int getSeqId() { return this.seqId; }

    @Override @NotNull public EntityRef<Addresses,Integer> parent() { return addresses; }

    @Override @NotNull public InnerEntitySeq<InnerAddress> siblings() { return getAddresses().getClientAddresses(); }

    @Override public int seqId() { return getSeqId(); }

    /** Returns the Street. */
    @NotNull public String getStreet() { return this.street; }

    /** Sets the value of the Street. */
    @NotNull public InnerAddress setStreet(@NotNull String street) {
        markAsModified();
        this.street = Strings.truncate(street, 60);
        return (InnerAddress) this;
    }

    /** Returns the City. */
    @NotNull public String getCity() { return this.city; }

    /** Sets the value of the City. */
    @NotNull public InnerAddress setCity(@NotNull String city) {
        markAsModified();
        this.city = Strings.truncate(city, 40);
        return (InnerAddress) this;
    }

    /** Returns the State. */
    @Nullable public String getState() { return this.state; }

    /** Sets the value of the State. */
    @NotNull public InnerAddress setState(@Nullable String state) {
        markAsModified();
        this.state = Strings.truncate(state, 40);
        return (InnerAddress) this;
    }

    /** Returns the Zip. */
    @Nullable public String getZip() { return this.zip; }

    /** Sets the value of the Zip. */
    @NotNull public InnerAddress setZip(@Nullable String zip) {
        markAsModified();
        this.zip = Strings.truncate(zip, 10);
        return (InnerAddress) this;
    }

    /** Returns the Country. */
    @NotNull public String getCountry() { return this.country; }

    /** Sets the value of the Country. */
    @NotNull public InnerAddress setCountry(@NotNull String country) {
        markAsModified();
        this.country = Strings.truncate(country, 30);
        return (InnerAddress) this;
    }

    /** Returns the Update Time. */
    @Override @NotNull public DateTime getUpdateTime() { return this.updateTime; }

    @NotNull private static EntityTable<InnerAddress,Tuple<Integer,Integer>> myEntityTable() { return EntityTable.forTable(INNER_ADDRESS); }

    @NotNull public EntityTable<InnerAddress,Tuple<Integer,Integer>> et() { return myEntityTable(); }

    @Override @NotNull public DbTable<InnerAddress,Tuple<Integer,Integer>> table() { return INNER_ADDRESS; }

    /** 
     * Try to finds an Object of type 'InnerAddress' in the database.
     * Identified by the primary key.
     * Returns <code>null</code> if is not present
     */
    @Nullable public static InnerAddress find(@NotNull Tuple<Integer,Integer> key) { return myEntityTable().find(key); }

    /** 
     * Try to finds an Object of type 'InnerAddress' in the database.
     * Identified by the primary key.
     * Throws EntityNotFoundException if is not present
     */
    @NotNull public static InnerAddress findOrFail(@NotNull Tuple<Integer,Integer> key) { return myEntityTable().findOrFail(key); }

    /** 
     * Try to finds an Object of type 'InnerAddress' in the database.
     * Identified by the primary key.
     * Ignoring caches and accessing the database
     * Returns <code>null</code> if is not present
     */
    @Nullable public static InnerAddress findPersisted(@NotNull Tuple<Integer,Integer> key) { return myEntityTable().findPersisted(key); }

    /** 
     * Try to finds an Object of type 'InnerAddress' in the database.
     * Identified by the primary key.
     * Ignoring caches and accessing the database
     * Throws EntityNotFoundException if is not present
     */
    @NotNull public static InnerAddress findPersistedOrFail(@NotNull Tuple<Integer,Integer> key) { return myEntityTable().findPersistedOrFail(key); }

    /** 
     * Try to finds an Object of type 'InnerAddress' in the database.
     * Identified by the primary key.
     * Returns <code>null</code> if is not present
     */
    @Nullable public static InnerAddress find(@NotNull String key) { return myEntityTable().findByString(key); }

    /** 
     * Try to finds an Object of type 'InnerAddress' in the database.
     * Identified by the primary key.
     * Returns <code>null</code> if is not present
     */
    @Nullable public static InnerAddress find(int addressesId, int seqId) { return find(Tuple.tuple2(addressesId, seqId)); }

    /** 
     * Try to finds an Object of type 'InnerAddress' in the database.
     * Identified by the primary key.
     * Returns <code>null</code> if is not present
     * Based on String key for Entities
     */
    @Nullable public static InnerAddress find(@NotNull String addresses, int seqId) { return find(Conversions.toInt(addresses), seqId); }

    /** 
     * Try to finds an Object of type 'InnerAddress' in the database.
     * That verifies the specified condition.
     * Returns <code>null</code> if is not present
     */
    @Nullable public static InnerAddress findWhere(@NotNull Criteria... condition) { return selectFrom(INNER_ADDRESS).where(condition).get(); }

    /** Create a selectFrom(INNER_ADDRESS). */
    @NotNull public static Select<InnerAddress> list() { return selectFrom(INNER_ADDRESS); }

    /** Performs the given action for each InnerAddress */
    public static void forEach(@Nullable Consumer<InnerAddress> consumer) { selectFrom(INNER_ADDRESS).forEach(consumer); }

    /** List instances of 'InnerAddress' with the specified keys. */
    @NotNull public static ImmutableList<InnerAddress> list(@Nullable Set<Tuple<Integer,Integer>> keys) { return myEntityTable().list(keys); }

    /** List instances of 'InnerAddress' with the specified keys. */
    @NotNull public static ImmutableList<InnerAddress> list(@Nullable Iterable<String> keys) { return myEntityTable().listFromStringKeys(keys); }

    /** List the instances of 'InnerAddress' that verify the specified condition. */
    @NotNull public static Select<InnerAddress> listWhere(@NotNull Criteria condition) { return selectFrom(INNER_ADDRESS).where(condition); }

    @Override @NotNull public final InnerAddress update() { return InnerInstance.super.update(); }

    @Override @NotNull public final InnerAddress insert() { return InnerInstance.super.insert(); }

    /** Register a Listener */
    public static void addListener(@NotNull EntityListenerType listenerType, @NotNull EntityListener<InnerAddress> listener) { myEntityTable().addListener(listenerType, listener); }

    /** Remove a Listener */
    public static void removeListener(@NotNull EntityListenerType listenerType, @NotNull EntityListener<InnerAddress> listener) { myEntityTable().removeListener(listenerType, listener); }

    @NotNull public String keyAsString() { return addressesId + ":" + seqId; }

    @NotNull public Tuple<Integer,Integer> keyObject() { return Tuple.tuple2(addressesId, seqId); }

    @Override @NotNull public final Seq<String> describe() { return formatList(getAddresses(), getSeqId()); }

    @Override @NotNull public String toString() { return "" + getAddresses() + " " + getSeqId(); }

    /** Gets mapper for SQL statements */
    @NotNull public static RowMapper<InnerAddress> rowMapper() { return INNER_ADDRESS.metadata().getRowMapper(); }

    @Override public void invalidate() { addresses.invalidate(); }

}
