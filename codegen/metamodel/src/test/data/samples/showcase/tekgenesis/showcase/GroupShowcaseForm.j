package tekgenesis.showcase;

import tekgenesis.form.Action;
import org.jetbrains.annotations.NotNull;

/** User class for form: GroupShowcaseForm */
public class GroupShowcaseForm
    extends GroupShowcaseFormBase
{

    //~ Methods ..................................................................................................................

    /** Invoked when the form is loaded */
    @Override public void load() { throw new IllegalStateException("To be implemented"); }

    /** Invoked when display($D51) is clicked */
    @Override @NotNull public Action doStuff() { throw new IllegalStateException("To be implemented"); }

    //~ Inner Classes ............................................................................................................

    public class TableRow
        extends TableRowBase
    {

    }

    public class BigTableRow
        extends BigTableRowBase
    {

    }
}
