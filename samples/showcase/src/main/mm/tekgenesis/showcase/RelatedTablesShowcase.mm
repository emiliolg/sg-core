package tekgenesis.showcase;

form RelatedTablesForm "Related Tables"
{
    header {
        message(title);
        "View Source" : label, link "/sg/view/source/tekgenesis.showcase.RelatedTablesForm", style "pull-right";
    };

    first "first" : table {
        a "a" : Int, default 1;
        b "b = a * 2" : Int, is a * 2;
    };
    horizontal, style "margin-top-20" {
        firstAddRowBottom "Add" : button(add_row, first), style "margin-right-5";
        firstRemoveRowBottom "Remove" : button(remove_row, first);
    };

    ftot "sum(b)" : Int, is sum(b), check ftot > 10 : inline "Must be greater than 10";

    mult "mult" : Int, default 2;

    second "second" : table {
        x "x = sum(a) * mult" : Int, is sum(a) * mult;
        y "y" : Int, default 1;
        z "z = x + y" : Int, is x + y;
    };
    horizontal, style "margin-top-20" {
        addRowBottom "Add" : button(add_row, second), style "margin-right-5";
        removeRowBottom "Remove" : button(remove_row, second);
    };

    stot "sum(z)" : Int, is sum(z);
}