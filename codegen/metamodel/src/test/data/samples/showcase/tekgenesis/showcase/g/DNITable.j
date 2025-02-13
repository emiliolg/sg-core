package tekgenesis.showcase.g;

import tekgenesis.cache.CacheType;
import tekgenesis.common.util.Conversions;
import tekgenesis.showcase.DNI;
import tekgenesis.showcase.DNISearcher;
import tekgenesis.persistence.TableField.DTime;
import tekgenesis.persistence.DbTable;
import tekgenesis.persistence.EntityTable;
import java.util.EnumSet;
import tekgenesis.persistence.TableField.Int;
import org.jetbrains.annotations.NotNull;
import tekgenesis.common.core.Option;
import tekgenesis.persistence.TableField.Str;
import static tekgenesis.type.Modifier.DEFAULT_SEARCHABLE;
import static tekgenesis.common.collections.Colls.listOf;

/** Metadata class for table associated to entity tekgenesis.showcase.DNI */
@SuppressWarnings({"DuplicateStringLiteralInspection", "MagicNumber"})
public class DNITable
    extends DbTable<DNI,Integer>
{

    //~ Fields ...................................................................................................................

    @NotNull public final Int NUMBER;
    @NotNull public final Str DESCR;
    @NotNull public final DTime UPDATE_TIME;

    //~ Constructors .............................................................................................................

    private DNITable() {
        super(DNI.class,"SHOWCASE","DNI","",EnumSet.of(DEFAULT_SEARCHABLE),CacheType.NONE);
        NUMBER = intField("number", "NUMBER", false, 9);
        DESCR = strField("descr", "DESCR", 255);
        UPDATE_TIME = dTimeField("updateTime", "UPDATE_TIME");
        primaryKey(listOf(NUMBER));
    }

    //~ Methods ..................................................................................................................

    @NotNull protected Integer strToKey(@NotNull String key) { return Conversions.toInt(key); }

    @Override @NotNull protected Option<DNISearcher> searcher() { return Option.of(DNISearcher.DNISEARCHER); }

    @Override @NotNull public final DNITable as(@NotNull String alias) { return createAlias(new DNITable(), alias); }

    @Override @NotNull protected final EntityTable<DNI,Integer> createEntityTable() { return new EntityTable<>(DNI_); }

    //~ Fields ...................................................................................................................

    @NotNull public static final DNITable DNI_ = new DNITable();

}
