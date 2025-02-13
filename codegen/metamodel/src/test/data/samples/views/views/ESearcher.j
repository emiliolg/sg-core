package views;

import views.g.ESearcherBase;
import java.util.List;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import tekgenesis.index.SearchResult;

/** User class for index and searching E */
public class ESearcher
    extends ESearcherBase
{

    //~ Methods ..................................................................................................................

    @Override @NotNull public List<SearchResult> search(@Nullable String query) { return super.search(query); }

}
