package tekgenesis.showcase;

form TableGroupHideShowcase "Tables Group"
    on_load load
{

    header {
        message(title);
        "View Source" : label, link "/sg/view/source/tekgenesis.showcase.TableGroupHideShowcase", style "pull-right";
    };

    external "Hide External" : Type, default A;

    table : table {
        type "Hide" : Type, default A;

        "Group (internal ref)" : horizontal {
            a : display, hide when type != A, optional when type != A, default "A";
            b : display, hide when type != B, optional when type != B, default "B";
            c : display, hide when type != C, optional when type != C, default "C";
        };

        "Group  (external ref)" : horizontal {
            x "X" : button, hide when external != A;
            y "Y" : button, hide when external != B;
            z "Z" : button, hide when external != C;
        };
    };
    horizontal, style "margin-top-20" {
        addRowBottom "Add" : button(add_row, table), style "margin-right-5";
        removeRowBottom "Remove" : button(remove_row, table);
    };
}

form TableLocalGlobalOptionsShowcase "Tables Options"
    on_load load
{
    header {
        message(title);
        "View Source" : label, link "/sg/view/source/tekgenesis.showcase.TableLocalGlobalOptionsShowcase", style "pull-right";
    };
    resetCombos "Reset combos": button, on_click resetCombos;
    horizontal {
        oneOptionCombo "One option": Int, combo_box;
        threeOptions   "Three options": Int, combo_box;
    };

    option "OptionWidgets" :vertical{
        combo "ComboBox": Type, combo_box , hint "This is a hint";
        intCombos "Int combos": Int, combo_box , hint "This is a hint";
        intCombosOptional "Int combos Optional": Int, combo_box , optional ,hint "This is a hint";
        stringOptional "String Optional": String, combo_box, optional, hint "This is a hint";
        stringRequired "String Required": String, combo_box, hint "This is a hint";
        stringCombos "String combos": String, combo_box , hint "This is a hint";
        tagsCombo "TagsComboBox": Type, tags_combo_box , hint "This is a hint", default A;
        comboButton "Set E" : button, content_style "btn-default", on_click setE;
        comboTagsButton "Set Tags E" : button, content_style "btn-default", on_click setTagsE;
    }  ;

    table : table {
        a "A without options" : String, display;
        b "default options" : Type;
        b1 "valid options" : Type;
        c "custom table options" : Type;
        d "custom row options" : Type;
        button "Set A to B" : button, on_click setAtoB;
        comment "Comment" : String, display;
    };

    change "Change options" : button, on_click changeOptions;
}

enum Type "Type"
{
   A;
   B;
   C;
   D;
   E;
   F;
}