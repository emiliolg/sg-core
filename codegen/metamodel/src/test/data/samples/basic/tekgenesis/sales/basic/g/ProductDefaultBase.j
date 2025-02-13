package tekgenesis.sales.basic.g;

import java.math.BigDecimal;
import tekgenesis.sales.basic.CategoryDefault;
import java.util.function.Consumer;
import tekgenesis.persistence.Criteria;
import tekgenesis.common.core.DateTime;
import tekgenesis.persistence.DbTable;
import tekgenesis.common.core.Decimals;
import tekgenesis.persistence.EntityInstanceImpl;
import tekgenesis.persistence.EntityListener;
import tekgenesis.persistence.EntityListenerType;
import tekgenesis.persistence.EntityRef;
import tekgenesis.persistence.EntitySeq;
import tekgenesis.persistence.EntityTable;
import tekgenesis.common.collections.ImmutableList;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import tekgenesis.persistence.PersistableInstance;
import tekgenesis.sales.basic.ProductDefault;
import tekgenesis.common.core.Resource;
import tekgenesis.sales.basic.Review;
import tekgenesis.database.RowMapper;
import tekgenesis.persistence.Select;
import tekgenesis.common.collections.Seq;
import java.util.Set;
import tekgenesis.sales.basic.State;
import tekgenesis.common.core.Strings;
import static tekgenesis.sales.basic.g.CategoryDefaultTable.CATEGORY_DEFAULT;
import static tekgenesis.sales.basic.g.ProductDefaultTable.PRODUCT_DEFAULT;
import static tekgenesis.sales.basic.g.ReviewTable.REVIEW;
import static tekgenesis.persistence.EntitySeq.createEntitySeq;
import static tekgenesis.common.util.Conversions.formatList;
import static tekgenesis.common.collections.Colls.listOf;
import static tekgenesis.persistence.Sql.selectFrom;

/** 
 * Generated base class for entity: ProductDefault.
 * Don't modify this as this is an auto generated class that's gets generated
 * every time the meta model file is modified. Use subclass instead.
 */
@SuppressWarnings({"DuplicateStringLiteralInspection", "WeakerAccess", "FieldMayBeFinal", "FieldCanBeLocal", "ConstantConditions", "ClassWithTooManyMethods", "ClassWithTooManyFields", "MagicNumber", "MethodOverridesStaticMethodOfSuperclass", "ParameterHidesMemberVariable", "FieldNameHidesFieldInSuperclass", "OverlyComplexClass", "RedundantCast", "UnusedReturnValue"})
public abstract class ProductDefaultBase
    extends EntityInstanceImpl<ProductDefault,Integer>
    implements PersistableInstance<ProductDefault,Integer>
{

    //~ Fields ...................................................................................................................

    private int id = EntityTable.DEFAULT_EMPTY_KEY;
    @NotNull String model = "";
    @Nullable String description = null;
    @NotNull BigDecimal price = BigDecimal.ZERO;
    @NotNull State state = State.CREATED;
    int mainCategoryId = 0;
    @NotNull EntityRef<CategoryDefault,Integer> mainCategory = new EntityRef<>(CATEGORY_DEFAULT, CategoryDefault::getProducts);
    @Nullable Resource image = null;
    @NotNull private EntitySeq<Review> reviews = createEntitySeq(REVIEW, (ProductDefault) this, c -> ((ReviewBase)c).product, listOf(REVIEW.PRODUCT_ID));
    @NotNull String comments = "";
    @NotNull DateTime updateTime = DateTime.EPOCH;

    //~ Methods ..................................................................................................................

    /** Returns the Id. */
    public int getId() { return this.id; }

    @Override public boolean hasEmptyKey() { return id == EntityTable.DEFAULT_EMPTY_KEY; }

    /** Returns the Model. */
    @NotNull public String getModel() { return this.model; }

    /** Sets the value of the Model. */
    @NotNull public ProductDefault setModel(@NotNull String model) {
        markAsModified();
        this.model = Strings.truncate(model, 30);
        return (ProductDefault) this;
    }

    /** Returns the Description. */
    @Nullable public String getDescription() { return this.description; }

    /** Sets the value of the Description. */
    @NotNull public ProductDefault setDescription(@Nullable String description) {
        markAsModified();
        this.description = Strings.truncate(description, 100);
        return (ProductDefault) this;
    }

    /** Returns the Price. */
    @NotNull public BigDecimal getPrice() { return this.price; }

    /** Sets the value of the Price. */
    @NotNull public ProductDefault setPrice(@NotNull BigDecimal price) {
        markAsModified();
        this.price = Decimals.scaleAndCheck("price", price, false, 10, 2);
        return (ProductDefault) this;
    }

    /** Returns the State. */
    @NotNull public State getState() { return this.state; }

    /** Sets the value of the State. */
    @NotNull public ProductDefault setState(@NotNull State state) {
        markAsModified();
        this.state = state;
        return (ProductDefault) this;
    }

    /** Returns the Main Category Id. */
    public int getMainCategoryId() { return this.mainCategoryId; }

    /** Returns the Main Category. */
    @NotNull public CategoryDefault getMainCategory() { return mainCategory.solveOrFail(this.mainCategoryId); }

    /** Sets the value of the Main Category Id. */
    @NotNull public ProductDefault setMainCategoryId(int mainCategoryId) {
        mainCategory.invalidate();
        this.mainCategoryId = mainCategoryId;
        return (ProductDefault) this;
    }

    /** Sets the value of the Main Category. */
    @NotNull public ProductDefault setMainCategory(@NotNull CategoryDefault mainCategory) {
        this.mainCategory.set(mainCategory);
        this.mainCategoryId = mainCategory.getId();
        return (ProductDefault) this;
    }

    /** Returns the Image. */
    @Nullable public Resource getImage() { return this.image; }

    /** Sets the value of the Image. */
    @NotNull public ProductDefault setImage(@Nullable Resource image) {
        markAsModified();
        this.image = image;
        return (ProductDefault) this;
    }

    /** Returns the Reviews. */
    @NotNull public EntitySeq<Review> getReviews() { return reviews; }

    /** Returns the Comments. */
    @NotNull public String getComments() { return this.comments; }

    /** Sets the value of the Comments. */
    @NotNull public ProductDefault setComments(@NotNull String comments) {
        markAsModified();
        this.comments = Strings.truncate(comments, 5000);
        return (ProductDefault) this;
    }

    /** Returns the Update Time. */
    @Override @NotNull public DateTime getUpdateTime() { return this.updateTime; }

    /** Creates a new {@link ProductDefault} instance. */
    @NotNull public static ProductDefault create() { return new ProductDefault(); }

    @NotNull private static EntityTable<ProductDefault,Integer> myEntityTable() { return EntityTable.forTable(PRODUCT_DEFAULT); }

    @NotNull public EntityTable<ProductDefault,Integer> et() { return myEntityTable(); }

    @Override @NotNull public DbTable<ProductDefault,Integer> table() { return PRODUCT_DEFAULT; }

    /** 
     * Try to finds an Object of type 'ProductDefault' in the database.
     * Identified by the primary key.
     * Returns <code>null</code> if is not present
     */
    @Nullable public static ProductDefault find(int id) { return myEntityTable().find(id); }

    /** 
     * Try to finds an Object of type 'ProductDefault' in the database.
     * Identified by the primary key.
     * Throws EntityNotFoundException if is not present
     */
    @NotNull public static ProductDefault findOrFail(int id) { return myEntityTable().findOrFail(id); }

    /** 
     * Try to finds an Object of type 'ProductDefault' in the database.
     * Identified by the primary key.
     * Ignoring caches and accessing the database
     * Returns <code>null</code> if is not present
     */
    @Nullable public static ProductDefault findPersisted(int id) { return myEntityTable().findPersisted(id); }

    /** 
     * Try to finds an Object of type 'ProductDefault' in the database.
     * Identified by the primary key.
     * Ignoring caches and accessing the database
     * Throws EntityNotFoundException if is not present
     */
    @NotNull public static ProductDefault findPersistedOrFail(int id) { return myEntityTable().findPersistedOrFail(id); }

    /** 
     * Try to finds an Object of type 'ProductDefault' in the database.
     * Identified by the primary key.
     * Returns <code>null</code> if is not present
     */
    @Nullable public static ProductDefault find(@NotNull String key) { return myEntityTable().findByString(key); }

    /** 
     * Try to finds an Object of type 'ProductDefault' in the database.
     * That verifies the specified condition.
     * Returns <code>null</code> if is not present
     */
    @Nullable public static ProductDefault findWhere(@NotNull Criteria... condition) { return selectFrom(PRODUCT_DEFAULT).where(condition).get(); }

    /** Create a selectFrom(PRODUCT_DEFAULT). */
    @NotNull public static Select<ProductDefault> list() { return selectFrom(PRODUCT_DEFAULT); }

    /** Performs the given action for each ProductDefault */
    public static void forEach(@Nullable Consumer<ProductDefault> consumer) { selectFrom(PRODUCT_DEFAULT).forEach(consumer); }

    /** List instances of 'ProductDefault' with the specified keys. */
    @NotNull public static ImmutableList<ProductDefault> list(@Nullable Set<Integer> keys) { return myEntityTable().list(keys); }

    /** List instances of 'ProductDefault' with the specified keys. */
    @NotNull public static ImmutableList<ProductDefault> list(@Nullable Iterable<String> keys) { return myEntityTable().listFromStringKeys(keys); }

    /** List the instances of 'ProductDefault' that verify the specified condition. */
    @NotNull public static Select<ProductDefault> listWhere(@NotNull Criteria condition) { return selectFrom(PRODUCT_DEFAULT).where(condition); }

    /** Insert specifying the primary key */
    public void insert(int key) {
        this.id = key;
        myEntityTable().insertDoNotGenerate((ProductDefault) this);
    }

    /** Register a Listener */
    public static void addListener(@NotNull EntityListenerType listenerType, @NotNull EntityListener<ProductDefault> listener) { myEntityTable().addListener(listenerType, listener); }

    /** Remove a Listener */
    public static void removeListener(@NotNull EntityListenerType listenerType, @NotNull EntityListener<ProductDefault> listener) { myEntityTable().removeListener(listenerType, listener); }

    @NotNull public String keyAsString() { return String.valueOf(id); }

    @NotNull public Integer keyObject() { return id; }

    @Override @NotNull public final Seq<String> describe() { return formatList(getDescription()); }

    @Override @NotNull public String toString() {
        return "" + (getDescription()==null ? "" : getDescription());
    }

    /** Gets mapper for SQL statements */
    @NotNull public static RowMapper<ProductDefault> rowMapper() { return PRODUCT_DEFAULT.metadata().getRowMapper(); }

    @Override public void invalidate() {
        reviews.invalidate();
        mainCategory.invalidate();
    }

}
