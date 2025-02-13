package tekgenesis.test;

entity EC
    primary_key a, b
    described_by b, c
{
    a : Int;
    b : Decimal(10, 2);
    c : String;
}

entity ED
    primary_key a, b
    described_by b, c
{
    a : Int;
    b : Decimal(10, 2);
    c : String;
}

enum EnumA {
    A;
    B;
}

enum EnumB {
    A;
    B;
}