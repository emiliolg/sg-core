package tekgenesis.doc;

import tekgenesis.doc.g.APersonSearcherBase;
import java.util.List;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import tekgenesis.index.SearchResult;

/** User class for index and searching APerson */
public class APersonSearcher
    extends APersonSearcherBase
{

    //~ Methods ..................................................................................................................

    @Override @NotNull public List<SearchResult> search(@Nullable String query) { return super.search(query); }

}
