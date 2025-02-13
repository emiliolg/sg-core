package tekgenesis.sales.basic.g;

import tekgenesis.cache.CacheType;
import tekgenesis.common.util.Conversions;
import tekgenesis.persistence.TableField.DTime;
import tekgenesis.persistence.DbTable;
import tekgenesis.persistence.EntityTable;
import tekgenesis.persistence.TableField.Int;
import tekgenesis.sales.basic.LongKey;
import tekgenesis.type.Modifier;
import org.jetbrains.annotations.NotNull;
import tekgenesis.common.core.Strings;
import tekgenesis.common.core.Tuple;
import tekgenesis.common.core.Tuple6;
import static tekgenesis.common.collections.Colls.listOf;

/** Metadata class for table associated to entity tekgenesis.sales.basic.LongKey */
@SuppressWarnings({"DuplicateStringLiteralInspection", "MagicNumber"})
public class LongKeyTable
    extends DbTable<LongKey,Tuple6<Integer,Integer,Integer,Integer,Integer,Integer>>
{

    //~ Fields ...................................................................................................................

    @NotNull public final Int A1;
    @NotNull public final Int A2;
    @NotNull public final Int A3;
    @NotNull public final Int A4;
    @NotNull public final Int A5;
    @NotNull public final Int A6;
    @NotNull public final Int A7;
    @NotNull public final DTime UPDATE_TIME;

    //~ Constructors .............................................................................................................

    private LongKeyTable() {
        super(LongKey.class,"BASIC","LONG_KEY","",Modifier.NONE,CacheType.NONE);
        A1 = intField("a1", "A1", false, 9);
        A2 = intField("a2", "A2", false, 9);
        A3 = intField("a3", "A3", false, 9);
        A4 = intField("a4", "A4", false, 9);
        A5 = intField("a5", "A5", false, 9);
        A6 = intField("a6", "A6", false, 9);
        A7 = intField("a7", "A7", false, 9);
        UPDATE_TIME = dTimeField("updateTime", "UPDATE_TIME");
        primaryKey(listOf(A1, A2, A3, A4, A5, A6));
    }

    //~ Methods ..................................................................................................................

    @NotNull protected Tuple6<Integer,Integer,Integer,Integer,Integer,Integer> strToKey(@NotNull String key) {
        final String[] parts = Strings.splitToArray(key, 6);
        return Tuple.tuple(Conversions.toInt(parts[0]), Conversions.toInt(parts[1]), Conversions.toInt(parts[2]), Conversions.toInt(parts[3]), Conversions.toInt(parts[4]), Conversions.toInt(parts[5]));
    }

    @Override @NotNull public final LongKeyTable as(@NotNull String alias) { return createAlias(new LongKeyTable(), alias); }

    @Override @NotNull protected final EntityTable<LongKey,Tuple6<Integer,Integer,Integer,Integer,Integer,Integer>> createEntityTable() { return new EntityTable<>(LONG_KEY); }

    //~ Fields ...................................................................................................................

    @NotNull public static final LongKeyTable LONG_KEY = new LongKeyTable();

}
