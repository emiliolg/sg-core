package tekgenesis.showcase;

import tekgenesis.form.Action;
import org.jetbrains.annotations.NotNull;

/** User class for form: SubformsShowcase */
public class SubformsShowcase
    extends SubformsShowcaseBase
{

    //~ Methods ..................................................................................................................

    /** Invoked when the form is loaded */
    @Override public void load() { throw new IllegalStateException("To be implemented"); }

    /** Invoked when button(syncSubForm) is clicked */
    @Override @NotNull public Action changeSubForm() { throw new IllegalStateException("To be implemented"); }

    //~ Inner Classes ............................................................................................................

    public class TableRow
        extends TableRowBase
    {

        //~ Methods ..................................................................................................................

        /** Invoked when button($B5) is clicked */
        @Override @NotNull public Action hideSubform() { throw new IllegalStateException("To be implemented"); }

    }
}
