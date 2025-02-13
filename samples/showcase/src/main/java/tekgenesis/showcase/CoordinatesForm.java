
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
 * User class for form: CoordinatesForm
 */
public class CoordinatesForm extends CoordinatesFormBase {

    //~ Methods ......................................................................................................................................

    @NotNull @Override CoordinateWidget defineFrom() {
        return new CoordinateWidget() {
            // @NotNull @Override public Action locate() {
            // return getAction("From coordinate locate.");
            // }
            @Override int getCounter() {
                setFromCount(getFromCount() + 1);
                return getFromCount();
            }
        };
    }

    @NotNull @Override CoordinateWidget defineTo() {
        return new CoordinateWidget() {
            @Override int getCounter() {
                setToCount(getToCount() + 1);
                return getToCount();
            }
            // @NotNull @Override public Action locate() {
            // return getAction("From coordinate locate.");
            // }
        };
    }

    // @NotNull private Action getAction(@NotNull final String message) {
    // return actions().getDefault().withMessage(message);
    // }
}
