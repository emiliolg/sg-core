package tekgenesis.showcase.g;

import tekgenesis.index.SearchableField.Decimal;
import tekgenesis.index.IndexSearcher;
import org.jetbrains.annotations.NotNull;
import tekgenesis.showcase.TypeA;
import tekgenesis.showcase.TypeASearcher;

/** Base class for index and searching tekgenesis.showcase.TypeA */
public class TypeASearcherBase
    extends IndexSearcher
{

    //~ Fields ...................................................................................................................

    @NotNull public final Decimal D;

    //~ Constructors .............................................................................................................

    /** Default constructor. */
    protected TypeASearcherBase() {
        super(TypeA.class);
        D = fields().decimalField("d", "d");
    }

    //~ Methods ..................................................................................................................

    @Override @NotNull public final String getIndexId() { return INDEX_ID; }

    //~ Fields ...................................................................................................................

    @NotNull private static final String INDEX_ID = "4cfd1cabec7a40b4a3cd5c06185f2e";
    @NotNull public static final TypeASearcher TYPE_ASEARCHER = new TypeASearcher();

}
