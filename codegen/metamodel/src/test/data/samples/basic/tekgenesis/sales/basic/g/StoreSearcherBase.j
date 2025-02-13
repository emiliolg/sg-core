package tekgenesis.sales.basic.g;

import tekgenesis.index.IndexSearcher;
import org.jetbrains.annotations.NotNull;
import tekgenesis.sales.basic.Store;
import tekgenesis.sales.basic.StoreSearcher;
import tekgenesis.index.SearchableField.Str;

/** Base class for index and searching tekgenesis.sales.basic.Store */
public class StoreSearcherBase
    extends IndexSearcher
{

    //~ Fields ...................................................................................................................

    @NotNull public final Str NAME;

    //~ Constructors .............................................................................................................

    /** Default constructor. */
    protected StoreSearcherBase() {
        super(Store.class);
        NAME = fields().strField("name", "name");
    }

    //~ Methods ..................................................................................................................

    @Override @NotNull public final String getIndexId() { return INDEX_ID; }

    //~ Fields ...................................................................................................................

    @NotNull private static final String INDEX_ID = "ecbcc3ff0e93c6a0f08d42bf69a904";
    @NotNull public static final StoreSearcher STORE_SEARCHER = new StoreSearcher();

}
