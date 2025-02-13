package tekgenesis.sales.basic.g;

import tekgenesis.cache.CacheType;
import tekgenesis.common.util.Conversions;
import tekgenesis.persistence.TableField.DTime;
import tekgenesis.persistence.DbTable;
import tekgenesis.persistence.TableField.Decimal;
import tekgenesis.persistence.EntityTable;
import tekgenesis.persistence.InnerEntityTable;
import tekgenesis.persistence.TableField.Int;
import tekgenesis.type.Modifier;
import org.jetbrains.annotations.NotNull;
import tekgenesis.sales.basic.Payment;
import tekgenesis.common.core.Strings;
import tekgenesis.common.core.Tuple;
import static tekgenesis.common.collections.Colls.listOf;

/** Metadata class for table associated to entity tekgenesis.sales.basic.Payment */
@SuppressWarnings({"DuplicateStringLiteralInspection", "MagicNumber"})
public class PaymentTable
    extends DbTable<Payment,Tuple<Integer,Integer>>
{

    //~ Fields ...................................................................................................................

    @NotNull public final Int INVOICE_ID_KEY;
    @NotNull public final Int SEQ_ID;
    @NotNull public final Int PAYMENT_ID;
    @NotNull public final Decimal AMOUNT;
    @NotNull public final DTime UPDATE_TIME;

    //~ Constructors .............................................................................................................

    private PaymentTable() {
        super(Payment.class,"BASIC","PAYMENT","",Modifier.NONE,CacheType.NONE);
        INVOICE_ID_KEY = intField("invoiceIdKey", "INVOICE_ID_KEY", false, 9);
        SEQ_ID = intField("seqId", "SEQ_ID", false, 9);
        PAYMENT_ID = intField("paymentId", "PAYMENT_ID", false, 9);
        AMOUNT = decimalField("amount", "AMOUNT", false, 10, 2);
        UPDATE_TIME = dTimeField("updateTime", "UPDATE_TIME");
        primaryKey(listOf(INVOICE_ID_KEY, SEQ_ID));
    }

    //~ Methods ..................................................................................................................

    @NotNull protected Tuple<Integer,Integer> strToKey(@NotNull String key) {
        final String[] parts = Strings.splitToArray(key, 2);
        return Tuple.tuple2(Conversions.toInt(parts[0]), Conversions.toInt(parts[1]));
    }

    @Override @NotNull public final PaymentTable as(@NotNull String alias) { return createAlias(new PaymentTable(), alias); }

    @Override @NotNull protected final EntityTable<Payment,Tuple<Integer,Integer>> createEntityTable() { return new InnerEntityTable<>(PAYMENT); }

    //~ Fields ...................................................................................................................

    @NotNull public static final PaymentTable PAYMENT = new PaymentTable();

}
