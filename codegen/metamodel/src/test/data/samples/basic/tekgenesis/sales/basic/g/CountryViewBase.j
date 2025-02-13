package tekgenesis.sales.basic.g;

import java.util.function.Consumer;
import tekgenesis.sales.basic.CountryView;
import tekgenesis.persistence.Criteria;
import tekgenesis.common.core.DateTime;
import tekgenesis.persistence.DbTable;
import tekgenesis.persistence.EntityInstance;
import tekgenesis.persistence.EntityInstanceImpl;
import tekgenesis.persistence.EntityTable;
import tekgenesis.common.collections.ImmutableList;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import tekgenesis.database.RowMapper;
import tekgenesis.persistence.Select;
import tekgenesis.common.core.Strings;
import static tekgenesis.sales.basic.g.CountryViewTable.COUNTRY_VIEW;
import static tekgenesis.persistence.Sql.selectFrom;

/** 
 * Generated base class for entity: CountryView.
 * Don't modify this as this is an auto generated class that's gets generated
 * every time the meta model file is modified. Use subclass instead.
 */
@SuppressWarnings({"DuplicateStringLiteralInspection", "WeakerAccess", "FieldMayBeFinal", "FieldCanBeLocal", "ConstantConditions", "ClassWithTooManyMethods", "ClassWithTooManyFields", "MagicNumber", "MethodOverridesStaticMethodOfSuperclass", "ParameterHidesMemberVariable", "FieldNameHidesFieldInSuperclass", "OverlyComplexClass", "RedundantCast", "UnusedReturnValue"})
public abstract class CountryViewBase
    extends EntityInstanceImpl<CountryView,String>
    implements EntityInstance<CountryView,String>
{

    //~ Fields ...................................................................................................................

    @NotNull String iso = "";
    @NotNull String name = "";
    @NotNull DateTime updateTime = DateTime.EPOCH;

    //~ Methods ..................................................................................................................

    /** Returns the Iso. */
    @NotNull public String getIso() { return this.iso; }

    /** Returns the Name. */
    @NotNull public String getName() { return this.name; }

    /** Returns the Update Time. */
    @Override @NotNull public DateTime getUpdateTime() { return this.updateTime; }

    /** Creates a new {@link CountryView} instance. */
    @NotNull protected static CountryView create(@NotNull String iso) {
        final CountryView result = new CountryView();
        ((CountryViewBase) result).iso = Strings.truncate(iso, 2);
        return result;
    }

    @NotNull private static EntityTable<CountryView,String> myEntityTable() { return EntityTable.forTable(COUNTRY_VIEW); }

    @NotNull public EntityTable<CountryView,String> et() { return myEntityTable(); }

    @Override @NotNull public DbTable<CountryView,String> table() { return COUNTRY_VIEW; }

    /** 
     * Try to finds an Object of type 'CountryView' in the database.
     * Identified by the primary key.
     * Returns <code>null</code> if is not present
     */
    @Nullable public static CountryView find(@NotNull String iso) { return myEntityTable().find(iso); }

    /** 
     * Try to finds an Object of type 'CountryView' in the database.
     * Identified by the primary key.
     * Throws EntityNotFoundException if is not present
     */
    @NotNull public static CountryView findOrFail(@NotNull String iso) { return myEntityTable().findOrFail(iso); }

    /** 
     * Try to finds an Object of type 'CountryView' in the database.
     * Identified by the primary key.
     * Ignoring caches and accessing the database
     * Returns <code>null</code> if is not present
     */
    @Nullable public static CountryView findPersisted(@NotNull String iso) { return myEntityTable().findPersisted(iso); }

    /** 
     * Try to finds an Object of type 'CountryView' in the database.
     * Identified by the primary key.
     * Ignoring caches and accessing the database
     * Throws EntityNotFoundException if is not present
     */
    @NotNull public static CountryView findPersistedOrFail(@NotNull String iso) { return myEntityTable().findPersistedOrFail(iso); }

    /** 
     * Try to finds an Object of type 'CountryView' in the database.
     * That verifies the specified condition.
     * Returns <code>null</code> if is not present
     */
    @Nullable public static CountryView findWhere(@NotNull Criteria... condition) { return selectFrom(COUNTRY_VIEW).where(condition).get(); }

    /** Create a selectFrom(COUNTRY_VIEW). */
    @NotNull public static Select<CountryView> list() { return selectFrom(COUNTRY_VIEW); }

    /** Performs the given action for each CountryView */
    public static void forEach(@Nullable Consumer<CountryView> consumer) { selectFrom(COUNTRY_VIEW).forEach(consumer); }

    /** List instances of 'CountryView' with the specified keys. */
    @NotNull public static ImmutableList<CountryView> list(@Nullable Iterable<String> keys) { return myEntityTable().listFromStringKeys(keys); }

    /** List the instances of 'CountryView' that verify the specified condition. */
    @NotNull public static Select<CountryView> listWhere(@NotNull Criteria condition) { return selectFrom(COUNTRY_VIEW).where(condition); }

    @NotNull public String keyAsString() { return iso; }

    @NotNull public String keyObject() { return iso; }

    /** Gets mapper for SQL statements */
    @NotNull public static RowMapper<CountryView> rowMapper() { return COUNTRY_VIEW.metadata().getRowMapper(); }

}
