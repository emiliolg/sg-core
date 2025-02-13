package idea.test;

entity A
    primary_key a, b
    index a, b, c
    unique c
    described_by c
    image d
    auditable
    searchable
{
    a : Int;
    b : String;
    c : Real;
    d : Resource;
}

type AType = A;

type AQualifiedType = idea.test.A;

entity B
{
    a : A;
    b : AType;
    c : AQualifiedType;
}

form C
    on_load load
    on_cancel cancel
    primary_key a, b, c
    parameters d, e
{
    a : A;
    b : AType;
    c : AQualifiedType;
    d : Int;
    e : Int;
}

form D
    entity B
{
    a;
    b;
    c : Int, check a != null : "A must be defined";
    d : A, is b;
    t : table {
        o : Int;
        p : Boolean;
    };
    q : Int, default sum(o);
    r : Int, disable when p;
}
