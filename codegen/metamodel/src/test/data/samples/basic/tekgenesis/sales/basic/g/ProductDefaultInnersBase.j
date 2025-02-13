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
import tekgenesis.persistence.EntityTable;
import tekgenesis.common.collections.ImmutableList;
import tekgenesis.persistence.InnerEntitySeq;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import tekgenesis.persistence.PersistableInstance;
import tekgenesis.sales.basic.ProductDefaultInners;
import tekgenesis.common.core.Resource;
import tekgenesis.sales.basic.Rev;
import tekgenesis.database.RowMapper;
import tekgenesis.persistence.Select;
import tekgenesis.common.collections.Seq;
import java.util.Set;
import tekgenesis.sales.basic.State;
import tekgenesis.common.core.Strings;
import static tekgenesis.sales.basic.g.CategoryDefaultTable.CATEGORY_DEFAULT;
import static tekgenesis.sales.basic.g.ProductDefaultInnersTable.PRODUCT_DEFAULT_INNERS;
import static tekgenesis.sales.basic.g.RevTable.REV;
import static tekgenesis.persistence.EntitySeq.createInnerEntitySeq;
import static tekgenesis.common.util.Conversions.formatList;
import static tekgenesis.persistence.Sql.selectFrom;

/** 
 * Generated base class for entity: ProductDefaultInners.
 * Don't modify this as this is an auto generated class that's gets generated
 * every time the meta model file is modified. Use subclass instead.
 */
@SuppressWarnings({"DuplicateStringLiteralInspection", "WeakerAccess", "FieldMayBeFinal", "FieldCanBeLocal", "ConstantConditions", "ClassWithTooManyMethods", "ClassWithTooManyFields", "MagicNumber", "MethodOverridesStaticMethodOfSuperclass", "ParameterHidesMemberVariable", "FieldNameHidesFieldInSuperclass", "OverlyComplexClass", "RedundantCast", "UnusedReturnValue"})
public abstract class ProductDefaultInnersBase
    extends EntityInstanceImpl<ProductDefaultInners,Integer>
    implements PersistableInstance<ProductDefaultInners,Integer>
{

    //~ Fields ...................................................................................................................

    private int id = EntityTable.DEFAULT_EMPTY_KEY;
    @NotNull String model = "";
    @Nullable String description = null;
    @NotNull BigDecimal price = BigDecimal.ZERO;
    @NotNull State state = State.CREATED;
    int mainCategoryId = 0;
    @NotNull EntityRef<CategoryDefault,Integer> mainCategory = new EntityRef<>(CATEGORY_DEFAULT);
    @Nullable Resource image = null;
    @NotNull private InnerEntitySeq<Rev> reviews = createInnerEntitySeq(REV, (ProductDefaultInners) this, c -> ((RevBase)c).productDefaultInners);
    @NotNull DateTime updateTime = DateTime.EPOCH;

    //~ Methods ..................................................................................................................

    /** Returns the Id. */
    public int getId() { return this.id; }

    @Override public boolean hasEmptyKey() { return id == EntityTable.DEFAULT_EMPTY_KEY; }

    /** Returns the Model. */
    @NotNull public String getModel() { return this.model; }

    /** Sets the value of the Model. */
    @NotNull public ProductDefaultInners setModel(@NotNull String model) {
        markAsModified();
        this.model = Strings.truncate(model, 30);
        return (ProductDefaultInners) this;
    }

    /** Returns the Description. */
    @Nullable public String getDescription() { return this.description; }

    /** Sets the value of the Description. */
    @NotNull public ProductDefaultInners setDescription(@Nullable String description) {
        markAsModified();
        this.description = Strings.truncate(description, 100);
        return (ProductDefaultInners) this;
    }

    /** Returns the Price. */
    @NotNull public BigDecimal getPrice() { return this.price; }

    /** Sets the value of the Price. */
    @NotNull public ProductDefaultInners setPrice(@NotNull BigDecimal price) {
        markAsModified();
        this.price = Decimals.scaleAndCheck("price", price, false, 10, 2);
        return (ProductDefaultInners) this;
    }

    /** Returns the State. */
    @NotNull public State getState() { return this.state; }

    /** Sets the value of the State. */
    @NotNull public ProductDefaultInners setState(@NotNull State state) {
        markAsModified();
        this.state = state;
        return (ProductDefaultInners) this;
    }

    /** Returns the Main Category Id. */
    public int getMainCategoryId() { return this.mainCategoryId; }

    /** Returns the Main Category. */
    @NotNull public CategoryDefault getMainCategory() { return mainCategory.solveOrFail(this.mainCategoryId); }

    /** Sets the value of the Main Category Id. */
    @NotNull public ProductDefaultInners setMainCategoryId(int mainCategoryId) {
        mainCategory.invalidate();
        this.mainCategoryId = mainCategoryId;
        return (ProductDefaultInners) this;
    }

    /** Sets the value of the Main Category. */
    @NotNull public ProductDefaultInners setMainCategory(@NotNull CategoryDefault mainCategory) {
        this.mainCategory.set(mainCategory);
        this.mainCategoryId = mainCategory.getId();
        return (ProductDefaultInners) this;
    }

    /** Returns the Image. */
    @Nullable public Resource getImage() { return this.image; }

    /** Sets the value of the Image. */
    @NotNull public ProductDefaultInners setImage(@Nullable Resource image) {
        markAsModified();
        this.image = image;
        return (ProductDefaultInners) this;
    }

    /** Returns the Reviews. */
    @NotNull public InnerEntitySeq<Rev> getReviews() { return reviews; }

    /** Returns the Update Time. */
    @Override @NotNull public DateTime getUpdateTime() { return this.updateTime; }

    /** Creates a new {@link ProductDefaultInners} instance. */
    @NotNull public static ProductDefaultInners create() { return new ProductDefaultInners(); }

    @NotNull private static EntityTable<ProductDefaultInners,Integer> myEntityTable() { return EntityTable.forTable(PRODUCT_DEFAULT_INNERS); }

    @NotNull public EntityTable<ProductDefaultInners,Integer> et() { return myEntityTable(); }

    @Override @NotNull public DbTable<ProductDefaultInners,Integer> table() { return PRODUCT_DEFAULT_INNERS; }

    /** 
     * Try to finds an Object of type 'ProductDefaultInners' in the database.
     * Identified by the primary key.
     * Returns <code>null</code> if is not present
     */
    @Nullable public static ProductDefaultInners find(int id) { return myEntityTable().find(id); }

    /** 
     * Try to finds an Object of type 'ProductDefaultInners' in the database.
     * Identified by the primary key.
     * Throws EntityNotFoundException if is not present
     */
    @NotNull public static ProductDefaultInners findOrFail(int id) { return myEntityTable().findOrFail(id); }

    /** 
     * Try to finds an Object of type 'ProductDefaultInners' in the database.
     * Identified by the primary key.
     * Ignoring caches and accessing the database
     * Returns <code>null</code> if is not present
     */
    @Nullable public static ProductDefaultInners findPersisted(int id) { return myEntityTable().findPersisted(id); }

    /** 
     * Try to finds an Object of type 'ProductDefaultInners' in the database.
     * Identified by the primary key.
     * Ignoring caches and accessing the database
     * Throws EntityNotFoundException if is not present
     */
    @NotNull public static ProductDefaultInners findPersistedOrFail(int id) { return myEntityTable().findPersistedOrFail(id); }

    /** 
     * Try to finds an Object of type 'ProductDefaultInners' in the database.
     * Identified by the primary key.
     * Returns <code>null</code> if is not present
     */
    @Nullable public static ProductDefaultInners find(@NotNull String key) { return myEntityTable().findByString(key); }

    /** 
     * Try to finds an Object of type 'ProductDefaultInners' in the database.
     * That verifies the specified condition.
     * Returns <code>null</code> if is not present
     */
    @Nullable public static ProductDefaultInners findWhere(@NotNull Criteria... condition) {
        return selectFrom(PRODUCT_DEFAULT_INNERS).where(condition).get();
    }

    /** Create a selectFrom(PRODUCT_DEFAULT_INNERS). */
    @NotNull public static Select<ProductDefaultInners> list() { return selectFrom(PRODUCT_DEFAULT_INNERS); }

    /** Performs the given action for each ProductDefaultInners */
    public static void forEach(@Nullable Consumer<ProductDefaultInners> consumer) { selectFrom(PRODUCT_DEFAULT_INNERS).forEach(consumer); }

    /** List instances of 'ProductDefaultInners' with the specified keys. */
    @NotNull public static ImmutableList<ProductDefaultInners> list(@Nullable Set<Integer> keys) { return myEntityTable().list(keys); }

    /** List instances of 'ProductDefaultInners' with the specified keys. */
    @NotNull public static ImmutableList<ProductDefaultInners> list(@Nullable Iterable<String> keys) { return myEntityTable().listFromStringKeys(keys); }

    /** List the instances of 'ProductDefaultInners' that verify the specified condition. */
    @NotNull public static Select<ProductDefaultInners> listWhere(@NotNull Criteria condition) { return selectFrom(PRODUCT_DEFAULT_INNERS).where(condition); }

    /** Insert specifying the primary key */
    public void insert(int key) {
        this.id = key;
        myEntityTable().insertDoNotGenerate((ProductDefaultInners) this);
    }

    /** Register a Listener */
    public static void addListener(@NotNull EntityListenerType listenerType, @NotNull EntityListener<ProductDefaultInners> listener) { myEntityTable().addListener(listenerType, listener); }

    /** Remove a Listener */
    public static void removeListener(@NotNull EntityListenerType listenerType, @NotNull EntityListener<ProductDefaultInners> listener) { myEntityTable().removeListener(listenerType, listener); }

    @NotNull public String keyAsString() { return String.valueOf(id); }

    @NotNull public Integer keyObject() { return id; }

    @Override @NotNull public final Seq<String> describe() { return formatList(getDescription()); }

    @Override @NotNull public String toString() {
        return "" + (getDescription()==null ? "" : getDescription());
    }

    /** Gets mapper for SQL statements */
    @NotNull public static RowMapper<ProductDefaultInners> rowMapper() { return PRODUCT_DEFAULT_INNERS.metadata().getRowMapper(); }

    @Override public void invalidate() {
        reviews.invalidate();
        mainCategory.invalidate();
    }

}
