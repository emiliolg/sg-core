package tekgenesis.showcase schema showcase;

entity Flight "Flight"
	primary_key idKey
	described_by name
{
	idKey "Id"     : Int;
	name "Name" : String(20);
	from "From" : String(20);
	to "To"     : String(20);
	price "Price" : Int;
}

form FlightForm "Flight Form"
    entity Flight
{
    header { message(title); };

    displaying : Boolean, internal, default false;

    "Flight" : tabs {
        data "Data" : vertical{
            id "Id"   : idKey, disable when displaying;
            name "Name" : name, disable when displaying;
            from "From" : from, disable when displaying;
            to "To"   : to, disable when displaying;
            price "Price" : price, disable when displaying;
        };

        sectionsTab "Sections" : vertical {
            sections : table, hide when !displaying {
                sectionFrom "From" : String(20), optional;
                sectionTo "To" : String(20), optional;
            };
        };
    };

    footer {
        button(save);
        button(cancel);
        button(delete), style "pull-right";
    };
}

form Flights "Flights"
{
    header {
        message(title);
    };

    //Adding table inside group due to strange gulliver's usage.
    results "Results" : vertical {
        flights    : Flight, table, on_load loadFlights {
            id "Id"    : idKey;
            name "Name"  : name;
            from "From"  : from;
            to "To"    : to;
            price "Price" : price;
            flightSubform "Open in subform" : subform(FlightForm), display name + " (" + from + ", " + to + ")" ;
        };
    };
}
