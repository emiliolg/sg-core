package tekgenesis.showcase;

import org.jetbrains.annotations.NotNull;

/** User class for form: ExportTable */
public class ExportTable
    extends ExportTableBase
{

    //~ Methods ..................................................................................................................

    /** Invoked when the form is loaded */
    @Override public void load() { throw new IllegalStateException("To be implemented"); }

    //~ Inner Classes ............................................................................................................

    public class OrdersRow
        extends OrdersRowBase
    {

        //~ Methods ..................................................................................................................

        /** Invoked when button(iconInTable) is clicked */
        @Override @NotNull public Action clicked() { throw new IllegalStateException("To be implemented"); }

    }
}
