package tekgenesis.snippets;

enum Sex {
    MALE;
    FEMALE;
}

//#innerMultiple
entity InnerMultiplePerson
	primary_key ssn
	described_by first, last
	searchable
{
	ssn "SSN #"        : Int(9);
	first "First name" : String(30);
	last "Last name"   : String(30);
	birth "Birthday"   : Date;
	addresses          :  entity MultipleInnerAddress* {
	    street: String;
	    addressId: String;
	    zipCode: String;
	};
	sex                : Sex, default FEMALE;
}
//#innerMultiple

form InnerMultiplePersonForm "Inner Multiple Person Form"
    entity InnerMultiplePerson
{
    header {
        message(entity), col 8;
        search_box, col 4, style "pull-right";
    };
    "SSN #"      : ssn, mask decimal;
    "First name" : first;
    "Last name"  : last;
    "Birthday"   : birth;
    "Addresses"  : addresses, table {
        "Street"     : street;
        "Address Id" : addressId;
        "Zip Code"   : zipCode;
    };
    horizontal, style "margin-top-20" {
        button(add_row, addresses), style "margin-right-5";
        button(remove_row, addresses);
    };
    "Sex"        : sex, default FEMALE;
    footer {
        button(save);
        button(cancel);
        button(delete), style "pull-right";
    };
}


//#simpleEntity
entity SimplePerson
	primary_key ssn
{
	ssn "SSN #"        : Int(9);
	first "First name" : String(30);
	last "Last name"   : String(30);
}
//#simpleEntity

//#externalMultiple
entity ExternalAddress {
    street: String;
    person: ExternalMultiplePerson;
}

entity ExternalMultiplePerson
	primary_key ssn
	described_by first, last
	searchable
{
	ssn "SSN #"        : Int(9);
	first "First name" : String(30);
	last "Last name"   : String(30);
	birth "Birthday"   : Date;
	addresses          : ExternalAddress*;
	sex                : Sex, default FEMALE;
}

//#externalMultiple
//#simpleCache
entity SimpleCacheEntity
    cache
{
    simpleId:String;
}
//#simpleCache

//#allCache
entity AllCacheEntity
    primary_key simpleId
    cache all
{
    simpleId:String;
}
//#allCache

//#qtyCache
entity QuantifiedCacheEntity
    cache 200
{
    simpleId:String;
}
//#qtyCache