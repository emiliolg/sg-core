
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
 * User class for Form: ClientForm
 */
@Generated(value = "tekgenesis/showcase/SubformsShowcase.mm", date = "1387809770150")
@SuppressWarnings("WeakerAccess")
public class ClientForm extends ClientFormBase {

    //~ Methods ......................................................................................................................................

    @NotNull @Override public Action some() {
        return actions.navigate(AddressesForm.class);
    }
}
