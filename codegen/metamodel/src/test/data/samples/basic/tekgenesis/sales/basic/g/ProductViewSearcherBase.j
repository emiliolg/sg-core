package tekgenesis.sales.basic.g;

import tekgenesis.index.IndexSearcher;
import org.jetbrains.annotations.NotNull;
import tekgenesis.sales.basic.ProductView;
import tekgenesis.sales.basic.ProductViewSearcher;
import tekgenesis.index.SearchableField.Str;

/** Base class for index and searching tekgenesis.sales.basic.ProductView */
public class ProductViewSearcherBase
    extends IndexSearcher
{

    //~ Fields ...................................................................................................................

    @NotNull public final Str VDESCR;
    @NotNull public final Str CATEGORY_ATT;

    //~ Constructors .............................................................................................................

    /** Default constructor. */
    protected ProductViewSearcherBase() {
        super(ProductView.class);
        VDESCR = fields().strField("vdescr", "vdescr");
        CATEGORY_ATT = fields().strField("categoryAtt", "categoryAtt").filterOnly();
    }

    //~ Methods ..................................................................................................................

    @Override @NotNull public final String getIndexId() { return INDEX_ID; }

    //~ Fields ...................................................................................................................

    @NotNull private static final String INDEX_ID = "77139b8b36ebd2b2ff7d2b3c95ce0a";
    @NotNull public static final ProductViewSearcher PRODUCT_VIEW_SEARCHER = new ProductViewSearcher();

}
