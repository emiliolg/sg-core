package tekgenesis.sales.basic;

import org.jetbrains.annotations.NotNull;

/** User class for form: CustomerInvoicesForm */
public class CustomerInvoicesForm
    extends CustomerInvoicesFormBase
{

    //~ Inner Classes ............................................................................................................

    public class InvoicesRow
        extends InvoicesRowBase
    {

        //~ Methods ..................................................................................................................

        /** Invoked from {@link #populate(Invoice) method} to handle protected fields. */
        @Override public void populateProtectedFields(@NotNull Invoice invoice) { throw new IllegalStateException("To be implemented"); }

    }
}
