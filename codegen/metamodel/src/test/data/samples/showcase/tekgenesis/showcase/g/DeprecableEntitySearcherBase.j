package tekgenesis.showcase.g;

import tekgenesis.showcase.DeprecableEntity;
import tekgenesis.showcase.DeprecableEntitySearcher;
import tekgenesis.index.IndexSearcher;
import org.jetbrains.annotations.NotNull;
import tekgenesis.index.SearchableField.Str;

/** Base class for index and searching tekgenesis.showcase.DeprecableEntity */
public class DeprecableEntitySearcherBase
    extends IndexSearcher
{

    //~ Fields ...................................................................................................................

    @NotNull public final Str NAME;

    //~ Constructors .............................................................................................................

    /** Default constructor. */
    protected DeprecableEntitySearcherBase() {
        super(DeprecableEntity.class);
        NAME = fields().strField("name", "name");
    }

    //~ Methods ..................................................................................................................

    @Override @NotNull public final String getIndexId() { return INDEX_ID; }

    //~ Fields ...................................................................................................................

    @NotNull private static final String INDEX_ID = "2d5e5816759e8afdbaaf0d18a031ab";
    @NotNull public static final DeprecableEntitySearcher DEPRECABLE_ENTITY_SEARCHER = new DeprecableEntitySearcher();

}
