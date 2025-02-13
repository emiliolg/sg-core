package tekgenesis.sales.basic.g;

import tekgenesis.cache.CacheType;
import tekgenesis.common.util.Conversions;
import tekgenesis.persistence.TableField.DTime;
import tekgenesis.persistence.DbTable;
import tekgenesis.persistence.TableField.Decimal;
import tekgenesis.persistence.EntityTable;
import tekgenesis.persistence.TableField.Enum;
import tekgenesis.persistence.TableField.Int;
import tekgenesis.type.Modifier;
import org.jetbrains.annotations.NotNull;
import tekgenesis.common.core.Option;
import tekgenesis.sales.basic.ProductDefaultInners;
import tekgenesis.sales.basic.ProductDefaultInnersSearcher;
import tekgenesis.persistence.TableField.Res;
import tekgenesis.sales.basic.State;
import tekgenesis.persistence.TableField.Str;
import static tekgenesis.common.collections.Colls.listOf;

/** Metadata class for table associated to entity tekgenesis.sales.basic.ProductDefaultInners */
@SuppressWarnings({"DuplicateStringLiteralInspection", "MagicNumber"})
public class ProductDefaultInnersTable
    extends DbTable<ProductDefaultInners,Integer>
{

    //~ Fields ...................................................................................................................

    @NotNull public final Int ID;
    @NotNull public final Str MODEL;
    @NotNull public final Str DESCRIPTION;
    @NotNull public final Decimal PRICE;
    @NotNull public final Enum<State,String> STATE;
    @NotNull public final Int MAIN_CATEGORY_ID;
    @NotNull public final Res IMAGE;
    @NotNull public final DTime UPDATE_TIME;

    //~ Constructors .............................................................................................................

    private ProductDefaultInnersTable() {
        super(ProductDefaultInners.class,"BASIC","PRODUCT_DEFAULT_INNERS","PRODUCT_DEFAULT_INNERS_SEQ",Modifier.NONE,CacheType.NONE);
        ID = intField("id", "ID", false, 9);
        MODEL = strField("model", "MODEL", 30);
        DESCRIPTION = strField("description", "DESCRIPTION", 100);
        PRICE = decimalField("price", "PRICE", false, 10, 2);
        STATE = enumField("state", "STATE", State.class);
        MAIN_CATEGORY_ID = intField("mainCategoryId", "MAIN_CATEGORY_ID", false, 9);
        IMAGE = resourceField("image", "IMAGE");
        UPDATE_TIME = dTimeField("updateTime", "UPDATE_TIME");
        primaryKey(listOf(ID));
    }

    //~ Methods ..................................................................................................................

    @NotNull protected Integer strToKey(@NotNull String key) { return Conversions.toInt(key); }

    @Override @NotNull protected Option<ProductDefaultInnersSearcher> searcher() {
        return Option.of(ProductDefaultInnersSearcher.PRODUCT_DEFAULT_INNERS_SEARCHER);
    }

    @Override @NotNull public final ProductDefaultInnersTable as(@NotNull String alias) { return createAlias(new ProductDefaultInnersTable(), alias); }

    @Override @NotNull protected final EntityTable<ProductDefaultInners,Integer> createEntityTable() { return new EntityTable<>(PRODUCT_DEFAULT_INNERS); }

    //~ Fields ...................................................................................................................

    @NotNull public static final ProductDefaultInnersTable PRODUCT_DEFAULT_INNERS = new ProductDefaultInnersTable();

}
