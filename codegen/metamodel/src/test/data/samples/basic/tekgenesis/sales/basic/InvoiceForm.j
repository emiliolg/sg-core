package tekgenesis.sales.basic;

import tekgenesis.form.Action;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/** User class for form: InvoiceForm */
public class InvoiceForm
    extends InvoiceFormBase
{

    //~ Methods ..................................................................................................................

    /** Invoked when the 'Create new' options of the suggest_box(customer) is clicked */
    @Override @NotNull public Action createCustomer(@Nullable String text) { throw new IllegalStateException("To be implemented"); }

    /** Invoked when suggest_box(customer) value changes */
    @Override @NotNull public Action updateCustomer() { throw new IllegalStateException("To be implemented"); }

    //~ Inner Classes ............................................................................................................

    public class ItemsRow
        extends ItemsRowBase
    {

        //~ Methods ..................................................................................................................

        /** Invoked when the 'Create new' options of the suggest_box(product) is clicked */
        @Override @NotNull public Action createProduct(@Nullable String text) { throw new IllegalStateException("To be implemented"); }

        /** Invoked when suggest_box(product) value changes */
        @Override @NotNull public Action updateUnitPrice() { throw new IllegalStateException("To be implemented"); }

    }

    public class PaymentsRow
        extends PaymentsRowBase
    {

        //~ Methods ..................................................................................................................

        /** Invoked when suggest_box(type) value changes */
        @Override @NotNull public Action updatePayments() { throw new IllegalStateException("To be implemented"); }

    }
}
