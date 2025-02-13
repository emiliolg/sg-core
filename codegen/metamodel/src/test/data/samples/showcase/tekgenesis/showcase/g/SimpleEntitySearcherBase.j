package tekgenesis.showcase.g;

import tekgenesis.index.IndexSearcher;
import org.jetbrains.annotations.NotNull;
import tekgenesis.showcase.SimpleEntity;
import tekgenesis.showcase.SimpleEntitySearcher;
import tekgenesis.index.SearchableField.Str;

/** Base class for index and searching tekgenesis.showcase.SimpleEntity */
public class SimpleEntitySearcherBase
    extends IndexSearcher
{

    //~ Fields ...................................................................................................................

    @NotNull public final Str NAME;
    @NotNull public final Str DESCRIPTION;

    //~ Constructors .............................................................................................................

    /** Default constructor. */
    protected SimpleEntitySearcherBase() {
        super(SimpleEntity.class);
        NAME = fields().strField("name", "name");
        DESCRIPTION = fields().strField("description", "description");
    }

    //~ Methods ..................................................................................................................

    @Override @NotNull public final String getIndexId() { return INDEX_ID; }

    //~ Fields ...................................................................................................................

    @NotNull private static final String INDEX_ID = "540d6d9f33999d5bdf6f99a70e52b3";
    @NotNull public static final SimpleEntitySearcher SIMPLE_ENTITY_SEARCHER = new SimpleEntitySearcher();

}
