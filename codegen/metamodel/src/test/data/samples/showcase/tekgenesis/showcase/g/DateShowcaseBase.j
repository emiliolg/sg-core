package tekgenesis.showcase.g;

import java.util.function.Consumer;
import tekgenesis.persistence.Criteria;
import tekgenesis.common.core.DateOnly;
import tekgenesis.showcase.DateShowcase;
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
import static tekgenesis.showcase.g.DateShowcaseTable.DATE_SHOWCASE;
import static tekgenesis.common.util.Conversions.formatList;
import static tekgenesis.persistence.Sql.selectFrom;

/** 
 * Generated base class for entity: DateShowcase.
 * Don't modify this as this is an auto generated class that's gets generated
 * every time the meta model file is modified. Use subclass instead.
 */
@SuppressWarnings({"DuplicateStringLiteralInspection", "WeakerAccess", "FieldMayBeFinal", "FieldCanBeLocal", "ConstantConditions", "ClassWithTooManyMethods", "ClassWithTooManyFields", "MagicNumber", "MethodOverridesStaticMethodOfSuperclass", "ParameterHidesMemberVariable", "FieldNameHidesFieldInSuperclass", "OverlyComplexClass", "RedundantCast", "UnusedReturnValue"})
public abstract class DateShowcaseBase
    extends EntityInstanceImpl<DateShowcase,Integer>
    implements PersistableInstance<DateShowcase,Integer>
{

    //~ Fields ...................................................................................................................

    int idKey = 0;
    @Nullable DateOnly dateFrom = null;
    @Nullable DateOnly dateTo = null;
    @Nullable DateTime timeFrom = null;
    @Nullable DateTime timeTo = null;
    @Nullable DateOnly doubleDateFrom = null;
    @Nullable DateOnly doubleDateTo = null;
    @Nullable DateOnly dateCombo = null;
    @Nullable DateOnly dateCombo1 = null;
    @NotNull DateTime updateTime = DateTime.EPOCH;

    //~ Methods ..................................................................................................................

    /** Returns the Id Key. */
    public int getIdKey() { return this.idKey; }

    /** Returns the Date From. */
    @Nullable public DateOnly getDateFrom() { return this.dateFrom; }

    /** Sets the value of the Date From. */
    @NotNull public DateShowcase setDateFrom(@Nullable DateOnly dateFrom) {
        markAsModified();
        this.dateFrom = dateFrom;
        return (DateShowcase) this;
    }

    /** Returns the Date To. */
    @Nullable public DateOnly getDateTo() { return this.dateTo; }

    /** Sets the value of the Date To. */
    @NotNull public DateShowcase setDateTo(@Nullable DateOnly dateTo) {
        markAsModified();
        this.dateTo = dateTo;
        return (DateShowcase) this;
    }

    /** Returns the Time From. */
    @Nullable public DateTime getTimeFrom() { return this.timeFrom; }

    /** Sets the value of the Time From. */
    @NotNull public DateShowcase setTimeFrom(@Nullable DateTime timeFrom) {
        markAsModified();
        this.timeFrom = timeFrom;
        return (DateShowcase) this;
    }

    /** Returns the Time To. */
    @Nullable public DateTime getTimeTo() { return this.timeTo; }

    /** Sets the value of the Time To. */
    @NotNull public DateShowcase setTimeTo(@Nullable DateTime timeTo) {
        markAsModified();
        this.timeTo = timeTo;
        return (DateShowcase) this;
    }

    /** Returns the Double Date From. */
    @Nullable public DateOnly getDoubleDateFrom() { return this.doubleDateFrom; }

    /** Sets the value of the Double Date From. */
    @NotNull public DateShowcase setDoubleDateFrom(@Nullable DateOnly doubleDateFrom) {
        markAsModified();
        this.doubleDateFrom = doubleDateFrom;
        return (DateShowcase) this;
    }

    /** Returns the Double Date To. */
    @Nullable public DateOnly getDoubleDateTo() { return this.doubleDateTo; }

    /** Sets the value of the Double Date To. */
    @NotNull public DateShowcase setDoubleDateTo(@Nullable DateOnly doubleDateTo) {
        markAsModified();
        this.doubleDateTo = doubleDateTo;
        return (DateShowcase) this;
    }

    /** Returns the Date Combo. */
    @Nullable public DateOnly getDateCombo() { return this.dateCombo; }

    /** Sets the value of the Date Combo. */
    @NotNull public DateShowcase setDateCombo(@Nullable DateOnly dateCombo) {
        markAsModified();
        this.dateCombo = dateCombo;
        return (DateShowcase) this;
    }

    /** Returns the Date Combo1. */
    @Nullable public DateOnly getDateCombo1() { return this.dateCombo1; }

    /** Sets the value of the Date Combo1. */
    @NotNull public DateShowcase setDateCombo1(@Nullable DateOnly dateCombo1) {
        markAsModified();
        this.dateCombo1 = dateCombo1;
        return (DateShowcase) this;
    }

    /** Returns the Update Time. */
    @Override @NotNull public DateTime getUpdateTime() { return this.updateTime; }

    /** Creates a new {@link DateShowcase} instance. */
    @NotNull public static DateShowcase create(int idKey) {
        final DateShowcase result = new DateShowcase();
        ((DateShowcaseBase) result).idKey = Integers.checkSignedLength("idKey", idKey, false, 9);
        return result;
    }

    /** 
     * Find (or create if not present) a 'DateShowcase' in the database.
     * Identified by the primary key.
     */
    @NotNull public static DateShowcase findOrCreate(int idKey) { return myEntityTable().findOrCreate(idKey); }

    /** 
     * Find (or create if not present) a 'DateShowcase' in the database.
     * Identified by the primary key.
     */
    @NotNull public static DateShowcase findOrCreate(@NotNull String key) { return myEntityTable().findOrCreateByString(key); }

    @NotNull private static EntityTable<DateShowcase,Integer> myEntityTable() { return EntityTable.forTable(DATE_SHOWCASE); }

    @NotNull public EntityTable<DateShowcase,Integer> et() { return myEntityTable(); }

    @Override @NotNull public DbTable<DateShowcase,Integer> table() { return DATE_SHOWCASE; }

    /** 
     * Try to finds an Object of type 'DateShowcase' in the database.
     * Identified by the primary key.
     * Returns <code>null</code> if is not present
     */
    @Nullable public static DateShowcase find(int idKey) { return myEntityTable().find(idKey); }

    /** 
     * Try to finds an Object of type 'DateShowcase' in the database.
     * Identified by the primary key.
     * Throws EntityNotFoundException if is not present
     */
    @NotNull public static DateShowcase findOrFail(int idKey) { return myEntityTable().findOrFail(idKey); }

    /** 
     * Try to finds an Object of type 'DateShowcase' in the database.
     * Identified by the primary key.
     * Ignoring caches and accessing the database
     * Returns <code>null</code> if is not present
     */
    @Nullable public static DateShowcase findPersisted(int idKey) { return myEntityTable().findPersisted(idKey); }

    /** 
     * Try to finds an Object of type 'DateShowcase' in the database.
     * Identified by the primary key.
     * Ignoring caches and accessing the database
     * Throws EntityNotFoundException if is not present
     */
    @NotNull public static DateShowcase findPersistedOrFail(int idKey) { return myEntityTable().findPersistedOrFail(idKey); }

    /** 
     * Try to finds an Object of type 'DateShowcase' in the database.
     * Identified by the primary key.
     * Returns <code>null</code> if is not present
     */
    @Nullable public static DateShowcase find(@NotNull String key) { return myEntityTable().findByString(key); }

    /** 
     * Try to finds an Object of type 'DateShowcase' in the database.
     * That verifies the specified condition.
     * Returns <code>null</code> if is not present
     */
    @Nullable public static DateShowcase findWhere(@NotNull Criteria... condition) { return selectFrom(DATE_SHOWCASE).where(condition).get(); }

    /** Create a selectFrom(DATE_SHOWCASE). */
    @NotNull public static Select<DateShowcase> list() { return selectFrom(DATE_SHOWCASE); }

    /** Performs the given action for each DateShowcase */
    public static void forEach(@Nullable Consumer<DateShowcase> consumer) { selectFrom(DATE_SHOWCASE).forEach(consumer); }

    /** List instances of 'DateShowcase' with the specified keys. */
    @NotNull public static ImmutableList<DateShowcase> list(@Nullable Set<Integer> keys) { return myEntityTable().list(keys); }

    /** List instances of 'DateShowcase' with the specified keys. */
    @NotNull public static ImmutableList<DateShowcase> list(@Nullable Iterable<String> keys) { return myEntityTable().listFromStringKeys(keys); }

    /** List the instances of 'DateShowcase' that verify the specified condition. */
    @NotNull public static Select<DateShowcase> listWhere(@NotNull Criteria condition) { return selectFrom(DATE_SHOWCASE).where(condition); }

    @Override @NotNull public final DateShowcase update() { return PersistableInstance.super.update(); }

    @Override @NotNull public final DateShowcase insert() { return PersistableInstance.super.insert(); }

    /** Register a Listener */
    public static void addListener(@NotNull EntityListenerType listenerType, @NotNull EntityListener<DateShowcase> listener) { myEntityTable().addListener(listenerType, listener); }

    /** Remove a Listener */
    public static void removeListener(@NotNull EntityListenerType listenerType, @NotNull EntityListener<DateShowcase> listener) { myEntityTable().removeListener(listenerType, listener); }

    @NotNull public String keyAsString() { return String.valueOf(idKey); }

    @NotNull public Integer keyObject() { return idKey; }

    @Override @NotNull public final Seq<String> describe() { return formatList(getIdKey()); }

    @Override @NotNull public String toString() { return "" + getIdKey(); }

    /** Gets mapper for SQL statements */
    @NotNull public static RowMapper<DateShowcase> rowMapper() { return DATE_SHOWCASE.metadata().getRowMapper(); }

}
