package models.g;

import java.util.function.Consumer;
import models.Country;
import tekgenesis.persistence.Criteria;
import tekgenesis.common.core.DateTime;
import tekgenesis.persistence.DbTable;
import tekgenesis.persistence.DeprecableInstance;
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
import static models.g.CountryTable.COUNTRY;
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
    implements PersistableInstance<Country,String>, DeprecableInstance<Country,String>
{

    //~ Fields ...................................................................................................................

    @NotNull String code = "";
    @NotNull String description = "";
    @NotNull DateTime updateTime = DateTime.EPOCH;
    @Nullable DateTime deprecationTime = null;
    @Nullable String deprecationUser = null;

    //~ Methods ..................................................................................................................

    /** Returns the Code. */
    @NotNull public String getCode() { return this.code; }

    /** Returns the Description. */
    @NotNull public String getDescription() { return this.description; }

    /** Sets the value of the Description. */
    @NotNull public Country setDescription(@NotNull String description) {
        markAsModified();
        this.description = Strings.truncate(description, 40);
        return (Country) this;
    }

    /** Returns the Update Time. */
    @Override @NotNull public DateTime getUpdateTime() { return this.updateTime; }

    /** Returns the Deprecation Time. */
    @Nullable public DateTime getDeprecationTime() { return this.deprecationTime; }

    /** Returns the Deprecation User. */
    @Nullable public String getDeprecationUser() { return this.deprecationUser; }

    /** Creates a new {@link Country} instance. */
    @NotNull public static Country create(@NotNull String code) {
        final Country result = new Country();
        ((CountryBase) result).code = Strings.truncate(code, 2);
        return result;
    }

    /** 
     * Find (or create if not present) a 'Country' in the database.
     * Identified by the primary key.
     */
    @NotNull public static Country findOrCreate(@NotNull String code) { return myEntityTable().findOrCreate(code); }

    @NotNull private static EntityTable<Country,String> myEntityTable() { return EntityTable.forTable(COUNTRY); }

    @NotNull public EntityTable<Country,String> et() { return myEntityTable(); }

    @Override @NotNull public DbTable<Country,String> table() { return COUNTRY; }

    /** 
     * Try to finds an Object of type 'Country' in the database.
     * Identified by the primary key.
     * Returns <code>null</code> if is not present
     */
    @Nullable public static Country find(@NotNull String code) { return myEntityTable().find(code); }

    /** 
     * Try to finds an Object of type 'Country' in the database.
     * Identified by the primary key.
     * Throws EntityNotFoundException if is not present
     */
    @NotNull public static Country findOrFail(@NotNull String code) { return myEntityTable().findOrFail(code); }

    /** 
     * Try to finds an Object of type 'Country' in the database.
     * Identified by the primary key.
     * Ignoring caches and accessing the database
     * Returns <code>null</code> if is not present
     */
    @Nullable public static Country findPersisted(@NotNull String code) { return myEntityTable().findPersisted(code); }

    /** 
     * Try to finds an Object of type 'Country' in the database.
     * Identified by the primary key.
     * Ignoring caches and accessing the database
     * Throws EntityNotFoundException if is not present
     */
    @NotNull public static Country findPersistedOrFail(@NotNull String code) { return myEntityTable().findPersistedOrFail(code); }

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

    @NotNull public String keyAsString() { return code; }

    @NotNull public String keyObject() { return code; }

    @Override @NotNull public final Seq<String> describe() { return formatList(getCode()); }

    @Override @NotNull public String toString() { return "" + getCode(); }

    /** Gets mapper for SQL statements */
    @NotNull public static RowMapper<Country> rowMapper() { return COUNTRY.metadata().getRowMapper(); }

}
