package tekgenesis.showcase.g;

import tekgenesis.showcase.Image;
import tekgenesis.showcase.ImageSearcher;
import tekgenesis.index.IndexSearcher;
import org.jetbrains.annotations.NotNull;
import tekgenesis.index.SearchableField.Str;

/** Base class for index and searching tekgenesis.showcase.Image */
public class ImageSearcherBase
    extends IndexSearcher
{

    //~ Fields ...................................................................................................................

    @NotNull public final Str NAME;

    //~ Constructors .............................................................................................................

    /** Default constructor. */
    protected ImageSearcherBase() {
        super(Image.class);
        NAME = fields().strField("name", "name");
    }

    //~ Methods ..................................................................................................................

    @Override @NotNull public final String getIndexId() { return INDEX_ID; }

    //~ Fields ...................................................................................................................

    @NotNull private static final String INDEX_ID = "4562b8299af89accd58c7d7bf09d35";
    @NotNull public static final ImageSearcher IMAGE_SEARCHER = new ImageSearcher();

}
