package tekgenesis.test;

import tekgenesis.test.g.BExtendedTypeBase;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.io.Serializable;

/** User class for Model: BExtendedType */
public class BExtendedType
    extends BExtendedTypeBase
    implements Serializable
{

    //~ Constructors .............................................................................................................

    /** Constructor for BExtendedType */
    @JsonCreator public BExtendedType(@JsonProperty("id") int id) { super(id); }

    //~ Fields ...................................................................................................................

    private static final long serialVersionUID = 2716900485968770259L;

}
