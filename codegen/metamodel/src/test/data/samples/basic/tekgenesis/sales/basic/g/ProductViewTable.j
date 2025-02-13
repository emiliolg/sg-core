package tekgenesis.sales.basic.g;

import tekgenesis.cache.CacheType;
import tekgenesis.persistence.TableField.DTime;
import tekgenesis.persistence.DbTable;
import tekgenesis.persistence.EntityTable;
import java.util.EnumSet;
import tekgenesis.persistence.TableField.EnumerationSet;
import tekgenesis.persistence.TableField.LongFld;
import org.jetbrains.annotations.NotNull;
import tekgenesis.common.core.Option;
import tekgenesis.sales.basic.ProductView;
import tekgenesis.sales.basic.ProductViewSearcher;
import tekgenesis.persistence.TableField.Str;
import tekgenesis.sales.basic.Tag;
import static tekgenesis.type.Modifier.REMOTE;
import static tekgenesis.common.collections.Colls.listOf;

/** Metadata class for table associated to entity tekgenesis.sales.basic.ProductView */
@SuppressWarnings({"DuplicateStringLiteralInspection", "MagicNumber"})
public class ProductViewTable
    extends DbTable<ProductView,String>
{

    //~ Fields ...................................................................................................................

    @NotNull public final Str VID;
    @NotNull public final Str VDESCR;
    @NotNull public final LongFld VCATEGORY_VID;
    @NotNull public final Str CATEGORY_ATT;
    @NotNull public final EnumerationSet<Tag,String> TAGS;
    @NotNull public final DTime UPDATE_TIME;

    //~ Constructors .............................................................................................................

    private ProductViewTable() {
        super(ProductView.class,"BASIC","PRODUCT_VIEW","",EnumSet.of(REMOTE),CacheType.NONE);
        VID = strField("vid", "VID", 8);
        VDESCR = strField("vdescr", "VDESCR", 100);
        VCATEGORY_VID = longField("vcategoryVid", "VCATEGORY_VID", false, 18);
        CATEGORY_ATT = strField("categoryAtt", "CATEGORY_ATT", 150);
        TAGS = enumSetField("tags", "TAGS", Tag.class);
        UPDATE_TIME = dTimeField("updateTime", "UPDATE_TIME");
        primaryKey(listOf(VID));
    }

    //~ Methods ..................................................................................................................

    @NotNull protected String strToKey(@NotNull String key) { return key; }

    @Override @NotNull protected Option<ProductViewSearcher> searcher() { return Option.of(ProductViewSearcher.PRODUCT_VIEW_SEARCHER); }

    @Override @NotNull public final ProductViewTable as(@NotNull String alias) { return createAlias(new ProductViewTable(), alias); }

    @Override @NotNull protected final EntityTable<ProductView,String> createEntityTable() { return new EntityTable<>(PRODUCT_VIEW); }

    //~ Fields ...................................................................................................................

    @NotNull public static final ProductViewTable PRODUCT_VIEW = new ProductViewTable();

}
