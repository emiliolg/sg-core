package tekgenesis.showcase;

import tekgenesis.form.Action;
import org.jetbrains.annotations.NotNull;

/** User class for widget: BinaryNode */
public abstract class BinaryNode
    extends BinaryNodeBase
{

    //~ Methods ..................................................................................................................

    /** Invoked when toggle_button(button) value ui changes */
    @Override @NotNull public Action toggle() { throw new IllegalStateException("To be implemented"); }

}
