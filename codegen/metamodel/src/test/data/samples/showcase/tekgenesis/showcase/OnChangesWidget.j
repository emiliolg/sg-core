package tekgenesis.showcase;

import tekgenesis.form.Action;
import org.jetbrains.annotations.NotNull;

/** User class for widget: OnChangesWidget */
public abstract class OnChangesWidget
    extends OnChangesWidgetBase
{

    //~ Methods ..................................................................................................................

    /** Invoked when text_field(b) value changes */
    @Override @NotNull public Action bValueChanged() { throw new IllegalStateException("To be implemented"); }

    /** Invoked when text_field(c) value changes */
    @Override @NotNull public Action cValueChanged() { throw new IllegalStateException("To be implemented"); }

}
