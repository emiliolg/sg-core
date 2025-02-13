package tekgenesis.showcase;

type DateString = String(25);

form GroupShowcase "Groups"
on_load load

{
    header {
        message(title);
        "View Source" : label, link "/sg/view/source/tekgenesis.showcase.GroupShowcase", style "pull-right";
    };



    "Horizontal Group" :horizontal, collapsible {
        "Text 1": text_field;
        "Text 2": text_field;
    };

    "Vertical Group" :vertical, collapsible, style "bordered", default true {
        "Text 1": text_field;
        "Text 2": text_field;
    };

    pop2  "click me!" : popover(bottom), style "btn btn-info", on_change popChange, content_style "open-right",
     icon chevron_right, hover, icon_selected chevron_left {
        header {
            "Im the header" : label;
        };
//        popData : message(plain);
//        "TextField 1": text_field;
//        "TextField 2": text_field;
    };

    "Label_Col 4" :vertical, label_col 4 {
        "Long Label could span 4 spaces": text_field;
        "And another one !": text_field;
    };


    "Horizontal custom cols" :horizontal, collapsible, label_col 3, style "bordered"{
        "Text 1": text_field, col 4;
        "Text 2": text_field, col 8;
    };

    "Input groups" : vertical {
        "Input Group" : input_group {
            "Text 1": text_field;
            "Text 2": text_field;
        };

        "Input Group" : input_group {
            "Text 1": text_field, col 6;
            "Text 2": text_field, col 2;
            "Text 2": text_field, col 2;
        };
        "Input Group, label_col 4" : input_group, label_col 4 {
            "Text 1": text_field;
            "Text 2": text_field;
        };
    };

    "Offsets" : vertical {
        "No offset": text_field;
        "Offset 1" : text_field, col 11, offset_col 1;
        "Offset 2" : text_field, col 10, offset_col 2;
        "Offset 3" : text_field, col 9,  offset_col 3;
        "Offset 4" : text_field, col 8,  offset_col 4;
    };

    "Offsets" : horizontal {
        "text1" : text_field, col 4;
        "text2" : text_field, col 4, offset_col 4;
    };

    "Horizontal Group" :horizontal, collapsible {

        "Nested Vertical Group" :vertical{

            "Nested Horizontal Group" :horizontal, label_col 4{
                "Text 1": String(20), text_field;
                "Text 2": String(20), text_field;
            };

             "Nested Horizontal Group" :horizontal, label_col 4{
                "Text 1": String(20), text_field;
                "Text 2": String(20), text_field;
            };
        };


        "Nested Vertical Group" :vertical{
            "Text 1": String(20), text_field;
            "Text 2": String(20), text_field;
        };
    };

    "Text 1": text_field;
    "Text 1": text_field;
    "Text 1": text_field;

    "Input Group" : input_group {
        "Text 1": text_field;
        "Text 2": text_field;
    };

    horizontal {
        "Text 1": text_field;
        "Text 2": text_field;
    };

    "Label col 4 inputGroup" : input_group, label_col 4{
        "Text 1": text_field;
        "Text 2": text_field;
    };

    "Offset 1" : text_field, col 11, offset_col 1;

    sect : section {
        vertical, col 4, label_col 3{
            "Text 1": text_field;
            "Text 2": text_field;
            "Some btn" : button, col 4, offset_col 3;
        };
    };

    "Horizontal" : horizontal{
        combo "ComboBox": Options, combo_box;
        combo2 "ComboBox2": Options, combo_box;
    };


    "Horizontal" : horizontal{
        Options, combo_box;
        Options, combo_box;
    };


    tab1 "Tabbed Group" : tabs, on_change tabChanged {

        t1 "Tab 1" :vertical, icon comments {

            tf1 "TextField 1": DateString;
            tf2 "TextField 2": DateString;

        }  ;

        t2 "Tab 2" :horizontal, icon comments {

            tf3 "TextField 1": DateString;
            tf4 "TextField 2": DateString;

        };

    };

}

form ValidateGroupForm "Validate Group Form" {

    hor "Horizontal" : horizontal {
        horInt "Hor Int": Int;
        horVert "Hor Vert": vertical {
            mail  "Mail ": mail_field(syntax), optional ;
            horVertInt "Hor Vert Int": Int;
        };
        horTable "Hor Table": table {
            horTableInt "Hor Table Int" : Int;
        };
        addButton: button(add_row);
        removeButton: button(remove_row, horTable);
        exportButton: button(export);
    };
    ver  "Ver ": horizontal {
        verInt "Ver Int": Int;
    };
    footer  "Footer ": horizontal {
        footInt "Foot Int": Int;
    };
    dialog "Dialog": dialog, display "Dialog" {
        dialogInt "Dialog Int": Int;
    };

    horButt "Hor Validate": button(validate, hor), on_click getDefault;
    verButt "Ver Validate": button(validate, ver), on_click getDefault;
    footButt "Foot Validate": button(validate, footer), on_click getDefault;
    dialogButt "Dialog Validate": button(validate, dialog), on_click getDefault;
    allButt "All Validate": button(validate), on_click getDefault;
}

form ResetGroupForm "Reset Group Form" {

    hor "Horizontal" : horizontal, disable when horButt, reset{
        horInt "Hor Int": Int;
        horVert "Hor Vert": vertical {
            mail  "Mail ": mail_field(syntax);
            horVertInt "Hor Vert Int": Int;
        };
        horTable "Hor Table": table {
            horTableInt "Hor Table Int" : Int;
        };
        addButton: button(add_row);
        removeButton: button(remove_row, horTable);
    };
    ver  "Ver ": horizontal, disable when verButt, reset {
        verInt "Ver Int": Int;
    };
    footer  "Footer ": horizontal, disable when footButt, reset {
        footInt "Foot Int": Int;
    };
    dialog "Dialog": dialog, display "Dialog", disable when dialogButt, reset {
        dialogInt "Dialog Int": Int;
    };

    horButt "Hor Reset": Boolean, default false;
    verButt "Ver Reset": Boolean, default false;
    footButt "Foot Reset": Boolean, default false;
    dialogButt "Dialog Reset": Boolean, default false;
}