package tekgenesis.showcase;
//#addressWidget
abstract widget AddressWidget : Address {
    street;
    state, on_change stateChanged;
    city;
    zip;
    country;
}
//#addressWidget
entity Customer
    primary_key document
{
    document : Int(8);
    firstName : String;
    lastName : String;
    homeAddress : Address;
    workAddress : Address, optional;
}

form CustomerForm : Customer
{
    header {
        message(entity), col 12;
    };

    document, mask decimal;
    firstName;
    lastName;
    home : display, is homeAddress.street + " " + homeAddress.state + " " + homeAddress.city;
    homeAddress, widget(AddressWidget);
    work : display, is workAddress.street + " " + workAddress.state + " " + workAddress.city;
    workAddress, widget(AddressWidget), optional;

    feedback : String;

    footer {
        button(save);
        button(cancel);
        button(delete), style "pull-right";
    };
}

form BinaryTree {
    insert "Choose to insert" : vertical, hide when rows(ref) == 0 {
		letters : section {
	        ref : internal;
	        element : button, label_expression ref, on_click select, inline_style "float:left;width:75px";
	    };
	};

    tree "Tree" : vertical {
		head    : widget(BinaryNode), optional;
	};

    selection "Selection" : vertical {
		input_group, hide when selected == null {
            selected : String, disable, optional, col 1;
		    deselect "Deselect" : button, on_click deselect;
		};
	};

    order "Tree inorder" : vertical {
		inorder : display, is head.inorder;
	};
}
//#binaryNode
abstract widget BinaryNode {
    vertical {
		vertical, style "center" {
			value   : internal;
			button : toggle_button, label_expression value, on_ui_change toggle;
		};
	    horizontal {
			left    : widget(BinaryNode), optional, col 6;
		    right   : widget(BinaryNode), optional, col 6;
		};
	};
    inorder : display, is left.inorder + " " + value + " " + right.inorder, style "center", content_style "margin-left-15";
}
//#binaryNode

// Usage @tests
widget ChildWidget {
    id "Child": String;
    click : button, on_click click;
}

// Usage @tests
widget FatherWidget {
    id "Father": String;
    child : widget(ChildWidget);
    both "Both" : display, is id + " " + child.id;
    click : button, on_click click;
}

// Usage @tests
form NestedWidgets {
    father : widget(FatherWidget);
    both "Both" : display, is father.both;
    copy "Copy" : display, is father.id + " " + father.child.id;
}

// Usage @tests
widget MultipleInWidget {
    choices : table {
        label : String, default "Value";
        length : Int, is length(label);
        click : button, on_click click;
    };
    sum "Sum" : Int, is sum(length);
    add "Add" : button, on_click add;
}

// Usage @tests
form WidgetInMultiple {
    widgets : table {
        father : widget(FatherWidget);
        letters : Int, is length(father.both);
    };
    sum "Sum" : Int, is sum(letters);
    add "Add" : button, on_click add;
}

// Usage @tests
form MultipleInWidgetForm {
    multiple : widget(MultipleInWidget);
}

// Usage @tests
form NestedMultiples {
    widgets : table {
        multiple : widget(MultipleInWidget);
        total : Int, is multiple.sum;
    };
    sum "Sum" : Int, is sum(total);
    simple "Add simple" : button, on_click addSimple;
    complex "Add complex" : button, on_click addComplex;
}

// Usage @tests
abstract widget OnChangesWidget {
    a : Int, default 1;
    b : Int, is a * 10, on_change bValueChanged;
    c : Int, is a * 100, on_change cValueChanged;
    opts : Options, default OPTION3;
}

// Usage @tests
abstract widget NestedOnChangesWidget {
    widgets : table {
        nested : widget(OnChangesWidget);
        d : Int, is nested.a * 1000, on_change dValueChanged;
    };
    e : Int, is sum(d), on_change eValueChanged;
}


// Usage @tests
form WidgetInMultipleWithChainedOnChanges {
    widgets : table {
        changes : widget(OnChangesWidget);
        a : Int, is changes.a;
    };
    sum "Sum" : Int, is sum(a);
    calls "Calls" : String, default "";
}

// Usage @tests
form NestedOnChangesWidgetForm {
    widgets : table {
        changes : widget(NestedOnChangesWidget);
        f : Int, is changes.e, on_change fValueChanged;
    };
    sum "Sum" : Int, is sum(f), on_change sumChanged;
    calls "Calls" : String, default "";
    opts : Options, default OPTION3;
}