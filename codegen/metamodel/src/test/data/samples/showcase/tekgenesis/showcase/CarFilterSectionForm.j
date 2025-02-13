package tekgenesis.showcase;

import tekgenesis.form.Action;
import org.jetbrains.annotations.NotNull;

/** User class for form: CarFilterSectionForm */
public class CarFilterSectionForm
    extends CarFilterSectionFormBase
{

    //~ Methods ..................................................................................................................

    /** Invoked when the form is loaded */
    @Override public void load() { throw new IllegalStateException("To be implemented"); }

    /** Invoked when button(filter) is clicked */
    @Override @NotNull public Action filter() { throw new IllegalStateException("To be implemented"); }

    //~ Inner Classes ............................................................................................................

    public class FiltersRow
        extends FiltersRowBase
    {

    }

    public class CarsRow
        extends CarsRowBase
    {

    }
}
