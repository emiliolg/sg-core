entity Customer "Customer"
    primary_key documentId
    described_by firstname, lastname
    searchable
{
	documentType    "Document Type" : DocType;
	documentId "Document"   : Decimal(10);
	firstname "First"       : String(50);
	lastname "Last"         : String(100);
	sex "Sex"		        : Sex;
}

enum Sex
{
    F   : "Female";
    M  	: "Male";
}

enum DocType
{
    DNI  : "DNI";
    PASS : "Passport";
}

entity Invoice "Invoice"
    primary_key id
    described_by id
{
    id "Id" : Int;
    date "Date" : Date;
    customer "Customer" : Customer;

    items "Items" : entity Item* {
        product "Product" : Product;
        quantity "Quantity" : Int;
    };

    /*payments "Payments" : entity Payment* {
        type "Type" : PaymentType;
        subtype "Subtype" : PaymentSubtype;
        amount "Amount" : Real;
    };*/
}

/*entity Payment "Payment"
	primary_key seq
	described_by type, amount
{
    seq "Sequence" : Decimal(20);
    invoice "Invoice" : Invoice;
    type "Type" : PaymentType;
    amount "Amount" : Real;
}*/

/*entity PaymentSubtype "Payment Subtype"
    primary_key type, subtype
    described_by subtype
{
    type "Type" : PaymentType;
    subtype "Subtype" : String;
}*/

enum PaymentType
{
    CASH : "Cash";
    CREDIT : "Credit";
    CHECK : "Check";
}

form InvoiceForm "Invoice Form"
    entity Invoice
{
    id;
    date;
    horizontal { customer; "+" : button, on_click createCustomer; };

    items {
        product, on_change updateUnitPrice;
        unit "Unit Price" : Decimal(10,2), disable;
        quantity;
        total "Total Price" : Decimal(10,2), is unit * quantity, disable;
    };

    /*payments {
        type;
        type "Type": PaymentType, on_change updatePaymentSubtype;
        subtype, combo_box;
        amount;
    };*/

    "Invoice Total" : Decimal(10,2), is sum(total);

    actions : horizontal, style "form-actions" {
        save : button(save);
        cancel : button(cancel);
        delete : button(delete), style "pull-right";
    };
}
