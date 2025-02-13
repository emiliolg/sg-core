package tekgenesis.sales.basic.g;

import tekgenesis.cache.CacheType;
import tekgenesis.common.util.Conversions;
import tekgenesis.sales.basic.CustomerSearchable;
import tekgenesis.sales.basic.CustomerSearchableSearcher;
import tekgenesis.persistence.TableField.DTime;
import tekgenesis.persistence.DbTable;
import tekgenesis.persistence.TableField.Decimal;
import tekgenesis.persistence.EntityTable;
import tekgenesis.persistence.TableField.Enum;
import tekgenesis.persistence.TableField.Int;
import tekgenesis.type.Modifier;
import org.jetbrains.annotations.NotNull;
import tekgenesis.common.core.Option;
import tekgenesis.sales.basic.Sex;
import tekgenesis.persistence.TableField.Str;
import static tekgenesis.common.collections.Colls.listOf;

/** Metadata class for table associated to entity tekgenesis.sales.basic.CustomerSearchable */
@SuppressWarnings({"DuplicateStringLiteralInspection", "MagicNumber"})
public class CustomerSearchableTable
    extends DbTable<CustomerSearchable,Integer>
{

    //~ Fields ...................................................................................................................

    @NotNull public final Int ID;
    @NotNull public final Str FIRST_NAME;
    @NotNull public final Str LAST_NAME;
    @NotNull public final Decimal DOCUMENT;
    @NotNull public final DTime BIRTH_DATE;
    @NotNull public final Enum<Sex,String> SEX;
    @NotNull public final DTime UPDATE_TIME;

    //~ Constructors .............................................................................................................

    private CustomerSearchableTable() {
        super(CustomerSearchable.class,"BASIC","CUSTOMER_SEARCHABLE","CUSTOMER_SEARCHABLE_SEQ",Modifier.NONE,CacheType.NONE);
        ID = intField("id", "ID", false, 9);
        FIRST_NAME = strField("firstName", "FIRST_NAME", 255);
        LAST_NAME = strField("lastName", "LAST_NAME", 255);
        DOCUMENT = decimalField("document", "DOCUMENT", false, 10, 0);
        BIRTH_DATE = dTimeField("birthDate", "BIRTH_DATE");
        SEX = enumField("sex", "SEX", Sex.class);
        UPDATE_TIME = dTimeField("updateTime", "UPDATE_TIME");
        primaryKey(listOf(ID));
    }

    //~ Methods ..................................................................................................................

    @NotNull protected Integer strToKey(@NotNull String key) { return Conversions.toInt(key); }

    @Override @NotNull protected Option<CustomerSearchableSearcher> searcher() {
        return Option.of(CustomerSearchableSearcher.CUSTOMER_SEARCHABLE_SEARCHER);
    }

    @Override @NotNull public final CustomerSearchableTable as(@NotNull String alias) { return createAlias(new CustomerSearchableTable(), alias); }

    @Override @NotNull protected final EntityTable<CustomerSearchable,Integer> createEntityTable() { return new EntityTable<>(CUSTOMER_SEARCHABLE); }

    //~ Fields ...................................................................................................................

    @NotNull public static final CustomerSearchableTable CUSTOMER_SEARCHABLE = new CustomerSearchableTable();

}
