package tekgenesis.sales.basic.g;

import tekgenesis.sales.basic.CategoryDefaultView;
import tekgenesis.sales.basic.CategoryDefaultViewSearcher;
import tekgenesis.index.IndexSearcher;
import org.jetbrains.annotations.NotNull;
import tekgenesis.index.SearchableField.Str;

/** Base class for index and searching tekgenesis.sales.basic.CategoryDefaultView */
public class CategoryDefaultViewSearcherBase
    extends IndexSearcher
{

    //~ Fields ...................................................................................................................

    @NotNull public final Str VNAME;
    @NotNull public final Str VDESCR;

    //~ Constructors .............................................................................................................

    /** Default constructor. */
    protected CategoryDefaultViewSearcherBase() {
        super(CategoryDefaultView.class);
        VNAME = fields().strField("vname", "vname");
        VDESCR = fields().strField("vdescr", "vdescr");
    }

    //~ Methods ..................................................................................................................

    @Override @NotNull public final String getIndexId() { return INDEX_ID; }

    //~ Fields ...................................................................................................................

    @NotNull private static final String INDEX_ID = "ed7bbf176d403f9d11f19bea1e9455";
    @NotNull public static final CategoryDefaultViewSearcher CATEGORY_DEFAULT_VIEW_SEARCHER = new CategoryDefaultViewSearcher();

}
