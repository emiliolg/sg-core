package tekgenesis.showcase;

import org.jetbrains.annotations.NotNull;

/** User class for form: CrazyEventsShowcase */
public class CrazyEventsShowcase
    extends CrazyEventsShowcaseBase
{

    //~ Inner Classes ............................................................................................................

    public class EventsTableRow
        extends EventsTableRowBase
    {

        //~ Methods ..................................................................................................................

        /** Invoked when display(theNumber) value changes */
        @Override @NotNull public Action changeNumber() { throw new IllegalStateException("To be implemented"); }

        /** Invoked when text_field(age) value changes */
        @Override @NotNull public Action changeAge() { throw new IllegalStateException("To be implemented"); }

    }
}
