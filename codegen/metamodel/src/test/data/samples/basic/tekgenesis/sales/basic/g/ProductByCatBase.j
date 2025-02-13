package tekgenesis.sales.basic.g;

import tekgenesis.persistence.AuditableInstance;
import tekgenesis.sales.basic.Category;
import java.util.function.Consumer;
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
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import tekgenesis.sales.basic.Product;
import tekgenesis.sales.basic.ProductByCat;
import tekgenesis.database.RowMapper;
import tekgenesis.persistence.Select;
import tekgenesis.common.collections.Seq;
import java.util.Set;
import tekgenesis.common.core.Strings;
import tekgenesis.common.core.Tuple;
import static tekgenesis.sales.basic.g.CategoryTable.CATEGORY;
import static tekgenesis.sales.basic.g.ProductTable.PRODUCT;
import static tekgenesis.sales.basic.g.ProductByCatTable.PRODUCT_BY_CAT;
import static tekgenesis.common.util.Conversions.formatList;
import static tekgenesis.persistence.Sql.selectFrom;

/** 
 * Product by category documentation
 * 
 * Generated base class for entity: ProductByCat.
 * Don't modify this as this is an auto generated class that's gets generated
 * every time the meta model file is modified. Use subclass instead.
 */
@SuppressWarnings({"DuplicateStringLiteralInspection", "WeakerAccess", "FieldMayBeFinal", "FieldCanBeLocal", "ConstantConditions", "ClassWithTooManyMethods", "ClassWithTooManyFields", "MagicNumber", "MethodOverridesStaticMethodOfSuperclass", "ParameterHidesMemberVariable", "FieldNameHidesFieldInSuperclass", "OverlyComplexClass", "RedundantCast", "UnusedReturnValue"})
public abstract class ProductByCatBase
    extends EntityInstanceImpl<ProductByCat,Tuple<String,Integer>>
    implements InnerInstance<ProductByCat,Tuple<String,Integer>,Product,String>, AuditableInstance
{

    //~ Fields ...................................................................................................................

    @NotNull String productProductId = "";
    @NotNull EntityRef<Product,String> product = new EntityRef<>(PRODUCT, Product::getSecondary);
    int seqId = 0;
    long secondaryCategoryIdKey = 0;
    @NotNull EntityRef<Category,Long> secondaryCategory = new EntityRef<>(CATEGORY);
    @NotNull DateTime updateTime = DateTime.EPOCH;
    @NotNull DateTime creationTime = DateTime.EPOCH;
    @Nullable String creationUser = null;
    @Nullable String updateUser = null;

    //~ Methods ..................................................................................................................

    /** Returns the Product Product Id. */
    @NotNull public String getProductProductId() { return this.productProductId; }

    /** Returns the Product. */
    @NotNull public Product getProduct() { return product.solveOrFail(this.productProductId); }

    /** Returns the Seq Id. */
    public int getSeqId() { return this.seqId; }

    @Override @NotNull public EntityRef<Product,String> parent() { return product; }

    @Override @NotNull public InnerEntitySeq<ProductByCat> siblings() { return getProduct().getSecondary(); }

    @Override public int seqId() { return getSeqId(); }

    /** Returns the Secondary Category Id Key. */
    public long getSecondaryCategoryIdKey() { return this.secondaryCategoryIdKey; }

    /** Returns the Secondary Category. */
    @NotNull public Category getSecondaryCategory() {
        return secondaryCategory.solveOrFail(this.secondaryCategoryIdKey);
    }

    /** Sets the value of the Secondary Category Id Key. */
    @NotNull public ProductByCat setSecondaryCategoryIdKey(long secondaryCategoryIdKey) {
        secondaryCategory.invalidate();
        this.secondaryCategoryIdKey = secondaryCategoryIdKey;
        return (ProductByCat) this;
    }

    /** Sets the value of the Secondary Category. */
    @NotNull public ProductByCat setSecondaryCategory(@NotNull Category secondaryCategory) {
        this.secondaryCategory.set(secondaryCategory);
        this.secondaryCategoryIdKey = secondaryCategory.getIdKey();
        return (ProductByCat) this;
    }

    /** Returns the Update Time. */
    @Override @NotNull public DateTime getUpdateTime() { return this.updateTime; }

    /** Returns the Creation Time. */
    @NotNull public DateTime getCreationTime() { return this.creationTime; }

    /** Returns the Creation User. */
    @Nullable public String getCreationUser() { return this.creationUser; }

    /** Returns the Update User. */
    @Nullable public String getUpdateUser() { return this.updateUser; }

    @NotNull private static EntityTable<ProductByCat,Tuple<String,Integer>> myEntityTable() { return EntityTable.forTable(PRODUCT_BY_CAT); }

    @NotNull public EntityTable<ProductByCat,Tuple<String,Integer>> et() { return myEntityTable(); }

    @Override @NotNull public DbTable<ProductByCat,Tuple<String,Integer>> table() { return PRODUCT_BY_CAT; }

    /** 
     * Try to finds an Object of type 'ProductByCat' in the database.
     * Identified by the primary key.
     * Returns <code>null</code> if is not present
     */
    @Nullable public static ProductByCat find(@NotNull Tuple<String,Integer> key) { return myEntityTable().find(key); }

    /** 
     * Try to finds an Object of type 'ProductByCat' in the database.
     * Identified by the primary key.
     * Throws EntityNotFoundException if is not present
     */
    @NotNull public static ProductByCat findOrFail(@NotNull Tuple<String,Integer> key) { return myEntityTable().findOrFail(key); }

    /** 
     * Try to finds an Object of type 'ProductByCat' in the database.
     * Identified by the primary key.
     * Ignoring caches and accessing the database
     * Returns <code>null</code> if is not present
     */
    @Nullable public static ProductByCat findPersisted(@NotNull Tuple<String,Integer> key) { return myEntityTable().findPersisted(key); }

    /** 
     * Try to finds an Object of type 'ProductByCat' in the database.
     * Identified by the primary key.
     * Ignoring caches and accessing the database
     * Throws EntityNotFoundException if is not present
     */
    @NotNull public static ProductByCat findPersistedOrFail(@NotNull Tuple<String,Integer> key) { return myEntityTable().findPersistedOrFail(key); }

    /** 
     * Try to finds an Object of type 'ProductByCat' in the database.
     * Identified by the primary key.
     * Returns <code>null</code> if is not present
     */
    @Nullable public static ProductByCat find(@NotNull String key) { return myEntityTable().findByString(key); }

    /** 
     * Try to finds an Object of type 'ProductByCat' in the database.
     * Identified by the primary key.
     * Returns <code>null</code> if is not present
     */
    @Nullable public static ProductByCat find(@NotNull String productProductId, int seqId) { return find(Tuple.tuple2(productProductId, seqId)); }

    /** 
     * Try to finds an Object of type 'ProductByCat' in the database.
     * That verifies the specified condition.
     * Returns <code>null</code> if is not present
     */
    @Nullable public static ProductByCat findWhere(@NotNull Criteria... condition) { return selectFrom(PRODUCT_BY_CAT).where(condition).get(); }

    /** Create a selectFrom(PRODUCT_BY_CAT). */
    @NotNull public static Select<ProductByCat> list() { return selectFrom(PRODUCT_BY_CAT); }

    /** Performs the given action for each ProductByCat */
    public static void forEach(@Nullable Consumer<ProductByCat> consumer) { selectFrom(PRODUCT_BY_CAT).forEach(consumer); }

    /** List instances of 'ProductByCat' with the specified keys. */
    @NotNull public static ImmutableList<ProductByCat> list(@Nullable Set<Tuple<String,Integer>> keys) { return myEntityTable().list(keys); }

    /** List instances of 'ProductByCat' with the specified keys. */
    @NotNull public static ImmutableList<ProductByCat> list(@Nullable Iterable<String> keys) { return myEntityTable().listFromStringKeys(keys); }

    /** List the instances of 'ProductByCat' that verify the specified condition. */
    @NotNull public static Select<ProductByCat> listWhere(@NotNull Criteria condition) { return selectFrom(PRODUCT_BY_CAT).where(condition); }

    @Override @NotNull public final ProductByCat update() { return InnerInstance.super.update(); }

    @Override @NotNull public final ProductByCat insert() { return InnerInstance.super.insert(); }

    /** Register a Listener */
    public static void addListener(@NotNull EntityListenerType listenerType, @NotNull EntityListener<ProductByCat> listener) { myEntityTable().addListener(listenerType, listener); }

    /** Remove a Listener */
    public static void removeListener(@NotNull EntityListenerType listenerType, @NotNull EntityListener<ProductByCat> listener) { myEntityTable().removeListener(listenerType, listener); }

    /** List the instances of 'ProductByCat' that matches the given parameters. */
    @NotNull public static ImmutableList<ProductByCat> listByUpdateTime(@NotNull DateTime updateTime) {
        return selectFrom(PRODUCT_BY_CAT).where(PRODUCT_BY_CAT.UPDATE_TIME.eq(updateTime)).list();
    }

    /** List the instances of 'ProductByCat' that matches the given parameters. */
    @NotNull public static ImmutableList<ProductByCat> listByCategory(long secondaryCategoryIdKey) {
        return selectFrom(PRODUCT_BY_CAT).where(PRODUCT_BY_CAT.SECONDARY_CATEGORY_ID_KEY.eq(secondaryCategoryIdKey)).list();
    }

    @NotNull public String keyAsString() {
        return Strings.escapeCharOn(productProductId, ':') + ":" + seqId;
    }

    @NotNull public Tuple<String,Integer> keyObject() { return Tuple.tuple2(productProductId, seqId); }

    @Override @NotNull public final Seq<String> describe() { return formatList(getSecondaryCategory()); }

    @Override @NotNull public String toString() { return "" + getSecondaryCategory(); }

    /** Gets mapper for SQL statements */
    @NotNull public static RowMapper<ProductByCat> rowMapper() { return PRODUCT_BY_CAT.metadata().getRowMapper(); }

    @Override public void invalidate() {
        secondaryCategory.invalidate();
        product.invalidate();
    }

}
