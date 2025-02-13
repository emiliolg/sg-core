package tekgenesis.sales.basic;

import org.jetbrains.annotations.NotNull;

/** User class for form: InvoiceReviewForm */
public class InvoiceReviewForm
    extends InvoiceReviewFormBase
{

    //~ Methods ..................................................................................................................

    /** Invoked when the form is loaded */
    @Override public void load() { throw new IllegalStateException("To be implemented"); }

    //~ Inner Classes ............................................................................................................

    public class InvoicesRow
        extends InvoicesRowBase
    {

        //~ Methods ..................................................................................................................

        /** Invoked when display(id) is clicked */
        @Override @NotNull public Action navigate() { throw new IllegalStateException("To be implemented"); }

    }
}
