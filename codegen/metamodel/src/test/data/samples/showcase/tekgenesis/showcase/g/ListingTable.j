package tekgenesis.showcase.g;

import tekgenesis.cache.CacheType;
import tekgenesis.common.util.Conversions;
import tekgenesis.persistence.TableField.DTime;
import tekgenesis.persistence.DbTable;
import tekgenesis.persistence.EntityTable;
import tekgenesis.persistence.InnerEntityTable;
import tekgenesis.persistence.TableField.Int;
import tekgenesis.showcase.Listing;
import tekgenesis.type.Modifier;
import org.jetbrains.annotations.NotNull;
import tekgenesis.common.core.Strings;
import tekgenesis.common.core.Tuple;
import static tekgenesis.common.collections.Colls.listOf;

/** Metadata class for table associated to entity tekgenesis.showcase.Listing */
@SuppressWarnings({"DuplicateStringLiteralInspection", "MagicNumber"})
public class ListingTable
    extends DbTable<Listing,Tuple<Integer,Integer>>
{

    //~ Fields ...................................................................................................................

    @NotNull public final Int VIEW_DATA_ID;
    @NotNull public final Int SEQ_ID;
    @NotNull public final Int PK;
    @NotNull public final DTime UPDATE_TIME;

    //~ Constructors .............................................................................................................

    private ListingTable() {
        super(Listing.class,"SHOWCASE","LISTING","",Modifier.NONE,CacheType.NONE);
        VIEW_DATA_ID = intField("viewDataId", "VIEW_DATA_ID", false, 9);
        SEQ_ID = intField("seqId", "SEQ_ID", false, 9);
        PK = intField("pk", "PK", false, 9);
        UPDATE_TIME = dTimeField("updateTime", "UPDATE_TIME");
        primaryKey(listOf(VIEW_DATA_ID, SEQ_ID));
    }

    //~ Methods ..................................................................................................................

    @NotNull protected Tuple<Integer,Integer> strToKey(@NotNull String key) {
        final String[] parts = Strings.splitToArray(key, 2);
        return Tuple.tuple2(Conversions.toInt(parts[0]), Conversions.toInt(parts[1]));
    }

    @Override @NotNull public final ListingTable as(@NotNull String alias) { return createAlias(new ListingTable(), alias); }

    @Override @NotNull protected final EntityTable<Listing,Tuple<Integer,Integer>> createEntityTable() { return new InnerEntityTable<>(LISTING); }

    //~ Fields ...................................................................................................................

    @NotNull public static final ListingTable LISTING = new ListingTable();

}
