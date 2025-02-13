package tekgenesis.sales.basic.g;

import tekgenesis.cache.CacheType;
import tekgenesis.common.util.Conversions;
import tekgenesis.persistence.TableField.DTime;
import tekgenesis.persistence.DbTable;
import tekgenesis.persistence.EntityTable;
import java.util.EnumSet;
import tekgenesis.persistence.TableField.Int;
import org.jetbrains.annotations.NotNull;
import tekgenesis.sales.basic.RevView;
import tekgenesis.persistence.TableField.Str;
import static tekgenesis.type.Modifier.REMOTE;
import static tekgenesis.common.collections.Colls.listOf;

/** Metadata class for table associated to entity tekgenesis.sales.basic.RevView */
@SuppressWarnings({"DuplicateStringLiteralInspection", "MagicNumber"})
public class RevViewTable
    extends DbTable<RevView,Integer>
{

    //~ Fields ...................................................................................................................

    @NotNull public final Int ID;
    @NotNull public final Str REV;
    @NotNull public final Int PROD_ID;
    @NotNull public final DTime UPDATE_TIME;

    //~ Constructors .............................................................................................................

    private RevViewTable() {
        super(RevView.class,"BASIC","REV_VIEW","REVIEW_SEQ",EnumSet.of(REMOTE),CacheType.NONE);
        ID = intField("id", "ID", false, 9);
        REV = strField("rev", "REV", 255);
        PROD_ID = intField("prodId", "PROD_ID", false, 9);
        UPDATE_TIME = dTimeField("updateTime", "UPDATE_TIME");
        primaryKey(listOf(ID));
    }

    //~ Methods ..................................................................................................................

    @NotNull protected Integer strToKey(@NotNull String key) { return Conversions.toInt(key); }

    @Override @NotNull public final RevViewTable as(@NotNull String alias) { return createAlias(new RevViewTable(), alias); }

    @Override @NotNull protected final EntityTable<RevView,Integer> createEntityTable() { return new EntityTable<>(REV_VIEW); }

    //~ Fields ...................................................................................................................

    @NotNull public static final RevViewTable REV_VIEW = new RevViewTable();

}
