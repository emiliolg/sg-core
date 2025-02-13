package tekgenesis.showcase.g;

import tekgenesis.showcase.Country;
import tekgenesis.index.SearchableField.Enum;
import tekgenesis.index.IndexSearcher;
import tekgenesis.showcase.Make;
import tekgenesis.showcase.MakeSearcher;
import org.jetbrains.annotations.NotNull;
import tekgenesis.index.SearchableField.Str;

/** Base class for index and searching tekgenesis.showcase.Make */
public class MakeSearcherBase
    extends IndexSearcher
{

    //~ Fields ...................................................................................................................

    @NotNull public final Str NAME;
    @NotNull public final Enum<Country> ORIGIN;

    //~ Constructors .............................................................................................................

    /** Default constructor. */
    protected MakeSearcherBase() {
        super(Make.class);
        NAME = fields().strField("name", "name");
        ORIGIN = fields().<Country>enumField("origin", "origin", Country.class);
    }

    //~ Methods ..................................................................................................................

    @Override @NotNull public final String getIndexId() { return INDEX_ID; }

    //~ Fields ...................................................................................................................

    @NotNull private static final String INDEX_ID = "2bef20adb3efaebcbc8b9f5a3e345b";
    @NotNull public static final MakeSearcher MAKE_SEARCHER = new MakeSearcher();

}
