
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
import java.util.List;

import tekgenesis.common.core.DateOnly;

import static tekgenesis.common.core.DateOnly.date;

/**
 * User class for form: FlightChooserForm
 */
public class FlightChooserForm extends FlightChooserFormBase {

    //~ Methods ......................................................................................................................................

    @Override public void load() {
        final FlightChooser route = createFlightChooser();
        getChooser().populate(route);
    }

    private FlightChooser createFlightChooser() {
        final FlightChooser result = new FlightChooser();
        final DateOnly      from   = date(2017, 9, 16);
        final DateOnly      to     = date(2017, 9, 26);

        result.setFrom(from);
        result.setTo(to);
        result.setOrigin("Buenos Aires");
        result.setDestiny("Natal");

        final List<FlightOption> options = result.getOptions();

        options.add(option(from, to, new FlightOption(), "AEP", "NAT", 8450));
        options.add(option(from, to, new FlightOption(), "EZE", "NAT", 9370));
        options.add(option(from, to, new FlightOption(), "AEP", "NAT", 12560));

        return result;
    }

    private FlightOption option(DateOnly from, DateOnly to, FlightOption option, String origin, String destiny, int price) {
        final FlightRoute out = option.getOut();
        out.setDate(from);
        out.getOrigin().setCity("Buenos Aires").setName(origin);
        out.getDestiny().setCity("Natal").setName(destiny);
        out.getItineraries()
            .add(
                new FlightItinerary().setAirline("GOL")
                                     .setDeparture(from.toDateTime().addHours(8).addMinutes(30))
                                     .setArrival(from.toDateTime().addHours(15).addMinutes(45))
                                     .setLegs(1));
        out.getItineraries()
            .add(
                new FlightItinerary().setAirline("TAM")
                                     .setDeparture(from.toDateTime().addHours(9).addMinutes(25))
                                     .setArrival(from.toDateTime().addHours(15).addMinutes(26))
                                     .setLegs(1));
        out.getItineraries()
            .add(
                new FlightItinerary().setAirline("GOL")
                                     .setDeparture(from.toDateTime().addHours(14).addMinutes(30))
                                     .setArrival(from.toDateTime().addHours(19).addMinutes(22))
                                     .setLegs(2));

        final FlightRoute in = option.getIn();
        in.setDate(to);
        in.getOrigin().setCity("Natal").setName(destiny);
        in.getDestiny().setCity("Buenos Aires").setName(origin);
        in.getItineraries()
            .add(
                new FlightItinerary().setAirline("GOL")
                                     .setDeparture(to.toDateTime().addHours(10).addMinutes(20))
                                     .setArrival(to.toDateTime().addHours(18).addMinutes(55))
                                     .setLegs(2));
        in.getItineraries()
            .add(
                new FlightItinerary().setAirline("TAM")
                                     .setDeparture(to.toDateTime().addHours(12).addMinutes(8))
                                     .setArrival(to.toDateTime().addHours(14).addMinutes(55))
                                     .setLegs(1));

        option.getPrice().setPrice(new BigDecimal(price)).setTax(new BigDecimal(1380));

        return option;
    }  // end method option
}  // end class FlightChooserForm
