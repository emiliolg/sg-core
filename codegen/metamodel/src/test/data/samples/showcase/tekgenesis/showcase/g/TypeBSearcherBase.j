package tekgenesis.showcase.g;

import tekgenesis.index.IndexSearcher;
import org.jetbrains.annotations.NotNull;
import tekgenesis.index.SearchableField.Str;
import tekgenesis.showcase.TypeB;
import tekgenesis.showcase.TypeBSearcher;

/** Base class for index and searching tekgenesis.showcase.TypeB */
public class TypeBSearcherBase
    extends IndexSearcher
{

    //~ Fields ...................................................................................................................

    @NotNull public final Str S;

    //~ Constructors .............................................................................................................

    /** Default constructor. */
    protected TypeBSearcherBase() {
        super(TypeB.class);
        S = fields().strField("s", "s");
    }

    //~ Methods ..................................................................................................................

    @Override @NotNull public final String getIndexId() { return INDEX_ID; }

    //~ Fields ...................................................................................................................

    @NotNull private static final String INDEX_ID = "2eff959dcbd40c3b965bce9b096ced";
    @NotNull public static final TypeBSearcher TYPE_BSEARCHER = new TypeBSearcher();

}
