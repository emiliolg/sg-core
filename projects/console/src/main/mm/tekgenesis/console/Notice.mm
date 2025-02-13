package tekgenesis.console;

import tekgenesis.admin.notice.Advice;


form AdviceForm "Notices"
{
    header {
        message(title), col 12;
    };

    horizontal { dismissAll "Dismiss All" : button, on_click dismissAll;
        horizontal {ss "Show Dismissed?" : label, style "marginLeft10";
            horizontal,style showDismissed ? "marginLeft10 alignleft switch-container switch-container-green" : "marginLeft10 alignleft switch-container switch-container-gray"
            {
                horizontal, style showDismissed ?  "switch switch-right" : "switch switch-left"  { };
                showDismissed :toggle_button, style "hidden-but", on_ui_change refresh;
                showDismissedSwitch :display, is showDismissed ? "Yes" : "No" ,style "switch-text";
            };
        };
        filter : text_field, optional;
        filterButton "Filter"  : button, on_click refresh;
        clearButton "Clear"  : button, on_click clearFilter, disable  when (filter == null || trim(filter) == "");
        timeline : SincePeriod , default LAST_24, on_change refresh;

    };

    notices    : Advice, table(20), sortable, lazy_fetch {
        "Id"             : id, internal, optional;
        levelIcon        : internal;
        tooltip          : internal;
        "Description"    : description, display;
        "Type"           : type, display;
        level "Level"    : horizontal {label, icon_expr levelIcon, tooltip tooltip;};
        dismiss "Dismiss": check_box, on_ui_change dismiss;
        "Created"        : creationTime;
    };
}

enum SincePeriod {
    LAST_24 : "Last 24 hours";
    LAST_WEEK: "Last Week";
    LAST_MONTH: "Last Month";
    LAST_YEAR: "Last Year";
    ANYTIME: "Any Time";
}





