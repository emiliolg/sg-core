package tekgenesis.test schema MODEL;

entity AWithInner
    primary_key code
    described_by code
    table MODEL.A_WITH_INNER
{
    code               : Int;
    fieldA             : String(30);
}

entity Address
    primary_key code
    described_by code
{
    code               : String(64);
    room               : String(64);
    street             : String(64);
}

entity AddressPhone
    primary_key docType, docCode, addressSeqId, seqId
    described_by docType, docCode, addressSeqId, seqId
{
    docType            : Int;
    docCode            : String(20);
    addressSeqId       : Int;
    seqId              : Int;
    phone              : Int;
}

entity Category
    primary_key code
    described_by code
{
    code               : String(30);
    name               : String(30);
    parentCode         : String(30);
}

entity Dummy
    described_by id
{
    name               : String;
}

entity DummyInner
    primary_key dummyId, seqId
    described_by dummyId, seqId
{
    dummyId            : Int;
    seqId              : Int;
    id2                : Int;
}

entity InnerA
    primary_key aWithInnerCode, seqId
    described_by aWithInnerCode, seqId
{
    aWithInnerCode     : Int;
    seqId              : Int;
    innerFieldA        : Int;
}

entity Jose
    described_by id
{
    ident              : Int;
}

entity Location
    described_by id
{
    name               : String(40);
}

entity Participant
    primary_key pid
    described_by pid
{
    pid                : Int;
    firstName          : String(64);
    lastName           : String(64);
    addressCode        : String(64);
}

entity Person
    primary_key docType, docCode
    described_by docType, docCode
{
    docType            : Int;
    docCode            : String(20);
    firstName          : String(64);
    lastName           : String(64);
    birthday           : Date;
    salary             : Decimal(10, 2);
    sex                : String(2);
}

entity PersonAddress
    primary_key docType, docCode, seqId
    described_by docType, docCode, seqId
{
    docType            : Int;
    docCode            : String(20);
    seqId              : Int;
    street             : String(64);
}

entity Product
    primary_key code
    described_by code
{
    code                : String(64);
    name                : String(64);
    mainCategoryCode    : String(64);
    color               : Int;
    optCategoryCode     : String(64);
}

entity Sequencer1
    described_by id
{
    firstName          : String(64);
    lastName           : String(64);
}

entity Sequencer2
    primary_key id
    described_by id
{
    id                 : Int(18);
    firstName          : String(64);
    lastName           : String(64);
}

entity SerialNumber
    primary_key productId, serialNumber
    described_by productId, serialNumber
{
    productId          : Int;
    serialNumber       : String(64);
    locationId         : Int;
}

entity Types
    primary_key int1
    described_by int1
{
    int1               : Int;
    num3               : Decimal(3);
    num92              : Decimal(9, 2);
    real1              : Real;
    date1              : Date;
    dt0                : DateTime;
    dt1                : DateTime(1);
    dt3                : DateTime(3);
    dt6                : DateTime(6);
    bool               : Boolean;
    str                : String(10);
}
