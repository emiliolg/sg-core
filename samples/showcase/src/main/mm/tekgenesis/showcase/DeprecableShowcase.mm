package tekgenesis.showcase;

entity DeprecableEntity
    described_by name
	searchable
	deprecable
{
	name "Name" : String(20);
	simpleEntity "Simple Entity" : SimpleEntity;
	anotherDeprecableEntity "Another Deprecable Entity" : AnotherDeprecableEntity;
}


form DeprecableEntityForm "Deprecable Entity"
    entity DeprecableEntity
{
    header, no_label {
        message(title), col 8, no_label;
        search_box, col 4, no_label;
    };
    "Id"                        : id, internal, optional;
    "Name"                      : name;
    "Simple Entity"             : simpleEntity;
    "Another Deprecable Entity" : anotherDeprecableEntity;
    footer, no_label {
        button(save), no_label;
        button(cancel), no_label;
        button(delete), style "pull-right", no_label;
        toggle_button(deprecate), style "pull-right", no_label;
    };
}


entity AnotherDeprecableEntity
	described_by name
	searchable
	deprecable
{
	name "Name" : String(20);
}

form ListingAnotherDeprecable listing AnotherDeprecableEntity;

form AnotherDeprecableEntityForm "Another Deprecable"
    entity AnotherDeprecableEntity
{
    header {
        message(title);
        search_box, style "pull-right";
    };
    "Id"   : id, internal, optional ;
    "Name" : name;
    footer {
        button(save);
        button(cancel);
        button(delete), style "pull-right";
        deprecate : toggle_button(deprecate), style "pull-right";
    };
}
