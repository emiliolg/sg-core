package tekgenesis.sales.basic.g;

import java.util.function.Consumer;
import tekgenesis.sales.basic.CountryView;
import tekgenesis.persistence.Criteria;
import tekgenesis.common.core.DateTime;
import tekgenesis.persistence.DbTable;
import tekgenesis.persistence.EntityInstance;
import tekgenesis.persistence.EntityInstanceImpl;
import tekgenesis.persistence.EntityRef;
import tekgenesis.persistence.EntityTable;
import tekgenesis.common.collections.ImmutableList;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import tekgenesis.database.RowMapper;
import tekgenesis.persistence.Select;
import java.util.Set;
import tekgenesis.sales.basic.StateProvinceView;
import tekgenesis.common.core.Strings;
import tekgenesis.common.core.Tuple;
import static tekgenesis.sales.basic.g.CountryViewTable.COUNTRY_VIEW;
import static tekgenesis.sales.basic.g.StateProvinceViewTable.STATE_PROVINCE_VIEW;
import static tekgenesis.persistence.Sql.selectFrom;

/** 
 * Generated base class for entity: StateProvinceView.
 * Don't modify this as this is an auto generated class that's gets generated
 * every time the meta model file is modified. Use subclass instead.
 */
@SuppressWarnings({"DuplicateStringLiteralInspection", "WeakerAccess", "FieldMayBeFinal", "FieldCanBeLocal", "ConstantConditions", "ClassWithTooManyMethods", "ClassWithTooManyFields", "MagicNumber", "MethodOverridesStaticMethodOfSuperclass", "ParameterHidesMemberVariable", "FieldNameHidesFieldInSuperclass", "OverlyComplexClass", "RedundantCast", "UnusedReturnValue"})
public abstract class StateProvinceViewBase
    extends EntityInstanceImpl<StateProvinceView,Tuple<String,String>>
    implements EntityInstance<StateProvinceView,Tuple<String,String>>
{

    //~ Fields ...................................................................................................................

    @NotNull String countryIso = "";
    @NotNull EntityRef<CountryView,String> country = new EntityRef<>(COUNTRY_VIEW);
    @NotNull String code = "";
    @NotNull String name = "";
    @NotNull DateTime updateTime = DateTime.EPOCH;

    //~ Methods ..................................................................................................................

    /** Returns the Country Iso. */
    @NotNull public String getCountryIso() { return this.countryIso; }

    /** Returns the Country. */
    @NotNull public CountryView getCountry() { return country.solveOrFail(this.countryIso); }

    /** Returns the Code. */
    @NotNull public String getCode() { return this.code; }

    /** Returns the Name. */
    @NotNull public String getName() { return this.name; }

    /** Returns the Update Time. */
    @Override @NotNull public DateTime getUpdateTime() { return this.updateTime; }

    /** Creates a new {@link StateProvinceView} instance. */
    @NotNull protected static StateProvinceView create(@NotNull String countryIso, @NotNull String code) {
        final StateProvinceView result = new StateProvinceView();
        ((StateProvinceViewBase) result).countryIso = Strings.truncate(countryIso, 2);
        ((StateProvinceViewBase) result).code = Strings.truncate(code, 2);
        return result;
    }

    /** 
     * Creates a new {@link StateProvinceView} instance.
     * Based on the primary key object
     */
    @NotNull protected static StateProvinceView create(@NotNull Tuple<String,String> key) { return create(key._1(), key._2()); }

    @NotNull private static EntityTable<StateProvinceView,Tuple<String,String>> myEntityTable() { return EntityTable.forTable(STATE_PROVINCE_VIEW); }

    @NotNull public EntityTable<StateProvinceView,Tuple<String,String>> et() { return myEntityTable(); }

    @Override @NotNull public DbTable<StateProvinceView,Tuple<String,String>> table() { return STATE_PROVINCE_VIEW; }

    /** 
     * Try to finds an Object of type 'StateProvinceView' in the database.
     * Identified by the primary key.
     * Returns <code>null</code> if is not present
     */
    @Nullable public static StateProvinceView find(@NotNull Tuple<String,String> key) { return myEntityTable().find(key); }

    /** 
     * Try to finds an Object of type 'StateProvinceView' in the database.
     * Identified by the primary key.
     * Throws EntityNotFoundException if is not present
     */
    @NotNull public static StateProvinceView findOrFail(@NotNull Tuple<String,String> key) { return myEntityTable().findOrFail(key); }

    /** 
     * Try to finds an Object of type 'StateProvinceView' in the database.
     * Identified by the primary key.
     * Ignoring caches and accessing the database
     * Returns <code>null</code> if is not present
     */
    @Nullable public static StateProvinceView findPersisted(@NotNull Tuple<String,String> key) { return myEntityTable().findPersisted(key); }

    /** 
     * Try to finds an Object of type 'StateProvinceView' in the database.
     * Identified by the primary key.
     * Ignoring caches and accessing the database
     * Throws EntityNotFoundException if is not present
     */
    @NotNull public static StateProvinceView findPersistedOrFail(@NotNull Tuple<String,String> key) { return myEntityTable().findPersistedOrFail(key); }

    /** 
     * Try to finds an Object of type 'StateProvinceView' in the database.
     * Identified by the primary key.
     * Returns <code>null</code> if is not present
     */
    @Nullable public static StateProvinceView find(@NotNull String key) { return myEntityTable().findByString(key); }

    /** 
     * Try to finds an Object of type 'StateProvinceView' in the database.
     * Identified by the primary key.
     * Returns <code>null</code> if is not present
     */
    @Nullable public static StateProvinceView find(@NotNull String countryIso, @NotNull String code) { return find(Tuple.tuple2(countryIso, code)); }

    /** 
     * Try to finds an Object of type 'StateProvinceView' in the database.
     * That verifies the specified condition.
     * Returns <code>null</code> if is not present
     */
    @Nullable public static StateProvinceView findWhere(@NotNull Criteria... condition) {
        return selectFrom(STATE_PROVINCE_VIEW).where(condition).get();
    }

    /** Create a selectFrom(STATE_PROVINCE_VIEW). */
    @NotNull public static Select<StateProvinceView> list() { return selectFrom(STATE_PROVINCE_VIEW); }

    /** Performs the given action for each StateProvinceView */
    public static void forEach(@Nullable Consumer<StateProvinceView> consumer) { selectFrom(STATE_PROVINCE_VIEW).forEach(consumer); }

    /** List instances of 'StateProvinceView' with the specified keys. */
    @NotNull public static ImmutableList<StateProvinceView> list(@Nullable Set<Tuple<String,String>> keys) { return myEntityTable().list(keys); }

    /** List instances of 'StateProvinceView' with the specified keys. */
    @NotNull public static ImmutableList<StateProvinceView> list(@Nullable Iterable<String> keys) { return myEntityTable().listFromStringKeys(keys); }

    /** List the instances of 'StateProvinceView' that verify the specified condition. */
    @NotNull public static Select<StateProvinceView> listWhere(@NotNull Criteria condition) { return selectFrom(STATE_PROVINCE_VIEW).where(condition); }

    /** List the instances of 'StateProvinceView' that matches the given parameters. */
    @NotNull public static ImmutableList<StateProvinceView> listByCountry(@NotNull String countryIso) {
        return selectFrom(STATE_PROVINCE_VIEW).where(STATE_PROVINCE_VIEW.COUNTRY_ISO.eq(countryIso)).list();
    }

    @NotNull public String keyAsString() {
        return Strings.escapeCharOn(countryIso, ':') + ":" + Strings.escapeCharOn(code, ':');
    }

    @NotNull public Tuple<String,String> keyObject() { return Tuple.tuple2(countryIso, code); }

    /** Gets mapper for SQL statements */
    @NotNull public static RowMapper<StateProvinceView> rowMapper() { return STATE_PROVINCE_VIEW.metadata().getRowMapper(); }

    @Override public void invalidate() { country.invalidate(); }

}
