package tekgenesis.sales.basic.g;

import tekgenesis.sales.basic.Category;
import tekgenesis.sales.basic.CategorySearcher;
import tekgenesis.index.IndexSearcher;
import tekgenesis.index.SearchableField.LongFld;
import org.jetbrains.annotations.NotNull;
import tekgenesis.index.SearchableField.Str;

/** Base class for index and searching tekgenesis.sales.basic.Category */
public class CategorySearcherBase
    extends IndexSearcher
{

    //~ Fields ...................................................................................................................

    @NotNull public final LongFld ID_KEY;
    @NotNull public final Str NAME;
    @NotNull public final Str DESCR;

    //~ Constructors .............................................................................................................

    /** Default constructor. */
    protected CategorySearcherBase() {
        super(Category.class);
        ID_KEY = fields().longField("idKey", "idKey");
        NAME = fields().strField("name", "name");
        DESCR = fields().strField("descr", "descr");
    }

    //~ Methods ..................................................................................................................

    @Override @NotNull public final String getIndexId() { return INDEX_ID; }

    //~ Fields ...................................................................................................................

    @NotNull private static final String INDEX_ID = "565afed727e19ed0f3944b7ac844d2";
    @NotNull public static final CategorySearcher CATEGORY_SEARCHER = new CategorySearcher();

}
