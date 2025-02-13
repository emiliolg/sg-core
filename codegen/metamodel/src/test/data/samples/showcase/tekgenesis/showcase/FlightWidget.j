package tekgenesis.showcase;

import tekgenesis.form.Action;
import org.jetbrains.annotations.NotNull;

/** User class for widget: FlightWidget */
public abstract class FlightWidget
    extends FlightWidgetBase
{

    //~ Methods ..................................................................................................................

    /** Invoked when button(add) is clicked */
    @Override @NotNull public Action addRow() { throw new IllegalStateException("To be implemented"); }

}
