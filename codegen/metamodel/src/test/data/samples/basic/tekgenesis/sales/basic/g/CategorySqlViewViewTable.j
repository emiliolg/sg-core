package tekgenesis.sales.basic.g;

import tekgenesis.cache.CacheType;
import tekgenesis.sales.basic.CategorySqlViewView;
import tekgenesis.sales.basic.CategorySqlViewViewSearcher;
import tekgenesis.common.util.Conversions;
import tekgenesis.persistence.TableField.DTime;
import tekgenesis.persistence.DbTable;
import tekgenesis.persistence.EntityTable;
import java.util.EnumSet;
import tekgenesis.persistence.TableField.Int;
import org.jetbrains.annotations.NotNull;
import tekgenesis.common.core.Option;
import tekgenesis.persistence.TableField.Str;
import static tekgenesis.type.Modifier.REMOTE;
import static tekgenesis.common.collections.Colls.listOf;

/** Metadata class for table associated to entity tekgenesis.sales.basic.CategorySqlViewView */
@SuppressWarnings({"DuplicateStringLiteralInspection", "MagicNumber"})
public class CategorySqlViewViewTable
    extends DbTable<CategorySqlViewView,Integer>
{

    //~ Fields ...................................................................................................................

    @NotNull public final Str VNAME;
    @NotNull public final Int VID;
    @NotNull public final Str VDESCR;
    @NotNull public final DTime UPDATE_TIME;

    //~ Constructors .............................................................................................................

    private CategorySqlViewViewTable() {
        super(CategorySqlViewView.class,"BASIC","CATEGORY_SQL_VIEW_VIEW","",EnumSet.of(REMOTE),CacheType.NONE);
        VNAME = strField("vname", "VNAME", 30);
        VID = intField("vid", "VID", false, 9);
        VDESCR = strField("vdescr", "VDESCR", 120);
        UPDATE_TIME = dTimeField("updateTime", "UPDATE_TIME");
        primaryKey(listOf(VID));
    }

    //~ Methods ..................................................................................................................

    @NotNull protected Integer strToKey(@NotNull String key) { return Conversions.toInt(key); }

    @Override @NotNull protected Option<CategorySqlViewViewSearcher> searcher() {
        return Option.of(CategorySqlViewViewSearcher.CATEGORY_SQL_VIEW_VIEW_SEARCHER);
    }

    @Override @NotNull public final CategorySqlViewViewTable as(@NotNull String alias) { return createAlias(new CategorySqlViewViewTable(), alias); }

    @Override @NotNull protected final EntityTable<CategorySqlViewView,Integer> createEntityTable() { return new EntityTable<>(CATEGORY_SQL_VIEW_VIEW); }

    //~ Fields ...................................................................................................................

    @NotNull public static final CategorySqlViewViewTable CATEGORY_SQL_VIEW_VIEW = new CategorySqlViewViewTable();

}
