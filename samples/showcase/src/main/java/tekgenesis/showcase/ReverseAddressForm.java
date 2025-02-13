
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
 * User class for Form: ReverseAddressForm
 */
public class ReverseAddressForm extends ReverseAddressFormBase {

    //~ Instance Fields ..............................................................................................................................

    @SuppressWarnings("BooleanVariableAlwaysNegated")
    private boolean populating;

    //~ Methods ......................................................................................................................................

    @NotNull @Override public Address populate() {
        populating = true;
        return super.populate();
    }

    @NotNull @Override public Action resetAboveFields() {
        if (!populating) reset(Field.STATE, Field.CITY, Field.STREET, Field.ZIP);

        if (isDefined(Field.COUNTRY)) {
            final String country = getCountry();
            setCity(country + " default city");
            setState(country + " default state");
        }

        return actions.getDefault();
    }
}
