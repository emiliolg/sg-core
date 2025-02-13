package tekgenesis.showcase;

import tekgenesis.form.Action;
import org.jetbrains.annotations.NotNull;

/** User class for form: ItemForm */
public class ItemForm
    extends ItemFormBase
{

    //~ Methods ..................................................................................................................

    /** Invoked when button($B4) is clicked */
    @Override @NotNull public Action prev() { throw new IllegalStateException("To be implemented"); }

    /** Invoked when button($B6) is clicked */
    @Override @NotNull public Action next() { throw new IllegalStateException("To be implemented"); }

}
