package tekgenesis.sales.basic.g;

import tekgenesis.index.IndexSearcher;
import org.jetbrains.annotations.NotNull;
import tekgenesis.sales.basic.Product;
import tekgenesis.sales.basic.ProductSearcher;
import tekgenesis.index.SearchableField.Str;

/** Base class for index and searching tekgenesis.sales.basic.Product */
public class ProductSearcherBase
    extends IndexSearcher
{

    //~ Fields ...................................................................................................................

    @NotNull public final Str DESCRIPTION;
    @NotNull public final Str PRODUCT_ID;
    @NotNull public final Str MODEL;

    //~ Constructors .............................................................................................................

    /** Default constructor. */
    protected ProductSearcherBase() {
        super(Product.class);
        DESCRIPTION = fields().strField("description", "description");
        PRODUCT_ID = fields().strField("productId", "productId");
        MODEL = fields().strField("model", "model");
    }

    //~ Methods ..................................................................................................................

    @Override @NotNull public final String getIndexId() { return INDEX_ID; }

    //~ Fields ...................................................................................................................

    @NotNull private static final String INDEX_ID = "15d1d600ab8cdd3e0045dec56230ba";
    @NotNull public static final ProductSearcher PRODUCT_SEARCHER = new ProductSearcher();

}
