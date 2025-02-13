package tekgenesis.test;

import tekgenesis.test.g.ArgumentsTypeBase;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.jetbrains.annotations.NotNull;
import java.io.Serializable;

/** User class for Model: ArgumentsType */
public class ArgumentsType
    extends ArgumentsTypeBase
    implements Serializable
{

    //~ Constructors .............................................................................................................

    /** Constructor for ArgumentsType */
    @JsonCreator public ArgumentsType(@JsonProperty("name") @NotNull String name, @JsonProperty("last") @NotNull String last) { super(name,last); }

    //~ Fields ...................................................................................................................

    private static final long serialVersionUID = -5358137238196858427L;

}
