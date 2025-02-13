package idea.test;

type Amount = Decimal(10, 2);

entity Invoice "Invoice"
    primary_key number
    remotable
{
	number  "Number"                : Decimal(12);
	date    "Date"                  : Date;
	type    "Type"			        : InvoiceType;
    items   "Items"                 : entity Item* "Item"
        described_by product,qty
            {
                product "Product"       : Product;
                qty "Quantity"          : Int, default 1;
                discount "Discount"     : Amount;
            };
}

entity Product "Product"
    primary_key id
    described_by description
{
    id "Id"                     : String(20);
    description "Description"   : String(50);
    price : Amount;
}

view ProductView of Product
    primary_key id
    remotable
{
    id : id;
    price : price;
}

view ProductSqlView "ProductView" of Product
    as "select ID,PRICE,UPDATE_TIME from QName(TEST,PRODUCT)"
    primary_key id
    remotable
{
    id  : Int;
    price "Price" : Amount;
    updateTime: DateTime(3);
}

enum InvoiceType "Invoice Type"
{
    STANDARD    : "Standard";
    AUTO_BILL  	: "Auto-Bill";
    PRO_FORMA  	: "Pro-Forma";
    QUICK  	    : "Quick";
}
