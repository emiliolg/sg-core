package tekgenesis.sales.basic.g;

import tekgenesis.sales.basic.City;
import java.util.function.Consumer;
import tekgenesis.sales.basic.Country;
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
import tekgenesis.sales.basic.StateProvince;
import tekgenesis.common.core.Strings;
import tekgenesis.common.core.Tuple;
import static tekgenesis.sales.basic.g.CityTable.CITY;
import static tekgenesis.sales.basic.g.StateProvinceTable.STATE_PROVINCE;
import static tekgenesis.common.util.Conversions.formatList;
import static tekgenesis.persistence.Sql.selectFrom;

/** 
 * Generated base class for entity: City.
 * Don't modify this as this is an auto generated class that's gets generated
 * every time the meta model file is modified. Use subclass instead.
 */
@SuppressWarnings({"DuplicateStringLiteralInspection", "WeakerAccess", "FieldMayBeFinal", "FieldCanBeLocal", "ConstantConditions", "ClassWithTooManyMethods", "ClassWithTooManyFields", "MagicNumber", "MethodOverridesStaticMethodOfSuperclass", "ParameterHidesMemberVariable", "FieldNameHidesFieldInSuperclass", "OverlyComplexClass", "RedundantCast", "UnusedReturnValue"})
public abstract class CityBase
    extends EntityInstanceImpl<City,Integer>
    implements PersistableInstance<City,Integer>
{

    //~ Fields ...................................................................................................................

    private int id = EntityTable.DEFAULT_EMPTY_KEY;
    @NotNull String name = "";
    @NotNull String stateProvinceCountryIso2 = "";
    @NotNull String stateProvinceCode = "";
    @NotNull EntityRef<StateProvince,Tuple<String,String>> stateProvince = new EntityRef<>(STATE_PROVINCE);
    @NotNull DateTime updateTime = DateTime.EPOCH;

    //~ Methods ..................................................................................................................

    /** Returns the Id. */
    public int getId() { return this.id; }

    @Override public boolean hasEmptyKey() { return id == EntityTable.DEFAULT_EMPTY_KEY; }

    /** Returns the Name. */
    @NotNull public String getName() { return this.name; }

    /** Sets the value of the Name. */
    @NotNull public City setName(@NotNull String name) {
        markAsModified();
        this.name = Strings.truncate(name, 30);
        return (City) this;
    }

    /** Returns the State Province Country Iso2. */
    @NotNull public String getStateProvinceCountryIso2() { return this.stateProvinceCountryIso2; }

    /** Returns the State Province Code. */
    @NotNull public String getStateProvinceCode() { return this.stateProvinceCode; }

    /** Returns the State Province. */
    @NotNull public StateProvince getStateProvince() {
        return stateProvince.solveOrFail(Tuple.tuple2(this.stateProvinceCountryIso2, this.stateProvinceCode));
    }

    /** Sets the value of the State Province Country Iso2. */
    @NotNull public City setStateProvinceCountryIso2(@NotNull String stateProvinceCountryIso2) {
        stateProvince.invalidate();
        this.stateProvinceCountryIso2 = stateProvinceCountryIso2;
        return (City) this;
    }

    /** Sets the value of the State Province Code. */
    @NotNull public City setStateProvinceCode(@NotNull String stateProvinceCode) {
        stateProvince.invalidate();
        this.stateProvinceCode = stateProvinceCode;
        return (City) this;
    }

    /** Sets the value of the State Province. */
    @NotNull public City setStateProvince(@NotNull StateProvince stateProvince) {
        this.stateProvince.set(stateProvince);
        this.stateProvinceCountryIso2 = stateProvince.getCountryIso2();
        this.stateProvinceCode = stateProvince.getCode();
        return (City) this;
    }

    /** Returns the Country. */
    @NotNull public abstract Country getCountry();

    /** Returns the Polygon. */
    @NotNull public abstract Seq<Integer> getPolygon();

    /** Returns the Countries. */
    @NotNull public abstract Seq<Country> getCountries();

    /** Returns the Country Name. */
    @NotNull public abstract String getCountryName();

    /** Returns the Update Time. */
    @Override @NotNull public DateTime getUpdateTime() { return this.updateTime; }

    /** Creates a new {@link City} instance. */
    @NotNull public static City create() { return new City(); }

    @NotNull private static EntityTable<City,Integer> myEntityTable() { return EntityTable.forTable(CITY); }

    @NotNull public EntityTable<City,Integer> et() { return myEntityTable(); }

    @Override @NotNull public DbTable<City,Integer> table() { return CITY; }

    /** 
     * Try to finds an Object of type 'City' in the database.
     * Identified by the primary key.
     * Returns <code>null</code> if is not present
     */
    @Nullable public static City find(int id) { return myEntityTable().find(id); }

    /** 
     * Try to finds an Object of type 'City' in the database.
     * Identified by the primary key.
     * Throws EntityNotFoundException if is not present
     */
    @NotNull public static City findOrFail(int id) { return myEntityTable().findOrFail(id); }

    /** 
     * Try to finds an Object of type 'City' in the database.
     * Identified by the primary key.
     * Ignoring caches and accessing the database
     * Returns <code>null</code> if is not present
     */
    @Nullable public static City findPersisted(int id) { return myEntityTable().findPersisted(id); }

    /** 
     * Try to finds an Object of type 'City' in the database.
     * Identified by the primary key.
     * Ignoring caches and accessing the database
     * Throws EntityNotFoundException if is not present
     */
    @NotNull public static City findPersistedOrFail(int id) { return myEntityTable().findPersistedOrFail(id); }

    /** 
     * Try to finds an Object of type 'City' in the database.
     * Identified by the primary key.
     * Returns <code>null</code> if is not present
     */
    @Nullable public static City find(@NotNull String key) { return myEntityTable().findByString(key); }

    /** 
     * Try to finds an Object of type 'City' in the database.
     * That verifies the specified condition.
     * Returns <code>null</code> if is not present
     */
    @Nullable public static City findWhere(@NotNull Criteria... condition) { return selectFrom(CITY).where(condition).get(); }

    /** Create a selectFrom(CITY). */
    @NotNull public static Select<City> list() { return selectFrom(CITY); }

    /** Performs the given action for each City */
    public static void forEach(@Nullable Consumer<City> consumer) { selectFrom(CITY).forEach(consumer); }

    /** List instances of 'City' with the specified keys. */
    @NotNull public static ImmutableList<City> list(@Nullable Set<Integer> keys) { return myEntityTable().list(keys); }

    /** List instances of 'City' with the specified keys. */
    @NotNull public static ImmutableList<City> list(@Nullable Iterable<String> keys) { return myEntityTable().listFromStringKeys(keys); }

    /** List the instances of 'City' that verify the specified condition. */
    @NotNull public static Select<City> listWhere(@NotNull Criteria condition) { return selectFrom(CITY).where(condition); }

    /** Insert specifying the primary key */
    public void insert(int key) {
        this.id = key;
        myEntityTable().insertDoNotGenerate((City) this);
    }

    /** Register a Listener */
    public static void addListener(@NotNull EntityListenerType listenerType, @NotNull EntityListener<City> listener) { myEntityTable().addListener(listenerType, listener); }

    /** Remove a Listener */
    public static void removeListener(@NotNull EntityListenerType listenerType, @NotNull EntityListener<City> listener) { myEntityTable().removeListener(listenerType, listener); }

    @NotNull public String keyAsString() { return String.valueOf(id); }

    @NotNull public Integer keyObject() { return id; }

    @Override @NotNull public final Seq<String> describe() { return formatList(getId()); }

    @Override @NotNull public String toString() { return "" + getId(); }

    /** Gets mapper for SQL statements */
    @NotNull public static RowMapper<City> rowMapper() { return CITY.metadata().getRowMapper(); }

    @Override public void invalidate() { stateProvince.invalidate(); }

}
