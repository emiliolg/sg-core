package tekgenesis.sales.basic.g;

import tekgenesis.sales.basic.CustomerSearchable;
import tekgenesis.sales.basic.CustomerSearchableSearcher;
import tekgenesis.index.SearchableField.DTime;
import tekgenesis.index.SearchableField.Decimal;
import tekgenesis.index.IndexSearcher;
import org.jetbrains.annotations.NotNull;
import tekgenesis.index.SearchableField.Str;

/** Base class for index and searching tekgenesis.sales.basic.CustomerSearchable */
public class CustomerSearchableSearcherBase
    extends IndexSearcher
{

    //~ Fields ...................................................................................................................

    @NotNull public final Str WITH_OTHER_ID;
    @NotNull public final Str LAST_NAME;
    @NotNull public final DTime BIRTH_DATE;
    @NotNull public final Decimal DOCUMENT;
    @NotNull public final Str SOME_EXTRA_FIELD;

    //~ Constructors .............................................................................................................

    /** Default constructor. */
    protected CustomerSearchableSearcherBase() {
        super(CustomerSearchable.class);
        WITH_OTHER_ID = fields().strField("withOtherId", "firstName").filterOnly();
        LAST_NAME = fields().strField("lastName", "lastName").withBoost(3);
        BIRTH_DATE = fields().dateTimeField("birthDate", "birthDate");
        DOCUMENT = fields().decimalField("document", "document");
        SOME_EXTRA_FIELD = fields().strField("someExtraField", "someExtraField");
    }

    //~ Methods ..................................................................................................................

    @Override @NotNull public final String getIndexId() { return INDEX_ID; }

    //~ Fields ...................................................................................................................

    @NotNull private static final String INDEX_ID = "67f1b7e2aacf42d30f66654d9b928c";
    @NotNull public static final CustomerSearchableSearcher CUSTOMER_SEARCHABLE_SEARCHER = new CustomerSearchableSearcher();

}
