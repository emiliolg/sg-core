package tekgenesis.showcase schema showcase;

type A = TypeA;
type B = TypeB;
type C = TypeC;
type D = Decimal(10,2);
type I = Int;
type R = Real;
type T = DateTime;
type S = String(60);
entity TypeC "Type C"
    described_by a
    searchable
{
    a : S;
}
entity TypeA "Type A"
	described_by d
	searchable
{
	d "Decimal Value" : D;
	i "Int Value" : I;
	r "Real Value" : R;
}

entity TypeB "Type B"
	described_by s
	searchable
{
	s "String Value" : S;
	t "DateTime Value" : T;
}

form FormA entity A;

form FormB entity B;
form FormC entity C;
form FormD {
    a "Type A" : A, on_new some;
    b "Type B" : B;
    c "Type C" : C;

    s "Type S" : S;

    table : A, table {
        d;
        i;
        r;
    };
    horizontal, style "margin-top-20" {
        addRowBottom "Add" : button(add_row, table), style "margin-right-5";
        removeRowBottom "Remove" : button(remove_row, table);
    };
}

final type PersonType {
    name : String(50);
    age : Int;
}

final type MultipleTypes {
    decimals: Decimal(10, 2)*;
    strings: String*;
    ints: Int*;
    reals: Real*;
    bools: Boolean*;
    dates: Date*;
    dateTimes: DateTime*;
    entities: PersonType*;
}

form TypeTest
    on_load load
{
    header { message(title); };

    id : String(50), on_ui_change changed;

    personType : PersonType, internal;
}

// For testing purposes only, not meant to be displayed on application
/*
menu TypesMenu {
    FormA;
    FormB;
    FormC;
    FormD;
}*/
