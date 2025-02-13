package tekgenesis.showcase;

import tekgenesis.form.Action;
import org.jetbrains.annotations.NotNull;

/** User class for form: InlineSubformShowcase */
public class InlineSubformShowcase
    extends InlineSubformShowcaseBase
{

    //~ Methods ..................................................................................................................

    /** Invoked when the form is loaded */
    @Override public void load() { throw new IllegalStateException("To be implemented"); }

    /** Invoked when text_field(flightTax) value changes */
    @Override @NotNull public Action taxChange() { throw new IllegalStateException("To be implemented"); }

    //~ Inner Classes ............................................................................................................

    public class FlightsRow
        extends FlightsRowBase
    {

    }
}
