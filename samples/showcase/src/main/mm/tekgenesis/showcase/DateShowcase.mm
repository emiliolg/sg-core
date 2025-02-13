package tekgenesis.showcase schema showcase;

entity DateShowcase "DateShowcase"
	primary_key idKey
	searchable
{
	idKey : Int;

	dateFrom : Date, optional;
	dateTo : Date, optional;

	timeFrom : DateTime, optional;
	timeTo : DateTime, optional;

	doubleDateFrom : Date, optional;
	doubleDateTo : Date, optional;

	dateCombo : Date, optional;
	dateCombo1 : Date, optional;
}

form DatesForm "Dates Form"
    entity DateShowcase
    on_load load
{
     header {
        message(title);
        search_box, style "pull-right";
        view "View Source" : label, link "/sg/view/source/tekgenesis.showcase.DateShowcase", style "pull-right";
    };

    id   "Id"   : idKey;

    dateFrom "Date From" : dateFrom, optional, default today(), from today(), on_change show;
    dateTo "Date To" : dateTo, optional, default today() + 1, to today() + 7, on_change show;

    datePickerFrom "DatePicker From" : Date, date_picker;
    datePickerTo "DatePicker To" : Date, date_picker;

    datePickerDefault "DatePicker with default" : Date, date_picker, from today() + 35, default today() + 35;

    timeFrom "From" : timeFrom, optional, default now(), from now(), on_change show;
    timeTo "To" : timeTo, optional, from timeFrom, midnight_as_24, on_change show;

    doubleDateFrom "From" : doubleDateFrom, double_date_box, default today(), from today(), on_change show;
    doubleDateTo "To" : doubleDateTo, double_date_box, from doubleDateFrom, on_change show;

    dateCombo "Date Combo" : dateCombo, combo_date_box, default "2013-11-15", on_change show;
    dateCombo1 "Date Combo2" : dateCombo1, combo_date_box, from today(), to "2020-01-01", on_change show;

    footer {
        button(save);
        button(cancel);
        button(delete), style "pull-right";
    };
}

entity DateEntity
    searchable
{
    date : Date;
}

form DateEntityForm "Date Entity Form"
    entity DateEntity
{
    header {
        message(title);
        search_box, style "pull-right";
    };

    "Id"   : id, internal, optional ;
    "Date" : date;

    leaveDate  "Leave Date"  : double_date_box, optional, from today(), default today(), style "vertical-label", on_ui_change changeReturnDate;
    returnDate "Return Date" : double_date_box, from leaveDate, optional, default today(), style "vertical-label";

    footer {
        button(save);
        button(cancel);
        button(delete), style "pull-right";
    };
}

form TimePickerForm
{

    header {
        message(title);
    };

    seconds "Seconds" : Int, optional;
    timePicker "Time Picker" : time_picker, on_ui_change changed, is seconds;

    timePickerStep5 "Time Picker Step 5" : time_picker, step 5, on_ui_change changed;
    timePickerStep15 "Time Picker Step 15" : time_picker, step 15, on_ui_change changed;

    from "From" : time_picker,  step 15, from 8, to 10, on_ui_change changed;
    to "To" : time_picker, step 15, from 16, to 18, on_ui_change changed;

    withTz "With Timezone" : time_picker, with_timezone, on_ui_change tzAwareValue;
}

form SimpleDate "Simple Date" {
    header {
        message(title);
        viewSource "View Source" : label, link "/sg/view/source/tekgenesis.showcase.SimpleDate", style "pull-right";
    };

    dateBox "DateBox" : date_box;

    doubleDateBox "DoubleDateBox" : double_date_box;

    dateTimeBox "DateTimeBox" : date_time_box;

    dateTimeBoxReset "DateTimeBox with reset" : date_time_box, reset_time "14:00";
}

form DateShowcaseForm "Dates"
    entity DateShowcase
{
    header {
        message(title);
        search_box, style "pull-right";
        "View Source" : label, link "/sg/view/source/tekgenesis.showcase.DateShowcase", style "pull-right";

    };

    id   "Id"   : idKey;

    horizontal {
        "Open" : dialog, display "Open" {
            dateFrom "Date From" : dateFrom, optional, default now(), check dateFrom == today() : "Error!!!", from today(), on_change show;
            dateTo "Date To" : dateTo, date_box(medium_format), optional, default today() + 1, to today() + 7, on_change show;
            addMessage "Add message" : button, on_click addMessage;
        };

        vertical {
            timeFrom "From" : timeFrom, optional, default today(), from today(), on_change show;
            timeTo "To" : timeTo, optional, default today() + 1, from timeFrom, midnight_as_24, on_change show;
        };

        vertical {
            doubleDateFrom "From" : doubleDateFrom, double_date_box, default today(), from today(), on_change show;
            doubleDateTo "To" : doubleDateTo, double_date_box, from doubleDateFrom, on_change show;
        };
    };

    addMessages "Add messages" : button, on_click addMessages;

    dateCombo "Date Combo" : dateCombo, combo_date_box, default "2013-11-15", on_change show;
    dateCombo1 "Date Combo2" : dateCombo1, combo_date_box, from today(), to "2020-01-01", on_change show;

    footer {
        button(save);
        button(cancel);
        button(delete), style "pull-right";
    };
}
