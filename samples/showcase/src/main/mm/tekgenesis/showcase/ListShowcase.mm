package tekgenesis.showcase schema showcase;

type Year = Int;

enum Country {
    ARGENTINA;
    GERMANY;
    JAPAN;
    COREA;
    UNITED_STATES;
    ITALY;
}

entity Make
 searchable by {
    name;
    origin;
 }
 described_by name
{
    name : String(30);
    origin : Country;
    models : entity Model*
        described_by model
        searchable
    {
        model : String(30);
        released : Date;
    };
}

entity Car "Car"
	described_by make, model
    searchable by {
        make;
        model, boost 3;
    }
{
	make : Make;
	model : Model;
    year : Year;
    engine : Engine;
    price : Decimal(7);
    mileage : Int;
    transmission : Transmission;
    color : String(30);
    air : Boolean;
    bluetooth : Boolean;
    cruise : Boolean;
}

enum Engine "Engine Type"
{
   GAS;
   DIESEL;
   GNC;
   HYBRID;
}

enum Transmission "Transmission Type"
{
    MANUAL;
    AUTOMATIC;
    SEMIAUTOMATIC;
    MANUMATIC;
}

form MakeForm entity Make;

form CarForm "Car Form"
    entity Car
    parameters year, engine, air, bluetooth, price
{
    header {
        message(title);
        search_box, style "pull-right";
        "View Source" : label, link "/sg/view/source/tekgenesis.showcase.CarForm", style "pull-right";
    };

    id, internal, optional ;
    make;
    model;
    year;
    engine;
    price;
    mileage;
    transmission;
    color;
    air;
    bluetooth;
    cruise;

    footer {
        button(save);
        button(cancel);
        button(delete), style "pull-right";
    };
}

form CarListForm "Car Listing Form"
{
    header {
        message(title);
        "View Source" : label, link "/sg/view/source/tekgenesis.showcase.CarListForm", style "pull-right";
    };

    cars    : Car, table, on_change saveCar, on_load loadCars {
        "Id"           : id, internal, optional ;
        "Make"         : make;
        "Model"        : model;
        "Year"         : year;
        "Engine"       : engine;
        "Price"        : price, mask currency;
        "Mileage"      : mileage;
        "Transmission" : transmission;
        "Color"        : color;
        "Air"          : air;
        "Bluetooth"    : bluetooth;
        "Cruise"       : cruise;
    } with {
        sum : price;
    };

    horizontal, style "margin-top-20" {
        addRowBottom "Add" : button(add_row, cars), style "margin-right-5";
        removeRowBottom "Remove" : button(remove_row, cars), on_click removeCar;
    };
}

form CarFilterForm "Car Filter Form"
    on_load load
{
    header {
        message(title);
        "View Source" : label, link "/sg/view/source/tekgenesis.showcase.CarFilterForm", style "pull-right";
    };

    filter "Create Filters" : button, on_click filter;

    main : horizontal {

        filterPanel : vertical, col 3 {
            filters : section, filtering cars, style "no-label-childs", row_style "thumbnail margin5" {
                title : display, style "lead";
                value : dynamic, multiple, optional;
            };
        };

        carsPanel : vertical, col 9 {
            cars : Car, table, on_click rowClicked {
                id, internal, optional ;
                make;
                model;
                year;
                engine;
                price;
                mileage;
                transmission, internal;
                publication "Published" : Date, internal;
                expiration "Expires" : DateTime, internal;
            };
        };
    };

    "Some Navigation" : button, on_click navigate;
}

form CarFilterSectionForm "Car Filter Form"
    on_load load
{
    header {
        message(title);
        viewSource "View Source" : label, link "/sg/view/source/tekgenesis.showcase.CarFilterSectionForm", style "pull-right";
    };

    filter "Create Filters" : button, on_click filter;

    main : horizontal {

        filterPanel : vertical, col 3 {
            filters : section, filtering cars, style "no-label-childs", row_style "thumbnail margin5" {
                title : display, style "lead";
                value : dynamic, multiple, optional;
            };
        };

        carsPanel : vertical, col 9 {
            cars : Car, section {
                horizontal {
					id, internal, optional ;
	                make;
	                model;
	                year;
	                engine;
	                price;
	                mileage;
	                transmission, internal;
	                publication "Published" : Date, internal;
	                expiration "Expires" : DateTime, internal;
				};
            };
        };
    };
}

form CarFilterMapForm
    on_load load
{
    header {
        message(title);
        viewSource "View Source" : label, link "/sg/view/source/tekgenesis.showcase.CarFilterMapForm", style "pull-right";
    };

    horizontal {
        filterPanel : vertical, col 3 {
            filters : section, filtering cars, style "no-label-childs", row_style "thumbnail margin5" {
                title : display, style "lead";
                value : dynamic, multiple, optional;
            };
        };

        carsPanel : vertical, col 9 {
            cars : Car, map {
                lat : Real;
                lng : Real;
                vertical {
	                make, display;
	                model, display;
				};
            };
        };
    };
}

form ExcludingFiltersForm "Excluding Filters Form"
    on_load load
{
    main : horizontal, style "row-fluid" {

        filterPanel : vertical, style "span3" {
            filters : section, filtering items, style "no-label-childs", row_style "thumbnail margin5" {
                title : display, style "lead";
                value : dynamic, multiple, optional;
            };
        };

        carsPanel : vertical, style "span9" {
            items : table {
                label : String;
            };
        };
    };
}

form MassiveTableForm "Massive Table Form"
    on_load load
{
    header { message(title); };

    items "Items" : table, lazy, filterable, sortable {
        internal;
        label "Label" : String(25);
        date "Date" : Date, display;
        expiration "Expiration" : DateTime, display;
        number "Number" : Int;
    };

    button(add_row);
    button(remove_row);
}

form FilterableTable "Filterable Table"
    on_load load
{
    header {
        message(title);
        "View Source" : label, link "/sg/view/source/tekgenesis.showcase.FilterableTable", style "pull-right";
    };

    items "Items" : table(10), filterable {
        number "Number" : Int;
        label "Label" : String;
        date "Date" : Date;
        entity "Simple Entity" : SimpleEntity;
        option "Option" : Options;
        valid "Valid" : Boolean;
    };

    button(add_row);
    button(remove_row);

    add10 "Add 10" : button, on_click add10;
    remove10 "Remove 10" : button, on_click remove10;
    nextPage "Next Page" : button, on_click nextPage;
}

entity DNI primary_key number searchable {
    number: Int;
    descr : String;
}



form DNIForm "Dniform"
    entity DNI
{
    header {
        message(entity), col 8;
        search_box, col 4, style "pull-right";
    };
    "Number" : number, mask decimal;
    "Descr"  : descr;
    manual "Manual update": button, on_click manual;
    manualUpdating "Manual update updating form" : button, on_click manualUpdating;
    footer {
        button(save);
        button(cancel);
        button(delete), style "pull-right";
    };
}



entity PersonWithDni primary_key dni searchable {
    dni: DNI;
    name: String;
    lastname: String;
}



form PersonWithDniForm "Person With Dni Form"
    entity PersonWithDni
{
    header {
        message(entity), col 8;
        search_box, col 4, style "pull-right";
    };
    "Dni"      : dni;
    "Name"     : name;
    "Lastname" : lastname;
    footer {
        button(save);
        button(cancel);
        button(delete), style "pull-right";
    };
}

form ListFilterCars
    on_load load
{

    header { message(title), col 8; };

    /*offset : Int, internal, default 0;
    limit : Int, internal, default 30;*/

    main : horizontal {

        filterPanel : vertical, col 3 {
            filters : section, filtering cars, style "no-label-childs", row_style "thumbnail margin5" {
                title : display, style "lead";
                value : dynamic, multiple, optional;
            };
        };

        carsPanel : vertical, col 9 {
            cars : Car, table(15), lazy_fetch {
                id, internal, optional;
                make;
                model;
                year;
                engine;
                price;
                mileage;
                transmission;
            };
        };

        /*horizontal, style "center" {
            moreDisabled : Boolean, internal, default false;
            more "More" : button, on_click more, disable when moreDisabled;
        };*/
    };

}

