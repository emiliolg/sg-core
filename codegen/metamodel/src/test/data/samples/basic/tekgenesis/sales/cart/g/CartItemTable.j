package tekgenesis.sales.cart.g;

import tekgenesis.cache.CacheType;
import tekgenesis.sales.cart.CartItem;
import tekgenesis.common.util.Conversions;
import tekgenesis.persistence.TableField.DTime;
import tekgenesis.persistence.DbTable;
import tekgenesis.persistence.EntityTable;
import tekgenesis.persistence.InnerEntityTable;
import tekgenesis.persistence.TableField.Int;
import tekgenesis.type.Modifier;
import org.jetbrains.annotations.NotNull;
import tekgenesis.persistence.TableField.Str;
import tekgenesis.common.core.Strings;
import tekgenesis.common.core.Tuple;
import static tekgenesis.common.collections.Colls.listOf;

/** Metadata class for table associated to entity tekgenesis.sales.cart.CartItem */
@SuppressWarnings({"DuplicateStringLiteralInspection", "MagicNumber"})
public class CartItemTable
    extends DbTable<CartItem,Tuple<Integer,Integer>>
{

    //~ Fields ...................................................................................................................

    @NotNull public final Int CART_ID;
    @NotNull public final Int SEQ_ID;
    @NotNull public final Str PRODUCT_PRODUCT_ID;
    @NotNull public final Int QUANTITY;
    @NotNull public final DTime UPDATE_TIME;

    //~ Constructors .............................................................................................................

    private CartItemTable() {
        super(CartItem.class,"CART","CART_ITEM","",Modifier.NONE,CacheType.NONE);
        CART_ID = intField("cartId", "CART_ID", false, 9);
        SEQ_ID = intField("seqId", "SEQ_ID", false, 9);
        PRODUCT_PRODUCT_ID = strField("productProductId", "PRODUCT_PRODUCT_ID", 8);
        QUANTITY = intField("quantity", "QUANTITY", false, 9);
        UPDATE_TIME = dTimeField("updateTime", "UPDATE_TIME");
        primaryKey(listOf(CART_ID, SEQ_ID));
    }

    //~ Methods ..................................................................................................................

    @NotNull protected Tuple<Integer,Integer> strToKey(@NotNull String key) {
        final String[] parts = Strings.splitToArray(key, 2);
        return Tuple.tuple2(Conversions.toInt(parts[0]), Conversions.toInt(parts[1]));
    }

    @Override @NotNull public final CartItemTable as(@NotNull String alias) { return createAlias(new CartItemTable(), alias); }

    @Override @NotNull protected final EntityTable<CartItem,Tuple<Integer,Integer>> createEntityTable() { return new InnerEntityTable<>(CART_ITEM); }

    //~ Fields ...................................................................................................................

    @NotNull public static final CartItemTable CART_ITEM = new CartItemTable();

}
