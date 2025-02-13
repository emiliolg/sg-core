package tekgenesis.samples.service;

final type Minion {
    name : String;
    mood : Boolean;
}

handler MinionHandler
    on_route "minions"
{
    "/"         : Minion*,  all;
    "/all"      : Minion*,  all;
    "/$name"    : Minion,   some;
}

handler ComponentHandler
    on_route "private"
{
    "/header/$name"     : Html, header;
    "/og/$name"         : Html, og;
    "/list/$name"       : Html, list;
    "/banner/$name"     : Html, banner;
    "/menu/$name"       : Html, menu;
    "/product/$name"    : Html, product;
}

final type Product (  productId "Id"              : String(8)) {
	model "Model No."           : String(30);
	description "Description"   : String(100), optional;
	price "Price"               : Decimal(10,2);
	created "Created"           : DateTime;
}

handler ServiceHandler
    on_route "services"
{
    "/list/$lid"       : Html, list;
    "/menu/$mid"       : Html, menu;
    "/product/create/$pid" : String, create, body Product, method post;
    "/products/create" : String, arrayCreate, body Product*, method post;
    "/products/all" : String, all, parameters {
        passPhrase : String;
        range : Int, default 10;
        from : String, optional;
        currencies : String*, optional;
    };
}

final type ProductComparison (left : Product;	right : Product) {}

final type ProductList {
    list : Int;
    products : Product*;
}

final type Menu {
    id : String(20);
    items : MenuItem*;
}

final type MenuItem {
    id : String(20);
    label : String(30);
    link : String(200);
}

final type OpenGraphMetadata {
    title : String(100);
    type : String(30);
    url : String(200);
}

enum FamousQuotes {
    PLATO : "Life must be lived as play.";
    JOBS : "Stay hungry, stay foolish.";
    DALAI : "Be kind whenever possible. It is always possible.";
}

form MinionForm { minion : String, default "Stuart"; }
