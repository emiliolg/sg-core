package tekgenesis.sales.basic.g;

import tekgenesis.cache.CacheType;
import tekgenesis.common.util.Conversions;
import tekgenesis.persistence.TableField.DTime;
import tekgenesis.persistence.DbTable;
import tekgenesis.persistence.EntityTable;
import java.util.EnumSet;
import tekgenesis.persistence.TableField.Int;
import org.jetbrains.annotations.NotNull;
import tekgenesis.common.core.Option;
import tekgenesis.sales.basic.ProductDefaultViewInners;
import tekgenesis.sales.basic.ProductDefaultViewInnersSearcher;
import tekgenesis.persistence.TableField.Res;
import tekgenesis.persistence.TableField.Str;
import static tekgenesis.type.Modifier.DEFAULT_SEARCHABLE;
import static tekgenesis.type.Modifier.REMOTE;
import static tekgenesis.common.collections.Colls.listOf;

/** Metadata class for table associated to entity tekgenesis.sales.basic.ProductDefaultViewInners */
@SuppressWarnings({"DuplicateStringLiteralInspection", "MagicNumber"})
public class ProductDefaultViewInnersTable
    extends DbTable<ProductDefaultViewInners,Integer>
{

    //~ Fields ...................................................................................................................

    @NotNull public final Int ID;
    @NotNull public final Str VDESCR;
    @NotNull public final Int V_CATEGORY_ID;
    @NotNull public final Res IMAGE;
    @NotNull public final DTime UPDATE_TIME;

    //~ Constructors .............................................................................................................

    private ProductDefaultViewInnersTable() {
        super(ProductDefaultViewInners.class,"BASIC","PRODUCT_DEFAULT_VIEW_INNERS","PRODUCT_DEFAULT_INNERS_SEQ",EnumSet.of(REMOTE, DEFAULT_SEARCHABLE),CacheType.NONE);
        ID = intField("id", "ID", false, 9);
        VDESCR = strField("vdescr", "VDESCR", 100);
        V_CATEGORY_ID = intField("vCategoryId", "V_CATEGORY_ID", false, 9);
        IMAGE = resourceField("image", "IMAGE");
        UPDATE_TIME = dTimeField("updateTime", "UPDATE_TIME");
        primaryKey(listOf(ID));
    }

    //~ Methods ..................................................................................................................

    @NotNull protected Integer strToKey(@NotNull String key) { return Conversions.toInt(key); }

    @Override @NotNull protected Option<ProductDefaultViewInnersSearcher> searcher() {
        return Option.of(ProductDefaultViewInnersSearcher.PRODUCT_DEFAULT_VIEW_INNERS_SEARCHER);
    }

    @Override @NotNull public final ProductDefaultViewInnersTable as(@NotNull String alias) {
        return createAlias(new ProductDefaultViewInnersTable(), alias);
    }

    @Override @NotNull protected final EntityTable<ProductDefaultViewInners,Integer> createEntityTable() { return new EntityTable<>(PRODUCT_DEFAULT_VIEW_INNERS); }

    //~ Fields ...................................................................................................................

    @NotNull public static final ProductDefaultViewInnersTable PRODUCT_DEFAULT_VIEW_INNERS = new ProductDefaultViewInnersTable();

}
