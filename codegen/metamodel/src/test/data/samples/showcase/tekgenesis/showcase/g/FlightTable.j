package tekgenesis.showcase.g;

import tekgenesis.cache.CacheType;
import tekgenesis.common.util.Conversions;
import tekgenesis.persistence.TableField.DTime;
import tekgenesis.persistence.DbTable;
import tekgenesis.persistence.EntityTable;
import tekgenesis.showcase.Flight;
import tekgenesis.persistence.TableField.Int;
import tekgenesis.type.Modifier;
import org.jetbrains.annotations.NotNull;
import tekgenesis.persistence.TableField.Str;
import static tekgenesis.common.collections.Colls.listOf;

/** Metadata class for table associated to entity tekgenesis.showcase.Flight */
@SuppressWarnings({"DuplicateStringLiteralInspection", "MagicNumber"})
public class FlightTable
    extends DbTable<Flight,Integer>
{

    //~ Fields ...................................................................................................................

    @NotNull public final Int ID_KEY;
    @NotNull public final Str NAME;
    @NotNull public final Str FROM;
    @NotNull public final Str TO;
    @NotNull public final Int PRICE;
    @NotNull public final DTime UPDATE_TIME;

    //~ Constructors .............................................................................................................

    private FlightTable() {
        super(Flight.class,"SHOWCASE","FLIGHT","",Modifier.NONE,CacheType.NONE);
        ID_KEY = intField("idKey", "ID_KEY", false, 9);
        NAME = strField("name", "NAME", 20);
        FROM = strField("from", "FROM", 20);
        TO = strField("to", "TO", 20);
        PRICE = intField("price", "PRICE", false, 9);
        UPDATE_TIME = dTimeField("updateTime", "UPDATE_TIME");
        primaryKey(listOf(ID_KEY));
    }

    //~ Methods ..................................................................................................................

    @NotNull protected Integer strToKey(@NotNull String key) { return Conversions.toInt(key); }

    @Override @NotNull public final FlightTable as(@NotNull String alias) { return createAlias(new FlightTable(), alias); }

    @Override @NotNull protected final EntityTable<Flight,Integer> createEntityTable() { return new EntityTable<>(FLIGHT); }

    //~ Fields ...................................................................................................................

    @NotNull public static final FlightTable FLIGHT = new FlightTable();

}
