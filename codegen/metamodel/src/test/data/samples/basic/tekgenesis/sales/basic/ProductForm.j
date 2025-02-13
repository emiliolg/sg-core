package tekgenesis.sales.basic;

import tekgenesis.form.Action;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/** User class for form: ProductForm */
public class ProductForm
    extends ProductFormBase
{

    //~ Methods ..................................................................................................................

    /** Invoked when the 'Create new' options of the suggest_box(category) is clicked */
    @Override @NotNull public Action createCategory(@Nullable String text) { throw new IllegalStateException("To be implemented"); }

}
