package tekgenesis.test.basic schema BasicTest;

entity DeprecableEntity "Deprecable entity"
	described_by firstName, lastName
    deprecable
    searchable
    primary_key documentNumber
{
	firstName "Name" : String(20);
	lastName "Last name" : String(256);
	documentNumber "Document number" : Int;
	email "E-mail" : String(256), optional;
}