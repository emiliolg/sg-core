package tekgenesis.sales.basic.g;

import tekgenesis.sales.basic.CityView;
import java.util.function.Consumer;
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
import tekgenesis.common.core.Tuple;
import static tekgenesis.sales.basic.g.CityViewTable.CITY_VIEW;
import static tekgenesis.sales.basic.g.StateProvinceViewTable.STATE_PROVINCE_VIEW;
import static tekgenesis.persistence.Sql.selectFrom;

/** 
 * Generated base class for entity: CityView.
 * Don't modify this as this is an auto generated class that's gets generated
 * every time the meta model file is modified. Use subclass instead.
 */
@SuppressWarnings({"DuplicateStringLiteralInspection", "WeakerAccess", "FieldMayBeFinal", "FieldCanBeLocal", "ConstantConditions", "ClassWithTooManyMethods", "ClassWithTooManyFields", "MagicNumber", "MethodOverridesStaticMethodOfSuperclass", "ParameterHidesMemberVariable", "FieldNameHidesFieldInSuperclass", "OverlyComplexClass", "RedundantCast", "UnusedReturnValue"})
public abstract class CityViewBase
    extends EntityInstanceImpl<CityView,Integer>
    implements EntityInstance<CityView,Integer>
{

    //~ Fields ...................................................................................................................

    private int id = EntityTable.DEFAULT_EMPTY_KEY;
    @NotNull String name = "";
    @NotNull String stateProvinceCountryIso = "";
    @NotNull String stateProvinceCode = "";
    @NotNull EntityRef<StateProvinceView,Tuple<String,String>> stateProvince = new EntityRef<>(STATE_PROVINCE_VIEW);
    @NotNull DateTime updateTime = DateTime.EPOCH;

    //~ Methods ..................................................................................................................

    /** Returns the Id. */
    public int getId() { return this.id; }

    @Override public boolean hasEmptyKey() { return id == EntityTable.DEFAULT_EMPTY_KEY; }

    /** Returns the Name. */
    @NotNull public String getName() { return this.name; }

    /** Returns the State Province Country Iso. */
    @NotNull public String getStateProvinceCountryIso() { return this.stateProvinceCountryIso; }

    /** Returns the State Province Code. */
    @NotNull public String getStateProvinceCode() { return this.stateProvinceCode; }

    /** Returns the State Province. */
    @NotNull public StateProvinceView getStateProvince() {
        return stateProvince.solveOrFail(Tuple.tuple2(this.stateProvinceCountryIso, this.stateProvinceCode));
    }

    /** Returns the Update Time. */
    @Override @NotNull public DateTime getUpdateTime() { return this.updateTime; }

    /** Creates a new {@link CityView} instance. */
    @NotNull protected static CityView create() { return new CityView(); }

    @NotNull private static EntityTable<CityView,Integer> myEntityTable() { return EntityTable.forTable(CITY_VIEW); }

    @NotNull public EntityTable<CityView,Integer> et() { return myEntityTable(); }

    @Override @NotNull public DbTable<CityView,Integer> table() { return CITY_VIEW; }

    /** 
     * Try to finds an Object of type 'CityView' in the database.
     * Identified by the primary key.
     * Returns <code>null</code> if is not present
     */
    @Nullable public static CityView find(int id) { return myEntityTable().find(id); }

    /** 
     * Try to finds an Object of type 'CityView' in the database.
     * Identified by the primary key.
     * Throws EntityNotFoundException if is not present
     */
    @NotNull public static CityView findOrFail(int id) { return myEntityTable().findOrFail(id); }

    /** 
     * Try to finds an Object of type 'CityView' in the database.
     * Identified by the primary key.
     * Ignoring caches and accessing the database
     * Returns <code>null</code> if is not present
     */
    @Nullable public static CityView findPersisted(int id) { return myEntityTable().findPersisted(id); }

    /** 
     * Try to finds an Object of type 'CityView' in the database.
     * Identified by the primary key.
     * Ignoring caches and accessing the database
     * Throws EntityNotFoundException if is not present
     */
    @NotNull public static CityView findPersistedOrFail(int id) { return myEntityTable().findPersistedOrFail(id); }

    /** 
     * Try to finds an Object of type 'CityView' in the database.
     * Identified by the primary key.
     * Returns <code>null</code> if is not present
     */
    @Nullable public static CityView find(@NotNull String key) { return myEntityTable().findByString(key); }

    /** 
     * Try to finds an Object of type 'CityView' in the database.
     * That verifies the specified condition.
     * Returns <code>null</code> if is not present
     */
    @Nullable public static CityView findWhere(@NotNull Criteria... condition) { return selectFrom(CITY_VIEW).where(condition).get(); }

    /** Create a selectFrom(CITY_VIEW). */
    @NotNull public static Select<CityView> list() { return selectFrom(CITY_VIEW); }

    /** Performs the given action for each CityView */
    public static void forEach(@Nullable Consumer<CityView> consumer) { selectFrom(CITY_VIEW).forEach(consumer); }

    /** List instances of 'CityView' with the specified keys. */
    @NotNull public static ImmutableList<CityView> list(@Nullable Set<Integer> keys) { return myEntityTable().list(keys); }

    /** List instances of 'CityView' with the specified keys. */
    @NotNull public static ImmutableList<CityView> list(@Nullable Iterable<String> keys) { return myEntityTable().listFromStringKeys(keys); }

    /** List the instances of 'CityView' that verify the specified condition. */
    @NotNull public static Select<CityView> listWhere(@NotNull Criteria condition) { return selectFrom(CITY_VIEW).where(condition); }

    @NotNull public String keyAsString() { return String.valueOf(id); }

    @NotNull public Integer keyObject() { return id; }

    /** Gets mapper for SQL statements */
    @NotNull public static RowMapper<CityView> rowMapper() { return CITY_VIEW.metadata().getRowMapper(); }

    @Override public void invalidate() { stateProvince.invalidate(); }

}
