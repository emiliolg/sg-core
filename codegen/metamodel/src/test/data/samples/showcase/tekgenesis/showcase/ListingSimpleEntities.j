package tekgenesis.showcase;

import tekgenesis.form.Action;
import org.jetbrains.annotations.NotNull;

/** User class for form: ListingSimpleEntities */
public class ListingSimpleEntities
    extends ListingSimpleEntitiesBase
{

    //~ Methods ..................................................................................................................

    /** Invoked when button(reload) is clicked */
    @Override @NotNull public Action reload() { throw new IllegalStateException("To be implemented"); }

    //~ Inner Classes ............................................................................................................

    public class EntitiesRow
        extends EntitiesRowBase
    {

    }
}
