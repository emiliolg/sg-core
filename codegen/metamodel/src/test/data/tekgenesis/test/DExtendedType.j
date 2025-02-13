package tekgenesis.test;

import tekgenesis.test.g.DExtendedTypeBase;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.jetbrains.annotations.Nullable;
import java.io.Serializable;

/** User class for Model: DExtendedType */
public class DExtendedType
    extends DExtendedTypeBase
    implements Serializable
{

    //~ Constructors .............................................................................................................

    /** Constructor for DExtendedType */
    @JsonCreator public DExtendedType(@JsonProperty("id") int id, @JsonProperty("dId") int dId, @JsonProperty("dni") @Nullable Integer dni) { super(id,dId,dni); }

    //~ Fields ...................................................................................................................

    private static final long serialVersionUID = -7953991070566600958L;

}
