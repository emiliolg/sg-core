package tekgenesis.sales.cart.g;

import tekgenesis.sales.cart.Cart;
import tekgenesis.sales.cart.CartItem;
import java.util.function.Consumer;
import tekgenesis.persistence.Criteria;
import tekgenesis.common.core.DateTime;
import tekgenesis.persistence.DbTable;
import tekgenesis.persistence.EntityInstanceImpl;
import tekgenesis.persistence.EntityListener;
import tekgenesis.persistence.EntityListenerType;
import tekgenesis.persistence.EntityTable;
import tekgenesis.common.collections.ImmutableList;
import tekgenesis.persistence.InnerEntitySeq;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import tekgenesis.persistence.PersistableInstance;
import tekgenesis.database.RowMapper;
import tekgenesis.persistence.Select;
import tekgenesis.common.collections.Seq;
import java.util.Set;
import tekgenesis.common.core.Strings;
import static tekgenesis.sales.cart.g.CartTable.CART;
import static tekgenesis.sales.cart.g.CartItemTable.CART_ITEM;
import static tekgenesis.persistence.EntitySeq.createInnerEntitySeq;
import static tekgenesis.common.util.Conversions.formatList;
import static tekgenesis.persistence.Sql.selectFrom;

/** 
 * Generated base class for entity: Cart.
 * Don't modify this as this is an auto generated class that's gets generated
 * every time the meta model file is modified. Use subclass instead.
 */
@SuppressWarnings({"DuplicateStringLiteralInspection", "WeakerAccess", "FieldMayBeFinal", "FieldCanBeLocal", "ConstantConditions", "ClassWithTooManyMethods", "ClassWithTooManyFields", "MagicNumber", "MethodOverridesStaticMethodOfSuperclass", "ParameterHidesMemberVariable", "FieldNameHidesFieldInSuperclass", "OverlyComplexClass", "RedundantCast", "UnusedReturnValue"})
public abstract class CartBase
    extends EntityInstanceImpl<Cart,Integer>
    implements PersistableInstance<Cart,Integer>
{

    //~ Fields ...................................................................................................................

    private int id = EntityTable.DEFAULT_EMPTY_KEY;
    @NotNull String user = "";
    @NotNull private InnerEntitySeq<CartItem> items = createInnerEntitySeq(CART_ITEM, (Cart) this, c -> ((CartItemBase)c).cart);
    @NotNull DateTime updateTime = DateTime.EPOCH;

    //~ Methods ..................................................................................................................

    /** Returns the Id. */
    public int getId() { return this.id; }

    @Override public boolean hasEmptyKey() { return id == EntityTable.DEFAULT_EMPTY_KEY; }

    /** Returns the User. */
    @NotNull public String getUser() { return this.user; }

    /** Sets the value of the User. */
    @NotNull public Cart setUser(@NotNull String user) {
        markAsModified();
        this.user = Strings.truncate(user, 20);
        return (Cart) this;
    }

    /** Returns the Items. */
    @NotNull public InnerEntitySeq<CartItem> getItems() { return items; }

    /** Returns the Update Time. */
    @Override @NotNull public DateTime getUpdateTime() { return this.updateTime; }

    /** Creates a new {@link Cart} instance. */
    @NotNull public static Cart create() { return new Cart(); }

    @NotNull private static EntityTable<Cart,Integer> myEntityTable() { return EntityTable.forTable(CART); }

    @NotNull public EntityTable<Cart,Integer> et() { return myEntityTable(); }

    @Override @NotNull public DbTable<Cart,Integer> table() { return CART; }

    /** 
     * Try to finds an Object of type 'Cart' in the database.
     * Identified by the primary key.
     * Returns <code>null</code> if is not present
     */
    @Nullable public static Cart find(int id) { return myEntityTable().find(id); }

    /** 
     * Try to finds an Object of type 'Cart' in the database.
     * Identified by the primary key.
     * Throws EntityNotFoundException if is not present
     */
    @NotNull public static Cart findOrFail(int id) { return myEntityTable().findOrFail(id); }

    /** 
     * Try to finds an Object of type 'Cart' in the database.
     * Identified by the primary key.
     * Ignoring caches and accessing the database
     * Returns <code>null</code> if is not present
     */
    @Nullable public static Cart findPersisted(int id) { return myEntityTable().findPersisted(id); }

    /** 
     * Try to finds an Object of type 'Cart' in the database.
     * Identified by the primary key.
     * Ignoring caches and accessing the database
     * Throws EntityNotFoundException if is not present
     */
    @NotNull public static Cart findPersistedOrFail(int id) { return myEntityTable().findPersistedOrFail(id); }

    /** 
     * Try to finds an Object of type 'Cart' in the database.
     * Identified by the primary key.
     * Returns <code>null</code> if is not present
     */
    @Nullable public static Cart find(@NotNull String key) { return myEntityTable().findByString(key); }

    /** 
     * Try to finds an Object of type 'Cart' in the database.
     * That verifies the specified condition.
     * Returns <code>null</code> if is not present
     */
    @Nullable public static Cart findWhere(@NotNull Criteria... condition) { return selectFrom(CART).where(condition).get(); }

    /** Create a selectFrom(CART). */
    @NotNull public static Select<Cart> list() { return selectFrom(CART); }

    /** Performs the given action for each Cart */
    public static void forEach(@Nullable Consumer<Cart> consumer) { selectFrom(CART).forEach(consumer); }

    /** List instances of 'Cart' with the specified keys. */
    @NotNull public static ImmutableList<Cart> list(@Nullable Set<Integer> keys) { return myEntityTable().list(keys); }

    /** List instances of 'Cart' with the specified keys. */
    @NotNull public static ImmutableList<Cart> list(@Nullable Iterable<String> keys) { return myEntityTable().listFromStringKeys(keys); }

    /** List the instances of 'Cart' that verify the specified condition. */
    @NotNull public static Select<Cart> listWhere(@NotNull Criteria condition) { return selectFrom(CART).where(condition); }

    /** Insert specifying the primary key */
    public void insert(int key) {
        this.id = key;
        myEntityTable().insertDoNotGenerate((Cart) this);
    }

    /** Register a Listener */
    public static void addListener(@NotNull EntityListenerType listenerType, @NotNull EntityListener<Cart> listener) { myEntityTable().addListener(listenerType, listener); }

    /** Remove a Listener */
    public static void removeListener(@NotNull EntityListenerType listenerType, @NotNull EntityListener<Cart> listener) { myEntityTable().removeListener(listenerType, listener); }

    /** Finds the instance */
    @Nullable public static Cart findByUser(@NotNull String user) { return myEntityTable().findByKey(0, user); }

    @NotNull public String keyAsString() { return String.valueOf(id); }

    @NotNull public Integer keyObject() { return id; }

    @Override @NotNull public final Seq<String> describe() { return formatList(getId()); }

    @Override @NotNull public String toString() { return "" + getId(); }

    /** Gets mapper for SQL statements */
    @NotNull public static RowMapper<Cart> rowMapper() { return CART.metadata().getRowMapper(); }

    @Override public void invalidate() { items.invalidate(); }

}
