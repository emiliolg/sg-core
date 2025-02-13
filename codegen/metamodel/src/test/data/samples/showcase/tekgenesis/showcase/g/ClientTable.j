package tekgenesis.showcase.g;

import tekgenesis.cache.CacheType;
import tekgenesis.showcase.Client;
import tekgenesis.common.util.Conversions;
import tekgenesis.persistence.TableField.DTime;
import tekgenesis.persistence.DbTable;
import tekgenesis.persistence.EntityTable;
import tekgenesis.persistence.TableField.Int;
import tekgenesis.type.Modifier;
import org.jetbrains.annotations.NotNull;
import tekgenesis.persistence.TableField.Str;
import static tekgenesis.common.collections.Colls.listOf;

/** Metadata class for table associated to entity tekgenesis.showcase.Client */
@SuppressWarnings({"DuplicateStringLiteralInspection", "MagicNumber"})
public class ClientTable
    extends DbTable<Client,Integer>
{

    //~ Fields ...................................................................................................................

    @NotNull public final Int ID;
    @NotNull public final Str NAME;
    @NotNull public final Int ADDRESS_ID;
    @NotNull public final DTime UPDATE_TIME;

    //~ Constructors .............................................................................................................

    private ClientTable() {
        super(Client.class,"SHOWCASE","CLIENT","CLIENT_SEQ",Modifier.NONE,CacheType.NONE);
        ID = intField("id", "ID", false, 9);
        NAME = strField("name", "NAME", 50);
        ADDRESS_ID = intField("addressId", "ADDRESS_ID", false, 9);
        UPDATE_TIME = dTimeField("updateTime", "UPDATE_TIME");
        primaryKey(listOf(ID));
    }

    //~ Methods ..................................................................................................................

    @NotNull protected Integer strToKey(@NotNull String key) { return Conversions.toInt(key); }

    @Override @NotNull public final ClientTable as(@NotNull String alias) { return createAlias(new ClientTable(), alias); }

    @Override @NotNull protected final EntityTable<Client,Integer> createEntityTable() { return new EntityTable<>(CLIENT); }

    //~ Fields ...................................................................................................................

    @NotNull public static final ClientTable CLIENT = new ClientTable();

}
