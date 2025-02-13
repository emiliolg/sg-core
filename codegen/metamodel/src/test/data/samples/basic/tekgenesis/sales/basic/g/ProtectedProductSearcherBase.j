package tekgenesis.sales.basic.g;

import tekgenesis.index.IndexSearcher;
import org.jetbrains.annotations.NotNull;
import tekgenesis.sales.basic.ProtectedProduct;
import tekgenesis.sales.basic.ProtectedProductSearcher;
import tekgenesis.index.SearchableField.Str;

/** Base class for index and searching tekgenesis.sales.basic.ProtectedProduct */
public class ProtectedProductSearcherBase
    extends IndexSearcher
{

    //~ Fields ...................................................................................................................

    @NotNull public final Str DESCRIPTION;

    //~ Constructors .............................................................................................................

    /** Default constructor. */
    protected ProtectedProductSearcherBase() {
        super(ProtectedProduct.class);
        DESCRIPTION = fields().strField("description", "description");
    }

    //~ Methods ..................................................................................................................

    @Override @NotNull public final String getIndexId() { return INDEX_ID; }

    //~ Fields ...................................................................................................................

    @NotNull private static final String INDEX_ID = "a0724929adebdad10f6d160dd77fd2";
    @NotNull public static final ProtectedProductSearcher PROTECTED_PRODUCT_SEARCHER = new ProtectedProductSearcher();

}
