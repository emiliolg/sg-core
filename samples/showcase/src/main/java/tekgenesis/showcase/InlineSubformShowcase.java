
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.showcase;

import java.math.BigDecimal;

import org.jetbrains.annotations.NotNull;

import tekgenesis.form.Action;

/**
 * User class for Form: InlineSubformShowcase
 */
public class InlineSubformShowcase extends InlineSubformShowcaseBase {

    //~ Methods ......................................................................................................................................

    @Override public void load() {
        setFlightTax(BigDecimal.valueOf(INITIAL_TAX));

        loadFlights();
    }

    @NotNull @Override public Action taxChange() {
        for (FlightsRow flightsRow : getFlights()) {
            if (flightsRow.getFlight() != null) flightsRow.getFlight().setTax(getFlightTax());
        }
        return actions.getDefault();
    }

    private void loadFlights() {
        for (int i = 0; i < 3; i++) {
            final FlightsRow flight        = getFlights().add();
            final FlightInfo flightSubform = flight.createFlight();

            flightSubform.loadPrices(BigDecimal.valueOf(1000 * (i + 1)), BigDecimal.valueOf(INITIAL_TAX));
        }
    }

    //~ Static Fields ................................................................................................................................

    public static final double INITIAL_TAX = 35.5;

    //~ Inner Classes ................................................................................................................................

    public class FlightsRow extends FlightsRowBase {}
}
