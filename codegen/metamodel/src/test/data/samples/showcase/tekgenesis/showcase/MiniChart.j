package tekgenesis.showcase;

import tekgenesis.form.Action;
import org.jetbrains.annotations.NotNull;

/** User class for form: MiniChart */
public class MiniChart
    extends MiniChartBase
{

    //~ Methods ..................................................................................................................

    /** Invoked when the form is loaded */
    @Override public void load() { throw new IllegalStateException("To be implemented"); }

    /** Invoked when scheduled interval is triggered */
    @Override @NotNull public Action refresh() { throw new IllegalStateException("To be implemented"); }

    //~ Inner Classes ............................................................................................................

    public class SessionsRow
        extends SessionsRowBase
    {

    }
}
