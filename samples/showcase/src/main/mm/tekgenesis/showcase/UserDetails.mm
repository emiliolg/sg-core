package tekgenesis.showcase;

form UserDetailsForm "User Details"
    on_load loadDetails
{
    header { message(title); };

	id "Id"     : String(20), display;
    name "Name"   : String(25), display;

    ou "Ou" : String(10), display;

    props : table {
        propId : String(10), display;
        propName : String(20), display;
        propValue : String(20), display;
    };

    footer { close "Close" : button(cancel); };
}