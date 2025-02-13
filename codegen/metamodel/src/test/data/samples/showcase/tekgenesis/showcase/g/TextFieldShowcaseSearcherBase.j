package tekgenesis.showcase.g;

import tekgenesis.index.IndexSearcher;
import tekgenesis.index.SearchableField.Int;
import org.jetbrains.annotations.NotNull;
import tekgenesis.showcase.TextFieldShowcase;
import tekgenesis.showcase.TextFieldShowcaseSearcher;

/** Base class for index and searching tekgenesis.showcase.TextFieldShowcase */
public class TextFieldShowcaseSearcherBase
    extends IndexSearcher
{

    //~ Fields ...................................................................................................................

    @NotNull public final Int ID_KEY;

    //~ Constructors .............................................................................................................

    /** Default constructor. */
    protected TextFieldShowcaseSearcherBase() {
        super(TextFieldShowcase.class);
        ID_KEY = fields().intField("idKey", "idKey");
    }

    //~ Methods ..................................................................................................................

    @Override @NotNull public final String getIndexId() { return INDEX_ID; }

    //~ Fields ...................................................................................................................

    @NotNull private static final String INDEX_ID = "4eee1bee4deff41338cfb55b6dfbed";
    @NotNull public static final TextFieldShowcaseSearcher TEXT_FIELD_SHOWCASE_SEARCHER = new TextFieldShowcaseSearcher();

}
