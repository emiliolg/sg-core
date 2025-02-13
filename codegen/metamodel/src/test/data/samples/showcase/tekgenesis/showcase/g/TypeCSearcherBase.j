package tekgenesis.showcase.g;

import tekgenesis.index.IndexSearcher;
import org.jetbrains.annotations.NotNull;
import tekgenesis.index.SearchableField.Str;
import tekgenesis.showcase.TypeC;
import tekgenesis.showcase.TypeCSearcher;

/** Base class for index and searching tekgenesis.showcase.TypeC */
public class TypeCSearcherBase
    extends IndexSearcher
{

    //~ Fields ...................................................................................................................

    @NotNull public final Str A;

    //~ Constructors .............................................................................................................

    /** Default constructor. */
    protected TypeCSearcherBase() {
        super(TypeC.class);
        A = fields().strField("a", "a");
    }

    //~ Methods ..................................................................................................................

    @Override @NotNull public final String getIndexId() { return INDEX_ID; }

    //~ Fields ...................................................................................................................

    @NotNull private static final String INDEX_ID = "1e6d7d53a00d797ae55e602a332848";
    @NotNull public static final TypeCSearcher TYPE_CSEARCHER = new TypeCSearcher();

}
