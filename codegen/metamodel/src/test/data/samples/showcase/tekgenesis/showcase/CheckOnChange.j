package tekgenesis.showcase;

import tekgenesis.form.Action;
import org.jetbrains.annotations.NotNull;

/** User class for form: CheckOnChange */
public class CheckOnChange
    extends CheckOnChangeBase
{

    //~ Methods ..................................................................................................................

    /** Invoked when text_field(item) value changes */
    @Override @NotNull public Action changed() { throw new IllegalStateException("To be implemented"); }

}
