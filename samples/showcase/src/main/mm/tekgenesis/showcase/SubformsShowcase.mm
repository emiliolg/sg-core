package tekgenesis.showcase schema showcase;

form DynamicFormA "Dynamic Form A"
{
    textField "Text Field" : String, on_ui_change textFieldChanged;
    navigate "Navigate" : button;
}

form DynamicFormB "Dynamic Form B"
{
    textField "Text Field" : String, on_ui_change textFieldChanged;
}

form DynamicTableForm "Dynamic Table"
  entity Addresses
{
    header {
        title : message(title);
    };

    id, internal, optional;
    clientAddresses "Addresses" : clientAddresses, table {
        country "Country"     : country;
        state   "State"       : state, hide when city == "CABA", optional when city == "CABA", reset;
        city    "City"        : city;
        street  "Street"      : street;
        zip     "Postal Code" : zip, optional;
    };
    horizontal, style "margin-top-20" {
        addRowBottom "Add" : button(add_row, clientAddresses), style "margin-right-5";
        removeRowBottom "Remove" : button(remove_row, clientAddresses);
    };

    footer {
        button(save);
        button(cancel);
        button(delete), style "pull-right";
    };
}

entity Client "Client"
{
    name : String(50);
    address : Address;
}

entity Address "Address"
{
    street     : String(60);
    city       : String(40);
    state      : String(40), optional when city == "CABA";
    zip        : String(10), optional;
    country    : String(30);
}


form AddressForm "Address Form"
    entity Address
{
    header {
        message(title);
        "View Source" : label, link "/sg/view/source/tekgenesis.showcase.AddressForm", style "pull-right";
    };
    id, internal, optional;
    street  "Street"      : street, text_field;
    city    "City"        : city;
    state   "State"       : state, hide when city == "CABA", optional when city == "CABA", reset;
    zip     "Postal Code" : zip, optional;
    country "Country"     : country;

    footer {
        button(save);
        button(cancel);
        button(delete), style "pull-right";
    };
}

entity Addresses {
    clientAddresses : entity InnerAddress* {
        street     : String(60);
        city       : String(40);
        state      : String(40), optional when city == "CABA";
        zip        : String(10), optional;
        country    : String(30);
    };
}

form ReverseAddressForm "Address Form"
    entity Address
{
    header {
        message(title);
        "View Source" : label, link "/sg/view/source/tekgenesis.showcase.ReverseAddressForm", style "pull-right";
    };
    id, internal, optional;
    country "Country"     : country, on_change resetAboveFields, default "Argentina";
    state   "State"       : state, hide when city == "CABA", optional when city == "CABA", reset;
    city    "City"        : city;
    street  "Street"      : street, text_field;
    zip     "Postal Code" : zip, optional;

    footer {
        button(save);
        button(cancel);
        button(delete), style "pull-right";
    };
}

form AddressesForm "Addresses Form"
    entity Addresses
{
    header {
        message(title);
        "View Source" : label, link "/sg/view/source/tekgenesis.showcase.AddressForm", style "pull-right";

    };

    id, internal, optional;
    clientAddresses "Addresses" : clientAddresses, table {
        country "Country"     : country, on_change resetState;
        state   "State"       : state, hide when city == "CABA", optional when city == "CABA", reset;
        city    "City"        : city;
        street  "Street"      : street, text_field;
        zip     "Postal Code" : zip, optional;
    };
    horizontal, style "margin-top-20" {
        addRowBottom "Add" : button(add_row, clientAddresses), style "margin-right-5";
        removeRowBottom "Remove" : button(remove_row, clientAddresses);
    };

    footer {
        button(save);
        button(cancel);
        button(delete), style "pull-right";
    };
}


form ClientForm "Client Form"
    entity Client
{
    header {
        message(title);
    };
    id, internal, optional ;
    name;
    address, subform(AddressForm);
    btn "Nav" : button, on_click some;

    footer {
        button(save);
        button(cancel);
        button(delete), style "pull-right";
    };
}


form SubformsShowcase "Subforms"
 on_load load
 {

    header {
        message(title);
        "View Source" : label, link "/sg/view/source/tekgenesis.showcase.SubformsShowcase", style "pull-right";
    };

    first "Firstname" : String(30);
    last "Lastname" : String(30);

    address "Home" : subform(AddressForm), display address.street, on placeholder;

    placeholder    : anchor;

    syncSubForm "Sync" : button, on_click changeSubForm;
    hideSubform : Boolean, internal;

    table "Other Addresses" : table {
        description "Description" : String(30);
        "Modal"  : horizontal { addressInSubform "Address" : subform(AddressForm), display addressInSubform.street, hide when hideSubform && addressInSubform2.street != null; };
        "Anchor" : horizontal { addressInSubform2 "Address" : Address, subform(AddressForm), display addressInSubform2.street, on placeholder, hide when addressInSubform.street != null; };
        "Hide"   : button, on_click hideSubform;
    };
    horizontal, style "margin-top-20" {
        addRowBottom "Add" : button(add_row), style "margin-right-5";
        removeRowBottom "Remove" : button(remove_row);
    };
}

form DependeciesForm
    primary_key product
{
    product : String(20);
    dependencies : table {
        dependency : String(20);
    };
    horizontal {
        button(add_row);
        button(remove_row);
    };
    total : Int, internal, is rows(dependency);
}

form SubformsExpressions "Subforms Expressions"
    on_load load
{
    count : Int, is sum(deps);

    products : table {
        product : String(20), is dependencies.product;
        deps : Int, is dependencies.total;
        dependencies : subform(DependeciesForm), display "Edit " + dependencies.product;
    };

    total : Int, internal, is rows(product);
}

form SubformActions "Subform Actions"
{
    someSubform: subform(SomeSubform), display "SomeSubForm";
}

form SomeSubform "Some Subform" {
    someCheckbox: Boolean;
    someOtherCheckbox: Boolean;
    someText: String;
    someOtherText: String;
    someNumber: Int;
    someDate "Martin Gutierrez Birthday": String;
}

form InlineSubformShowcase "Inline Subforms Showcase"
    on_load load
{
    header{
        message(title);
    };

    flightTax "Change me to change subforms tax!!": Decimal(10, 2), on_change taxChange;

    flights: section, style "margin20" {
        flight: subform(FlightInfo), inline;
    };
}

form SubformClearForm
     on_load load
{
    header{
        message(title);
    };

    someSection: section {
        mysub: subform(SomeNumberForm), inline;
        myText "Display It": String, display;
    };

    clearAndAdd "Clear and Add": button, on_click clearAndAdd;
    clear "Just Clear": button, on_click clear;
    justAdd "Just Add": button, on_click justAdd;
    changeFirst "Change First": button, on_click changeFirst;
}

form SomeNumberForm {
    someNumber "Subform It": Real, display;
}

form FlightInfo
    on_load load
{
    tax: Decimal(10, 2), internal, on_change taxChange;

    horizontal, style "hover-border" {
        flightsTable: table, col 9, style "condensed no-thead", row_style rowType == TITLE ? "title-row" : "" {
            rowType: RowType, internal;
            leaveTime: String(50), display;
            arriveTime: String(20), display, hide when rowType == TITLE;
            flightDuration: String(20), display, hide when rowType == TITLE;
            airline: String(20), display, hide when rowType == TITLE;
        };

        vertical, col 3 {
            horizontal {
				initialPrice: Decimal(10, 2), display, mask currency, content_style "initial-price pull-right";
			};
            horizontal {
				infoText: String(20), display, is "Price per adult", content_style "info-text pull-right";
			};

            priceTable: table, style "condensed no-thead margin-right-error" {
                description: String(20), display;
                price: Decimal(10,2), display, mask currency, style "price-bold";
            };
        };
    };
}

enum RowType{
    TITLE;
    INFO;
}

form Anakin
    on_load load
{
    header { message(title); };
    text "Text" : text_field, placeholder "Luke, I'm your father!";

    luke : subform(Luke), inline;
}

form Luke {
    header { message(title); };
    text "Text" : text_field, placeholder "I'm a child of Anakin";

    ben "Ben" : subform(Ben), display ben.text;
}

form Ben {
    header { message(title); };
    text "Text" : text_field, placeholder "I'm a child of Luke, grandchild of Anakin";
}

form SubformValidate
    entity SimpleEntity
    on_load load
{
    header { message(title); };

    "Name"        : name;
    "Description" : description;

    inner : subform(ToBeValidated), inline;

    button(validate), on_click validated;

    footer {
        button(save);
        button(cancel);
        button(delete), style "pull-right";
    };
}

form ToBeValidated
    on_load load
{
    textRequired "Required" : text_field, placeholder "I should be required";
    textOptional "Optional" : text_field, placeholder "I should be optional", optional;

    inner : subform(ToBeValidated1), inline;

    section : section {
       nested : subform(ToBeValidated1), inline;
    };
}

form ToBeValidated1
{
    textRequired "Required" : text_field, placeholder "I should be required";
    textOptional "Optional" : text_field, placeholder "I should be optional", optional;
}

form SubformOnDialog
    on_load load
{

    mergeReceptionDialog "Reception to add" : dialog {
        vertical {
            tableInDialog : table {
                string : display;
            };

            mergeReceptionSubForm       : subform(InlineSubformInDialog), inline;
            doMergeReception "Confirm"  : button, icon bomb, content_style "btn-primary", on_click doMergeReception;
        };
    };

    footer {
        horizontal, col 2 {
            doShowMergeReception "Merge Reception"  : button, icon bomb, content_style "btn-info", on_click doShowMergeReception;
        };
    };
}

form InlineSubformInDialog
    on_load load
{
    id : Int, internal;

    mergeReception : table, col 12 {
        reception : String, display;
    };
}

form SuperNestedMultiples {  
    tabs "Tabs" : tabs  { 
        first "First": horizontal { 
            sections: section { 
                subf : subform(TableSubform), inline; 
            }; 
        }; 
        second "Second": horizontal { 
            str : String; 
        }; 
    }; 
}

  form TableSubform { 
    table "Table" : section { 
        bool "Bool" : Boolean;
        someName "Some name" : String; 
        dynamicW "Dynamic" : dynamic, disable when bool;
        delete "Delete": button, on_click delete, abstract_invocation; 
    };
 }