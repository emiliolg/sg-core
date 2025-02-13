package tekgenesis.test;

import tekgenesis.test.g.AParentTypeBase;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.io.Serializable;

/** User class for Model: AParentType */
public class AParentType
    extends AParentTypeBase
    implements Serializable
{

    //~ Constructors .............................................................................................................

    /** Constructor for AParentType */
    @JsonCreator public AParentType(@JsonProperty("id") int id) { super(id); }

    //~ Fields ...................................................................................................................

    private static final long serialVersionUID = -6553026603483835785L;

}
