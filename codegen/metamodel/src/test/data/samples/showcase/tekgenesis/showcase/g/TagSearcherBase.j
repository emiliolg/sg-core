package tekgenesis.showcase.g;

import tekgenesis.index.IndexSearcher;
import org.jetbrains.annotations.NotNull;
import tekgenesis.index.SearchableField.Str;
import tekgenesis.showcase.Tag;
import tekgenesis.showcase.TagSearcher;

/** Base class for index and searching tekgenesis.showcase.Tag */
public class TagSearcherBase
    extends IndexSearcher
{

    //~ Fields ...................................................................................................................

    @NotNull public final Str NAME;

    //~ Constructors .............................................................................................................

    /** Default constructor. */
    protected TagSearcherBase() {
        super(Tag.class);
        NAME = fields().strField("name", "name");
    }

    //~ Methods ..................................................................................................................

    @Override @NotNull public final String getIndexId() { return INDEX_ID; }

    //~ Fields ...................................................................................................................

    @NotNull private static final String INDEX_ID = "2240ec591203a783721ef2a939acba";
    @NotNull public static final TagSearcher TAG_SEARCHER = new TagSearcher();

}
