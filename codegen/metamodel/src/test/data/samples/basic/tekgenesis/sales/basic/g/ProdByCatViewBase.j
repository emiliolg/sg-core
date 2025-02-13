package tekgenesis.sales.basic.g;

import tekgenesis.sales.basic.CategoryView;
import java.util.function.Consumer;
import tekgenesis.persistence.Criteria;
import tekgenesis.common.core.DateTime;
import tekgenesis.persistence.DbTable;
import tekgenesis.persistence.EntityInstanceImpl;
import tekgenesis.persistence.EntityRef;
import tekgenesis.persistence.EntityTable;
import tekgenesis.common.collections.ImmutableList;
import tekgenesis.persistence.InnerEntitySeq;
import tekgenesis.persistence.InnerInstance;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import tekgenesis.sales.basic.ProdByCatView;
import tekgenesis.sales.basic.ProductView;
import tekgenesis.database.RowMapper;
import tekgenesis.persistence.Select;
import java.util.Set;
import tekgenesis.common.core.Strings;
import tekgenesis.common.core.Tuple;
import static tekgenesis.sales.basic.g.CategoryViewTable.CATEGORY_VIEW;
import static tekgenesis.sales.basic.g.ProductViewTable.PRODUCT_VIEW;
import static tekgenesis.sales.basic.g.ProdByCatViewTable.PROD_BY_CAT_VIEW;
import static tekgenesis.persistence.Sql.selectFrom;

/** 
 * Generated base class for entity: ProdByCatView.
 * Don't modify this as this is an auto generated class that's gets generated
 * every time the meta model file is modified. Use subclass instead.
 */
@SuppressWarnings({"DuplicateStringLiteralInspection", "WeakerAccess", "FieldMayBeFinal", "FieldCanBeLocal", "ConstantConditions", "ClassWithTooManyMethods", "ClassWithTooManyFields", "MagicNumber", "MethodOverridesStaticMethodOfSuperclass", "ParameterHidesMemberVariable", "FieldNameHidesFieldInSuperclass", "OverlyComplexClass", "RedundantCast", "UnusedReturnValue"})
public abstract class ProdByCatViewBase
    extends EntityInstanceImpl<ProdByCatView,Tuple<String,Integer>>
    implements InnerInstance<ProdByCatView,Tuple<String,Integer>,ProductView,String>
{

    //~ Fields ...................................................................................................................

    @NotNull String productVid = "";
    @NotNull EntityRef<ProductView,String> product = new EntityRef<>(PRODUCT_VIEW, ProductView::getSecondary);
    int seqId = 0;
    long catVid = 0;
    @NotNull EntityRef<CategoryView,Long> cat = new EntityRef<>(CATEGORY_VIEW);
    @NotNull DateTime updateTime = DateTime.EPOCH;

    //~ Methods ..................................................................................................................

    /** Returns the Product Vid. */
    @NotNull public String getProductVid() { return this.productVid; }

    /** Returns the Product. */
    @NotNull public ProductView getProduct() { return product.solveOrFail(this.productVid); }

    /** Returns the Seq Id. */
    public int getSeqId() { return this.seqId; }

    @Override @NotNull public EntityRef<ProductView,String> parent() { return product; }

    @Override @NotNull public InnerEntitySeq<ProdByCatView> siblings() { return getProduct().getSecondary(); }

    @Override public int seqId() { return getSeqId(); }

    /** Returns the Cat Vid. */
    public long getCatVid() { return this.catVid; }

    /** Returns the Cat. */
    @NotNull public CategoryView getCat() { return cat.solveOrFail(this.catVid); }

    /** Returns the Update Time. */
    @Override @NotNull public DateTime getUpdateTime() { return this.updateTime; }

    @NotNull private static EntityTable<ProdByCatView,Tuple<String,Integer>> myEntityTable() { return EntityTable.forTable(PROD_BY_CAT_VIEW); }

    @NotNull public EntityTable<ProdByCatView,Tuple<String,Integer>> et() { return myEntityTable(); }

    @Override @NotNull public DbTable<ProdByCatView,Tuple<String,Integer>> table() { return PROD_BY_CAT_VIEW; }

    /** 
     * Try to finds an Object of type 'ProdByCatView' in the database.
     * Identified by the primary key.
     * Returns <code>null</code> if is not present
     */
    @Nullable public static ProdByCatView find(@NotNull Tuple<String,Integer> key) { return myEntityTable().find(key); }

    /** 
     * Try to finds an Object of type 'ProdByCatView' in the database.
     * Identified by the primary key.
     * Throws EntityNotFoundException if is not present
     */
    @NotNull public static ProdByCatView findOrFail(@NotNull Tuple<String,Integer> key) { return myEntityTable().findOrFail(key); }

    /** 
     * Try to finds an Object of type 'ProdByCatView' in the database.
     * Identified by the primary key.
     * Ignoring caches and accessing the database
     * Returns <code>null</code> if is not present
     */
    @Nullable public static ProdByCatView findPersisted(@NotNull Tuple<String,Integer> key) { return myEntityTable().findPersisted(key); }

    /** 
     * Try to finds an Object of type 'ProdByCatView' in the database.
     * Identified by the primary key.
     * Ignoring caches and accessing the database
     * Throws EntityNotFoundException if is not present
     */
    @NotNull public static ProdByCatView findPersistedOrFail(@NotNull Tuple<String,Integer> key) { return myEntityTable().findPersistedOrFail(key); }

    /** 
     * Try to finds an Object of type 'ProdByCatView' in the database.
     * Identified by the primary key.
     * Returns <code>null</code> if is not present
     */
    @Nullable public static ProdByCatView find(@NotNull String key) { return myEntityTable().findByString(key); }

    /** 
     * Try to finds an Object of type 'ProdByCatView' in the database.
     * Identified by the primary key.
     * Returns <code>null</code> if is not present
     */
    @Nullable public static ProdByCatView find(@NotNull String productVid, int seqId) { return find(Tuple.tuple2(productVid, seqId)); }

    /** 
     * Try to finds an Object of type 'ProdByCatView' in the database.
     * That verifies the specified condition.
     * Returns <code>null</code> if is not present
     */
    @Nullable public static ProdByCatView findWhere(@NotNull Criteria... condition) { return selectFrom(PROD_BY_CAT_VIEW).where(condition).get(); }

    /** Create a selectFrom(PROD_BY_CAT_VIEW). */
    @NotNull public static Select<ProdByCatView> list() { return selectFrom(PROD_BY_CAT_VIEW); }

    /** Performs the given action for each ProdByCatView */
    public static void forEach(@Nullable Consumer<ProdByCatView> consumer) { selectFrom(PROD_BY_CAT_VIEW).forEach(consumer); }

    /** List instances of 'ProdByCatView' with the specified keys. */
    @NotNull public static ImmutableList<ProdByCatView> list(@Nullable Set<Tuple<String,Integer>> keys) { return myEntityTable().list(keys); }

    /** List instances of 'ProdByCatView' with the specified keys. */
    @NotNull public static ImmutableList<ProdByCatView> list(@Nullable Iterable<String> keys) { return myEntityTable().listFromStringKeys(keys); }

    /** List the instances of 'ProdByCatView' that verify the specified condition. */
    @NotNull public static Select<ProdByCatView> listWhere(@NotNull Criteria condition) { return selectFrom(PROD_BY_CAT_VIEW).where(condition); }

    @NotNull public String keyAsString() { return Strings.escapeCharOn(productVid, ':') + ":" + seqId; }

    @NotNull public Tuple<String,Integer> keyObject() { return Tuple.tuple2(productVid, seqId); }

    /** Gets mapper for SQL statements */
    @NotNull public static RowMapper<ProdByCatView> rowMapper() { return PROD_BY_CAT_VIEW.metadata().getRowMapper(); }

    @Override public void invalidate() {
        product.invalidate();
        cat.invalidate();
    }

}
