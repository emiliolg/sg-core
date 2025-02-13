package tekgenesis.showcase;

form ValidationShowcase "Validation"

{
    header {
        message(title);
        "View Source" : label, link "/sg/view/source/tekgenesis.showcase.ValidationShowcase", style "pull-right";
    };

    requiredField "Required Field": String(20), text_field;
    optionalField "Optional Field": String(20), text_field, optional;
    optionalCheck "Make the following optional" : check_box, default true;
    optionalCheckField "Field": String(20), text_field, optional when optionalCheck;

    fromField "Start Date" : date_box, required;
    toField "End Date" : date_box, required, check toField>fromField : "Start date should be earlier than End date";

    "Validation Types" :vertical  {

          input1 "Default error" : Int, check (input1 > 10 : "Must be over 10", input1 < 15 : "Must be under 15"), hint "check someNumber > 10 <message>, check someNumber < 15 <message>";

          input2 "Default warning"  : Int, check input2 > 10 : warning "Must be over 10", hint "check someNumber > 10 : warning <message>";

          input3 "Default info"  : Int, check input3 > 10 : info "Must be over 10", hint "check someNumber > 10 : info <message>";

          input4 "Inline error"  : Int, check input4 > 10 : inline "Must be over 10", hint "check someNumber > 10 : inline <message>";

          input5 "Inline warning"  : Int, check input5 > 10 : inline warning "Must be over 10", hint "check someNumber > 10 : inline warning <message>";

          input6 "Inline info"  : Int, check input6 > 10 : inline info "Must be over 10", hint "check someNumber > 10 : inline info <message>";

    };

    footer { button(save); };
}

form Dispatcher
    on_route "/route"
    handler DispatcherHandler
    primary_key name
{
    name: String;
}