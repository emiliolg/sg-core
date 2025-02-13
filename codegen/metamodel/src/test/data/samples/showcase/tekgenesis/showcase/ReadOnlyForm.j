package tekgenesis.showcase;

import tekgenesis.form.Action;
import org.jetbrains.annotations.NotNull;

/** User class for form: ReadOnlyForm */
public class ReadOnlyForm
    extends ReadOnlyFormBase
{

    //~ Methods ..................................................................................................................

    /** 
     * Invoked when button(toggle) is clicked
     * Invoked when button(cancel) is clicked
     */
    @Override @NotNull public Action readOnly() { throw new IllegalStateException("To be implemented"); }

    /** Invoked when button(sync) is clicked */
    @Override @NotNull public Action otherSync() { throw new IllegalStateException("To be implemented"); }

}
