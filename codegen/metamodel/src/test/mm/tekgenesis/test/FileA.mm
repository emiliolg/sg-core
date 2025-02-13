package tekgenesis.test;

entity EA
    primary_key a, b
    described_by b, c
    searchable
{
    a : Int;
    b : Decimal(10, 2);
    c : String;
}

entity EB
    primary_key a, b
    described_by b, c
{
    a : Int;
    b : Decimal(10, 2);
    c : String;
}