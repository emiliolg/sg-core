
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
 * User class for Form: DNIForm
 */
public class DNIForm extends DNIFormBase {

    //~ Methods ......................................................................................................................................

    @NotNull @Override public Action manual() {
        manualUpdate();
        return actions.getDefault();
    }

    @NotNull @Override public Action manualUpdating() {
        final DNI dni = manualUpdate();
        forms.lockingTimestamp(dni);
        return actions.getDefault();
    }

    @NotNull private DNI manualUpdate() {
        final DNI dni = find();
        copyTo(dni);
        dni.update();
        return dni;
    }
}
