package tekgenesis.showcase;

import tekgenesis.form.Action;
import org.jetbrains.annotations.NotNull;

/** User class for form: DropdownForm */
public class DropdownForm
    extends DropdownFormBase
{

    //~ Methods ..................................................................................................................

    /** 
     * Invoked when label(first) is clicked
     * Invoked when label(second) is clicked
     */
    @Override @NotNull public Action firstClick() { throw new IllegalStateException("To be implemented"); }

    //~ Inner Classes ............................................................................................................

    public class TableRow
        extends TableRowBase
    {

        //~ Methods ..................................................................................................................

        /** 
         * Invoked when button(nr1) is clicked
         * Invoked when label(l1) is clicked
         * Invoked when label(l2) is clicked
         */
        @Override @NotNull public Action any() { throw new IllegalStateException("To be implemented"); }

    }
}
