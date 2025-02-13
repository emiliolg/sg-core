package tekgenesis.showcase.g;

import java.util.function.Consumer;
import tekgenesis.common.util.Conversions;
import tekgenesis.persistence.Criteria;
import tekgenesis.common.core.DateTime;
import tekgenesis.persistence.DbTable;
import tekgenesis.persistence.EntityInstanceImpl;
import tekgenesis.persistence.EntityListener;
import tekgenesis.persistence.EntityListenerType;
import tekgenesis.persistence.EntityRef;
import tekgenesis.persistence.EntityTable;
import tekgenesis.common.collections.ImmutableList;
import tekgenesis.persistence.InnerEntitySeq;
import tekgenesis.persistence.InnerInstance;
import tekgenesis.showcase.InnerTestProtected;
import tekgenesis.common.core.Integers;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import tekgenesis.database.RowMapper;
import tekgenesis.persistence.Select;
import tekgenesis.common.collections.Seq;
import java.util.Set;
import tekgenesis.common.core.Strings;
import tekgenesis.showcase.TestProtected;
import tekgenesis.common.core.Tuple;
import static tekgenesis.showcase.g.InnerTestProtectedTable.INNER_TEST_PROTECTED;
import static tekgenesis.showcase.g.TestProtectedTable.TEST_PROTECTED;
import static tekgenesis.common.util.Conversions.formatList;
import static tekgenesis.persistence.Sql.selectFrom;

/** 
 * Generated base class for entity: InnerTestProtected.
 * Don't modify this as this is an auto generated class that's gets generated
 * every time the meta model file is modified. Use subclass instead.
 */
@SuppressWarnings({"DuplicateStringLiteralInspection", "WeakerAccess", "FieldMayBeFinal", "FieldCanBeLocal", "ConstantConditions", "ClassWithTooManyMethods", "ClassWithTooManyFields", "MagicNumber", "MethodOverridesStaticMethodOfSuperclass", "ParameterHidesMemberVariable", "FieldNameHidesFieldInSuperclass", "OverlyComplexClass", "RedundantCast", "UnusedReturnValue"})
public abstract class InnerTestProtectedBase
    extends EntityInstanceImpl<InnerTestProtected,Tuple<Integer,Integer>>
    implements InnerInstance<InnerTestProtected,Tuple<Integer,Integer>,TestProtected,Integer>
{

    //~ Fields ...................................................................................................................

    int testProtectedId = 0;
    @NotNull EntityRef<TestProtected,Integer> testProtected = new EntityRef<>(TEST_PROTECTED, TestProtected::getInners);
    int seqId = 0;
    @NotNull String desc = "";
    int length = 0;
    @NotNull DateTime updateTime = DateTime.EPOCH;

    //~ Methods ..................................................................................................................

    /** Returns the Test Protected Id. */
    public int getTestProtectedId() { return this.testProtectedId; }

    /** Returns the Test Protected. */
    @NotNull public TestProtected getTestProtected() { return testProtected.solveOrFail(this.testProtectedId); }

    /** Returns the Seq Id. */
    public int getSeqId() { return this.seqId; }

    @Override @NotNull public EntityRef<TestProtected,Integer> parent() { return testProtected; }

    @Override @NotNull public InnerEntitySeq<InnerTestProtected> siblings() { return getTestProtected().getInners(); }

    @Override public int seqId() { return getSeqId(); }

    /** Returns the Desc. */
    @NotNull public String getDesc() { return this.desc; }

    /** Sets the value of the Desc. */
    @NotNull public InnerTestProtected setDesc(@NotNull String desc) {
        markAsModified();
        this.desc = Strings.truncate(desc, 255);
        return (InnerTestProtected) this;
    }

    /** Returns the Length. */
    protected int getLength() { return this.length; }

    /** Sets the value of the Length. */
    @NotNull protected InnerTestProtected setLength(int length) {
        markAsModified();
        this.length = Integers.checkSignedLength("length", length, false, 9);
        return (InnerTestProtected) this;
    }

    /** Returns the Update Time. */
    @Override @NotNull public DateTime getUpdateTime() { return this.updateTime; }

    @NotNull private static EntityTable<InnerTestProtected,Tuple<Integer,Integer>> myEntityTable() { return EntityTable.forTable(INNER_TEST_PROTECTED); }

    @NotNull public EntityTable<InnerTestProtected,Tuple<Integer,Integer>> et() { return myEntityTable(); }

    @Override @NotNull public DbTable<InnerTestProtected,Tuple<Integer,Integer>> table() { return INNER_TEST_PROTECTED; }

    /** 
     * Try to finds an Object of type 'InnerTestProtected' in the database.
     * Identified by the primary key.
     * Returns <code>null</code> if is not present
     */
    @Nullable public static InnerTestProtected find(@NotNull Tuple<Integer,Integer> key) { return myEntityTable().find(key); }

    /** 
     * Try to finds an Object of type 'InnerTestProtected' in the database.
     * Identified by the primary key.
     * Throws EntityNotFoundException if is not present
     */
    @NotNull public static InnerTestProtected findOrFail(@NotNull Tuple<Integer,Integer> key) { return myEntityTable().findOrFail(key); }

    /** 
     * Try to finds an Object of type 'InnerTestProtected' in the database.
     * Identified by the primary key.
     * Ignoring caches and accessing the database
     * Returns <code>null</code> if is not present
     */
    @Nullable public static InnerTestProtected findPersisted(@NotNull Tuple<Integer,Integer> key) { return myEntityTable().findPersisted(key); }

    /** 
     * Try to finds an Object of type 'InnerTestProtected' in the database.
     * Identified by the primary key.
     * Ignoring caches and accessing the database
     * Throws EntityNotFoundException if is not present
     */
    @NotNull public static InnerTestProtected findPersistedOrFail(@NotNull Tuple<Integer,Integer> key) { return myEntityTable().findPersistedOrFail(key); }

    /** 
     * Try to finds an Object of type 'InnerTestProtected' in the database.
     * Identified by the primary key.
     * Returns <code>null</code> if is not present
     */
    @Nullable public static InnerTestProtected find(@NotNull String key) { return myEntityTable().findByString(key); }

    /** 
     * Try to finds an Object of type 'InnerTestProtected' in the database.
     * Identified by the primary key.
     * Returns <code>null</code> if is not present
     */
    @Nullable public static InnerTestProtected find(int testProtectedId, int seqId) { return find(Tuple.tuple2(testProtectedId, seqId)); }

    /** 
     * Try to finds an Object of type 'InnerTestProtected' in the database.
     * Identified by the primary key.
     * Returns <code>null</code> if is not present
     * Based on String key for Entities
     */
    @Nullable public static InnerTestProtected find(@NotNull String testProtected, int seqId) { return find(Conversions.toInt(testProtected), seqId); }

    /** 
     * Try to finds an Object of type 'InnerTestProtected' in the database.
     * That verifies the specified condition.
     * Returns <code>null</code> if is not present
     */
    @Nullable public static InnerTestProtected findWhere(@NotNull Criteria... condition) {
        return selectFrom(INNER_TEST_PROTECTED).where(condition).get();
    }

    /** Create a selectFrom(INNER_TEST_PROTECTED). */
    @NotNull public static Select<InnerTestProtected> list() { return selectFrom(INNER_TEST_PROTECTED); }

    /** Performs the given action for each InnerTestProtected */
    public static void forEach(@Nullable Consumer<InnerTestProtected> consumer) { selectFrom(INNER_TEST_PROTECTED).forEach(consumer); }

    /** List instances of 'InnerTestProtected' with the specified keys. */
    @NotNull public static ImmutableList<InnerTestProtected> list(@Nullable Set<Tuple<Integer,Integer>> keys) { return myEntityTable().list(keys); }

    /** List instances of 'InnerTestProtected' with the specified keys. */
    @NotNull public static ImmutableList<InnerTestProtected> list(@Nullable Iterable<String> keys) { return myEntityTable().listFromStringKeys(keys); }

    /** List the instances of 'InnerTestProtected' that verify the specified condition. */
    @NotNull public static Select<InnerTestProtected> listWhere(@NotNull Criteria condition) { return selectFrom(INNER_TEST_PROTECTED).where(condition); }

    @Override @NotNull public final InnerTestProtected update() { return InnerInstance.super.update(); }

    @Override @NotNull public final InnerTestProtected insert() { return InnerInstance.super.insert(); }

    /** Register a Listener */
    public static void addListener(@NotNull EntityListenerType listenerType, @NotNull EntityListener<InnerTestProtected> listener) { myEntityTable().addListener(listenerType, listener); }

    /** Remove a Listener */
    public static void removeListener(@NotNull EntityListenerType listenerType, @NotNull EntityListener<InnerTestProtected> listener) { myEntityTable().removeListener(listenerType, listener); }

    @NotNull public String keyAsString() { return testProtectedId + ":" + seqId; }

    @NotNull public Tuple<Integer,Integer> keyObject() { return Tuple.tuple2(testProtectedId, seqId); }

    @Override @NotNull public final Seq<String> describe() { return formatList(getTestProtected(), getSeqId()); }

    @Override @NotNull public String toString() { return "" + getTestProtected() + " " + getSeqId(); }

    /** Gets mapper for SQL statements */
    @NotNull public static RowMapper<InnerTestProtected> rowMapper() { return INNER_TEST_PROTECTED.metadata().getRowMapper(); }

    @Override public void invalidate() { testProtected.invalidate(); }

}
