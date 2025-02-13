package tekgenesis.showcase;

import tekgenesis.form.Action;
import org.jetbrains.annotations.NotNull;

/** User class for form: TableFilter */
public class TableFilter
    extends TableFilterBase
{

    //~ Methods ..................................................................................................................

    /** Invoked when the form is loaded */
    @Override public void load() { throw new IllegalStateException("To be implemented"); }

    /** 
     * Invoked when tags(colors) value changes
     * Invoked when combo_box(lowest) value changes
     * Invoked when combo_box(highest) value changes
     * Invoked when tags(categories) value changes
     */
    @Override @NotNull public Action refresh() { throw new IllegalStateException("To be implemented"); }

    //~ Inner Classes ............................................................................................................

    public class ProductsRow
        extends ProductsRowBase
    {

    }
}
