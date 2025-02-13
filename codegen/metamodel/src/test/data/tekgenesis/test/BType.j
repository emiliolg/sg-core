package tekgenesis.test;

import tekgenesis.test.g.BTypeBase;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.io.Serializable;

/** User class for Model: BType */
public class BType
    extends BTypeBase
    implements Serializable
{

    //~ Constructors .............................................................................................................

    /** Constructor for BType */
    @JsonCreator public BType(@JsonProperty("id") int id) { super(id); }

    //~ Fields ...................................................................................................................

    private static final long serialVersionUID = -2934465930150221204L;

}
