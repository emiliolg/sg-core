package tekgenesis.showcase.g;

import tekgenesis.index.IndexSearcher;
import tekgenesis.showcase.Node;
import tekgenesis.showcase.NodeSearcher;
import org.jetbrains.annotations.NotNull;
import tekgenesis.index.SearchableField.Str;

/** Base class for index and searching tekgenesis.showcase.Node */
public class NodeSearcherBase
    extends IndexSearcher
{

    //~ Fields ...................................................................................................................

    @NotNull public final Str NAME;

    //~ Constructors .............................................................................................................

    /** Default constructor. */
    protected NodeSearcherBase() {
        super(Node.class);
        NAME = fields().strField("name", "name");
    }

    //~ Methods ..................................................................................................................

    @Override @NotNull public final String getIndexId() { return INDEX_ID; }

    //~ Fields ...................................................................................................................

    @NotNull private static final String INDEX_ID = "efcd8632cf435c012dca1180f49769";
    @NotNull public static final NodeSearcher NODE_SEARCHER = new NodeSearcher();

}
