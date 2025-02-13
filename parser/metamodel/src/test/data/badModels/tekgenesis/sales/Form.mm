
package tekgenesis.sales.basic;


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


form CustomerForm "Binded Customer Form"
    entity Customer
{
    documentType;
	documentId;
    firstname;
    lastname;
    sexman;

    horizontal, style "form-actions" {
        button(save);
        button(cancel);
        button(delete), style "pull-right";
    };
}

form UnbindedCustomerForm "Unbinded Customer Form"
    entity Customer
    permissions save, print, print
{
	documentId  "Document"  : Decimaal(10);
    firstname "First"       : String(50);
    lastname     : String(100);
    sex	"Sex"	            : Seax;

    horizontal, style "form-actions" {
        button(save);
        button(cancel);
        button(delete), style "pull-right";
    };
}

form EntityLessForm "Entity-Less Form"
{
	documentId  "Document"  : Decimal(10);
    firstname "First"       : String(50);
    lastname "Last"         : String(100);
    sex;

    horizontal, style "form-actions" {
        "Accept" : button(save);
    };
}

form InvalidWidgetOptionsForm "Invalid Widget Options Form"
    permissions Create, CREATE, create
{
    button;
	button(save);
	button(cancel);
	button(delete);
	button(invalid);
	button(save, invalid);

    tabs;
    tabs(top);
    tabs(right);
    tabs(left);
    tabs(bottom);
    tabs(pill);
    tabs(invalid);
    tabs(top, invalid);

    number1 : Int, check number1 > 10 "Faltan los :";
    number3 : Int, check number3 > 10 : inliiinnee "Inline mal escrito";
    number4 : Int, check number4 > 10 : inline errrrooorr "Error mal escrito";
    number5 : Int, check number5 > 10 : inline warning pepepe "pepepe?";
}
