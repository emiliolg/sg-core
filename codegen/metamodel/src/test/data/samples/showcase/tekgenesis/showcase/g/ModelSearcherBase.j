package tekgenesis.showcase.g;

import tekgenesis.index.IndexSearcher;
import tekgenesis.showcase.Model;
import tekgenesis.showcase.ModelSearcher;
import org.jetbrains.annotations.NotNull;
import tekgenesis.index.SearchableField.Str;

/** Base class for index and searching tekgenesis.showcase.Model */
public class ModelSearcherBase
    extends IndexSearcher
{

    //~ Fields ...................................................................................................................

    @NotNull public final Str MODEL;

    //~ Constructors .............................................................................................................

    /** Default constructor. */
    protected ModelSearcherBase() {
        super(Model.class);
        MODEL = fields().strField("model", "model");
    }

    //~ Methods ..................................................................................................................

    @Override @NotNull public final String getIndexId() { return INDEX_ID; }

    //~ Fields ...................................................................................................................

    @NotNull private static final String INDEX_ID = "3a34acab55120a50f3ac9cf91a2536";
    @NotNull public static final ModelSearcher MODEL_SEARCHER = new ModelSearcher();

}
