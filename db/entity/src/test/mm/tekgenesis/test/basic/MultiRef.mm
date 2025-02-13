package tekgenesis.test.basic schema BasicTest;

entity A "Entity A"
   primary_key a
   described_by x
{
   a "A entity Key" : Int;
   x "A entity Value" : Int;
}

entity B "Entity B"
   primary_key bb
   described_by y
{
   bb "B entity Key" : Int;
   y "B entity Value" : Int;
}

entity C "Entity C - MultiRef"
   primary_key a, bb
   described_by z
{
   a "C entity Key(A)" : A;
   bb "C entity Key(B)" : B;
   z "C entity Value" : Int;
}

entity D "Entity D - MultiRef"
   primary_key c, d
   described_by t
{
   c "D entity Key(C)" : C;
   d "D entity Key(D)" : Int;
   t "D entity Value" : Int;
}

entity E "Entity E - Multiref"
   primary_key idKey
   described_by idKey
   index A(a)
   unique B(bb)
{
   idKey "TableRef entity Key" : Int;
   a "TableRef entity Value" : A;
   bb "TableRef entity Value" : B;
   c "TableRef entity Value" : C, optional ;
}

entity F "Entity F - MultiRef"
   primary_key idKey
{
   idKey : Int;
   c : C;
}

entity G "Entity G - MultiRef"
   primary_key a, g
   unique Inverse(bb, g)
   index A(a)
{
   g "Entity value" : Int;
   a "Entity ref(A)" : A;
   bb "Entity ref(B)" : B;
   h : H using gs;
}

entity H "Entity H" {
    name : String(256);
    h1 : H, optional;
    h2 : H, optional;
    g  : G, optional;
    gs : G*;
}
