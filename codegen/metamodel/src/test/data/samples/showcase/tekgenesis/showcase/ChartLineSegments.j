package tekgenesis.showcase;

import tekgenesis.form.Action;
import org.jetbrains.annotations.NotNull;

/** User class for form: ChartLineSegments */
public class ChartLineSegments
    extends ChartLineSegmentsBase
{

    //~ Methods ..................................................................................................................

    /** Invoked when the form is loaded */
    @Override public void load() { throw new IllegalStateException("To be implemented"); }

    /** Invoked when chart(segments) is clicked */
    @Override @NotNull public Action doClick(@NotNull Field field) { throw new IllegalStateException("To be implemented"); }

    //~ Inner Classes ............................................................................................................

    public class SegmentsRow
        extends SegmentsRowBase
    {

    }
}
