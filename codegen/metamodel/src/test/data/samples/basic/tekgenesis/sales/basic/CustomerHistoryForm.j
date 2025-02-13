package tekgenesis.sales.basic;

import tekgenesis.form.Action;
import org.jetbrains.annotations.NotNull;

/** User class for form: CustomerHistoryForm */
public class CustomerHistoryForm
    extends CustomerHistoryFormBase
{

    //~ Methods ..................................................................................................................

    /** Invoked when label(edit) on subform(invoices) is clicked */
    @Override @NotNull public Action editInvoice() { throw new IllegalStateException("To be implemented"); }

}
