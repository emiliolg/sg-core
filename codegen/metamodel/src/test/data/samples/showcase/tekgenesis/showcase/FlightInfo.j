package tekgenesis.showcase;

import tekgenesis.form.Action;
import org.jetbrains.annotations.NotNull;

/** User class for form: FlightInfo */
public class FlightInfo
    extends FlightInfoBase
{

    //~ Methods ..................................................................................................................

    /** Invoked when the form is loaded */
    @Override public void load() { throw new IllegalStateException("To be implemented"); }

    /** Invoked when internal(tax) value changes */
    @Override @NotNull public Action taxChange() { throw new IllegalStateException("To be implemented"); }

    //~ Inner Classes ............................................................................................................

    public class FlightsTableRow
        extends FlightsTableRowBase
    {

    }

    public class PriceTableRow
        extends PriceTableRowBase
    {

    }
}
