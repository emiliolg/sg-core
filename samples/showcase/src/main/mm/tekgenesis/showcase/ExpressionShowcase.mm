package tekgenesis.showcase;

form ExpressionShowcase "Expressions"
{
    header {
        message(title);
        "View Source" : label, link "/sg/view/source/tekgenesis.showcase.ExpressionShowcase", style "pull-right";
    };

    horizontal {

        itemGroup "Items" : vertical {
            ignoredItem "Ignored Item" : Int, default -1, disable;
            item1 "Item #1": Int, default 20 ;
            item2 "Item #2": Int, default 2*item1 ;
            item3 "Item #3": Int, default item2-item1 ;
            subtotal "SubTotal" : Int, is item1+item2+item3;
            iva "Iva" : Decimal (10,2), is subtotal*0.21;
            total "Total" : Decimal(10,2), is subtotal+iva;

        };

        username "User" : vertical {

            first "First" : String(20);
            last "Last" : String(20);
            available : Boolean, internal;
            nick "Nick" : String(40), default substring(first,0,1) + "" + last, on_change isNickAvailable, check available : "Nick already chosen";
        };

    };

    "Client side functions" : vertical {
        target "Target" :  String(30);
        function "Function" :  String(30);
        arg1 "Arg # 1" :  String(30), optional;
        arg2 "Arg # 2" :  String(30), optional;
        arg3 "Arg # 3" :  Int, optional;
        "Invoke" : button, on_click invoke;
    };

    footer { button(save); };
}

form CrazyEventsShowcase {
    header {
        message(title);
        "View Source" : label, link "/sg/view/source/tekgenesis.showcase.CrazyEventsShowcase", style "pull-right";
    };

    eventsTable "Events Table": table {
        theNumber "The Number": String, display, default 5, on_change changeNumber;
        age "Age": String, default 10, on_change changeAge;
    };

	horizontal {
		button(add_row);
		button(remove_row);
	};
}

form OnChangeShowcase "OnChange Showcase" on_load onLoad {

    header {
        message(title);
        "View Source" : label, link "/sg/view/source/tekgenesis.showcase.OnChangeShowcase", style "pull-right";
    };

    item "Item" : Int, on_ui_change uiChanged;
}

form CheckOnChange "Check OnChange" {

    header {
        message(title);
        "View Source" : label, link "/sg/view/source/tekgenesis.showcase.CheckOnChange", style "pull-right";
    };

    item "Item" : Int, check item > 5 : "Must be greater than 5.", on_change changed;
    item2 "Item 2" : Int, is item;

    changed "Changed" : Boolean, default false, disable;
}

form DependsOn {

    header { message(title); };

    editable "Editable?" : Boolean, default true;

    warehouse "Warehouse" : text_field, disable when !editable;

    scheduledDate "Scheduled Date" : text_field, depends_on warehouse, disable when warehouse == null || !editable;

}
