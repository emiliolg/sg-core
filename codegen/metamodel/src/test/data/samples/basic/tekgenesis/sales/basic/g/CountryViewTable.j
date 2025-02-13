package tekgenesis.sales.basic.g;

import tekgenesis.cache.CacheType;
import tekgenesis.sales.basic.CountryView;
import tekgenesis.persistence.TableField.DTime;
import tekgenesis.persistence.DbTable;
import tekgenesis.persistence.EntityTable;
import java.util.EnumSet;
import org.jetbrains.annotations.NotNull;
import tekgenesis.persistence.TableField.Str;
import static tekgenesis.type.Modifier.REMOTE;
import static tekgenesis.common.collections.Colls.listOf;

/** Metadata class for table associated to entity tekgenesis.sales.basic.CountryView */
@SuppressWarnings({"DuplicateStringLiteralInspection", "MagicNumber"})
public class CountryViewTable
    extends DbTable<CountryView,String>
{

    //~ Fields ...................................................................................................................

    @NotNull public final Str ISO;
    @NotNull public final Str NAME;
    @NotNull public final DTime UPDATE_TIME;

    //~ Constructors .............................................................................................................

    private CountryViewTable() {
        super(CountryView.class,"BASIC","COUNTRY_VIEW","",EnumSet.of(REMOTE),CacheType.NONE);
        ISO = strField("iso", "ISO", 2);
        NAME = strField("name", "NAME", 30);
        UPDATE_TIME = dTimeField("updateTime", "UPDATE_TIME");
        primaryKey(listOf(ISO));
    }

    //~ Methods ..................................................................................................................

    @NotNull protected String strToKey(@NotNull String key) { return key; }

    @Override @NotNull public final CountryViewTable as(@NotNull String alias) { return createAlias(new CountryViewTable(), alias); }

    @Override @NotNull protected final EntityTable<CountryView,String> createEntityTable() { return new EntityTable<>(COUNTRY_VIEW); }

    //~ Fields ...................................................................................................................

    @NotNull public static final CountryViewTable COUNTRY_VIEW = new CountryViewTable();

}
