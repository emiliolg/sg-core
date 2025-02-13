package tekgenesis.showcase;

import tekgenesis.form.Action;
import org.jetbrains.annotations.NotNull;

/** User class for form: DynamicForm */
public class DynamicForm
    extends DynamicFormBase
{

    //~ Methods ..................................................................................................................

    /** Invoked when button(button) is clicked */
    @Override @NotNull public Action changeMailType() { throw new IllegalStateException("To be implemented"); }

    /** Invoked when dynamic(range) value changes */
    @Override @NotNull public Action rangeChanged() { throw new IllegalStateException("To be implemented"); }

    /** Invoked when combo_box(combo) value changes */
    @Override @NotNull public Action updateDyn() { throw new IllegalStateException("To be implemented"); }

    /** Invoked when button($B7) is clicked */
    @Override @NotNull public Action onValidate() { throw new IllegalStateException("To be implemented"); }

    //~ Inner Classes ............................................................................................................

    public class WidgetsRow
        extends WidgetsRowBase
    {

        //~ Methods ..................................................................................................................

        /** Invoked when combo_box(property) value changes */
        @Override @NotNull public Action updateDynamic() { throw new IllegalStateException("To be implemented"); }

        /** Invoked when button(clear) is clicked */
        @Override @NotNull public Action clear() { throw new IllegalStateException("To be implemented"); }

    }
}
