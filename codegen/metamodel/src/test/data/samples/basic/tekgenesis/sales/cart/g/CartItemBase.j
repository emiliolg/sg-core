package tekgenesis.sales.cart.g;

import tekgenesis.sales.cart.Cart;
import tekgenesis.sales.cart.CartItem;
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
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import tekgenesis.sales.basic.Product;
import tekgenesis.database.RowMapper;
import tekgenesis.persistence.Select;
import tekgenesis.common.collections.Seq;
import java.util.Set;
import tekgenesis.common.core.Tuple;
import static tekgenesis.sales.cart.g.CartTable.CART;
import static tekgenesis.sales.cart.g.CartItemTable.CART_ITEM;
import static tekgenesis.sales.basic.g.ProductTable.PRODUCT;
import static tekgenesis.common.util.Conversions.formatList;
import static tekgenesis.persistence.Sql.selectFrom;

/** 
 * Generated base class for entity: CartItem.
 * Don't modify this as this is an auto generated class that's gets generated
 * every time the meta model file is modified. Use subclass instead.
 */
@SuppressWarnings({"DuplicateStringLiteralInspection", "WeakerAccess", "FieldMayBeFinal", "FieldCanBeLocal", "ConstantConditions", "ClassWithTooManyMethods", "ClassWithTooManyFields", "MagicNumber", "MethodOverridesStaticMethodOfSuperclass", "ParameterHidesMemberVariable", "FieldNameHidesFieldInSuperclass", "OverlyComplexClass", "RedundantCast", "UnusedReturnValue"})
public abstract class CartItemBase
    extends EntityInstanceImpl<CartItem,Tuple<Integer,Integer>>
    implements InnerInstance<CartItem,Tuple<Integer,Integer>,Cart,Integer>
{

    //~ Fields ...................................................................................................................

    int cartId = 0;
    @NotNull EntityRef<Cart,Integer> cart = new EntityRef<>(CART, Cart::getItems);
    int seqId = 0;
    @NotNull String productProductId = "";
    @NotNull EntityRef<Product,String> product = new EntityRef<>(PRODUCT);
    int quantity = 0;
    @NotNull DateTime updateTime = DateTime.EPOCH;

    //~ Methods ..................................................................................................................

    /** Returns the Cart Id. */
    public int getCartId() { return this.cartId; }

    /** Returns the Cart. */
    @NotNull public Cart getCart() { return cart.solveOrFail(this.cartId); }

    /** Returns the Seq Id. */
    public int getSeqId() { return this.seqId; }

    @Override @NotNull public EntityRef<Cart,Integer> parent() { return cart; }

    @Override @NotNull public InnerEntitySeq<CartItem> siblings() { return getCart().getItems(); }

    @Override public int seqId() { return getSeqId(); }

    /** Returns the Product Product Id. */
    @NotNull public String getProductProductId() { return this.productProductId; }

    /** Returns the Product. */
    @NotNull public Product getProduct() { return product.solveOrFail(this.productProductId); }

    /** Sets the value of the Product Product Id. */
    @NotNull public CartItem setProductProductId(@NotNull String productProductId) {
        product.invalidate();
        this.productProductId = productProductId;
        return (CartItem) this;
    }

    /** Sets the value of the Product. */
    @NotNull public CartItem setProduct(@NotNull Product product) {
        this.product.set(product);
        this.productProductId = product.getProductId();
        return (CartItem) this;
    }

    /** Returns the Quantity. */
    public int getQuantity() { return this.quantity; }

    /** Sets the value of the Quantity. */
    @NotNull public CartItem setQuantity(int quantity) {
        markAsModified();
        this.quantity = Integers.checkSignedLength("quantity", quantity, false, 9);
        return (CartItem) this;
    }

    /** Returns the Update Time. */
    @Override @NotNull public DateTime getUpdateTime() { return this.updateTime; }

    @NotNull private static EntityTable<CartItem,Tuple<Integer,Integer>> myEntityTable() { return EntityTable.forTable(CART_ITEM); }

    @NotNull public EntityTable<CartItem,Tuple<Integer,Integer>> et() { return myEntityTable(); }

    @Override @NotNull public DbTable<CartItem,Tuple<Integer,Integer>> table() { return CART_ITEM; }

    /** 
     * Try to finds an Object of type 'CartItem' in the database.
     * Identified by the primary key.
     * Returns <code>null</code> if is not present
     */
    @Nullable public static CartItem find(@NotNull Tuple<Integer,Integer> key) { return myEntityTable().find(key); }

    /** 
     * Try to finds an Object of type 'CartItem' in the database.
     * Identified by the primary key.
     * Throws EntityNotFoundException if is not present
     */
    @NotNull public static CartItem findOrFail(@NotNull Tuple<Integer,Integer> key) { return myEntityTable().findOrFail(key); }

    /** 
     * Try to finds an Object of type 'CartItem' in the database.
     * Identified by the primary key.
     * Ignoring caches and accessing the database
     * Returns <code>null</code> if is not present
     */
    @Nullable public static CartItem findPersisted(@NotNull Tuple<Integer,Integer> key) { return myEntityTable().findPersisted(key); }

    /** 
     * Try to finds an Object of type 'CartItem' in the database.
     * Identified by the primary key.
     * Ignoring caches and accessing the database
     * Throws EntityNotFoundException if is not present
     */
    @NotNull public static CartItem findPersistedOrFail(@NotNull Tuple<Integer,Integer> key) { return myEntityTable().findPersistedOrFail(key); }

    /** 
     * Try to finds an Object of type 'CartItem' in the database.
     * Identified by the primary key.
     * Returns <code>null</code> if is not present
     */
    @Nullable public static CartItem find(@NotNull String key) { return myEntityTable().findByString(key); }

    /** 
     * Try to finds an Object of type 'CartItem' in the database.
     * Identified by the primary key.
     * Returns <code>null</code> if is not present
     */
    @Nullable public static CartItem find(int cartId, int seqId) { return find(Tuple.tuple2(cartId, seqId)); }

    /** 
     * Try to finds an Object of type 'CartItem' in the database.
     * Identified by the primary key.
     * Returns <code>null</code> if is not present
     * Based on String key for Entities
     */
    @Nullable public static CartItem find(@NotNull String cart, int seqId) { return find(Conversions.toInt(cart), seqId); }

    /** 
     * Try to finds an Object of type 'CartItem' in the database.
     * That verifies the specified condition.
     * Returns <code>null</code> if is not present
     */
    @Nullable public static CartItem findWhere(@NotNull Criteria... condition) { return selectFrom(CART_ITEM).where(condition).get(); }

    /** Create a selectFrom(CART_ITEM). */
    @NotNull public static Select<CartItem> list() { return selectFrom(CART_ITEM); }

    /** Performs the given action for each CartItem */
    public static void forEach(@Nullable Consumer<CartItem> consumer) { selectFrom(CART_ITEM).forEach(consumer); }

    /** List instances of 'CartItem' with the specified keys. */
    @NotNull public static ImmutableList<CartItem> list(@Nullable Set<Tuple<Integer,Integer>> keys) { return myEntityTable().list(keys); }

    /** List instances of 'CartItem' with the specified keys. */
    @NotNull public static ImmutableList<CartItem> list(@Nullable Iterable<String> keys) { return myEntityTable().listFromStringKeys(keys); }

    /** List the instances of 'CartItem' that verify the specified condition. */
    @NotNull public static Select<CartItem> listWhere(@NotNull Criteria condition) { return selectFrom(CART_ITEM).where(condition); }

    @Override @NotNull public final CartItem update() { return InnerInstance.super.update(); }

    @Override @NotNull public final CartItem insert() { return InnerInstance.super.insert(); }

    /** Register a Listener */
    public static void addListener(@NotNull EntityListenerType listenerType, @NotNull EntityListener<CartItem> listener) { myEntityTable().addListener(listenerType, listener); }

    /** Remove a Listener */
    public static void removeListener(@NotNull EntityListenerType listenerType, @NotNull EntityListener<CartItem> listener) { myEntityTable().removeListener(listenerType, listener); }

    @NotNull public String keyAsString() { return cartId + ":" + seqId; }

    @NotNull public Tuple<Integer,Integer> keyObject() { return Tuple.tuple2(cartId, seqId); }

    @Override @NotNull public final Seq<String> describe() { return formatList(getCart(), getSeqId()); }

    @Override @NotNull public String toString() { return "" + getCart() + " " + getSeqId(); }

    /** Gets mapper for SQL statements */
    @NotNull public static RowMapper<CartItem> rowMapper() { return CART_ITEM.metadata().getRowMapper(); }

    @Override public void invalidate() {
        product.invalidate();
        cart.invalidate();
    }

}
