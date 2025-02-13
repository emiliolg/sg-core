
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.showcase;

import javax.annotation.Generated;

import org.jetbrains.annotations.NotNull;

import tekgenesis.form.Action;

/**
 * User class for Form: AddressesForm
 */
@Generated(value = "tekgenesis/showcase/SubformsShowcase.mm", date = "1368470209137")
public class AddressesForm extends AddressesFormBase {

    //~ Instance Fields ..............................................................................................................................

    private boolean populating;

    //~ Methods ......................................................................................................................................

    @NotNull @Override public Addresses populate() {
        populating = true;
        return super.populate();
    }

    //~ Inner Classes ................................................................................................................................

    public class ClientAddressesRow extends ClientAddressesRowBase {
        /** Invoked when text_field(country) value changes. */
        @NotNull @Override public Action resetState() {
            if (isDefined(Field.STATE) && !populating) reset(Field.STATE);
            return actions.getDefault();
        }
    }
}
