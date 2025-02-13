package tekgenesis.sales.basic.g;

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
import tekgenesis.common.core.Integers;
import tekgenesis.sales.basic.Invoice;
import tekgenesis.sales.basic.Item;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import tekgenesis.sales.basic.Product;
import tekgenesis.database.RowMapper;
import tekgenesis.persistence.Select;
import tekgenesis.common.collections.Seq;
import java.util.Set;
import tekgenesis.common.core.Tuple;
import static tekgenesis.sales.basic.g.InvoiceTable.INVOICE;
import static tekgenesis.sales.basic.g.ItemTable.ITEM;
import static tekgenesis.sales.basic.g.ProductTable.PRODUCT;
import static tekgenesis.common.util.Conversions.formatList;
import static tekgenesis.persistence.Sql.selectFrom;

/** 
 * Generated base class for entity: Item.
 * Don't modify this as this is an auto generated class that's gets generated
 * every time the meta model file is modified. Use subclass instead.
 */
@SuppressWarnings({"DuplicateStringLiteralInspection", "WeakerAccess", "FieldMayBeFinal", "FieldCanBeLocal", "ConstantConditions", "ClassWithTooManyMethods", "ClassWithTooManyFields", "MagicNumber", "MethodOverridesStaticMethodOfSuperclass", "ParameterHidesMemberVariable", "FieldNameHidesFieldInSuperclass", "OverlyComplexClass", "RedundantCast", "UnusedReturnValue"})
public abstract class ItemBase
    extends EntityInstanceImpl<Item,Tuple<Integer,Integer>>
    implements InnerInstance<Item,Tuple<Integer,Integer>,Invoice,Integer>
{

    //~ Fields ...................................................................................................................

    int invoiceIdKey = 0;
    @NotNull EntityRef<Invoice,Integer> invoice = new EntityRef<>(INVOICE, Invoice::getItems);
    int seqId = 0;
    @NotNull String productProductId = "";
    @NotNull EntityRef<Product,String> product = new EntityRef<>(PRODUCT);
    int quantity = 0;
    int discount = 0;
    @NotNull DateTime updateTime = DateTime.EPOCH;

    //~ Methods ..................................................................................................................

    /** Returns the Invoice Id Key. */
    public int getInvoiceIdKey() { return this.invoiceIdKey; }

    /** Returns the Invoice. */
    @NotNull public Invoice getInvoice() { return invoice.solveOrFail(this.invoiceIdKey); }

    /** Returns the Seq Id. */
    public int getSeqId() { return this.seqId; }

    @Override @NotNull public EntityRef<Invoice,Integer> parent() { return invoice; }

    @Override @NotNull public InnerEntitySeq<Item> siblings() { return getInvoice().getItems(); }

    @Override public int seqId() { return getSeqId(); }

    /** Returns the Product Product Id. */
    @NotNull public String getProductProductId() { return this.productProductId; }

    /** Returns the Product. */
    @NotNull public Product getProduct() { return product.solveOrFail(this.productProductId); }

    /** Sets the value of the Product Product Id. */
    @NotNull public Item setProductProductId(@NotNull String productProductId) {
        product.invalidate();
        this.productProductId = productProductId;
        return (Item) this;
    }

    /** Sets the value of the Product. */
    @NotNull public Item setProduct(@NotNull Product product) {
        this.product.set(product);
        this.productProductId = product.getProductId();
        return (Item) this;
    }

    /** Returns the Quantity. */
    public int getQuantity() { return this.quantity; }

    /** Sets the value of the Quantity. */
    @NotNull public Item setQuantity(int quantity) {
        markAsModified();
        this.quantity = Integers.checkSignedLength("quantity", quantity, false, 9);
        return (Item) this;
    }

    /** Returns the Discount. */
    public int getDiscount() { return this.discount; }

    /** Sets the value of the Discount. */
    @NotNull public Item setDiscount(int discount) {
        markAsModified();
        this.discount = Integers.checkSignedLength("discount", discount, false, 9);
        return (Item) this;
    }

    /** Returns the Update Time. */
    @Override @NotNull public DateTime getUpdateTime() { return this.updateTime; }

    @NotNull private static EntityTable<Item,Tuple<Integer,Integer>> myEntityTable() { return EntityTable.forTable(ITEM); }

    @NotNull public EntityTable<Item,Tuple<Integer,Integer>> et() { return myEntityTable(); }

    @Override @NotNull public DbTable<Item,Tuple<Integer,Integer>> table() { return ITEM; }

    /** 
     * Try to finds an Object of type 'Item' in the database.
     * Identified by the primary key.
     * Returns <code>null</code> if is not present
     */
    @Nullable public static Item find(@NotNull Tuple<Integer,Integer> key) { return myEntityTable().find(key); }

    /** 
     * Try to finds an Object of type 'Item' in the database.
     * Identified by the primary key.
     * Throws EntityNotFoundException if is not present
     */
    @NotNull public static Item findOrFail(@NotNull Tuple<Integer,Integer> key) { return myEntityTable().findOrFail(key); }

    /** 
     * Try to finds an Object of type 'Item' in the database.
     * Identified by the primary key.
     * Ignoring caches and accessing the database
     * Returns <code>null</code> if is not present
     */
    @Nullable public static Item findPersisted(@NotNull Tuple<Integer,Integer> key) { return myEntityTable().findPersisted(key); }

    /** 
     * Try to finds an Object of type 'Item' in the database.
     * Identified by the primary key.
     * Ignoring caches and accessing the database
     * Throws EntityNotFoundException if is not present
     */
    @NotNull public static Item findPersistedOrFail(@NotNull Tuple<Integer,Integer> key) { return myEntityTable().findPersistedOrFail(key); }

    /** 
     * Try to finds an Object of type 'Item' in the database.
     * Identified by the primary key.
     * Returns <code>null</code> if is not present
     */
    @Nullable public static Item find(@NotNull String key) { return myEntityTable().findByString(key); }

    /** 
     * Try to finds an Object of type 'Item' in the database.
     * Identified by the primary key.
     * Returns <code>null</code> if is not present
     */
    @Nullable public static Item find(int invoiceIdKey, int seqId) { return find(Tuple.tuple2(invoiceIdKey, seqId)); }

    /** 
     * Try to finds an Object of type 'Item' in the database.
     * Identified by the primary key.
     * Returns <code>null</code> if is not present
     * Based on String key for Entities
     */
    @Nullable public static Item find(@NotNull String invoice, int seqId) { return find(Conversions.toInt(invoice), seqId); }

    /** 
     * Try to finds an Object of type 'Item' in the database.
     * That verifies the specified condition.
     * Returns <code>null</code> if is not present
     */
    @Nullable public static Item findWhere(@NotNull Criteria... condition) { return selectFrom(ITEM).where(condition).get(); }

    /** Create a selectFrom(ITEM). */
    @NotNull public static Select<Item> list() { return selectFrom(ITEM); }

    /** Performs the given action for each Item */
    public static void forEach(@Nullable Consumer<Item> consumer) { selectFrom(ITEM).forEach(consumer); }

    /** List instances of 'Item' with the specified keys. */
    @NotNull public static ImmutableList<Item> list(@Nullable Set<Tuple<Integer,Integer>> keys) { return myEntityTable().list(keys); }

    /** List instances of 'Item' with the specified keys. */
    @NotNull public static ImmutableList<Item> list(@Nullable Iterable<String> keys) { return myEntityTable().listFromStringKeys(keys); }

    /** List the instances of 'Item' that verify the specified condition. */
    @NotNull public static Select<Item> listWhere(@NotNull Criteria condition) { return selectFrom(ITEM).where(condition); }

    @Override @NotNull public final Item update() { return InnerInstance.super.update(); }

    @Override @NotNull public final Item insert() { return InnerInstance.super.insert(); }

    /** Register a Listener */
    public static void addListener(@NotNull EntityListenerType listenerType, @NotNull EntityListener<Item> listener) { myEntityTable().addListener(listenerType, listener); }

    /** Remove a Listener */
    public static void removeListener(@NotNull EntityListenerType listenerType, @NotNull EntityListener<Item> listener) { myEntityTable().removeListener(listenerType, listener); }

    @NotNull public String keyAsString() { return invoiceIdKey + ":" + seqId; }

    @NotNull public Tuple<Integer,Integer> keyObject() { return Tuple.tuple2(invoiceIdKey, seqId); }

    @Override @NotNull public final Seq<String> describe() { return formatList(getInvoice(), getSeqId()); }

    @Override @NotNull public String toString() { return "" + getInvoice() + " " + getSeqId(); }

    /** Gets mapper for SQL statements */
    @NotNull public static RowMapper<Item> rowMapper() { return ITEM.metadata().getRowMapper(); }

    @Override public void invalidate() {
        product.invalidate();
        invoice.invalidate();
    }

}
