package tekgenesis.showcase.g;

import tekgenesis.cache.CacheType;
import tekgenesis.common.util.Conversions;
import tekgenesis.persistence.TableField.DTime;
import tekgenesis.persistence.DbTable;
import tekgenesis.persistence.EntityTable;
import tekgenesis.persistence.TableField.Enum;
import tekgenesis.persistence.InnerEntityTable;
import tekgenesis.persistence.TableField.Int;
import tekgenesis.type.Modifier;
import org.jetbrains.annotations.NotNull;
import tekgenesis.showcase.PropertyType;
import tekgenesis.persistence.TableField.Str;
import tekgenesis.common.core.Strings;
import tekgenesis.common.core.Tuple;
import tekgenesis.common.core.Tuple3;
import tekgenesis.showcase.ValidValue;
import static tekgenesis.common.collections.Colls.listOf;

/** Metadata class for table associated to entity tekgenesis.showcase.ValidValue */
@SuppressWarnings({"DuplicateStringLiteralInspection", "MagicNumber"})
public class ValidValueTable
    extends DbTable<ValidValue,Tuple3<String,PropertyType,Integer>>
{

    //~ Fields ...................................................................................................................

    @NotNull public final Str PROPERTY_NAME;
    @NotNull public final Enum<PropertyType,String> PROPERTY_TYPE;
    @NotNull public final Int SEQ_ID;
    @NotNull public final Str VALUE;
    @NotNull public final DTime UPDATE_TIME;

    //~ Constructors .............................................................................................................

    private ValidValueTable() {
        super(ValidValue.class,"SHOWCASE","VALID_VALUE","",Modifier.NONE,CacheType.NONE);
        PROPERTY_NAME = strField("propertyName", "PROPERTY_NAME", 20);
        PROPERTY_TYPE = enumField("propertyType", "PROPERTY_TYPE", PropertyType.class);
        SEQ_ID = intField("seqId", "SEQ_ID", false, 9);
        VALUE = strField("value", "VALUE", 100);
        UPDATE_TIME = dTimeField("updateTime", "UPDATE_TIME");
        primaryKey(listOf(PROPERTY_NAME, PROPERTY_TYPE, SEQ_ID));
    }

    //~ Methods ..................................................................................................................

    @NotNull protected Tuple3<String,PropertyType,Integer> strToKey(@NotNull String key) {
        final String[] parts = Strings.splitToArray(key, 3);
        return Tuple.tuple(parts[0], PropertyType.valueOf(parts[1]), Conversions.toInt(parts[2]));
    }

    @Override @NotNull public final ValidValueTable as(@NotNull String alias) { return createAlias(new ValidValueTable(), alias); }

    @Override @NotNull protected final EntityTable<ValidValue,Tuple3<String,PropertyType,Integer>> createEntityTable() { return new InnerEntityTable<>(VALID_VALUE); }

    //~ Fields ...................................................................................................................

    @NotNull public static final ValidValueTable VALID_VALUE = new ValidValueTable();

}
