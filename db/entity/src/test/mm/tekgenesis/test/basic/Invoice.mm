package tekgenesis.test.basic schema BasicTest;

entity Invoice "Invoice"
    primary_key idKey
    described_by idKey
{
    idKey "Id" : Int;
    invoiceDate "Date" : Date;
    customer "Customer" : Customer;

    items "Items" : entity Item* {
        product "Product" : Product;
        quantity "Quantity" : Int;
        discount "Discount" : Int;
    };

    payments "Payments" : entity Payment* {
        payment "Payment" : PaymentType;
        amount "Amount" : Decimal(10,2);
    };
}

enum PaymentOption "Payment Option"
{
    CASH;
    DEBIT;
    CREDIT;
    CHECK;
}

entity PaymentType "Payment Type"
    described_by subtype
    searchable by {
        subtype;
        type;
        description;
    }
    index type(type)
{
    type "Type" : PaymentOption;
    subtype "SubType" : String(30);
    description "Description" : String(160);
}
