package domain;

entity Plan "Plan"
	described_by name
{
	name "Name" : String(20);
	description "desc" : String(50);
}

form PlanForm "Plan Form"
    entity Plan
{
    header {
        message(title);
    };
    "Id"   : id, internal, optional;
    "Name" : name;
    "description" : Boolean, <caret>;
    footer {
        button(save);
        button(cancel);
        button(delete), style "pull-right";
    };
}


menu PlanMenu
{
	opt1 "Plan" : PlanForm;
}