package tekgenesis.multiref;

entity A "Entity A"
    primary_key a
    described_by x
{
    a "A entity Key"   : Int;
    x "A entity Value" : Int;
}

entity B "Entity B"
    primary_key b
    described_by y
{
    b "B entity Key"   : Int;
    y "B entity Value" : Int;
}

entity C "Entity C - MultiRef"
    primary_key a, b
    described_by z
{
    a "C entity Key(A)" : A;
    b "C entity Key(B)" : B;
    z "C entity Value"  : Int;
}

entity D "Entity D - MultiRef"
    primary_key c, d
    described_by t
{
    c "D entity Key(C)" : C;
    d "D entity Key(D)" : Int;
    t "D entity Value"  : Int;
}

entity E "Entity E - Multiref"
    primary_key eId
    unique B(b)
    index A(a)
{
    eId "TableRef entity Key"   : Int;
    a  "TableRef entity Value" : A;
    b  "TableRef entity Value" : B;
}

entity F "Entity F - MultiRef"
    primary_key fId
{
    fId    : Int;
    c     : C;
}

entity G "Entity G - MultiRef"
    primary_key a, g
    unique Inverse(b, g)
    index A(a)
{
    g "Entity value"  : Int;
    a "Entity ref(A)" : A;
    b "Entity ref(B)" : B;
}
