package tekgenesis.sales.basic.g;

import tekgenesis.cache.CacheType;
import tekgenesis.persistence.TableField.DTime;
import tekgenesis.persistence.DbTable;
import tekgenesis.persistence.EntityTable;
import java.util.EnumSet;
import org.jetbrains.annotations.NotNull;
import tekgenesis.sales.basic.StateProvince;
import tekgenesis.persistence.TableField.Str;
import tekgenesis.common.core.Strings;
import tekgenesis.common.core.Tuple;
import static tekgenesis.type.Modifier.REMOTABLE;
import static tekgenesis.common.collections.Colls.listOf;

/** Metadata class for table associated to entity tekgenesis.sales.basic.StateProvince */
@SuppressWarnings({"DuplicateStringLiteralInspection", "MagicNumber"})
public class StateProvinceTable
    extends DbTable<StateProvince,Tuple<String,String>>
{

    //~ Fields ...................................................................................................................

    @NotNull public final Str COUNTRY_ISO2;
    @NotNull public final Str CODE;
    @NotNull public final Str NAME;
    @NotNull public final DTime UPDATE_TIME;

    //~ Constructors .............................................................................................................

    private StateProvinceTable() {
        super(StateProvince.class,"BASIC","STATE_PROVINCE","",EnumSet.of(REMOTABLE),CacheType.NONE);
        COUNTRY_ISO2 = strField("countryIso2", "COUNTRY_ISO2", 2);
        CODE = strField("code", "CODE", 2);
        NAME = strField("name", "NAME", 30);
        UPDATE_TIME = dTimeField("updateTime", "UPDATE_TIME");
        primaryKey(listOf(COUNTRY_ISO2, CODE));
    }

    //~ Methods ..................................................................................................................

    @NotNull protected Tuple<String,String> strToKey(@NotNull String key) {
        final String[] parts = Strings.splitToArray(key, 2);
        return Tuple.tuple2(parts[0], parts[1]);
    }

    @Override @NotNull public final StateProvinceTable as(@NotNull String alias) { return createAlias(new StateProvinceTable(), alias); }

    @Override @NotNull protected final EntityTable<StateProvince,Tuple<String,String>> createEntityTable() { return new EntityTable<>(STATE_PROVINCE); }

    //~ Fields ...................................................................................................................

    @NotNull public static final StateProvinceTable STATE_PROVINCE = new StateProvinceTable();

}
