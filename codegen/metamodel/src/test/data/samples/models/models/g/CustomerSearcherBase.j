package models.g;

import models.Customer;
import models.CustomerSearcher;
import tekgenesis.index.SearchableField.Decimal;
import tekgenesis.index.IndexSearcher;
import org.jetbrains.annotations.NotNull;
import tekgenesis.index.SearchableField.Str;

/** Base class for index and searching models.Customer */
public class CustomerSearcherBase
    extends IndexSearcher
{

    //~ Fields ...................................................................................................................

    @NotNull public final Str FIRST_NAME;
    @NotNull public final Str LAST_NAME;
    @NotNull public final Decimal DOCUMENT_ID;

    //~ Constructors .............................................................................................................

    /** Default constructor. */
    protected CustomerSearcherBase() {
        super(Customer.class);
        FIRST_NAME = fields().strField("firstName", "firstName");
        LAST_NAME = fields().strField("lastName", "lastName");
        DOCUMENT_ID = fields().decimalField("documentId", "documentId");
    }

    //~ Methods ..................................................................................................................

    @Override @NotNull public final String getIndexId() { return INDEX_ID; }

    //~ Fields ...................................................................................................................

    @NotNull private static final String INDEX_ID = "cddf4b91a42e6f1b21b2f3db178467";
    @NotNull public static final CustomerSearcher CUSTOMER_SEARCHER = new CustomerSearcher();

}
