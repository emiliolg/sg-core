
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
 * User class for widget: FlightChooserWidget
 */
public class FlightChooserWidget extends FlightChooserWidgetBase {

    //~ Methods ......................................................................................................................................

    @NotNull @Override public Action search() {
        return actions().getDefault();
    }

    //~ Inner Classes ................................................................................................................................

    public class OptionsRow extends OptionsRowBase {}
}
