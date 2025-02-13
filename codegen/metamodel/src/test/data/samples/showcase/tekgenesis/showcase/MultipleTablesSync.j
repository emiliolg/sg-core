package tekgenesis.showcase;

import tekgenesis.form.Action;
import org.jetbrains.annotations.NotNull;

/** User class for form: MultipleTablesSync */
public class MultipleTablesSync
    extends MultipleTablesSyncBase
{

    //~ Methods ..................................................................................................................

    /** Invoked when text_field(rows) value changes */
    @Override @NotNull public Action loadTables() { throw new IllegalStateException("To be implemented"); }

    //~ Inner Classes ............................................................................................................

    public class PendingsRow
        extends PendingsRowBase
    {

        //~ Methods ..................................................................................................................

        /** Invoked when button(print) is clicked */
        @Override @NotNull public Action print() { throw new IllegalStateException("To be implemented"); }

        /** Invoked when button(download) is clicked */
        @Override @NotNull public Action export() { throw new IllegalStateException("To be implemented"); }

    }
}
