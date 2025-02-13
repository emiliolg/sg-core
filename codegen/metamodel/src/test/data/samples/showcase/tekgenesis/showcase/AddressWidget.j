package tekgenesis.showcase;

import tekgenesis.form.Action;
import org.jetbrains.annotations.NotNull;

/** User class for widget: AddressWidget */
public abstract class AddressWidget
    extends AddressWidgetBase
{

    //~ Methods ..................................................................................................................

    /** Invoked when text_field(state) value changes */
    @Override @NotNull public Action stateChanged() { throw new IllegalStateException("To be implemented"); }

}
