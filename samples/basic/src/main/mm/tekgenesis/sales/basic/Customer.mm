package tekgenesis.sales.basic schema basic;

entity Customer "Customer"
    primary_key documentType, documentId,sex
    form CustomerForm
    described_by firstName, lastName
    searchable by {
        firstName;
        lastName;
        documentId;
    }
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

form CustomerForm "Customer" : Customer
    primary_key documentType, documentId, sex
{
    header {
        message(title);
        search_box, style "pull-right";
    };

    documentType;
	documentId;
    firstName "First name" : firstName, text_field;
    lastName "Last name" : lastName, text_field;
    nickname "Nickname" : nickname, text_field, default substring(firstName,0,1) + "" + lastName;
    sex;
    photo;

    footer {
        button(save);
        button(cancel);
        button(delete), style "pull-right";
    };
}

entity CustomerSearchable
  described_by firstName, lastName
  searchable by {
    withOtherId: firstName, filter_only;
    lastName, boost 3;
    birthDate;
    document;
    someExtraField;
} {
    firstName: String;
    lastName: String;
    document: Decimal(10);
    birthDate: DateTime;
    sex: Sex;
    someExtraField: String, abstract, read_only;
}

entity DatabaseCustomerSearchable
  described_by firstName, lastName
  searchable by database {
    withOtherId: firstName, filter_only;
    lastName, boost 3;
    document;
} {
    firstName: String;
    lastName: String;
    document: Decimal(10);
    birthDate: DateTime;
    sex: Sex;
}

form DatabaseCustomerSearchableForm entity DatabaseCustomerSearchable;

form CustomerSearchableForm entity CustomerSearchable;

form SearchFilterForm {
    header {
        message(title);
    };

    someBool: Boolean, default false;
    someLastName: String;

    someFilter: CustomerSearchable, filter (withOtherId = "Martin", lastName = (someLastName, "Gutierrez"), lastName != "Sanchez" when someBool);
}

enum Sex
  index number
  with {
    number: Int;
  }
{
    F   : "Female", 0;
    M  	: "Male", 1;
}

enum DocType "Document Type"
    default DNI
    primary_key id
    with {
        local: Boolean;
        id: Int;
        sex: Sex;
    }
{
    DNI  : "DNI", true, 0, F;
    PASS : "Passport", false, 1, M;
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
    implements MailDigester
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
        total "Total" : Decimal(10,2), is unit * quantity;
    };

    subtotal "$" : Decimal(10,2), is sum(total), optional, style "pull-right aggregator";
}

form PaymentsForm "Payments"
{
    header { message(title); };

    payments : table {
        type "Type": String(25), disable;
        payment "Payment": String(25), disable;
        amount "Amount" : Decimal(10,2), disable;
    };

    received "$" : Decimal(10,2), is sum(amount), optional, style "pull-right aggregator";
}

form CustomerReviewForm "Customer Review"
     entity Customer
 {
     header { message(title); };

     documentType "Document Type": text_field, disable;
 	 documentId "Document Id": documentId, disable;
     firstName "First name" : firstName, text_field, disable;
     lastName "Last name" : lastName, text_field, disable;
     nickname "Nickname" : nickname, text_field, default substring(firstName,0,1) + "" + lastName, disable;
     sex "Sex": text_field, disable;
 }


form InvoiceReviewForm "Invoice Review"
    on_load load
{
    header { message(title); };

    invoices : table {
        id "Id" : Int, display, on_click navigate;
        items "Items" : subform(ItemsForm), display items.count + " Items";
        payments "Payments" : subform(PaymentsForm), display "$ " + payments.received;
        customer "Customer" : subform(CustomerReviewForm), display customer.lastName + ", " + customer.firstName;
        date "Date" : Date, disable;
    };
}
