package tekgenesis.showcase;

import tekgenesis.form.Action;
import org.jetbrains.annotations.NotNull;

/** User class for widget: AddressTypeWidget */
public abstract class AddressTypeWidget
    extends AddressTypeWidgetBase
{

    //~ Methods ..................................................................................................................

    /** Invoked when text_field(state) value changes */
    @Override @NotNull public Action stateChanged() { throw new IllegalStateException("To be implemented"); }

}
