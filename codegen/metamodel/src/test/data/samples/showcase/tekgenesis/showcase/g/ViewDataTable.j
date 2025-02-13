package tekgenesis.showcase.g;

import tekgenesis.cache.CacheType;
import tekgenesis.common.util.Conversions;
import tekgenesis.persistence.TableField.DTime;
import tekgenesis.persistence.DbTable;
import tekgenesis.persistence.EntityTable;
import tekgenesis.persistence.TableField.Int;
import tekgenesis.type.Modifier;
import org.jetbrains.annotations.NotNull;
import tekgenesis.showcase.ViewData;
import static tekgenesis.common.collections.Colls.listOf;

/** Metadata class for table associated to entity tekgenesis.showcase.ViewData */
@SuppressWarnings({"DuplicateStringLiteralInspection", "MagicNumber"})
public class ViewDataTable
    extends DbTable<ViewData,Integer>
{

    //~ Fields ...................................................................................................................

    @NotNull public final Int ID;
    @NotNull public final Int CURRENT;
    @NotNull public final DTime UPDATE_TIME;

    //~ Constructors .............................................................................................................

    private ViewDataTable() {
        super(ViewData.class,"SHOWCASE","VIEW_DATA","VIEW_DATA_SEQ",Modifier.NONE,CacheType.NONE);
        ID = intField("id", "ID", false, 9);
        CURRENT = intField("current", "CURRENT", false, 9);
        UPDATE_TIME = dTimeField("updateTime", "UPDATE_TIME");
        primaryKey(listOf(ID));
    }

    //~ Methods ..................................................................................................................

    @NotNull protected Integer strToKey(@NotNull String key) { return Conversions.toInt(key); }

    @Override @NotNull public final ViewDataTable as(@NotNull String alias) { return createAlias(new ViewDataTable(), alias); }

    @Override @NotNull protected final EntityTable<ViewData,Integer> createEntityTable() { return new EntityTable<>(VIEW_DATA); }

    //~ Fields ...................................................................................................................

    @NotNull public static final ViewDataTable VIEW_DATA = new ViewDataTable();

}
