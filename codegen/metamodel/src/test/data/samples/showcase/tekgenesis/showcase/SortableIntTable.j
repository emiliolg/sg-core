package tekgenesis.showcase;

import org.jetbrains.annotations.NotNull;

/** User class for form: SortableIntTable */
public class SortableIntTable
    extends SortableIntTableBase
{

    //~ Methods ..................................................................................................................

    /** Invoked when populating a form instance */
    @Override @NotNull public Object populate() { throw new IllegalStateException("To be implemented"); }

    /** Invoked when the form is loaded */
    @Override public void load() { throw new IllegalStateException("To be implemented"); }

    //~ Inner Classes ............................................................................................................

    public class TableRow
        extends TableRowBase
    {

    }
}
