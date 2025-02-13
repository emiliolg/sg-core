package tekgenesis.sales.basic.g;

import tekgenesis.sales.basic.CategoryComposite;
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
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import tekgenesis.persistence.PersistableInstance;
import tekgenesis.database.RowMapper;
import tekgenesis.persistence.Select;
import tekgenesis.common.collections.Seq;
import java.util.Set;
import tekgenesis.common.core.Strings;
import tekgenesis.common.core.Tuple;
import tekgenesis.common.core.Tuple3;
import static tekgenesis.sales.basic.g.CategoryCompositeTable.CATEGORY_COMPOSITE;
import static tekgenesis.common.util.Conversions.formatList;
import static tekgenesis.persistence.Sql.selectFrom;

/** 
 * Category entity
 * <b>Searchable: </b> using CategorySearcher, indexing fields idKey and name
 * 
 * Generated base class for entity: CategoryComposite.
 * Don't modify this as this is an auto generated class that's gets generated
 * every time the meta model file is modified. Use subclass instead.
 */
@SuppressWarnings({"DuplicateStringLiteralInspection", "WeakerAccess", "FieldMayBeFinal", "FieldCanBeLocal", "ConstantConditions", "ClassWithTooManyMethods", "ClassWithTooManyFields", "MagicNumber", "MethodOverridesStaticMethodOfSuperclass", "ParameterHidesMemberVariable", "FieldNameHidesFieldInSuperclass", "OverlyComplexClass", "RedundantCast", "UnusedReturnValue"})
public abstract class CategoryCompositeBase
    extends EntityInstanceImpl<CategoryComposite,Tuple3<Long,String,String>>
    implements PersistableInstance<CategoryComposite,Tuple3<Long,String,String>>
{

    //~ Fields ...................................................................................................................

    long idKey = 0;
    @NotNull String descr = "";
    @NotNull String shortDesc = "";
    @NotNull String name = "";
    @NotNull DateTime updateTime = DateTime.EPOCH;

    //~ Methods ..................................................................................................................

    /** Returns the Id Key. */
    public long getIdKey() { return this.idKey; }

    /** Returns the Descr. */
    @NotNull public String getDescr() { return this.descr; }

    /** Returns the Short Desc. */
    @NotNull public String getShortDesc() { return this.shortDesc; }

    /** Returns the Name. */
    @NotNull public String getName() { return this.name; }

    /** Sets the value of the Name. */
    @NotNull public CategoryComposite setName(@NotNull String name) {
        markAsModified();
        this.name = Strings.truncate(name, 30);
        return (CategoryComposite) this;
    }

    /** Returns the Update Time. */
    @Override @NotNull public DateTime getUpdateTime() { return this.updateTime; }

    /** Creates a new {@link CategoryComposite} instance. */
    @NotNull public static CategoryComposite create(long idKey, @NotNull String descr, @NotNull String shortDesc) {
        final CategoryComposite result = new CategoryComposite();
        ((CategoryCompositeBase) result).idKey = Integers.checkSignedLength("idKey", idKey, false, 18);
        ((CategoryCompositeBase) result).descr = Strings.truncate(descr, 120);
        ((CategoryCompositeBase) result).shortDesc = Strings.truncate(shortDesc, 120);
        return result;
    }

    /** 
     * Creates a new {@link CategoryComposite} instance.
     * Based on the primary key object
     */
    @NotNull public static CategoryComposite create(@NotNull Tuple3<Long,String,String> key) { return create(key._1(), key._2(), key._3()); }

    /** 
     * Find (or create if not present) a 'CategoryComposite' in the database.
     * Identified by the primary key.
     */
    @NotNull public static CategoryComposite findOrCreate(@NotNull Tuple3<Long,String,String> key) { return myEntityTable().findOrCreate(key); }

    /** 
     * Find (or create if not present) a 'CategoryComposite' in the database.
     * Identified by the primary key.
     */
    @NotNull public static CategoryComposite findOrCreate(@NotNull String key) { return myEntityTable().findOrCreateByString(key); }

    /** 
     * Find (or create if not present) a 'CategoryComposite' in the database.
     * Identified by the primary key.
     */
    @NotNull public static CategoryComposite findOrCreate(long idKey, @NotNull String descr, @NotNull String shortDesc) { return findOrCreate(Tuple.tuple(idKey, descr, shortDesc)); }

    @NotNull private static EntityTable<CategoryComposite,Tuple3<Long,String,String>> myEntityTable() { return EntityTable.forTable(CATEGORY_COMPOSITE); }

    @NotNull public EntityTable<CategoryComposite,Tuple3<Long,String,String>> et() { return myEntityTable(); }

    @Override @NotNull public DbTable<CategoryComposite,Tuple3<Long,String,String>> table() { return CATEGORY_COMPOSITE; }

    /** 
     * Try to finds an Object of type 'CategoryComposite' in the database.
     * Identified by the primary key.
     * Returns <code>null</code> if is not present
     */
    @Nullable public static CategoryComposite find(@NotNull Tuple3<Long,String,String> key) { return myEntityTable().find(key); }

    /** 
     * Try to finds an Object of type 'CategoryComposite' in the database.
     * Identified by the primary key.
     * Throws EntityNotFoundException if is not present
     */
    @NotNull public static CategoryComposite findOrFail(@NotNull Tuple3<Long,String,String> key) { return myEntityTable().findOrFail(key); }

    /** 
     * Try to finds an Object of type 'CategoryComposite' in the database.
     * Identified by the primary key.
     * Ignoring caches and accessing the database
     * Returns <code>null</code> if is not present
     */
    @Nullable public static CategoryComposite findPersisted(@NotNull Tuple3<Long,String,String> key) { return myEntityTable().findPersisted(key); }

    /** 
     * Try to finds an Object of type 'CategoryComposite' in the database.
     * Identified by the primary key.
     * Ignoring caches and accessing the database
     * Throws EntityNotFoundException if is not present
     */
    @NotNull public static CategoryComposite findPersistedOrFail(@NotNull Tuple3<Long,String,String> key) { return myEntityTable().findPersistedOrFail(key); }

    /** 
     * Try to finds an Object of type 'CategoryComposite' in the database.
     * Identified by the primary key.
     * Returns <code>null</code> if is not present
     */
    @Nullable public static CategoryComposite find(@NotNull String key) { return myEntityTable().findByString(key); }

    /** 
     * Try to finds an Object of type 'CategoryComposite' in the database.
     * Identified by the primary key.
     * Returns <code>null</code> if is not present
     */
    @Nullable public static CategoryComposite find(long idKey, @NotNull String descr, @NotNull String shortDesc) { return find(Tuple.tuple(idKey, descr, shortDesc)); }

    /** 
     * Try to finds an Object of type 'CategoryComposite' in the database.
     * That verifies the specified condition.
     * Returns <code>null</code> if is not present
     */
    @Nullable public static CategoryComposite findWhere(@NotNull Criteria... condition) {
        return selectFrom(CATEGORY_COMPOSITE).where(condition).get();
    }

    /** Create a selectFrom(CATEGORY_COMPOSITE). */
    @NotNull public static Select<CategoryComposite> list() { return selectFrom(CATEGORY_COMPOSITE); }

    /** Performs the given action for each CategoryComposite */
    public static void forEach(@Nullable Consumer<CategoryComposite> consumer) { selectFrom(CATEGORY_COMPOSITE).forEach(consumer); }

    /** List instances of 'CategoryComposite' with the specified keys. */
    @NotNull public static ImmutableList<CategoryComposite> list(@Nullable Set<Tuple3<Long,String,String>> keys) { return myEntityTable().list(keys); }

    /** List instances of 'CategoryComposite' with the specified keys. */
    @NotNull public static ImmutableList<CategoryComposite> list(@Nullable Iterable<String> keys) { return myEntityTable().listFromStringKeys(keys); }

    /** List the instances of 'CategoryComposite' that verify the specified condition. */
    @NotNull public static Select<CategoryComposite> listWhere(@NotNull Criteria condition) { return selectFrom(CATEGORY_COMPOSITE).where(condition); }

    @Override @NotNull public final CategoryComposite update() { return PersistableInstance.super.update(); }

    @Override @NotNull public final CategoryComposite insert() { return PersistableInstance.super.insert(); }

    /** Register a Listener */
    public static void addListener(@NotNull EntityListenerType listenerType, @NotNull EntityListener<CategoryComposite> listener) { myEntityTable().addListener(listenerType, listener); }

    /** Remove a Listener */
    public static void removeListener(@NotNull EntityListenerType listenerType, @NotNull EntityListener<CategoryComposite> listener) { myEntityTable().removeListener(listenerType, listener); }

    @NotNull public String keyAsString() {
        return idKey + ":" + Strings.escapeCharOn(descr, ':') + ":" + Strings.escapeCharOn(shortDesc, ':');
    }

    @NotNull public Tuple3<Long,String,String> keyObject() { return Tuple.tuple(idKey, descr, shortDesc); }

    @Override @NotNull public final Seq<String> describe() { return formatList(getDescr()); }

    @Override @NotNull public String toString() { return "" + getDescr(); }

    /** Gets mapper for SQL statements */
    @NotNull public static RowMapper<CategoryComposite> rowMapper() { return CATEGORY_COMPOSITE.metadata().getRowMapper(); }

}
