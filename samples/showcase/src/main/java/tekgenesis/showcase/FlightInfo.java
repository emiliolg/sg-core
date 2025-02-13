
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
import java.math.RoundingMode;

import org.jetbrains.annotations.NotNull;

import tekgenesis.form.Action;

import static tekgenesis.showcase.RowType.*;

/**
 * User class for Form: FlightInfo
 */
public class FlightInfo extends FlightInfoBase {

    //~ Methods ......................................................................................................................................

    /** Invoked when the form is loaded. */
    @Override public void load() {
        loadFlightInfo();
    }

    /** Set ticket prices. */
    public void loadPrices(BigDecimal initialPrice, BigDecimal tax) {
        setInitialPrice(initialPrice);

        final PriceTableRow firstRow = getPriceTable().add();
        firstRow.setDescription("1 Adult");
        firstRow.setPrice(initialPrice);

        getPriceTable().add();
        // noinspection DuplicateStringLiteralInspection
        getPriceTable().add().setDescription("Total");

        setTax(tax);
    }

    @NotNull @Override public Action taxChange() {
        if (!getPriceTable().isEmpty()) {
            getPriceTable().get(1).setDescription("Taxes " + getTax() + "%");
            getPriceTable().get(1).setPrice(getInitialPrice().multiply(getTax()).divide(BigDecimal.valueOf(100), RoundingMode.HALF_DOWN));

            getPriceTable().get(2).setPrice(getInitialPrice().add(getPriceTable().get(1).getPrice()));
        }
        return actions.getDefault();
    }

    private void addFlightInfoRow(String leaveTime, RowType rowType, String arriveTime, String flightDuration, String airline) {
        final FlightsTableRow flightsTableRow = getFlightsTable().add();
        flightsTableRow.setLeaveTime(leaveTime);
        flightsTableRow.setRowType(rowType);
        flightsTableRow.setArriveTime(arriveTime);
        flightsTableRow.setFlightDuration(flightDuration);
        flightsTableRow.setAirline(airline);
    }

    private void loadFlightInfo() {
        addFlightInfoRow("Go from Buenos Aires, Argentina to Miami, USA", TITLE, "", "", "");
        addFlightInfoRow("Goes 23:15hs", INFO, "Arrives 7:25hs", "Duration: 9:10hs", "Some Airline");
        addFlightInfoRow("Return from Buenos Aires, Argentina to Miami, USA", TITLE, "", "", "");
        addFlightInfoRow("Goes 09:30hs", INFO, "Arrives 19.30hs", "Duration: 9:00hs", "Some Other Airline");
    }

    //~ Inner Classes ................................................................................................................................

    public class FlightsTableRow extends FlightsTableRowBase {}

    public class PriceTableRow extends PriceTableRowBase {}
}  // end class FlightInfo
