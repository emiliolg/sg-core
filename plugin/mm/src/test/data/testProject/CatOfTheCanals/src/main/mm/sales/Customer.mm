package sales;

entity Customer "Customer"
    primary_key documentType, documentId,sex
    form CustomerForm
    described_by firstName, lastName
    searchable
    unique nick(nickname)
    index last(lastName, firstName)
{
	documentType  		: DocType;
	documentId "Document"   : Decimal(10);
	firstName        	: String(50);
	lastName         	: String(50);
	nickname "Username"     : String(50);
	sex 		        : Sex;
	photo               : Resource, optional;
}

enum Sex
{
    F   : "Female";
    M  	: "Male";
}

enum DocType "Document Type"
with { local: Boolean; }
{
    DNI  : "DNI", true;
    PASS : "Passport", false;
}

entity Preferences "Customer Preferences"
    described_by customer
    unique customer(customer)
{
    customer "Customer" : Customer;
    mail "Email" : String(60);
    twitter : String(60);
    digest : MailDigest;
}

enum MailDigest "Mail Digest"
{
   DAILY;
   WEEKLY;
   SUMMARY;
   NONE;
}

form PreferencesForm "Customer Preferences"
    entity Preferences
{
    header {
        message(title);
    };
    id, internal, optional;
    customer, disable;
    mail, text_field(25);
    digest;
    twitter, text_field(25), placeholder "@garbarino", on_change checkUser;
    image : image, hide when twitter == "";

    footer {
        button(save);
        "Skip" : button(cancel);
        button(delete), style "pull-right";
    };
}

form ItemsForm "Items"
{
    header { message(title); };

    count : Int, internal, default 0;

    items : table {
        product "Product" : String(25), disable;
        unit "Unit Price" : Decimal(10, 2), disable;
        quantity "Quantity" : Int, disable;
        total "Total" : Decimal(10,2), is unit * quantity, disable;
    };

    subtotal "$" : Decimal(10,2), is sum(total), disable, optional, style "pull-right aggregator";
}

form PaymentsForm "Payments"
{
    header { message(title); };

    payments : table {
        type "Type": String(25), disable;
        payment "Payment": String(25), disable;
        amount "Amount" : Decimal(10,2), disable;
    };

    received "$" : Decimal(10,2), is sum(amount), disable, optional, style "pull-right aggregator";
}

form CustomerReviewForm "Customer Review"
     entity Customer
 {
     header { message(title); };

     documentType "Document Type": text_field, disable;
 	 documentId "Document Id": documentId, disable;
     firstName "First name" : firstName, text_field, disable;
     lastName "Last name" : lastName, text_field, disable;
     nickname "Nickname" : nickname, text_field, disable;
     sex "Sex": text_field, disable;
 }


form InvoiceReviewForm "Invoice Review"
{
    header { message(title); };

    invoices : table {
        id "Id" : Int, disable;
        items "Items" : subform(ItemsForm), display count + " Items";
        payments "Payments" : subform(PaymentsForm), display "$ " + received;
        customer "Customer" : subform(CustomerReviewForm), display lastName + ", " + firstName;
        date "Date" : Date, disable;
    };
}
