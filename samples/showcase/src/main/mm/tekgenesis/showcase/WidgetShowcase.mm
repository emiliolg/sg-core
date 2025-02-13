package tekgenesis.showcase schema showcase;

form WidgetShowcase "Widgets"
    on_load load
{
    header {
        message(title);
        "View Source" : label, link "/sg/view/source/tekgenesis.showcase.WidgetShowcase", style "pull-right";
    };

    textWidgets "TextWidgets" : vertical {

        text "Text" : String(20), display, is "This is a text.";
        textfield "TextField": String(20), text_field,  placeholder "This is a placeholder..", hint "This is a hint",
                               tooltip "This is a great tooltip because: <ul> <li> It accepts simple HTML </li> <li> It's hidden by default </li> <li> It's a talking icon </li> </ul>";
        mailFiled "MailField": mail_field, hint "This is a Mail Field", tooltip "This is a tooltip for mail fields";
        iconField "Field with Icon": String(20), text_field, icon calculator, placeholder "This is a placeholder..", hint "This is a hint",
                               tooltip "This is a great tooltip because: <ul> <li> It accepts simple HTML </li> <li> It's hidden by default </li> <li> It's a talking icon </li> </ul>";
        noPasteTextfield "No Paste TextField" : String(20), nopaste;
        password "PasswordField": password_field, hint "This is a hint";
        textarea "TextArea": text_area,  placeholder "Some text...", hint "This is a hint";
        richText "RichTextArea" : rich_text_area;

        colorpicker "Color Picker" : color_picker, on_change printColor;

        breadcrumb "Breadcrumb" : breadcrumb, on_change breadCrumbNavigation;
        suggest "SuggestBox" : Make, on_new_form MakeForm, hint "Type to search for a Car Make or create new.", on_change makeChanged;
        suggestInfo : message(info);
        message(info), is "My info msg ( insert text in TextArea ) : " + textarea;
        "Info" : message(info), is "My info msg ( insert text in TextArea ) : " + textarea;
        message(warning), is "My warning message with some html tags like <b>bold</b>, <i>italic</i> and line breaks<br>Line 2<br>Line 3";
    }  ;

    option "OptionWidgets" :vertical{
        combo "ComboBox": Options, combo_box , hint "This is a hint";
        tagsCombo "TagsComboBox": Options, tags_combo_box , hint "This is a hint", default OPTION1;
        checkBoxGroup "CheckboxGroup": Options, check_box_group , hint "This is a hint";
        radiogroup "RadioGroup": Options, radio_group, hint "This is a hint";
        listbox "ListBox" : Options, list_box, hint "This is a hint";
        pick "PickList" : Options, pick_list, hint "This is a hint", on_ui_change changed;

        tags "Tags" : String, tags;
        intTags "Int Tags" : Int, tags;
        realTags "Real Tags" : Real, tags;
        decimalTags "Decimal Tags" : Decimal(5,2), tags;
        mailTags "Mail Tags" : String, tags, content_style "tagMail";

        gallery "Gallery" : gallery;
        //breadcrumb "Breadcrumb" : breadcrumb, on_click navigate;
        treeView "TreeView": Node, tree_view, on_change nodeSelected;
    }  ;

    "Progress Bar" : vertical {
        progressNumber "ProgressNumber": Real, optional, hint "Insert a number between 0 and 100";
        fromNum "FromNumber": Real, optional, hint "Insert a number between 0 and 100";
        progressBar "ProgressBar": progress, is progressNumber, from fromNum;
    };

    "DIALOG !" : button, content_style "btn-primary", on_click doStuff;

    buttons "ButtonWidgets" :vertical{

        button "Button" : button, icon star, tooltip "Button with a tooltip";
        synBtn "Synchronous Btn" : button, icon refresh, hint "Syncronous server action", synchronous, on_click doWait;
        longBtn "Long Execution Btn" : button, icon clock_o, hint "Long server action", feedback, on_click doLongExecution;
        btn "Confirm Button" : button, on_click randomProgress, confirm "Are you sure you want to call this action?  (random progressbar number)";
        "Link" : label, link "http://www.google.com", icon google_plus, tooltip "Go to Google";
        "Link New tab" : label, link "http://www.google.com", target_blank, tooltip "Open Google on new tab";
        horizontal {
            words "Input" : String(30);
            download "Download" : button, on_click download, tooltip "Click to download a random file.";
        };
        togglebutton "ToggleButton": toggle_button, icon square_o, icon_selected check_square_o, content_style "btn-large btn-info";
        checkbox "Checkbox": check_box;
    };

    date "TimePickers" :horizontal{

        vertical, label_col 4 {
            datebox "DateBox" : date_box, hint "from 2012-07-09 to 2012-08-09.", from "2012-07-09", to "2012-08-09", icon calendar;
            datebox2 "DateBox2" : date_box, hint "from today().", from today(), on_change viewDate, icon calendar;
            datetimebox "DateTimeBox" : date_time_box, hint "from 2012-07-09.", from "2012-07-09", on_change viewDateTimeBox, icon calendar;
        };

        timepicker "TimePicker" : time_picker, label_col 4;
        datepicker "DatePicker": date_picker, hint "today() + 10", to today() + 10, label_col 4;

    }  ;

    some "Dialog!" : dialog, display "Open Me" {
        "Text" : String(20);
        "ComboBox": Options, combo_box , hint "This is a hint";
        horizontal {
            String(20);
            String(20);
        };

        "Click me" : popover(left), icon android, content_style "btn btn-default" {
            header{
                "Hola": label;
            };
            String(20);
            String(20);
        };



        tabs {
            "hor" : horizontal {
                String(20);
                String(20);
            };
          "vert" : vertical {
                String(20);
                String(20);
                String(20);
                String(20);
                String(20);
            };
        };

        footer {
            "Button1" : button, content_style "btn-primary", on_click doStuff, confirm "Are you sure?";
            "Button2" : button;
            "Button3" : button, content_style "btn-danger";
        };


    };

    footer { button(save); };

}

form FeedbackForm "Feedback Form" {
    header {
        message(title);
    };
    innerMessage: String, internal, optional ;
    message, hide when innerMessage == null , is innerMessage;
    button "Feedback" : button, on_click feedbackAction, feedback;
    button2 "Feedback Exception" : button, on_click feedbackException, feedback;
    asd "simple btton" : button, on_click simpleClick;
    combo "Combo" : Options;
}

enum FeedbackMessages {
    STARTING: "Starting...";
    PROGRESS: "Progess... %s %%";
    CANCELED: "Execution canceled!";
    COMPLETED: "Execution completed!";
    EXCEPTION: "Feedback interrupted by ME (RuntimeException) ";
}

form RatingForm {
    text1 "Text 1" : String, hint "Str hint";
    stars "Stars" : Int, rating, optional, default 4, tooltip "asd", hint "4";
    hearts "Hearts" : Int, rating(hearts), on_change sync, tooltip "asd", disable when stars == 2, hide when stars == 3;
    text2 "Text 2" : String, on_change sync, disable when stars == 2;
    but "Validate" : button(validate), on_click sync;
}

form TabsForm "Tabs form" {
    header {
        message(title);
    };

    hideFirst  : Boolean;
    hideSecond : Boolean;

    tabs "Tabs" : tabs {
        "First" : horizontal, hide when hideFirst {
            "Message A" : message, is "Mensaje";
            "Message B" : message, is "Mensaje";
        };
        "Second" : horizontal, hide when hideSecond {
            "Message C" : message, is "Mensaje";
            "Message D" : message, is "Mensaje";
        };
        third : horizontal, label_expression str {
            str : String;
        };
    };
}

entity Node
    described_by name
    searchable
{
    name : String;
    parent : Node, optional;
    children : Node*;
}

form DialogsForm "Dialogs Form" on_load load {

    open "Open" : button, on_click openDialog;
    diag "Dialog" : dialog, display "Open me" {
        text1  "Text1" : String(20), optional;
        text2 "Text2" : String(20), optional;
    };
    foc "Focus next text": button, on_click focusText;
    foc2 "This has on change" : text_field, on_change putText4;
    text3 "Text3" : String;
    text4 "Text3" : String;

    some "Dialog!" : dialog, display "Open Me" {
        text "Text" : String(20), optional;
        footer {
            confirm "Confirm" : button, on_click confirm, confirm "Estas seguro?";
            "Open Other dialog" : button, on_click showOtherDialog;
            "Close" : button, content_style "btn-danger", on_click hideDialog;
        };
    };

    other "Other Dialog!" : dialog, on_ui_change goBackToPrevious {
        otherText "Other" : String(20), optional;
        footer {
            "Open table 0" : button, on_click openTableZero;
            "Open table 1" : button, on_click openTableOne;
            "Open Section 0" : button, on_click openSectionZero;
            "Open Section 1" : button, on_click openSectionOne;
            "Close dialog" : button, content_style "btn-danger", on_click goBackToPrevious;
        };
    };

    table : table {
        tableDialog "Table dialog" : dialog, display "I'm in a table" {
            tableText "Table": String(10), optional;
            "Close Dialog": button, on_click goBackToOther;
        };
    };

    section : section {
        sectionDialog "section dialog" : dialog, display "I'm in a section" {
            sectionText "section": String(10), optional;
            "Close Dialog": button, on_click goBackToOther;
        };
    };
}

form InsideScreenForm "Inside Screen Form" {

    header {
        message(title);
    };


    "Bottom to left" : popover(bottom), icon android, content_style "btn btn-default" {
        header{
            "Hola": label;
        };
        String(20);
        String(20);
    };
    tab1 "First Table": table {
        fst: String;
        snd: String;
        tth: String;
        fth: String;
        ffth: dropdown, split {
            l1 "Label 1": label;
            l2 "Label 2": label;
            l3 "Label 3": label;
        };
    };
	horizontal {
		button(add_row, tab1);
		button(remove_row, tab1);
	};
    tab2 "Second Table": table {
        fst2: String;
        snd2: String;
        tth2: String;
        fth2: String;

        "Bottom to right" : popover(bottom), icon android, style "pull-right", content_style "btn btn-default" {
            header{
                "Hola": label;
            };
            String(20);
            String(20);
        };
    };
	horizontal {
		button(add_row, tab2);
		button(remove_row, tab2);
	};
    some "Dialog!" : dialog, display "Open Me" {
        "Text" : String(20);
        "ComboBox": Options, combo_box , hint "This is a hint";
        horizontal {
            String(20);
            String(20);

            "Bottom" : popover(bottom), icon android, style "pull-right", content_style "btn btn-default" {
                header{
                    "Hola": label;
                };
                String(20);
                String(20);
            };
        };
        horizontal {
            String(20);
            String(20);

            "Left" : popover(left), icon android, style "pull-right", content_style "btn btn-default" {
                header{
                    "Hola": label;
                };
                String(20);
                String(20);
            };
        };

        "Right" : popover(right), icon android, content_style "btn btn-default" {
            header{
                "Hola": label;
            };
            String(20);
            String(20);
        };

        "Top" : popover(top), icon android, content_style "btn btn-default" {
            header{
                "Hola": label;
            };
            String(20);
            String(20);
        };

        "Bottom" : popover(bottom), icon android, content_style "btn btn-default" {
            header{
                "Hola": label;
            };
            String(20);
            String(20);
        };

        tabs {
            "hor" : horizontal {
                String(20);
                String(20);
            };
          "vert" : vertical {
                String(20);
                String(20);
                String(20);
                String(20);
                String(20);
            };
        };

        footer {
            "Button2" : button;
            "Button3" : button, content_style "btn-danger";
        };


    };
}

form DropdownForm {
    a "A": Boolean;
    b "B": Boolean;

    ddown "Dropdown": dropdown, split {
        first: label, on_click firstClick, hide when b;
        second: label, on_click firstClick, hide when a;
    };

    vertical , disable when b {
		table: table {
	        name "Name": String;
	        lastname "Lastname": String;
	        "Horizontal" : horizontal {
				available: Boolean, default false;
		        nr1 "Nr1": button, hide when available, disable when false, on_click any;
		        drop: dropdown, hide when !available, disable when false, split {
		            l1 "L1": label, on_click any, hide when a;
		            l2 "L2": label, on_click any, hide when b;
		        };
			};
	    };
	};

    button(add_row);
    button(remove_row);
}

entity Image described_by name searchable image sec {
    name: String;
    resource : Resource;
    sec: Resource, abstract;
}

form ReadOnlyForm {

    check : Boolean, default true;
    input : String, disable when check;
    str   : String, is isReadOnly() ? "Read only" : "No read only";

    toggle "Click me": button, on_click readOnly;
    sync "Other sync": button, on_click otherSync;

    cancel: button(cancel), on_click readOnly;
}

form ImageVariantForm entity Image {

    header {
        message(entity), col 8;
        search: search_box, col 4, style "pull-right";
    };

    id, internal, optional;
    name;
    resource, upload, on_ui_change copyImage;

    vertical, hide when !isUpdate(), col 4 {
        fillButt "Fill gallery": button, on_click fillGallery;
        showcase "Showcase": showcase;
        showcase1 "Showcase with custom variant": showcase, custom_variant "jlongo";
        gal "Gallery" : gallery, custom_variant "jlongo";
    };

    footer {
        button(save);
        button(cancel);
        button(delete), style "pull-right";
    };
}

form UploadForm on_load cropConfig {
    header {
        message(title);
        "View Source" : label, link "/sg/view/source/tekgenesis.showcase.UploadForm", style "pull-right";
    };

    camera "Camera": upload, camera;
    up "Upload" : upload, multiple;
    showcase "Showcase": showcase;
    butt "Reset me": button, on_click resetMe;
    fillButt "Fill gallery": button, on_click fillGallery;
    gal "Gallery" : gallery;
}

form NodeForm "NodeForm" entity Node {

    header {
        message(title);
        "View Source" : label, link "/sg/view/source/tekgenesis.showcase.NodeForm", style "pull-right";
    };
    id, internal, optional;
    name;
    parent;

    footer {
        button(save);
    };
}

form FilterNodeForm {
    header {
        message(title);
    };

    filter1 "Node filter 1" : String, default "One";
    filter2 "Node filter 2" : String, default "Two";

    box "Search for nodes": Node, filter (name = (filter1, filter2));
    boxString: String, internal, is box;
    box2 "Search for more nodes": Node, filter (name = boxString);
}

form TitleEntityForm entity NamedItem {

    header {
        title: message(entity), col 8, icon_expr isUpdate() ? "play" : "pause";
        search: search_box, col 4, style "pull-right";
    };

    "Id"    : idKey, internal;
    name "Name"  : name;
    color "Color" : color;

    footer {
        submit "Submit": button, on_click saveAndRedirect;
        button(cancel);
        button(delete), style "pull-right";
    };
}

form IconShowcaseForm "Icon Showcase" {

    header {
        title : message(title), col 8, icon_expr change ? "play": "pause";
    };

    change: Boolean, internal, default true;

    vertical "Vertical with label and icon" : vertical, icon apple {
        m1 "Info" : message(info), is "My info msg with <b>icon expression</b>", icon_expr change ? "exclamation-triangle" : "money";
        message(warning), is "My warning message with <b>icon exclamation!</b>", icon exclamation_triangle;
    };

    horizontal "Horizontal with label and icon expr": horizontal, icon_expr change ? "android" : "apple" {
        message(info), is "My info msg with <b>icon expression</b>", icon_expr change ? "exclamation-triangle" : "money";
        m2 "Warning" : message(warning), is "My warning message with <b>icon exclamation!</b>", icon exclamation_triangle;
    };

    horizontal, icon android, label_expression change ? "Horizontal with icon and label expression without label" : "ChangedText!" {
        m3 "Info" : message(info), is change ? "My info msg with icon expression <i>exclamation-triangle</i>" :
                                               "My info msg with icon expression <i>money</i>", icon_expr change ? "exclamation-triangle" : "money";
        message(warning), is "My warning message with icon crosshairs!", icon crosshairs;
    };

    vertical, icon_expr change ? "android" : "apple" {
        message(info), is "My info msg with <b>icon expression</b>", icon_expr change ? "exclamation-triangle" : "money";
        pepe "Default": message(warning), default "My warning message with <b>icon exclamation!</b>", icon dropbox,
                        label_expression change ? "Default label is overriten by label_expression" : "Changed text by label_expression!";
    };

    plainHorizontal "Horizontal with message plain!": horizontal, icon_expr change ? "android" : "apple" {
        message(plain), is "My plain msg with <b>icon expression</b>", icon_expr change ? "exclamation-triangle" : "money";
        defaultPlain "Default": message(plain), default "My plain message with <b>icon dropbox!</b>", icon dropbox,
                        label_expression change ? "Default label is overriten by label_expression" : "Changed text by label_expression!";
    };

    icons "More icons" : horizontal {

        icons1 "Icon Expresions" : vertical, icon check_circle {
            but "Button" : button, icon_expr change ? "play" : "stop", style "form-align" ;
            tf1 "TextField" : String(10), icon_expr change ? "check-square-o" : "square-o";
            label, icon_expr change ? "star" : "star-o", style "form-align";
            display, icon_expr change ? "caret-square-o-down" : "caret-square-o-up";
        };

        icons2 "Icon Enums" : vertical, icon check_circle_o {
            tf "TextField" : String(10), icon windows;
            db "DateBox" : date_box, from "2012-07-09", to "2012-08-09", icon calendar;
            bu "Button" : button, icon youtube, style "form-align";
            tb "Toggle Button" : toggle_button, icon android, icon_selected apple, style "form-align";
            horizontal, style "form-align" {
                label, icon smile_o, style "margin-right-5";
                label, icon scissors, style "margin-right-5";
                label, icon shopping_cart, style "margin-right-5";
                label, icon spinner, style "margin-right-5";
            };
        };
    };

    changeMe "Change expressions": button, on_click changeAll;
}

form SampleIcon {
    textfield "TextField" : String(20), text_field,  placeholder "This is a placeholder..", hint "This is a hint", on_ui_change shortMessage;
    tfTooltip "Field with tooltip" : String(20), text_field, placeholder "This is a placeholder .. ",  tooltip "This is a tooltip for mail fields", on_ui_change longMessage;
    iconField "Field with Icon": String(20), text_field, placeholder "This is a placeholder..",  icon calendar,  hint "This is a hint";
    complete "Field complete": String(20), text_field, placeholder "This is a placeholder..",  icon calendar,  hint "This is a hint",  tooltip "This is a tooltip for mail fields";
    mailFiled "MailField" : mail_field, hint "This is a Mail Field", tooltip "This is a tooltip for mail fields";
    date "Date" : date_box, tooltip "This is a tooltip for mail fields";
}

enum Options
{
    OPTION1     : "Option1";
    OPTION2     : "Option2";
    OPTION3     : "Option3";
    OPTION4     : "Option4";
    OPTION5     : "Option5";
    OPTION6     : "Option6";
}
