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
import tekgenesis.sales.basic.ProductDefaultView;
import tekgenesis.sales.basic.ProductDefaultViewSearcher;
import tekgenesis.persistence.TableField.Res;
import tekgenesis.sales.basic.State;
import tekgenesis.persistence.TableField.Str;
import static tekgenesis.type.Modifier.DEFAULT_SEARCHABLE;
import static tekgenesis.type.Modifier.REMOTE;
import static tekgenesis.common.collections.Colls.listOf;

/** Metadata class for table associated to entity tekgenesis.sales.basic.ProductDefaultView */
@SuppressWarnings({"DuplicateStringLiteralInspection", "MagicNumber"})
public class ProductDefaultViewTable
    extends DbTable<ProductDefaultView,Integer>
{

    //~ Fields ...................................................................................................................

    @NotNull public final Int ID;
    @NotNull public final Str VDESCR;
    @NotNull public final Int V_CATEGORY_ID;
    @NotNull public final Res IMAGE;
    @NotNull public final Enum<State,String> STATE;
    @NotNull public final Decimal PRICE;
    @NotNull public final Clob COMMENTS;
    @NotNull public final DTime UPDATE_TIME;

    //~ Constructors .............................................................................................................

    private ProductDefaultViewTable() {
        super(ProductDefaultView.class,"BASIC","PRODUCT_DEFAULT_VIEW","PRODUCT_DEFAULT_SEQ",EnumSet.of(REMOTE, DEFAULT_SEARCHABLE),CacheType.NONE);
        ID = intField("id", "ID", false, 9);
        VDESCR = strField("vdescr", "VDESCR", 100);
        V_CATEGORY_ID = intField("vCategoryId", "V_CATEGORY_ID", false, 9);
        IMAGE = resourceField("image", "IMAGE");
        STATE = enumField("state", "STATE", State.class);
        PRICE = decimalField("price", "PRICE", false, 10, 2);
        COMMENTS = clobField("comments", "COMMENTS", 5000);
        UPDATE_TIME = dTimeField("updateTime", "UPDATE_TIME");
        primaryKey(listOf(ID));
    }

    //~ Methods ..................................................................................................................

    @NotNull protected Integer strToKey(@NotNull String key) { return Conversions.toInt(key); }

    @Override @NotNull protected Option<ProductDefaultViewSearcher> searcher() {
        return Option.of(ProductDefaultViewSearcher.PRODUCT_DEFAULT_VIEW_SEARCHER);
    }

    @Override @NotNull public final ProductDefaultViewTable as(@NotNull String alias) { return createAlias(new ProductDefaultViewTable(), alias); }

    @Override @NotNull protected final EntityTable<ProductDefaultView,Integer> createEntityTable() { return new EntityTable<>(PRODUCT_DEFAULT_VIEW); }

    //~ Fields ...................................................................................................................

    @NotNull public static final ProductDefaultViewTable PRODUCT_DEFAULT_VIEW = new ProductDefaultViewTable();

}
