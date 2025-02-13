package tekgenesis.showcase;

import tekgenesis.form.Action;
import org.jetbrains.annotations.NotNull;

/** User class for widget: CoordinateWidget */
public abstract class CoordinateWidget
    extends CoordinateWidgetBase
{

    //~ Methods ..................................................................................................................

    /** Invoked when text_field(lng) value changes */
    @Override @NotNull public Action lngChanged() { throw new IllegalStateException("To be implemented"); }

}
