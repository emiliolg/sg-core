package idea.test;

entity Invoice "Invoice"
    primary_key number
    described_by statement
    remotable
{
	number  "Number"                : Decimal(12);
	statement   "Statement"         : String(50);
	instructions  "Instructions"    : String(100), optional;
	order  "Order Number"           : Int;
	/*date    "Date"                  : Date;*/
	type    "Type"			        : Type;
    items   "Items"                 : Item*;
}

entity Product "Product"
    primary_key id
    described_by description
{
    id "Id"                     : String(20);
    description "Description"   : String(50);
}

entity Item "Item"
	primary_key slot
	described_by product,qty
	remotable
{
    slot "Slot"             : Int;
    product "Product"       : Product;
    invoice "Invoice"       : Invoice;
    qty "Quantity"          : Int, default 1;
    unit_price "Unit Price" : Decimal(10,2);
    discount "Discount"     : Decimal(10,2);
}

form InvoiceForm "Invoice"
    entity Invoice
{
	number "#" : number;
    statement;
    instructions, required;
	"Order" : horizontal {
	    "#" : order;
	    //"Random" : button, on_click randomOrder;
	    };
    /*date;*/
    holliday "Holliday" : check_box;
	type;
	"Shortname" : horizontal {
	   // shortname : text_field, on_change checkShortname;
	    available : text_field;
	    };
	other "Other" : String(20), internal;
	items {
	    slot;
	    product, combo_box;
        qty;
        unit_price, disable;
        discount;
        price "Price" : Decimal(10,2), is unit_price * qty;
	};
	"Total" : Decimal(10,2)/*, is sum(price)*/, disable;
}

enum Type "Invoice Type"
{
    STANDARD    : "Standard";
    AUTO_BILL  	: "Auto-Bill";
    PRO_FORMA  	: "Pro-Forma";
    QUICK  	    : "Quick";
}
