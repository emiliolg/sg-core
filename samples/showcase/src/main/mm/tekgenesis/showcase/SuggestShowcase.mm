package tekgenesis.showcase;

form TagSuggestBoxOnNew {

    header {
        message(title);
        viewSource "View Source" : label, link "/sg/view/source/tekgenesis.showcase.TagSuggestBoxOnNew", style "pull-right";
    };

    tags "Enter tags" : Tag, tags_suggest_box, on_new createAndTag;
}

entity Tag
    described_by name
    searchable
    primary_key name
{
    name : String(20);
}

form SuggestBoxShowcase "SuggestBoxes"
    on_load load
{
    header {
        message(title);
        "View Source" : label, link "/sg/view/source/tekgenesis.showcase.SuggestBoxShowcase", style "pull-right";
    };

    today : Date, internal, default today();

    options "Options suggest" : Options, suggest_box, hint "enum type suggest box";

    entity "Entity suggest" : SimpleEntity, hint "SimpleEntity type suggest box", on_change entityChanged, optional;
    entityWithSuggest "Entity with suggest" : SimpleEntity, on_suggest suggestEntity, hint "SimpleEntity type suggest box with on suggest", optional;

    integer "Integer suggest" : Int, suggest_box, on_suggest suggestInts, hint "integer type suggest box with on suggest", on_change integerChanged, optional;
    strings "String suggest" : String, suggest_box, on_suggest onSuggestStrings(entity), on_ui_change changed, hint "string type suggest box with on suggest(entity)", optional;

    stringsSync "String suggest with sync" : String, suggest_box, on_suggest_sync suggestSync, hint "string type suggest box with on suggest sync", on_change stringsChanged, optional;
    entitySync "Entity suggest with sync" : SimpleEntity, on_suggest_sync entitySuggestSync, hint "entity type suggest box with on suggest sync", optional;

    resetStrings "Reset String suggest" : button, on_click resetStrings;

    withTags "With Tags" : vertical {
        optionTags "Options tags suggest" : Options, tags_suggest_box, hint "enum type tags suggest box", optional;
        entityTags "Entity tags suggest" : SimpleEntity, tags_suggest_box, hint "SimpleEntity type tags suggest box", optional;
        integerTags "Integer tags suggest" : Int, tags_suggest_box, on_suggest suggestInts, hint "integer type tags suggest box with on suggest", optional;
        stringTags "String tags suggest" : String, tags_suggest_box, on_suggest onSuggestStrings(entity), hint "string type tags suggest box with on suggest(entity)", optional;

        stringsTagsSync "String tags suggest with sync" : String, tags_suggest_box, on_suggest_sync suggestSync, hint "string type tags suggest box with on suggest sync", on_change stringsTagsSyncChanged, optional;
        entityTagsSync "Entity tags suggest" : SimpleEntity, tags_suggest_box, on_suggest_sync entitySuggestSync, hint "entity type tags suggest box with on suggest sync", on_change entityTagsSyncChanged, optional;
    };

    table : table {
            columnStrings "String suggest" : String, suggest_box, on_suggest onSuggestStrings(entity), on_ui_change changed, hint "string type suggest box with on suggest(entity)", optional;

            columnStringsSync "String suggest with sync" : String, suggest_box, on_suggest_sync tableSuggestSync, hint "string type suggest box with on suggest sync", on_change stringsChanged, optional;
            columnEntitySync "Entity suggest with sync" : SimpleEntity, on_suggest_sync entitySuggestSync, hint "entity type suggest box with on suggest sync", optional;
    };

    click "Print" : button, on_click click;
    clear "Clear" : button, on_click clear;
}

form Suggester {
    header {
        message(title);
    };

    date : Date, internal, default today();

    simpleSync "Suggest sync" : SimpleEntity, on_suggest_sync suggestSimpleSync;
    simple "Suggest simple" : SimpleEntity, on_suggest suggestSimple;
    simpleParam "Suggest Param": SimpleEntity, on_suggest suggestSimpleParam(date);

    stringSync "String sync"  : String, suggest_box, on_suggest_sync suggestStringSync;
    string "String" : String, suggest_box, on_suggest suggestString;
    stringParam "String param" : String, suggest_box, on_suggest suggestStringParam(date);
}

form DeprecableSuggester "Deprecable Suggester"
    entity AnotherDeprecableEntity
{
    header {
        message(title);
        search : search_box, style "pull-right", on_suggest suggest(name);
    };

    "Id"                        : id, internal, optional;
    "Name"                      : name;

    footer {
        button(save);
        button(cancel);
        button(delete), style "pull-right";
        toggle_button(deprecate), style "pull-right";
    };
}

entity AnotherSimpleEntity
    searchable
    described_by name
{
    name : String(30);
    options: Options*;

    simpleEntities : entity SimpleEntities * {
        simpleEntity : SimpleEntity;
    };
}

form AnotherSimpleEntityForm "Another Simple Entity Form"
    entity AnotherSimpleEntity
{
    header {
        message(title);
        search_box, style "pull-right";
    };

    "Id"              : id, internal, optional ;
    "Name"            : name;
    "Options"         : options;
    simpleEntities "Simple Entities" : SimpleEntity, tags_suggest_box, optional;

    footer {
        button(save);
        button(cancel);
        button(delete), style "pull-right";
    };
}

entity SuggestedPerson
    described_by name, lastName
    searchable
{
    name : String(30);
    lastName : String(30), optional;
}

form SuggestedForm entity SuggestedPerson;

form SuggestedListForm "Suggested List Form"
 on_load load
{
    header {
        message(title), col 12;
    };

    persons "Persons" : SuggestedPerson;

    suggestedPersons : table {
        person "Display" : SuggestedPerson, display;
        suggest "SuggestBox" : SuggestedPerson;
    };
}

