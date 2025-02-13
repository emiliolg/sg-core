package tekgenesis.showcase.g;

import tekgenesis.showcase.Address;
import tekgenesis.cache.CacheType;
import tekgenesis.common.util.Conversions;
import tekgenesis.persistence.TableField.DTime;
import tekgenesis.persistence.DbTable;
import tekgenesis.persistence.EntityTable;
import tekgenesis.persistence.TableField.Int;
import tekgenesis.type.Modifier;
import org.jetbrains.annotations.NotNull;
import tekgenesis.persistence.TableField.Str;
import static tekgenesis.common.collections.Colls.listOf;

/** Metadata class for table associated to entity tekgenesis.showcase.Address */
@SuppressWarnings({"DuplicateStringLiteralInspection", "MagicNumber"})
public class AddressTable
    extends DbTable<Address,Integer>
{

    //~ Fields ...................................................................................................................

    @NotNull public final Int ID;
    @NotNull public final Str STREET;
    @NotNull public final Str CITY;
    @NotNull public final Str STATE;
    @NotNull public final Str ZIP;
    @NotNull public final Str COUNTRY;
    @NotNull public final DTime UPDATE_TIME;

    //~ Constructors .............................................................................................................

    private AddressTable() {
        super(Address.class,"SHOWCASE","ADDRESS","ADDRESS_SEQ",Modifier.NONE,CacheType.NONE);
        ID = intField("id", "ID", false, 9);
        STREET = strField("street", "STREET", 60);
        CITY = strField("city", "CITY", 40);
        STATE = strField("state", "STATE", 40);
        ZIP = strField("zip", "ZIP", 10);
        COUNTRY = strField("country", "COUNTRY", 30);
        UPDATE_TIME = dTimeField("updateTime", "UPDATE_TIME");
        primaryKey(listOf(ID));
    }

    //~ Methods ..................................................................................................................

    @NotNull protected Integer strToKey(@NotNull String key) { return Conversions.toInt(key); }

    @Override @NotNull public final AddressTable as(@NotNull String alias) { return createAlias(new AddressTable(), alias); }

    @Override @NotNull protected final EntityTable<Address,Integer> createEntityTable() { return new EntityTable<>(ADDRESS); }

    //~ Fields ...................................................................................................................

    @NotNull public static final AddressTable ADDRESS = new AddressTable();

}
