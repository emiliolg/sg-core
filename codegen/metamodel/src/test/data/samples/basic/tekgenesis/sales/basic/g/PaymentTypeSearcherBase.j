package tekgenesis.sales.basic.g;

import tekgenesis.index.SearchableField.Enum;
import tekgenesis.index.IndexSearcher;
import org.jetbrains.annotations.NotNull;
import tekgenesis.sales.basic.PaymentOption;
import tekgenesis.sales.basic.PaymentType;
import tekgenesis.sales.basic.PaymentTypeSearcher;
import tekgenesis.index.SearchableField.Str;

/** Base class for index and searching tekgenesis.sales.basic.PaymentType */
public class PaymentTypeSearcherBase
    extends IndexSearcher
{

    //~ Fields ...................................................................................................................

    @NotNull public final Enum<PaymentOption> TYPE;
    @NotNull public final Str DESCRIPTION;
    @NotNull public final Str SUBTYPE;

    //~ Constructors .............................................................................................................

    /** Default constructor. */
    protected PaymentTypeSearcherBase() {
        super(PaymentType.class);
        TYPE = fields().<PaymentOption>enumField("type", "type", PaymentOption.class);
        DESCRIPTION = fields().strField("description", "description");
        SUBTYPE = fields().strField("subtype", "subtype");
    }

    //~ Methods ..................................................................................................................

    @Override @NotNull public final String getIndexId() { return INDEX_ID; }

    //~ Fields ...................................................................................................................

    @NotNull private static final String INDEX_ID = "4f4ea809bbcbbd651e84cb0a9f9439";
    @NotNull public static final PaymentTypeSearcher PAYMENT_TYPE_SEARCHER = new PaymentTypeSearcher();

}
