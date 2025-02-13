package tekgenesis.sales.basic.g;

import tekgenesis.sales.basic.DatabaseCustomerSearchable;
import tekgenesis.sales.basic.DatabaseCustomerSearchableSearcher;
import tekgenesis.index.DatabaseSearcher;
import org.jetbrains.annotations.NotNull;

/** Base class for index and searching tekgenesis.sales.basic.DatabaseCustomerSearchable */
public class DatabaseCustomerSearchableSearcherBase
    extends DatabaseSearcher
{

    //~ Constructors .............................................................................................................

    /** Default constructor. */
    protected DatabaseCustomerSearchableSearcherBase() {
        super(DatabaseCustomerSearchable.class);
        fields().strField("withOtherId", "firstName").filterOnly();
        fields().strField("lastName", "lastName").withBoost(3);
        fields().decimalField("document", "document");
    }

    //~ Fields ...................................................................................................................

    @NotNull public static final DatabaseCustomerSearchableSearcher DATABASE_CUSTOMER_SEARCHABLE_SEARCHER = new DatabaseCustomerSearchableSearcher();

}
