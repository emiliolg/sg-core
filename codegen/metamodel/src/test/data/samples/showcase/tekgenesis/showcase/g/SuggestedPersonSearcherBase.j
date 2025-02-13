package tekgenesis.showcase.g;

import tekgenesis.index.IndexSearcher;
import org.jetbrains.annotations.NotNull;
import tekgenesis.index.SearchableField.Str;
import tekgenesis.showcase.SuggestedPerson;
import tekgenesis.showcase.SuggestedPersonSearcher;

/** Base class for index and searching tekgenesis.showcase.SuggestedPerson */
public class SuggestedPersonSearcherBase
    extends IndexSearcher
{

    //~ Fields ...................................................................................................................

    @NotNull public final Str NAME;
    @NotNull public final Str LAST_NAME;

    //~ Constructors .............................................................................................................

    /** Default constructor. */
    protected SuggestedPersonSearcherBase() {
        super(SuggestedPerson.class);
        NAME = fields().strField("name", "name");
        LAST_NAME = fields().strField("lastName", "lastName");
    }

    //~ Methods ..................................................................................................................

    @Override @NotNull public final String getIndexId() { return INDEX_ID; }

    //~ Fields ...................................................................................................................

    @NotNull private static final String INDEX_ID = "f85cf339c2bed9a95841c715f343ea";
    @NotNull public static final SuggestedPersonSearcher SUGGESTED_PERSON_SEARCHER = new SuggestedPersonSearcher();

}
