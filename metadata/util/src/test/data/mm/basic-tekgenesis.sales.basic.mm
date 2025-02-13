package tekgenesis.sales.basic;

entity Cart "Cart"
    primary_key cartId
{
    cartId    "Id"    : Int;
    items "Items" : entity CartItem* {
        product  "Product"  : Product;
        quantity "Quantity" : Int;
    };
}

form CartForm "Cart"
    entity Cart
{
    id       "Id"    : cartId;
    items    "Items" : items, table {
        product  "Product"     : product, on_change updateUnitPrice;
        unit     "Unit Price"  : Decimal(10, 2), disable;
        quantity "Quantity"    : quantity;
        total    "Total Price" : Decimal(10, 2), is unit * quantity;
    };
    subtotal "$"     : Decimal(10, 2), optional, is sum(total), style "pull-right aggregator";
    horizontal, style "form-actions" {
        button(save);
        button(cancel);
        button(delete), style "pull-right";
    };
}


entity Category "Category"
    primary_key categoryId
    described_by descr
    searchable
{
    categoryId "Id #"      : Int, check categoryId > 0 : "Must be positive";
    name     "Name"        : String(30);
    descr    "Description" : String(120);
    products "Products"    : Product*;
}

form CategoryForm "Category"
    entity Category
{
    editMode               : Boolean, internal;
    id       "Id #"        : categoryId, check (id > 0 : "Must be positive", id > 0 : "Must be positive");
    name     "Name"        : name;
    descr    "Description" : descr, default name;
    products "Products"    : products, table, hide when editMode == false {
        productId   "Id"          : productId;
        model       "Model No."   : model;
        description "Description" : description, text_field(100), optional;
        price       "Price"       : price;
        state       "State"       : state;
    };
    horizontal, style "form-actions" {
        button(save);
        button(cancel);
        button(delete), style "pull-right";
    };
}

entity Customer "Customer"
    primary_key documentType, documentId
    described_by firstName, lastName
    searchable
    unique nick(nickname)
    index last(lastName, firstName)
{
    documentType            : DocType;
    documentId   "Document" : Decimal(10);
    firstName               : String(50);
    lastName                : String(50);
    nickname     "Username" : String(50);
    sex                     : Sex;
}

form CustomerForm "Customer"
    entity Customer
{
    documentType "Document Type" : documentType;
    documentId   "Document"      : documentId;
    firstName    "First name"    : firstName, text_field(50);
    lastName     "Last name"     : lastName, text_field(50);
    nickname     "Nickname"      : nickname, text_field(50), default (substring(firstName, 0, 1) + "") + lastName;
    sex          "Sex"           : sex;
    horizontal, style "form-actions" {
        button(save);
        button(cancel);
        button(delete), style "pull-right";
    };
}

form CustomerReviewForm "Customer Review"
    entity Customer
{
    documentType "Document Type" : String, disable;
    documentId   "Document Id"   : documentId, disable;
    firstName    "First name"    : firstName, text_field(50), disable;
    lastName     "Last name"     : lastName, text_field(50), disable;
    nickname     "Nickname"      : nickname, text_field(50), default (substring(firstName, 0, 1) + "") + lastName, disable;
    sex          "Sex"           : String, disable;
}

enum DocType "Document Type"
with {
    local    : Boolean;
}
{
    DNI  : "DNI", true;
    PASS : "Passport", false;
}

form DocTypeForm enum DocType;


entity Invoice "Invoice"
{
    invoiceDate "Date"     : Date;
    customer    "Customer" : Customer;
    items       "Items"    : entity Item* {
        product  "Product"  : Product;
        quantity "Quantity" : Int;
        discount "Discount" : Int;
    };
    payments    "Payments" : entity Payment* {
        payment "Payment" : PaymentType;
        amount  "Amount"  : Decimal(10, 2);
    };
}

form InvoiceForm "Invoice Form"
    entity Invoice
{
    id          "Id"       : id;
    invoiceDate "Date"     : invoiceDate;
    customer    "Customer" : customer, on_change updateCustomer, on_new createCustomer;
    disc                   : Int, internal, default 10;
    items       "Items"    : items, table,
                             row_style (((discount * 100) / (unit * quantity)) > disc) ? "red" : ((((discount * 100) / (unit * quantity)) >= (disc / 2)) && (((discount * 100) / (unit * quantity)) <= disc)) ? "yellow" : "" {
        product  "Product"     : product, on_change updateUnitPrice;
        unit     "Unit Price"  : Decimal(10, 2), disable;
        quantity "Quantity"    : quantity;
        discount "Discount"    : discount, default 0;
        total    "Total Price" : Decimal(10, 2), is (unit * quantity) - discount, disable;
    };
    subtotal    "$"        : Decimal(10, 2), optional, is sum(total), disable, style "pull-right aggregator";
    payments    "Payments" : payments, table {
        type    "Type"    : PaymentOption, suggest_box, on_change updatePayments;
        payment "Payment" : payment, combo_box;
        amount  "Amount"  : amount;
    };
    received    "$"        : Decimal(10, 2), optional, is sum(amount), disable, style "pull-right aggregator",
                             check ((subtotal - received) >= 0.0 : inline "Exceeded payments", (subtotal - received) <= 0.0 : inline "Missing payments");
    horizontal, style "form-actions" {
        button(save);
        button(cancel);
        button(delete), style "pull-right";
    };
}

form InvoiceReviewForm "Invoice Review" on_load load{
    invoices    : String, table, static {
        id       "Id"       : Int, disable;
        items    "Items"    : sub_form(ItemsForm), display count + " Items";
        payments "Payments" : sub_form(PaymentsForm), display "$ " + received;
        customer "Customer" : sub_form(CustomerReviewForm), display (lastName + ", ") + firstName;
        date     "Date"     : Date, disable;
    };
}


form ItemsForm "Items" {
    count        : Int, internal, default 0;
    items        : String, table, static {
        product  "Product"    : String(25), disable;
        unit     "Unit Price" : Decimal(10, 2), disable;
        quantity "Quantity"   : Int, disable;
        total    "Total"      : Decimal(10, 2), is unit * quantity, disable;
    };
    subtotal "$" : Decimal(10, 2), optional, is sum(total), disable, style "pull-right aggregator";
}

enum MailDigest "Mail Digest" {
    DAILY;
    WEEKLY;
    SUMMARY;
    NONE;
}


open enum PaymentOption "Payment Option" {
    CASH;
    DEBIT;
    CREDIT;
    CHECK;
}

form PaymentOptionForm enum PaymentOption;

entity PaymentType "Payment Type"
    described_by subtype
    searchable
    index type
{
    type        "Type"        : PaymentOption;
    subtype     "SubType"     : String(30);
    description "Description" : String(160);
}

form PaymentTypeForm entity PaymentType;

form PaymentsForm "Payments" {
    payments     : String, table, static {
        type    "Type"    : String(25), disable;
        payment "Payment" : String(25), disable;
        amount  "Amount"  : Decimal(10, 2), disable;
    };
    received "$" : Decimal(10, 2), optional, is sum(amount), disable, style "pull-right aggregator";
}

entity Preferences "Customer Preferences"
    described_by customer
    unique customer
{
    customer "Customer" : Customer;
    mail     "Email"    : String(60);
    twitter             : String(60);
    digest              : MailDigest;
}

form PreferencesForm "Customer Preferences"
    entity Preferences
{
    id       "Id"       : id, internal;
    customer "Customer" : customer, disable;
    mail     "Email"    : mail, text_field(25);
    digest   "Digest"   : digest;
    twitter  "Twitter"  : twitter, text_field(25), placeholder "@garbarino", on_change checkUser;
    image               : image, optional, hide when twitter == "";
    horizontal, style "form-actions" {
        button(save);
         "Skip" : button(cancel);
        button(delete), style "pull-right";
    };
}

entity Product "Product"
    primary_key productId
    described_by description
    searchable
{
    productId   "Id"          : String(8);
    model       "Model No."   : String(30);
    description "Description" : String(100), optional;
    price       "Price"       : Decimal(10, 2);
    state       "State"       : State;
    category    "Category"    : Category;
    images                    : entity Image* described_by imageId {
        imageId    : String(100);
    };
}

form ProductForm "Product Form"
    entity Product
{
    productId   "Id"          : productId;
    model       "Model No."   : model;
    description "Description" : description, optional, default model;
    price       "Price"       : price;
    state       "State"       : state;
    category    "Category"    : category, on_new createCategory;
    gallery     "Images"      : gallery(image), optional, multiple, file_type "image";
    horizontal, style "form-actions" {
        button(save);
        button(cancel);
        button(delete), style "pull-right";
    };
}

form Products "Products" on_load load{
    products    : String, table {
        productId  "Product Id"  : String, disable, style "small";
        model      "Model"       : String, disable;
        category   "Cateogry"    : String, disable;
        descrption "Description" : String, disable;
        active     "Active?"     : Boolean, disable, style "small";
        price      "Price"       : String, disable, style "small";
        image      "Image"       : image;
    };
}

enum Sex {
    F : "Female";
    M : "Male";
}

enum State {
    ACTIVE;
    DISCONTINUED;
}
