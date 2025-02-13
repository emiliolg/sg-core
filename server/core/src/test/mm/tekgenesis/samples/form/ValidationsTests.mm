package tekgenesis.samples.form;

form FormValidationsTestDefaults "Default Validations"
{
    item1   : Int, default 10, check item1 > 20 : "Item value should be greater than 20";
    item2   : Int, default 10;
    total   : Int, is item1 + item2, check total > 40 : "Total should be greater than 40";
}

form FormValidationsTestInteractive "Check Validations"
{
    from    : Int, check from > 0 : "Should be positive";
    to      : Int, check to > from : "'To' should be greater than 'From'";
}

form FormValidationsTestTable "Table Validations"
{
    optional : Boolean;

    field : String, optional when optional;
    vertical {
        fieldInGroup : String, optional when optional;
    };

    table : table {
        column : String, optional when optional;
        vertical {
            columnInGroup : String, optional when optional;
        };
    };

    horizontal, style "margin-top-20" {
        addRowBottom "Add" : button(add_row, table), style "margin-right-5";
        removeRowBottom "Remove" : button(remove_row, table);
    };
}

entity Some searchable described_by some
{
    some : String;
}

form InternalValidations
{

    header { message(title); };

    string : Some, internal;

    field "Field" : text_field;

    horizontal { button(validate), on_click clicked; };
}
