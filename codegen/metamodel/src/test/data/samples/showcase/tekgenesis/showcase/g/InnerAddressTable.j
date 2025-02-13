package tekgenesis.showcase.g;

import tekgenesis.cache.CacheType;
import tekgenesis.common.util.Conversions;
import tekgenesis.persistence.TableField.DTime;
import tekgenesis.persistence.DbTable;
import tekgenesis.persistence.EntityTable;
import tekgenesis.showcase.InnerAddress;
import tekgenesis.persistence.InnerEntityTable;
import tekgenesis.persistence.TableField.Int;
import tekgenesis.type.Modifier;
import org.jetbrains.annotations.NotNull;
import tekgenesis.persistence.TableField.Str;
import tekgenesis.common.core.Strings;
import tekgenesis.common.core.Tuple;
import static tekgenesis.common.collections.Colls.listOf;

/** Metadata class for table associated to entity tekgenesis.showcase.InnerAddress */
@SuppressWarnings({"DuplicateStringLiteralInspection", "MagicNumber"})
public class InnerAddressTable
    extends DbTable<InnerAddress,Tuple<Integer,Integer>>
{

    //~ Fields ...................................................................................................................

    @NotNull public final Int ADDRESSES_ID;
    @NotNull public final Int SEQ_ID;
    @NotNull public final Str STREET;
    @NotNull public final Str CITY;
    @NotNull public final Str STATE;
    @NotNull public final Str ZIP;
    @NotNull public final Str COUNTRY;
    @NotNull public final DTime UPDATE_TIME;

    //~ Constructors .............................................................................................................

    private InnerAddressTable() {
        super(InnerAddress.class,"SHOWCASE","INNER_ADDRESS","",Modifier.NONE,CacheType.NONE);
        ADDRESSES_ID = intField("addressesId", "ADDRESSES_ID", false, 9);
        SEQ_ID = intField("seqId", "SEQ_ID", false, 9);
        STREET = strField("street", "STREET", 60);
        CITY = strField("city", "CITY", 40);
        STATE = strField("state", "STATE", 40);
        ZIP = strField("zip", "ZIP", 10);
        COUNTRY = strField("country", "COUNTRY", 30);
        UPDATE_TIME = dTimeField("updateTime", "UPDATE_TIME");
        primaryKey(listOf(ADDRESSES_ID, SEQ_ID));
    }

    //~ Methods ..................................................................................................................

    @NotNull protected Tuple<Integer,Integer> strToKey(@NotNull String key) {
        final String[] parts = Strings.splitToArray(key, 2);
        return Tuple.tuple2(Conversions.toInt(parts[0]), Conversions.toInt(parts[1]));
    }

    @Override @NotNull public final InnerAddressTable as(@NotNull String alias) { return createAlias(new InnerAddressTable(), alias); }

    @Override @NotNull protected final EntityTable<InnerAddress,Tuple<Integer,Integer>> createEntityTable() { return new InnerEntityTable<>(INNER_ADDRESS); }

    //~ Fields ...................................................................................................................

    @NotNull public static final InnerAddressTable INNER_ADDRESS = new InnerAddressTable();

}
