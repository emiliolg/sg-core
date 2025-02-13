package tekgenesis.doc.entity.g;

import tekgenesis.index.InstanceSearcher;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import static tekgenesis.metadata.entity.SearchField.SearchFieldType.*;
import static tekgenesis.metadata.entity.SearchFieldBuilder.field;

/** Base class for index and searching tekgenesis.doc.entity.Person */
public class PersonSearcherBase
    extends InstanceSearcher
{

    //~ Constructors .............................................................................................................

    /** Default constructor, must be ran :). */
    protected PersonSearcherBase() {
        super(field("first", STRING).build(),field("last", STRING).build());
    }

    //~ Methods ..................................................................................................................

    @Override @NotNull public String getEntityFqn() { return ENTITY_FQN; }

    //~ Fields ...................................................................................................................

    @Nullable private static final String ENTITY_FQN = "tekgenesis.doc.entity.Person";

}
