package tekgenesis.sales.cart;

import tekgenesis.form.Action;
import org.jetbrains.annotations.NotNull;

/** User class for form: AddToCart */
public class AddToCart
    extends AddToCartBase
{

    //~ Methods ..................................................................................................................

    /** Invoked when suggest_box(product) value changes */
    @Override @NotNull public Action productChanged() { throw new IllegalStateException("To be implemented"); }

    /** Invoked when button($B2) is clicked */
    @Override @NotNull public Action addClicked() { throw new IllegalStateException("To be implemented"); }

}
