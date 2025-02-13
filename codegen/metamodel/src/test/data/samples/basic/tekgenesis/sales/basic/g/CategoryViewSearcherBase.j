package tekgenesis.sales.basic.g;

import tekgenesis.sales.basic.CategoryView;
import tekgenesis.sales.basic.CategoryViewSearcher;
import tekgenesis.index.IndexSearcher;
import org.jetbrains.annotations.NotNull;
import tekgenesis.index.SearchableField.Str;

/** Base class for index and searching tekgenesis.sales.basic.CategoryView */
public class CategoryViewSearcherBase
    extends IndexSearcher
{

    //~ Fields ...................................................................................................................

    @NotNull public final Str VDESCR;
    @NotNull public final Str VNAME;

    //~ Constructors .............................................................................................................

    /** Default constructor. */
    protected CategoryViewSearcherBase() {
        super(CategoryView.class);
        VDESCR = fields().strField("vdescr", "vdescr");
        VNAME = fields().strField("vname", "vname");
    }

    //~ Methods ..................................................................................................................

    @Override @NotNull public final String getIndexId() { return INDEX_ID; }

    //~ Fields ...................................................................................................................

    @NotNull private static final String INDEX_ID = "bdce6f01f72347c9d401e8da7fab42";
    @NotNull public static final CategoryViewSearcher CATEGORY_VIEW_SEARCHER = new CategoryViewSearcher();

}
