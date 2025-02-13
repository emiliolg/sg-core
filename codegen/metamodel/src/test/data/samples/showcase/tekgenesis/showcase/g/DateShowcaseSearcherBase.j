package tekgenesis.showcase.g;

import tekgenesis.showcase.DateShowcase;
import tekgenesis.showcase.DateShowcaseSearcher;
import tekgenesis.index.IndexSearcher;
import tekgenesis.index.SearchableField.Int;
import org.jetbrains.annotations.NotNull;

/** Base class for index and searching tekgenesis.showcase.DateShowcase */
public class DateShowcaseSearcherBase
    extends IndexSearcher
{

    //~ Fields ...................................................................................................................

    @NotNull public final Int ID_KEY;

    //~ Constructors .............................................................................................................

    /** Default constructor. */
    protected DateShowcaseSearcherBase() {
        super(DateShowcase.class);
        ID_KEY = fields().intField("idKey", "idKey");
    }

    //~ Methods ..................................................................................................................

    @Override @NotNull public final String getIndexId() { return INDEX_ID; }

    //~ Fields ...................................................................................................................

    @NotNull private static final String INDEX_ID = "813d0ea7c9c57951a7962bf84339ab";
    @NotNull public static final DateShowcaseSearcher DATE_SHOWCASE_SEARCHER = new DateShowcaseSearcher();

}
