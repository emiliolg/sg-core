package tekgenesis.showcase;

import tekgenesis.form.Action;
import org.jetbrains.annotations.NotNull;

/** User class for form: SimpleEntitiesList */
public class SimpleEntitiesList
    extends SimpleEntitiesListBase
{

    //~ Methods ..................................................................................................................

    /** Invoked when button(add) is clicked */
    @Override @NotNull public Action createNewOne() { throw new IllegalStateException("To be implemented"); }

    //~ Inner Classes ............................................................................................................

    public class EntitiesRow
        extends EntitiesRowBase
    {

    }
}
