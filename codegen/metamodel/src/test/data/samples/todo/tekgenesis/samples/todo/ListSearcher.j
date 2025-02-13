package tekgenesis.samples.todo;

import java.util.List;
import tekgenesis.samples.todo.g.ListSearcherBase;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import tekgenesis.index.SearchResult;

/** User class for index and searching List */
public class ListSearcher
    extends ListSearcherBase
{

    //~ Methods ..................................................................................................................

    @Override @NotNull public List<SearchResult> search(@Nullable String query) { return super.search(query); }

}
