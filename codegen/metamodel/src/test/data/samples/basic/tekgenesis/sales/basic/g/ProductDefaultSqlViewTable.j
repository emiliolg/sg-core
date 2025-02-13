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
import tekgenesis.common.core.Option;
import tekgenesis.sales.basic.ProductDefaultSqlView;
import tekgenesis.sales.basic.ProductDefaultSqlViewSearcher;
import tekgenesis.persistence.TableField.Str;
import static tekgenesis.type.Modifier.DEFAULT_SEARCHABLE;
import static tekgenesis.type.Modifier.REMOTE;
import static tekgenesis.common.collections.Colls.listOf;

/** Metadata class for table associated to entity tekgenesis.sales.basic.ProductDefaultSqlView */
@SuppressWarnings({"DuplicateStringLiteralInspection", "MagicNumber"})
public class ProductDefaultSqlViewTable
    extends DbTable<ProductDefaultSqlView,Integer>
{

    //~ Fields ...................................................................................................................

    @NotNull public final Int ID;
    @NotNull public final Str MODEL;
    @NotNull public final Decimal PRICE;
    @NotNull public final DTime UPDATE_TIME;

    //~ Constructors .............................................................................................................

    private ProductDefaultSqlViewTable() {
        super(ProductDefaultSqlView.class,"BASIC","PRODUCT_DEFAULT_SQL_VIEW","",EnumSet.of(REMOTE, DEFAULT_SEARCHABLE),CacheType.NONE);
        ID = intField("id", "ID", false, 9);
        MODEL = strField("model", "MODEL", 30);
        PRICE = decimalField("price", "PRICE", false, 10, 2);
        UPDATE_TIME = dTimeField("updateTime", "UPDATE_TIME");
        primaryKey(listOf(ID));
    }

    //~ Methods ..................................................................................................................

    @NotNull protected Integer strToKey(@NotNull String key) { return Conversions.toInt(key); }

    @Override @NotNull protected Option<ProductDefaultSqlViewSearcher> searcher() {
        return Option.of(ProductDefaultSqlViewSearcher.PRODUCT_DEFAULT_SQL_VIEW_SEARCHER);
    }

    @Override @NotNull public final ProductDefaultSqlViewTable as(@NotNull String alias) { return createAlias(new ProductDefaultSqlViewTable(), alias); }

    @Override @NotNull protected final EntityTable<ProductDefaultSqlView,Integer> createEntityTable() { return new EntityTable<>(PRODUCT_DEFAULT_SQL_VIEW); }

    //~ Fields ...................................................................................................................

    @NotNull public static final ProductDefaultSqlViewTable PRODUCT_DEFAULT_SQL_VIEW = new ProductDefaultSqlViewTable();

}
