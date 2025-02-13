package tekgenesis.showcase.g;

import tekgenesis.cache.CacheType;
import tekgenesis.common.util.Conversions;
import tekgenesis.persistence.TableField.DTime;
import tekgenesis.persistence.DbTable;
import tekgenesis.persistence.EntityTable;
import java.util.EnumSet;
import tekgenesis.persistence.TableField.Int;
import tekgenesis.showcase.NamedItem;
import tekgenesis.showcase.NamedItemSearcher;
import org.jetbrains.annotations.NotNull;
import tekgenesis.common.core.Option;
import tekgenesis.persistence.TableField.Str;
import static tekgenesis.type.Modifier.DEFAULT_SEARCHABLE;
import static tekgenesis.common.collections.Colls.listOf;

/** Metadata class for table associated to entity tekgenesis.showcase.NamedItem */
@SuppressWarnings({"DuplicateStringLiteralInspection", "MagicNumber"})
public class NamedItemTable
    extends DbTable<NamedItem,Integer>
{

    //~ Fields ...................................................................................................................

    @NotNull public final Int ID_KEY;
    @NotNull public final Str NAME;
    @NotNull public final Str COLOR;
    @NotNull public final DTime UPDATE_TIME;

    //~ Constructors .............................................................................................................

    private NamedItemTable() {
        super(NamedItem.class,"SHOWCASE","NAMED_ITEM","",EnumSet.of(DEFAULT_SEARCHABLE),CacheType.NONE);
        ID_KEY = intField("idKey", "ID_KEY", false, 9);
        NAME = strField("name", "NAME", 20);
        COLOR = strField("color", "COLOR", 30);
        UPDATE_TIME = dTimeField("updateTime", "UPDATE_TIME");
        primaryKey(listOf(ID_KEY));
    }

    //~ Methods ..................................................................................................................

    @NotNull protected Integer strToKey(@NotNull String key) { return Conversions.toInt(key); }

    @Override @NotNull protected Option<NamedItemSearcher> searcher() { return Option.of(NamedItemSearcher.NAMED_ITEM_SEARCHER); }

    @Override @NotNull public final NamedItemTable as(@NotNull String alias) { return createAlias(new NamedItemTable(), alias); }

    @Override @NotNull protected final EntityTable<NamedItem,Integer> createEntityTable() { return new EntityTable<>(NAMED_ITEM); }

    //~ Fields ...................................................................................................................

    @NotNull public static final NamedItemTable NAMED_ITEM = new NamedItemTable();

}
