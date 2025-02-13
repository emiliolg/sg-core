package tekgenesis.sales.basic.g;

import tekgenesis.index.IndexSearcher;
import tekgenesis.index.SearchableField.Int;
import org.jetbrains.annotations.NotNull;
import tekgenesis.sales.basic.ProductDefaultSqlView;
import tekgenesis.sales.basic.ProductDefaultSqlViewSearcher;

/** Base class for index and searching tekgenesis.sales.basic.ProductDefaultSqlView */
public class ProductDefaultSqlViewSearcherBase
    extends IndexSearcher
{

    //~ Fields ...................................................................................................................

    @NotNull public final Int ID;

    //~ Constructors .............................................................................................................

    /** Default constructor. */
    protected ProductDefaultSqlViewSearcherBase() {
        super(ProductDefaultSqlView.class);
        ID = fields().intField("id", "id");
    }

    //~ Methods ..................................................................................................................

    @Override @NotNull public final String getIndexId() { return INDEX_ID; }

    //~ Fields ...................................................................................................................

    @NotNull private static final String INDEX_ID = "f362cb55d1cede35cfa2dd4c6eaa3b";
    @NotNull public static final ProductDefaultSqlViewSearcher PRODUCT_DEFAULT_SQL_VIEW_SEARCHER = new ProductDefaultSqlViewSearcher();

}
