package tekgenesis.sales.basic.g;

import tekgenesis.cache.CacheType;
import tekgenesis.common.util.Conversions;
import tekgenesis.persistence.TableField.DTime;
import tekgenesis.sales.basic.DatabaseCustomerSearchable;
import tekgenesis.sales.basic.DatabaseCustomerSearchableSearcher;
import tekgenesis.persistence.DbTable;
import tekgenesis.persistence.TableField.Decimal;
import tekgenesis.persistence.EntityTable;
import tekgenesis.persistence.TableField.Enum;
import java.util.EnumSet;
import tekgenesis.persistence.TableField.Int;
import org.jetbrains.annotations.NotNull;
import tekgenesis.common.core.Option;
import tekgenesis.sales.basic.Sex;
import tekgenesis.persistence.TableField.Str;
import static tekgenesis.type.Modifier.DATABASE_SEARCHABLE;
import static tekgenesis.common.collections.Colls.listOf;

/** Metadata class for table associated to entity tekgenesis.sales.basic.DatabaseCustomerSearchable */
@SuppressWarnings({"DuplicateStringLiteralInspection", "MagicNumber"})
public class DatabaseCustomerSearchableTable
    extends DbTable<DatabaseCustomerSearchable,Integer>
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

    private DatabaseCustomerSearchableTable() {
        super(DatabaseCustomerSearchable.class,"BASIC","DATABASE_CUSTOMER_SEARCHABLE","DATABASE_CUSTOMER_S_C5E1B6_SEQ",EnumSet.of(DATABASE_SEARCHABLE),CacheType.NONE);
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

    @Override @NotNull protected Option<DatabaseCustomerSearchableSearcher> searcher() {
        return Option.of(DatabaseCustomerSearchableSearcher.DATABASE_CUSTOMER_SEARCHABLE_SEARCHER);
    }

    @Override @NotNull public final DatabaseCustomerSearchableTable as(@NotNull String alias) {
        return createAlias(new DatabaseCustomerSearchableTable(), alias);
    }

    @Override @NotNull protected final EntityTable<DatabaseCustomerSearchable,Integer> createEntityTable() { return new EntityTable<>(DATABASE_CUSTOMER_SEARCHABLE); }

    //~ Fields ...................................................................................................................

    @NotNull public static final DatabaseCustomerSearchableTable DATABASE_CUSTOMER_SEARCHABLE = new DatabaseCustomerSearchableTable();

}
