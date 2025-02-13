package tekgenesis.sales.cart;

form ProductListService on_load onLoad {
    title : String(100);

    path : table {
        pathId : String(100);
        pathDescription : String(100);
    };

    products : table {
        id : String(100);
        description : String(100);
        price : Decimal(10,2);
    };
}

form CustomerService
{
    dni:String;
    name:String;
    lastName:String;
    nickName:String,optional;
}
