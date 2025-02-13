package tekgenesis.showcase.g;

import tekgenesis.showcase.Classroom;
import tekgenesis.showcase.ClassroomSearcher;
import tekgenesis.index.IndexSearcher;
import tekgenesis.index.SearchableField.Int;
import org.jetbrains.annotations.NotNull;
import tekgenesis.index.SearchableField.Str;

/** Base class for index and searching tekgenesis.showcase.Classroom */
public class ClassroomSearcherBase
    extends IndexSearcher
{

    //~ Fields ...................................................................................................................

    @NotNull public final Int ID_KEY;
    @NotNull public final Str ROOM;

    //~ Constructors .............................................................................................................

    /** Default constructor. */
    protected ClassroomSearcherBase() {
        super(Classroom.class);
        ID_KEY = fields().intField("idKey", "idKey");
        ROOM = fields().strField("room", "room");
    }

    //~ Methods ..................................................................................................................

    @Override @NotNull public final String getIndexId() { return INDEX_ID; }

    //~ Fields ...................................................................................................................

    @NotNull private static final String INDEX_ID = "f6e3e19d7acbd5b42d06aa138d0c66";
    @NotNull public static final ClassroomSearcher CLASSROOM_SEARCHER = new ClassroomSearcher();

}
