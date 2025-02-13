package tekgenesis.showcase;

import tekgenesis.form.Action;
import org.jetbrains.annotations.NotNull;

/** User class for form: DNIForm */
public class DNIForm
    extends DNIFormBase
{

    //~ Methods ..................................................................................................................

    /** Invoked when button(manual) is clicked */
    @Override @NotNull public Action manual() { throw new IllegalStateException("To be implemented"); }

    /** Invoked when button(manualUpdating) is clicked */
    @Override @NotNull public Action manualUpdating() { throw new IllegalStateException("To be implemented"); }

}
