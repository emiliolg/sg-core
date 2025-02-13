package catalog;


entity Invoice "Invoice"
    primary_key id
    described_by id
{
    id "Id" : Int;
    invoiceDate "Date" : Date;

    items "Items" : entity Item* {
        quantity "Quantity" : Int;
        discount "Discount" : Int;
    };

    payments "Payments" : entity Payment* {
        payment "Payment" : PaymentType;
        amount "Amount" : Decimal(10,2);
    };
}

form InvoiceForm "Invoice Form"
    entity Invoice
{
    header {
        message(title);
    };
    "Id"       : id;
    "Date"     : invoiceDate;
    "Items"    : items, table {
        "Quantity" : quantity;
        "Discount" : discount;
    };
    "Payments" : payments, table {
        "Payment" : payment;
        "Amount"  : amount;
    };
    footer {
        button(save);
        button(cancel);
        button(delete), style "pull-right";
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
    searchable
    index type(type)
{
    type "Type" : PaymentOption;
    subtype "SubType" : String(30);
    description "Description" : String(160);
}

form PaymentTypeForm entity PaymentType;


