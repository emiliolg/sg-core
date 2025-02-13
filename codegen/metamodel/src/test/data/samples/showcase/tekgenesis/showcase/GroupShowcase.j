package tekgenesis.showcase;

import tekgenesis.form.Action;
import org.jetbrains.annotations.NotNull;

/** User class for form: GroupShowcase */
public class GroupShowcase
    extends GroupShowcaseBase
{

    //~ Methods ..................................................................................................................

    /** Invoked when the form is loaded */
    @Override public void load() { throw new IllegalStateException("To be implemented"); }

    /** Invoked when popover(pop2) value changes */
    @Override @NotNull public Action popChange() { throw new IllegalStateException("To be implemented"); }

    /** Invoked when tabs(tab1) value changes */
    @Override @NotNull public Action tabChanged() { throw new IllegalStateException("To be implemented"); }

    //~ Inner Classes ............................................................................................................

    public class SectRow
        extends SectRowBase
    {

    }
}
