package tekgenesis.test.basic schema BasicTest;

entity AuditableCustomer "Auditable Customer"
    primary_key documentType, documentId,sex
    described_by firstName, lastName
    searchable by {
        firstName;
        lastName;
        documentId;
    }
    unique nick(nickname)
    index last(lastName, firstName)
    auditable
{
	documentType  		: DocType;
	documentId "Document"   : Decimal(10);
	firstName        	: String(50);
	lastName         	: String(50);
	nickname "Username"     : String(50);
	sex 		        : Sex;
	photo               : Resource, optional;
}
