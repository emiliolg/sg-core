
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.showcase;

import javax.annotation.Generated;

import org.jetbrains.annotations.NotNull;

import tekgenesis.form.FormTable;

/**
 * User class for Form: Flights
 */
@Generated(value = "tekgenesis/showcase/ComplexSubformShowcase.mm", date = "1361215217763")
@SuppressWarnings("WeakerAccess")
public class Flights extends FlightsBase {

    //~ Inner Classes ................................................................................................................................

    public class FlightsRow extends FlightsRowBase {
        @Override public void populate(@NotNull Flight flight) {
            super.populate(flight);

            final FlightForm flightSubform = createFlightSubform();
            flightSubform.setDisplaying(true);

            flightSubform.setId(getId());
            flightSubform.setName(getName());
            flightSubform.setFrom(getFrom());
            flightSubform.setTo(getTo());
            flightSubform.setPrice(getPrice());

            final FormTable<FlightForm.SectionsRow> sections = flightSubform.getSections();
            for (int i = 0; i < 3; i++) {
                final FlightForm.SectionsRow row = sections.add();
                row.setSectionFrom("Blah " + i);
                row.setSectionTo("Blah " + i);
            }
        }
    }
}
