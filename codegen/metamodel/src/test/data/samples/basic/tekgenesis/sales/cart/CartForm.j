package tekgenesis.sales.cart;

import tekgenesis.form.Action;
import org.jetbrains.annotations.NotNull;

/** User class for form: CartForm */
public class CartForm
    extends CartFormBase
{

    //~ Methods ..................................................................................................................

    /** Invoked when the form is loaded */
    @Override public void load() { throw new IllegalStateException("To be implemented"); }

    /** Invoked when button($B4) is clicked */
    @Override @NotNull public Action checkout() { throw new IllegalStateException("To be implemented"); }

    //~ Inner Classes ............................................................................................................

    public class ItemsRow
        extends ItemsRowBase
    {

        //~ Methods ..................................................................................................................

        /** Invoked when suggest_box(product) value changes */
        @Override @NotNull public Action updateUnitPrice() { throw new IllegalStateException("To be implemented"); }

    }
}
