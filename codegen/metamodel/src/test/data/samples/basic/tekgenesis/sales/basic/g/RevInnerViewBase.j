package tekgenesis.sales.basic.g;

import java.util.function.Consumer;
import tekgenesis.common.util.Conversions;
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
import tekgenesis.sales.basic.ProductDefaultViewInners;
import tekgenesis.sales.basic.RevInnerView;
import tekgenesis.database.RowMapper;
import tekgenesis.persistence.Select;
import java.util.Set;
import tekgenesis.common.core.Tuple;
import static tekgenesis.sales.basic.g.ProductDefaultViewInnersTable.PRODUCT_DEFAULT_VIEW_INNERS;
import static tekgenesis.sales.basic.g.RevInnerViewTable.REV_INNER_VIEW;
import static tekgenesis.persistence.Sql.selectFrom;

/** 
 * Generated base class for entity: RevInnerView.
 * Don't modify this as this is an auto generated class that's gets generated
 * every time the meta model file is modified. Use subclass instead.
 */
@SuppressWarnings({"DuplicateStringLiteralInspection", "WeakerAccess", "FieldMayBeFinal", "FieldCanBeLocal", "ConstantConditions", "ClassWithTooManyMethods", "ClassWithTooManyFields", "MagicNumber", "MethodOverridesStaticMethodOfSuperclass", "ParameterHidesMemberVariable", "FieldNameHidesFieldInSuperclass", "OverlyComplexClass", "RedundantCast", "UnusedReturnValue"})
public abstract class RevInnerViewBase
    extends EntityInstanceImpl<RevInnerView,Tuple<Integer,Integer>>
    implements InnerInstance<RevInnerView,Tuple<Integer,Integer>,ProductDefaultViewInners,Integer>
{

    //~ Fields ...................................................................................................................

    int productDefaultInnersId = 0;
    @NotNull EntityRef<ProductDefaultViewInners,Integer> productDefaultInners = new EntityRef<>(PRODUCT_DEFAULT_VIEW_INNERS, ProductDefaultViewInners::getRevsInner);
    int seqId = 0;
    @NotNull String rev = "";
    @NotNull DateTime updateTime = DateTime.EPOCH;

    //~ Methods ..................................................................................................................

    /** Returns the Product Default Inners Id. */
    public int getProductDefaultInnersId() { return this.productDefaultInnersId; }

    /** Returns the Product Default Inners. */
    @NotNull public ProductDefaultViewInners getProductDefaultInners() {
        return productDefaultInners.solveOrFail(this.productDefaultInnersId);
    }

    /** Returns the Seq Id. */
    public int getSeqId() { return this.seqId; }

    @Override @NotNull public EntityRef<ProductDefaultViewInners,Integer> parent() { return productDefaultInners; }

    @Override @NotNull public InnerEntitySeq<RevInnerView> siblings() { return getProductDefaultInners().getRevsInner(); }

    @Override public int seqId() { return getSeqId(); }

    /** Returns the Rev. */
    @NotNull public String getRev() { return this.rev; }

    /** Returns the Update Time. */
    @Override @NotNull public DateTime getUpdateTime() { return this.updateTime; }

    @NotNull private static EntityTable<RevInnerView,Tuple<Integer,Integer>> myEntityTable() { return EntityTable.forTable(REV_INNER_VIEW); }

    @NotNull public EntityTable<RevInnerView,Tuple<Integer,Integer>> et() { return myEntityTable(); }

    @Override @NotNull public DbTable<RevInnerView,Tuple<Integer,Integer>> table() { return REV_INNER_VIEW; }

    /** 
     * Try to finds an Object of type 'RevInnerView' in the database.
     * Identified by the primary key.
     * Returns <code>null</code> if is not present
     */
    @Nullable public static RevInnerView find(@NotNull Tuple<Integer,Integer> key) { return myEntityTable().find(key); }

    /** 
     * Try to finds an Object of type 'RevInnerView' in the database.
     * Identified by the primary key.
     * Throws EntityNotFoundException if is not present
     */
    @NotNull public static RevInnerView findOrFail(@NotNull Tuple<Integer,Integer> key) { return myEntityTable().findOrFail(key); }

    /** 
     * Try to finds an Object of type 'RevInnerView' in the database.
     * Identified by the primary key.
     * Ignoring caches and accessing the database
     * Returns <code>null</code> if is not present
     */
    @Nullable public static RevInnerView findPersisted(@NotNull Tuple<Integer,Integer> key) { return myEntityTable().findPersisted(key); }

    /** 
     * Try to finds an Object of type 'RevInnerView' in the database.
     * Identified by the primary key.
     * Ignoring caches and accessing the database
     * Throws EntityNotFoundException if is not present
     */
    @NotNull public static RevInnerView findPersistedOrFail(@NotNull Tuple<Integer,Integer> key) { return myEntityTable().findPersistedOrFail(key); }

    /** 
     * Try to finds an Object of type 'RevInnerView' in the database.
     * Identified by the primary key.
     * Returns <code>null</code> if is not present
     */
    @Nullable public static RevInnerView find(@NotNull String key) { return myEntityTable().findByString(key); }

    /** 
     * Try to finds an Object of type 'RevInnerView' in the database.
     * Identified by the primary key.
     * Returns <code>null</code> if is not present
     */
    @Nullable public static RevInnerView find(int productDefaultInnersId, int seqId) { return find(Tuple.tuple2(productDefaultInnersId, seqId)); }

    /** 
     * Try to finds an Object of type 'RevInnerView' in the database.
     * Identified by the primary key.
     * Returns <code>null</code> if is not present
     * Based on String key for Entities
     */
    @Nullable public static RevInnerView find(@NotNull String productDefaultInners, int seqId) { return find(Conversions.toInt(productDefaultInners), seqId); }

    /** 
     * Try to finds an Object of type 'RevInnerView' in the database.
     * That verifies the specified condition.
     * Returns <code>null</code> if is not present
     */
    @Nullable public static RevInnerView findWhere(@NotNull Criteria... condition) { return selectFrom(REV_INNER_VIEW).where(condition).get(); }

    /** Create a selectFrom(REV_INNER_VIEW). */
    @NotNull public static Select<RevInnerView> list() { return selectFrom(REV_INNER_VIEW); }

    /** Performs the given action for each RevInnerView */
    public static void forEach(@Nullable Consumer<RevInnerView> consumer) { selectFrom(REV_INNER_VIEW).forEach(consumer); }

    /** List instances of 'RevInnerView' with the specified keys. */
    @NotNull public static ImmutableList<RevInnerView> list(@Nullable Set<Tuple<Integer,Integer>> keys) { return myEntityTable().list(keys); }

    /** List instances of 'RevInnerView' with the specified keys. */
    @NotNull public static ImmutableList<RevInnerView> list(@Nullable Iterable<String> keys) { return myEntityTable().listFromStringKeys(keys); }

    /** List the instances of 'RevInnerView' that verify the specified condition. */
    @NotNull public static Select<RevInnerView> listWhere(@NotNull Criteria condition) { return selectFrom(REV_INNER_VIEW).where(condition); }

    @NotNull public String keyAsString() { return productDefaultInnersId + ":" + seqId; }

    @NotNull public Tuple<Integer,Integer> keyObject() { return Tuple.tuple2(productDefaultInnersId, seqId); }

    /** Gets mapper for SQL statements */
    @NotNull public static RowMapper<RevInnerView> rowMapper() { return REV_INNER_VIEW.metadata().getRowMapper(); }

    @Override public void invalidate() { productDefaultInners.invalidate(); }

}
