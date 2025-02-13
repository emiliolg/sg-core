package tekgenesis.showcase;


type Coordinate {
    lat: Int, signed;
    lng: Int, signed;
}

abstract widget CoordinateWidget : Coordinate {
    "Lat" : lat;
    "Lng" : lng, on_change lngChanged;
//    "Lng" : lng;
//    "Locate" : button, on_click locate;
}

form CoordinatesForm {
    from "From" : widget(CoordinateWidget), no_label, style 3 == lngDiff ? "text-primary" : "text-danger";
    to "To" : widget(CoordinateWidget), hide when lngDiff == 2, label_expression 4 ==lngDiff ? "Exactly 4" : "Not 4", icon_expr 4 ==lngDiff ? "star" : "android";

    latDiff "Lat difference" : Int, display, is to.lat > from.lat ?  to.lat - from.lat : from.lat - to.lat;
    lngDiff "Lng difference" : Int, display, is to.lng > from.lng ?  to.lng - from.lng : from.lng - to.lng;
    fromCount "From counts" : Int, display;
    toCount "To counts": Int, display;
}


type AddressType
{
    street     : String(60);
    city       : String(40);
    state      : String(40), optional when city == "CABA";
    zip        : String(10), optional;
    country    : String(30);
}



abstract widget AddressTypeWidget : AddressType {
    street;
    city;
    state, on_change stateChanged;
    zip;
    country;
}

form CustomerTypeForm {
    header {
        message(title), col 12;
    };

    document "Document": Int, mask decimal;
    firstName "First name": String;
    lastName "Last name": String;
    home "Home address": display, is homeAddress.street + " " + homeAddress.state + " " + homeAddress.city;
    homeAddress "Home address": widget(AddressTypeWidget);

    createWorkAddress "Create work" : button, on_click reateWorkAddress;
    work "Work address": display, is workAddress.street + " " + workAddress.state + " " + workAddress.city;
    workAddress "Work address": widget(AddressTypeWidget), optional;

    feedback "Feedback" : String;
}

type FlightType "Flight" {
	idKey "Id"     : Int;
	name "Name" : String;
	from "From" : String;
	to "To"     : String;
	price "Price" : Int;
	schedules: FlightSchedule*;
}

type FlightSchedule {
    fromSchedule "From": String;
    toSchedule "To": String, optional;
}

abstract widget FlightWidget : FlightType {
    displaying : Boolean, internal, default true;

    flightNr : Int, internal;

    info "Info" : vertical, col 6 {
        idKey, disable when displaying;
        name, disable when displaying;
        from, disable when displaying;
        to, disable when displaying;
        price, disable when displaying;
    };

    schedule "Schedules" : vertical, col 6 {
        add "Add" : button, on_click addRow;
        sections : schedules, table {
            fromSchedule, on_change fromChanged;
            toSchedule ;
        };
    };
}

form AirportForm {
    sections: section {
        horizontal {
            flight : widget(FlightWidget);
        };
    };
}

form FlightChooserForm {
    chooser : widget(FlightChooserWidget);
}

type FlightChooser
    widget FlightChooserWidget
{
    origin : String;
    destiny : String;
    from : Date;
    to : Date;
    options : FlightOption*;
}

type FlightOption
    widget FlightOptionWidget
{
    out : FlightRoute;
    in : FlightRoute;
    price : FlightPrice;
}

type FlightRoute
    widget FlightRouteWidget
{
    date : Date;
    origin : Airport;
    destiny : Airport;
    itineraries : FlightItinerary*;
}

type FlightItinerary
    widget FlightItineraryWidget
{
    airline : String;
    departure : DateTime;
    arrival : DateTime;
    legs : Int;
}

type FlightPrice
    widget FlightPriceWidget
{
    price : Decimal(10, 2);
    tax : Decimal(10, 2);
}

type Airport
    widget AirportWidget
{
    name : String;
    city : String;
}

widget FlightChooserWidget : FlightChooser {
    vertical , col 3{
        origin;
        destiny;
        from;
        to;
        button "Search" : button, on_click search, icon search, style "text-center margin-top-20", content_style "btn-warning";
    };
    vertical , col 9 {
        options, section, no_label {
            option : _, widget(FlightOptionWidget);
        };
    };
}

widget FlightOptionWidget : FlightOption {
    horizontal, style "air-itinerary-section" {
		vertical, col 9, style "vertical-line-right" {
			out, widget(FlightRouteWidget), icon arrow_right, label_col 2;
		    in, widget(FlightRouteWidget), icon arrow_left, label_col 2;
		};
		vertical, col 3 {
            price, widget(FlightPriceWidget), no_label, style "margin-left-10";
		};
	};
}

widget FlightItineraryWidget : FlightItinerary {
    horizontal {
		airline, display, col 2, icon plane, no_label;
	    departure, display, col 2, no_label;
	    legs, display, col 2, no_label;
	    arrival, display, col 2, no_label;
	};
}

widget AirportWidget : Airport {
    city, internal;
    name, display, hint city, no_label;
}

widget FlightRouteWidget : FlightRoute {
    horizontal, style "itinerary-title" {
		date, display, no_label, col 2;
	    origin, widget(AirportWidget), col 2, no_label;
	    destiny, widget(AirportWidget), col 2, offset_col 2, no_label;
	};
	horizontal {
	    airlines: display, is "Airline", col 2, style "itinerary-label";
	    out: display, is "Departure", col 2, style "itinerary-label";
	    legs: display, is "Legs", col 2, style "itinerary-label";
	    in: display, is "Arrival", col 2, style "itinerary-label";
	};
    itineraries, table, no_label {
        itinerary : _, widget(FlightItineraryWidget);
    };
}


widget FlightPriceWidget "Flight Price Widget" : FlightPrice {
    horizontal {
        display, is "Price", style "prices-font";
        vertical {
            "Price" : price, display, mask decimal, no_label, hint "Flight";
            "Tax"   : tax, display, mask decimal, no_label, hint "Taxes";
        };
    };
    horizontal {
        reserve "Reserve": button, on_click reserve;
        buy "Buy" : button, on_click buy;
    };
}
