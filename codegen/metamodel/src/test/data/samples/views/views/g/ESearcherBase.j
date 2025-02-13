package views.g;

import tekgenesis.index.InstanceSearcher;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import static tekgenesis.metadata.entity.SearchField.SearchFieldType.*;
import static tekgenesis.metadata.entity.SearchFieldBuilder.field;

/** Base class for index and searching views.E */
public class ESearcherBase
    extends InstanceSearcher
{

    //~ Constructors .............................................................................................................

    /** Default constructor, must be ran :). */
    protected ESearcherBase() { super(field("r", STRING).build()); }

    //~ Methods ..................................................................................................................

    @Override @NotNull public String getEntityFqn() { return ENTITY_FQN; }

    //~ Fields ...................................................................................................................

    @Nullable private static final String ENTITY_FQN = "views.E";

}
