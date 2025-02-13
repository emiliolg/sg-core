package tekgenesis.showcase.g;

import tekgenesis.showcase.AnotherDeprecableEntity;
import tekgenesis.showcase.AnotherDeprecableEntitySearcher;
import tekgenesis.index.IndexSearcher;
import org.jetbrains.annotations.NotNull;
import tekgenesis.index.SearchableField.Str;

/** Base class for index and searching tekgenesis.showcase.AnotherDeprecableEntity */
public class AnotherDeprecableEntitySearcherBase
    extends IndexSearcher
{

    //~ Fields ...................................................................................................................

    @NotNull public final Str NAME;

    //~ Constructors .............................................................................................................

    /** Default constructor. */
    protected AnotherDeprecableEntitySearcherBase() {
        super(AnotherDeprecableEntity.class);
        NAME = fields().strField("name", "name");
    }

    //~ Methods ..................................................................................................................

    @Override @NotNull public final String getIndexId() { return INDEX_ID; }

    //~ Fields ...................................................................................................................

    @NotNull private static final String INDEX_ID = "ab26a4f8bf5ecf2f8bec997848e8b1";
    @NotNull public static final AnotherDeprecableEntitySearcher ANOTHER_DEPRECABLE_ENTITY_SEARCHER = new AnotherDeprecableEntitySearcher();

}
