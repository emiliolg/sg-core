package tekgenesis.showcase;

import tekgenesis.form.Action;
import org.jetbrains.annotations.NotNull;

/** User class for form: CarFilterForm */
public class CarFilterForm
    extends CarFilterFormBase
{

    //~ Methods ..................................................................................................................

    /** Invoked when the form is loaded */
    @Override public void load() { throw new IllegalStateException("To be implemented"); }

    /** Invoked when button(filter) is clicked */
    @Override @NotNull public Action filter() { throw new IllegalStateException("To be implemented"); }

    /** Invoked when table(cars) is clicked */
    @Override @NotNull public Action rowClicked() { throw new IllegalStateException("To be implemented"); }

    /** Invoked when button($B3) is clicked */
    @Override @NotNull public Action navigate() { throw new IllegalStateException("To be implemented"); }

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
