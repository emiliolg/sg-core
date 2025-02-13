package models;

entity Country
    primary_key code
    remotable
    deprecable
{
    code "Code" : String(2);
    description : String(40);
}

entity State
    primary_key country, code
{
    country "State" : Country;
    code "Code" : String(2);
    description : String(40);
}

final type Address {
    street : String(50);
    number : Int;
    country : String;
}

entity Customer "Customer"
    primary_key documentType, documentId
    described_by firstName, lastName
    searchable by {
        firstName;
        lastName;
        documentId;
    }
    unique nick(nickname)
    index last(lastName, firstName)
    table CUST
{
    documentType            : DocType;
    documentId   "Document" : Decimal(10);
    firstName               : String(50), column FIRST;
    lastName                : String(50);
    nickname     "Username" : String(50);
    sex                     : Sex;
    state                   : State, column (CCODE, SCODE);
    sports                  : Sport*, default FOOTBALL;
    banks   : entity Bank* auditable { name : String;};
}

final type Person {
    countries : String*;
}

enum Sport {FOOTBALL; TENNIS; RUGBY;}

enum DocType { PASSPORT; DNI; }

enum Sex { MALE; FEMALE; }

task MyTask "MyTask";

importer task ImportTask "ImportTask"
    pattern "*.csv";

processor task ProcesorTask "ProcessorTask"
    schedule "0/1"
    transaction each 10;

runnable task RunnableTask "RunnableTask"
    exclusion_group "A";

lifecycle task LifeTask "LifeTask"
    cluster;

lifecycle task LifeTaskNode "LifeTask";

