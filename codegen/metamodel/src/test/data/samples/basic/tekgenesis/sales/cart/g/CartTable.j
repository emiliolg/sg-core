package tekgenesis.sales.cart.g;

import tekgenesis.cache.CacheType;
import tekgenesis.sales.cart.Cart;
import tekgenesis.common.util.Conversions;
import tekgenesis.persistence.TableField.DTime;
import tekgenesis.persistence.DbTable;
import tekgenesis.persistence.EntityTable;
import tekgenesis.persistence.TableField.Int;
import tekgenesis.type.Modifier;
import org.jetbrains.annotations.NotNull;
import tekgenesis.persistence.TableField.Str;
import static tekgenesis.common.collections.Colls.listOf;

/** Metadata class for table associated to entity tekgenesis.sales.cart.Cart */
@SuppressWarnings({"DuplicateStringLiteralInspection", "MagicNumber"})
public class CartTable
    extends DbTable<Cart,Integer>
{

    //~ Fields ...................................................................................................................

    @NotNull public final Int ID;
    @NotNull public final Str USER;
    @NotNull public final DTime UPDATE_TIME;

    //~ Constructors .............................................................................................................

    private CartTable() {
        super(Cart.class,"CART","CART","CART_SEQ",Modifier.NONE,CacheType.NONE);
        ID = intField("id", "ID", false, 9);
        USER = strField("user", "USER", 20);
        UPDATE_TIME = dTimeField("updateTime", "UPDATE_TIME");
        primaryKey(listOf(ID));
        secondaryKeys(listOf(listOf(USER)));
    }

    //~ Methods ..................................................................................................................

    @NotNull protected Integer strToKey(@NotNull String key) { return Conversions.toInt(key); }

    @Override @NotNull public final CartTable as(@NotNull String alias) { return createAlias(new CartTable(), alias); }

    @Override @NotNull protected final EntityTable<Cart,Integer> createEntityTable() { return new EntityTable<>(CART); }

    //~ Fields ...................................................................................................................

    @NotNull public static final CartTable CART = new CartTable();

}
