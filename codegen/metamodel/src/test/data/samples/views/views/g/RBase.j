package views.g;

import CacheType;
import java.util.function.Consumer;
import tekgenesis.common.util.Conversions;
import tekgenesis.persistence.Criteria;
import tekgenesis.common.core.DateTime;
import tekgenesis.persistence.EntityInstance;
import tekgenesis.persistence.EntityTable;
import tekgenesis.common.collections.ImmutableList;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import views.R;
import views.RSearcher;
import tekgenesis.database.RowMapper;
import tekgenesis.persistence.Select;
import java.util.Set;
import static views.g.RTable.R;
import static tekgenesis.persistence.Sql.selectFrom;

/** 
 * Generated base class for entity: R.
 * Don't modify this as this is an auto generated class that's gets generated
 * every time the meta model file is modified. Use subclass instead.
 */
@SuppressWarnings({"DuplicateStringLiteralInspection", "WeakerAccess", "FieldMayBeFinal", "FieldCanBeLocal", "ConstantConditions", "ClassWithTooManyMethods", "ClassWithTooManyFields", "MagicNumber", "MethodOverridesStaticMethodOfSuperclass", "ParameterHidesMemberVariable", "FieldNameHidesFieldInSuperclass", "OverlyComplexClass", "RedundantCast", "UnusedReturnValue"})
public abstract class RBase
    extends EntityInstance<R,Integer>
{

    //~ Fields ...................................................................................................................

    @NotNull private final R.Data _data = new R.Data();

    //~ Methods ..................................................................................................................

    @NotNull protected final R.Data _data() { return _data; }

    @NotNull protected final R.Data data() { return _data; }

    /** Returns the Id. */
    public int getId() { return _data.id; }

    /** Creates a new {@link R} instance. */
    @NotNull protected static R create() { return new R(); }

    /** Returns the N. */
    @NotNull public String getN() { return _data.n; }

    /** Returns the D. */
    @NotNull public String getD() { return _data.d; }

    /** Returns the Update Time. */
    @Override @NotNull public DateTime getUpdateTime() { return _data.updateTime; }

    @NotNull private static EntityTable<R,Integer> myEntityTable() { return EntityTable.forTable(R); }

    @NotNull public EntityTable<R,Integer> table() { return myEntityTable(); }

    /** 
     * Try to finds an Object of type 'R' in the database.
     * Identified by the primary key.
     * Returns <code>null</code> if is not present
     */
    @Nullable public static R find(int id) { return myEntityTable().find(id); }

    /** 
     * Try to finds an Object of type 'R' in the database.
     * Identified by the primary key.
     * Throws EntityNotFoundException if is not present
     */
    @NotNull public static R findOrFail(int id) { return myEntityTable().findOrFail(id); }

    /** 
     * Try to finds an Object of type 'R' in the database.
     * Identified by the primary key.
     * Ignoring caches and accessing the database
     * Returns <code>null</code> if is not present
     */
    @Nullable public static R findPersisted(int id) { return myEntityTable().findPersisted(id); }

    /** 
     * Try to finds an Object of type 'R' in the database.
     * Identified by the primary key.
     * Ignoring caches and accessing the database
     * Throws EntityNotFoundException if is not present
     */
    @NotNull public static R findPersistedOrFail(int id) { return myEntityTable().findPersistedOrFail(id); }

    /** 
     * Try to finds an Object of type 'R' in the database.
     * Identified by the primary key.
     * Returns <code>null</code> if is not present
     */
    @Nullable public static R find(@NotNull String key) { return myEntityTable().findByString(key); }

    /** 
     * Try to finds an Object of type 'R' in the database.
     * That verifies the specified condition.
     * Returns <code>null</code> if is not present
     */
    @Nullable public static R findWhere(@NotNull Criteria... condition) { return selectFrom(R).where(condition).get(); }

    /** Create a selectFrom(R). */
    @NotNull public static Select<R> list() { return selectFrom(R); }

    /** Performs the given action for each R */
    public static void forEach(@Nullable Consumer<R> consumer) { selectFrom(R).forEach(consumer); }

    /** List instances of 'R' with the specified keys. */
    @NotNull public static ImmutableList<R> list(@Nullable Set<Integer> keys) { return myEntityTable().list(keys); }

    /** List instances of 'R' with the specified keys. */
    @NotNull public static ImmutableList<R> list(@Nullable Iterable<String> keys) { return myEntityTable().listFromStringKeys(keys); }

    /** List the instances of 'R' that verify the specified condition. */
    @NotNull public static Select<R> listWhere(@NotNull Criteria condition) { return selectFrom(R).where(condition); }

    /** List the instances of 'R' that matches the given parameters. */
    @NotNull public static ImmutableList<R> listByN(@NotNull String n) { return selectFrom(R).where(R.N.eq(n)).list(); }

    @NotNull public String keyAsString() { return _data.keyAsString(); }

    @NotNull public static Integer keyFromString(@NotNull String key) { return myEntityTable().keyFromString(key); }

    @NotNull public Integer keyObject() { return _data.keyObject(); }

    public void forceIndex() { myEntityTable().forceIndex((R) this); }

    /** Gets mapper for SQL statements */
    @NotNull public static RowMapper<?> rowMapper() { return myEntityTable().rowMapper(); }

    //~ Inner Classes ............................................................................................................

    @SuppressWarnings("FieldMayBeFinal")
    protected static class Data
        extends EntityInstance.AbstractData<R,Integer>
    {

        //~ Fields ...................................................................................................................

        private int id = EntityTable.DEFAULT_EMPTY_KEY;
        @NotNull private String n = "";
        @NotNull private String d = "";
        @NotNull private DateTime updateTime = DateTime.EPOCH;

        //~ Methods ..................................................................................................................

        @Override public boolean hasEmptyKey() { return id == EntityTable.DEFAULT_EMPTY_KEY; }

        @NotNull public String keyAsString() { return String.valueOf(id); }

        @NotNull public Integer keyObject() { return id; }

    }

    protected static class TableBase
        extends EntityTable<R,Integer>
    {

        //~ Fields ...................................................................................................................

        @NotNull private final RSearcher searcher = new RSearcher();

        //~ Constructors .............................................................................................................

        public TableBase() {
            super(R,CacheType.NONE,"A_SEQ");
            primaryKey(R.ID);
            setRemoteView(true);
        }

        //~ Getters ..................................................................................................................

        /** Returns the Searcher. */
        @Override @NotNull public RSearcher getSearcher() { return searcher; }

        //~ Methods ..................................................................................................................

        @NotNull public R create() { return RBase.create(); }

        protected void setKey(@NotNull R instance, @NotNull Integer key) {
            final R.Data data = ((RBase) instance)._data;
            data.id = key;
        }

        @NotNull public Integer keyFromString(@NotNull String key) { return Conversions.toInt(key); }

        @Override public void insert(@NotNull R instance) { throw new UnsupportedOperationException(); }

    }
}
