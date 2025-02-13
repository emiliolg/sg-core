package tekgenesis.sales.basic.g;

import java.util.function.Consumer;
import tekgenesis.persistence.Criteria;
import tekgenesis.common.core.DateTime;
import tekgenesis.persistence.DbTable;
import tekgenesis.persistence.EntityInstanceImpl;
import tekgenesis.persistence.EntityListener;
import tekgenesis.persistence.EntityListenerType;
import tekgenesis.persistence.EntityTable;
import tekgenesis.common.collections.ImmutableList;
import tekgenesis.common.core.Integers;
import tekgenesis.sales.basic.LongKey;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import tekgenesis.persistence.PersistableInstance;
import tekgenesis.database.RowMapper;
import tekgenesis.persistence.Select;
import tekgenesis.common.collections.Seq;
import java.util.Set;
import tekgenesis.common.core.Tuple;
import tekgenesis.common.core.Tuple6;
import static tekgenesis.sales.basic.g.LongKeyTable.LONG_KEY;
import static tekgenesis.common.util.Conversions.formatList;
import static tekgenesis.persistence.Sql.selectFrom;

/** 
 * Generated base class for entity: LongKey.
 * Don't modify this as this is an auto generated class that's gets generated
 * every time the meta model file is modified. Use subclass instead.
 */
@SuppressWarnings({"DuplicateStringLiteralInspection", "WeakerAccess", "FieldMayBeFinal", "FieldCanBeLocal", "ConstantConditions", "ClassWithTooManyMethods", "ClassWithTooManyFields", "MagicNumber", "MethodOverridesStaticMethodOfSuperclass", "ParameterHidesMemberVariable", "FieldNameHidesFieldInSuperclass", "OverlyComplexClass", "RedundantCast", "UnusedReturnValue"})
public abstract class LongKeyBase
    extends EntityInstanceImpl<LongKey,Tuple6<Integer,Integer,Integer,Integer,Integer,Integer>>
    implements PersistableInstance<LongKey,Tuple6<Integer,Integer,Integer,Integer,Integer,Integer>>
{

    //~ Fields ...................................................................................................................

    int a1 = 0;
    int a2 = 0;
    int a3 = 0;
    int a4 = 0;
    int a5 = 0;
    int a6 = 0;
    int a7 = 0;
    @NotNull DateTime updateTime = DateTime.EPOCH;

    //~ Methods ..................................................................................................................

    /** Returns the A1. */
    public int getA1() { return this.a1; }

    /** Returns the A2. */
    public int getA2() { return this.a2; }

    /** Returns the A3. */
    public int getA3() { return this.a3; }

    /** Returns the A4. */
    public int getA4() { return this.a4; }

    /** Returns the A5. */
    public int getA5() { return this.a5; }

    /** Returns the A6. */
    public int getA6() { return this.a6; }

    /** Returns the A7. */
    public int getA7() { return this.a7; }

    /** Sets the value of the A7. */
    @NotNull public LongKey setA7(int a7) {
        markAsModified();
        this.a7 = Integers.checkSignedLength("a7", a7, false, 9);
        return (LongKey) this;
    }

    /** Returns the Update Time. */
    @Override @NotNull public DateTime getUpdateTime() { return this.updateTime; }

    /** Creates a new {@link LongKey} instance. */
    @NotNull public static LongKey create(int a1, int a2, int a3, int a4, int a5, int a6) {
        final LongKey result = new LongKey();
        ((LongKeyBase) result).a1 = Integers.checkSignedLength("a1", a1, false, 9);
        ((LongKeyBase) result).a2 = Integers.checkSignedLength("a2", a2, false, 9);
        ((LongKeyBase) result).a3 = Integers.checkSignedLength("a3", a3, false, 9);
        ((LongKeyBase) result).a4 = Integers.checkSignedLength("a4", a4, false, 9);
        ((LongKeyBase) result).a5 = Integers.checkSignedLength("a5", a5, false, 9);
        ((LongKeyBase) result).a6 = Integers.checkSignedLength("a6", a6, false, 9);
        return result;
    }

    /** 
     * Creates a new {@link LongKey} instance.
     * Based on the primary key object
     */
    @NotNull public static LongKey create(@NotNull Tuple6<Integer,Integer,Integer,Integer,Integer,Integer> key) {
        return create(key._1(), key._2(), key._3(), key._4(), key._5(), key._6());
    }

    /** 
     * Find (or create if not present) a 'LongKey' in the database.
     * Identified by the primary key.
     */
    @NotNull public static LongKey findOrCreate(@NotNull Tuple6<Integer,Integer,Integer,Integer,Integer,Integer> key) { return myEntityTable().findOrCreate(key); }

    /** 
     * Find (or create if not present) a 'LongKey' in the database.
     * Identified by the primary key.
     */
    @NotNull public static LongKey findOrCreate(@NotNull String key) { return myEntityTable().findOrCreateByString(key); }

    /** 
     * Find (or create if not present) a 'LongKey' in the database.
     * Identified by the primary key.
     */
    @NotNull public static LongKey findOrCreate(int a1, int a2, int a3, int a4, int a5, int a6) { return findOrCreate(Tuple.tuple(a1, a2, a3, a4, a5, a6)); }

    @NotNull private static EntityTable<LongKey,Tuple6<Integer,Integer,Integer,Integer,Integer,Integer>> myEntityTable() { return EntityTable.forTable(LONG_KEY); }

    @NotNull public EntityTable<LongKey,Tuple6<Integer,Integer,Integer,Integer,Integer,Integer>> et() { return myEntityTable(); }

    @Override @NotNull public DbTable<LongKey,Tuple6<Integer,Integer,Integer,Integer,Integer,Integer>> table() { return LONG_KEY; }

    /** 
     * Try to finds an Object of type 'LongKey' in the database.
     * Identified by the primary key.
     * Returns <code>null</code> if is not present
     */
    @Nullable public static LongKey find(@NotNull Tuple6<Integer,Integer,Integer,Integer,Integer,Integer> key) { return myEntityTable().find(key); }

    /** 
     * Try to finds an Object of type 'LongKey' in the database.
     * Identified by the primary key.
     * Throws EntityNotFoundException if is not present
     */
    @NotNull public static LongKey findOrFail(@NotNull Tuple6<Integer,Integer,Integer,Integer,Integer,Integer> key) { return myEntityTable().findOrFail(key); }

    /** 
     * Try to finds an Object of type 'LongKey' in the database.
     * Identified by the primary key.
     * Ignoring caches and accessing the database
     * Returns <code>null</code> if is not present
     */
    @Nullable public static LongKey findPersisted(@NotNull Tuple6<Integer,Integer,Integer,Integer,Integer,Integer> key) { return myEntityTable().findPersisted(key); }

    /** 
     * Try to finds an Object of type 'LongKey' in the database.
     * Identified by the primary key.
     * Ignoring caches and accessing the database
     * Throws EntityNotFoundException if is not present
     */
    @NotNull public static LongKey findPersistedOrFail(@NotNull Tuple6<Integer,Integer,Integer,Integer,Integer,Integer> key) { return myEntityTable().findPersistedOrFail(key); }

    /** 
     * Try to finds an Object of type 'LongKey' in the database.
     * Identified by the primary key.
     * Returns <code>null</code> if is not present
     */
    @Nullable public static LongKey find(@NotNull String key) { return myEntityTable().findByString(key); }

    /** 
     * Try to finds an Object of type 'LongKey' in the database.
     * Identified by the primary key.
     * Returns <code>null</code> if is not present
     */
    @Nullable public static LongKey find(int a1, int a2, int a3, int a4, int a5, int a6) { return find(Tuple.tuple(a1, a2, a3, a4, a5, a6)); }

    /** 
     * Try to finds an Object of type 'LongKey' in the database.
     * That verifies the specified condition.
     * Returns <code>null</code> if is not present
     */
    @Nullable public static LongKey findWhere(@NotNull Criteria... condition) { return selectFrom(LONG_KEY).where(condition).get(); }

    /** Create a selectFrom(LONG_KEY). */
    @NotNull public static Select<LongKey> list() { return selectFrom(LONG_KEY); }

    /** Performs the given action for each LongKey */
    public static void forEach(@Nullable Consumer<LongKey> consumer) { selectFrom(LONG_KEY).forEach(consumer); }

    /** List instances of 'LongKey' with the specified keys. */
    @NotNull public static ImmutableList<LongKey> list(@Nullable Set<Tuple6<Integer,Integer,Integer,Integer,Integer,Integer>> keys) { return myEntityTable().list(keys); }

    /** List instances of 'LongKey' with the specified keys. */
    @NotNull public static ImmutableList<LongKey> list(@Nullable Iterable<String> keys) { return myEntityTable().listFromStringKeys(keys); }

    /** List the instances of 'LongKey' that verify the specified condition. */
    @NotNull public static Select<LongKey> listWhere(@NotNull Criteria condition) { return selectFrom(LONG_KEY).where(condition); }

    @Override @NotNull public final LongKey update() { return PersistableInstance.super.update(); }

    @Override @NotNull public final LongKey insert() { return PersistableInstance.super.insert(); }

    /** Register a Listener */
    public static void addListener(@NotNull EntityListenerType listenerType, @NotNull EntityListener<LongKey> listener) { myEntityTable().addListener(listenerType, listener); }

    /** Remove a Listener */
    public static void removeListener(@NotNull EntityListenerType listenerType, @NotNull EntityListener<LongKey> listener) { myEntityTable().removeListener(listenerType, listener); }

    @NotNull public String keyAsString() {
        return a1 + ":" + a2 + ":" + a3 + ":" + a4 + ":" + a5 + ":" + a6;
    }

    @NotNull public Tuple6<Integer,Integer,Integer,Integer,Integer,Integer> keyObject() { return Tuple.tuple(a1, a2, a3, a4, a5, a6); }

    @Override @NotNull public final Seq<String> describe() {
        return formatList(getA1(), getA2(), getA3(), getA4(), getA5(), getA6());
    }

    @Override @NotNull public String toString() {
        return "" + getA1() + " " + getA2() + " " + getA3() + " " + getA4() + " " + getA5() + " " + getA6();
    }

    /** Gets mapper for SQL statements */
    @NotNull public static RowMapper<LongKey> rowMapper() { return LONG_KEY.metadata().getRowMapper(); }

}
