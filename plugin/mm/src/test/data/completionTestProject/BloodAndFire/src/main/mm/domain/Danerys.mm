package domain;

entity Danerys "Danerys"
	described_by name
{
	name "Name" : String(20);
}

form DanerysForm "Danerys Form"
    entity Danerys
{
    header {
        message(title);
    };
    "Id"   : id, internal, optional;
    "Name" : name;
    "Artsan" : Artsan,<caret>;
    footer {
        button(save);
        button(cancel);
        button(delete), style "pull-right";
    };
}


menu DanerysMenu
{
	DanerysForm;
}