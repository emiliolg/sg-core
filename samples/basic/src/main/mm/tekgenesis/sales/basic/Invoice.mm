package tekgenesis.sales.basic schema basic;

type Cust = tekgenesis.sales.basic.Customer;

entity Invoice "Invoice"
    primary_key idKey
    described_by idKey
{
    idKey "Id" : Int,protected;
    invoiceDate "Date" : Date;
    customer "Customer" : Cust;

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
    primary_key symbol
with {
    symbol : String(2);
}
{
    CASH : "Cash", "CA";
    DEBIT : "Debit", "DE";
    CREDIT : "Credit", "CR";
    CHECK : "Check", "CH";
}

entity PaymentType "Payment Type"
    described_by subtype
    searchable by {
        type;
        description;
        subtype;
    }
    index type(type)
{
    type "Type" : PaymentOption;
    subtype "SubType" : String(30);
    description "Description" : String(160);
}

form PaymentTypeForm "Payment Type" entity PaymentType;

form InvoiceForm "Invoice"
    entity Invoice
    permissions draft
{
    header {
        message(title);
    };

    idKey;
    invoiceDate;
    customer, on_new createCustomer, on_change updateCustomer;

    disc: Int, internal, default 10;

    items, row_style discount * 100 / (unit * quantity) > disc ? "red" :
                     discount * 100 / (unit * quantity) >= (disc / 2) && discount * 100 / (unit * quantity) <= disc ? "yellow" : ""
    {
        product, on_change updateUnitPrice, on_new createProduct;
        unit "Unit Price" : Decimal(10, 2), disable;
        quantity, default 1;
        discount, default 0;
        total "Total Price" : Decimal(10,2), is unit * quantity - discount;
    } with {
        sum : total;
    };

    horizontal, style "margin-top-20" {
        "Add" : button(add_row, items), style "margin-right-5";
        "Remove" : button(remove_row, items);
    };

    subtotal : Decimal(10,2), internal, is sum(total), optional;

    payments {
        type "Type": PaymentOption, suggest_box, on_change updatePayments;
        payment, combo_box;
        amount;
    };

    horizontal, style "margin-top-20" {
        "Add" : button(add_row, payments), style "margin-right-5";
        "Remove" : button(remove_row, payments);
    };

    received "$" : Decimal(10,2), is sum(amount), optional, style "pull-right aggregator",
                   check (subtotal - received >= 0.0 : inline "Exceeded payments",
                          subtotal - received <= 0.0 : inline "Missing payments");

    footer {
        button(save);
        button(cancel);
        "Draft" : button, hide when forbidden(draft); // Do nothing :S
        button(delete), style "pull-right";
    };
}
