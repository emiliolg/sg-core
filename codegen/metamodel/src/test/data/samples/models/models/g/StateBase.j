package models.g;

import java.util.function.Consumer;
import models.Country;
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
import models.State;
import tekgenesis.common.core.Strings;
import tekgenesis.common.core.Tuple;
import static models.g.CountryTable.COUNTRY;
import static models.g.StateTable.STATE;
import static tekgenesis.common.util.Conversions.formatList;
import static tekgenesis.persistence.Sql.selectFrom;

/** 
 * Generated base class for entity: State.
 * Don't modify this as this is an auto generated class that's gets generated
 * every time the meta model file is modified. Use subclass instead.
 */
@SuppressWarnings({"DuplicateStringLiteralInspection", "WeakerAccess", "FieldMayBeFinal", "FieldCanBeLocal", "ConstantConditions", "ClassWithTooManyMethods", "ClassWithTooManyFields", "MagicNumber", "MethodOverridesStaticMethodOfSuperclass", "ParameterHidesMemberVariable", "FieldNameHidesFieldInSuperclass", "OverlyComplexClass", "RedundantCast", "UnusedReturnValue"})
public abstract class StateBase
    extends EntityInstanceImpl<State,Tuple<String,String>>
    implements PersistableInstance<State,Tuple<String,String>>
{

    //~ Fields ...................................................................................................................

    @NotNull String countryCode = "";
    @NotNull EntityRef<Country,String> country = new EntityRef<>(COUNTRY);
    @NotNull String code = "";
    @NotNull String description = "";
    @NotNull DateTime updateTime = DateTime.EPOCH;

    //~ Methods ..................................................................................................................

    /** Returns the Country Code. */
    @NotNull public String getCountryCode() { return this.countryCode; }

    /** Returns the Country. */
    @NotNull public Country getCountry() { return country.solveOrFail(this.countryCode); }

    /** Returns the Code. */
    @NotNull public String getCode() { return this.code; }

    /** Returns the Description. */
    @NotNull public String getDescription() { return this.description; }

    /** Sets the value of the Description. */
    @NotNull public State setDescription(@NotNull String description) {
        markAsModified();
        this.description = Strings.truncate(description, 40);
        return (State) this;
    }

    /** Returns the Update Time. */
    @Override @NotNull public DateTime getUpdateTime() { return this.updateTime; }

    /** Creates a new {@link State} instance. */
    @NotNull public static State create(@NotNull String countryCode, @NotNull String code) {
        final State result = new State();
        ((StateBase) result).countryCode = Strings.truncate(countryCode, 2);
        ((StateBase) result).code = Strings.truncate(code, 2);
        return result;
    }

    /** 
     * Creates a new {@link State} instance.
     * Based on the primary key object
     */
    @NotNull public static State create(@NotNull Tuple<String,String> key) { return create(key._1(), key._2()); }

    /** 
     * Find (or create if not present) a 'State' in the database.
     * Identified by the primary key.
     */
    @NotNull public static State findOrCreate(@NotNull Tuple<String,String> key) { return myEntityTable().findOrCreate(key); }

    /** 
     * Find (or create if not present) a 'State' in the database.
     * Identified by the primary key.
     */
    @NotNull public static State findOrCreate(@NotNull String key) { return myEntityTable().findOrCreateByString(key); }

    /** 
     * Find (or create if not present) a 'State' in the database.
     * Identified by the primary key.
     */
    @NotNull public static State findOrCreate(@NotNull String countryCode, @NotNull String code) { return findOrCreate(Tuple.tuple2(countryCode, code)); }

    @NotNull private static EntityTable<State,Tuple<String,String>> myEntityTable() { return EntityTable.forTable(STATE); }

    @NotNull public EntityTable<State,Tuple<String,String>> et() { return myEntityTable(); }

    @Override @NotNull public DbTable<State,Tuple<String,String>> table() { return STATE; }

    /** 
     * Try to finds an Object of type 'State' in the database.
     * Identified by the primary key.
     * Returns <code>null</code> if is not present
     */
    @Nullable public static State find(@NotNull Tuple<String,String> key) { return myEntityTable().find(key); }

    /** 
     * Try to finds an Object of type 'State' in the database.
     * Identified by the primary key.
     * Throws EntityNotFoundException if is not present
     */
    @NotNull public static State findOrFail(@NotNull Tuple<String,String> key) { return myEntityTable().findOrFail(key); }

    /** 
     * Try to finds an Object of type 'State' in the database.
     * Identified by the primary key.
     * Ignoring caches and accessing the database
     * Returns <code>null</code> if is not present
     */
    @Nullable public static State findPersisted(@NotNull Tuple<String,String> key) { return myEntityTable().findPersisted(key); }

    /** 
     * Try to finds an Object of type 'State' in the database.
     * Identified by the primary key.
     * Ignoring caches and accessing the database
     * Throws EntityNotFoundException if is not present
     */
    @NotNull public static State findPersistedOrFail(@NotNull Tuple<String,String> key) { return myEntityTable().findPersistedOrFail(key); }

    /** 
     * Try to finds an Object of type 'State' in the database.
     * Identified by the primary key.
     * Returns <code>null</code> if is not present
     */
    @Nullable public static State find(@NotNull String key) { return myEntityTable().findByString(key); }

    /** 
     * Try to finds an Object of type 'State' in the database.
     * Identified by the primary key.
     * Returns <code>null</code> if is not present
     */
    @Nullable public static State find(@NotNull String countryCode, @NotNull String code) { return find(Tuple.tuple2(countryCode, code)); }

    /** 
     * Try to finds an Object of type 'State' in the database.
     * That verifies the specified condition.
     * Returns <code>null</code> if is not present
     */
    @Nullable public static State findWhere(@NotNull Criteria... condition) { return selectFrom(STATE).where(condition).get(); }

    /** Create a selectFrom(STATE). */
    @NotNull public static Select<State> list() { return selectFrom(STATE); }

    /** Performs the given action for each State */
    public static void forEach(@Nullable Consumer<State> consumer) { selectFrom(STATE).forEach(consumer); }

    /** List instances of 'State' with the specified keys. */
    @NotNull public static ImmutableList<State> list(@Nullable Set<Tuple<String,String>> keys) { return myEntityTable().list(keys); }

    /** List instances of 'State' with the specified keys. */
    @NotNull public static ImmutableList<State> list(@Nullable Iterable<String> keys) { return myEntityTable().listFromStringKeys(keys); }

    /** List the instances of 'State' that verify the specified condition. */
    @NotNull public static Select<State> listWhere(@NotNull Criteria condition) { return selectFrom(STATE).where(condition); }

    @Override @NotNull public final State update() { return PersistableInstance.super.update(); }

    @Override @NotNull public final State insert() { return PersistableInstance.super.insert(); }

    /** Register a Listener */
    public static void addListener(@NotNull EntityListenerType listenerType, @NotNull EntityListener<State> listener) { myEntityTable().addListener(listenerType, listener); }

    /** Remove a Listener */
    public static void removeListener(@NotNull EntityListenerType listenerType, @NotNull EntityListener<State> listener) { myEntityTable().removeListener(listenerType, listener); }

    @NotNull public String keyAsString() {
        return Strings.escapeCharOn(countryCode, ':') + ":" + Strings.escapeCharOn(code, ':');
    }

    @NotNull public Tuple<String,String> keyObject() { return Tuple.tuple2(countryCode, code); }

    @Override @NotNull public final Seq<String> describe() { return formatList(getCountry(), getCode()); }

    @Override @NotNull public String toString() { return "" + getCountry() + " " + getCode(); }

    /** Gets mapper for SQL statements */
    @NotNull public static RowMapper<State> rowMapper() { return STATE.metadata().getRowMapper(); }

    @Override public void invalidate() { country.invalidate(); }

}
