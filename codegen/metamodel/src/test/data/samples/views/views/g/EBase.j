package views.g;

import CacheType;
import java.util.function.Consumer;
import tekgenesis.common.util.Conversions;
import tekgenesis.persistence.Criteria;
import tekgenesis.common.core.DateTime;
import views.E;
import views.ESearcher;
import tekgenesis.persistence.EntityInstance;
import tekgenesis.persistence.EntityListener;
import tekgenesis.persistence.EntityListenerType;
import tekgenesis.persistence.EntityRef;
import tekgenesis.persistence.EntityTable;
import tekgenesis.common.collections.ImmutableList;
import tekgenesis.common.core.Integers;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import tekgenesis.persistence.PersistableInstance;
import views.R;
import tekgenesis.database.RowMapper;
import tekgenesis.persistence.Select;
import tekgenesis.common.collections.Seq;
import java.util.Set;
import tekgenesis.common.core.Strings;
import static views.g.ETable.E;
import static views.g.RTable.R;
import static tekgenesis.persistence.Sql.selectFrom;
import static tekgenesis.common.core.Strings.strList;

/** 
 * Generated base class for entity: E.
 * Don't modify this as this is an auto generated class that's gets generated
 * every time the meta model file is modified. Use subclass instead.
 */
@SuppressWarnings({"DuplicateStringLiteralInspection", "WeakerAccess", "FieldMayBeFinal", "FieldCanBeLocal", "ConstantConditions", "ClassWithTooManyMethods", "ClassWithTooManyFields", "MagicNumber", "MethodOverridesStaticMethodOfSuperclass", "ParameterHidesMemberVariable", "FieldNameHidesFieldInSuperclass", "OverlyComplexClass", "RedundantCast", "UnusedReturnValue"})
public abstract class EBase
    extends PersistableInstance<E,Integer>
{

    //~ Fields ...................................................................................................................

    @NotNull private final E.Data _data = new E.Data();
    @NotNull private EntityRef<R,Integer> r = new EntityRef<>(R);

    //~ Methods ..................................................................................................................

    @NotNull protected final E.Data _data() { return _data; }

    @NotNull protected final E.Data data() { return _data; }

    /** Returns the R Id. */
    public int getRId() { return _data.rId; }

    /** Returns the R. */
    @NotNull public R getR() { return r.solveOrFail(_data.rId); }

    /** Creates a new {@link E} instance. */
    @NotNull public static E create(int rId) {
        final E result = new E();
        ((EBase) result)._data.rId = Integers.checkSignedLength(rId, false, 9);
        return result;
    }

    /** 
     * Creates a new {@link E} instance.
     * Based on String key for Entities
     */
    @NotNull public static E create(@NotNull String r) { return create(Conversions.toInt(r)); }

    /** Returns the Desc. */
    @NotNull public String getDesc() { return _data.desc; }

    /** Sets the value of the Desc. */
    @NotNull public E setDesc(@NotNull String desc) {
        markAsModified(_data);
        _data.desc = Strings.truncate(desc, 255);
        return (E) this;
    }

    /** Returns the Update Time. */
    @Override @NotNull public DateTime getUpdateTime() { return _data.updateTime; }

    @NotNull private static EntityTable<E,Integer> myEntityTable() { return EntityTable.forTable(E); }

    @NotNull public EntityTable<E,Integer> table() { return myEntityTable(); }

    /** 
     * Try to finds an Object of type 'E' in the database.
     * Identified by the primary key.
     * Returns <code>null</code> if is not present
     */
    @Nullable public static E find(int rId) { return myEntityTable().find(rId); }

    /** 
     * Try to finds an Object of type 'E' in the database.
     * Identified by the primary key.
     * Throws EntityNotFoundException if is not present
     */
    @NotNull public static E findOrFail(int rId) { return myEntityTable().findOrFail(rId); }

    /** 
     * Try to finds an Object of type 'E' in the database.
     * Identified by the primary key.
     * Ignoring caches and accessing the database
     * Returns <code>null</code> if is not present
     */
    @Nullable public static E findPersisted(int rId) { return myEntityTable().findPersisted(rId); }

    /** 
     * Try to finds an Object of type 'E' in the database.
     * Identified by the primary key.
     * Ignoring caches and accessing the database
     * Throws EntityNotFoundException if is not present
     */
    @NotNull public static E findPersistedOrFail(int rId) { return myEntityTable().findPersistedOrFail(rId); }

    /** 
     * Try to finds an Object of type 'E' in the database.
     * Identified by the primary key.
     * Returns <code>null</code> if is not present
     */
    @Nullable public static E find(@NotNull String key) { return myEntityTable().findByString(key); }

    /** 
     * Try to finds an Object of type 'E' in the database.
     * That verifies the specified condition.
     * Returns <code>null</code> if is not present
     */
    @Nullable public static E findWhere(@NotNull Criteria... condition) { return selectFrom(E).where(condition).get(); }

    /** Create a selectFrom(E). */
    @NotNull public static Select<E> list() { return selectFrom(E); }

    /** Performs the given action for each E */
    public static void forEach(@Nullable Consumer<E> consumer) { selectFrom(E).forEach(consumer); }

    /** List instances of 'E' with the specified keys. */
    @NotNull public static ImmutableList<E> list(@Nullable Set<Integer> keys) { return myEntityTable().list(keys); }

    /** List instances of 'E' with the specified keys. */
    @NotNull public static ImmutableList<E> list(@Nullable Iterable<String> keys) { return myEntityTable().listFromStringKeys(keys); }

    /** List the instances of 'E' that verify the specified condition. */
    @NotNull public static Select<E> listWhere(@NotNull Criteria condition) { return selectFrom(E).where(condition); }

    @Override @NotNull public final E update() { return super.update(); }

    @Override @NotNull public final E insert() { return super.insert(); }

    /** Register a Listener */
    public static void addListener(@NotNull EntityListenerType listenerType, @NotNull EntityListener<E> listener) { myEntityTable().addListener(listenerType, listener); }

    /** Remove a Listener */
    public static void removeListener(@NotNull EntityListenerType listenerType, @NotNull EntityListener<E> listener) { myEntityTable().removeListener(listenerType, listener); }

    /** 
     * Find (or create if not present) a 'E' in the database.
     * Identified by the primary key.
     */
    @NotNull public static E findOrCreate(int rId) { return myEntityTable().findOrCreate(rId); }

    /** 
     * Find (or create if not present) a 'E' in the database.
     * Identified by the primary key.
     */
    @NotNull public static E findOrCreate(@NotNull String key) { return myEntityTable().findOrCreateByString(key); }

    @NotNull public String keyAsString() { return _data.keyAsString(); }

    @NotNull public static Integer keyFromString(@NotNull String key) { return myEntityTable().keyFromString(key); }

    @NotNull public Integer keyObject() { return _data.keyObject(); }

    public void forceIndex() { myEntityTable().forceIndex((E) this); }

    @Override @NotNull public Seq<String> describe() { return strList(getR()); }

    @Override @NotNull public String toString() { return "" + getR(); }

    /** Gets mapper for SQL statements */
    @NotNull public static RowMapper<?> rowMapper() { return myEntityTable().rowMapper(); }

    @Override public void invalidate() { r.invalidate(); }

    //~ Inner Classes ............................................................................................................

    @SuppressWarnings("FieldMayBeFinal")
    protected static class Data
        extends EntityInstance.AbstractData<E,Integer>
    {

        //~ Fields ...................................................................................................................

        private int rId = 0;
        @NotNull private String desc = "";
        @NotNull private DateTime updateTime = DateTime.EPOCH;

        //~ Methods ..................................................................................................................

        @NotNull public String keyAsString() { return String.valueOf(rId); }

        @NotNull public Integer keyObject() { return rId; }

    }

    protected static class TableBase
        extends EntityTable<E,Integer>
    {

        //~ Fields ...................................................................................................................

        @NotNull private final ESearcher searcher = new ESearcher();

        //~ Constructors .............................................................................................................

        public TableBase() {
            super(E,CacheType.NONE);
            primaryKey(E.R_ID);
        }

        //~ Getters ..................................................................................................................

        /** Returns the Searcher. */
        @Override @NotNull public ESearcher getSearcher() { return searcher; }

        //~ Methods ..................................................................................................................

        @NotNull public E create(@NotNull Integer key) { return EBase.create(key); }

        protected void setKey(@NotNull E instance, @NotNull Integer key) {
            final E.Data data = ((EBase) instance)._data;
            data.rId = key;
        }

        @NotNull public Integer keyFromString(@NotNull String key) { return Conversions.toInt(key); }

    }
}
