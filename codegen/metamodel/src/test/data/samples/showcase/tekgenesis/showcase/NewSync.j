package tekgenesis.showcase;

import tekgenesis.form.Action;
import org.jetbrains.annotations.NotNull;

/** User class for form: NewSync */
public class NewSync
    extends NewSyncBase
{

    //~ Methods ..................................................................................................................

    /** Invoked when the form is loaded */
    @Override public void load() { throw new IllegalStateException("To be implemented"); }

    /** Invoked when button(addRow) is clicked */
    @Override @NotNull public Action addClicked() { throw new IllegalStateException("To be implemented"); }

    /** Invoked when button(addMessageFirst) is clicked */
    @Override @NotNull public Action addMessageToFirst() { throw new IllegalStateException("To be implemented"); }

    /** Invoked when button(setOptionsFirst) is clicked */
    @Override @NotNull public Action setOptionsToFirst() { throw new IllegalStateException("To be implemented"); }

    /** Invoked when button(setOptionsGlobal) is clicked */
    @Override @NotNull public Action setOptionsGlobal() { throw new IllegalStateException("To be implemented"); }

    /** Invoked when button(resetFirst) is clicked */
    @Override @NotNull public Action resetFirst() { throw new IllegalStateException("To be implemented"); }

    /** Invoked when button(configFirst) is clicked */
    @Override @NotNull public Action configFirst() { throw new IllegalStateException("To be implemented"); }

    //~ Inner Classes ............................................................................................................

    public class TableRow
        extends TableRowBase
    {

    }
}
