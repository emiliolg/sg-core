package tekgenesis.sales.basic.g;

import java.math.BigDecimal;
import tekgenesis.sales.basic.Category;
import java.util.function.Consumer;
import tekgenesis.persistence.Criteria;
import tekgenesis.common.core.DateTime;
import tekgenesis.persistence.DbTable;
import tekgenesis.common.core.Decimals;
import tekgenesis.persistence.EntityInstanceImpl;
import tekgenesis.persistence.EntityListener;
import tekgenesis.persistence.EntityListenerType;
import tekgenesis.persistence.EntityRef;
import tekgenesis.persistence.EntityTable;
import java.util.EnumSet;
import tekgenesis.sales.basic.Image;
import tekgenesis.common.collections.ImmutableList;
import tekgenesis.persistence.InnerEntitySeq;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import tekgenesis.persistence.PersistableInstance;
import tekgenesis.sales.basic.Product;
import tekgenesis.sales.basic.ProductByCat;
import tekgenesis.database.RowMapper;
import tekgenesis.persistence.Select;
import tekgenesis.common.collections.Seq;
import tekgenesis.sales.basic.State;
import tekgenesis.common.core.Strings;
import tekgenesis.sales.basic.Tag;
import static tekgenesis.sales.basic.g.CategoryTable.CATEGORY;
import static tekgenesis.sales.basic.g.ImageTable.IMAGE;
import static tekgenesis.sales.basic.g.ProductTable.PRODUCT;
import static tekgenesis.sales.basic.g.ProductByCatTable.PRODUCT_BY_CAT;
import static tekgenesis.persistence.EntitySeq.createInnerEntitySeq;
import static tekgenesis.common.util.Conversions.formatList;
import static tekgenesis.persistence.Sql.selectFrom;

/** 
 * Product entity
 * <b>Described by: </b> description.
 * <b>Searcheable: </b> using ProductSearcher, that looks by id and by model.
 * 
 * Generated base class for entity: Product.
 * Don't modify this as this is an auto generated class that's gets generated
 * every time the meta model file is modified. Use subclass instead.
 */
@SuppressWarnings({"DuplicateStringLiteralInspection", "WeakerAccess", "FieldMayBeFinal", "FieldCanBeLocal", "ConstantConditions", "ClassWithTooManyMethods", "ClassWithTooManyFields", "MagicNumber", "MethodOverridesStaticMethodOfSuperclass", "ParameterHidesMemberVariable", "FieldNameHidesFieldInSuperclass", "OverlyComplexClass", "RedundantCast", "UnusedReturnValue"})
public abstract class ProductBase
    extends EntityInstanceImpl<Product,String>
    implements PersistableInstance<Product,String>
{

    //~ Fields ...................................................................................................................

    @NotNull String productId = "";
    @NotNull String model = "";
    @Nullable String description = null;
    @NotNull BigDecimal price = BigDecimal.ZERO;
    @NotNull State state = State.CREATED;
    long categoryIdKey = 0;
    @NotNull EntityRef<Category,Long> category = new EntityRef<>(CATEGORY, Category::getProducts);
    @NotNull String categoryAttPersisted = "";
    @NotNull private InnerEntitySeq<ProductByCat> secondary = createInnerEntitySeq(PRODUCT_BY_CAT, (Product) this, c -> ((ProductByCatBase)c).product);
    @NotNull EnumSet<Tag> tags = EnumSet.noneOf(Tag.class);
    @NotNull private InnerEntitySeq<Image> images = createInnerEntitySeq(IMAGE, (Product) this, c -> ((ImageBase)c).product);
    @NotNull DateTime updateTime = DateTime.EPOCH;

    //~ Methods ..................................................................................................................

    /** Returns the Product Id. */
    @NotNull public String getProductId() { return this.productId; }

    /** Returns the Model. */
    @NotNull public String getModel() { return this.model; }

    /** Sets the value of the Model. */
    @NotNull public Product setModel(@NotNull String model) {
        markAsModified();
        this.model = Strings.truncate(model, 30);
        return (Product) this;
    }

    /** Returns the Description. */
    @Nullable public String getDescription() { return this.description; }

    /** Sets the value of the Description. */
    @NotNull public Product setDescription(@Nullable String description) {
        markAsModified();
        this.description = Strings.truncate(description, 100);
        return (Product) this;
    }

    /** Returns the Price. */
    @NotNull public BigDecimal getPrice() { return this.price; }

    /** Sets the value of the Price. */
    @NotNull public Product setPrice(@NotNull BigDecimal price) {
        markAsModified();
        this.price = Decimals.scaleAndCheck("price", price, false, 10, 2);
        return (Product) this;
    }

    /** Returns the State. */
    @NotNull public State getState() { return this.state; }

    /** Sets the value of the State. */
    @NotNull public Product setState(@NotNull State state) {
        markAsModified();
        this.state = state;
        return (Product) this;
    }

    /** Returns the Category Id Key. */
    public long getCategoryIdKey() { return this.categoryIdKey; }

    /** Returns the Category. */
    @NotNull public Category getCategory() { return category.solveOrFail(this.categoryIdKey); }

    /** Sets the value of the Category Id Key. */
    @NotNull public Product setCategoryIdKey(long categoryIdKey) {
        category.invalidate();
        this.categoryIdKey = categoryIdKey;
        return (Product) this;
    }

    /** Sets the value of the Category. */
    @NotNull public Product setCategory(@NotNull Category category) {
        this.category.set(category);
        this.categoryIdKey = category.getIdKey();
        return (Product) this;
    }

    /** Returns the Category Att Persisted. */
    @NotNull public String getCategoryAttPersisted() { return this.categoryAttPersisted; }

    /** Sets the value of the Category Att Persisted. */
    @NotNull public Product setCategoryAttPersisted(@NotNull String categoryAttPersisted) {
        markAsModified();
        this.categoryAttPersisted = Strings.truncate(categoryAttPersisted, 150);
        return (Product) this;
    }

    /** Returns the Secondary. */
    @NotNull public InnerEntitySeq<ProductByCat> getSecondary() { return secondary; }

    /** Returns the Tags. */
    @NotNull public EnumSet<Tag> getTags() { return this.tags; }

    /** Sets the value of the Tags. */
    @NotNull public Product setTags(@NotNull EnumSet<Tag> tags) {
        markAsModified();
        this.tags = tags;
        return (Product) this;
    }

    /** Returns the Images. */
    @NotNull public InnerEntitySeq<Image> getImages() { return images; }

    /** Returns the Update Time. */
    @Override @NotNull public DateTime getUpdateTime() { return this.updateTime; }

    /** Creates a new {@link Product} instance. */
    @NotNull public static Product create(@NotNull String productId) {
        final Product result = new Product();
        ((ProductBase) result).productId = Strings.truncate(productId, 8);
        return result;
    }

    /** 
     * Find (or create if not present) a 'Product' in the database.
     * Identified by the primary key.
     */
    @NotNull public static Product findOrCreate(@NotNull String productId) { return myEntityTable().findOrCreate(productId); }

    @NotNull private static EntityTable<Product,String> myEntityTable() { return EntityTable.forTable(PRODUCT); }

    @NotNull public EntityTable<Product,String> et() { return myEntityTable(); }

    @Override @NotNull public DbTable<Product,String> table() { return PRODUCT; }

    /** 
     * Try to finds an Object of type 'Product' in the database.
     * Identified by the primary key.
     * Returns <code>null</code> if is not present
     */
    @Nullable public static Product find(@NotNull String productId) { return myEntityTable().find(productId); }

    /** 
     * Try to finds an Object of type 'Product' in the database.
     * Identified by the primary key.
     * Throws EntityNotFoundException if is not present
     */
    @NotNull public static Product findOrFail(@NotNull String productId) { return myEntityTable().findOrFail(productId); }

    /** 
     * Try to finds an Object of type 'Product' in the database.
     * Identified by the primary key.
     * Ignoring caches and accessing the database
     * Returns <code>null</code> if is not present
     */
    @Nullable public static Product findPersisted(@NotNull String productId) { return myEntityTable().findPersisted(productId); }

    /** 
     * Try to finds an Object of type 'Product' in the database.
     * Identified by the primary key.
     * Ignoring caches and accessing the database
     * Throws EntityNotFoundException if is not present
     */
    @NotNull public static Product findPersistedOrFail(@NotNull String productId) { return myEntityTable().findPersistedOrFail(productId); }

    /** 
     * Try to finds an Object of type 'Product' in the database.
     * That verifies the specified condition.
     * Returns <code>null</code> if is not present
     */
    @Nullable public static Product findWhere(@NotNull Criteria... condition) { return selectFrom(PRODUCT).where(condition).get(); }

    /** Create a selectFrom(PRODUCT). */
    @NotNull public static Select<Product> list() { return selectFrom(PRODUCT); }

    /** Performs the given action for each Product */
    public static void forEach(@Nullable Consumer<Product> consumer) { selectFrom(PRODUCT).forEach(consumer); }

    /** List instances of 'Product' with the specified keys. */
    @NotNull public static ImmutableList<Product> list(@Nullable Iterable<String> keys) { return myEntityTable().listFromStringKeys(keys); }

    /** List the instances of 'Product' that verify the specified condition. */
    @NotNull public static Select<Product> listWhere(@NotNull Criteria condition) { return selectFrom(PRODUCT).where(condition); }

    @Override @NotNull public final Product update() { return PersistableInstance.super.update(); }

    @Override @NotNull public final Product insert() { return PersistableInstance.super.insert(); }

    /** Register a Listener */
    public static void addListener(@NotNull EntityListenerType listenerType, @NotNull EntityListener<Product> listener) { myEntityTable().addListener(listenerType, listener); }

    /** Remove a Listener */
    public static void removeListener(@NotNull EntityListenerType listenerType, @NotNull EntityListener<Product> listener) { myEntityTable().removeListener(listenerType, listener); }

    @NotNull public String keyAsString() { return productId; }

    @NotNull public String keyObject() { return productId; }

    @Override @NotNull public final Seq<String> describe() { return formatList(getDescription()); }

    @Override @NotNull public String toString() {
        return "" + (getDescription()==null ? "" : getDescription());
    }

    /** Gets mapper for SQL statements */
    @NotNull public static RowMapper<Product> rowMapper() { return PRODUCT.metadata().getRowMapper(); }

    @Override public void invalidate() {
        secondary.invalidate();
        images.invalidate();
        category.invalidate();
    }

}
