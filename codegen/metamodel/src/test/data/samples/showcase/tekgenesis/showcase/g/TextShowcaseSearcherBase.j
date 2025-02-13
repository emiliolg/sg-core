package tekgenesis.showcase.g;

import tekgenesis.index.IndexSearcher;
import tekgenesis.index.SearchableField.Int;
import org.jetbrains.annotations.NotNull;
import tekgenesis.showcase.TextShowcase;
import tekgenesis.showcase.TextShowcaseSearcher;

/** Base class for index and searching tekgenesis.showcase.TextShowcase */
public class TextShowcaseSearcherBase
    extends IndexSearcher
{

    //~ Fields ...................................................................................................................

    @NotNull public final Int ID_KEY;

    //~ Constructors .............................................................................................................

    /** Default constructor. */
    protected TextShowcaseSearcherBase() {
        super(TextShowcase.class);
        ID_KEY = fields().intField("idKey", "idKey");
    }

    //~ Methods ..................................................................................................................

    @Override @NotNull public final String getIndexId() { return INDEX_ID; }

    //~ Fields ...................................................................................................................

    @NotNull private static final String INDEX_ID = "9d33ff0f45b8f50fe0ee941f142978";
    @NotNull public static final TextShowcaseSearcher TEXT_SHOWCASE_SEARCHER = new TextShowcaseSearcher();

}
