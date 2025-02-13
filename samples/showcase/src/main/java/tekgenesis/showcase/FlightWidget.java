
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
import tekgenesis.form.FormTable;

/**
 * User class for widget: FlightWidget
 */
public abstract class FlightWidget extends FlightWidgetBase {

    //~ Methods ......................................................................................................................................

    @NotNull @Override public Action addRow() {
        final FormTable<SectionsRow> sections = getSections();
        sections.add().populate(sections.size() - 1);
        return actions().getDefault();
    }

    public abstract Action fromChange(SectionsRow row);

    //~ Inner Classes ................................................................................................................................

    public class SectionsRow extends SectionsRowBase {
        @NotNull @Override public Action fromChanged() {
            return fromChange(this);
        }

        public void populate(int i) {
            setFromSchedule("30" + i).setToSchedule("40" + i);
        }
    }
}
