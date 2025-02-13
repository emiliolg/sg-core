package tekgenesis.sales.basic.g;

import tekgenesis.cache.CacheType;
import tekgenesis.common.util.Conversions;
import tekgenesis.persistence.TableField.DTime;
import tekgenesis.persistence.DbTable;
import tekgenesis.persistence.EntityTable;
import java.util.EnumSet;
import tekgenesis.persistence.InnerEntityTable;
import tekgenesis.persistence.TableField.Int;
import tekgenesis.persistence.TableField.LongFld;
import org.jetbrains.annotations.NotNull;
import tekgenesis.sales.basic.ProdByCatView;
import tekgenesis.persistence.TableField.Str;
import tekgenesis.common.core.Strings;
import tekgenesis.common.core.Tuple;
import static tekgenesis.type.Modifier.REMOTE;
import static tekgenesis.common.collections.Colls.listOf;

/** Metadata class for table associated to entity tekgenesis.sales.basic.ProdByCatView */
@SuppressWarnings({"DuplicateStringLiteralInspection", "MagicNumber"})
public class ProdByCatViewTable
    extends DbTable<ProdByCatView,Tuple<String,Integer>>
{

    //~ Fields ...................................................................................................................

    @NotNull public final Int SEQ_ID;
    @NotNull public final LongFld CAT_VID;
    @NotNull public final DTime UPDATE_TIME;
    @NotNull public final Str PRODUCT_VID;

    //~ Constructors .............................................................................................................

    private ProdByCatViewTable() {
        super(ProdByCatView.class,"BASIC","PROD_BY_CAT_VIEW","",EnumSet.of(REMOTE),CacheType.NONE);
        SEQ_ID = intField("seqId", "SEQ_ID", false, 9);
        CAT_VID = longField("catVid", "CAT_VID", false, 18);
        UPDATE_TIME = dTimeField("updateTime", "UPDATE_TIME");
        PRODUCT_VID = strField("productVid", "PRODUCT_VID", 8);
        primaryKey(listOf(PRODUCT_VID, SEQ_ID));
    }

    //~ Methods ..................................................................................................................

    @NotNull protected Tuple<String,Integer> strToKey(@NotNull String key) {
        final String[] parts = Strings.splitToArray(key, 2);
        return Tuple.tuple2(parts[0], Conversions.toInt(parts[1]));
    }

    @Override @NotNull public final ProdByCatViewTable as(@NotNull String alias) { return createAlias(new ProdByCatViewTable(), alias); }

    @Override @NotNull protected final EntityTable<ProdByCatView,Tuple<String,Integer>> createEntityTable() { return new InnerEntityTable<>(PROD_BY_CAT_VIEW); }

    //~ Fields ...................................................................................................................

    @NotNull public static final ProdByCatViewTable PROD_BY_CAT_VIEW = new ProdByCatViewTable();

}
