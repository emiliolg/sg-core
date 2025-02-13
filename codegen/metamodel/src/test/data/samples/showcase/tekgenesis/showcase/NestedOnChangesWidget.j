package tekgenesis.showcase;

import tekgenesis.form.Action;
import org.jetbrains.annotations.NotNull;

/** User class for widget: NestedOnChangesWidget */
public abstract class NestedOnChangesWidget
    extends NestedOnChangesWidgetBase
{

    //~ Methods ..................................................................................................................

    /** Invoked when text_field(e) value changes */
    @Override @NotNull public Action eValueChanged() { throw new IllegalStateException("To be implemented"); }

}
