package tekgenesis.test.fleet schema BasicTest;

import tekgenesis.test.basic.Product;

entity Vehicle "Vehicle"
	described_by brand, model, plate
	searchable
{
	plate "Plate"                                   : String(256);
	brand "Brand"                                   : Brand;
	model "Model"                                   : String(20);
    type "Type"                                     : VehicleType;
    vtvExpiration "VTV expiration date"             : Date;
    insuranceExpiration "Insurance expiration date" : Date;
    active "Active"                                 : Boolean, default true;
    images "Images"                                 : entity ImageResources* {
        image "Image"               : Resource, optional;
        description "Description"   : String(256);
    };
    comments "Comments"                             : String(100);
}

entity Brand "Brand"
	described_by name
	searchable
{
	name "Name" : String(20);
	products : Product*;
}

enum VehicleType "VehicleType"
{
	CAR; TRUCK;
}

entity Zone "Zone"
	described_by name
	searchable
{
	name "Name" : String(20);
}

entity SecuritySystem "SecuritySystem"
	described_by name
	searchable
{
	name "Name" : String(20);
}

entity TrackingSystem "TrackingSystem"
	described_by name
	searchable
{
	name "Name" : String(20);
}


enum Ranking
{
    ONE; TWO; THREE; FOUR; FIVE;
}

enum Fleet
{
    INTERNAL; CLIENT; INTERIOR;
}





