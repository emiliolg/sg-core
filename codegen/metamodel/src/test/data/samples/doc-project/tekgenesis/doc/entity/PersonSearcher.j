package tekgenesis.doc.entity;

import java.util.List;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import tekgenesis.doc.entity.g.PersonSearcherBase;
import tekgenesis.index.SearchResult;

/** User class for index and searching Person */
public class PersonSearcher
    extends PersonSearcherBase
{

    //~ Methods ..................................................................................................................

    @Override @NotNull public List<SearchResult> search(@Nullable String query) { return super.search(query); }

}
