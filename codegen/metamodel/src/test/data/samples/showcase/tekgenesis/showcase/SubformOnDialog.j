package tekgenesis.showcase;

import tekgenesis.form.Action;
import org.jetbrains.annotations.NotNull;

/** User class for form: SubformOnDialog */
public class SubformOnDialog
    extends SubformOnDialogBase
{

    //~ Methods ..................................................................................................................

    /** Invoked when the form is loaded */
    @Override public void load() { throw new IllegalStateException("To be implemented"); }

    /** Invoked when button(doMergeReception) is clicked */
    @Override @NotNull public Action doMergeReception() { throw new IllegalStateException("To be implemented"); }

    /** Invoked when button(doShowMergeReception) is clicked */
    @Override @NotNull public Action doShowMergeReception() { throw new IllegalStateException("To be implemented"); }

    //~ Inner Classes ............................................................................................................

    public class TableInDialogRow
        extends TableInDialogRowBase
    {

    }
}
