
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
 * User class for Form: CheckOnChange
 */
public class CheckOnChange extends CheckOnChangeBase {

    //~ Methods ......................................................................................................................................

    /** Invoked when text_field(item) value changes. */
    @NotNull @Override public Action changed() {
        setChanged(true);
        return actions.getDefault();
    }
}
