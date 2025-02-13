package tekgenesis.sales.basic.g;

import java.math.BigDecimal;
import tekgenesis.cache.CacheType;
import tekgenesis.common.util.Conversions;
import tekgenesis.sales.basic.Customer;
import tekgenesis.sales.basic.CustomerSearcher;
import tekgenesis.persistence.TableField.DTime;
import tekgenesis.persistence.DbTable;
import tekgenesis.persistence.TableField.Decimal;
import tekgenesis.sales.basic.DocType;
import tekgenesis.persistence.EntityTable;
import tekgenesis.persistence.TableField.Enum;
import tekgenesis.type.Modifier;
import org.jetbrains.annotations.NotNull;
import tekgenesis.common.core.Option;
import tekgenesis.persistence.TableField.Res;
import tekgenesis.sales.basic.Sex;
import tekgenesis.persistence.TableField.Str;
import tekgenesis.common.core.Strings;
import tekgenesis.common.core.Tuple;
import tekgenesis.common.core.Tuple3;
import static tekgenesis.common.collections.Colls.listOf;

/** Metadata class for table associated to entity tekgenesis.sales.basic.Customer */
@SuppressWarnings({"DuplicateStringLiteralInspection", "MagicNumber"})
public class CustomerTable
    extends DbTable<Customer,Tuple3<DocType,BigDecimal,Sex>>
{

    //~ Fields ...................................................................................................................

    @NotNull public final Enum<DocType,Integer> DOCUMENT_TYPE;
    @NotNull public final Decimal DOCUMENT_ID;
    @NotNull public final Str FIRST_NAME;
    @NotNull public final Str LAST_NAME;
    @NotNull public final Str NICKNAME;
    @NotNull public final Enum<Sex,String> SEX;
    @NotNull public final Res PHOTO;
    @NotNull public final DTime UPDATE_TIME;

    //~ Constructors .............................................................................................................

    private CustomerTable() {
        super(Customer.class,"BASIC","CUSTOMER","",Modifier.NONE,CacheType.NONE);
        DOCUMENT_TYPE = enumField("documentType", "DOCUMENT_TYPE", DocType.class);
        DOCUMENT_ID = decimalField("documentId", "DOCUMENT_ID", false, 10, 0);
        FIRST_NAME = strField("firstName", "FIRST_NAME", 50);
        LAST_NAME = strField("lastName", "LAST_NAME", 50);
        NICKNAME = strField("nickname", "NICKNAME", 50);
        SEX = enumField("sex", "SEX", Sex.class);
        PHOTO = resourceField("photo", "PHOTO");
        UPDATE_TIME = dTimeField("updateTime", "UPDATE_TIME");
        primaryKey(listOf(DOCUMENT_TYPE, DOCUMENT_ID, SEX));
        secondaryKeys(listOf(listOf(NICKNAME)));
    }

    //~ Methods ..................................................................................................................

    @NotNull protected Tuple3<DocType,BigDecimal,Sex> strToKey(@NotNull String key) {
        final String[] parts = Strings.splitToArray(key, 3);
        return Tuple.tuple(DocType.valueOf(parts[0]), Conversions.toDecimal(parts[1]), Sex.valueOf(parts[2]));
    }

    @Override @NotNull protected Option<CustomerSearcher> searcher() { return Option.of(CustomerSearcher.CUSTOMER_SEARCHER); }

    @Override @NotNull public final CustomerTable as(@NotNull String alias) { return createAlias(new CustomerTable(), alias); }

    @Override @NotNull protected final EntityTable<Customer,Tuple3<DocType,BigDecimal,Sex>> createEntityTable() { return new EntityTable<>(CUSTOMER); }

    //~ Fields ...................................................................................................................

    @NotNull public static final CustomerTable CUSTOMER = new CustomerTable();

}
