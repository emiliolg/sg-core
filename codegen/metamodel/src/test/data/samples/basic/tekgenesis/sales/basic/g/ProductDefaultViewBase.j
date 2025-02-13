package tekgenesis.sales.basic.g;

import java.math.BigDecimal;
import tekgenesis.sales.basic.CategoryDefaultView;
import java.util.function.Consumer;
import tekgenesis.persistence.Criteria;
import tekgenesis.common.core.DateTime;
import tekgenesis.persistence.DbTable;
import tekgenesis.persistence.EntityInstance;
import tekgenesis.persistence.EntityInstanceImpl;
import tekgenesis.persistence.EntityRef;
import tekgenesis.persistence.EntitySeq;
import tekgenesis.persistence.EntityTable;
import tekgenesis.common.collections.ImmutableList;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import tekgenesis.sales.basic.ProductDefaultView;
import tekgenesis.common.core.Resource;
import tekgenesis.sales.basic.RevView;
import tekgenesis.database.RowMapper;
import tekgenesis.persistence.Select;
import tekgenesis.common.collections.Seq;
import java.util.Set;
import tekgenesis.sales.basic.State;
import static tekgenesis.sales.basic.g.CategoryDefaultViewTable.CATEGORY_DEFAULT_VIEW;
import static tekgenesis.sales.basic.g.ProductDefaultViewTable.PRODUCT_DEFAULT_VIEW;
import static tekgenesis.sales.basic.g.RevViewTable.REV_VIEW;
import static tekgenesis.persistence.EntitySeq.createEntitySeq;
import static tekgenesis.common.util.Conversions.formatList;
import static tekgenesis.common.collections.Colls.listOf;
import static tekgenesis.persistence.Sql.selectFrom;

/** 
 * Generated base class for entity: ProductDefaultView.
 * Don't modify this as this is an auto generated class that's gets generated
 * every time the meta model file is modified. Use subclass instead.
 */
@SuppressWarnings({"DuplicateStringLiteralInspection", "WeakerAccess", "FieldMayBeFinal", "FieldCanBeLocal", "ConstantConditions", "ClassWithTooManyMethods", "ClassWithTooManyFields", "MagicNumber", "MethodOverridesStaticMethodOfSuperclass", "ParameterHidesMemberVariable", "FieldNameHidesFieldInSuperclass", "OverlyComplexClass", "RedundantCast", "UnusedReturnValue"})
public abstract class ProductDefaultViewBase
    extends EntityInstanceImpl<ProductDefaultView,Integer>
    implements EntityInstance<ProductDefaultView,Integer>
{

    //~ Fields ...................................................................................................................

    private int id = EntityTable.DEFAULT_EMPTY_KEY;
    @Nullable String vdescr = null;
    int vCategoryId = 0;
    @NotNull EntityRef<CategoryDefaultView,Integer> vCategory = new EntityRef<>(CATEGORY_DEFAULT_VIEW);
    @Nullable Resource image = null;
    @NotNull State state = State.CREATED;
    @NotNull BigDecimal price = BigDecimal.ZERO;
    @NotNull private EntitySeq<RevView> revs = createEntitySeq(REV_VIEW, (ProductDefaultView) this, c -> ((RevViewBase)c).prod, listOf(REV_VIEW.PROD_ID));
    @NotNull String comments = "";
    @NotNull DateTime updateTime = DateTime.EPOCH;

    //~ Methods ..................................................................................................................

    /** Returns the Id. */
    public int getId() { return this.id; }

    @Override public boolean hasEmptyKey() { return id == EntityTable.DEFAULT_EMPTY_KEY; }

    /** Returns the Vdescr. */
    @Nullable public String getVdescr() { return this.vdescr; }

    /** Returns the V Category Id. */
    public int getVCategoryId() { return this.vCategoryId; }

    /** Returns the V Category. */
    @NotNull public CategoryDefaultView getVCategory() { return vCategory.solveOrFail(this.vCategoryId); }

    /** Returns the Image. */
    @Nullable public Resource getImage() { return this.image; }

    /** Returns the State. */
    @NotNull public State getState() { return this.state; }

    /** Returns the Price. */
    @NotNull public BigDecimal getPrice() { return this.price; }

    /** Returns the Revs. */
    @NotNull public EntitySeq<RevView> getRevs() { return revs; }

    /** Returns the Comments. */
    @NotNull public String getComments() { return this.comments; }

    /** Returns the Update Time. */
    @Override @NotNull public DateTime getUpdateTime() { return this.updateTime; }

    /** Creates a new {@link ProductDefaultView} instance. */
    @NotNull protected static ProductDefaultView create() { return new ProductDefaultView(); }

    @NotNull private static EntityTable<ProductDefaultView,Integer> myEntityTable() { return EntityTable.forTable(PRODUCT_DEFAULT_VIEW); }

    @NotNull public EntityTable<ProductDefaultView,Integer> et() { return myEntityTable(); }

    @Override @NotNull public DbTable<ProductDefaultView,Integer> table() { return PRODUCT_DEFAULT_VIEW; }

    /** 
     * Try to finds an Object of type 'ProductDefaultView' in the database.
     * Identified by the primary key.
     * Returns <code>null</code> if is not present
     */
    @Nullable public static ProductDefaultView find(int id) { return myEntityTable().find(id); }

    /** 
     * Try to finds an Object of type 'ProductDefaultView' in the database.
     * Identified by the primary key.
     * Throws EntityNotFoundException if is not present
     */
    @NotNull public static ProductDefaultView findOrFail(int id) { return myEntityTable().findOrFail(id); }

    /** 
     * Try to finds an Object of type 'ProductDefaultView' in the database.
     * Identified by the primary key.
     * Ignoring caches and accessing the database
     * Returns <code>null</code> if is not present
     */
    @Nullable public static ProductDefaultView findPersisted(int id) { return myEntityTable().findPersisted(id); }

    /** 
     * Try to finds an Object of type 'ProductDefaultView' in the database.
     * Identified by the primary key.
     * Ignoring caches and accessing the database
     * Throws EntityNotFoundException if is not present
     */
    @NotNull public static ProductDefaultView findPersistedOrFail(int id) { return myEntityTable().findPersistedOrFail(id); }

    /** 
     * Try to finds an Object of type 'ProductDefaultView' in the database.
     * Identified by the primary key.
     * Returns <code>null</code> if is not present
     */
    @Nullable public static ProductDefaultView find(@NotNull String key) { return myEntityTable().findByString(key); }

    /** 
     * Try to finds an Object of type 'ProductDefaultView' in the database.
     * That verifies the specified condition.
     * Returns <code>null</code> if is not present
     */
    @Nullable public static ProductDefaultView findWhere(@NotNull Criteria... condition) {
        return selectFrom(PRODUCT_DEFAULT_VIEW).where(condition).get();
    }

    /** Create a selectFrom(PRODUCT_DEFAULT_VIEW). */
    @NotNull public static Select<ProductDefaultView> list() { return selectFrom(PRODUCT_DEFAULT_VIEW); }

    /** Performs the given action for each ProductDefaultView */
    public static void forEach(@Nullable Consumer<ProductDefaultView> consumer) { selectFrom(PRODUCT_DEFAULT_VIEW).forEach(consumer); }

    /** List instances of 'ProductDefaultView' with the specified keys. */
    @NotNull public static ImmutableList<ProductDefaultView> list(@Nullable Set<Integer> keys) { return myEntityTable().list(keys); }

    /** List instances of 'ProductDefaultView' with the specified keys. */
    @NotNull public static ImmutableList<ProductDefaultView> list(@Nullable Iterable<String> keys) { return myEntityTable().listFromStringKeys(keys); }

    /** List the instances of 'ProductDefaultView' that verify the specified condition. */
    @NotNull public static Select<ProductDefaultView> listWhere(@NotNull Criteria condition) { return selectFrom(PRODUCT_DEFAULT_VIEW).where(condition); }

    @NotNull public String keyAsString() { return String.valueOf(id); }

    @NotNull public Integer keyObject() { return id; }

    @Override @NotNull public final Seq<String> describe() { return formatList(getVdescr()); }

    @Override @NotNull public String toString() { return "" + (getVdescr()==null ? "" : getVdescr()); }

    /** Gets mapper for SQL statements */
    @NotNull public static RowMapper<ProductDefaultView> rowMapper() { return PRODUCT_DEFAULT_VIEW.metadata().getRowMapper(); }

    @Override public void invalidate() {
        revs.invalidate();
        vCategory.invalidate();
    }

}
