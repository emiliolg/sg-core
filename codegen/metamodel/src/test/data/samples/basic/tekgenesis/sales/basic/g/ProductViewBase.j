package tekgenesis.sales.basic.g;

import tekgenesis.sales.basic.CategoryView;
import java.util.function.Consumer;
import tekgenesis.persistence.Criteria;
import tekgenesis.common.core.DateTime;
import tekgenesis.persistence.DbTable;
import tekgenesis.persistence.EntityInstance;
import tekgenesis.persistence.EntityInstanceImpl;
import tekgenesis.persistence.EntityRef;
import tekgenesis.persistence.EntityTable;
import java.util.EnumSet;
import tekgenesis.common.collections.ImmutableList;
import tekgenesis.persistence.InnerEntitySeq;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import tekgenesis.sales.basic.ProdByCatView;
import tekgenesis.sales.basic.ProductView;
import tekgenesis.database.RowMapper;
import tekgenesis.persistence.Select;
import tekgenesis.common.collections.Seq;
import tekgenesis.common.core.Strings;
import tekgenesis.sales.basic.Tag;
import static tekgenesis.sales.basic.g.CategoryViewTable.CATEGORY_VIEW;
import static tekgenesis.sales.basic.g.ProductViewTable.PRODUCT_VIEW;
import static tekgenesis.sales.basic.g.ProdByCatViewTable.PROD_BY_CAT_VIEW;
import static tekgenesis.persistence.EntitySeq.createInnerEntitySeq;
import static tekgenesis.common.util.Conversions.formatList;
import static tekgenesis.persistence.Sql.selectFrom;

/** 
 * Generated base class for entity: ProductView.
 * Don't modify this as this is an auto generated class that's gets generated
 * every time the meta model file is modified. Use subclass instead.
 */
@SuppressWarnings({"DuplicateStringLiteralInspection", "WeakerAccess", "FieldMayBeFinal", "FieldCanBeLocal", "ConstantConditions", "ClassWithTooManyMethods", "ClassWithTooManyFields", "MagicNumber", "MethodOverridesStaticMethodOfSuperclass", "ParameterHidesMemberVariable", "FieldNameHidesFieldInSuperclass", "OverlyComplexClass", "RedundantCast", "UnusedReturnValue"})
public abstract class ProductViewBase
    extends EntityInstanceImpl<ProductView,String>
    implements EntityInstance<ProductView,String>
{

    //~ Fields ...................................................................................................................

    @NotNull String vid = "";
    @Nullable String vdescr = null;
    long vcategoryVid = 0;
    @NotNull EntityRef<CategoryView,Long> vcategory = new EntityRef<>(CATEGORY_VIEW);
    @NotNull private InnerEntitySeq<ProdByCatView> secondary = createInnerEntitySeq(PROD_BY_CAT_VIEW, (ProductView) this, c -> ((ProdByCatViewBase)c).product);
    @NotNull String categoryAtt = "";
    @NotNull EnumSet<Tag> tags = EnumSet.noneOf(Tag.class);
    @NotNull DateTime updateTime = DateTime.EPOCH;

    //~ Methods ..................................................................................................................

    /** Returns the Vid. */
    @NotNull public String getVid() { return this.vid; }

    /** Returns the Vdescr. */
    @Nullable public String getVdescr() { return this.vdescr; }

    /** Returns the Vcategory Vid. */
    public long getVcategoryVid() { return this.vcategoryVid; }

    /** Returns the Vcategory. */
    @NotNull public CategoryView getVcategory() { return vcategory.solveOrFail(this.vcategoryVid); }

    /** Returns the Secondary. */
    @NotNull public InnerEntitySeq<ProdByCatView> getSecondary() { return secondary; }

    /** Returns the Category Att. */
    @NotNull public String getCategoryAtt() { return this.categoryAtt; }

    /** Returns the My Desc. */
    @NotNull public abstract String getMyDesc();

    /** Returns the Tags. */
    @NotNull public EnumSet<Tag> getTags() { return this.tags; }

    /** Returns the Update Time. */
    @Override @NotNull public DateTime getUpdateTime() { return this.updateTime; }

    /** Creates a new {@link ProductView} instance. */
    @NotNull protected static ProductView create(@NotNull String vid) {
        final ProductView result = new ProductView();
        ((ProductViewBase) result).vid = Strings.truncate(vid, 8);
        return result;
    }

    @NotNull private static EntityTable<ProductView,String> myEntityTable() { return EntityTable.forTable(PRODUCT_VIEW); }

    @NotNull public EntityTable<ProductView,String> et() { return myEntityTable(); }

    @Override @NotNull public DbTable<ProductView,String> table() { return PRODUCT_VIEW; }

    /** 
     * Try to finds an Object of type 'ProductView' in the database.
     * Identified by the primary key.
     * Returns <code>null</code> if is not present
     */
    @Nullable public static ProductView find(@NotNull String vid) { return myEntityTable().find(vid); }

    /** 
     * Try to finds an Object of type 'ProductView' in the database.
     * Identified by the primary key.
     * Throws EntityNotFoundException if is not present
     */
    @NotNull public static ProductView findOrFail(@NotNull String vid) { return myEntityTable().findOrFail(vid); }

    /** 
     * Try to finds an Object of type 'ProductView' in the database.
     * Identified by the primary key.
     * Ignoring caches and accessing the database
     * Returns <code>null</code> if is not present
     */
    @Nullable public static ProductView findPersisted(@NotNull String vid) { return myEntityTable().findPersisted(vid); }

    /** 
     * Try to finds an Object of type 'ProductView' in the database.
     * Identified by the primary key.
     * Ignoring caches and accessing the database
     * Throws EntityNotFoundException if is not present
     */
    @NotNull public static ProductView findPersistedOrFail(@NotNull String vid) { return myEntityTable().findPersistedOrFail(vid); }

    /** 
     * Try to finds an Object of type 'ProductView' in the database.
     * That verifies the specified condition.
     * Returns <code>null</code> if is not present
     */
    @Nullable public static ProductView findWhere(@NotNull Criteria... condition) { return selectFrom(PRODUCT_VIEW).where(condition).get(); }

    /** Create a selectFrom(PRODUCT_VIEW). */
    @NotNull public static Select<ProductView> list() { return selectFrom(PRODUCT_VIEW); }

    /** Performs the given action for each ProductView */
    public static void forEach(@Nullable Consumer<ProductView> consumer) { selectFrom(PRODUCT_VIEW).forEach(consumer); }

    /** List instances of 'ProductView' with the specified keys. */
    @NotNull public static ImmutableList<ProductView> list(@Nullable Iterable<String> keys) { return myEntityTable().listFromStringKeys(keys); }

    /** List the instances of 'ProductView' that verify the specified condition. */
    @NotNull public static Select<ProductView> listWhere(@NotNull Criteria condition) { return selectFrom(PRODUCT_VIEW).where(condition); }

    /** List the instances of 'ProductView' that matches the given parameters. */
    @NotNull public static ImmutableList<ProductView> listByVdescr(@NotNull String vdescr) {
        return selectFrom(PRODUCT_VIEW).where(PRODUCT_VIEW.VDESCR.eq(vdescr)).list();
    }

    @NotNull public String keyAsString() { return vid; }

    @NotNull public String keyObject() { return vid; }

    @Override @NotNull public final Seq<String> describe() { return formatList(getVdescr()); }

    @Override @NotNull public String toString() { return "" + (getVdescr()==null ? "" : getVdescr()); }

    /** Gets mapper for SQL statements */
    @NotNull public static RowMapper<ProductView> rowMapper() { return PRODUCT_VIEW.metadata().getRowMapper(); }

    @Override public void invalidate() {
        secondary.invalidate();
        vcategory.invalidate();
    }

}
