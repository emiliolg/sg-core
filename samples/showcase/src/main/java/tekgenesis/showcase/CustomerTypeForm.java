
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
 * User class for form: CustomerTypeForm
 */
public class CustomerTypeForm extends CustomerTypeFormBase {

    //~ Methods ......................................................................................................................................

    @NotNull @Override public Action reateWorkAddress() {
        getWorkAddress().create();
        return actions().getDefault();
    }

    @NotNull @Override AddressTypeWidget defineHomeAddress() {
        return new HomeAddressTypeWidget();
    }

    @NotNull @Override AddressTypeWidget defineWorkAddress() {
        return new WorkAddressTypeWidget();
    }

    //~ Inner Classes ................................................................................................................................

    private class HomeAddressTypeWidget extends AddressTypeWidget {
        @Override Action stateChangedAction() {
            setFeedback("homeChanged");
            return actions().getDefault();
        }
    }

    private class WorkAddressTypeWidget extends AddressTypeWidget {
        @Override Action stateChangedAction() {
            setFeedback("workChanged");
            return actions().getDefault();
        }
    }
}
