package tekgenesis.showcase.g;

import tekgenesis.index.IndexSearcher;
import tekgenesis.showcase.NamedItem;
import tekgenesis.showcase.NamedItemSearcher;
import org.jetbrains.annotations.NotNull;
import tekgenesis.index.SearchableField.Str;

/** Base class for index and searching tekgenesis.showcase.NamedItem */
public class NamedItemSearcherBase
    extends IndexSearcher
{

    //~ Fields ...................................................................................................................

    @NotNull public final Str NAME;

    //~ Constructors .............................................................................................................

    /** Default constructor. */
    protected NamedItemSearcherBase() {
        super(NamedItem.class);
        NAME = fields().strField("name", "name");
    }

    //~ Methods ..................................................................................................................

    @Override @NotNull public final String getIndexId() { return INDEX_ID; }

    //~ Fields ...................................................................................................................

    @NotNull private static final String INDEX_ID = "0a70ea09a31a9b289223c4ed7ddc22";
    @NotNull public static final NamedItemSearcher NAMED_ITEM_SEARCHER = new NamedItemSearcher();

}
