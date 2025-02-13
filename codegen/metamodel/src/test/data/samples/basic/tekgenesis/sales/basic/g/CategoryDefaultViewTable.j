package tekgenesis.sales.basic.g;

import tekgenesis.cache.CacheType;
import tekgenesis.sales.basic.CategoryDefaultView;
import tekgenesis.sales.basic.CategoryDefaultViewSearcher;
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

/** Metadata class for table associated to entity tekgenesis.sales.basic.CategoryDefaultView */
@SuppressWarnings({"DuplicateStringLiteralInspection", "MagicNumber"})
public class CategoryDefaultViewTable
    extends DbTable<CategoryDefaultView,Integer>
{

    //~ Fields ...................................................................................................................

    @NotNull public final Int ID;
    @NotNull public final Str VNAME;
    @NotNull public final Str VDESCR;
    @NotNull public final Int VPARENT_ID;
    @NotNull public final DTime UPDATE_TIME;
    @NotNull public final DTime DEPRECATION_TIME;
    @NotNull public final Str DEPRECATION_USER;

    //~ Constructors .............................................................................................................

    private CategoryDefaultViewTable() {
        super(CategoryDefaultView.class,"BASIC","CATEGORY_DEFAULT_VIEW","CATEGORY_DEFAULT_SEQ",EnumSet.of(REMOTE),CacheType.NONE);
        ID = intField("id", "ID", false, 9);
        VNAME = strField("vname", "VNAME", 30);
        VDESCR = strField("vdescr", "VDESCR", 120);
        VPARENT_ID = intField("vparentId", "VPARENT_ID", false, 9);
        UPDATE_TIME = dTimeField("updateTime", "UPDATE_TIME");
        DEPRECATION_TIME = dTimeField("deprecationTime", "DEPRECATION_TIME");
        DEPRECATION_USER = strField("deprecationUser", "DEPRECATION_USER", 100);
        primaryKey(listOf(ID));
    }

    //~ Methods ..................................................................................................................

    @NotNull protected Integer strToKey(@NotNull String key) { return Conversions.toInt(key); }

    @Override @NotNull protected Option<CategoryDefaultViewSearcher> searcher() {
        return Option.of(CategoryDefaultViewSearcher.CATEGORY_DEFAULT_VIEW_SEARCHER);
    }

    @Override @NotNull public final CategoryDefaultViewTable as(@NotNull String alias) { return createAlias(new CategoryDefaultViewTable(), alias); }

    @Override @NotNull protected final EntityTable<CategoryDefaultView,Integer> createEntityTable() { return new EntityTable<>(CATEGORY_DEFAULT_VIEW); }

    //~ Fields ...................................................................................................................

    @NotNull public static final CategoryDefaultViewTable CATEGORY_DEFAULT_VIEW = new CategoryDefaultViewTable();

}
