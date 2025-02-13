package tekgenesis.showcase;

import org.jetbrains.annotations.NotNull;

/** User class for form: AddressesForm */
public class AddressesForm
    extends AddressesFormBase
{

    //~ Inner Classes ............................................................................................................

    public class ClientAddressesRow
        extends ClientAddressesRowBase
    {

        //~ Methods ..................................................................................................................

        /** Invoked when text_field(country) value changes */
        @Override @NotNull public Action resetState() { throw new IllegalStateException("To be implemented"); }

    }
}
