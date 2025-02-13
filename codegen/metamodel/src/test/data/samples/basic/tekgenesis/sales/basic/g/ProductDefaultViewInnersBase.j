package tekgenesis.sales.basic.g;

import tekgenesis.sales.basic.CategoryDefaultView;
import java.util.function.Consumer;
import tekgenesis.persistence.Criteria;
import tekgenesis.common.core.DateTime;
import tekgenesis.persistence.DbTable;
import tekgenesis.persistence.EntityInstance;
import tekgenesis.persistence.EntityInstanceImpl;
import tekgenesis.persistence.EntityRef;
import tekgenesis.persistence.EntityTable;
import tekgenesis.common.collections.ImmutableList;
import tekgenesis.persistence.InnerEntitySeq;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import tekgenesis.sales.basic.ProductDefaultViewInners;
import tekgenesis.common.core.Resource;
import tekgenesis.sales.basic.RevInnerView;
import tekgenesis.database.RowMapper;
import tekgenesis.persistence.Select;
import tekgenesis.common.collections.Seq;
import java.util.Set;
import static tekgenesis.sales.basic.g.CategoryDefaultViewTable.CATEGORY_DEFAULT_VIEW;
import static tekgenesis.sales.basic.g.ProductDefaultViewInnersTable.PRODUCT_DEFAULT_VIEW_INNERS;
import static tekgenesis.sales.basic.g.RevInnerViewTable.REV_INNER_VIEW;
import static tekgenesis.persistence.EntitySeq.createInnerEntitySeq;
import static tekgenesis.common.util.Conversions.formatList;
import static tekgenesis.persistence.Sql.selectFrom;

/** 
 * Generated base class for entity: ProductDefaultViewInners.
 * Don't modify this as this is an auto generated class that's gets generated
 * every time the meta model file is modified. Use subclass instead.
 */
@SuppressWarnings({"DuplicateStringLiteralInspection", "WeakerAccess", "FieldMayBeFinal", "FieldCanBeLocal", "ConstantConditions", "ClassWithTooManyMethods", "ClassWithTooManyFields", "MagicNumber", "MethodOverridesStaticMethodOfSuperclass", "ParameterHidesMemberVariable", "FieldNameHidesFieldInSuperclass", "OverlyComplexClass", "RedundantCast", "UnusedReturnValue"})
public abstract class ProductDefaultViewInnersBase
    extends EntityInstanceImpl<ProductDefaultViewInners,Integer>
    implements EntityInstance<ProductDefaultViewInners,Integer>
{

    //~ Fields ...................................................................................................................

    private int id = EntityTable.DEFAULT_EMPTY_KEY;
    @Nullable String vdescr = null;
    int vCategoryId = 0;
    @NotNull EntityRef<CategoryDefaultView,Integer> vCategory = new EntityRef<>(CATEGORY_DEFAULT_VIEW);
    @Nullable Resource image = null;
    @NotNull private InnerEntitySeq<RevInnerView> revsInner = createInnerEntitySeq(REV_INNER_VIEW, (ProductDefaultViewInners) this, c -> ((RevInnerViewBase)c).productDefaultInners);
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

    /** Returns the Revs Inner. */
    @NotNull public InnerEntitySeq<RevInnerView> getRevsInner() { return revsInner; }

    /** Returns the Update Time. */
    @Override @NotNull public DateTime getUpdateTime() { return this.updateTime; }

    /** Creates a new {@link ProductDefaultViewInners} instance. */
    @NotNull protected static ProductDefaultViewInners create() { return new ProductDefaultViewInners(); }

    @NotNull private static EntityTable<ProductDefaultViewInners,Integer> myEntityTable() { return EntityTable.forTable(PRODUCT_DEFAULT_VIEW_INNERS); }

    @NotNull public EntityTable<ProductDefaultViewInners,Integer> et() { return myEntityTable(); }

    @Override @NotNull public DbTable<ProductDefaultViewInners,Integer> table() { return PRODUCT_DEFAULT_VIEW_INNERS; }

    /** 
     * Try to finds an Object of type 'ProductDefaultViewInners' in the database.
     * Identified by the primary key.
     * Returns <code>null</code> if is not present
     */
    @Nullable public static ProductDefaultViewInners find(int id) { return myEntityTable().find(id); }

    /** 
     * Try to finds an Object of type 'ProductDefaultViewInners' in the database.
     * Identified by the primary key.
     * Throws EntityNotFoundException if is not present
     */
    @NotNull public static ProductDefaultViewInners findOrFail(int id) { return myEntityTable().findOrFail(id); }

    /** 
     * Try to finds an Object of type 'ProductDefaultViewInners' in the database.
     * Identified by the primary key.
     * Ignoring caches and accessing the database
     * Returns <code>null</code> if is not present
     */
    @Nullable public static ProductDefaultViewInners findPersisted(int id) { return myEntityTable().findPersisted(id); }

    /** 
     * Try to finds an Object of type 'ProductDefaultViewInners' in the database.
     * Identified by the primary key.
     * Ignoring caches and accessing the database
     * Throws EntityNotFoundException if is not present
     */
    @NotNull public static ProductDefaultViewInners findPersistedOrFail(int id) { return myEntityTable().findPersistedOrFail(id); }

    /** 
     * Try to finds an Object of type 'ProductDefaultViewInners' in the database.
     * Identified by the primary key.
     * Returns <code>null</code> if is not present
     */
    @Nullable public static ProductDefaultViewInners find(@NotNull String key) { return myEntityTable().findByString(key); }

    /** 
     * Try to finds an Object of type 'ProductDefaultViewInners' in the database.
     * That verifies the specified condition.
     * Returns <code>null</code> if is not present
     */
    @Nullable public static ProductDefaultViewInners findWhere(@NotNull Criteria... condition) {
        return selectFrom(PRODUCT_DEFAULT_VIEW_INNERS).where(condition).get();
    }

    /** Create a selectFrom(PRODUCT_DEFAULT_VIEW_INNERS). */
    @NotNull public static Select<ProductDefaultViewInners> list() { return selectFrom(PRODUCT_DEFAULT_VIEW_INNERS); }

    /** Performs the given action for each ProductDefaultViewInners */
    public static void forEach(@Nullable Consumer<ProductDefaultViewInners> consumer) { selectFrom(PRODUCT_DEFAULT_VIEW_INNERS).forEach(consumer); }

    /** List instances of 'ProductDefaultViewInners' with the specified keys. */
    @NotNull public static ImmutableList<ProductDefaultViewInners> list(@Nullable Set<Integer> keys) { return myEntityTable().list(keys); }

    /** List instances of 'ProductDefaultViewInners' with the specified keys. */
    @NotNull public static ImmutableList<ProductDefaultViewInners> list(@Nullable Iterable<String> keys) { return myEntityTable().listFromStringKeys(keys); }

    /** List the instances of 'ProductDefaultViewInners' that verify the specified condition. */
    @NotNull public static Select<ProductDefaultViewInners> listWhere(@NotNull Criteria condition) {
        return selectFrom(PRODUCT_DEFAULT_VIEW_INNERS).where(condition);
    }

    @NotNull public String keyAsString() { return String.valueOf(id); }

    @NotNull public Integer keyObject() { return id; }

    @Override @NotNull public final Seq<String> describe() { return formatList(getVdescr()); }

    @Override @NotNull public String toString() { return "" + (getVdescr()==null ? "" : getVdescr()); }

    /** Gets mapper for SQL statements */
    @NotNull public static RowMapper<ProductDefaultViewInners> rowMapper() {
        return PRODUCT_DEFAULT_VIEW_INNERS.metadata().getRowMapper();
    }

    @Override public void invalidate() {
        revsInner.invalidate();
        vCategory.invalidate();
    }

}
