package tekgenesis.sales.basic.g;

import java.util.function.Consumer;
import tekgenesis.sales.basic.Country;
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
import tekgenesis.common.core.Strings;
import static tekgenesis.sales.basic.g.CountryTable.COUNTRY;
import static tekgenesis.common.util.Conversions.formatList;
import static tekgenesis.persistence.Sql.selectFrom;

/** 
 * Generated base class for entity: Country.
 * Don't modify this as this is an auto generated class that's gets generated
 * every time the meta model file is modified. Use subclass instead.
 */
@SuppressWarnings({"DuplicateStringLiteralInspection", "WeakerAccess", "FieldMayBeFinal", "FieldCanBeLocal", "ConstantConditions", "ClassWithTooManyMethods", "ClassWithTooManyFields", "MagicNumber", "MethodOverridesStaticMethodOfSuperclass", "ParameterHidesMemberVariable", "FieldNameHidesFieldInSuperclass", "OverlyComplexClass", "RedundantCast", "UnusedReturnValue"})
public abstract class CountryBase
    extends EntityInstanceImpl<Country,String>
    implements PersistableInstance<Country,String>
{

    //~ Fields ...................................................................................................................

    @NotNull String iso2 = "";
    @NotNull String name = "";
    @NotNull DateTime updateTime = DateTime.EPOCH;

    //~ Methods ..................................................................................................................

    /** Returns the Iso2. */
    @NotNull public String getIso2() { return this.iso2; }

    /** Returns the Name. */
    @NotNull public String getName() { return this.name; }

    /** Sets the value of the Name. */
    @NotNull public Country setName(@NotNull String name) {
        markAsModified();
        this.name = Strings.truncate(name, 30);
        return (Country) this;
    }

    /** Returns the Update Time. */
    @Override @NotNull public DateTime getUpdateTime() { return this.updateTime; }

    /** Creates a new {@link Country} instance. */
    @NotNull public static Country create(@NotNull String iso2) {
        final Country result = new Country();
        ((CountryBase) result).iso2 = Strings.truncate(iso2, 2);
        return result;
    }

    /** 
     * Find (or create if not present) a 'Country' in the database.
     * Identified by the primary key.
     */
    @NotNull public static Country findOrCreate(@NotNull String iso2) { return myEntityTable().findOrCreate(iso2); }

    @NotNull private static EntityTable<Country,String> myEntityTable() { return EntityTable.forTable(COUNTRY); }

    @NotNull public EntityTable<Country,String> et() { return myEntityTable(); }

    @Override @NotNull public DbTable<Country,String> table() { return COUNTRY; }

    /** 
     * Try to finds an Object of type 'Country' in the database.
     * Identified by the primary key.
     * Returns <code>null</code> if is not present
     */
    @Nullable public static Country find(@NotNull String iso2) { return myEntityTable().find(iso2); }

    /** 
     * Try to finds an Object of type 'Country' in the database.
     * Identified by the primary key.
     * Throws EntityNotFoundException if is not present
     */
    @NotNull public static Country findOrFail(@NotNull String iso2) { return myEntityTable().findOrFail(iso2); }

    /** 
     * Try to finds an Object of type 'Country' in the database.
     * Identified by the primary key.
     * Ignoring caches and accessing the database
     * Returns <code>null</code> if is not present
     */
    @Nullable public static Country findPersisted(@NotNull String iso2) { return myEntityTable().findPersisted(iso2); }

    /** 
     * Try to finds an Object of type 'Country' in the database.
     * Identified by the primary key.
     * Ignoring caches and accessing the database
     * Throws EntityNotFoundException if is not present
     */
    @NotNull public static Country findPersistedOrFail(@NotNull String iso2) { return myEntityTable().findPersistedOrFail(iso2); }

    /** 
     * Try to finds an Object of type 'Country' in the database.
     * That verifies the specified condition.
     * Returns <code>null</code> if is not present
     */
    @Nullable public static Country findWhere(@NotNull Criteria... condition) { return selectFrom(COUNTRY).where(condition).get(); }

    /** Create a selectFrom(COUNTRY). */
    @NotNull public static Select<Country> list() { return selectFrom(COUNTRY); }

    /** Performs the given action for each Country */
    public static void forEach(@Nullable Consumer<Country> consumer) { selectFrom(COUNTRY).forEach(consumer); }

    /** List instances of 'Country' with the specified keys. */
    @NotNull public static ImmutableList<Country> list(@Nullable Iterable<String> keys) { return myEntityTable().listFromStringKeys(keys); }

    /** List the instances of 'Country' that verify the specified condition. */
    @NotNull public static Select<Country> listWhere(@NotNull Criteria condition) { return selectFrom(COUNTRY).where(condition); }

    @Override @NotNull public final Country update() { return PersistableInstance.super.update(); }

    @Override @NotNull public final Country insert() { return PersistableInstance.super.insert(); }

    /** Register a Listener */
    public static void addListener(@NotNull EntityListenerType listenerType, @NotNull EntityListener<Country> listener) { myEntityTable().addListener(listenerType, listener); }

    /** Remove a Listener */
    public static void removeListener(@NotNull EntityListenerType listenerType, @NotNull EntityListener<Country> listener) { myEntityTable().removeListener(listenerType, listener); }

    @NotNull public String keyAsString() { return iso2; }

    @NotNull public String keyObject() { return iso2; }

    @Override @NotNull public final Seq<String> describe() { return formatList(getIso2()); }

    @Override @NotNull public String toString() { return "" + getIso2(); }

    /** Gets mapper for SQL statements */
    @NotNull public static RowMapper<Country> rowMapper() { return COUNTRY.metadata().getRowMapper(); }

}
