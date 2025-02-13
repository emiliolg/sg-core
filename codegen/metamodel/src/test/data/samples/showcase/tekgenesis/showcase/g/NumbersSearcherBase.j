package tekgenesis.showcase.g;

import tekgenesis.index.IndexSearcher;
import org.jetbrains.annotations.NotNull;
import tekgenesis.showcase.Numbers;
import tekgenesis.showcase.NumbersSearcher;
import tekgenesis.index.SearchableField.Str;

/** Base class for index and searching tekgenesis.showcase.Numbers */
public class NumbersSearcherBase
    extends IndexSearcher
{

    //~ Fields ...................................................................................................................

    @NotNull public final Str NAME;

    //~ Constructors .............................................................................................................

    /** Default constructor. */
    protected NumbersSearcherBase() {
        super(Numbers.class);
        NAME = fields().strField("name", "name");
    }

    //~ Methods ..................................................................................................................

    @Override @NotNull public final String getIndexId() { return INDEX_ID; }

    //~ Fields ...................................................................................................................

    @NotNull private static final String INDEX_ID = "e3dbad84c9ffce0b7ddaadaf2f1d8e";
    @NotNull public static final NumbersSearcher NUMBERS_SEARCHER = new NumbersSearcher();

}
