package tekgenesis.test.basic schema BasicTest;

import tekgenesis.test.fleet.Brand;

entity Product "Product"
    primary_key productId
    described_by description
    index description
    index state
    searchable by {
        productId;
        model;
    }
    auditable
    cache
    table PROD
{
	productId "Id"              : String(8), custom_mask "XX-XXXX-XX";
	model "Model No."           : String(30);
	description "Description"   : String(100), column DESCR;
	price "Price"               : Decimal(10,2);
	state "State"               : State, default CREATED;
	active "Active"             : Boolean;
	category "Category"         : Category, column CAT;
	mainImage : Resource, optional;
	images : entity Image* described_by imageId {
	    imageId : Resource;	state "State"               : State, default CREATED;
	};
	brand : Brand, optional;
}

/*
query Products {
    byState              : ByState(state:State , n: Int) = select state, count(*) n from Product group by state;
    byState(cat:Category): ByState = select state, count(*) n from Product group by state;
}
*/


entity Category "Category"
    primary_key idKey
    unique name
    described_by descr
    optimistic
    searchable by {
        idKey;
        name;
    }
    cache
{
    idKey "Id #"           : Int, check idKey > 0 : "Must be positive";
    name "Name"         : String (30), optional;
    descr "Description" : String (120);
    products "Products" : Product*;
}


entity CachedCategory "Category"
    primary_key idKey
    described_by descr
    searchable
    cache all
{
    idKey "Id #"           : Int, check idKey > 0 : "Must be positive";
    name "Name"         : String (30);
    descr "Description" : String (120);
}

enum State
{
    CREATED;
    ACTIVE;
    DISCONTINUED;
}

entity Store "Store"
    cache
{
   name "Name"          : String(60);
   locations            : entity Location* { address : String(60); };
   activeModules: ActiveModule*;
}

entity Cart "Cart"
{
items "Items" : entity CartItem* index salableProduct {salableProduct "Product" : Product; quantity "Quantity" : Int; };
}

entity Sale "Association between Products"
    primary_key pa_id

{
    pa_id              : Int;
    store: Store;
    pickupStore: Store, optional;
    date: DateTime;
}

enum ActiveModule
    primary_key pk
    with { pk: Int; }
{
    SUPPLY : "Supply", 0;
    CREDITCARD: "Credit Card Authorization", 1;
    BILLING: "Billing", 2;
}