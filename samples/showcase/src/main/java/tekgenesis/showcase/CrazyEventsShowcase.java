
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
 * User class for Form: CrazyEventsShowcase
 */
public class CrazyEventsShowcase extends CrazyEventsShowcaseBase {

    //~ Inner Classes ................................................................................................................................

    public class EventsTableRow extends EventsTableRowBase {
        /** Invoked when text_field(age) value changes. */
        @NotNull @Override public Action changeAge() {
            final EventsTableRow lastRow = getEventsTable().get(getEventsTable().size() - 1);
            lastRow.setTheNumber(lastRow.getTheNumber() + 1);
            return actions.getDefault();
        }

        @NotNull @Override public Action changeNumber() {
            final int index = getEventsTable().indexOf(this) - 1;
            if (index >= 0) {
                final EventsTableRow previous = getEventsTable().get(index);
                previous.setTheNumber(previous.getTheNumber() + 1);
            }
            return actions.getDefault();
        }
    }
}
