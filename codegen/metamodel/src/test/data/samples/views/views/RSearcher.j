package views;

import java.util.List;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import views.g.RSearcherBase;
import tekgenesis.index.SearchResult;

/** User class for index and searching R */
public class RSearcher
    extends RSearcherBase
{

    //~ Methods ..................................................................................................................

    @Override @NotNull public List<SearchResult> search(@Nullable String query) { return super.search(query); }

}
