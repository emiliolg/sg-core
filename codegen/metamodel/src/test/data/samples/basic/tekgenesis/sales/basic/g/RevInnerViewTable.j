package tekgenesis.sales.basic.g;

import tekgenesis.cache.CacheType;
import tekgenesis.common.util.Conversions;
import tekgenesis.persistence.TableField.DTime;
import tekgenesis.persistence.DbTable;
import tekgenesis.persistence.EntityTable;
import java.util.EnumSet;
import tekgenesis.persistence.InnerEntityTable;
import tekgenesis.persistence.TableField.Int;
import org.jetbrains.annotations.NotNull;
import tekgenesis.sales.basic.RevInnerView;
import tekgenesis.persistence.TableField.Str;
import tekgenesis.common.core.Strings;
import tekgenesis.common.core.Tuple;
import static tekgenesis.type.Modifier.REMOTE;
import static tekgenesis.common.collections.Colls.listOf;

/** Metadata class for table associated to entity tekgenesis.sales.basic.RevInnerView */
@SuppressWarnings({"DuplicateStringLiteralInspection", "MagicNumber"})
public class RevInnerViewTable
    extends DbTable<RevInnerView,Tuple<Integer,Integer>>
{

    //~ Fields ...................................................................................................................

    @NotNull public final Int SEQ_ID;
    @NotNull public final Str REV;
    @NotNull public final DTime UPDATE_TIME;
    @NotNull public final Int PRODUCT_DEFAULT_INNERS_ID;

    //~ Constructors .............................................................................................................

    private RevInnerViewTable() {
        super(RevInnerView.class,"BASIC","REV_INNER_VIEW","",EnumSet.of(REMOTE),CacheType.NONE);
        SEQ_ID = intField("seqId", "SEQ_ID", false, 9);
        REV = strField("rev", "REV", 255);
        UPDATE_TIME = dTimeField("updateTime", "UPDATE_TIME");
        PRODUCT_DEFAULT_INNERS_ID = intField("productDefaultInnersId", "PRODUCT_DEFAULT_INNERS_ID", false, 9);
        primaryKey(listOf(PRODUCT_DEFAULT_INNERS_ID, SEQ_ID));
    }

    //~ Methods ..................................................................................................................

    @NotNull protected Tuple<Integer,Integer> strToKey(@NotNull String key) {
        final String[] parts = Strings.splitToArray(key, 2);
        return Tuple.tuple2(Conversions.toInt(parts[0]), Conversions.toInt(parts[1]));
    }

    @Override @NotNull public final RevInnerViewTable as(@NotNull String alias) { return createAlias(new RevInnerViewTable(), alias); }

    @Override @NotNull protected final EntityTable<RevInnerView,Tuple<Integer,Integer>> createEntityTable() { return new InnerEntityTable<>(REV_INNER_VIEW); }

    //~ Fields ...................................................................................................................

    @NotNull public static final RevInnerViewTable REV_INNER_VIEW = new RevInnerViewTable();

}
