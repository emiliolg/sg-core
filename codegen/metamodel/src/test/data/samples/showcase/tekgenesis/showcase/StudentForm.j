package tekgenesis.showcase;

import tekgenesis.form.Action;
import org.jetbrains.annotations.NotNull;

/** User class for form: StudentForm */
public class StudentForm
    extends StudentFormBase
{

    //~ Methods ..................................................................................................................

    /** Invoked when the form is loaded */
    @Override public void load() { throw new IllegalStateException("To be implemented"); }

    /** Invoked when label(twitter2) is clicked */
    @Override @NotNull public Action widgetsForm() { throw new IllegalStateException("To be implemented"); }

    //~ Inner Classes ............................................................................................................

    public class TableRow
        extends TableRowBase
    {

    }
}
