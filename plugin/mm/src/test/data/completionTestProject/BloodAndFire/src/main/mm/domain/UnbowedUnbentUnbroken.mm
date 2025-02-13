package domain;

entity UnbowedUnbentUnbroken "UnbowedUnbentUnbroken"
	described_by name
{
	name "Name" : String(20);
	martels "martels" : Martels*;
	yronwoods "yronwoods" : YronWoods*;
}

form UnbowedUnbentUnbrokenForm "Unbowed Unbent Unbroken Form"
    entity UnbowedUnbentUnbroken
{
    header {
        message(title);
    };
    "Id"   : id, internal, <caret>;
    "Name" : name;
    footer {
        button(save);
        button(cancel);
        button(delete), style "pull-right";
    };
}


menu UnbowedUnbentUnbrokenMenu
{
	opt1 "UnbowedUnbentUnbroken" : UnbowedUnbentUnbrokenForm;
}
entity YronWoods "YronWoods"
{
    unBowedUnBen "ununu": UnbowedUnbentUnbroken;
    name "name" : String(50);
}

entity Martels "Martels"
{
    UnBowedUnbent "ununun": UnbowedUnbentUnbroken;
    name "Name"  : String(50);

}