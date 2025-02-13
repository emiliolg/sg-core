package models.g;

import java.math.BigDecimal;
import tekgenesis.cache.CacheType;
import tekgenesis.common.util.Conversions;
import models.Customer;
import models.CustomerSearcher;
import tekgenesis.persistence.TableField.DTime;
import tekgenesis.persistence.DbTable;
import tekgenesis.persistence.TableField.Decimal;
import models.DocType;
import tekgenesis.persistence.EntityTable;
import tekgenesis.persistence.TableField.Enum;
import tekgenesis.persistence.TableField.EnumerationSet;
import tekgenesis.type.Modifier;
import org.jetbrains.annotations.NotNull;
import tekgenesis.common.core.Option;
import models.Sex;
import models.Sport;
import tekgenesis.persistence.TableField.Str;
import tekgenesis.common.core.Strings;
import tekgenesis.common.core.Tuple;
import static tekgenesis.common.collections.Colls.listOf;

/** Metadata class for table associated to entity models.Customer */
@SuppressWarnings({"DuplicateStringLiteralInspection", "MagicNumber"})
public class CustomerTable
    extends DbTable<Customer,Tuple<DocType,BigDecimal>>
{

    //~ Fields ...................................................................................................................

    @NotNull public final Enum<DocType,String> DOCUMENT_TYPE;
    @NotNull public final Decimal DOCUMENT_ID;
    @NotNull public final Str FIRST_NAME;
    @NotNull public final Str LAST_NAME;
    @NotNull public final Str NICKNAME;
    @NotNull public final Enum<Sex,String> SEX;
    @NotNull public final Str STATE_COUNTRY_CODE;
    @NotNull public final Str STATE_CODE;
    @NotNull public final EnumerationSet<Sport,String> SPORTS;
    @NotNull public final DTime UPDATE_TIME;

    //~ Constructors .............................................................................................................

    private CustomerTable() {
        super(Customer.class,"MODELS","CUST","",Modifier.NONE,CacheType.NONE);
        DOCUMENT_TYPE = enumField("documentType", "DOCUMENT_TYPE", DocType.class);
        DOCUMENT_ID = decimalField("documentId", "DOCUMENT_ID", false, 10, 0);
        FIRST_NAME = strField("firstName", "FIRST", 50);
        LAST_NAME = strField("lastName", "LAST_NAME", 50);
        NICKNAME = strField("nickname", "NICKNAME", 50);
        SEX = enumField("sex", "SEX", Sex.class);
        STATE_COUNTRY_CODE = strField("stateCountryCode", "CCODE", 2);
        STATE_CODE = strField("stateCode", "SCODE", 2);
        SPORTS = enumSetField("sports", "SPORTS", Sport.class);
        UPDATE_TIME = dTimeField("updateTime", "UPDATE_TIME");
        primaryKey(listOf(DOCUMENT_TYPE, DOCUMENT_ID));
        secondaryKeys(listOf(listOf(NICKNAME)));
    }

    //~ Methods ..................................................................................................................

    @NotNull protected Tuple<DocType,BigDecimal> strToKey(@NotNull String key) {
        final String[] parts = Strings.splitToArray(key, 2);
        return Tuple.tuple2(DocType.valueOf(parts[0]), Conversions.toDecimal(parts[1]));
    }

    @Override @NotNull protected Option<CustomerSearcher> searcher() { return Option.of(CustomerSearcher.CUSTOMER_SEARCHER); }

    @Override @NotNull public final CustomerTable as(@NotNull String alias) { return createAlias(new CustomerTable(), alias); }

    @Override @NotNull protected final EntityTable<Customer,Tuple<DocType,BigDecimal>> createEntityTable() { return new EntityTable<>(CUSTOMER); }

    //~ Fields ...................................................................................................................

    @NotNull public static final CustomerTable CUSTOMER = new CustomerTable();

}
