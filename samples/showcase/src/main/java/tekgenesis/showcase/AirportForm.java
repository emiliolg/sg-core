
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
 * User class for form: AirportForm
 */
public class AirportForm extends AirportFormBase {

    //~ Methods ......................................................................................................................................

    @Override public void load() {
        getSections().add();
        getSections().add();
        getSections().add();
        getSections().add();
    }

    //~ Inner Classes ................................................................................................................................

    public class SectionsRow extends SectionsRowBase {
        @NotNull @Override FlightWidget defineFlight() {
            return new FlightWidget() {
                @Override public Action fromChange(SectionsRow row) {
                    return actions().getDefault().withMessage("Row changed " + row.getFromSchedule());
                }
            };
        }
    }
}
