package domain;


entity Quentyn "Quentyn"
	described_by name
{
	name "Name" : String(20);
	description "desc" : String(50);
}

form QuentynForm "Quentyn Form"
    entity Quentyn
{
    header {
        message(title);
    };
    "Id"   : id, internal, optional;
    "Name" : name;
    "description" : description,<caret>;
    footer {
        button(save);
        button(cancel);
        button(delete), style "pull-right";
    };
}


menu QuentynMenu
{
	opt1 "Quentyn" : QuentynForm;
}