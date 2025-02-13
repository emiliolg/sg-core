package views.g;

import tekgenesis.index.InstanceSearcher;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import static tekgenesis.metadata.entity.SearchField.SearchFieldType.*;
import static tekgenesis.metadata.entity.SearchFieldBuilder.field;

/** Base class for index and searching views.R */
public class RSearcherBase
    extends InstanceSearcher
{

    //~ Constructors .............................................................................................................

    /** Default constructor, must be ran :). */
    protected RSearcherBase() { super(field("id", INTEGER).build()); }

    //~ Methods ..................................................................................................................

    @Override @NotNull public String getEntityFqn() { return ENTITY_FQN; }

    //~ Fields ...................................................................................................................

    @Nullable private static final String ENTITY_FQN = "views.R";

}
