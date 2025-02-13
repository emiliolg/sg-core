package models;

final type Address {
    street     : String(50);
    number     : Int;
    country    : String;
}


entity Country
    primary_key code
    deprecable
    remotable
    described_by code
{
    code            "Code" : String(2);
    description            : String(40);
}

entity Customer "Customer"
    primary_key documentType, documentId
    described_by firstName, lastName
    searchable by {
        firstName: firstName,  boost 1;
        lastName: lastName,  boost 1;
        documentId: documentId,  boost 1;
    }
    index last(lastName, firstName)
    unique nick(nickname)
    table MODELS.CUST
{
    documentType            : DocType;
    documentId   "Document" : Decimal(10);
    firstName               : String(50), column FIRST;
    lastName                : String(50);
    nickname     "Username" : String(50);
    sex                     : Sex;
    state                   : State, column (CCODE, SCODE);
    sports                  : Sport*, default FOOTBALL;
    banks                   : entity Bank* described_by customer, seqId {
        name            : String;
    };
}

enum DocType {
    PASSPORT;
    DNI;
}

importer task ImportTask "Import Task"
    pattern "*.csv";


lifecycle task LifeTask "Life Task"
    cluster;


lifecycle task LifeTaskNode "Life Task";


task MyTask "My Task";


final type Person {
    countries    : String*;
}

processor task ProcesorTask "Processor Task"
    schedule "0/1"
    transaction each 10;


runnable task RunnableTask "Runnable Task"
    exclusion_group "A";


enum Sex {
    MALE;
    FEMALE;
}

enum Sport {
    FOOTBALL;
    TENNIS;
    RUGBY;
}

entity State
    primary_key country, code
    described_by country, code
{
    country     "State" : Country;
    code        "Code"  : String(2);
    description         : String(40);
}
