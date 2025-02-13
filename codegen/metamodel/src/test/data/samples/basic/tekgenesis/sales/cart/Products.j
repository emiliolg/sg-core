package tekgenesis.sales.cart;

import org.jetbrains.annotations.NotNull;

/** User class for form: Products */
public class Products
    extends ProductsBase
{

    //~ Methods ..................................................................................................................

    /** Invoked when the form is loaded */
    @Override public void load() { throw new IllegalStateException("To be implemented"); }

    //~ Inner Classes ............................................................................................................

    public class ProductsRow
        extends ProductsRowBase
    {

        //~ Methods ..................................................................................................................

        /** Invoked when button(goTo) is clicked */
        @Override @NotNull public Action goToProduct() { throw new IllegalStateException("To be implemented"); }

    }
}
