package tekgenesis.showcase.g;

import tekgenesis.showcase.AnotherSimpleEntity;
import tekgenesis.showcase.AnotherSimpleEntitySearcher;
import tekgenesis.index.IndexSearcher;
import org.jetbrains.annotations.NotNull;
import tekgenesis.index.SearchableField.Str;

/** Base class for index and searching tekgenesis.showcase.AnotherSimpleEntity */
public class AnotherSimpleEntitySearcherBase
    extends IndexSearcher
{

    //~ Fields ...................................................................................................................

    @NotNull public final Str NAME;

    //~ Constructors .............................................................................................................

    /** Default constructor. */
    protected AnotherSimpleEntitySearcherBase() {
        super(AnotherSimpleEntity.class);
        NAME = fields().strField("name", "name");
    }

    //~ Methods ..................................................................................................................

    @Override @NotNull public final String getIndexId() { return INDEX_ID; }

    //~ Fields ...................................................................................................................

    @NotNull private static final String INDEX_ID = "1a54d86d09c5ffcecc55b48dfbc614";
    @NotNull public static final AnotherSimpleEntitySearcher ANOTHER_SIMPLE_ENTITY_SEARCHER = new AnotherSimpleEntitySearcher();

}
