package tekgenesis.sales.basic.g;

import tekgenesis.index.IndexSearcher;
import tekgenesis.index.SearchableField.Int;
import org.jetbrains.annotations.NotNull;
import tekgenesis.sales.basic.ProductDefaultInners;
import tekgenesis.sales.basic.ProductDefaultInnersSearcher;
import tekgenesis.index.SearchableField.Str;

/** Base class for index and searching tekgenesis.sales.basic.ProductDefaultInners */
public class ProductDefaultInnersSearcherBase
    extends IndexSearcher
{

    //~ Fields ...................................................................................................................

    @NotNull public final Int ID;
    @NotNull public final Str MODEL;
    @NotNull public final Str DESCRIPTION;

    //~ Constructors .............................................................................................................

    /** Default constructor. */
    protected ProductDefaultInnersSearcherBase() {
        super(ProductDefaultInners.class);
        ID = fields().intField("id", "id");
        MODEL = fields().strField("model", "model");
        DESCRIPTION = fields().strField("description", "description");
    }

    //~ Methods ..................................................................................................................

    @Override @NotNull public final String getIndexId() { return INDEX_ID; }

    //~ Fields ...................................................................................................................

    @NotNull private static final String INDEX_ID = "703e4fbc4980434046e1390e625f55";
    @NotNull public static final ProductDefaultInnersSearcher PRODUCT_DEFAULT_INNERS_SEARCHER = new ProductDefaultInnersSearcher();

}
