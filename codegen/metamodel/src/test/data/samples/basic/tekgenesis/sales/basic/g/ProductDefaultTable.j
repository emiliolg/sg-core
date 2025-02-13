package tekgenesis.sales.basic.g;

import tekgenesis.cache.CacheType;
import tekgenesis.persistence.TableField.Clob;
import tekgenesis.common.util.Conversions;
import tekgenesis.persistence.TableField.DTime;
import tekgenesis.persistence.DbTable;
import tekgenesis.persistence.TableField.Decimal;
import tekgenesis.persistence.EntityTable;
import tekgenesis.persistence.TableField.Enum;
import java.util.EnumSet;
import tekgenesis.persistence.TableField.Int;
import org.jetbrains.annotations.NotNull;
import tekgenesis.common.core.Option;
import tekgenesis.sales.basic.ProductDefault;
import tekgenesis.sales.basic.ProductDefaultSearcher;
import tekgenesis.persistence.TableField.Res;
import tekgenesis.sales.basic.State;
import tekgenesis.persistence.TableField.Str;
import static tekgenesis.type.Modifier.DEFAULT_SEARCHABLE;
import static tekgenesis.common.collections.Colls.listOf;

/** Metadata class for table associated to entity tekgenesis.sales.basic.ProductDefault */
@SuppressWarnings({"DuplicateStringLiteralInspection", "MagicNumber"})
public class ProductDefaultTable
    extends DbTable<ProductDefault,Integer>
{

    //~ Fields ...................................................................................................................

    @NotNull public final Int ID;
    @NotNull public final Str MODEL;
    @NotNull public final Str DESCRIPTION;
    @NotNull public final Decimal PRICE;
    @NotNull public final Enum<State,String> STATE;
    @NotNull public final Int MAIN_CATEGORY_ID;
    @NotNull public final Res IMAGE;
    @NotNull public final Clob COMMENTS;
    @NotNull public final DTime UPDATE_TIME;

    //~ Constructors .............................................................................................................

    private ProductDefaultTable() {
        super(ProductDefault.class,"BASIC","PRODUCT_DEFAULT","PRODUCT_DEFAULT_SEQ",EnumSet.of(DEFAULT_SEARCHABLE),CacheType.NONE);
        ID = intField("id", "ID", false, 9);
        MODEL = strField("model", "MODEL", 30);
        DESCRIPTION = strField("description", "DESCRIPTION", 100);
        PRICE = decimalField("price", "PRICE", false, 10, 2);
        STATE = enumField("state", "STATE", State.class);
        MAIN_CATEGORY_ID = intField("mainCategoryId", "MAIN_CATEGORY_ID", false, 9);
        IMAGE = resourceField("image", "IMAGE");
        COMMENTS = clobField("comments", "COMMENTS", 5000);
        UPDATE_TIME = dTimeField("updateTime", "UPDATE_TIME");
        primaryKey(listOf(ID));
    }

    //~ Methods ..................................................................................................................

    @NotNull protected Integer strToKey(@NotNull String key) { return Conversions.toInt(key); }

    @Override @NotNull protected Option<ProductDefaultSearcher> searcher() {
        return Option.of(ProductDefaultSearcher.PRODUCT_DEFAULT_SEARCHER);
    }

    @Override @NotNull public final ProductDefaultTable as(@NotNull String alias) { return createAlias(new ProductDefaultTable(), alias); }

    @Override @NotNull protected final EntityTable<ProductDefault,Integer> createEntityTable() { return new EntityTable<>(PRODUCT_DEFAULT); }

    //~ Fields ...................................................................................................................

    @NotNull public static final ProductDefaultTable PRODUCT_DEFAULT = new ProductDefaultTable();

}
