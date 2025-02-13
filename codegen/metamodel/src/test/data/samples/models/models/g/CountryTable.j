package models.g;

import tekgenesis.cache.CacheType;
import models.Country;
import tekgenesis.persistence.TableField.DTime;
import tekgenesis.persistence.DbTable;
import tekgenesis.persistence.EntityTable;
import java.util.EnumSet;
import org.jetbrains.annotations.NotNull;
import tekgenesis.persistence.TableField.Str;
import static tekgenesis.type.Modifier.DEPRECABLE;
import static tekgenesis.type.Modifier.REMOTABLE;
import static tekgenesis.common.collections.Colls.listOf;

/** Metadata class for table associated to entity models.Country */
@SuppressWarnings({"DuplicateStringLiteralInspection", "MagicNumber"})
public class CountryTable
    extends DbTable<Country,String>
{

    //~ Fields ...................................................................................................................

    @NotNull public final Str CODE;
    @NotNull public final Str DESCRIPTION;
    @NotNull public final DTime UPDATE_TIME;
    @NotNull public final DTime DEPRECATION_TIME;
    @NotNull public final Str DEPRECATION_USER;

    //~ Constructors .............................................................................................................

    private CountryTable() {
        super(Country.class,"MODELS","COUNTRY","",EnumSet.of(DEPRECABLE, REMOTABLE),CacheType.NONE);
        CODE = strField("code", "CODE", 2);
        DESCRIPTION = strField("description", "DESCRIPTION", 40);
        UPDATE_TIME = dTimeField("updateTime", "UPDATE_TIME");
        DEPRECATION_TIME = dTimeField("deprecationTime", "DEPRECATION_TIME");
        DEPRECATION_USER = strInternField("deprecationUser", "DEPRECATION_USER", 100);
        primaryKey(listOf(CODE));
    }

    //~ Methods ..................................................................................................................

    @NotNull protected String strToKey(@NotNull String key) { return key; }

    @Override @NotNull public final CountryTable as(@NotNull String alias) { return createAlias(new CountryTable(), alias); }

    @Override @NotNull protected final EntityTable<Country,String> createEntityTable() { return new EntityTable<>(COUNTRY); }

    //~ Fields ...................................................................................................................

    @NotNull public static final CountryTable COUNTRY = new CountryTable();

}
