
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
 * User class for widget: CoordinateWidget
 */
public abstract class CoordinateWidget extends CoordinateWidgetBase {

    //~ Methods ......................................................................................................................................

    @NotNull @Override public Action lngChanged() {
        return actions.getDefault().withMessage("Change number : " + getCounter());
    }

    abstract int getCounter();

    // @NotNull @Override public abstract Action locate();
}
