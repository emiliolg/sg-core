package tekgenesis.sales.basic.g;

import tekgenesis.cache.CacheType;
import tekgenesis.persistence.TableField.DTime;
import tekgenesis.persistence.DbTable;
import tekgenesis.persistence.TableField.Decimal;
import tekgenesis.persistence.EntityTable;
import tekgenesis.persistence.TableField.Enum;
import tekgenesis.persistence.TableField.EnumerationSet;
import tekgenesis.persistence.TableField.LongFld;
import tekgenesis.type.Modifier;
import org.jetbrains.annotations.NotNull;
import tekgenesis.common.core.Option;
import tekgenesis.sales.basic.Product;
import tekgenesis.sales.basic.ProductSearcher;
import tekgenesis.sales.basic.State;
import tekgenesis.persistence.TableField.Str;
import tekgenesis.sales.basic.Tag;
import static tekgenesis.common.collections.Colls.listOf;

/** Metadata class for table associated to entity tekgenesis.sales.basic.Product */
@SuppressWarnings({"DuplicateStringLiteralInspection", "MagicNumber"})
public class ProductTable
    extends DbTable<Product,String>
{

    //~ Fields ...................................................................................................................

    @NotNull public final Str PRODUCT_ID;
    @NotNull public final Str MODEL;
    @NotNull public final Str DESCRIPTION;
    @NotNull public final Decimal PRICE;
    @NotNull public final Enum<State,String> STATE;
    @NotNull public final LongFld CATEGORY_ID_KEY;
    @NotNull public final Str CATEGORY_ATT_PERSISTED;
    @NotNull public final EnumerationSet<Tag,String> TAGS;
    @NotNull public final DTime UPDATE_TIME;

    //~ Constructors .............................................................................................................

    private ProductTable() {
        super(Product.class,"BASIC","PRODUCT","",Modifier.NONE,CacheType.NONE);
        PRODUCT_ID = strField("productId", "PRODUCT_ID", 8);
        MODEL = strField("model", "MODEL", 30);
        DESCRIPTION = strField("description", "DESCRIPTION", 100);
        PRICE = decimalField("price", "PRICE", false, 10, 2);
        STATE = enumField("state", "STATE", State.class);
        CATEGORY_ID_KEY = longField("categoryIdKey", "CATEGORY_ID_KEY", false, 18);
        CATEGORY_ATT_PERSISTED = strField("categoryAttPersisted", "CATEGORY_ATT_PERSISTED", 150);
        TAGS = enumSetField("tags", "TAGS", Tag.class);
        UPDATE_TIME = dTimeField("updateTime", "UPDATE_TIME");
        primaryKey(listOf(PRODUCT_ID));
    }

    //~ Methods ..................................................................................................................

    @NotNull protected String strToKey(@NotNull String key) { return key; }

    @Override @NotNull protected Option<ProductSearcher> searcher() { return Option.of(ProductSearcher.PRODUCT_SEARCHER); }

    @Override @NotNull public final ProductTable as(@NotNull String alias) { return createAlias(new ProductTable(), alias); }

    @Override @NotNull protected final EntityTable<Product,String> createEntityTable() { return new EntityTable<>(PRODUCT); }

    //~ Fields ...................................................................................................................

    @NotNull public static final ProductTable PRODUCT = new ProductTable();

}
