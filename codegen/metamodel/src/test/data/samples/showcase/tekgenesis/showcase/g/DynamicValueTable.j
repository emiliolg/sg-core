package tekgenesis.showcase.g;

import tekgenesis.cache.CacheType;
import tekgenesis.common.util.Conversions;
import tekgenesis.persistence.TableField.DTime;
import tekgenesis.persistence.DbTable;
import tekgenesis.showcase.DynamicValue;
import tekgenesis.persistence.EntityTable;
import tekgenesis.persistence.InnerEntityTable;
import tekgenesis.persistence.TableField.Int;
import tekgenesis.type.Modifier;
import org.jetbrains.annotations.NotNull;
import tekgenesis.persistence.TableField.Str;
import tekgenesis.common.core.Strings;
import tekgenesis.common.core.Tuple;
import static tekgenesis.common.collections.Colls.listOf;

/** Metadata class for table associated to entity tekgenesis.showcase.DynamicValue */
@SuppressWarnings({"DuplicateStringLiteralInspection", "MagicNumber"})
public class DynamicValueTable
    extends DbTable<DynamicValue,Tuple<Integer,Integer>>
{

    //~ Fields ...................................................................................................................

    @NotNull public final Int DYNAMIC_PROPERTY_ID;
    @NotNull public final Int SEQ_ID;
    @NotNull public final Str VALUE;
    @NotNull public final DTime UPDATE_TIME;

    //~ Constructors .............................................................................................................

    private DynamicValueTable() {
        super(DynamicValue.class,"SHOWCASE","DYNAMIC_VALUE","",Modifier.NONE,CacheType.NONE);
        DYNAMIC_PROPERTY_ID = intField("dynamicPropertyId", "DYNAMIC_PROPERTY_ID", false, 9);
        SEQ_ID = intField("seqId", "SEQ_ID", false, 9);
        VALUE = strField("value", "VALUE", 100);
        UPDATE_TIME = dTimeField("updateTime", "UPDATE_TIME");
        primaryKey(listOf(DYNAMIC_PROPERTY_ID, SEQ_ID));
    }

    //~ Methods ..................................................................................................................

    @NotNull protected Tuple<Integer,Integer> strToKey(@NotNull String key) {
        final String[] parts = Strings.splitToArray(key, 2);
        return Tuple.tuple2(Conversions.toInt(parts[0]), Conversions.toInt(parts[1]));
    }

    @Override @NotNull public final DynamicValueTable as(@NotNull String alias) { return createAlias(new DynamicValueTable(), alias); }

    @Override @NotNull protected final EntityTable<DynamicValue,Tuple<Integer,Integer>> createEntityTable() { return new InnerEntityTable<>(DYNAMIC_VALUE); }

    //~ Fields ...................................................................................................................

    @NotNull public static final DynamicValueTable DYNAMIC_VALUE = new DynamicValueTable();

}
