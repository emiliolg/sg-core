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
import tekgenesis.sales.basic.Rev;
import tekgenesis.persistence.TableField.Str;
import tekgenesis.common.core.Strings;
import tekgenesis.common.core.Tuple;
import static tekgenesis.type.Modifier.AUDITABLE;
import static tekgenesis.type.Modifier.REMOTABLE;
import static tekgenesis.common.collections.Colls.listOf;

/** Metadata class for table associated to entity tekgenesis.sales.basic.Rev */
@SuppressWarnings({"DuplicateStringLiteralInspection", "MagicNumber"})
public class RevTable
    extends DbTable<Rev,Tuple<Integer,Integer>>
{

    //~ Fields ...................................................................................................................

    @NotNull public final Int PRODUCT_DEFAULT_INNERS_ID;
    @NotNull public final Int SEQ_ID;
    @NotNull public final Str REVIEW;
    @NotNull public final DTime UPDATE_TIME;
    @NotNull public final DTime CREATION_TIME;
    @NotNull public final Str CREATION_USER;
    @NotNull public final Str UPDATE_USER;

    //~ Constructors .............................................................................................................

    private RevTable() {
        super(Rev.class,"BASIC","REV","",EnumSet.of(AUDITABLE, REMOTABLE),CacheType.NONE);
        PRODUCT_DEFAULT_INNERS_ID = intField("productDefaultInnersId", "PRODUCT_DEFAULT_INNERS_ID", false, 9);
        SEQ_ID = intField("seqId", "SEQ_ID", false, 9);
        REVIEW = strField("review", "REVIEW", 255);
        UPDATE_TIME = dTimeField("updateTime", "UPDATE_TIME");
        CREATION_TIME = dTimeField("creationTime", "CREATION_TIME");
        CREATION_USER = strInternField("creationUser", "CREATION_USER", 100);
        UPDATE_USER = strInternField("updateUser", "UPDATE_USER", 100);
        primaryKey(listOf(PRODUCT_DEFAULT_INNERS_ID, SEQ_ID));
    }

    //~ Methods ..................................................................................................................

    @NotNull protected Tuple<Integer,Integer> strToKey(@NotNull String key) {
        final String[] parts = Strings.splitToArray(key, 2);
        return Tuple.tuple2(Conversions.toInt(parts[0]), Conversions.toInt(parts[1]));
    }

    @Override @NotNull public final RevTable as(@NotNull String alias) { return createAlias(new RevTable(), alias); }

    @Override @NotNull protected final EntityTable<Rev,Tuple<Integer,Integer>> createEntityTable() { return new InnerEntityTable<>(REV); }

    //~ Fields ...................................................................................................................

    @NotNull public static final RevTable REV = new RevTable();

}
