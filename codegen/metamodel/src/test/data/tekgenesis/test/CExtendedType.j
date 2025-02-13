package tekgenesis.test;

import tekgenesis.test.g.CExtendedTypeBase;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.io.Serializable;

/** User class for Model: CExtendedType */
public class CExtendedType
    extends CExtendedTypeBase
    implements Serializable
{

    //~ Constructors .............................................................................................................

    /** Constructor for CExtendedType */
    @JsonCreator public CExtendedType(@JsonProperty("id") int id, @JsonProperty("cId") int cId) { super(id,cId); }

    //~ Fields ...................................................................................................................

    private static final long serialVersionUID = 6398010568744254364L;

}
