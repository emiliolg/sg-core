package tekgenesis.sales.basic.g;

import tekgenesis.sales.basic.CategorySqlViewView;
import tekgenesis.sales.basic.CategorySqlViewViewSearcher;
import tekgenesis.index.IndexSearcher;
import org.jetbrains.annotations.NotNull;
import tekgenesis.index.SearchableField.Str;

/** Base class for index and searching tekgenesis.sales.basic.CategorySqlViewView */
public class CategorySqlViewViewSearcherBase
    extends IndexSearcher
{

    //~ Fields ...................................................................................................................

    @NotNull public final Str VDESCR;

    //~ Constructors .............................................................................................................

    /** Default constructor. */
    protected CategorySqlViewViewSearcherBase() {
        super(CategorySqlViewView.class);
        VDESCR = fields().strField("vdescr", "vdescr");
    }

    //~ Methods ..................................................................................................................

    @Override @NotNull public final String getIndexId() { return INDEX_ID; }

    //~ Fields ...................................................................................................................

    @NotNull private static final String INDEX_ID = "dd695d9c91c9a8d33739fcdcb98287";
    @NotNull public static final CategorySqlViewViewSearcher CATEGORY_SQL_VIEW_VIEW_SEARCHER = new CategorySqlViewViewSearcher();

}
