package tekgenesis.sales.basic.g;

import tekgenesis.cache.CacheType;
import tekgenesis.sales.basic.CityView;
import tekgenesis.common.util.Conversions;
import tekgenesis.persistence.TableField.DTime;
import tekgenesis.persistence.DbTable;
import tekgenesis.persistence.EntityTable;
import java.util.EnumSet;
import tekgenesis.persistence.TableField.Int;
import org.jetbrains.annotations.NotNull;
import tekgenesis.persistence.TableField.Str;
import static tekgenesis.type.Modifier.REMOTE;
import static tekgenesis.common.collections.Colls.listOf;

/** Metadata class for table associated to entity tekgenesis.sales.basic.CityView */
@SuppressWarnings({"DuplicateStringLiteralInspection", "MagicNumber"})
public class CityViewTable
    extends DbTable<CityView,Integer>
{

    //~ Fields ...................................................................................................................

    @NotNull public final Int ID;
    @NotNull public final Str NAME;
    @NotNull public final Str STATE_PROVINCE_COUNTRY_ISO;
    @NotNull public final Str STATE_PROVINCE_CODE;
    @NotNull public final DTime UPDATE_TIME;

    //~ Constructors .............................................................................................................

    private CityViewTable() {
        super(CityView.class,"BASIC","CITY_VIEW","CITY_SEQ",EnumSet.of(REMOTE),CacheType.NONE);
        ID = intField("id", "ID", false, 9);
        NAME = strField("name", "NAME", 30);
        STATE_PROVINCE_COUNTRY_ISO = strField("stateProvinceCountryIso", "STATE_PROVINCE_COUNTRY_ISO", 2);
        STATE_PROVINCE_CODE = strField("stateProvinceCode", "STATE_PROVINCE_CODE", 2);
        UPDATE_TIME = dTimeField("updateTime", "UPDATE_TIME");
        primaryKey(listOf(ID));
    }

    //~ Methods ..................................................................................................................

    @NotNull protected Integer strToKey(@NotNull String key) { return Conversions.toInt(key); }

    @Override @NotNull public final CityViewTable as(@NotNull String alias) { return createAlias(new CityViewTable(), alias); }

    @Override @NotNull protected final EntityTable<CityView,Integer> createEntityTable() { return new EntityTable<>(CITY_VIEW); }

    //~ Fields ...................................................................................................................

    @NotNull public static final CityViewTable CITY_VIEW = new CityViewTable();

}
