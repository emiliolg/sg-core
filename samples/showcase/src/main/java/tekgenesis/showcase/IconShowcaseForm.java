
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
 * User class for Form: IconShowcaseForm
 */
public class IconShowcaseForm extends IconShowcaseFormBase {

    //~ Methods ......................................................................................................................................

    @NotNull @Override public Action changeAll() {
        setChange(!isChange());
        return actions.getDefault();
    }
}
