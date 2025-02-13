package tekgenesis.snippets;

form SortableTableForm{
//#sortableTable
featureTable "Feature"  : table , sortable, filterable, col 3 {
    group           : Boolean, internal;
    featureName     : String, display;
    featureValue    : String, display;
};
//#sortableTable
horizontal {
    button(add_row), style "margin-right-5";
    button(remove_row);
};
}

form TableForm{
//#table
featureValues "Feature"  : table {
    group           : Boolean, internal;
    featureName     : String, display;
    featureValue    : String, display;
};
//#table
//#buttons
horizontal {
    button(add_row), style "margin-right-5";
    button(remove_row);
};
//#buttons
}
entity SimpleEntity{
row: Int;
}
form PagedTableForm{
//#tablePaged
featureValues "Feature"  : table(5) {
    group           : Boolean, internal;
    featureName     : String, display;
    featureValue    : String, display;
};
//#tablePaged
//#tableSimple
simpleValues "Feature"  : SimpleEntity, table  {
row;
};
//#tableSimple
//#buttonsWithId
horizontal {
    button(add_row, featureValues), style "margin-right-5";
    button(remove_row, featureValues);
};
//#buttonsWithId
}
entity Car primary_key id{
    id: String;
    make:String;
    model:String;
    year:String;
    engine:String;
    price:String;
}
form CarsForm{
//#carTable
cars : Car, table, on_change saveCar {
    "Id"           : id, internal, optional ;
    "Make"         : make;
    "Model"        : model;
    "Year"         : year;
    "Engine"       : engine;
    "Price"        : price;
};
horizontal, style "margin-top-20" {
    button(add_row), style "margin-right-5";
    button(remove_row), on_click removeCar;
};
//#carTable
}
//#carListing
form Cars listing Car;
//#carListing