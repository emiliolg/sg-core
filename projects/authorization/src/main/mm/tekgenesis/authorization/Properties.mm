package tekgenesis.authorization schema authorization;

enum PropertyScope {
    COMPANY;
    ORG_UNIT : "Organizational Unit";
    USER;
    DOMAIN;
}

enum PropertyType {
    STRING;
    INT;
    REAL;
    BOOLEAN;
    DATE;
}

entity Property "Property"
    primary_key id, name, type
    searchable by {
        id;
        name;
    }
    described_by name
{
    id        : String(10);
    name      : String(20);
    type      : PropertyType;
    required  : Boolean;
    scope       : PropertyScope;
}

form PropertyForm "Property"
    entity Property
{
    header {
        message(title);
        search_box, style "pull-right";
    };

    id     "Id"     : id;
    name   "Name"   : name;
    type   "Type"   : type;
    required "Required" : required, disable when type == BOOLEAN, reset;
    scope "Scope" : scope;

    footer {
        button(save);
        button(cancel);
        button(delete), style "pull-right";
    };
}