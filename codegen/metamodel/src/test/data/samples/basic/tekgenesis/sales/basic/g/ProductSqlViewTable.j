package tekgenesis.sales.basic.g;

import tekgenesis.cache.CacheType;
import tekgenesis.common.util.Conversions;
import tekgenesis.persistence.TableField.DTime;
import tekgenesis.persistence.DbTable;
import tekgenesis.persistence.TableField.Decimal;
import tekgenesis.persistence.EntityTable;
import java.util.EnumSet;
import tekgenesis.persistence.TableField.Int;
import org.jetbrains.annotations.NotNull;
import tekgenesis.sales.basic.ProductSqlView;
import tekgenesis.persistence.TableField.Str;
import static tekgenesis.type.Modifier.REMOTABLE;
import static tekgenesis.common.collections.Colls.listOf;

/** Metadata class for table associated to entity tekgenesis.sales.basic.ProductSqlView */
@SuppressWarnings({"DuplicateStringLiteralInspection", "MagicNumber"})
public class ProductSqlViewTable
    extends DbTable<ProductSqlView,Integer>
{

    //~ Fields ...................................................................................................................

    @NotNull public final Int ID;
    @NotNull public final Str MODEL;
    @NotNull public final Decimal PRICE;
    @NotNull public final DTime UPDATE_TIME;

    //~ Constructors .............................................................................................................

    private ProductSqlViewTable() {
        super(ProductSqlView.class,"BASIC","PRODUCT_SQL_VIEW","",EnumSet.of(REMOTABLE),CacheType.NONE);
        ID = intField("id", "ID", false, 9);
        MODEL = strField("model", "MODEL", 30);
        PRICE = decimalField("price", "PRICE", false, 10, 2);
        UPDATE_TIME = dTimeField("updateTime", "UPDATE_TIME");
        primaryKey(listOf(ID));
    }

    //~ Methods ..................................................................................................................

    @NotNull protected Integer strToKey(@NotNull String key) { return Conversions.toInt(key); }

    @Override @NotNull public final ProductSqlViewTable as(@NotNull String alias) { return createAlias(new ProductSqlViewTable(), alias); }

    @Override @NotNull protected final EntityTable<ProductSqlView,Integer> createEntityTable() { return new EntityTable<>(PRODUCT_SQL_VIEW); }

    //~ Fields ...................................................................................................................

    @NotNull public static final ProductSqlViewTable PRODUCT_SQL_VIEW = new ProductSqlViewTable();

}
