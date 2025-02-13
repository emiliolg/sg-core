package tekgenesis.showcase;

import tekgenesis.form.Action;
import org.jetbrains.annotations.NotNull;

/** User class for form: DynamicFormA */
public class DynamicFormA
    extends DynamicFormABase
{

    //~ Methods ..................................................................................................................

    /** Invoked when text_field(textField) value ui changes */
    @Override @NotNull public Action textFieldChanged() { throw new IllegalStateException("To be implemented"); }

}
