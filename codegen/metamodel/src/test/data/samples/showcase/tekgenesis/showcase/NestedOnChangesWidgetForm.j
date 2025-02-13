package tekgenesis.showcase;

import tekgenesis.form.Action;
import org.jetbrains.annotations.NotNull;

/** User class for form: NestedOnChangesWidgetForm */
public class NestedOnChangesWidgetForm
    extends NestedOnChangesWidgetFormBase
{

    //~ Methods ..................................................................................................................

    /** Invoked when text_field(sum) value changes */
    @Override @NotNull public Action sumChanged() { throw new IllegalStateException("To be implemented"); }

    //~ Inner Classes ............................................................................................................

    public class WidgetsRow
        extends WidgetsRowBase
    {

        //~ Methods ..................................................................................................................

        /** Invoked when text_field(f) value changes */
        @Override @NotNull public Action fValueChanged() { throw new IllegalStateException("To be implemented"); }

    }
}
