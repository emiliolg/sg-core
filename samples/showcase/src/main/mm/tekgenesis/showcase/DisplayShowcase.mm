package tekgenesis.showcase;

form DisplayShowcase "Displays"

{
    header {
        message(title);
        "View Source" : label, link "/sg/view/source/tekgenesis.showcase.DisplayShowcase", style "pull-right";
    };

    hidePanel "Hide Panel" : check_box;

    hiddenPanel "Hidden Panel" : horizontal, hide when hidePanel == true {

        displayCombo "Display" : DisplayOptions;

        vertical, hide when displayCombo == HIDE {
            text1 "Input #1" : String(20), default "disable", disable when displayCombo == DISABLE;
            text2 "Input #2" : String(20), hint "Change Input #1", disable when (displayCombo == DISABLE || text1=="disable"), placeholder "look up!";
        };
    };

    simpleSync "Simple sync" : button, on_click simpleSync;

    vertical {

        horizontal{
            disableCheck "Disable All": check_box;
            hideCheck "Hide Some": check_box;
        };

        input "Text Field": text_field, disable when disableCheck == true;
        combo "ComboBox": Options, disable when disableCheck == true, hide when hideCheck == true;
        radiogroup "RadioGroup": Options, radio_group , disable when disableCheck == true, hide when hideCheck == true;
        button "Button" : button, disable when disableCheck == true, on_click addRow;
        checkbox "Checkbox": check_box, disable when disableCheck == true, hide when hideCheck == true;

    };

    items :  table {
        checkItem "Disable" : Boolean;
        numberItem "#" : Int, disable when checkItem, check numberItem > 3 : "Must be greater than 3", hint checkItem ? "" : "##";
    };
}

form DisplayRadioTag
    on_load load
{

    header {
        message(title);
        viewSource "View Source" : label, link "/sg/view/source/tekgenesis.showcase.DisplayRadioTag", style "pull-right";
    };

    checkBox "Button or alert styles?" : check_box, on_ui_change changeStyles;
    radioTags "Radio" : SimpleEntity, radio_group, on_change changed;
}

enum DisplayOptions
{
    VISIBLE     : "Visible";
    DISABLE     : "Disable";
    HIDE        : "Hide";
}

form LoadDisplayForm
    on_load load
    on_display display
{

    header {
        message(title);
        viewSource "View Source" : label, link "/sg/view/source/tekgenesis.showcase.LoadDisplayForm", style "pull-right";
    };

    string "String" : String, optional;

    button(save);
    click "Click!" : button;
}
