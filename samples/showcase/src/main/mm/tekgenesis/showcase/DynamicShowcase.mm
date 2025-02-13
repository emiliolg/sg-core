package tekgenesis.showcase;

type Value = String(100);

enum PropertyType "Property Type" {
    STRING;
    INT;
    REAL;
    BOOLEAN;
    DATE;
}

entity Property "Property"
    primary_key name, type
    searchable
    described_by name
{
    name      : String(20);
    type      : PropertyType;
    multiple  : Boolean;
    values    : entity ValidValue* described_by value {
        value           : Value;
    };
}

form PropertyForm "Property Form" entity Property;

entity DynamicProperty "Dynamic Property"
    described_by property
{
    property : Property;
    values    : entity DynamicValue* described_by value {
        value           : Value;
    };
}

form DynamicForm "Dynamic" {
    header {
        message(title);
        "View Source" : label, link "/sg/view/source/tekgenesis.showcase.DynamicForm", style "pull-right";
    };

    widgets "Dynamic Properties" : DynamicProperty, table {
        id, internal, optional;
        mandatory "Mandatory" : Boolean, default true;
        property, combo_box, on_change updateDynamic;
        value "Component value" : dynamic, multiple, optional when !mandatory;
        "Model value" : String, is value;
        clear "Clear" : button, on_click clear, hide when size(value) == 0;
    };
    horizontal, style "margin-top-20" {
        addRowBottom "Add" : button(add_row, widgets), style "margin-right-5";
        removeRowBottom "Remove" : button(remove_row, widgets);
    };

    mail "Mail" : dynamic;
    mailInternal : Boolean, internal, default true;
    button "Change me": button, on_click changeMailType;

    range "Component Range" : dynamic, multiple, on_change rangeChanged;
    rangeValue "Component Range Value" : dynamic;
    radioDynamic "Radio dynamic": dynamic;


    combo "Dynamic Type": DynOptions, on_change updateDyn;
    dynimicValue "Value" : dynamic;

    footer {
        button(save);
        button(validate), on_click onValidate;
    };
}

enum DynOptions {
    STRING      : "String";
    STRING5     : "String5";
    INT         : "Int";
    INT5        : "Int5";
    REAL        : "Real";
    DECIMAL     : "Decimal";
    DECIMAL42   : "Decimal(4,2)";
    DATE        : "DateOnly";
    STRING_SECRET: "String Secret";
}

form OptionWidgets {
    rawOptions "Options (Default Type)" : vertical {
        tags "Tags" : tags;
    };

    realOptions "Options (Real Type)" : vertical {
        realCombo "ComboBox": Real, combo_box;
        realTagsCombo "TagsComboBox": Real, tags_combo_box ;
        realCheckBoxGroup "CheckboxGroup": Real, check_box_group ;
        realRadiogroup "RadioGroup": Real, radio_group;
        realListbox "ListBox" : Real, list_box;
    };

    decimalOptions "Options (Decimal Type)" : vertical {
        decimalCombo "ComboBox": Decimal(10,3), combo_box;
        decimalTagsCombo "TagsComboBox": Decimal(10,3), tags_combo_box ;
        decimalCheckBoxGroup "CheckboxGroup": Decimal(10,3), check_box_group ;
        decimalRadiogroup "RadioGroup": Decimal(10,3), radio_group;
        decimalListbox "ListBox" : Decimal(10,3), list_box;
    };

    stringOptions "Options (String Type)" : vertical {
        stringCombo "ComboBox": String, combo_box;
        stringTagsCombo "TagsComboBox": String, tags_combo_box ;
        stringCheckBoxGroup "CheckboxGroup": String, check_box_group ;
        stringRadiogroup "RadioGroup": String, radio_group;
        stringListbox "ListBox" : String, list_box;
    };

    enumOptions "Options (Enum Type)" : vertical {
        enumCombo "ComboBox": Options, combo_box;
        enumTagsCombo "TagsComboBox": Options, tags_combo_box ;
        enumCheckBoxGroup "CheckboxGroup": Options, check_box_group ;
        enumRadiogroup "RadioGroup": Options, radio_group;
        enumListbox "ListBox" : Options, list_box;
    };

    entityOptions "Option (Entity Type)" :vertical{
        entityCombo "ComboBox": Make, combo_box;
        entityTagsCombo "TagsComboBox": Make, tags_combo_box ;
        entityCheckBoxGroup "CheckboxGroup": Make, check_box_group ;
        entityRadiogroup "RadioGroup": Make, radio_group;
        entityListbox "ListBox" : Make, list_box;
    };

    button : button, on_click some;
}