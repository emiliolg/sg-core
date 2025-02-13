package tekgenesis.sales.basic.g;

import java.util.function.Consumer;
import tekgenesis.persistence.Criteria;
import tekgenesis.common.core.DateTime;
import tekgenesis.persistence.DbTable;
import tekgenesis.persistence.EntityInstanceImpl;
import tekgenesis.persistence.EntityListener;
import tekgenesis.persistence.EntityListenerType;
import tekgenesis.persistence.EntityRef;
import tekgenesis.persistence.EntityTable;
import tekgenesis.sales.basic.Image;
import tekgenesis.common.collections.ImmutableList;
import tekgenesis.persistence.InnerEntitySeq;
import tekgenesis.persistence.InnerInstance;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import tekgenesis.sales.basic.Product;
import tekgenesis.common.core.Resource;
import tekgenesis.database.RowMapper;
import tekgenesis.persistence.Select;
import tekgenesis.common.collections.Seq;
import java.util.Set;
import tekgenesis.common.core.Strings;
import tekgenesis.common.core.Tuple;
import static tekgenesis.sales.basic.g.ImageTable.IMAGE;
import static tekgenesis.sales.basic.g.ProductTable.PRODUCT;
import static tekgenesis.common.util.Conversions.formatList;
import static tekgenesis.persistence.Sql.selectFrom;

/** 
 * Generated base class for entity: Image.
 * Don't modify this as this is an auto generated class that's gets generated
 * every time the meta model file is modified. Use subclass instead.
 */
@SuppressWarnings({"DuplicateStringLiteralInspection", "WeakerAccess", "FieldMayBeFinal", "FieldCanBeLocal", "ConstantConditions", "ClassWithTooManyMethods", "ClassWithTooManyFields", "MagicNumber", "MethodOverridesStaticMethodOfSuperclass", "ParameterHidesMemberVariable", "FieldNameHidesFieldInSuperclass", "OverlyComplexClass", "RedundantCast", "UnusedReturnValue"})
public abstract class ImageBase
    extends EntityInstanceImpl<Image,Tuple<String,Integer>>
    implements InnerInstance<Image,Tuple<String,Integer>,Product,String>
{

    //~ Fields ...................................................................................................................

    @NotNull String productProductId = "";
    @NotNull EntityRef<Product,String> product = new EntityRef<>(PRODUCT, Product::getImages);
    int seqId = 0;
    @NotNull Resource imageId = null;
    @NotNull DateTime updateTime = DateTime.EPOCH;

    //~ Methods ..................................................................................................................

    /** Returns the Product Product Id. */
    @NotNull public String getProductProductId() { return this.productProductId; }

    /** Returns the Product. */
    @NotNull public Product getProduct() { return product.solveOrFail(this.productProductId); }

    /** Returns the Seq Id. */
    public int getSeqId() { return this.seqId; }

    @Override @NotNull public EntityRef<Product,String> parent() { return product; }

    @Override @NotNull public InnerEntitySeq<Image> siblings() { return getProduct().getImages(); }

    @Override public int seqId() { return getSeqId(); }

    /** Returns the Image Id. */
    @SuppressWarnings("NullableProblems") @NotNull public Resource getImageId() { return this.imageId; }

    /** Sets the value of the Image Id. */
    @NotNull public Image setImageId(@NotNull Resource imageId) {
        markAsModified();
        this.imageId = imageId;
        return (Image) this;
    }

    /** Returns the Update Time. */
    @Override @NotNull public DateTime getUpdateTime() { return this.updateTime; }

    @NotNull private static EntityTable<Image,Tuple<String,Integer>> myEntityTable() { return EntityTable.forTable(IMAGE); }

    @NotNull public EntityTable<Image,Tuple<String,Integer>> et() { return myEntityTable(); }

    @Override @NotNull public DbTable<Image,Tuple<String,Integer>> table() { return IMAGE; }

    /** 
     * Try to finds an Object of type 'Image' in the database.
     * Identified by the primary key.
     * Returns <code>null</code> if is not present
     */
    @Nullable public static Image find(@NotNull Tuple<String,Integer> key) { return myEntityTable().find(key); }

    /** 
     * Try to finds an Object of type 'Image' in the database.
     * Identified by the primary key.
     * Throws EntityNotFoundException if is not present
     */
    @NotNull public static Image findOrFail(@NotNull Tuple<String,Integer> key) { return myEntityTable().findOrFail(key); }

    /** 
     * Try to finds an Object of type 'Image' in the database.
     * Identified by the primary key.
     * Ignoring caches and accessing the database
     * Returns <code>null</code> if is not present
     */
    @Nullable public static Image findPersisted(@NotNull Tuple<String,Integer> key) { return myEntityTable().findPersisted(key); }

    /** 
     * Try to finds an Object of type 'Image' in the database.
     * Identified by the primary key.
     * Ignoring caches and accessing the database
     * Throws EntityNotFoundException if is not present
     */
    @NotNull public static Image findPersistedOrFail(@NotNull Tuple<String,Integer> key) { return myEntityTable().findPersistedOrFail(key); }

    /** 
     * Try to finds an Object of type 'Image' in the database.
     * Identified by the primary key.
     * Returns <code>null</code> if is not present
     */
    @Nullable public static Image find(@NotNull String key) { return myEntityTable().findByString(key); }

    /** 
     * Try to finds an Object of type 'Image' in the database.
     * Identified by the primary key.
     * Returns <code>null</code> if is not present
     */
    @Nullable public static Image find(@NotNull String productProductId, int seqId) { return find(Tuple.tuple2(productProductId, seqId)); }

    /** 
     * Try to finds an Object of type 'Image' in the database.
     * That verifies the specified condition.
     * Returns <code>null</code> if is not present
     */
    @Nullable public static Image findWhere(@NotNull Criteria... condition) { return selectFrom(IMAGE).where(condition).get(); }

    /** Create a selectFrom(IMAGE). */
    @NotNull public static Select<Image> list() { return selectFrom(IMAGE); }

    /** Performs the given action for each Image */
    public static void forEach(@Nullable Consumer<Image> consumer) { selectFrom(IMAGE).forEach(consumer); }

    /** List instances of 'Image' with the specified keys. */
    @NotNull public static ImmutableList<Image> list(@Nullable Set<Tuple<String,Integer>> keys) { return myEntityTable().list(keys); }

    /** List instances of 'Image' with the specified keys. */
    @NotNull public static ImmutableList<Image> list(@Nullable Iterable<String> keys) { return myEntityTable().listFromStringKeys(keys); }

    /** List the instances of 'Image' that verify the specified condition. */
    @NotNull public static Select<Image> listWhere(@NotNull Criteria condition) { return selectFrom(IMAGE).where(condition); }

    @Override @NotNull public final Image update() { return InnerInstance.super.update(); }

    @Override @NotNull public final Image insert() { return InnerInstance.super.insert(); }

    /** Register a Listener */
    public static void addListener(@NotNull EntityListenerType listenerType, @NotNull EntityListener<Image> listener) { myEntityTable().addListener(listenerType, listener); }

    /** Remove a Listener */
    public static void removeListener(@NotNull EntityListenerType listenerType, @NotNull EntityListener<Image> listener) { myEntityTable().removeListener(listenerType, listener); }

    @NotNull public String keyAsString() {
        return Strings.escapeCharOn(productProductId, ':') + ":" + seqId;
    }

    @NotNull public Tuple<String,Integer> keyObject() { return Tuple.tuple2(productProductId, seqId); }

    @Override @NotNull public final Seq<String> describe() { return formatList(getImageId()); }

    @Override @NotNull public String toString() { return "" + getImageId(); }

    /** Gets mapper for SQL statements */
    @NotNull public static RowMapper<Image> rowMapper() { return IMAGE.metadata().getRowMapper(); }

    @Override public void invalidate() { product.invalidate(); }

}
