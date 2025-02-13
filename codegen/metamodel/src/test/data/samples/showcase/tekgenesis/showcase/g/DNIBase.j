package tekgenesis.showcase.g;

import java.util.function.Consumer;
import tekgenesis.persistence.Criteria;
import tekgenesis.showcase.DNI;
import tekgenesis.common.core.DateTime;
import tekgenesis.persistence.DbTable;
import tekgenesis.persistence.EntityInstanceImpl;
import tekgenesis.persistence.EntityListener;
import tekgenesis.persistence.EntityListenerType;
import tekgenesis.persistence.EntityTable;
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
import static tekgenesis.showcase.g.DNITable.DNI_;
import static tekgenesis.common.util.Conversions.formatList;
import static tekgenesis.persistence.Sql.selectFrom;

/** 
 * Generated base class for entity: DNI.
 * Don't modify this as this is an auto generated class that's gets generated
 * every time the meta model file is modified. Use subclass instead.
 */
@SuppressWarnings({"DuplicateStringLiteralInspection", "WeakerAccess", "FieldMayBeFinal", "FieldCanBeLocal", "ConstantConditions", "ClassWithTooManyMethods", "ClassWithTooManyFields", "MagicNumber", "MethodOverridesStaticMethodOfSuperclass", "ParameterHidesMemberVariable", "FieldNameHidesFieldInSuperclass", "OverlyComplexClass", "RedundantCast", "UnusedReturnValue"})
public abstract class DNIBase
    extends EntityInstanceImpl<DNI,Integer>
    implements PersistableInstance<DNI,Integer>
{

    //~ Fields ...................................................................................................................

    int number = 0;
    @NotNull String descr = "";
    @NotNull DateTime updateTime = DateTime.EPOCH;

    //~ Methods ..................................................................................................................

    /** Returns the Number. */
    public int getNumber() { return this.number; }

    /** Returns the Descr. */
    @NotNull public String getDescr() { return this.descr; }

    /** Sets the value of the Descr. */
    @NotNull public DNI setDescr(@NotNull String descr) {
        markAsModified();
        this.descr = Strings.truncate(descr, 255);
        return (DNI) this;
    }

    /** Returns the Update Time. */
    @Override @NotNull public DateTime getUpdateTime() { return this.updateTime; }

    /** Creates a new {@link DNI} instance. */
    @NotNull public static DNI create(int number) {
        final DNI result = new DNI();
        ((DNIBase) result).number = Integers.checkSignedLength("number", number, false, 9);
        return result;
    }

    /** 
     * Find (or create if not present) a 'DNI' in the database.
     * Identified by the primary key.
     */
    @NotNull public static DNI findOrCreate(int number) { return myEntityTable().findOrCreate(number); }

    /** 
     * Find (or create if not present) a 'DNI' in the database.
     * Identified by the primary key.
     */
    @NotNull public static DNI findOrCreate(@NotNull String key) { return myEntityTable().findOrCreateByString(key); }

    @NotNull private static EntityTable<DNI,Integer> myEntityTable() { return EntityTable.forTable(DNI_); }

    @NotNull public EntityTable<DNI,Integer> et() { return myEntityTable(); }

    @Override @NotNull public DbTable<DNI,Integer> table() { return DNI_; }

    /** 
     * Try to finds an Object of type 'DNI' in the database.
     * Identified by the primary key.
     * Returns <code>null</code> if is not present
     */
    @Nullable public static DNI find(int number) { return myEntityTable().find(number); }

    /** 
     * Try to finds an Object of type 'DNI' in the database.
     * Identified by the primary key.
     * Throws EntityNotFoundException if is not present
     */
    @NotNull public static DNI findOrFail(int number) { return myEntityTable().findOrFail(number); }

    /** 
     * Try to finds an Object of type 'DNI' in the database.
     * Identified by the primary key.
     * Ignoring caches and accessing the database
     * Returns <code>null</code> if is not present
     */
    @Nullable public static DNI findPersisted(int number) { return myEntityTable().findPersisted(number); }

    /** 
     * Try to finds an Object of type 'DNI' in the database.
     * Identified by the primary key.
     * Ignoring caches and accessing the database
     * Throws EntityNotFoundException if is not present
     */
    @NotNull public static DNI findPersistedOrFail(int number) { return myEntityTable().findPersistedOrFail(number); }

    /** 
     * Try to finds an Object of type 'DNI' in the database.
     * Identified by the primary key.
     * Returns <code>null</code> if is not present
     */
    @Nullable public static DNI find(@NotNull String key) { return myEntityTable().findByString(key); }

    /** 
     * Try to finds an Object of type 'DNI' in the database.
     * That verifies the specified condition.
     * Returns <code>null</code> if is not present
     */
    @Nullable public static DNI findWhere(@NotNull Criteria... condition) { return selectFrom(DNI_).where(condition).get(); }

    /** Create a selectFrom(DNI_). */
    @NotNull public static Select<DNI> list() { return selectFrom(DNI_); }

    /** Performs the given action for each DNI */
    public static void forEach(@Nullable Consumer<DNI> consumer) { selectFrom(DNI_).forEach(consumer); }

    /** List instances of 'DNI' with the specified keys. */
    @NotNull public static ImmutableList<DNI> list(@Nullable Set<Integer> keys) { return myEntityTable().list(keys); }

    /** List instances of 'DNI' with the specified keys. */
    @NotNull public static ImmutableList<DNI> list(@Nullable Iterable<String> keys) { return myEntityTable().listFromStringKeys(keys); }

    /** List the instances of 'DNI' that verify the specified condition. */
    @NotNull public static Select<DNI> listWhere(@NotNull Criteria condition) { return selectFrom(DNI_).where(condition); }

    @Override @NotNull public final DNI update() { return PersistableInstance.super.update(); }

    @Override @NotNull public final DNI insert() { return PersistableInstance.super.insert(); }

    /** Register a Listener */
    public static void addListener(@NotNull EntityListenerType listenerType, @NotNull EntityListener<DNI> listener) { myEntityTable().addListener(listenerType, listener); }

    /** Remove a Listener */
    public static void removeListener(@NotNull EntityListenerType listenerType, @NotNull EntityListener<DNI> listener) { myEntityTable().removeListener(listenerType, listener); }

    @NotNull public String keyAsString() { return String.valueOf(number); }

    @NotNull public Integer keyObject() { return number; }

    @Override @NotNull public final Seq<String> describe() { return formatList(getNumber()); }

    @Override @NotNull public String toString() { return "" + getNumber(); }

    /** Gets mapper for SQL statements */
    @NotNull public static RowMapper<DNI> rowMapper() { return DNI_.metadata().getRowMapper(); }

}
