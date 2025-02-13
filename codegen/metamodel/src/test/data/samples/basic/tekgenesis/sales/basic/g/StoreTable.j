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
import tekgenesis.persistence.TableField.Real;
import tekgenesis.sales.basic.Store;
import tekgenesis.sales.basic.StoreSearcher;
import tekgenesis.persistence.TableField.Str;
import static tekgenesis.type.Modifier.DEFAULT_SEARCHABLE;
import static tekgenesis.common.collections.Colls.listOf;

/** Metadata class for table associated to entity tekgenesis.sales.basic.Store */
@SuppressWarnings({"DuplicateStringLiteralInspection", "MagicNumber"})
public class StoreTable
    extends DbTable<Store,Integer>
{

    //~ Fields ...................................................................................................................

    @NotNull public final Int ID;
    @NotNull public final Str NAME;
    @NotNull public final Str ADDRESS;
    @NotNull public final Real LAT;
    @NotNull public final Real LNG;
    @NotNull public final DTime UPDATE_TIME;

    //~ Constructors .............................................................................................................

    private StoreTable() {
        super(Store.class,"BASIC","STORE","STORE_SEQ",EnumSet.of(DEFAULT_SEARCHABLE),CacheType.NONE);
        ID = intField("id", "ID", false, 9);
        NAME = strField("name", "NAME", 50);
        ADDRESS = strField("address", "ADDRESS", 150);
        LAT = realField("lat", "LAT", true);
        LNG = realField("lng", "LNG", true);
        UPDATE_TIME = dTimeField("updateTime", "UPDATE_TIME");
        primaryKey(listOf(ID));
    }

    //~ Methods ..................................................................................................................

    @NotNull protected Integer strToKey(@NotNull String key) { return Conversions.toInt(key); }

    @Override @NotNull protected Option<StoreSearcher> searcher() { return Option.of(StoreSearcher.STORE_SEARCHER); }

    @Override @NotNull public final StoreTable as(@NotNull String alias) { return createAlias(new StoreTable(), alias); }

    @Override @NotNull protected final EntityTable<Store,Integer> createEntityTable() { return new EntityTable<>(STORE); }

    //~ Fields ...................................................................................................................

    @NotNull public static final StoreTable STORE = new StoreTable();

}
