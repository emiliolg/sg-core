package tekgenesis.showcase.g;

import java.util.function.Consumer;
import tekgenesis.common.util.Conversions;
import tekgenesis.persistence.Criteria;
import tekgenesis.showcase.DNI;
import tekgenesis.common.core.DateTime;
import tekgenesis.persistence.DbTable;
import tekgenesis.persistence.EntityInstanceImpl;
import tekgenesis.persistence.EntityListener;
import tekgenesis.persistence.EntityListenerType;
import tekgenesis.persistence.EntityRef;
import tekgenesis.persistence.EntityTable;
import tekgenesis.common.collections.ImmutableList;
import tekgenesis.common.core.Integers;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import tekgenesis.persistence.PersistableInstance;
import tekgenesis.showcase.PersonWithDni;
import tekgenesis.database.RowMapper;
import tekgenesis.persistence.Select;
import tekgenesis.common.collections.Seq;
import java.util.Set;
import tekgenesis.common.core.Strings;
import static tekgenesis.showcase.g.DNITable.DNI_;
import static tekgenesis.showcase.g.PersonWithDniTable.PERSON_WITH_DNI;
import static tekgenesis.common.util.Conversions.formatList;
import static tekgenesis.persistence.Sql.selectFrom;

/** 
 * Generated base class for entity: PersonWithDni.
 * Don't modify this as this is an auto generated class that's gets generated
 * every time the meta model file is modified. Use subclass instead.
 */
@SuppressWarnings({"DuplicateStringLiteralInspection", "WeakerAccess", "FieldMayBeFinal", "FieldCanBeLocal", "ConstantConditions", "ClassWithTooManyMethods", "ClassWithTooManyFields", "MagicNumber", "MethodOverridesStaticMethodOfSuperclass", "ParameterHidesMemberVariable", "FieldNameHidesFieldInSuperclass", "OverlyComplexClass", "RedundantCast", "UnusedReturnValue"})
public abstract class PersonWithDniBase
    extends EntityInstanceImpl<PersonWithDni,Integer>
    implements PersistableInstance<PersonWithDni,Integer>
{

    //~ Fields ...................................................................................................................

    int dniNumber = 0;
    @NotNull EntityRef<DNI,Integer> dni = new EntityRef<>(DNI_);
    @NotNull String name = "";
    @NotNull String lastname = "";
    @NotNull DateTime updateTime = DateTime.EPOCH;

    //~ Methods ..................................................................................................................

    /** Returns the Dni Number. */
    public int getDniNumber() { return this.dniNumber; }

    /** Returns the Dni. */
    @NotNull public DNI getDni() { return dni.solveOrFail(this.dniNumber); }

    /** Returns the Name. */
    @NotNull public String getName() { return this.name; }

    /** Sets the value of the Name. */
    @NotNull public PersonWithDni setName(@NotNull String name) {
        markAsModified();
        this.name = Strings.truncate(name, 255);
        return (PersonWithDni) this;
    }

    /** Returns the Lastname. */
    @NotNull public String getLastname() { return this.lastname; }

    /** Sets the value of the Lastname. */
    @NotNull public PersonWithDni setLastname(@NotNull String lastname) {
        markAsModified();
        this.lastname = Strings.truncate(lastname, 255);
        return (PersonWithDni) this;
    }

    /** Returns the Update Time. */
    @Override @NotNull public DateTime getUpdateTime() { return this.updateTime; }

    /** Creates a new {@link PersonWithDni} instance. */
    @NotNull public static PersonWithDni create(int dniNumber) {
        final PersonWithDni result = new PersonWithDni();
        ((PersonWithDniBase) result).dniNumber = Integers.checkSignedLength("dniNumber", dniNumber, false, 9);
        return result;
    }

    /** 
     * Creates a new {@link PersonWithDni} instance.
     * Based on String key for Entities
     */
    @NotNull public static PersonWithDni create(@NotNull String dni) { return create(Conversions.toInt(dni)); }

    /** 
     * Find (or create if not present) a 'PersonWithDni' in the database.
     * Identified by the primary key.
     */
    @NotNull public static PersonWithDni findOrCreate(int dniNumber) { return myEntityTable().findOrCreate(dniNumber); }

    /** 
     * Find (or create if not present) a 'PersonWithDni' in the database.
     * Identified by the primary key.
     */
    @NotNull public static PersonWithDni findOrCreate(@NotNull String key) { return myEntityTable().findOrCreateByString(key); }

    @NotNull private static EntityTable<PersonWithDni,Integer> myEntityTable() { return EntityTable.forTable(PERSON_WITH_DNI); }

    @NotNull public EntityTable<PersonWithDni,Integer> et() { return myEntityTable(); }

    @Override @NotNull public DbTable<PersonWithDni,Integer> table() { return PERSON_WITH_DNI; }

    /** 
     * Try to finds an Object of type 'PersonWithDni' in the database.
     * Identified by the primary key.
     * Returns <code>null</code> if is not present
     */
    @Nullable public static PersonWithDni find(int dniNumber) { return myEntityTable().find(dniNumber); }

    /** 
     * Try to finds an Object of type 'PersonWithDni' in the database.
     * Identified by the primary key.
     * Throws EntityNotFoundException if is not present
     */
    @NotNull public static PersonWithDni findOrFail(int dniNumber) { return myEntityTable().findOrFail(dniNumber); }

    /** 
     * Try to finds an Object of type 'PersonWithDni' in the database.
     * Identified by the primary key.
     * Ignoring caches and accessing the database
     * Returns <code>null</code> if is not present
     */
    @Nullable public static PersonWithDni findPersisted(int dniNumber) { return myEntityTable().findPersisted(dniNumber); }

    /** 
     * Try to finds an Object of type 'PersonWithDni' in the database.
     * Identified by the primary key.
     * Ignoring caches and accessing the database
     * Throws EntityNotFoundException if is not present
     */
    @NotNull public static PersonWithDni findPersistedOrFail(int dniNumber) { return myEntityTable().findPersistedOrFail(dniNumber); }

    /** 
     * Try to finds an Object of type 'PersonWithDni' in the database.
     * Identified by the primary key.
     * Returns <code>null</code> if is not present
     */
    @Nullable public static PersonWithDni find(@NotNull String key) { return myEntityTable().findByString(key); }

    /** 
     * Try to finds an Object of type 'PersonWithDni' in the database.
     * That verifies the specified condition.
     * Returns <code>null</code> if is not present
     */
    @Nullable public static PersonWithDni findWhere(@NotNull Criteria... condition) { return selectFrom(PERSON_WITH_DNI).where(condition).get(); }

    /** Create a selectFrom(PERSON_WITH_DNI). */
    @NotNull public static Select<PersonWithDni> list() { return selectFrom(PERSON_WITH_DNI); }

    /** Performs the given action for each PersonWithDni */
    public static void forEach(@Nullable Consumer<PersonWithDni> consumer) { selectFrom(PERSON_WITH_DNI).forEach(consumer); }

    /** List instances of 'PersonWithDni' with the specified keys. */
    @NotNull public static ImmutableList<PersonWithDni> list(@Nullable Set<Integer> keys) { return myEntityTable().list(keys); }

    /** List instances of 'PersonWithDni' with the specified keys. */
    @NotNull public static ImmutableList<PersonWithDni> list(@Nullable Iterable<String> keys) { return myEntityTable().listFromStringKeys(keys); }

    /** List the instances of 'PersonWithDni' that verify the specified condition. */
    @NotNull public static Select<PersonWithDni> listWhere(@NotNull Criteria condition) { return selectFrom(PERSON_WITH_DNI).where(condition); }

    @Override @NotNull public final PersonWithDni update() { return PersistableInstance.super.update(); }

    @Override @NotNull public final PersonWithDni insert() { return PersistableInstance.super.insert(); }

    /** Register a Listener */
    public static void addListener(@NotNull EntityListenerType listenerType, @NotNull EntityListener<PersonWithDni> listener) { myEntityTable().addListener(listenerType, listener); }

    /** Remove a Listener */
    public static void removeListener(@NotNull EntityListenerType listenerType, @NotNull EntityListener<PersonWithDni> listener) { myEntityTable().removeListener(listenerType, listener); }

    @NotNull public String keyAsString() { return String.valueOf(dniNumber); }

    @NotNull public Integer keyObject() { return dniNumber; }

    @Override @NotNull public final Seq<String> describe() { return formatList(getDni()); }

    @Override @NotNull public String toString() { return "" + getDni(); }

    /** Gets mapper for SQL statements */
    @NotNull public static RowMapper<PersonWithDni> rowMapper() { return PERSON_WITH_DNI.metadata().getRowMapper(); }

    @Override public void invalidate() { dni.invalidate(); }

}
