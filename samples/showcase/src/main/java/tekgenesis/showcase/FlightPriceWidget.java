
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
 * User class for widget: FlightPriceWidget
 */
public class FlightPriceWidget extends FlightPriceWidgetBase {

    //~ Methods ......................................................................................................................................

    @NotNull @Override public Action buy() {
        return actions().getDefault();
    }

    @NotNull @Override public Action reserve() {
        return actions().getDefault();
    }
}
