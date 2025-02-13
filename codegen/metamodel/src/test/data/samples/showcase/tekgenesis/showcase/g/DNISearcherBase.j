package tekgenesis.showcase.g;

import tekgenesis.showcase.DNI;
import tekgenesis.showcase.DNISearcher;
import tekgenesis.index.IndexSearcher;
import tekgenesis.index.SearchableField.Int;
import org.jetbrains.annotations.NotNull;

/** Base class for index and searching tekgenesis.showcase.DNI */
public class DNISearcherBase
    extends IndexSearcher
{

    //~ Fields ...................................................................................................................

    @NotNull public final Int NUMBER;

    //~ Constructors .............................................................................................................

    /** Default constructor. */
    protected DNISearcherBase() {
        super(DNI.class);
        NUMBER = fields().intField("number", "number");
    }

    //~ Methods ..................................................................................................................

    @Override @NotNull public final String getIndexId() { return INDEX_ID; }

    //~ Fields ...................................................................................................................

    @NotNull private static final String INDEX_ID = "c723a170cf83b603c0a7b2e9b86949";
    @NotNull public static final DNISearcher DNISEARCHER = new DNISearcher();

}
