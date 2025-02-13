package tekgenesis.showcase;

import tekgenesis.form.Action;
import org.jetbrains.annotations.NotNull;

/** User class for form: SynchronousForm */
public class SynchronousForm
    extends SynchronousFormBase
{

    //~ Methods ..................................................................................................................

    /** 
     * Invoked when button(click) is clicked
     * Invoked when button(sync) is clicked
     */
    @Override @NotNull public Action clicked() { throw new IllegalStateException("To be implemented"); }

    /** Invoked when button(actionError) is clicked */
    @Override @NotNull public Action error() { throw new IllegalStateException("To be implemented"); }

}
