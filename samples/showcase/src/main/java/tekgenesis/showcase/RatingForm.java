
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

import tekgenesis.common.collections.Colls;
import tekgenesis.form.Action;

/**
 * User class for form: RatingForm
 */
public class RatingForm extends RatingFormBase {

    //~ Methods ......................................................................................................................................

    @Override public void load() {
        setStarsOptions(Colls.listOf(1, 2, 3, 4));
        setHeartsOptions(Colls.listOf(1, 2, 3, 4));
        setHearts(3);
    }

    @NotNull @Override public Action sync() {
        return actions.getDefault().withMessage("Stars: " + getStars() + " , Hearts: " + getHearts());
    }
}
