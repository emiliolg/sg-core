package tekgenesis.showcase.g;

import tekgenesis.showcase.Addresses;
import tekgenesis.cache.CacheType;
import tekgenesis.common.util.Conversions;
import tekgenesis.persistence.TableField.DTime;
import tekgenesis.persistence.DbTable;
import tekgenesis.persistence.EntityTable;
import tekgenesis.persistence.TableField.Int;
import tekgenesis.type.Modifier;
import org.jetbrains.annotations.NotNull;
import static tekgenesis.common.collections.Colls.listOf;

/** Metadata class for table associated to entity tekgenesis.showcase.Addresses */
@SuppressWarnings({"DuplicateStringLiteralInspection", "MagicNumber"})
public class AddressesTable
    extends DbTable<Addresses,Integer>
{

    //~ Fields ...................................................................................................................

    @NotNull public final Int ID;
    @NotNull public final DTime UPDATE_TIME;

    //~ Constructors .............................................................................................................

    private AddressesTable() {
        super(Addresses.class,"SHOWCASE","ADDRESSES","ADDRESSES_SEQ",Modifier.NONE,CacheType.NONE);
        ID = intField("id", "ID", false, 9);
        UPDATE_TIME = dTimeField("updateTime", "UPDATE_TIME");
        primaryKey(listOf(ID));
    }

    //~ Methods ..................................................................................................................

    @NotNull protected Integer strToKey(@NotNull String key) { return Conversions.toInt(key); }

    @Override @NotNull public final AddressesTable as(@NotNull String alias) { return createAlias(new AddressesTable(), alias); }

    @Override @NotNull protected final EntityTable<Addresses,Integer> createEntityTable() { return new EntityTable<>(ADDRESSES); }

    //~ Fields ...................................................................................................................

    @NotNull public static final AddressesTable ADDRESSES = new AddressesTable();

}
