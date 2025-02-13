package tekgenesis.showcase.g;

import java.util.function.Consumer;
import tekgenesis.persistence.Criteria;
import tekgenesis.common.core.DateTime;
import tekgenesis.persistence.DbTable;
import tekgenesis.persistence.EntityInstanceImpl;
import tekgenesis.persistence.EntityListener;
import tekgenesis.persistence.EntityListenerType;
import tekgenesis.persistence.EntityTable;
import tekgenesis.common.collections.ImmutableList;
import tekgenesis.persistence.InnerEntitySeq;
import tekgenesis.showcase.InnerTestProtected;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import tekgenesis.persistence.PersistableInstance;
import tekgenesis.database.RowMapper;
import tekgenesis.persistence.Select;
import tekgenesis.common.collections.Seq;
import java.util.Set;
import tekgenesis.common.core.Strings;
import tekgenesis.showcase.TestProtected;
import static tekgenesis.showcase.g.InnerTestProtectedTable.INNER_TEST_PROTECTED;
import static tekgenesis.showcase.g.TestProtectedTable.TEST_PROTECTED;
import static tekgenesis.persistence.EntitySeq.createInnerEntitySeq;
import static tekgenesis.common.util.Conversions.formatList;
import static tekgenesis.persistence.Sql.selectFrom;

/** 
 * Generated base class for entity: TestProtected.
 * Don't modify this as this is an auto generated class that's gets generated
 * every time the meta model file is modified. Use subclass instead.
 */
@SuppressWarnings({"DuplicateStringLiteralInspection", "WeakerAccess", "FieldMayBeFinal", "FieldCanBeLocal", "ConstantConditions", "ClassWithTooManyMethods", "ClassWithTooManyFields", "MagicNumber", "MethodOverridesStaticMethodOfSuperclass", "ParameterHidesMemberVariable", "FieldNameHidesFieldInSuperclass", "OverlyComplexClass", "RedundantCast", "UnusedReturnValue"})
public abstract class TestProtectedBase
    extends EntityInstanceImpl<TestProtected,Integer>
    implements PersistableInstance<TestProtected,Integer>
{

    //~ Fields ...................................................................................................................

    private int id = EntityTable.DEFAULT_EMPTY_KEY;
    @NotNull String name = "";
    @NotNull String bla = "";
    @NotNull private InnerEntitySeq<InnerTestProtected> inners = createInnerEntitySeq(INNER_TEST_PROTECTED, (TestProtected) this, c -> ((InnerTestProtectedBase)c).testProtected);
    @NotNull DateTime updateTime = DateTime.EPOCH;

    //~ Methods ..................................................................................................................

    /** Returns the Id. */
    public int getId() { return this.id; }

    @Override public boolean hasEmptyKey() { return id == EntityTable.DEFAULT_EMPTY_KEY; }

    /** Returns the Name. */
    @NotNull public String getName() { return this.name; }

    /** Sets the value of the Name. */
    @NotNull public TestProtected setName(@NotNull String name) {
        markAsModified();
        this.name = Strings.truncate(name, 255);
        return (TestProtected) this;
    }

    /** Returns the Bla. */
    @NotNull protected String getBla() { return this.bla; }

    /** Sets the value of the Bla. */
    @NotNull protected TestProtected setBla(@NotNull String bla) {
        markAsModified();
        this.bla = Strings.truncate(bla, 255);
        return (TestProtected) this;
    }

    /** Returns the Inners. */
    @NotNull public InnerEntitySeq<InnerTestProtected> getInners() { return inners; }

    /** Returns the Update Time. */
    @Override @NotNull public DateTime getUpdateTime() { return this.updateTime; }

    /** Creates a new {@link TestProtected} instance. */
    @NotNull public static TestProtected create() { return new TestProtected(); }

    @NotNull private static EntityTable<TestProtected,Integer> myEntityTable() { return EntityTable.forTable(TEST_PROTECTED); }

    @NotNull public EntityTable<TestProtected,Integer> et() { return myEntityTable(); }

    @Override @NotNull public DbTable<TestProtected,Integer> table() { return TEST_PROTECTED; }

    /** 
     * Try to finds an Object of type 'TestProtected' in the database.
     * Identified by the primary key.
     * Returns <code>null</code> if is not present
     */
    @Nullable public static TestProtected find(int id) { return myEntityTable().find(id); }

    /** 
     * Try to finds an Object of type 'TestProtected' in the database.
     * Identified by the primary key.
     * Throws EntityNotFoundException if is not present
     */
    @NotNull public static TestProtected findOrFail(int id) { return myEntityTable().findOrFail(id); }

    /** 
     * Try to finds an Object of type 'TestProtected' in the database.
     * Identified by the primary key.
     * Ignoring caches and accessing the database
     * Returns <code>null</code> if is not present
     */
    @Nullable public static TestProtected findPersisted(int id) { return myEntityTable().findPersisted(id); }

    /** 
     * Try to finds an Object of type 'TestProtected' in the database.
     * Identified by the primary key.
     * Ignoring caches and accessing the database
     * Throws EntityNotFoundException if is not present
     */
    @NotNull public static TestProtected findPersistedOrFail(int id) { return myEntityTable().findPersistedOrFail(id); }

    /** 
     * Try to finds an Object of type 'TestProtected' in the database.
     * Identified by the primary key.
     * Returns <code>null</code> if is not present
     */
    @Nullable public static TestProtected find(@NotNull String key) { return myEntityTable().findByString(key); }

    /** 
     * Try to finds an Object of type 'TestProtected' in the database.
     * That verifies the specified condition.
     * Returns <code>null</code> if is not present
     */
    @Nullable public static TestProtected findWhere(@NotNull Criteria... condition) { return selectFrom(TEST_PROTECTED).where(condition).get(); }

    /** Create a selectFrom(TEST_PROTECTED). */
    @NotNull public static Select<TestProtected> list() { return selectFrom(TEST_PROTECTED); }

    /** Performs the given action for each TestProtected */
    public static void forEach(@Nullable Consumer<TestProtected> consumer) { selectFrom(TEST_PROTECTED).forEach(consumer); }

    /** List instances of 'TestProtected' with the specified keys. */
    @NotNull public static ImmutableList<TestProtected> list(@Nullable Set<Integer> keys) { return myEntityTable().list(keys); }

    /** List instances of 'TestProtected' with the specified keys. */
    @NotNull public static ImmutableList<TestProtected> list(@Nullable Iterable<String> keys) { return myEntityTable().listFromStringKeys(keys); }

    /** List the instances of 'TestProtected' that verify the specified condition. */
    @NotNull public static Select<TestProtected> listWhere(@NotNull Criteria condition) { return selectFrom(TEST_PROTECTED).where(condition); }

    /** Insert specifying the primary key */
    public void insert(int key) {
        this.id = key;
        myEntityTable().insertDoNotGenerate((TestProtected) this);
    }

    /** Register a Listener */
    public static void addListener(@NotNull EntityListenerType listenerType, @NotNull EntityListener<TestProtected> listener) { myEntityTable().addListener(listenerType, listener); }

    /** Remove a Listener */
    public static void removeListener(@NotNull EntityListenerType listenerType, @NotNull EntityListener<TestProtected> listener) { myEntityTable().removeListener(listenerType, listener); }

    @NotNull public String keyAsString() { return String.valueOf(id); }

    @NotNull public Integer keyObject() { return id; }

    @Override @NotNull public final Seq<String> describe() { return formatList(getId()); }

    @Override @NotNull public String toString() { return "" + getId(); }

    /** Gets mapper for SQL statements */
    @NotNull public static RowMapper<TestProtected> rowMapper() { return TEST_PROTECTED.metadata().getRowMapper(); }

    @Override public void invalidate() { inners.invalidate(); }

}
