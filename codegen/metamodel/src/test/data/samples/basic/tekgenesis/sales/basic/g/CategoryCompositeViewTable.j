package tekgenesis.sales.basic.g;

import tekgenesis.cache.CacheType;
import tekgenesis.sales.basic.CategoryCompositeView;
import tekgenesis.common.util.Conversions;
import tekgenesis.persistence.TableField.DTime;
import tekgenesis.persistence.DbTable;
import tekgenesis.persistence.EntityTable;
import java.util.EnumSet;
import tekgenesis.persistence.TableField.LongFld;
import org.jetbrains.annotations.NotNull;
import tekgenesis.persistence.TableField.Str;
import tekgenesis.common.core.Strings;
import tekgenesis.common.core.Tuple;
import tekgenesis.common.core.Tuple3;
import static tekgenesis.type.Modifier.REMOTE;
import static tekgenesis.common.collections.Colls.listOf;

/** Metadata class for table associated to entity tekgenesis.sales.basic.CategoryCompositeView */
@SuppressWarnings({"DuplicateStringLiteralInspection", "MagicNumber"})
public class CategoryCompositeViewTable
    extends DbTable<CategoryCompositeView,Tuple3<Long,String,String>>
{

    //~ Fields ...................................................................................................................

    @NotNull public final LongFld VID;
    @NotNull public final Str VNAME;
    @NotNull public final Str VDESCR;
    @NotNull public final Str VSHORT;
    @NotNull public final DTime UPDATE_TIME;

    //~ Constructors .............................................................................................................

    private CategoryCompositeViewTable() {
        super(CategoryCompositeView.class,"BASIC","CATEGORY_COMPOSITE_VIEW","",EnumSet.of(REMOTE),CacheType.NONE);
        VID = longField("vid", "VID", false, 18);
        VNAME = strField("vname", "VNAME", 30);
        VDESCR = strField("vdescr", "VDESCR", 120);
        VSHORT = strField("vshort", "VSHORT", 120);
        UPDATE_TIME = dTimeField("updateTime", "UPDATE_TIME");
        primaryKey(listOf(VID, VDESCR, VSHORT));
    }

    //~ Methods ..................................................................................................................

    @NotNull protected Tuple3<Long,String,String> strToKey(@NotNull String key) {
        final String[] parts = Strings.splitToArray(key, 3);
        return Tuple.tuple(Conversions.toLong(parts[0]), parts[1], parts[2]);
    }

    @Override @NotNull public final CategoryCompositeViewTable as(@NotNull String alias) { return createAlias(new CategoryCompositeViewTable(), alias); }

    @Override @NotNull protected final EntityTable<CategoryCompositeView,Tuple3<Long,String,String>> createEntityTable() { return new EntityTable<>(CATEGORY_COMPOSITE_VIEW); }

    //~ Fields ...................................................................................................................

    @NotNull public static final CategoryCompositeViewTable CATEGORY_COMPOSITE_VIEW = new CategoryCompositeViewTable();

}
