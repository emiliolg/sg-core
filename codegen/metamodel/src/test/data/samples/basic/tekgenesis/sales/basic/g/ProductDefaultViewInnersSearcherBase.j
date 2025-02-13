package tekgenesis.sales.basic.g;

import tekgenesis.index.IndexSearcher;
import org.jetbrains.annotations.NotNull;
import tekgenesis.sales.basic.ProductDefaultViewInners;
import tekgenesis.sales.basic.ProductDefaultViewInnersSearcher;
import tekgenesis.index.SearchableField.Str;

/** Base class for index and searching tekgenesis.sales.basic.ProductDefaultViewInners */
public class ProductDefaultViewInnersSearcherBase
    extends IndexSearcher
{

    //~ Fields ...................................................................................................................

    @NotNull public final Str VDESCR;

    //~ Constructors .............................................................................................................

    /** Default constructor. */
    protected ProductDefaultViewInnersSearcherBase() {
        super(ProductDefaultViewInners.class);
        VDESCR = fields().strField("vdescr", "vdescr");
    }

    //~ Methods ..................................................................................................................

    @Override @NotNull public final String getIndexId() { return INDEX_ID; }

    //~ Fields ...................................................................................................................

    @NotNull private static final String INDEX_ID = "d2412059447d3a9ab9e6b75a3f1299";
    @NotNull public static final ProductDefaultViewInnersSearcher PRODUCT_DEFAULT_VIEW_INNERS_SEARCHER = new ProductDefaultViewInnersSearcher();

}
