
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.showcase;

import org.jetbrains.annotations.NotNull;

import tekgenesis.form.Action;

/**
 * User class for form: CustomerForm
 */
public class CustomerForm extends CustomerFormBase {

    //~ Methods ......................................................................................................................................

    @NotNull @Override public AddressWidget defineHomeAddress() {
        return new HomeAddressWidget();
    }

    @NotNull @Override public AddressWidget defineWorkAddress() {
        return new WorkAddressWidget();
    }

    //~ Inner Classes ................................................................................................................................

    private class HomeAddressWidget extends AddressWidget {
        @Override Action stateChangedAction() {
            setFeedback("homeChanged");
            return actions().getDefault();
        }
    }

    private class WorkAddressWidget extends AddressWidget {
        @Override Action stateChangedAction() {
            setFeedback("workChanged");
            return actions().getDefault();
        }
    }
}
