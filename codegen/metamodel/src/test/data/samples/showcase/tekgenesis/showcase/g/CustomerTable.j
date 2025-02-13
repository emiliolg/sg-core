package tekgenesis.showcase.g;

import tekgenesis.cache.CacheType;
import tekgenesis.common.util.Conversions;
import tekgenesis.showcase.Customer;
import tekgenesis.persistence.TableField.DTime;
import tekgenesis.persistence.DbTable;
import tekgenesis.persistence.EntityTable;
import tekgenesis.persistence.TableField.Int;
import tekgenesis.type.Modifier;
import org.jetbrains.annotations.NotNull;
import tekgenesis.persistence.TableField.Str;
import static tekgenesis.common.collections.Colls.listOf;

/** Metadata class for table associated to entity tekgenesis.showcase.Customer */
@SuppressWarnings({"DuplicateStringLiteralInspection", "MagicNumber"})
public class CustomerTable
    extends DbTable<Customer,Integer>
{

    //~ Fields ...................................................................................................................

    @NotNull public final Int DOCUMENT;
    @NotNull public final Str FIRST_NAME;
    @NotNull public final Str LAST_NAME;
    @NotNull public final Int HOME_ADDRESS_ID;
    @NotNull public final Int WORK_ADDRESS_ID;
    @NotNull public final DTime UPDATE_TIME;

    //~ Constructors .............................................................................................................

    private CustomerTable() {
        super(Customer.class,"SHOWCASE","CUSTOMER","",Modifier.NONE,CacheType.NONE);
        DOCUMENT = intField("document", "DOCUMENT", false, 8);
        FIRST_NAME = strField("firstName", "FIRST_NAME", 255);
        LAST_NAME = strField("lastName", "LAST_NAME", 255);
        HOME_ADDRESS_ID = intField("homeAddressId", "HOME_ADDRESS_ID", false, 9);
        WORK_ADDRESS_ID = intField("workAddressId", "WORK_ADDRESS_ID", false, 9);
        UPDATE_TIME = dTimeField("updateTime", "UPDATE_TIME");
        primaryKey(listOf(DOCUMENT));
    }

    //~ Methods ..................................................................................................................

    @NotNull protected Integer strToKey(@NotNull String key) { return Conversions.toInt(key); }

    @Override @NotNull public final CustomerTable as(@NotNull String alias) { return createAlias(new CustomerTable(), alias); }

    @Override @NotNull protected final EntityTable<Customer,Integer> createEntityTable() { return new EntityTable<>(CUSTOMER); }

    //~ Fields ...................................................................................................................

    @NotNull public static final CustomerTable CUSTOMER = new CustomerTable();

}
