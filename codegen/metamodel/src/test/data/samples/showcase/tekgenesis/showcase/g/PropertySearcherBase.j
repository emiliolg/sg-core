package tekgenesis.showcase.g;

import tekgenesis.index.IndexSearcher;
import org.jetbrains.annotations.NotNull;
import tekgenesis.showcase.Property;
import tekgenesis.showcase.PropertySearcher;
import tekgenesis.index.SearchableField.Str;

/** Base class for index and searching tekgenesis.showcase.Property */
public class PropertySearcherBase
    extends IndexSearcher
{

    //~ Fields ...................................................................................................................

    @NotNull public final Str NAME;

    //~ Constructors .............................................................................................................

    /** Default constructor. */
    protected PropertySearcherBase() {
        super(Property.class);
        NAME = fields().strField("name", "name");
    }

    //~ Methods ..................................................................................................................

    @Override @NotNull public final String getIndexId() { return INDEX_ID; }

    //~ Fields ...................................................................................................................

    @NotNull private static final String INDEX_ID = "329234413d1845a198b006ca2592fb";
    @NotNull public static final PropertySearcher PROPERTY_SEARCHER = new PropertySearcher();

}
