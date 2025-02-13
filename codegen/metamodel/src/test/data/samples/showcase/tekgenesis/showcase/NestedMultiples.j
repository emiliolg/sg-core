package tekgenesis.showcase;

import tekgenesis.form.Action;
import org.jetbrains.annotations.NotNull;

/** User class for form: NestedMultiples */
public class NestedMultiples
    extends NestedMultiplesBase
{

    //~ Methods ..................................................................................................................

    /** Invoked when button(simple) is clicked */
    @Override @NotNull public Action addSimple() { throw new IllegalStateException("To be implemented"); }

    /** Invoked when button(complex) is clicked */
    @Override @NotNull public Action addComplex() { throw new IllegalStateException("To be implemented"); }

    //~ Inner Classes ............................................................................................................

    public class WidgetsRow
        extends WidgetsRowBase
    {

    }
}
