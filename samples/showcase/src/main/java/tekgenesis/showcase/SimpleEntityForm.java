
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
 * Simple Entity Form class.
 */
@SuppressWarnings("WeakerAccess")
public class SimpleEntityForm extends SimpleEntityFormBase {

    //~ Methods ......................................................................................................................................

    @NotNull @Override public Action create() {
        if ("Martin".equals(getName())) {
            message(Field.NAME, "Nombre no disponible");
            message(Field.DESCRIPTION, "Descripción no es correcta");
            return actions.getError().withMessage("Mensaje de error").withSummary();
                   // return actions.getError().withSummary();
                   // return actions.getError();
        }
        return super.create();
    }
}
