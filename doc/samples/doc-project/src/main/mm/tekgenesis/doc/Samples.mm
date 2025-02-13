package tekgenesis.doc;

form TypeSampleForm
{
//#numeric
    intNumber: Int, default 9;
    decimalNumber: Decimal(2,2), default 0.21;
    realNumber: Real, default 454.44;
//#numeric

}
enum PaymentOption "Payment Option"
{
    CASH : "Cash", "CA";
    DEBIT : "Debit", "DE";
    CREDIT : "Credit", "CR";
    CHECK : "Check", "CH";
}
//#person
enum Sex {
    MALE;
    FEMALE;
}

entity APerson
	primary_key ssn
	described_by first, last
	searchable
{
	ssn "SSN #"        : Int(9);
	first "First name" : String(30);
	last "Last name"   : String(30);
	birth "Birthday"   : Date;
	addresses          :  entity InnerAddress* {
	};
	sex                : Sex, default FEMALE;
}
//#person

//#enums
enum GenderType {
    MALE   : "Male";
    FEMALE : "Female";
    NONE   : "Not informed";
}

enum IdType with {
    unique : Boolean, default true;
    minLength : Int;
    maxLength : Int;
    numeric : Boolean;
    mask : String;
}
{
    NOT_INFORMED    : "Not informed", true, 0, 0, false, "";
    DNI             : "DNI", true, 6, 8, true, "R##.###.###";
    LC              : "LC", true, 5, 8, true, "R##.###.###";
    LE              : "LE", true, 5, 8, true, "R##.###.###";
    CI              : "CI", true, 6, 8, true, "R##.###.###";
    PASSPORT        : "Passport", true, 5, 9, false, "#########";
    TAX_CODE        : "Tax Code", true, 11, 11, false, "##-##.###.###-#";
    VENDOR_ID       : "EBS Vendor Id", true, 1, 10, true, "##########";
}
//#enums

entity Car "Car"
described_by model, year
searchable
primary_key model,year
{
	model : String;
    year : Int;
}
enum Colors "Colors"{
    Red: "Red";
}
enum Category "Category"{
    cat:"cat";
}
form AddressForm{
    placeholder : anchor;
}
form Form3{
    placeholder : anchor;
}
form Form1{
    placeholder : anchor;
}
form Form2{
    placeholder : anchor;
}

//#searchable

entity SearchablePerson
  searchable by{
    name: name, boost 3;
    lastName;
    someExtra;
    someFilterAndExtra, filter_only, boost 2;
}{
    name: String;
    lastName: String;
    birthday: DateTime;
    someExtra: Int;
    someFilterAndExtra: String, abstract, read_only;
}


form SearchablePersonForm "Searchable Person Form"
    entity SearchablePerson
{
    header {
        message(entity), col 8;
        search_box, col 4, style "pull-right";
    };
    "Id"                    : id, internal, optional;
    "Name"                  : name;
    "Last Name"             : lastName;
    "Birthday"              : birthday;
    "Some Extra"            : someExtra, mask decimal;
    "Some Filter And Extra" : someFilterAndExtra;
    footer {
        button(save);
        button(cancel);
        button(delete), style "pull-right";
    };
}


//#searchable

form Samples entity Car
{
//#summary
summary "Summary" : String, optional;
//#summary

//#filter
oneBool: Boolean;
anotherBool: Boolean;
lastBool: Boolean;
someOtherString: String;

filterBox : Car, filter (model = ("someModel", someOtherString) when oneBool, year != anotherBool ? 1999 : 1998);
//#filter
//#complexFilter
complexFilterBox: Car, on_suggest suggest;
//#complexFilter

//#anchor
placeholder : anchor;
//#anchor

//#breadcrumb
path "You are here!" : breadcrumb, on_change navigateThere;
//#breadcrumb
//#table
tableId "tableId"  : table {
    group           : Boolean, internal;
    featureName     : String, display;
    featureValue    : String, display;
};
//#table

//#vertical
groupId: vertical {
	inputMini "Input mini"       : text_field, style "mini";
	inputSmall "Input small"     : text_field, style "small";
	inputMedium "Input medium"   : text_field, style "medium";
};
//#vertical

//#buttons
print "Print" : button, on_click doPrint;
export "Export" : button(export, tableId), on_click doExport;
validate "Validate" : button(validate, groupId), on_click doValidate;
//#buttons

//#check_box
support "Supports WiFi?" : check_box;
//#check_box

//#check_box_group
paymentsGroup "Available payments" : PaymentOption, check_box_group;
//#check_box_group

//#color_picker
color "Choose a color" : color_picker;
//#color_picker

//#combo
payments "Available payments": PaymentOption;
//#combo

//#combo_date_box
birthday "Birthday" : combo_date_box;
//#combo_date_box

//#date_box
dateBox "To" : date_box;
//#date_box

//#date_picker
to "To" : date_picker;
//#date_picker
//#date_time_box
date "To" : date_time_box;
//#date_time_box

//#display
car "Entity" : Car, display;
//#display

//#double_date_box
doubleDate "From" : double_date_box;
//#double_date_box

//#dynamic
value "Component value" : dynamic;
//#dynamic
//#gallery
gallery "Images" : gallery;
//#gallery
//#image
homer "Homer" : image;
//#image
// #internal
id : internal;
//#internal

//#label
message "Some message" : label;
//#label
//#list_box
listBox "Colors" : Colors, list_box;
//#list_box

//#mail

mail "Email" : mail_field;
syntax "Syntax" : mail_field(syntax);
domain "Domain" : mail_field(domain);
//address "Address" : mail_field(address);
//domain "Domain" : mail_field(syntaxdomain);
//#mail

//#message
infoMsg "Title" : message(info), is "My info message";
//#message
//#password_field
password "Password" : password_field;
//#password_field

//#pick_list
pickList "Options": PaymentOption, pick_list;
//#pick_list
//#progress
progressBar "Progress Bar" : progress;
//#progress


//#radio_group
 colors "Colors" : radio_group;
//#radio_group

//#rating
 rating "Rating" : Int, rating;
//#rating

//#rich_text_area
 html "Html" : rich_text_area;
//#rich_text_area

//#search_box
search "Search" : search_box;
//#search_box

//#showcase
 images "Images" : showcase;
//#showcase


//#suggest_box
 categories "Categories" : Category, suggest_box;
//#suggest_box
//#tags
 tags "Tags" : tags;
//#tags
//#tags_combo_box
tagsCombo "TagsComboBox": PaymentOption, tags_combo_box;
//#tags_combo_box
//#tags_suggest_box
suggestBox "Colors" : Colors, tags_suggest_box;
//#tags_suggest_box

//#text_area
description "Description" : text_area;
//#text_area

//#text_field
text "Enter some text" : text_field;
//#text_field
//#time_picker
time "Time" : time_picker;
//#time_picker

//#toggle_button
toggle "ToggleButton" : toggle_button, icon square_o, icon_selected check_square_o;
//#toggle_button
//#upload
upload "Choose a file" : upload;
//#upload

//#video
video "Watch a video" : video;
//#video


//#chart
chartColumn "Chart" : chart(column), on_click click {
    key : internal;
    label : String;
    integer "Int" : Int;
    real "Real" : Real;
    bigDecimal "BigDecimal" : Decimal(5,2);
};
//#chart
//#map
argentina "Argentina" : map {
    argLatitude : Real;
    argLongitude : Real;
};
//#map
//#section
some "Cols" : section {
    vertical, col 4 {
        colImg : String, image, style "no-label";
        displayText : display, style "no-label";
    };
};
//#section

//#dialog
dialog "Dialog example" : dialog, display "Open dialog" {
    "A label inside a dialog." : label;
};
//#dialog

//#dropdown
actions : dropdown, split {
    label, icon cog, on_click click;
    edit "Edit" : label, link "www.google.com", icon pencil_square_o;
    remove "Remove" : label, link_form SampleForm, icon times;
};
//#dropdown
//#footer
footer {
    button(save);
    button(cancel);
    button(delete), style "pull-right";
};
//#footer
//#header
header {
    message(title);
    search_box, style "pull-right";
};
//#header


//#horizontal
horizontal {
    buttonLarge "Large" : button, content_style "btn-large";
    buttonDefault1 "Default" : button, style "margin-left-5";
    buttonSmall "Small" : button, style "margin-left-5";
    buttonMini "Mini" : button, style "margin-left-5";
};
//#horizontal
//#input_group
 "Input group " : input_group {
        "Ignored" : String (20), col 4;
        "Ignored" : String (20);
        "Ignored" : String (20);
    };
//#input_group

//op2  "Im the header!" : popover(bottom), style "btn btn-info", display "Click me !" {
//#popover
op2  "Im the header!"  : popover(bottom) , style "btn btn-info" {
    msg "This dialog was opened from the client, no need to go the server in this case." : label;
    txt1 "TextField 1": text_field;
    txt2 "TextField 2": text_field;
    footer { "Got it" : button, on_click click; };
};
//#popover

//#tabs
tabs "Tabbed Group" : tabs, on_change changed {
    t1 "Tab 1" :vertical, icon comments {
        tf11 "TextField 1" : text_field;
        tf12 "TextField 2" : text_field;
    };
    t2 "Tab 2" :horizontal, icon comments {
        tf21 "TextField 1" : text_field;
        tf22 "TextField 2" : text_field;
    };
};
//#tabs
street:internal;
//#subform
address "Home" : subform(AddressForm), display street, on placeholder;
place_holder    : anchor;

address2 "Address" : subform(AddressForm), display street;
//#subform

}

//#menu
menu MainMenu "Main" {
	Samples;
}
//#menu

//#menu2
menu OtherMenu{
	Form1; // using the form label as menu item label
}
//#menu2
//#linkMenu
link Google "Google" = "www.google.com";

menu LinkMenu
{
	 Google;
}
//#linkMenu

//#sampleForm
form SampleForm
{
    name : String, default "Ruby";
    created : Int, default 2014;
    expiration : Int, is created + 1;
    email : String, optional;
}
//#sampleForm
//#longMenu
menu LongMenu {
	Form1;
	Form2;
	Form3;
}
//#longMenu
