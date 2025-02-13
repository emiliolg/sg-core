package tekgenesis.showcase.g;

import tekgenesis.showcase.DateEntity;
import tekgenesis.showcase.DateEntitySearcher;
import tekgenesis.index.IndexSearcher;
import tekgenesis.index.SearchableField.Int;
import org.jetbrains.annotations.NotNull;

/** Base class for index and searching tekgenesis.showcase.DateEntity */
public class DateEntitySearcherBase
    extends IndexSearcher
{

    //~ Fields ...................................................................................................................

    @NotNull public final Int ID;

    //~ Constructors .............................................................................................................

    /** Default constructor. */
    protected DateEntitySearcherBase() {
        super(DateEntity.class);
        ID = fields().intField("id", "id");
    }

    //~ Methods ..................................................................................................................

    @Override @NotNull public final String getIndexId() { return INDEX_ID; }

    //~ Fields ...................................................................................................................

    @NotNull private static final String INDEX_ID = "473522a204f7e23330e21bdb80955f";
    @NotNull public static final DateEntitySearcher DATE_ENTITY_SEARCHER = new DateEntitySearcher();

}
