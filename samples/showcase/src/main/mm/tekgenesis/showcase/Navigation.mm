package tekgenesis.showcase schema showcase;

entity NamedItem "Named Item"
	primary_key idKey
	described_by name
	searchable
{
	idKey "Id" : Int;
	name "Name" : String(20);
	color "Color" : String(30);
}


form ItemForm "Item Form"
    entity NamedItem
{
    header {
        message(title);
        "View Source" : label, link "/sg/view/source/tekgenesis.showcase.ItemForm", style "pull-right";
    };

    prev : Int, internal, default -1;
    next : Int, internal, default -1;
    data : ViewData, internal;

    id   "Id"   : idKey;
    name "Name" : name;
    color "Color" : color, default "blue";

    footer {
        "Previous" : button, on_click prev, hide when prev == -1;
        "Close" : button(cancel);
        "Next" : button, on_click next, hide when next == -1;
    };
}

entity ViewData "View Data" {
    current : Int;
    items : entity Listing* {
        pk : Int;
    };
}

form ItemViewer "Item Viewer"
    entity ViewData
{
    header {
        message(title);
        "View Source" : label, link "/sg/view/source/tekgenesis.showcase.ItemViewer", style "pull-right";
    };
    id, internal, optional;
    colors "Colors" : tags, on_change refresh;

    items : table {
        item "Id" : Int, disable;
        name "Name" : String(20);
        color "Color" : String(30);
        "View" : button, on_click navigate;
    };
    horizontal, style "margin-top-20" {
        addRowBottom "Add" : button(add_row, items), style "margin-right-5";
        removeRowBottom "Remove" : button(remove_row, items);
    };
}

/*entity Simple "Simple"
{
    name : String(30);
    complex : Complex;
}

entity Complex "Complex"
{
    simples : Simple*;
}


form ComplexForm "Complex Form"
    entity Complex
{
    id      "Id"      : id, internal;
    simples "Simples" : simples, table {
        sid "Id" : id;
        name "Name" : name;
    };
    horizontal, style "form-actions" {
        button(save);
        button(cancel);
        button(delete), style "pull-right";
    };
}*/

form NavigateWithParameters
    parameters name, oneOption, someOptions, oneDate, decimal, oneDateTime, someInt, oneEntity, moreEntity
    on_load load
{
    header{
        message(title);
        "View Source" : label, link "/sg/view/source/tekgenesis.showcase.NavigateToMe", style "pull-right";
    };

    name "Name": String(13);
    oneOption "One Option": Options;
    someOptions "Some Options": Options, tags_combo_box;
    oneDateTime "One Date, No Time": Date;
    oneDate "One Date": DateTime;
    decimal "One Decimal": Decimal(9,3);
    someInt "One Int": Int;
    oneEntity "One Entity": NamedItem;
    moreEntity "Some More Entities": NamedItem, tags_suggest_box;

    horizontal {
		navi "Navigate To Me!": button, on_click navigate;
	    back "Back Back Back (watch time go back)": button(cancel);
	};
}