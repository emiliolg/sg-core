package tekgenesis.sales.basic.g;

import tekgenesis.persistence.AuditableInstance;
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
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import tekgenesis.persistence.PersistableInstance;
import tekgenesis.sales.basic.ProductDefault;
import tekgenesis.sales.basic.Review;
import tekgenesis.database.RowMapper;
import tekgenesis.persistence.Select;
import tekgenesis.common.collections.Seq;
import java.util.Set;
import tekgenesis.common.core.Strings;
import static tekgenesis.sales.basic.g.ProductDefaultTable.PRODUCT_DEFAULT;
import static tekgenesis.sales.basic.g.ReviewTable.REVIEW;
import static tekgenesis.common.util.Conversions.formatList;
import static tekgenesis.persistence.Sql.selectFrom;

/** 
 * Review comment, if double asterisk is used will be a documentation token and not a comment one.
 * 
 * Generated base class for entity: Review.
 * Don't modify this as this is an auto generated class that's gets generated
 * every time the meta model file is modified. Use subclass instead.
 */
@SuppressWarnings({"DuplicateStringLiteralInspection", "WeakerAccess", "FieldMayBeFinal", "FieldCanBeLocal", "ConstantConditions", "ClassWithTooManyMethods", "ClassWithTooManyFields", "MagicNumber", "MethodOverridesStaticMethodOfSuperclass", "ParameterHidesMemberVariable", "FieldNameHidesFieldInSuperclass", "OverlyComplexClass", "RedundantCast", "UnusedReturnValue"})
public abstract class ReviewBase
    extends EntityInstanceImpl<Review,Integer>
    implements PersistableInstance<Review,Integer>, AuditableInstance
{

    //~ Fields ...................................................................................................................

    private int id = EntityTable.DEFAULT_EMPTY_KEY;
    int productId = 0;
    @NotNull EntityRef<ProductDefault,Integer> product = new EntityRef<>(PRODUCT_DEFAULT, ProductDefault::getReviews);
    @NotNull String review = "";
    @NotNull DateTime updateTime = DateTime.EPOCH;
    @NotNull DateTime creationTime = DateTime.EPOCH;
    @Nullable String creationUser = null;
    @Nullable String updateUser = null;

    //~ Methods ..................................................................................................................

    /** Returns the Id. */
    public int getId() { return this.id; }

    @Override public boolean hasEmptyKey() { return id == EntityTable.DEFAULT_EMPTY_KEY; }

    /** Returns the Product Id. */
    public int getProductId() { return this.productId; }

    /** Returns the Product. */
    @NotNull public ProductDefault getProduct() { return product.solveOrFail(this.productId); }

    /** Sets the value of the Product Id. */
    @NotNull public Review setProductId(int productId) {
        product.invalidate();
        this.productId = productId;
        return (Review) this;
    }

    /** Sets the value of the Product. */
    @NotNull public Review setProduct(@NotNull ProductDefault product) {
        this.product.set(product);
        this.productId = product.getId();
        return (Review) this;
    }

    /** Returns the Review. */
    @NotNull public String getReview() { return this.review; }

    /** Sets the value of the Review. */
    @NotNull public Review setReview(@NotNull String review) {
        markAsModified();
        this.review = Strings.truncate(review, 255);
        return (Review) this;
    }

    /** Returns the Update Time. */
    @Override @NotNull public DateTime getUpdateTime() { return this.updateTime; }

    /** Returns the Creation Time. */
    @NotNull public DateTime getCreationTime() { return this.creationTime; }

    /** Returns the Creation User. */
    @Nullable public String getCreationUser() { return this.creationUser; }

    /** Returns the Update User. */
    @Nullable public String getUpdateUser() { return this.updateUser; }

    /** Creates a new {@link Review} instance. */
    @NotNull public static Review create() { return new Review(); }

    @NotNull private static EntityTable<Review,Integer> myEntityTable() { return EntityTable.forTable(REVIEW); }

    @NotNull public EntityTable<Review,Integer> et() { return myEntityTable(); }

    @Override @NotNull public DbTable<Review,Integer> table() { return REVIEW; }

    /** 
     * Try to finds an Object of type 'Review' in the database.
     * Identified by the primary key.
     * Returns <code>null</code> if is not present
     */
    @Nullable public static Review find(int id) { return myEntityTable().find(id); }

    /** 
     * Try to finds an Object of type 'Review' in the database.
     * Identified by the primary key.
     * Throws EntityNotFoundException if is not present
     */
    @NotNull public static Review findOrFail(int id) { return myEntityTable().findOrFail(id); }

    /** 
     * Try to finds an Object of type 'Review' in the database.
     * Identified by the primary key.
     * Ignoring caches and accessing the database
     * Returns <code>null</code> if is not present
     */
    @Nullable public static Review findPersisted(int id) { return myEntityTable().findPersisted(id); }

    /** 
     * Try to finds an Object of type 'Review' in the database.
     * Identified by the primary key.
     * Ignoring caches and accessing the database
     * Throws EntityNotFoundException if is not present
     */
    @NotNull public static Review findPersistedOrFail(int id) { return myEntityTable().findPersistedOrFail(id); }

    /** 
     * Try to finds an Object of type 'Review' in the database.
     * Identified by the primary key.
     * Returns <code>null</code> if is not present
     */
    @Nullable public static Review find(@NotNull String key) { return myEntityTable().findByString(key); }

    /** 
     * Try to finds an Object of type 'Review' in the database.
     * That verifies the specified condition.
     * Returns <code>null</code> if is not present
     */
    @Nullable public static Review findWhere(@NotNull Criteria... condition) { return selectFrom(REVIEW).where(condition).get(); }

    /** Create a selectFrom(REVIEW). */
    @NotNull public static Select<Review> list() { return selectFrom(REVIEW); }

    /** Performs the given action for each Review */
    public static void forEach(@Nullable Consumer<Review> consumer) { selectFrom(REVIEW).forEach(consumer); }

    /** List instances of 'Review' with the specified keys. */
    @NotNull public static ImmutableList<Review> list(@Nullable Set<Integer> keys) { return myEntityTable().list(keys); }

    /** List instances of 'Review' with the specified keys. */
    @NotNull public static ImmutableList<Review> list(@Nullable Iterable<String> keys) { return myEntityTable().listFromStringKeys(keys); }

    /** List the instances of 'Review' that verify the specified condition. */
    @NotNull public static Select<Review> listWhere(@NotNull Criteria condition) { return selectFrom(REVIEW).where(condition); }

    /** Insert specifying the primary key */
    public void insert(int key) {
        this.id = key;
        myEntityTable().insertDoNotGenerate((Review) this);
    }

    /** Register a Listener */
    public static void addListener(@NotNull EntityListenerType listenerType, @NotNull EntityListener<Review> listener) { myEntityTable().addListener(listenerType, listener); }

    /** Remove a Listener */
    public static void removeListener(@NotNull EntityListenerType listenerType, @NotNull EntityListener<Review> listener) { myEntityTable().removeListener(listenerType, listener); }

    /** List the instances of 'Review' that matches the given parameters. */
    @NotNull public static ImmutableList<Review> listByProduct(int productId) {
        return selectFrom(REVIEW).where(REVIEW.PRODUCT_ID.eq(productId)).list();
    }

    @NotNull public String keyAsString() { return String.valueOf(id); }

    @NotNull public Integer keyObject() { return id; }

    @Override @NotNull public final Seq<String> describe() { return formatList(getId()); }

    @Override @NotNull public String toString() { return "" + getId(); }

    /** Gets mapper for SQL statements */
    @NotNull public static RowMapper<Review> rowMapper() { return REVIEW.metadata().getRowMapper(); }

    @Override public void invalidate() { product.invalidate(); }

}
