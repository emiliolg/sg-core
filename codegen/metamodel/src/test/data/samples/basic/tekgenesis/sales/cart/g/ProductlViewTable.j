package tekgenesis.sales.cart.g;

import tekgenesis.cache.CacheType;
import tekgenesis.common.util.Conversions;
import tekgenesis.persistence.TableField.DTime;
import tekgenesis.persistence.DbTable;
import tekgenesis.persistence.TableField.Decimal;
import tekgenesis.persistence.EntityTable;
import tekgenesis.persistence.TableField.Int;
import tekgenesis.type.Modifier;
import org.jetbrains.annotations.NotNull;
import tekgenesis.sales.cart.ProductlView;
import tekgenesis.persistence.TableField.Str;
import static tekgenesis.common.collections.Colls.listOf;

/** Metadata class for table associated to entity tekgenesis.sales.cart.ProductlView */
@SuppressWarnings({"DuplicateStringLiteralInspection", "MagicNumber"})
public class ProductlViewTable
    extends DbTable<ProductlView,Integer>
{

    //~ Fields ...................................................................................................................

    @NotNull public final Int ID;
    @NotNull public final Str MODEL;
    @NotNull public final Decimal PRICE;
    @NotNull public final DTime UPDATE_TIME;

    //~ Constructors .............................................................................................................

    private ProductlViewTable() {
        super(ProductlView.class,"CART","PRODUCTL_VIEW","",Modifier.NONE,CacheType.NONE);
        ID = intField("id", "ID", false, 9);
        MODEL = strField("model", "MODEL", 30);
        PRICE = decimalField("price", "PRICE", false, 10, 2);
        UPDATE_TIME = dTimeField("updateTime", "UPDATE_TIME");
        primaryKey(listOf(ID));
    }

    //~ Methods ..................................................................................................................

    @NotNull protected Integer strToKey(@NotNull String key) { return Conversions.toInt(key); }

    @Override @NotNull public final ProductlViewTable as(@NotNull String alias) { return createAlias(new ProductlViewTable(), alias); }

    @Override @NotNull protected final EntityTable<ProductlView,Integer> createEntityTable() { return new EntityTable<>(PRODUCTL_VIEW); }

    //~ Fields ...................................................................................................................

    @NotNull public static final ProductlViewTable PRODUCTL_VIEW = new ProductlViewTable();

}
