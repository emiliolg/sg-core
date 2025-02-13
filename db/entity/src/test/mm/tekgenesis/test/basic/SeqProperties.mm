/*
* Inner reference sample
*/
package tekgenesis.test.basic schema BasicTest;

entity SeqProperties
    described_by name, description
{
    name	: String(50);
    description	: String(100), optional;
    properties 	: entity SeqProperty* {
		name	: String(50);
		values 	: entity SeqValue* {
		    name : String(50);
		    value : SeqObject;
		};
	};
}

entity SeqObject
    described_by value
{
    value : String(50);
}

entity SeqOptionalProperties
    described_by name
{
    name : String(50);
    properties : SeqProperties;
}