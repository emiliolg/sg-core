package tekgenesis.showcase;

import tekgenesis.form.Action;
import org.jetbrains.annotations.NotNull;

/** User class for form: TableImageForm */
public class TableImageForm
    extends TableImageFormBase
{

    //~ Methods ..................................................................................................................

    /** Invoked when button(clear) is clicked */
    @Override @NotNull public Action clear() { throw new IllegalStateException("To be implemented"); }

    //~ Inner Classes ............................................................................................................

    public class ImgsRow
        extends ImgsRowBase
    {

    }
}
