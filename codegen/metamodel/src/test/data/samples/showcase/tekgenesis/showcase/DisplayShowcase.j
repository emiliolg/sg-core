package tekgenesis.showcase;

import tekgenesis.form.Action;
import org.jetbrains.annotations.NotNull;

/** User class for form: DisplayShowcase */
public class DisplayShowcase
    extends DisplayShowcaseBase
{

    //~ Methods ..................................................................................................................

    /** Invoked when button(simpleSync) is clicked */
    @Override @NotNull public Action simpleSync() { throw new IllegalStateException("To be implemented"); }

    /** Invoked when button(button) is clicked */
    @Override @NotNull public Action addRow() { throw new IllegalStateException("To be implemented"); }

    //~ Inner Classes ............................................................................................................

    public class ItemsRow
        extends ItemsRowBase
    {

    }
}
