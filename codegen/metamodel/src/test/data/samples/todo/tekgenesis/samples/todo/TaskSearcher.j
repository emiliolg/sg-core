package tekgenesis.samples.todo;

import java.util.List;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import tekgenesis.index.SearchResult;
import tekgenesis.samples.todo.g.TaskSearcherBase;

/** User class for index and searching Task */
public class TaskSearcher
    extends TaskSearcherBase
{

    //~ Methods ..................................................................................................................

    @Override @NotNull public List<SearchResult> search(@Nullable String query) { return super.search(query); }

}
