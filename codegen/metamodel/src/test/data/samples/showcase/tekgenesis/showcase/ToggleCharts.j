package tekgenesis.showcase;

import tekgenesis.form.Action;
import org.jetbrains.annotations.NotNull;

/** User class for form: ToggleCharts */
public class ToggleCharts
    extends ToggleChartsBase
{

    //~ Methods ..................................................................................................................

    /** Invoked when button(search) is clicked */
    @Override @NotNull public Action search() { throw new IllegalStateException("To be implemented"); }

    //~ Inner Classes ............................................................................................................

    public class SalesByStoreChartRow
        extends SalesByStoreChartRowBase
    {

    }

    public class SalesByCategoryChartRow
        extends SalesByCategoryChartRowBase
    {

    }
}
