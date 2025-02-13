package tekgenesis.sales.basic.g;

import tekgenesis.cache.CacheType;
import tekgenesis.common.util.Conversions;
import tekgenesis.persistence.TableField.DTime;
import tekgenesis.persistence.TableField.Date;
import tekgenesis.persistence.DbTable;
import tekgenesis.persistence.TableField.Decimal;
import tekgenesis.sales.basic.DocType;
import tekgenesis.persistence.EntityTable;
import tekgenesis.persistence.TableField.Enum;
import tekgenesis.persistence.TableField.Int;
import tekgenesis.sales.basic.Invoice;
import tekgenesis.type.Modifier;
import org.jetbrains.annotations.NotNull;
import tekgenesis.sales.basic.Sex;
import static tekgenesis.common.collections.Colls.listOf;

/** Metadata class for table associated to entity tekgenesis.sales.basic.Invoice */
@SuppressWarnings({"DuplicateStringLiteralInspection", "MagicNumber"})
public class InvoiceTable
    extends DbTable<Invoice,Integer>
{

    //~ Fields ...................................................................................................................

    @NotNull public final Int ID_KEY;
    @NotNull public final Date INVOICE_DATE;
    @NotNull public final Enum<DocType,Integer> CUSTOMER_DOCUMENT_TYPE;
    @NotNull public final Decimal CUSTOMER_DOCUMENT_ID;
    @NotNull public final Enum<Sex,String> CUSTOMER_SEX;
    @NotNull public final DTime UPDATE_TIME;

    //~ Constructors .............................................................................................................

    private InvoiceTable() {
        super(Invoice.class,"BASIC","INVOICE","",Modifier.NONE,CacheType.NONE);
        ID_KEY = intField("idKey", "ID_KEY", false, 9);
        INVOICE_DATE = dateField("invoiceDate", "INVOICE_DATE");
        CUSTOMER_DOCUMENT_TYPE = enumField("customerDocumentType", "CUSTOMER_DOCUMENT_TYPE", DocType.class);
        CUSTOMER_DOCUMENT_ID = decimalField("customerDocumentId", "CUSTOMER_DOCUMENT_ID", false, 10, 0);
        CUSTOMER_SEX = enumField("customerSex", "CUSTOMER_SEX", Sex.class);
        UPDATE_TIME = dTimeField("updateTime", "UPDATE_TIME");
        primaryKey(listOf(ID_KEY));
    }

    //~ Methods ..................................................................................................................

    @NotNull protected Integer strToKey(@NotNull String key) { return Conversions.toInt(key); }

    @Override @NotNull public final InvoiceTable as(@NotNull String alias) { return createAlias(new InvoiceTable(), alias); }

    @Override @NotNull protected final EntityTable<Invoice,Integer> createEntityTable() { return new EntityTable<>(INVOICE); }

    //~ Fields ...................................................................................................................

    @NotNull public static final InvoiceTable INVOICE = new InvoiceTable();

}
