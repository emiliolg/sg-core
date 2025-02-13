package tekgenesis.sales.basic.g;

import tekgenesis.cache.CacheType;
import tekgenesis.sales.basic.Country;
import tekgenesis.persistence.TableField.DTime;
import tekgenesis.persistence.DbTable;
import tekgenesis.persistence.EntityTable;
import java.util.EnumSet;
import org.jetbrains.annotations.NotNull;
import tekgenesis.persistence.TableField.Str;
import static tekgenesis.type.Modifier.REMOTABLE;
import static tekgenesis.common.collections.Colls.listOf;

/** Metadata class for table associated to entity tekgenesis.sales.basic.Country */
@SuppressWarnings({"DuplicateStringLiteralInspection", "MagicNumber"})
public class CountryTable
    extends DbTable<Country,String>
{

    //~ Fields ...................................................................................................................

    @NotNull public final Str NAME;
    @NotNull public final Str ISO2;
    @NotNull public final DTime UPDATE_TIME;

    //~ Constructors .............................................................................................................

    private CountryTable() {
        super(Country.class,"BASIC","COUNTRY","",EnumSet.of(REMOTABLE),CacheType.NONE);
        NAME = strField("name", "NAME", 30);
        ISO2 = strField("iso2", "ISO2", 2);
        UPDATE_TIME = dTimeField("updateTime", "UPDATE_TIME");
        primaryKey(listOf(ISO2));
    }

    //~ Methods ..................................................................................................................

    @NotNull protected String strToKey(@NotNull String key) { return key; }

    @Override @NotNull public final CountryTable as(@NotNull String alias) { return createAlias(new CountryTable(), alias); }

    @Override @NotNull protected final EntityTable<Country,String> createEntityTable() { return new EntityTable<>(COUNTRY); }

    //~ Fields ...................................................................................................................

    @NotNull public static final CountryTable COUNTRY = new CountryTable();

}
