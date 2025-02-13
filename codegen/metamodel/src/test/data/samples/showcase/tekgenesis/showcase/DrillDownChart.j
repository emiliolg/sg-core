package tekgenesis.showcase;

import tekgenesis.form.Action;
import org.jetbrains.annotations.NotNull;

/** User class for form: DrillDownChart */
public class DrillDownChart
    extends DrillDownChartBase
{

    //~ Methods ..................................................................................................................

    /** Invoked when button(back) is clicked */
    @Override @NotNull public Action back() { throw new IllegalStateException("To be implemented"); }

    /** Invoked when chart(column) is clicked */
    @Override @NotNull public Action drilldown(@NotNull Field field) { throw new IllegalStateException("To be implemented"); }

    //~ Inner Classes ............................................................................................................

    public class ColumnRow
        extends ColumnRowBase
    {

    }
}
