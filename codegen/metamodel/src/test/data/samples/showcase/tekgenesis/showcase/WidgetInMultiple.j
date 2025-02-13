package tekgenesis.showcase;

import tekgenesis.form.Action;
import org.jetbrains.annotations.NotNull;

/** User class for form: WidgetInMultiple */
public class WidgetInMultiple
    extends WidgetInMultipleBase
{

    //~ Methods ..................................................................................................................

    /** Invoked when button(add) is clicked */
    @Override @NotNull public Action add() { throw new IllegalStateException("To be implemented"); }

    //~ Inner Classes ............................................................................................................

    public class WidgetsRow
        extends WidgetsRowBase
    {

    }
}
