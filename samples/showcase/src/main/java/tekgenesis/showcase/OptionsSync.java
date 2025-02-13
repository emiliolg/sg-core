
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

import static tekgenesis.common.collections.Colls.listOf;
import static tekgenesis.showcase.Options.*;

/**
 * User class for form: OptionsSync
 */
public class OptionsSync extends OptionsSyncBase {

    //~ Methods ......................................................................................................................................

    /** Invoked when button(changeOptions) is clicked. */
    @NotNull @Override public Action changeOptions() {
        setOptionsOptions(listOf(OPTION1, OPTION3, OPTION5));
        return actions.getDefault();
    }

    @Override public void load() {
        setOptionsOptions(listOf(OPTION2, OPTION4, OPTION6));
    }

    @NotNull @Override public Action otherAction() {
        System.out.println("OptionsSync.otherAction");
        return actions.getDefault();
    }
}
