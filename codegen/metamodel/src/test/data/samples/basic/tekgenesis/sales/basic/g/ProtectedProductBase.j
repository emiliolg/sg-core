package tekgenesis.sales.basic.g;

import java.math.BigDecimal;
import tekgenesis.sales.basic.Category;
import tekgenesis.common.core.DateTime;
import tekgenesis.persistence.DbTable;
import tekgenesis.common.core.Decimals;
import tekgenesis.persistence.EntityInstanceImpl;
import tekgenesis.persistence.EntityRef;
import tekgenesis.persistence.EntityTable;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import tekgenesis.persistence.PersistableInstance;
import tekgenesis.sales.basic.ProtectedProduct;
import tekgenesis.database.RowMapper;
import tekgenesis.common.collections.Seq;
import tekgenesis.sales.basic.State;
import tekgenesis.common.core.Strings;
import static tekgenesis.sales.basic.g.CategoryTable.CATEGORY;
import static tekgenesis.sales.basic.g.ProtectedProductTable.PROTECTED_PRODUCT;
import static tekgenesis.common.util.Conversions.formatList;

/** 
 * Generated base class for entity: ProtectedProduct.
 * Don't modify this as this is an auto generated class that's gets generated
 * every time the meta model file is modified. Use subclass instead.
 */
@SuppressWarnings({"DuplicateStringLiteralInspection", "WeakerAccess", "FieldMayBeFinal", "FieldCanBeLocal", "ConstantConditions", "ClassWithTooManyMethods", "ClassWithTooManyFields", "MagicNumber", "MethodOverridesStaticMethodOfSuperclass", "ParameterHidesMemberVariable", "FieldNameHidesFieldInSuperclass", "OverlyComplexClass", "RedundantCast", "UnusedReturnValue"})
public abstract class ProtectedProductBase
    extends EntityInstanceImpl<ProtectedProduct,String>
    implements PersistableInstance<ProtectedProduct,String>
{

    //~ Fields ...................................................................................................................

    @NotNull String productId = "";
    @NotNull String model = "";
    @Nullable String description = null;
    @NotNull BigDecimal price = BigDecimal.ZERO;
    @NotNull State state = State.CREATED;
    long categoryIdKey = 0;
    @NotNull EntityRef<Category,Long> category = new EntityRef<>(CATEGORY);
    @NotNull DateTime updateTime = DateTime.EPOCH;

    //~ Methods ..................................................................................................................

    /** Returns the Product Id. */
    @NotNull protected String getProductId() { return this.productId; }

    /** Returns the Model. */
    @NotNull protected String getModel() { return this.model; }

    /** Sets the value of the Model. */
    @NotNull protected ProtectedProduct setModel(@NotNull String model) {
        markAsModified();
        this.model = Strings.truncate(model, 30);
        return (ProtectedProduct) this;
    }

    /** Returns the Description. */
    @Nullable protected String getDescription() { return this.description; }

    /** Sets the value of the Description. */
    @NotNull protected ProtectedProduct setDescription(@Nullable String description) {
        markAsModified();
        this.description = Strings.truncate(description, 100);
        return (ProtectedProduct) this;
    }

    /** Returns the Price. */
    @NotNull protected BigDecimal getPrice() { return this.price; }

    /** Sets the value of the Price. */
    @NotNull protected ProtectedProduct setPrice(@NotNull BigDecimal price) {
        markAsModified();
        this.price = Decimals.scaleAndCheck("price", price, false, 10, 2);
        return (ProtectedProduct) this;
    }

    /** Returns the State. */
    @NotNull protected State getState() { return this.state; }

    /** Sets the value of the State. */
    @NotNull protected ProtectedProduct setState(@NotNull State state) {
        markAsModified();
        this.state = state;
        return (ProtectedProduct) this;
    }

    /** Returns the Category Id Key. */
    protected long getCategoryIdKey() { return this.categoryIdKey; }

    /** Returns the Category. */
    @NotNull protected Category getCategory() { return category.solveOrFail(this.categoryIdKey); }

    /** Sets the value of the Category Id Key. */
    @NotNull protected ProtectedProduct setCategoryIdKey(long categoryIdKey) {
        category.invalidate();
        this.categoryIdKey = categoryIdKey;
        return (ProtectedProduct) this;
    }

    /** Sets the value of the Category. */
    @NotNull protected ProtectedProduct setCategory(@NotNull Category category) {
        this.category.set(category);
        this.categoryIdKey = category.getIdKey();
        return (ProtectedProduct) this;
    }

    /** Returns the Update Time. */
    @Override @NotNull public DateTime getUpdateTime() { return this.updateTime; }

    /** Creates a new {@link ProtectedProduct} instance. */
    @NotNull public static ProtectedProduct create(@NotNull String productId) {
        final ProtectedProduct result = new ProtectedProduct();
        ((ProtectedProductBase) result).productId = Strings.truncate(productId, 8);
        return result;
    }

    /** 
     * Find (or create if not present) a 'ProtectedProduct' in the database.
     * Identified by the primary key.
     */
    @NotNull public static ProtectedProduct findOrCreate(@NotNull String productId) { return myEntityTable().findOrCreate(productId); }

    @NotNull private static EntityTable<ProtectedProduct,String> myEntityTable() { return EntityTable.forTable(PROTECTED_PRODUCT); }

    @NotNull public EntityTable<ProtectedProduct,String> et() { return myEntityTable(); }

    @Override @NotNull public DbTable<ProtectedProduct,String> table() { return PROTECTED_PRODUCT; }

    @NotNull public String keyAsString() { return productId; }

    @NotNull public String keyObject() { return productId; }

    @Override @NotNull public final Seq<String> describe() { return formatList(getDescription()); }

    @Override @NotNull public String toString() {
        return "" + (getDescription()==null ? "" : getDescription());
    }

    /** Gets mapper for SQL statements */
    @NotNull public static RowMapper<ProtectedProduct> rowMapper() { return PROTECTED_PRODUCT.metadata().getRowMapper(); }

    @Override public void invalidate() { category.invalidate(); }

}
