package tekgenesis.sales.basic.g;

import tekgenesis.sales.basic.Category;
import java.util.function.Consumer;
import tekgenesis.persistence.Criteria;
import tekgenesis.common.core.DateTime;
import tekgenesis.persistence.DbTable;
import tekgenesis.persistence.EntityInstanceImpl;
import tekgenesis.persistence.EntityListener;
import tekgenesis.persistence.EntityListenerType;
import tekgenesis.persistence.EntitySeq;
import tekgenesis.persistence.EntityTable;
import tekgenesis.common.collections.ImmutableList;
import tekgenesis.common.core.Integers;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import tekgenesis.persistence.PersistableInstance;
import tekgenesis.sales.basic.Product;
import tekgenesis.database.RowMapper;
import tekgenesis.persistence.Select;
import tekgenesis.common.collections.Seq;
import java.util.Set;
import tekgenesis.common.core.Strings;
import static tekgenesis.sales.basic.g.CategoryTable.CATEGORY;
import static tekgenesis.sales.basic.g.ProductTable.PRODUCT;
import static tekgenesis.persistence.EntitySeq.createEntitySeq;
import static tekgenesis.common.util.Conversions.formatList;
import static tekgenesis.common.collections.Colls.listOf;
import static tekgenesis.persistence.Sql.selectFrom;

/** 
 * Category entity
 * <b>Searchable: </b> using CategorySearcher, indexing fields idKey and name
 * 
 * Generated base class for entity: Category.
 * Don't modify this as this is an auto generated class that's gets generated
 * every time the meta model file is modified. Use subclass instead.
 */
@SuppressWarnings({"DuplicateStringLiteralInspection", "WeakerAccess", "FieldMayBeFinal", "FieldCanBeLocal", "ConstantConditions", "ClassWithTooManyMethods", "ClassWithTooManyFields", "MagicNumber", "MethodOverridesStaticMethodOfSuperclass", "ParameterHidesMemberVariable", "FieldNameHidesFieldInSuperclass", "OverlyComplexClass", "RedundantCast", "UnusedReturnValue"})
public abstract class CategoryBase
    extends EntityInstanceImpl<Category,Long>
    implements PersistableInstance<Category,Long>
{

    //~ Fields ...................................................................................................................

    long idKey = 0;
    @NotNull String name = "";
    @NotNull String descr = "";
    @NotNull private EntitySeq<Product> products = createEntitySeq(PRODUCT, (Category) this, c -> ((ProductBase)c).category, listOf(PRODUCT.CATEGORY_ID_KEY));
    @NotNull DateTime updateTime = DateTime.EPOCH;

    //~ Methods ..................................................................................................................

    /** Returns the Id Key. */
    public long getIdKey() { return this.idKey; }

    /** Returns the Name. */
    @NotNull public String getName() { return this.name; }

    /** Sets the value of the Name. */
    @NotNull public Category setName(@NotNull String name) {
        markAsModified();
        this.name = Strings.truncate(name, 30);
        return (Category) this;
    }

    /** Returns the Descr. */
    @NotNull public String getDescr() { return this.descr; }

    /** Sets the value of the Descr. */
    @NotNull public Category setDescr(@NotNull String descr) {
        markAsModified();
        this.descr = Strings.truncate(descr, 120);
        return (Category) this;
    }

    /** Returns the Products. */
    @NotNull public EntitySeq<Product> getProducts() { return products; }

    /** Returns the Update Time. */
    @Override @NotNull public DateTime getUpdateTime() { return this.updateTime; }

    /** Creates a new {@link Category} instance. */
    @NotNull public static Category create(long idKey) {
        final Category result = new Category();
        ((CategoryBase) result).idKey = Integers.checkSignedLength("idKey", idKey, false, 18);
        return result;
    }

    /** 
     * Find (or create if not present) a 'Category' in the database.
     * Identified by the primary key.
     */
    @NotNull public static Category findOrCreate(long idKey) { return myEntityTable().findOrCreate(idKey); }

    /** 
     * Find (or create if not present) a 'Category' in the database.
     * Identified by the primary key.
     */
    @NotNull public static Category findOrCreate(@NotNull String key) { return myEntityTable().findOrCreateByString(key); }

    @NotNull private static EntityTable<Category,Long> myEntityTable() { return EntityTable.forTable(CATEGORY); }

    @NotNull public EntityTable<Category,Long> et() { return myEntityTable(); }

    @Override @NotNull public DbTable<Category,Long> table() { return CATEGORY; }

    /** 
     * Try to finds an Object of type 'Category' in the database.
     * Identified by the primary key.
     * Returns <code>null</code> if is not present
     */
    @Nullable public static Category find(long idKey) { return myEntityTable().find(idKey); }

    /** 
     * Try to finds an Object of type 'Category' in the database.
     * Identified by the primary key.
     * Throws EntityNotFoundException if is not present
     */
    @NotNull public static Category findOrFail(long idKey) { return myEntityTable().findOrFail(idKey); }

    /** 
     * Try to finds an Object of type 'Category' in the database.
     * Identified by the primary key.
     * Ignoring caches and accessing the database
     * Returns <code>null</code> if is not present
     */
    @Nullable public static Category findPersisted(long idKey) { return myEntityTable().findPersisted(idKey); }

    /** 
     * Try to finds an Object of type 'Category' in the database.
     * Identified by the primary key.
     * Ignoring caches and accessing the database
     * Throws EntityNotFoundException if is not present
     */
    @NotNull public static Category findPersistedOrFail(long idKey) { return myEntityTable().findPersistedOrFail(idKey); }

    /** 
     * Try to finds an Object of type 'Category' in the database.
     * Identified by the primary key.
     * Returns <code>null</code> if is not present
     */
    @Nullable public static Category find(@NotNull String key) { return myEntityTable().findByString(key); }

    /** 
     * Try to finds an Object of type 'Category' in the database.
     * That verifies the specified condition.
     * Returns <code>null</code> if is not present
     */
    @Nullable public static Category findWhere(@NotNull Criteria... condition) { return selectFrom(CATEGORY).where(condition).get(); }

    /** Create a selectFrom(CATEGORY). */
    @NotNull public static Select<Category> list() { return selectFrom(CATEGORY); }

    /** Performs the given action for each Category */
    public static void forEach(@Nullable Consumer<Category> consumer) { selectFrom(CATEGORY).forEach(consumer); }

    /** List instances of 'Category' with the specified keys. */
    @NotNull public static ImmutableList<Category> list(@Nullable Set<Long> keys) { return myEntityTable().list(keys); }

    /** List instances of 'Category' with the specified keys. */
    @NotNull public static ImmutableList<Category> list(@Nullable Iterable<String> keys) { return myEntityTable().listFromStringKeys(keys); }

    /** List the instances of 'Category' that verify the specified condition. */
    @NotNull public static Select<Category> listWhere(@NotNull Criteria condition) { return selectFrom(CATEGORY).where(condition); }

    @Override @NotNull public final Category update() { return PersistableInstance.super.update(); }

    @Override @NotNull public final Category insert() { return PersistableInstance.super.insert(); }

    /** Register a Listener */
    public static void addListener(@NotNull EntityListenerType listenerType, @NotNull EntityListener<Category> listener) { myEntityTable().addListener(listenerType, listener); }

    /** Remove a Listener */
    public static void removeListener(@NotNull EntityListenerType listenerType, @NotNull EntityListener<Category> listener) { myEntityTable().removeListener(listenerType, listener); }

    @NotNull public String keyAsString() { return String.valueOf(idKey); }

    @NotNull public Long keyObject() { return idKey; }

    @Override @NotNull public final Seq<String> describe() { return formatList(getDescr()); }

    @Override @NotNull public String toString() { return "" + getDescr(); }

    /** Gets mapper for SQL statements */
    @NotNull public static RowMapper<Category> rowMapper() { return CATEGORY.metadata().getRowMapper(); }

    @Override public void invalidate() { products.invalidate(); }

}
