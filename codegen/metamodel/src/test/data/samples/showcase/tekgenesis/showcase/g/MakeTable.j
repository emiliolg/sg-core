package tekgenesis.showcase.g;

import tekgenesis.cache.CacheType;
import tekgenesis.common.util.Conversions;
import tekgenesis.showcase.Country;
import tekgenesis.persistence.TableField.DTime;
import tekgenesis.persistence.DbTable;
import tekgenesis.persistence.EntityTable;
import tekgenesis.persistence.TableField.Enum;
import tekgenesis.persistence.TableField.Int;
import tekgenesis.showcase.Make;
import tekgenesis.showcase.MakeSearcher;
import tekgenesis.type.Modifier;
import org.jetbrains.annotations.NotNull;
import tekgenesis.common.core.Option;
import tekgenesis.persistence.TableField.Str;
import static tekgenesis.common.collections.Colls.listOf;

/** Metadata class for table associated to entity tekgenesis.showcase.Make */
@SuppressWarnings({"DuplicateStringLiteralInspection", "MagicNumber"})
public class MakeTable
    extends DbTable<Make,Integer>
{

    //~ Fields ...................................................................................................................

    @NotNull public final Int ID;
    @NotNull public final Str NAME;
    @NotNull public final Enum<Country,String> ORIGIN;
    @NotNull public final DTime UPDATE_TIME;

    //~ Constructors .............................................................................................................

    private MakeTable() {
        super(Make.class,"SHOWCASE","MAKE","MAKE_SEQ",Modifier.NONE,CacheType.NONE);
        ID = intField("id", "ID", false, 9);
        NAME = strField("name", "NAME", 30);
        ORIGIN = enumField("origin", "ORIGIN", Country.class);
        UPDATE_TIME = dTimeField("updateTime", "UPDATE_TIME");
        primaryKey(listOf(ID));
    }

    //~ Methods ..................................................................................................................

    @NotNull protected Integer strToKey(@NotNull String key) { return Conversions.toInt(key); }

    @Override @NotNull protected Option<MakeSearcher> searcher() { return Option.of(MakeSearcher.MAKE_SEARCHER); }

    @Override @NotNull public final MakeTable as(@NotNull String alias) { return createAlias(new MakeTable(), alias); }

    @Override @NotNull protected final EntityTable<Make,Integer> createEntityTable() { return new EntityTable<>(MAKE); }

    //~ Fields ...................................................................................................................

    @NotNull public static final MakeTable MAKE = new MakeTable();

}
