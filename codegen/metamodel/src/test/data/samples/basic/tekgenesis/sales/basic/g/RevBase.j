package tekgenesis.sales.basic.g;

import tekgenesis.persistence.AuditableInstance;
import java.util.function.Consumer;
import tekgenesis.common.util.Conversions;
import tekgenesis.persistence.Criteria;
import tekgenesis.common.core.DateTime;
import tekgenesis.persistence.DbTable;
import tekgenesis.persistence.EntityInstanceImpl;
import tekgenesis.persistence.EntityListener;
import tekgenesis.persistence.EntityListenerType;
import tekgenesis.persistence.EntityRef;
import tekgenesis.persistence.EntityTable;
import tekgenesis.common.collections.ImmutableList;
import tekgenesis.persistence.InnerEntitySeq;
import tekgenesis.persistence.InnerInstance;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import tekgenesis.sales.basic.ProductDefaultInners;
import tekgenesis.sales.basic.Rev;
import tekgenesis.database.RowMapper;
import tekgenesis.persistence.Select;
import tekgenesis.common.collections.Seq;
import java.util.Set;
import tekgenesis.common.core.Strings;
import tekgenesis.common.core.Tuple;
import static tekgenesis.sales.basic.g.ProductDefaultInnersTable.PRODUCT_DEFAULT_INNERS;
import static tekgenesis.sales.basic.g.RevTable.REV;
import static tekgenesis.common.util.Conversions.formatList;
import static tekgenesis.persistence.Sql.selectFrom;

/** 
 * Generated base class for entity: Rev.
 * Don't modify this as this is an auto generated class that's gets generated
 * every time the meta model file is modified. Use subclass instead.
 */
@SuppressWarnings({"DuplicateStringLiteralInspection", "WeakerAccess", "FieldMayBeFinal", "FieldCanBeLocal", "ConstantConditions", "ClassWithTooManyMethods", "ClassWithTooManyFields", "MagicNumber", "MethodOverridesStaticMethodOfSuperclass", "ParameterHidesMemberVariable", "FieldNameHidesFieldInSuperclass", "OverlyComplexClass", "RedundantCast", "UnusedReturnValue"})
public abstract class RevBase
    extends EntityInstanceImpl<Rev,Tuple<Integer,Integer>>
    implements InnerInstance<Rev,Tuple<Integer,Integer>,ProductDefaultInners,Integer>, AuditableInstance
{

    //~ Fields ...................................................................................................................

    int productDefaultInnersId = 0;
    @NotNull EntityRef<ProductDefaultInners,Integer> productDefaultInners = new EntityRef<>(PRODUCT_DEFAULT_INNERS, ProductDefaultInners::getReviews);
    int seqId = 0;
    @NotNull String review = "";
    @NotNull DateTime updateTime = DateTime.EPOCH;
    @NotNull DateTime creationTime = DateTime.EPOCH;
    @Nullable String creationUser = null;
    @Nullable String updateUser = null;

    //~ Methods ..................................................................................................................

    /** Returns the Product Default Inners Id. */
    public int getProductDefaultInnersId() { return this.productDefaultInnersId; }

    /** Returns the Product Default Inners. */
    @NotNull public ProductDefaultInners getProductDefaultInners() {
        return productDefaultInners.solveOrFail(this.productDefaultInnersId);
    }

    /** Returns the Seq Id. */
    public int getSeqId() { return this.seqId; }

    @Override @NotNull public EntityRef<ProductDefaultInners,Integer> parent() { return productDefaultInners; }

    @Override @NotNull public InnerEntitySeq<Rev> siblings() { return getProductDefaultInners().getReviews(); }

    @Override public int seqId() { return getSeqId(); }

    /** Returns the Review. */
    @NotNull public String getReview() { return this.review; }

    /** Sets the value of the Review. */
    @NotNull public Rev setReview(@NotNull String review) {
        markAsModified();
        this.review = Strings.truncate(review, 255);
        return (Rev) this;
    }

    /** Returns the Update Time. */
    @Override @NotNull public DateTime getUpdateTime() { return this.updateTime; }

    /** Returns the Creation Time. */
    @NotNull public DateTime getCreationTime() { return this.creationTime; }

    /** Returns the Creation User. */
    @Nullable public String getCreationUser() { return this.creationUser; }

    /** Returns the Update User. */
    @Nullable public String getUpdateUser() { return this.updateUser; }

    @NotNull private static EntityTable<Rev,Tuple<Integer,Integer>> myEntityTable() { return EntityTable.forTable(REV); }

    @NotNull public EntityTable<Rev,Tuple<Integer,Integer>> et() { return myEntityTable(); }

    @Override @NotNull public DbTable<Rev,Tuple<Integer,Integer>> table() { return REV; }

    /** 
     * Try to finds an Object of type 'Rev' in the database.
     * Identified by the primary key.
     * Returns <code>null</code> if is not present
     */
    @Nullable public static Rev find(@NotNull Tuple<Integer,Integer> key) { return myEntityTable().find(key); }

    /** 
     * Try to finds an Object of type 'Rev' in the database.
     * Identified by the primary key.
     * Throws EntityNotFoundException if is not present
     */
    @NotNull public static Rev findOrFail(@NotNull Tuple<Integer,Integer> key) { return myEntityTable().findOrFail(key); }

    /** 
     * Try to finds an Object of type 'Rev' in the database.
     * Identified by the primary key.
     * Ignoring caches and accessing the database
     * Returns <code>null</code> if is not present
     */
    @Nullable public static Rev findPersisted(@NotNull Tuple<Integer,Integer> key) { return myEntityTable().findPersisted(key); }

    /** 
     * Try to finds an Object of type 'Rev' in the database.
     * Identified by the primary key.
     * Ignoring caches and accessing the database
     * Throws EntityNotFoundException if is not present
     */
    @NotNull public static Rev findPersistedOrFail(@NotNull Tuple<Integer,Integer> key) { return myEntityTable().findPersistedOrFail(key); }

    /** 
     * Try to finds an Object of type 'Rev' in the database.
     * Identified by the primary key.
     * Returns <code>null</code> if is not present
     */
    @Nullable public static Rev find(@NotNull String key) { return myEntityTable().findByString(key); }

    /** 
     * Try to finds an Object of type 'Rev' in the database.
     * Identified by the primary key.
     * Returns <code>null</code> if is not present
     */
    @Nullable public static Rev find(int productDefaultInnersId, int seqId) { return find(Tuple.tuple2(productDefaultInnersId, seqId)); }

    /** 
     * Try to finds an Object of type 'Rev' in the database.
     * Identified by the primary key.
     * Returns <code>null</code> if is not present
     * Based on String key for Entities
     */
    @Nullable public static Rev find(@NotNull String productDefaultInners, int seqId) { return find(Conversions.toInt(productDefaultInners), seqId); }

    /** 
     * Try to finds an Object of type 'Rev' in the database.
     * That verifies the specified condition.
     * Returns <code>null</code> if is not present
     */
    @Nullable public static Rev findWhere(@NotNull Criteria... condition) { return selectFrom(REV).where(condition).get(); }

    /** Create a selectFrom(REV). */
    @NotNull public static Select<Rev> list() { return selectFrom(REV); }

    /** Performs the given action for each Rev */
    public static void forEach(@Nullable Consumer<Rev> consumer) { selectFrom(REV).forEach(consumer); }

    /** List instances of 'Rev' with the specified keys. */
    @NotNull public static ImmutableList<Rev> list(@Nullable Set<Tuple<Integer,Integer>> keys) { return myEntityTable().list(keys); }

    /** List instances of 'Rev' with the specified keys. */
    @NotNull public static ImmutableList<Rev> list(@Nullable Iterable<String> keys) { return myEntityTable().listFromStringKeys(keys); }

    /** List the instances of 'Rev' that verify the specified condition. */
    @NotNull public static Select<Rev> listWhere(@NotNull Criteria condition) { return selectFrom(REV).where(condition); }

    @Override @NotNull public final Rev update() { return InnerInstance.super.update(); }

    @Override @NotNull public final Rev insert() { return InnerInstance.super.insert(); }

    /** Register a Listener */
    public static void addListener(@NotNull EntityListenerType listenerType, @NotNull EntityListener<Rev> listener) { myEntityTable().addListener(listenerType, listener); }

    /** Remove a Listener */
    public static void removeListener(@NotNull EntityListenerType listenerType, @NotNull EntityListener<Rev> listener) { myEntityTable().removeListener(listenerType, listener); }

    @NotNull public String keyAsString() { return productDefaultInnersId + ":" + seqId; }

    @NotNull public Tuple<Integer,Integer> keyObject() { return Tuple.tuple2(productDefaultInnersId, seqId); }

    @Override @NotNull public final Seq<String> describe() { return formatList(getProductDefaultInners(), getSeqId()); }

    @Override @NotNull public String toString() { return "" + getProductDefaultInners() + " " + getSeqId(); }

    /** Gets mapper for SQL statements */
    @NotNull public static RowMapper<Rev> rowMapper() { return REV.metadata().getRowMapper(); }

    @Override public void invalidate() { productDefaultInners.invalidate(); }

}
