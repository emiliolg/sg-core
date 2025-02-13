package tekgenesis.sales.basic.g;

import tekgenesis.index.IndexSearcher;
import org.jetbrains.annotations.NotNull;
import tekgenesis.sales.basic.ProductDefaultView;
import tekgenesis.sales.basic.ProductDefaultViewSearcher;
import tekgenesis.index.SearchableField.Str;

/** Base class for index and searching tekgenesis.sales.basic.ProductDefaultView */
public class ProductDefaultViewSearcherBase
    extends IndexSearcher
{

    //~ Fields ...................................................................................................................

    @NotNull public final Str VDESCR;

    //~ Constructors .............................................................................................................

    /** Default constructor. */
    protected ProductDefaultViewSearcherBase() {
        super(ProductDefaultView.class);
        VDESCR = fields().strField("vdescr", "vdescr");
    }

    //~ Methods ..................................................................................................................

    @Override @NotNull public final String getIndexId() { return INDEX_ID; }

    //~ Fields ...................................................................................................................

    @NotNull private static final String INDEX_ID = "f7558b199f12ba038dbeb6b3abbb05";
    @NotNull public static final ProductDefaultViewSearcher PRODUCT_DEFAULT_VIEW_SEARCHER = new ProductDefaultViewSearcher();

}
