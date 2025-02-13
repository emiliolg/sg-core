package tekgenesis.showcase.g;

import java.util.function.Consumer;
import tekgenesis.persistence.Criteria;
import tekgenesis.common.core.DateTime;
import tekgenesis.persistence.DbTable;
import tekgenesis.persistence.EntityInstanceImpl;
import tekgenesis.persistence.EntityListener;
import tekgenesis.persistence.EntityListenerType;
import tekgenesis.persistence.EntityTable;
import tekgenesis.showcase.Flight;
import tekgenesis.common.collections.ImmutableList;
import tekgenesis.common.core.Integers;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import tekgenesis.persistence.PersistableInstance;
import tekgenesis.database.RowMapper;
import tekgenesis.persistence.Select;
import tekgenesis.common.collections.Seq;
import java.util.Set;
import tekgenesis.common.core.Strings;
import static tekgenesis.showcase.g.FlightTable.FLIGHT;
import static tekgenesis.common.util.Conversions.formatList;
import static tekgenesis.persistence.Sql.selectFrom;

/** 
 * Generated base class for entity: Flight.
 * Don't modify this as this is an auto generated class that's gets generated
 * every time the meta model file is modified. Use subclass instead.
 */
@SuppressWarnings({"DuplicateStringLiteralInspection", "WeakerAccess", "FieldMayBeFinal", "FieldCanBeLocal", "ConstantConditions", "ClassWithTooManyMethods", "ClassWithTooManyFields", "MagicNumber", "MethodOverridesStaticMethodOfSuperclass", "ParameterHidesMemberVariable", "FieldNameHidesFieldInSuperclass", "OverlyComplexClass", "RedundantCast", "UnusedReturnValue"})
public abstract class FlightBase
    extends EntityInstanceImpl<Flight,Integer>
    implements PersistableInstance<Flight,Integer>
{

    //~ Fields ...................................................................................................................

    int idKey = 0;
    @NotNull String name = "";
    @NotNull String from = "";
    @NotNull String to = "";
    int price = 0;
    @NotNull DateTime updateTime = DateTime.EPOCH;

    //~ Methods ..................................................................................................................

    /** Returns the Id Key. */
    public int getIdKey() { return this.idKey; }

    /** Returns the Name. */
    @NotNull public String getName() { return this.name; }

    /** Sets the value of the Name. */
    @NotNull public Flight setName(@NotNull String name) {
        markAsModified();
        this.name = Strings.truncate(name, 20);
        return (Flight) this;
    }

    /** Returns the From. */
    @NotNull public String getFrom() { return this.from; }

    /** Sets the value of the From. */
    @NotNull public Flight setFrom(@NotNull String from) {
        markAsModified();
        this.from = Strings.truncate(from, 20);
        return (Flight) this;
    }

    /** Returns the To. */
    @NotNull public String getTo() { return this.to; }

    /** Sets the value of the To. */
    @NotNull public Flight setTo(@NotNull String to) {
        markAsModified();
        this.to = Strings.truncate(to, 20);
        return (Flight) this;
    }

    /** Returns the Price. */
    public int getPrice() { return this.price; }

    /** Sets the value of the Price. */
    @NotNull public Flight setPrice(int price) {
        markAsModified();
        this.price = Integers.checkSignedLength("price", price, false, 9);
        return (Flight) this;
    }

    /** Returns the Update Time. */
    @Override @NotNull public DateTime getUpdateTime() { return this.updateTime; }

    /** Creates a new {@link Flight} instance. */
    @NotNull public static Flight create(int idKey) {
        final Flight result = new Flight();
        ((FlightBase) result).idKey = Integers.checkSignedLength("idKey", idKey, false, 9);
        return result;
    }

    /** 
     * Find (or create if not present) a 'Flight' in the database.
     * Identified by the primary key.
     */
    @NotNull public static Flight findOrCreate(int idKey) { return myEntityTable().findOrCreate(idKey); }

    /** 
     * Find (or create if not present) a 'Flight' in the database.
     * Identified by the primary key.
     */
    @NotNull public static Flight findOrCreate(@NotNull String key) { return myEntityTable().findOrCreateByString(key); }

    @NotNull private static EntityTable<Flight,Integer> myEntityTable() { return EntityTable.forTable(FLIGHT); }

    @NotNull public EntityTable<Flight,Integer> et() { return myEntityTable(); }

    @Override @NotNull public DbTable<Flight,Integer> table() { return FLIGHT; }

    /** 
     * Try to finds an Object of type 'Flight' in the database.
     * Identified by the primary key.
     * Returns <code>null</code> if is not present
     */
    @Nullable public static Flight find(int idKey) { return myEntityTable().find(idKey); }

    /** 
     * Try to finds an Object of type 'Flight' in the database.
     * Identified by the primary key.
     * Throws EntityNotFoundException if is not present
     */
    @NotNull public static Flight findOrFail(int idKey) { return myEntityTable().findOrFail(idKey); }

    /** 
     * Try to finds an Object of type 'Flight' in the database.
     * Identified by the primary key.
     * Ignoring caches and accessing the database
     * Returns <code>null</code> if is not present
     */
    @Nullable public static Flight findPersisted(int idKey) { return myEntityTable().findPersisted(idKey); }

    /** 
     * Try to finds an Object of type 'Flight' in the database.
     * Identified by the primary key.
     * Ignoring caches and accessing the database
     * Throws EntityNotFoundException if is not present
     */
    @NotNull public static Flight findPersistedOrFail(int idKey) { return myEntityTable().findPersistedOrFail(idKey); }

    /** 
     * Try to finds an Object of type 'Flight' in the database.
     * Identified by the primary key.
     * Returns <code>null</code> if is not present
     */
    @Nullable public static Flight find(@NotNull String key) { return myEntityTable().findByString(key); }

    /** 
     * Try to finds an Object of type 'Flight' in the database.
     * That verifies the specified condition.
     * Returns <code>null</code> if is not present
     */
    @Nullable public static Flight findWhere(@NotNull Criteria... condition) { return selectFrom(FLIGHT).where(condition).get(); }

    /** Create a selectFrom(FLIGHT). */
    @NotNull public static Select<Flight> list() { return selectFrom(FLIGHT); }

    /** Performs the given action for each Flight */
    public static void forEach(@Nullable Consumer<Flight> consumer) { selectFrom(FLIGHT).forEach(consumer); }

    /** List instances of 'Flight' with the specified keys. */
    @NotNull public static ImmutableList<Flight> list(@Nullable Set<Integer> keys) { return myEntityTable().list(keys); }

    /** List instances of 'Flight' with the specified keys. */
    @NotNull public static ImmutableList<Flight> list(@Nullable Iterable<String> keys) { return myEntityTable().listFromStringKeys(keys); }

    /** List the instances of 'Flight' that verify the specified condition. */
    @NotNull public static Select<Flight> listWhere(@NotNull Criteria condition) { return selectFrom(FLIGHT).where(condition); }

    @Override @NotNull public final Flight update() { return PersistableInstance.super.update(); }

    @Override @NotNull public final Flight insert() { return PersistableInstance.super.insert(); }

    /** Register a Listener */
    public static void addListener(@NotNull EntityListenerType listenerType, @NotNull EntityListener<Flight> listener) { myEntityTable().addListener(listenerType, listener); }

    /** Remove a Listener */
    public static void removeListener(@NotNull EntityListenerType listenerType, @NotNull EntityListener<Flight> listener) { myEntityTable().removeListener(listenerType, listener); }

    @NotNull public String keyAsString() { return String.valueOf(idKey); }

    @NotNull public Integer keyObject() { return idKey; }

    @Override @NotNull public final Seq<String> describe() { return formatList(getName()); }

    @Override @NotNull public String toString() { return "" + getName(); }

    /** Gets mapper for SQL statements */
    @NotNull public static RowMapper<Flight> rowMapper() { return FLIGHT.metadata().getRowMapper(); }

}
