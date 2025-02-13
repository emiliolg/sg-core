package tekgenesis.test.basic schema BasicTest;

type IntType = Int(10);

entity BasicTypes "BasicTypes"
	primary_key idKey
	described_by name
{
	idKey "Id" : Int;
	name "Name" : String(20);
	bool "Bool" : Boolean;
	real "Real" : Real;
	date "Date" : Date;
	dateTime "DateTime" : DateTime(3);
	decimal "Decimal" : Decimal(10,4);
	notLongName "Not LongName" : String(2000);
	notSoLongName "Not So LongName" : String(3000);
	longName "LongName" : String(20000);
	longNullableName "LongName" : String(20000), optional;
    aName "Abstract Name" : String(100), abstract;
    myInt : IntType, default 5;
}

enum DocumentType "Document Type"
with { local: Boolean; desc: String(40),optional;}
{
    DNI  : "DNI", true;
    PASS : "Passport", false, "Passport";
}

entity DatabaseSearchableTypes searchable by database {
    str; integer; decimal; real;
    myEnum: en; opt; nullEnum;
}
{
    opt: String, optional;
    str: String;
    integer: Int;
    decimal: Decimal(10, 2);
    real: Real;
    bool: Boolean;
    date: Date;
    dateTime: DateTime;
    en: DocumentType;
    nullEnum: DocumentType, optional;
    ens: DocumentType*;
    enti: SuperSimple;
}

entity SearchableTypes searchable by {
    str; strs; integer; integers; decimal; decimals; real; reals; bool; bools;
    date; dates; dateTime; dateTimes; myEnum: en; myEnums: ens; myEntity: enti; opt; nullEnum;
}
{
    opt: String, optional;
    str: String;
    strs: String*, abstract, read_only;
    integer: Int;
    integers: Int*, abstract, read_only;
    decimal: Decimal(10, 2);
    decimals: Decimal(10, 2)*, abstract, read_only;
    real: Real;
    reals: Real*, abstract, read_only;
    bool: Boolean;
    bools: Boolean*, abstract, read_only;
    date: Date;
    dates: Date*, abstract, read_only;
    dateTime: DateTime;
    dateTimes: DateTime*, abstract, read_only;
    en: DocumentType;
    nullEnum: DocumentType, optional;
    ens: DocumentType*;
    enti: SuperSimple;
}

entity SuperSimple {
    name: String;
}