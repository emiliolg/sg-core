package tekgenesis.sales.basic.g;

import tekgenesis.cache.CacheType;
import tekgenesis.common.util.Conversions;
import tekgenesis.persistence.TableField.DTime;
import tekgenesis.persistence.DbTable;
import tekgenesis.persistence.EntityTable;
import tekgenesis.persistence.TableField.Enum;
import tekgenesis.persistence.TableField.Int;
import tekgenesis.type.Modifier;
import org.jetbrains.annotations.NotNull;
import tekgenesis.common.core.Option;
import tekgenesis.sales.basic.PaymentOption;
import tekgenesis.sales.basic.PaymentType;
import tekgenesis.sales.basic.PaymentTypeSearcher;
import tekgenesis.persistence.TableField.Str;
import static tekgenesis.common.collections.Colls.listOf;

/** Metadata class for table associated to entity tekgenesis.sales.basic.PaymentType */
@SuppressWarnings({"DuplicateStringLiteralInspection", "MagicNumber"})
public class PaymentTypeTable
    extends DbTable<PaymentType,Integer>
{

    //~ Fields ...................................................................................................................

    @NotNull public final Int ID;
    @NotNull public final Enum<PaymentOption,String> TYPE;
    @NotNull public final Str SUBTYPE;
    @NotNull public final Str DESCRIPTION;
    @NotNull public final DTime UPDATE_TIME;

    //~ Constructors .............................................................................................................

    private PaymentTypeTable() {
        super(PaymentType.class,"BASIC","PAYMENT_TYPE","PAYMENT_TYPE_SEQ",Modifier.NONE,CacheType.NONE);
        ID = intField("id", "ID", false, 9);
        TYPE = enumField("type", "TYPE", PaymentOption.class);
        SUBTYPE = strField("subtype", "SUBTYPE", 30);
        DESCRIPTION = strField("description", "DESCRIPTION", 160);
        UPDATE_TIME = dTimeField("updateTime", "UPDATE_TIME");
        primaryKey(listOf(ID));
    }

    //~ Methods ..................................................................................................................

    @NotNull protected Integer strToKey(@NotNull String key) { return Conversions.toInt(key); }

    @Override @NotNull protected Option<PaymentTypeSearcher> searcher() { return Option.of(PaymentTypeSearcher.PAYMENT_TYPE_SEARCHER); }

    @Override @NotNull public final PaymentTypeTable as(@NotNull String alias) { return createAlias(new PaymentTypeTable(), alias); }

    @Override @NotNull protected final EntityTable<PaymentType,Integer> createEntityTable() { return new EntityTable<>(PAYMENT_TYPE); }

    //~ Fields ...................................................................................................................

    @NotNull public static final PaymentTypeTable PAYMENT_TYPE = new PaymentTypeTable();

}
