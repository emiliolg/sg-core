package tekgenesis.showcase.g;

import tekgenesis.showcase.DNI;
import tekgenesis.index.SearchableField.Ent;
import tekgenesis.index.IndexSearcher;
import org.jetbrains.annotations.NotNull;
import tekgenesis.showcase.PersonWithDni;
import tekgenesis.showcase.PersonWithDniSearcher;

/** Base class for index and searching tekgenesis.showcase.PersonWithDni */
public class PersonWithDniSearcherBase
    extends IndexSearcher
{

    //~ Fields ...................................................................................................................

    @NotNull public final Ent<DNI> DNI;

    //~ Constructors .............................................................................................................

    /** Default constructor. */
    protected PersonWithDniSearcherBase() {
        super(PersonWithDni.class);
        DNI = fields().entityField("dni", "dni", DNI.class);
    }

    //~ Methods ..................................................................................................................

    @Override @NotNull public final String getIndexId() { return INDEX_ID; }

    //~ Fields ...................................................................................................................

    @NotNull private static final String INDEX_ID = "86eff4e3b640275158a13db62e2f76";
    @NotNull public static final PersonWithDniSearcher PERSON_WITH_DNI_SEARCHER = new PersonWithDniSearcher();

}
