package tekgenesis.showcase;

import tekgenesis.form.Action;
import org.jetbrains.annotations.NotNull;

/** User class for form: DisabledForm */
public class DisabledForm
    extends DisabledFormBase
{

    //~ Methods ..................................................................................................................

    /** 
     * Invoked when label(mainAction) is clicked
     * Invoked when label(someAction) is clicked
     */
    @Override @NotNull public Action mainAction() { throw new IllegalStateException("To be implemented"); }

    /** Invoked when label(secondaryAction) is clicked */
    @Override @NotNull public Action secondaryAction() { throw new IllegalStateException("To be implemented"); }

    /** Invoked when button(click) is clicked */
    @Override @NotNull public Action clicked() { throw new IllegalStateException("To be implemented"); }

}
