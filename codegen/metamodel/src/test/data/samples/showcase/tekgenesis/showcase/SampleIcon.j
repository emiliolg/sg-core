package tekgenesis.showcase;

import tekgenesis.form.Action;
import org.jetbrains.annotations.NotNull;

/** User class for form: SampleIcon */
public class SampleIcon
    extends SampleIconBase
{

    //~ Methods ..................................................................................................................

    /** Invoked when text_field(textfield) value ui changes */
    @Override @NotNull public Action shortMessage() { throw new IllegalStateException("To be implemented"); }

    /** Invoked when text_field(tfTooltip) value ui changes */
    @Override @NotNull public Action longMessage() { throw new IllegalStateException("To be implemented"); }

}
