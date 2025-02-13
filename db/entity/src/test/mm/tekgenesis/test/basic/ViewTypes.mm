package tekgenesis.test.basic schema BasicTest;

view MiniCustomer "View of Client" of Customer
    described_by nombre, apellido
{
    nombre    "Nombre" : firstName;
    apellido           : lastName;
    documento          : documentId;
}

view MiniCustomerUpdatable "Updatable View of Client" of Customer
    described_by nombre, apellido
    updatable
{
    nombre    "Nombre" : firstName;
    apellido           : lastName;
    documento          : documentId;
}

view MiniPreferences "Updatable View of Preferences" of Preferences
    updatable
{
    cliente : customer;
    correo : mail;

}


view CustomerSqlView
    of Customer
    as """
         select DOCUMENT_ID, FIRST_NAME, LAST_NAME, P.MAIL
         from TableName(BASIC_TEST, CUSTOMER)
         join TableName(BASIC_TEST, PREFERENCES) P
         on DOCUMENT_TYPE = P.CUSTOMER_DOCUMENT_TYPE and DOCUMENT_ID = P.CUSTOMER_DOCUMENT_ID and SEX = P.CUSTOMER_SEX
    """
    described_by firstName, lastName
    primary_key documentId
{
    documentId "Document" : Decimal(10);
	firstName        	: String(50);
	lastName         	: String(50);
	mail        "Email" : String(60);
}

