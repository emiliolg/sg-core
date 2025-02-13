package tekgenesis.sales.basic.g;

import tekgenesis.cache.CacheType;
import tekgenesis.persistence.TableField.DTime;
import tekgenesis.persistence.DbTable;
import tekgenesis.persistence.EntityTable;
import java.util.EnumSet;
import org.jetbrains.annotations.NotNull;
import tekgenesis.sales.basic.StateProvinceView;
import tekgenesis.persistence.TableField.Str;
import tekgenesis.common.core.Strings;
import tekgenesis.common.core.Tuple;
import static tekgenesis.type.Modifier.REMOTE;
import static tekgenesis.common.collections.Colls.listOf;

/** Metadata class for table associated to entity tekgenesis.sales.basic.StateProvinceView */
@SuppressWarnings({"DuplicateStringLiteralInspection", "MagicNumber"})
public class StateProvinceViewTable
    extends DbTable<StateProvinceView,Tuple<String,String>>
{

    //~ Fields ...................................................................................................................

    @NotNull public final Str COUNTRY_ISO;
    @NotNull public final Str CODE;
    @NotNull public final Str NAME;
    @NotNull public final DTime UPDATE_TIME;

    //~ Constructors .............................................................................................................

    private StateProvinceViewTable() {
        super(StateProvinceView.class,"BASIC","STATE_PROVINCE_VIEW","",EnumSet.of(REMOTE),CacheType.NONE);
        COUNTRY_ISO = strField("countryIso", "COUNTRY_ISO", 2);
        CODE = strField("code", "CODE", 2);
        NAME = strField("name", "NAME", 30);
        UPDATE_TIME = dTimeField("updateTime", "UPDATE_TIME");
        primaryKey(listOf(COUNTRY_ISO, CODE));
    }

    //~ Methods ..................................................................................................................

    @NotNull protected Tuple<String,String> strToKey(@NotNull String key) {
        final String[] parts = Strings.splitToArray(key, 2);
        return Tuple.tuple2(parts[0], parts[1]);
    }

    @Override @NotNull public final StateProvinceViewTable as(@NotNull String alias) { return createAlias(new StateProvinceViewTable(), alias); }

    @Override @NotNull protected final EntityTable<StateProvinceView,Tuple<String,String>> createEntityTable() { return new EntityTable<>(STATE_PROVINCE_VIEW); }

    //~ Fields ...................................................................................................................

    @NotNull public static final StateProvinceViewTable STATE_PROVINCE_VIEW = new StateProvinceViewTable();

}
