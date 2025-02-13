package tekgenesis.sales.basic.g;

import tekgenesis.index.IndexSearcher;
import org.jetbrains.annotations.NotNull;
import tekgenesis.sales.basic.ProductDefault;
import tekgenesis.sales.basic.ProductDefaultSearcher;
import tekgenesis.index.SearchableField.Str;

/** Base class for index and searching tekgenesis.sales.basic.ProductDefault */
public class ProductDefaultSearcherBase
    extends IndexSearcher
{

    //~ Fields ...................................................................................................................

    @NotNull public final Str DESCRIPTION;

    //~ Constructors .............................................................................................................

    /** Default constructor. */
    protected ProductDefaultSearcherBase() {
        super(ProductDefault.class);
        DESCRIPTION = fields().strField("description", "description");
    }

    //~ Methods ..................................................................................................................

    @Override @NotNull public final String getIndexId() { return INDEX_ID; }

    //~ Fields ...................................................................................................................

    @NotNull private static final String INDEX_ID = "a727a15367b7a4b48cf79f5d6b9117";
    @NotNull public static final ProductDefaultSearcher PRODUCT_DEFAULT_SEARCHER = new ProductDefaultSearcher();

}
