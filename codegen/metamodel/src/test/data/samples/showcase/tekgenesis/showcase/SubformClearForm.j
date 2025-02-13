package tekgenesis.showcase;

import tekgenesis.form.Action;
import org.jetbrains.annotations.NotNull;

/** User class for form: SubformClearForm */
public class SubformClearForm
    extends SubformClearFormBase
{

    //~ Methods ..................................................................................................................

    /** Invoked when the form is loaded */
    @Override public void load() { throw new IllegalStateException("To be implemented"); }

    /** Invoked when button(clearAndAdd) is clicked */
    @Override @NotNull public Action clearAndAdd() { throw new IllegalStateException("To be implemented"); }

    /** Invoked when button(clear) is clicked */
    @Override @NotNull public Action clear() { throw new IllegalStateException("To be implemented"); }

    /** Invoked when button(justAdd) is clicked */
    @Override @NotNull public Action justAdd() { throw new IllegalStateException("To be implemented"); }

    /** Invoked when button(changeFirst) is clicked */
    @Override @NotNull public Action changeFirst() { throw new IllegalStateException("To be implemented"); }

    //~ Inner Classes ............................................................................................................

    public class SomeSectionRow
        extends SomeSectionRowBase
    {

    }
}
