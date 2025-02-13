package tekgenesis.showcase.g;

import tekgenesis.index.IndexSearcher;
import tekgenesis.index.SearchableField.Int;
import tekgenesis.showcase.Label;
import tekgenesis.showcase.LabelSearcher;
import org.jetbrains.annotations.NotNull;

/** Base class for index and searching tekgenesis.showcase.Label */
public class LabelSearcherBase
    extends IndexSearcher
{

    //~ Fields ...................................................................................................................

    @NotNull public final Int ID;

    //~ Constructors .............................................................................................................

    /** Default constructor. */
    protected LabelSearcherBase() {
        super(Label.class);
        ID = fields().intField("id", "id");
    }

    //~ Methods ..................................................................................................................

    @Override @NotNull public final String getIndexId() { return INDEX_ID; }

    //~ Fields ...................................................................................................................

    @NotNull private static final String INDEX_ID = "832af022e99b8b027dba0e0db84f68";
    @NotNull public static final LabelSearcher LABEL_SEARCHER = new LabelSearcher();

}
