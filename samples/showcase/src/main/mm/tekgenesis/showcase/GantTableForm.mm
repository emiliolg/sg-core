package tekgenesis.showcase;

form GantTableForm
    on_load load
{
	gant "Gant Example" : table, style "gant" {
	    name "Activity" : String(20), display;
	    description: String(20), internal;
	    from : Real, internal;
	    amount : Real, internal;
	    day : Int, internal;
	    sunday "Sunday": progress, is amount, from from, style "activity", placeholder description;
	    monday "Monday" : Boolean, display, hide;
	    tuesday "Tuesday" : Boolean, display, hide;
	    wednesday "Wednesday" : Boolean, display, hide;
	    thursday "Thrusday" : Boolean, display, hide;
	    friday "Friday" : Boolean, display, hide;
	    saturday "Saturday" : Boolean, display, hide;
	};
}