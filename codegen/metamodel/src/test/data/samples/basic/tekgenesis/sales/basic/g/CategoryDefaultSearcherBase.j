package tekgenesis.sales.basic.g;

import tekgenesis.sales.basic.CategoryDefault;
import tekgenesis.sales.basic.CategoryDefaultSearcher;
import tekgenesis.index.IndexSearcher;
import tekgenesis.index.SearchableField.Int;
import org.jetbrains.annotations.NotNull;
import tekgenesis.index.SearchableField.Str;

/** Base class for index and searching tekgenesis.sales.basic.CategoryDefault */
public class CategoryDefaultSearcherBase
    extends IndexSearcher
{

    //~ Fields ...................................................................................................................

    @NotNull public final Str DESCR;
    @NotNull public final Int ID;
    @NotNull public final Str NAME;

    //~ Constructors .............................................................................................................

    /** Default constructor. */
    protected CategoryDefaultSearcherBase() {
        super(CategoryDefault.class);
        DESCR = fields().strField("descr", "descr");
        ID = fields().intField("id", "id");
        NAME = fields().strField("name", "name");
    }

    //~ Methods ..................................................................................................................

    @Override @NotNull public final String getIndexId() { return INDEX_ID; }

    //~ Fields ...................................................................................................................

    @NotNull private static final String INDEX_ID = "6389966c0ded7cb26f737d45256979";
    @NotNull public static final CategoryDefaultSearcher CATEGORY_DEFAULT_SEARCHER = new CategoryDefaultSearcher();

}
