package tekgenesis.sales.basic.g;

import tekgenesis.cache.CacheType;
import tekgenesis.sales.basic.CategoryView;
import tekgenesis.sales.basic.CategoryViewSearcher;
import tekgenesis.common.util.Conversions;
import tekgenesis.persistence.TableField.DTime;
import tekgenesis.persistence.DbTable;
import tekgenesis.persistence.EntityTable;
import java.util.EnumSet;
import tekgenesis.persistence.TableField.LongFld;
import org.jetbrains.annotations.NotNull;
import tekgenesis.common.core.Option;
import tekgenesis.persistence.TableField.Str;
import static tekgenesis.type.Modifier.REMOTE;
import static tekgenesis.common.collections.Colls.listOf;

/** Metadata class for table associated to entity tekgenesis.sales.basic.CategoryView */
@SuppressWarnings({"DuplicateStringLiteralInspection", "MagicNumber"})
public class CategoryViewTable
    extends DbTable<CategoryView,Long>
{

    //~ Fields ...................................................................................................................

    @NotNull public final LongFld VID;
    @NotNull public final Str VNAME;
    @NotNull public final Str VDESCR;
    @NotNull public final DTime UPDATE_TIME;

    //~ Constructors .............................................................................................................

    private CategoryViewTable() {
        super(CategoryView.class,"BASIC","CATEGORY_VIEW","",EnumSet.of(REMOTE),CacheType.NONE);
        VID = longField("vid", "VID", false, 18);
        VNAME = strField("vname", "VNAME", 30);
        VDESCR = strField("vdescr", "VDESCR", 120);
        UPDATE_TIME = dTimeField("updateTime", "UPDATE_TIME");
        primaryKey(listOf(VID));
    }

    //~ Methods ..................................................................................................................

    @NotNull protected Long strToKey(@NotNull String key) { return Conversions.toLong(key); }

    @Override @NotNull protected Option<CategoryViewSearcher> searcher() {
        return Option.of(CategoryViewSearcher.CATEGORY_VIEW_SEARCHER);
    }

    @Override @NotNull public final CategoryViewTable as(@NotNull String alias) { return createAlias(new CategoryViewTable(), alias); }

    @Override @NotNull protected final EntityTable<CategoryView,Long> createEntityTable() { return new EntityTable<>(CATEGORY_VIEW); }

    //~ Fields ...................................................................................................................

    @NotNull public static final CategoryViewTable CATEGORY_VIEW = new CategoryViewTable();

}
