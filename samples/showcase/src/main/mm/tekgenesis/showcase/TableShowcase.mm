package tekgenesis.showcase schema showcase;

form TableMultipleOnChange {

    header {
        message(title);
    };

    field "Field" : Int;
    twoTimesField "Two times field" : Int, default 2 * field, on_change bchanged;
    setField10 "Set field to 10" : button, on_click setField;
    any "Any Button" : button, on_click anyClick;

    items : table {
        column1 "Column1" : Int;
        column2 "Column2" : Int, is column1 + twoTimesField, on_change changed;
        save : button(save);
    };
    horizontal, style "margin-top-20" {
        addRowBottom "Add" : button(add_row, items), style "margin-right-5";
        removeRowBottom "Remove" : button(remove_row, items);
    };

    sum "Sum of c2" : Int, default sum(column2), on_change sumchanged;
    otherField "Other field" : Int, on_change echanged;

    progressDebug "Progress" : text_area(20, 150), style "full-width", optional, default "";
}

enum Gender {
    MALE : "Male";
    FEMALE : "Female";
    UNDEFINED : "Undefined";
}

type Name = String(20);

entity Classroom "Classroom"
    primary_key idKey
    described_by idKey
    searchable by {
        idKey;
        room;
    }
{
    idKey "Id" : Int;
    room "Room" : String(4);
    students "Students" : entity Student* {
        dni "Dni" : Int;
        firstName "First Name" : Name;
        lastName "Last Name" : String(20);
        age "Age" : Int;
        gender "Gender" : Gender;
    };
}


form StudentForm "Student Form"
    on_load load
{
    header {
        message(title);
    };
    dni : display;
    table : table {
        column : Int;
    };
    vertical {
        horizontal {
            vertical {
                saleEnabled: Boolean, default false;
                suggest: Classroom, on_new_form ClassroomForm;
                saleDropDown : dropdown, split, hide when !saleEnabled, content_style "btn-warning",
                            style "pull-right show-button margin-right-15 margin-left-10" {
                            google "Google" : label, icon shopping_cart, link "http://www.google.com", target_blank;
                            twitter "Twitter" : label, icon fighter_jet, link "http://www.twitter.com", target_blank;
                        };
                saleDropDown2 : dropdown, split, content_style "btn-warning",
                            style "pull-right show-button margin-right-15 margin-left-10" {
                            google2 "Google" : label, icon shopping_cart, link_form WidgetShowcase;
                            facebook2 "Facebook" : label, icon fighter_jet, link_form WidgetShowcase;
                            twitter2 "Twitter" : label, icon fighter_jet, on_click widgetsForm;
                        };

            };
        };
    };
}

form SortableTableWithGroups "SortableTable with Groups"
    on_load load
{

    header {
        message(title);
        viewSource "View Source" : label, link "/sg/view/source/tekgenesis.showcase.SortableTableWithGroups", style "pull-right";
    };

    sales : table, sortable {
        saleId          "Id"          : vertical {
            id "Id"      : Int, display(13);
            session "Session" : display(40);
        };
        user            "User"        : vertical {
            first "First Name" : String(50), display;
            last "Last Name"  : String(50), display;
            email "Email"      : String(128), display(128);
        };
        fullfillment "Fulfillment" : Options, display;
        amount "Amount"      : Decimal(9,2), display(13);
        //state "State"       : DisplayOptions, display, default OPEN;
        sync "Sync"        : Boolean, display;
        updateTime "Date"        : DateTime, display;
    };
}


form ClassroomForm "Tables"
    entity Classroom
    on_route "showcase/table"
{
    header {
        message(title);
        search_box, style "pull-right";
        "View Source" : label, link "/sg/view/source/tekgenesis.showcase.ClassroomForm", style "pull-right";
    };

    idKey, expand;
    room, width 40;
    fill "Fill on change:" : text_field, is "("+idKey+")", on_change fill, hint "Each time id is changed, table is populated";

    "Show/Hide Column" : horizontal, label_col 10 {
        showDni "Dni" : Boolean, default true;
        showFirstName "First Name" : Boolean, default true;
        showLastName "Last Name" : Boolean, default true;
        showAge "Age" : Boolean, default true;
        showGender "Gender" : Boolean, default true;
    };

    horizontal {
        clear "Clear" : button, on_click clear;
        clearFill "Clear & Fill" : button, on_click clearAndFill;
        random "Random" : button, on_click random;
        first "First" : button, on_click first;
        second "Second" : button, on_click second;
        rows : Int, display;
    };

    swipe "Swipe?" : check_box, default true;
    navigate "Navigate" : button, on_click navigateToWidgetShowcase;

    students, table(5), draggable, sortable, row_style age > 17 ? "red" : "yellow", on_click rowClicked, scrollable {
        dni, hide_column when !showDni, width 10;
        firstName, hide_column when !showFirstName;
        lastName, hide_column when !showLastName;
        age, hide_column when !showAge;
        gender, hide_column when !showGender;
        view "view": button, on_click swipe;
        ddown: dropdown, split {
           label, link "www.google.com", icon cog;
           "Edit" : label, link "www.google.com", icon pencil_square_o;
           "Remove" : label, link_form GroupShowcase, icon times;
        };
    };

    horizontal {
        addRowBottom "Add" : button(add_row);
        removeRowBottom "Remove" : button(remove_row);
    };

    footer {
        button(save);
        button(cancel);
        button(delete), style "pull-right";
    };
}

form SortableIntTable
    on_load load
    primary_key autoLoad
{
    header {
        message(title);
        viewSource "View Source" : label, link "/sg/view/source/tekgenesis.showcase.SortableIntTable", style "pull-right";
    };

    autoLoad : Boolean, internal, default true;

    table "Table" : table, sortable, row_style integer > 50 ? "red" : "yellow" {
        integer "Age" : Int, check integer < 100 : "Must be lower than 100";
        string "Name" : String(25), optional;
    };

    button(add_row);
}

form DisabledTable {
    firstBool : Boolean;

    firstTable : table(5), disable when firstBool && field3, draggable {
        field1 "F one": String;
        field2 "F two": String, disable when field3;
        field3 "F 3": Boolean;
    };
    button(add_row, firstTable);
    button(remove_row, firstTable);

    secondTable : table(5), disable {
        field4 "F four" : String, disable when firstBool;
        field5 "F five" : String;
    };
    button(add_row, secondTable);
    button(remove_row, secondTable);
}

form SortableTable
    on_load load
{

    header {
        message(title);
        "View Source" : label, link "/sg/view/source/tekgenesis.showcase.SortableTable", style "pull-right";
    };

    test "Test" : String, on_change changeScalar;

    table "Table" : table(10), sortable, filterable, on_selection select {
        key : Int, internal;
        icon "Icon" : label, icon twitter;
        integer "Age" : Int, display;
        string "Name" : String(25), display;
        currency "Currency" : Real, mask currency, on_change change;
        simple "Simple" : SimpleEntity, on_suggest_sync suggest;
    };

    /*table2 "Table2" : table, on_selection select2 {
        string2 "String" : String;
    };*/

}

form TableFilter "Form Filter"
    on_load load
{
    header {
        message(title);
        "View Source" : label, link "/sg/view/source/tekgenesis.showcase.TableFilter", style "pull-right";
    };

    colors "Color" : tags, on_change refresh, optional;
    message(warning), is "Colors defined: " + size(colors) + " and rows defined: " + count(name)/*, hide when count(colors) != 0*/;
    lowest "Lowest" : String, combo_box, on_change refresh;
    highest "Highest" : String, combo_box, on_change refresh;
    categories "Categories" : tags, on_change refresh;
    products "Products" : table {
        name "Product" : String(20);
        price "Price" : String;
        category "Category" : String(30);
        color "Color" : String(30);
    };
    horizontal, style "margin-top-20" {
        addRowBottom "Add" : button(add_row, products), style "margin-right-5";
        removeRowBottom "Remove" : button(remove_row, products);
    };
}

form NewTable "New Table"
 on_load load
 permissions add_row
{
    header {
        message(title);
        viewSource "View Source" : label, link "/sg/view/source/tekgenesis.showcase.NewTable", style "pull-right";
    };

    horizontal, style "table-right-actions" {
        addRowTop : button(add_row), style "margin-right-5", icon plus;
        removeRowTop : button(remove_row), icon minus;
    };

    test "Test" : text_field;

    testLabel "Test Label" : text_field, label_expression test;

    table "Table" : table, label_expression test {
        strCol "String" : String, label_expression test;
        intCol "Integer" : Int;
        dateCol "Date" : Date;
        removeInRow "Remove" : button(remove_row), label_expression "Remove " + strCol;
    };

    horizontal, style "margin-top-20" {
        button(add_row), style "margin-right-5", on_click addClicked, disable when forbidden(add_row);
        button(remove_row), on_click remove;
    };
}

form ExportTable
  on_load load
{

    header {
        message(title);
        viewSource "View Source" : label, link "/sg/view/source/tekgenesis.showcase.ExportTable", style "pull-right";
    };

    orders "Orders" : table {
        strCol "String" : String, display;
        intCol "Integer" : Int, display;
        dateCol "Date" : Date, display;
        dateTimeCol "DateTime" : DateTime, display;
        removeInRow "Remove" : button(remove_row);
        iconInTable "Icon" : button, icon cog, tooltip "nice tooltip", on_click clicked;
    };

    horizontal, style "margin-top-20" {
        downloadPdf "Download PDF" : button(export), export_type pdf;
        downloadCsv "Download CSV" : button(export), export_type csv;
        inline "Open" : button(export), export_type pdf, on_click inline;
        print "Print" : button(export), export_type pdf, on_click print;
    };
}

form NewSync "New Sync"
 on_load load
{
    header {
        message(title);
        viewSource "View Source" : label, link "/sg/view/source/tekgenesis.showcase.NewSync", style "pull-right";
    };

    horizontal, style "table-right-actions" {
        addRowTop : button(add_row), style "margin-right-5", icon plus;
        removeRowTop : button(remove_row), icon minus;
    };

    test "Test" : text_field;

    table : table {
        strCol "String" : String, label_expression test;
        intCol "Integer" : Int;
        dateCol "Date" : Date;
        comboCol "Combo" : combo_box;
        address "Address" : subform(AddressesForm), display "Show Address";
        removeInRow "Remove" : button(remove_row), shortcut "ctrl + y";
    };

    horizontal, style "margin-top-20" {
        addRow : button(add_row), style "margin-right-5", on_click addClicked, shortcut "ctrl + a";
        removeRow : button(remove_row), style "margin-right-5", on_click remove;
        addMessageFirst "Add Message to First Row." : button, style "margin-right-5", on_click addMessageToFirst;
        setOptionsFirst "Set Options to First Row." : button, style "margin-right-5", on_click setOptionsToFirst;
        setOptionsGlobal "Set Options to Global Row." : button, style "margin-right-5", on_click setOptionsGlobal;
        resetFirst "Reset First." : button, style "margin-right-5", on_click resetFirst;
        configFirst "Config First." : button, style "margin-right-5", on_click configFirst;
    };
}

form Table "Table"
{
    tax   "Tax"            : Decimal(10, 2);
    table "Product Prices" : table(3),
                             hide when tax > 0.3,
                             row_style (quantity > 10) ? "red" : "yellow" {
        product   "Product"    : String;
        unitPrice "Unit Price" : Decimal(10, 2), disable when valid == true;
        taxPrice  "Tax"        : Decimal(10, 2), is unitPrice * tax;
        quantity  "Quantity"   : Int, disable when valid == true;
        price     "Price"      : Decimal(10, 2), is (taxPrice + unitPrice) * quantity;
        valid     "Deprecate"  : Boolean, default false;
    };
    horizontal, style "margin-top-20" {
        addRowBottom "Add" : button(add_row, table), style "margin-right-5";
        removeRowBottom "Remove" : button(remove_row, table);
    };

    total "Total"          : Decimal(10, 2), is sum(price);
}

form MultipleTablesSync {
    rows : Int, on_change loadTables;
    printer : Boolean, combo_box, disable when rows == null, optional, placeholder "Printer...";
    downloader : Boolean, internal;



    tabs : tabs, no_label {
        labelTab "Pending Summary" : vertical {
            summary "Symmary" : display, is rows(moe) + " pendings.";
        };

        pendingsTab "Pending Pickups" : vertical {
            pendings : table, sortable {
                moe "Moe" : String;
                larry "Larry" : String;
                curly "Curly" : String;

                horizontal {
                    print : button, icon print, on_click print, disable when printer == null;
                    download : button, icon file_pdf_o, on_click export, hide when downloader;
                };
            };
        };
    };
}

entity TableEntity
{
    moe : String;
    larry : Int;
    curly : Real;
}

form FormWithTableEntityWithOnChange {
    table : TableEntity, table, on_change saveEntity {
        tableId : id, internal, optional;
        tableA : moe;
        tableB : larry;
        tableC : curly;
    };
}

form DecimalsForm
    on_load load
{
    header { message(title); };

    a "Decimal(12,2)" : Decimal(12,2);
    b "Int" : Int;

    aDisp "Display Decimal(12,2)" : Decimal(12,2), display, is a, mask NONE;
    bDisp "Display Int" : Int, display, is b;
}

form PercentagesTable
    on_load load
{

    header { message(title); };

    table : table {
        label : display;
        total "Total"             : Decimal(10,2), display, mask DECIMAL, optional;
        discountTotal "Discount Total"   : Decimal(10,2), display, optional;
        discount "Discount (%)"   : Decimal(3,2), display, mask PERCENT, is (discountTotal / total);
    };
}

form OptionsSync
{
    header { message(title); };

    options "Options" : Options;

    changeOptions "Change Options" : button, on_click changeOptions;
    otherAction "Other Action" : button, on_click otherAction;
}

