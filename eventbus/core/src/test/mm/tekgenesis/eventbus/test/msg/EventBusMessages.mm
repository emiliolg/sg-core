package tekgenesis.eventbus.test.msg;

enum EnumMessage
{
	NAME : "name";
}

type TypeMessage {
    name : String;
    number: Int;
}

entity EntityMessage
    primary_key  name
{
    name: String;
    number: Int;
}

entity ComplexEntityMessage {
    name :String;
    enumAttr : EnumMessage;
    entity : EntityMessage;
}

