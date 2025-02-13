
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
 * User class for Form: AnotherDeprecableEntityForm
 */
@Generated(value = "tekgenesis/showcase/DeprecableShowcase.mm", date = "1369064245097")
@SuppressWarnings("WeakerAccess")
public class AnotherDeprecableEntityForm extends AnotherDeprecableEntityFormBase {

    //~ Methods ......................................................................................................................................

    @NotNull @Override public Action deprecate(boolean status) {
        if (isDefined(Field.NAME) && !getName().equals("test")) return super.deprecate(status);
        return actions.getError().withMessage("No te depreco nada!");
    }
}
