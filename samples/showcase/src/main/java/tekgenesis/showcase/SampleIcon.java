
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

import static tekgenesis.showcase.SampleIconBase.Field.TEXTFIELD;
import static tekgenesis.showcase.SampleIconBase.Field.TF_TOOLTIP;

/**
 * User class for Form: SampleIcon
 */
public class SampleIcon extends SampleIconBase {

    //~ Methods ......................................................................................................................................

    @NotNull @Override public Action longMessage() {
        message(TF_TOOLTIP,
            "Incorrect format<br>NN:year of customs clearance<br>NNN:customs code<br>XXXX:destination<br>NNNNNN:clearance no<br>A: control");
        return actions().getDefault();
    }

    @NotNull @Override public Action shortMessage() {
        message(TEXTFIELD, "Incorrect format.");
        return actions().getDefault();
    }
}
