package tekgenesis.sales.basic.g;

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
import static tekgenesis.sales.basic.g.CountryTable.COUNTRY;
import static tekgenesis.sales.basic.g.StateProvinceTable.STATE_PROVINCE;
import static tekgenesis.common.util.Conversions.formatList;
import static tekgenesis.persistence.Sql.selectFrom;

/** 
 * Generated base class for entity: StateProvince.
 * Don't modify this as this is an auto generated class that's gets generated
 * every time the meta model file is modified. Use subclass instead.
 */
@SuppressWarnings({"DuplicateStringLiteralInspection", "WeakerAccess", "FieldMayBeFinal", "FieldCanBeLocal", "ConstantConditions", "ClassWithTooManyMethods", "ClassWithTooManyFields", "MagicNumber", "MethodOverridesStaticMethodOfSuperclass", "ParameterHidesMemberVariable", "FieldNameHidesFieldInSuperclass", "OverlyComplexClass", "RedundantCast", "UnusedReturnValue"})
public abstract class StateProvinceBase
    extends EntityInstanceImpl<StateProvince,Tuple<String,String>>
    implements PersistableInstance<StateProvince,Tuple<String,String>>
{

    //~ Fields ...................................................................................................................

    @NotNull String countryIso2 = "";
    @NotNull EntityRef<Country,String> country = new EntityRef<>(COUNTRY);
    @NotNull String code = "";
    @NotNull String name = "";
    @NotNull DateTime updateTime = DateTime.EPOCH;

    //~ Methods ..................................................................................................................

    /** Returns the Country Iso2. */
    @NotNull public String getCountryIso2() { return this.countryIso2; }

    /** Returns the Country. */
    @NotNull public Country getCountry() { return country.solveOrFail(this.countryIso2); }

    /** Returns the Code. */
    @NotNull public String getCode() { return this.code; }

    /** Returns the Name. */
    @NotNull public String getName() { return this.name; }

    /** Sets the value of the Name. */
    @NotNull public StateProvince setName(@NotNull String name) {
        markAsModified();
        this.name = Strings.truncate(name, 30);
        return (StateProvince) this;
    }

    /** Returns the Update Time. */
    @Override @NotNull public DateTime getUpdateTime() { return this.updateTime; }

    /** Creates a new {@link StateProvince} instance. */
    @NotNull public static StateProvince create(@NotNull String countryIso2, @NotNull String code) {
        final StateProvince result = new StateProvince();
        ((StateProvinceBase) result).countryIso2 = Strings.truncate(countryIso2, 2);
        ((StateProvinceBase) result).code = Strings.truncate(code, 2);
        return result;
    }

    /** 
     * Creates a new {@link StateProvince} instance.
     * Based on the primary key object
     */
    @NotNull public static StateProvince create(@NotNull Tuple<String,String> key) { return create(key._1(), key._2()); }

    /** 
     * Find (or create if not present) a 'StateProvince' in the database.
     * Identified by the primary key.
     */
    @NotNull public static StateProvince findOrCreate(@NotNull Tuple<String,String> key) { return myEntityTable().findOrCreate(key); }

    /** 
     * Find (or create if not present) a 'StateProvince' in the database.
     * Identified by the primary key.
     */
    @NotNull public static StateProvince findOrCreate(@NotNull String key) { return myEntityTable().findOrCreateByString(key); }

    /** 
     * Find (or create if not present) a 'StateProvince' in the database.
     * Identified by the primary key.
     */
    @NotNull public static StateProvince findOrCreate(@NotNull String countryIso2, @NotNull String code) { return findOrCreate(Tuple.tuple2(countryIso2, code)); }

    @NotNull private static EntityTable<StateProvince,Tuple<String,String>> myEntityTable() { return EntityTable.forTable(STATE_PROVINCE); }

    @NotNull public EntityTable<StateProvince,Tuple<String,String>> et() { return myEntityTable(); }

    @Override @NotNull public DbTable<StateProvince,Tuple<String,String>> table() { return STATE_PROVINCE; }

    /** 
     * Try to finds an Object of type 'StateProvince' in the database.
     * Identified by the primary key.
     * Returns <code>null</code> if is not present
     */
    @Nullable public static StateProvince find(@NotNull Tuple<String,String> key) { return myEntityTable().find(key); }

    /** 
     * Try to finds an Object of type 'StateProvince' in the database.
     * Identified by the primary key.
     * Throws EntityNotFoundException if is not present
     */
    @NotNull public static StateProvince findOrFail(@NotNull Tuple<String,String> key) { return myEntityTable().findOrFail(key); }

    /** 
     * Try to finds an Object of type 'StateProvince' in the database.
     * Identified by the primary key.
     * Ignoring caches and accessing the database
     * Returns <code>null</code> if is not present
     */
    @Nullable public static StateProvince findPersisted(@NotNull Tuple<String,String> key) { return myEntityTable().findPersisted(key); }

    /** 
     * Try to finds an Object of type 'StateProvince' in the database.
     * Identified by the primary key.
     * Ignoring caches and accessing the database
     * Throws EntityNotFoundException if is not present
     */
    @NotNull public static StateProvince findPersistedOrFail(@NotNull Tuple<String,String> key) { return myEntityTable().findPersistedOrFail(key); }

    /** 
     * Try to finds an Object of type 'StateProvince' in the database.
     * Identified by the primary key.
     * Returns <code>null</code> if is not present
     */
    @Nullable public static StateProvince find(@NotNull String key) { return myEntityTable().findByString(key); }

    /** 
     * Try to finds an Object of type 'StateProvince' in the database.
     * Identified by the primary key.
     * Returns <code>null</code> if is not present
     */
    @Nullable public static StateProvince find(@NotNull String countryIso2, @NotNull String code) { return find(Tuple.tuple2(countryIso2, code)); }

    /** 
     * Try to finds an Object of type 'StateProvince' in the database.
     * That verifies the specified condition.
     * Returns <code>null</code> if is not present
     */
    @Nullable public static StateProvince findWhere(@NotNull Criteria... condition) { return selectFrom(STATE_PROVINCE).where(condition).get(); }

    /** Create a selectFrom(STATE_PROVINCE). */
    @NotNull public static Select<StateProvince> list() { return selectFrom(STATE_PROVINCE); }

    /** Performs the given action for each StateProvince */
    public static void forEach(@Nullable Consumer<StateProvince> consumer) { selectFrom(STATE_PROVINCE).forEach(consumer); }

    /** List instances of 'StateProvince' with the specified keys. */
    @NotNull public static ImmutableList<StateProvince> list(@Nullable Set<Tuple<String,String>> keys) { return myEntityTable().list(keys); }

    /** List instances of 'StateProvince' with the specified keys. */
    @NotNull public static ImmutableList<StateProvince> list(@Nullable Iterable<String> keys) { return myEntityTable().listFromStringKeys(keys); }

    /** List the instances of 'StateProvince' that verify the specified condition. */
    @NotNull public static Select<StateProvince> listWhere(@NotNull Criteria condition) { return selectFrom(STATE_PROVINCE).where(condition); }

    @Override @NotNull public final StateProvince update() { return PersistableInstance.super.update(); }

    @Override @NotNull public final StateProvince insert() { return PersistableInstance.super.insert(); }

    /** Register a Listener */
    public static void addListener(@NotNull EntityListenerType listenerType, @NotNull EntityListener<StateProvince> listener) { myEntityTable().addListener(listenerType, listener); }

    /** Remove a Listener */
    public static void removeListener(@NotNull EntityListenerType listenerType, @NotNull EntityListener<StateProvince> listener) { myEntityTable().removeListener(listenerType, listener); }

    @NotNull public String keyAsString() {
        return Strings.escapeCharOn(countryIso2, ':') + ":" + Strings.escapeCharOn(code, ':');
    }

    @NotNull public Tuple<String,String> keyObject() { return Tuple.tuple2(countryIso2, code); }

    @Override @NotNull public final Seq<String> describe() { return formatList(getCountry(), getCode()); }

    @Override @NotNull public String toString() { return "" + getCountry() + " " + getCode(); }

    /** Gets mapper for SQL statements */
    @NotNull public static RowMapper<StateProvince> rowMapper() { return STATE_PROVINCE.metadata().getRowMapper(); }

    @Override public void invalidate() { country.invalidate(); }

}
