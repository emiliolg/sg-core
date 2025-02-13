package tekgenesis.showcase;

import tekgenesis.form.Action;
import org.jetbrains.annotations.NotNull;

/** User class for form: FilterableTable */
public class FilterableTable
    extends FilterableTableBase
{

    //~ Methods ..................................................................................................................

    /** Invoked when the form is loaded */
    @Override public void load() { throw new IllegalStateException("To be implemented"); }

    /** Invoked when button(add10) is clicked */
    @Override @NotNull public Action add10() { throw new IllegalStateException("To be implemented"); }

    /** Invoked when button(remove10) is clicked */
    @Override @NotNull public Action remove10() { throw new IllegalStateException("To be implemented"); }

    /** Invoked when button(nextPage) is clicked */
    @Override @NotNull public Action nextPage() { throw new IllegalStateException("To be implemented"); }

    //~ Inner Classes ............................................................................................................

    public class ItemsRow
        extends ItemsRowBase
    {

    }
}
